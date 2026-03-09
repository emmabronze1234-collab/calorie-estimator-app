# AI Food Calorie Estimator

A Java application that uses a Convolutional Neural Network (CNN) to estimate calories from food images.

## Features
- **Image Selection**: Upload food images via GUI
- **CNN-Based Prediction**: Custom CNN implementation with:
    - Convolutional layer (32 filters)
    - ReLU activation
    - Max pooling
    - Fully connected layer
- **Food Database**: Built-in calorie database for reference
- **Swing GUI**: User-friendly interface

## How It Works
1. User selects a food image
2. Image is preprocessed (resize to 224x224, normalize)
3. CNN processes the image through:
    - Convolution → ReLU → Pooling
    - Flatten → Fully connected → Output
4. Calorie estimate is displayed

## Project Structure
- `Main.java` - GUI and application entry point
- `CaloriePredictor.java` - Main prediction logic
- `CNNModel.java` - Neural network implementation
- `ImageProcessor.java` - Image preprocessing
- `FoodDataset.java` - Food calorie database

## Technologies Used
- Java
- Swing (GUI)
- Custom CNN implementation (no external ML libraries)

## Future Improvements
- Train model on actual food datasets
- Add more convolutional layers
- Implement food classification before calorie estimation
- Support batch processing