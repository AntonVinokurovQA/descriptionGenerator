package generator;


import data.PhoneInfo;
import web.CopyMonkey;
import web.Shop;

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
        setTitle("Description generator");
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
        convertButton.addActionListener(e -> {
            try {
                performConversion();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        progressTextArea = new JTextArea(20, 65);
        progressTextArea.setEditable(false);
        progressTextArea.setLineWrap(true);
        progressTextArea.setMargin(new Insets(2, 2, 2, 2)); // Установка отступов


        JScrollPane scrollPane = new JScrollPane(progressTextArea); // Оборачиваем JTextArea в JScrollPane
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Включаем вертикальную полосу прокрутки


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
        gbc.fill = GridBagConstraints.BOTH; // Растягиваем JScrollPane по вертикали и горизонтали
        panel.add(scrollPane, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(convertButton);
        buttonPanel.add(closeButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    void performConversion() throws IOException {
        String picName = additionalInfoField.getText();
        String inputUrl = inputUrlField.getText();
        String outputPath = outputFileField.getText();
        String outputFile = outputFileField.getText()+"\\info.txt";
        String login = copyMonkeyLoginField.getText();
        String password = copyMonkeyPasswordField.getText();

        progressTextArea.setText("");

        updateProgress("Running ...");

        Shop shop = new Shop(inputUrl);
        CopyMonkey copyMonkey = new CopyMonkey(login, password);

        String[] modelsUrl = shop.getModelsUrl();

        SwingUtilities.invokeLater(() -> updateProgress("Model's URL: " + shop.getModelsListUrl()));
        SwingUtilities.invokeLater(() -> updateProgress(". . ."));

        SwingUtilities.invokeLater(() -> updateProgress(modelsUrl.length + " phones have been found"));
        SwingUtilities.invokeLater(() -> updateProgress(". . ."));
        SwingUtilities.invokeLater(() -> updateProgress("Phone's URLs:"));

        for (String url : modelsUrl) {
            SwingUtilities.invokeLater(() -> updateProgress(url));
        }

        SwingUtilities.invokeLater(() -> updateProgress(". . ."));

        PhoneInfo[] phoneInfo = shop.getListOfDetails(modelsUrl);
        SwingUtilities.invokeLater(() -> updateProgress("Phones info:"));
        for (PhoneInfo info : phoneInfo) {
            SwingUtilities.invokeLater(() -> updateProgress("title: " + info.getTitle()));
            SwingUtilities.invokeLater(() -> updateProgress("characteristics: " + info.getCharacteristics()));
            SwingUtilities.invokeLater(() -> updateProgress(" "));
        }

        SwingUtilities.invokeLater(() -> updateProgress(". . ."));

        String[] descriptions = new String[phoneInfo.length];
        SwingUtilities.invokeLater(() -> updateProgress("Running copymonkey ..."));

        copyMonkey.login();
        copyMonkey.goToProductDescription();

        for (int i = 0; i < phoneInfo.length; i++) {
            descriptions[i] = copyMonkey.generateDescription(phoneInfo[i].getTitle(), phoneInfo[i].getCharacteristics());

            int finalI = i;
            SwingUtilities.invokeLater(() -> updateProgress(phoneInfo[finalI].getTitle()));
            writeInfoFile(outputFile, phoneInfo[i].getTitle());

            SwingUtilities.invokeLater(() -> updateProgress(descriptions[finalI]));
            writeInfoFile(outputFile, descriptions[i]);

            SwingUtilities.invokeLater(() -> updateProgress(""));
            writeInfoFile(outputFile, "");
        }

        writeInfoFile(outputFile, "final");
        SwingUtilities.invokeLater(() -> updateProgress("File succesfully created . . ."));

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(outputFile), "UTF-8"));
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
            SwingUtilities.invokeLater(() -> updateProgress("HTML Files generated in " + outputPath));
        } catch (IOException e) {
            progressTextArea.append("\nAn error occurred: " + e.getMessage() + "\n");
        }
    }


    private void updateProgress(String text) {
        progressTextArea.append(text + "\n");
        progressTextArea.repaint();
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

    public static String extractFirstSentence(String text) {
        int endIndex = text.indexOf('.');
        if (endIndex != -1) {
            return text.substring(0, endIndex + 1).trim();
        } else {
            return text;
        }
    }

    private void writeInfoFile(String fileName, String text) throws IOException {
        File infoFile = new File(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(infoFile, true))) {
            if (!infoFile.exists()) {
                infoFile.createNewFile();
            }
            writer.write(text);
            writer.newLine();
        } catch (Exception e){
            SwingUtilities.invokeLater(() -> updateProgress("File error: " + e.getMessage()));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileConversionApp app = new FileConversionApp();
            app.setVisible(true);
        });
    }
}
