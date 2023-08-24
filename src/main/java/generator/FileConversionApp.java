package generator;


import javax.swing.*;
import java.awt.*;
import java.io.*;

public class FileConversionApp extends JFrame {
    private JTextField inputFileField;
    private JTextField outputFileField;
    private JTextField additionalInfoField;
    private JButton convertButton;
    private JButton closeButton;

    public FileConversionApp() {
        setTitle("File Conversion App");
        setSize(600, 250); // Adjusted height for the new field
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);

        JLabel inputFileLabel = new JLabel("Select Input File:");
        inputFileField = new JTextField(20);
        JButton inputFileBrowseButton = new JButton("Browse...");
        inputFileBrowseButton.addActionListener(e -> chooseInputFile());

        JLabel outputFileLabel = new JLabel("Select Output Path:");
        outputFileField = new JTextField(20);
        JButton outputFileBrowseButton = new JButton("Browse...");
        outputFileBrowseButton.addActionListener(e -> chooseOutputDirectory());

        JLabel additionalInfoLabel = new JLabel("Pictures name prefix:");
        additionalInfoField = new JTextField(20);

        convertButton = new JButton("Convert");
        convertButton.addActionListener(e -> performConversion());

        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose()); // Close the application

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(inputFileLabel, gbc);
        gbc.gridx = 1;
        panel.add(inputFileField, gbc);
        gbc.gridx = 2;
        panel.add(inputFileBrowseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(outputFileLabel, gbc);
        gbc.gridx = 1;
        panel.add(outputFileField, gbc);
        gbc.gridx = 2;
        panel.add(outputFileBrowseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(additionalInfoLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2; // Make the additional info field span 2 columns
        panel.add(additionalInfoField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(convertButton);
        buttonPanel.add(closeButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void chooseInputFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            inputFileField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void chooseOutputDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            outputFileField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void performConversion() {
        String picName = additionalInfoField.getText();
        String filename = inputFileField.getText();
        String outputPath = outputFileField.getText();
        System.out.println(picName + "\n" + filename + "\n" + outputPath);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            String line;
            int counter = 1; // Счетчик для имен файлов

            String title = "";
            StringBuilder description = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) { // Пустая строка - конец блока
                    createHtmlFile(title, description.toString(), picName, outputPath);
                    counter++;
                    title = "";
                    description = new StringBuilder();
                } else if (title.isEmpty()) {
                    title = line;
                } else {
                    description.append(line).append(" ");
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void createHtmlFile( String title, String description, String picName, String filePath) {
        String fileName = title.replaceAll("\\\\", "").replaceAll("/", "") + ".html";
        String prefix = "Описание смартфона ";

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath+"/"+title.replaceAll("\\\\", "").replaceAll("/", "") + ".html"), "UTF-8"))) {
            writer.write("<h2 style=\"text-align:center;\">" + prefix  + title.replace("Смартфон ", "") + "</h2>\n");

            String[] descriptionParts = TextSplitter.splitText(description);
            int picNo = 1;
            for (String part : descriptionParts) {
                writer.write("<p>" + part + "</p>\n");
                writer.write("<p style=\"text-align:center;\">\n");
                writer.write("<img alt=\"" +
                        extractFirstSentence(part) +
                        "\" src=\"" + picName + picNo+ ".jpg\" style=\"max-width: 100%;width: 800px;\" title=\""+
                        extractFirstSentence(part) +"\">\n");
                writer.write("</p>\n");
                picNo ++;
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while creating the HTML file: " + e.getMessage());
        }
    }

    private static String[] splitDescription(String description) {
        // Разбиваем описание на 4 части
        int partLength = (int) Math.ceil(description.length() / 4.0);
        String[] parts = new String[4];

        for (int i = 0; i < 4; i++) {
            int start = i * partLength;
            int end = Math.min((i + 1) * partLength, description.length());
            parts[i] = description.substring(start, end);
        }

        return parts;
    }

    public static String extractFirstSentence(String text) {
        int endIndex = text.indexOf('.');
        if (endIndex != -1) {
            return text.substring(0, endIndex + 1).trim();
        } else {
            return text;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileConversionApp app = new FileConversionApp();
            app.setVisible(true);
        });
    }
}
