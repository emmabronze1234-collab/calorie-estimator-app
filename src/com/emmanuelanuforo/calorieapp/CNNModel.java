package com.emmanuelanuforo.calorieapp;// src/com.emmanuelanuforo.calorieapp.CNNModel.java
import java.util.Random;

public class CNNModel {
    private double[][][][] conv1Weights;
    private double[] conv1Biases;
    private double[][] fcWeights;
    private double[] fcBiases;
    private Random random;

    // CNN Architecture parameters
    private static final int INPUT_HEIGHT = 224;
    private static final int INPUT_WIDTH = 224;
    private static final int INPUT_CHANNELS = 3;
    private static final int CONV1_FILTERS = 32;
    private static final int CONV1_FILTER_SIZE = 3;
    private static final int POOL_SIZE = 2;
    private static final int FC_UNITS = 128;

    public CNNModel() {
        this.random = new Random(42); // Fixed seed for reproducibility
        initializeWeights();
    }

    private void initializeWeights() {
        // Convolutional layer 1 weights
        conv1Weights = new double[CONV1_FILTERS][CONV1_FILTER_SIZE][CONV1_FILTER_SIZE][INPUT_CHANNELS];
        conv1Biases = new double[CONV1_FILTERS];

        initializeConvWeights(conv1Weights, conv1Biases, CONV1_FILTER_SIZE * CONV1_FILTER_SIZE * INPUT_CHANNELS);

        // Calculate dimensions after conv and pooling
        int conv1Height = INPUT_HEIGHT - CONV1_FILTER_SIZE + 1;
        int conv1Width = INPUT_WIDTH - CONV1_FILTER_SIZE + 1;
        int pool1Height = conv1Height / POOL_SIZE;
        int pool1Width = conv1Width / POOL_SIZE;
        int fcInputSize = pool1Height * pool1Width * CONV1_FILTERS;

        // Fully connected layer weights
        fcWeights = new double[FC_UNITS][fcInputSize];
        fcBiases = new double[FC_UNITS];

        initializeFCWeights(fcWeights, fcBiases, fcInputSize);
    }

    private void initializeConvWeights(double[][][][] weights, double[] biases, int fanIn) {
        double scale = Math.sqrt(2.0 / fanIn); // He initialization
        for (int f = 0; f < weights.length; f++) {
            for (int i = 0; i < weights[f].length; i++) {
                for (int j = 0; j < weights[f][i].length; j++) {
                    for (int k = 0; k < weights[f][i][j].length; k++) {
                        weights[f][i][j][k] = random.nextGaussian() * scale;
                    }
                }
            }
            biases[f] = 0.1;
        }
    }

    private void initializeFCWeights(double[][] weights, double[] biases, int fanIn) {
        double scale = Math.sqrt(2.0 / fanIn);
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = random.nextGaussian() * scale;
            }
            biases[i] = 0.1;
        }
    }

    public double predict(double[][][] input) {
        // Convolutional layer 1
        double[][][] conv1Output = convolutionalLayer(input, conv1Weights, conv1Biases, 1);

        // ReLU activation
        double[][][] relu1Output = relu(conv1Output);

        // Max pooling
        double[][][] pool1Output = maxPooling(relu1Output, POOL_SIZE);

        // Flatten
        double[] flattened = flatten(pool1Output);

        // Fully connected layer
        double[] fcOutput = fullyConnectedLayer(flattened, fcWeights, fcBiases);

        // Output layer (single neuron for regression)
        double prediction = outputLayer(fcOutput);

        return Math.max(0, prediction); // Ensure non-negative calories
    }

    private double[][][] convolutionalLayer(double[][][] input, double[][][][] weights, double[] biases, int stride) {
        int inputHeight = input.length;
        int inputWidth = input[0].length;
        int inputChannels = input[0][0].length;
        int numFilters = weights.length;
        int filterSize = weights[0].length;

        int outputHeight = (inputHeight - filterSize) / stride + 1;
        int outputWidth = (inputWidth - filterSize) / stride + 1;

        double[][][] output = new double[outputHeight][outputWidth][numFilters];

        for (int f = 0; f < numFilters; f++) {
            for (int i = 0; i < outputHeight; i++) {
                for (int j = 0; j < outputWidth; j++) {
                    double sum = 0.0;
                    for (int ki = 0; ki < filterSize; ki++) {
                        for (int kj = 0; kj < filterSize; kj++) {
                            for (int c = 0; c < inputChannels; c++) {
                                int inputI = i * stride + ki;
                                int inputJ = j * stride + kj;
                                sum += input[inputI][inputJ][c] * weights[f][ki][kj][c];
                            }
                        }
                    }
                    output[i][j][f] = sum + biases[f];
                }
            }
        }
        return output;
    }

    private double[][][] relu(double[][][] input) {
        double[][][] output = new double[input.length][input[0].length][input[0][0].length];
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                for (int k = 0; k < input[i][j].length; k++) {
                    output[i][j][k] = Math.max(0, input[i][j][k]);
                }
            }
        }
        return output;
    }

    private double[][][] maxPooling(double[][][] input, int poolSize) {
        int inputHeight = input.length;
        int inputWidth = input[0].length;
        int inputChannels = input[0][0].length;

        int outputHeight = inputHeight / poolSize;
        int outputWidth = inputWidth / poolSize;

        double[][][] output = new double[outputHeight][outputWidth][inputChannels];

        for (int c = 0; c < inputChannels; c++) {
            for (int i = 0; i < outputHeight; i++) {
                for (int j = 0; j < outputWidth; j++) {
                    double maxVal = Double.NEGATIVE_INFINITY;
                    for (int pi = 0; pi < poolSize; pi++) {
                        for (int pj = 0; pj < poolSize; pj++) {
                            int inputI = i * poolSize + pi;
                            int inputJ = j * poolSize + pj;
                            if (inputI < inputHeight && inputJ < inputWidth) {
                                maxVal = Math.max(maxVal, input[inputI][inputJ][c]);
                            }
                        }
                    }
                    output[i][j][c] = maxVal;
                }
            }
        }
        return output;
    }

    private double[] flatten(double[][][] input) {
        int height = input.length;
        int width = input[0].length;
        int channels = input[0][0].length;

        double[] flattened = new double[height * width * channels];
        int index = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int c = 0; c < channels; c++) {
                    flattened[index++] = input[i][j][c];
                }
            }
        }
        return flattened;
    }

    private double[] fullyConnectedLayer(double[] input, double[][] weights, double[] biases) {
        double[] output = new double[weights.length];

        for (int i = 0; i < weights.length; i++) {
            double sum = 0.0;
            for (int j = 0; j < weights[i].length; j++) {
                sum += input[j] * weights[i][j];
            }
            output[i] = Math.max(0, sum + biases[i]); // ReLU activation
        }
        return output;
    }

    private double outputLayer(double[] input) {
        // Simple output layer - average of activated features
        // In a real implementation, you'd have trained weights here
        double sum = 0.0;
        for (double value : input) {
            sum += value;
        }
        return sum / input.length * 1000; // Scale factor for calorie estimation
    }
}