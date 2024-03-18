package generator;


import javax.swing.*;
import java.awt.*;
import java.io.*;

public class FileConversionApp extends JFrame {
    private JTextField inputUrlField;
    private JTextField outputFileField;
    private JTextField additionalInfoField;
    private JButton convertButton;
    private JButton closeButton;
    private JTextArea progressTextArea;
    private JTextField copyMonkeyLoginField; // Добавлено поле для ввода логина CopyMonkey
    private JPasswordField copyMonkeyPasswordField; // Добавлено поле для ввода пароля CopyMonkey


    public FileConversionApp() {
        setTitle("File Conversion App");
        setSize(950, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);

        JLabel inputFileLabel = new JLabel("Select Input URL:");
        inputUrlField = new JTextField(20);

        JLabel outputFileLabel = new JLabel("Select Output Path:");
        outputFileField = new JTextField(20);

        JLabel additionalInfoLabel = new JLabel("Pictures name prefix:");
        additionalInfoField = new JTextField(20);

        JLabel copyMonkeyLoginLabel = new JLabel("CopyMonkey Login:"); // Метка для поля логина
        copyMonkeyLoginField = new JTextField(15); // Поле для ввода логина

        JLabel copyMonkeyPasswordLabel = new JLabel("CopyMonkey Password:"); // Метка для поля пароля
        copyMonkeyPasswordField = new JPasswordField(15); // Поле для ввода пароля

        convertButton = new JButton("Convert");
        convertButton.addActionListener(e -> performConversion());

        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        progressTextArea = new JTextArea(20, 65);
        progressTextArea.setEditable(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(inputFileLabel, gbc);
        gbc.gridx = 1;
        panel.add(inputUrlField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(outputFileLabel, gbc);
        gbc.gridx = 1;
        panel.add(outputFileField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        panel.add(copyMonkeyLoginLabel, gbc); // Добавляем метку для логина
        gbc.gridx = 3;
        panel.add(copyMonkeyLoginField, gbc); // Добавляем поле для ввода логина

        gbc.gridx = 2;
        gbc.gridy = 1;
        panel.add(copyMonkeyPasswordLabel, gbc); // Добавляем метку для пароля
        gbc.gridx = 3;
        panel.add(copyMonkeyPasswordField, gbc); // Добавляем поле для ввода пароля

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(additionalInfoLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(additionalInfoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        panel.add(progressTextArea, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(convertButton);
        buttonPanel.add(closeButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void performConversion() {
        // Переменные для данных из полей формы
        String picName = additionalInfoField.getText();
        String filename = inputUrlField.getText();
        String outputPath = outputFileField.getText();

        // Очистка области прогресса перед новым запуском
        progressTextArea.setText("");

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
            progressTextArea.append("An error occurred: " + e.getMessage() + "\n");
        }
    }

    // Метод для добавления текста в область прогресса
    private void updateProgress(String text) {
        progressTextArea.append(text + "\n");
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
