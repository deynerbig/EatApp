package com.example.eatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Publicar_Receta extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener {

    private RecyclerView recipesRecyclerView;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private FirebaseFirestore db;
    private Button addRecipeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Habilitamos EdgeToEdge como en Publicar_Receta
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_publicar_receta);

        // Ajuste de insets como en Publicar_Receta
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recipesRecyclerView = findViewById(R.id.recipesRecyclerView);
        addRecipeButton = findViewById(R.id.addRecipeButton);

        db = FirebaseFirestore.getInstance();
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(recipeList, this);

        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipesRecyclerView.setAdapter(recipeAdapter);

        loadRecipes();

        // Botón para añadir receta
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Publicar_Receta.this, Historial.class));
            }
        });
    }

    private void loadRecipes() {
        db.collection("recipes")
                .whereEqualTo("public", true) // Filtro para recetas públicas
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Manejar el error
                        return;
                    }

                    recipeList.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        Recipe recipe = doc.toObject(Recipe.class);
                        recipe.setId(doc.getId());
                        recipeList.add(recipe);
                    }
                    recipeAdapter.notifyDataSetChanged();
                });
    }

    // Método para compartir receta públicamente
    private void shareRecipePublicly(String recipeId) {
        db.collection("recipes")
                .document(recipeId)  // Usamos el ID de la receta seleccionada
                .update("public", true)  // Marcar la receta como pública
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Publicar_Receta.this, "Receta publicada", Toast.LENGTH_SHORT).show();
                    loadRecipes();  // Recargar la lista de recetas públicas
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Publicar_Receta.this, "Error al publicar la receta", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        // Aquí pasamos el ID de la receta seleccionada al método para hacerla pública
        shareRecipePublicly(recipe.getId());  // Pasar el ID de la receta seleccionada
    }
}