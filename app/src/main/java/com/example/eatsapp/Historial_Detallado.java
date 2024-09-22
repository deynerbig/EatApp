package com.example.eatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import java.util.Map;

public class Historial_Detallado extends DialogFragment {
    private static final String ARG_RECIPE_ID = "recipeId"; // Constante para el argumento del ID
    private String recipeId; // Variable para almacenar el ID de la receta
    private TextView tvRecipeName; // TextView para mostrar el nombre de la receta
    private TextView tvIngredients; // TextView para mostrar los ingredientes
    private DatabaseReference databaseReference; // Referencia a Firebase Realtime Database

    // Método estático para crear una nueva instancia del fragmento
    public static Historial_Detallado newInstance(String recipeId) {
        Historial_Detallado fragment = new Historial_Detallado();
        Bundle args = new Bundle();
        args.putString(ARG_RECIPE_ID, recipeId); // Pasar el ID como argumento
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeId = getArguments().getString(ARG_RECIPE_ID); // Obtener el ID de los argumentos
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("recipes").child(recipeId); // Referencia a la receta en Realtime Database
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historial__detallado, container, false);
        tvRecipeName = view.findViewById(R.id.tvRecipeName); // Inicializar TextView para el nombre
        tvIngredients = view.findViewById(R.id.tvIngredients); // Inicializar TextView para ingredientes

        Button btnModify = view.findViewById(R.id.btnModify);
        Button btnDelete = view.findViewById(R.id.btnDelete);
        Button btnShare = view.findViewById(R.id.btnShare);

        loadRecipeDetails(); // Cargar detalles de la receta

        btnModify.setOnClickListener(v -> {
            // Implementar la lógica para modificar la receta
            // Puedes abrir una nueva actividad o fragmento para editar
        });

        // Lógica para el botón de eliminar
        btnDelete.setOnClickListener(v -> {
            deleteRecipe();
        });

        // Lógica para el botón de compartir
        btnShare.setOnClickListener(v -> {
            shareRecipe();
        });

        return view;
    }
    private void deleteRecipe() {
        databaseReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Receta eliminada", Toast.LENGTH_SHORT).show();
                dismiss(); // Cerrar el diálogo
            } else {
                Toast.makeText(getContext(), "Error al eliminar la receta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void shareRecipe() {
        // Primero, obtener la receta actual de la base de datos
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    if (recipe != null) {
                        // Crear la referencia a la colección pública
                        DatabaseReference publicRecipesRef = FirebaseDatabase.getInstance()
                                .getReference("publicRecipes").child(recipeId);

                        // Subir la receta a la colección pública
                        publicRecipesRef.setValue(recipe).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Receta publicada exitosamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Error al publicar la receta", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Receta no encontrada", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error al cargar la receta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para cargar los detalles de la receta
    private void loadRecipeDetails() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class); // Convertir el DataSnapshot a un objeto Recipe
                    if (recipe != null) {
                        tvRecipeName.setText(recipe.getName()); // Mostrar el nombre de la receta

                        List<Map<String, String>> ingredientsList = recipe.getIngredients();
                        StringBuilder ingredients = new StringBuilder();

                        for (Map<String, String> ingredientMap : ingredientsList) {
                            for (Map.Entry<String, String> entry : ingredientMap.entrySet()) {
                                ingredients.append(entry.getKey())
                                        .append(": ")
                                        .append(entry.getValue())
                                        .append("\n");
                            }
                        }
                        tvIngredients.setText(ingredients.toString()); // Mostrar los ingredientes
                    }
                } else {
                    Toast.makeText(getContext(), "Receta no encontrada", Toast.LENGTH_SHORT).show(); // Mensaje si no se encuentra la receta
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error al cargar la receta", Toast.LENGTH_SHORT).show(); // Mensaje de error
            }
        });
    }
}

