package com.emmanuelanuforo.calorieapp;// src/com.emmanuelanuforo.calorieapp.CaloriePredictor.java
import java.io.File;

public class CaloriePredictor {
    private CNNModel model;
    private FoodDataset foodDataset;

    public CaloriePredictor() {
        this.model = new CNNModel();
        this.foodDataset = new FoodDataset();
    }

    public double predictCalories(File imageFile) throws Exception {
        // Preprocess image
        double[][][] processedImage = ImageProcessor.preprocessImage(imageFile);

        // Get prediction from CNN model
        double rawPrediction = model.predict(processedImage);

        // Apply food-specific adjustments based on dataset knowledge
        double adjustedPrediction = adjustPrediction(rawPrediction, processedImage);

        return adjustedPrediction;
    }

    private double adjustPrediction(double rawPrediction, double[][][] image) {
        // Simple heuristic adjustments
        // In a real implementation, this would be based on trained classifiers
        double averageColor = calculateAverageColor(image);
        double colorVariance = calculateColorVariance(image);

        // Adjust based on color characteristics (crude food type detection)
        if (averageColor > 0.6) {
            // Lighter foods (rice, bread, etc.) - adjust calories
            rawPrediction *= 0.8;
        } else if (averageColor < 0.4) {
            // Darker foods (chocolate, meat, etc.) - adjust calories
            rawPrediction *= 1.2;
        }

        // Adjust based on color variance (mixed foods vs uniform)
        if (colorVariance > 0.1) {
            // High variance - likely mixed food, adjust accordingly
            rawPrediction *= 1.1;
        }

        return Math.max(50, Math.min(1500, rawPrediction)); // Reasonable calorie range
    }

    private double calculateAverageColor(double[][][] image) {
        double sum = 0.0;
        int count = 0;

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                for (int c = 0; c < image[i][j].length; c++) {
                    sum += image[i][j][c];
                    count++;
                }
            }
        }

        return sum / count;
    }

    private double calculateColorVariance(double[][][] image) {
        double mean = calculateAverageColor(image);
        double sumSquaredDiff = 0.0;
        int count = 0;

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                for (int c = 0; c < image[i][j].length; c++) {
                    double diff = image[i][j][c] - mean;
                    sumSquaredDiff += diff * diff;
                    count++;
                }
            }
        }

        return Math.sqrt(sumSquaredDiff / count);
    }
}