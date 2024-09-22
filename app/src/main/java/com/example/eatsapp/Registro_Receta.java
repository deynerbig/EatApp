package com.example.eatsapp;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registro_Receta extends AppCompatActivity {

    private LinearLayout ingredientsList;
    private EditText etRecipeName;
    private DatabaseReference database;  // Referencia a la base de datos de Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_receta);

        // Inicializar Firebase Realtime Database
        database = FirebaseDatabase.getInstance().getReference();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etRecipeName = findViewById(R.id.etRecipeName);
        ingredientsList = findViewById(R.id.ingredientsList);

        Button btnAddIngredient = findViewById(R.id.btnAddIngredient);
        Button btnSaveRecipe = findViewById(R.id.btnSaveRecipe);

        btnAddIngredient.setOnClickListener(v -> addIngredientInput());
        btnSaveRecipe.setOnClickListener(v -> saveRecipe());
    }

    private void addIngredientInput() {
        LinearLayout ingredientLayout = new LinearLayout(this);
        ingredientLayout.setOrientation(LinearLayout.HORIZONTAL);

        EditText etIngredient = new EditText(this);
        etIngredient.setHint("Ingrediente");
        etIngredient.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        EditText etQuantity = new EditText(this);
        etQuantity.setHint("Cantidad");
        etQuantity.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etQuantity.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        ingredientLayout.addView(etIngredient);
        ingredientLayout.addView(etQuantity);

        ingredientsList.addView(ingredientLayout);
    }

    private void saveRecipe() {
        String recipeName = etRecipeName.getText().toString();
        List<Map<String, String>> ingredients = new ArrayList<>();

        for (int i = 0; i < ingredientsList.getChildCount(); i++) {
            View view = ingredientsList.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout layout = (LinearLayout) view;
                String name = ((EditText) layout.getChildAt(0)).getText().toString();
                String quantity = ((EditText) layout.getChildAt(1)).getText().toString();
                if (!name.isEmpty() && !quantity.isEmpty()) {
                    // Crear un mapa para cada ingrediente
                    Map<String, String> ingredient = new HashMap<>();
                    ingredient.put("name", name);
                    ingredient.put("quantity", quantity);
                    ingredients.add(ingredient);
                }
            }
        }

        if (recipeName.isEmpty() || ingredients.isEmpty()) {
            Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un ID único para cada receta
        String recipeId = database.child("recipes").push().getKey();

        // Crear un objeto de receta
        Map<String, Object> recipe = new HashMap<>();
        recipe.put("name", recipeName);
        recipe.put("ingredients", ingredients);

        // Guardar la receta en Realtime Database
        if (recipeId != null) {
            database.child("recipes").child(recipeId).setValue(recipe)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Receta guardada: " + recipeName, Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al guardar la receta", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}