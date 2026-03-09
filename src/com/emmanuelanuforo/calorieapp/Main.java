package com.emmanuelanuforo.calorieapp;// src/com.emmanuelanuforo.calorieapp.Main.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Main extends JFrame {
    private JLabel imageLabel;
    private JLabel resultLabel;
    private JButton selectImageButton;
    private JButton predictButton;
    private File selectedImage;
    private CaloriePredictor caloriePredictor;

    public Main() {
        initializeUI();
        initializeModel();
    }

    private void initializeUI() {
        setTitle("Food Calorie Estimator - CNN");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // com.emmanuelanuforo.calorieapp.Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("AI Food Calorie Estimator", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 100, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Image display area
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createTitledBorder("Selected Food Image"));

        imageLabel = new JLabel("No image selected", JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(400, 300));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        selectImageButton = new JButton("Select Food Image");
        predictButton = new JButton("Estimate Calories");
        predictButton.setEnabled(false);

        selectImageButton.addActionListener(new SelectImageListener());
        predictButton.addActionListener(new PredictListener());

        buttonPanel.add(selectImageButton);
        buttonPanel.add(predictButton);

        imagePanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(imagePanel, BorderLayout.CENTER);

        // Results panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Calorie Estimation Results"));

        resultLabel = new JLabel("Please select an image and click 'Estimate Calories'", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        resultLabel.setForeground(Color.BLUE);

        resultPanel.add(resultLabel, BorderLayout.CENTER);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void initializeModel() {
        try {
            caloriePredictor = new CaloriePredictor();
            resultLabel.setText("Model loaded successfully!");
        } catch (Exception e) {
            resultLabel.setText("Error loading model: " + e.getMessage());
            predictButton.setEnabled(false);
        }
    }

    private class SelectImageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Image files", "jpg", "jpeg", "png", "bmp", "gif"));

            int result = fileChooser.showOpenDialog(Main.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedImage = fileChooser.getSelectedFile();

                // Display image
                ImageIcon imageIcon = new ImageIcon(selectedImage.getPath());
                Image image = imageIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(image));
                imageLabel.setText("");

                predictButton.setEnabled(true);
                resultLabel.setText("Image selected. Click 'Estimate Calories' to analyze.");
            }
        }
    }

    private class PredictListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedImage != null && caloriePredictor != null) {
                try {
                    double calories = caloriePredictor.predictCalories(selectedImage);
                    resultLabel.setText(String.format("Estimated Calories: %.1f kcal", calories));
                    resultLabel.setForeground(new Color(0, 100, 0));
                } catch (Exception ex) {
                    resultLabel.setText("Error during prediction: " + ex.getMessage());
                    resultLabel.setForeground(Color.RED);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}