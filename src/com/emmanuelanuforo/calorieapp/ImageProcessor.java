package com.emmanuelanuforo.calorieapp;// src/com.emmanuelanuforo.calorieapp.ImageProcessor.java
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {
    private static final int TARGET_WIDTH = 224;
    private static final int TARGET_HEIGHT = 224;

    public static double[][][] preprocessImage(File imageFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(imageFile);
        BufferedImage resizedImage = resizeImage(originalImage, TARGET_WIDTH, TARGET_HEIGHT);
        return convertToNormalizedArray(resizedImage);
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    private static double[][][] convertToNormalizedArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double[][][] array = new double[height][width][3]; // [height][width][RGB]

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // Normalize to [0, 1] range
                array[y][x][0] = r / 255.0; // Red
                array[y][x][1] = g / 255.0; // Green
                array[y][x][2] = b / 255.0; // Blue
            }
        }
        return array;
    }

    public static double[] flattenImageArray(double[][][] imageArray) {
        int height = imageArray.length;
        int width = imageArray[0].length;
        int channels = imageArray[0][0].length;

        double[] flattened = new double[height * width * channels];
        int index = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int c = 0; c < channels; c++) {
                    flattened[index++] = imageArray[y][x][c];
                }
            }
        }
        return flattened;
    }
}