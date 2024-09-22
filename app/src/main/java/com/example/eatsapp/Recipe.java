package com.example.eatsapp;

import java.util.List;
import java.util.Map;

public class Recipe {
    private String id;  // ID de Firebase
    private String name;  // Nombre de la receta
    private List<Map<String, String>> ingredients;  // Ingredientes y sus cantidades

    // Constructor vacío necesario para Firebase
    public Recipe() {
    }

    public Recipe(String name, List<Map<String, String>> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, String>> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Map<String, String>> ingredients) {
        this.ingredients = ingredients;
    }
}
