package com.emmanuelanuforo.calorieapp;// src/com.emmanuelanuforo.calorieapp.FoodDataset.java
import java.util.HashMap;

public class FoodDataset {
    private HashMap<String, Double> foodCalorieAverages;

    public FoodDataset() {
        initializeFoodDatabase();
    }

    private void initializeFoodDatabase() {
        foodCalorieAverages = new HashMap<>();

        // Average calories per 100g for common foods
        foodCalorieAverages.put("apple", 52.0);
        foodCalorieAverages.put("banana", 89.0);
        foodCalorieAverages.put("orange", 47.0);
        foodCalorieAverages.put("bread", 265.0);
        foodCalorieAverages.put("rice", 130.0);
        foodCalorieAverages.put("pasta", 131.0);
        foodCalorieAverages.put("chicken", 165.0);
        foodCalorieAverages.put("beef", 250.0);
        foodCalorieAverages.put("fish", 206.0);
        foodCalorieAverages.put("eggs", 155.0);
        foodCalorieAverages.put("milk", 42.0);
        foodCalorieAverages.put("cheese", 402.0);
        foodCalorieAverages.put("yogurt", 59.0);
        foodCalorieAverages.put("potato", 77.0);
        foodCalorieAverages.put("tomato", 18.0);
        foodCalorieAverages.put("carrot", 41.0);
        foodCalorieAverages.put("broccoli", 34.0);
        foodCalorieAverages.put("chocolate", 546.0);
        foodCalorieAverages.put("cake", 371.0);
        foodCalorieAverages.put("pizza", 266.0);
    }

    public double getAverageCalories(String foodName) {
        return foodCalorieAverages.getOrDefault(foodName.toLowerCase(), 200.0); // Default value
    }

    public boolean containsFood(String foodName) {
        return foodCalorieAverages.containsKey(foodName.toLowerCase());
    }
}