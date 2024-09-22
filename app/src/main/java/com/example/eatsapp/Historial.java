package com.example.eatsapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Historial extends AppCompatActivity {
    private RecyclerView recipeRecyclerView; // RecyclerView para mostrar las recetas
    private RecipeAdapter adapter; // Adaptador para el RecyclerView
    private List<Recipe> recipeList; // Lista de recetas
    private DatabaseReference databaseReference; // Referencia a Realtime Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial); // Establecer el layout de la actividad

        recipeRecyclerView = findViewById(R.id.recipeRecyclerView); // Inicializar RecyclerView
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // Establecer el LayoutManager

        recipeList = new ArrayList<>(); // Inicializar la lista de recetas
        adapter = new RecipeAdapter(recipeList, this::onRecipeClick); // Inicializar el adaptador
        recipeRecyclerView.setAdapter(adapter); // Establecer el adaptador en el RecyclerView

        databaseReference = FirebaseDatabase.getInstance().getReference("recipes"); // Referencia a la colección "recipes"
        loadRecipes(); // Cargar las recetas desde Firebase Realtime Database
    }

    // Método para cargar recetas
    private void loadRecipes() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recipeList.clear(); // Limpiar la lista antes de llenarla
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = snapshot.getValue(Recipe.class); // Convertir el DataSnapshot a un objeto Recipe
                    recipe.setId(snapshot.getKey()); // Establecer el ID de la receta
                    recipeList.add(recipe); // Agregar la receta a la lista
                }
                adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Manejar el error si ocurre
            }
        });
    }

    // Método que se llama al hacer clic en una receta
    private void onRecipeClick(Recipe recipe) {
        Historial_Detallado fragment = Historial_Detallado.newInstance(recipe.getId()); // Crear una nueva instancia del fragmento con el ID de la receta
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, fragment) // Reemplazar el fragmento actual
                .addToBackStack(null) // Agregar la transacción al back stack
                .commit(); // Confirmar la transacción
    }
}