package com.example.eatsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Historial_Detallado2 extends AppCompatActivity {

    private TextView recipeNameTextView, recipeDescriptionTextView, averageRatingTextView;
    private RatingBar ratingBar;
    private EditText commentEditText;
    private Button submitCommentButton;
    private RecyclerView commentsRecyclerView;

    private FirebaseFirestore db;
    private String recipeId;
    private CommentAdapter commentAdapter;//error
    private List<Comment> commentList;//error

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Habilitar EdgeToEdge para que la interfaz sea fluida
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historial_detallado2);

        // Ajuste de insets para respetar los bordes del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar las vistas y la lógica de Firebase
        recipeId = getIntent().getStringExtra("RECIPE_ID");
        db = FirebaseFirestore.getInstance();

        initializeViews();
        loadRecipeDetails();
        setupCommentsList();
        setupListeners();
    }

    private void initializeViews() {
        recipeNameTextView = findViewById(R.id.recipeNameTextView);
        recipeDescriptionTextView = findViewById(R.id.recipeDescriptionTextView);
        averageRatingTextView = findViewById(R.id.averageRatingTextView);
        ratingBar = findViewById(R.id.ratingBar);
        commentEditText = findViewById(R.id.commentEditText);
        submitCommentButton = findViewById(R.id.submitCommentButton);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
    }

    private void loadRecipeDetails() {
        db.collection("recipes").document(recipeId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Recipe recipe = documentSnapshot.toObject(Recipe.class);
                    if (recipe != null) {
                        recipeNameTextView.setText(recipe.getName());
                        recipeDescriptionTextView.setText(formatIngredients(recipe.getIngredients())); // Cambia aquí
                        loadAverageRating(); // Cargar la valoración promedio
                    }
                });
    }

    private String formatIngredients(List<Map<String, String>> ingredients) {
        StringBuilder formattedIngredients = new StringBuilder();
        for (Map<String, String> ingredient : ingredients) {
            // Asume que cada mapa tiene claves "name" y "quantity"
            String name = ingredient.get("name");
            String quantity = ingredient.get("quantity");
            if (name != null && quantity != null) {
                formattedIngredients.append(quantity).append(" ").append(name).append("\n"); // Formato: "cantidad nombre"
            }
        }
        return formattedIngredients.toString();
    }

    private void loadAverageRating() {
        db.collection("ratings")
                .whereEqualTo("recipeId", recipeId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        double totalRating = 0;
                        int count = queryDocumentSnapshots.size();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) { //error
                            totalRating += documentSnapshot.getDouble("rating");//error
                        }
                        double averageRating = totalRating / count;
                        averageRatingTextView.setText(String.format("Valoración promedio: %.1f", averageRating));
                    }
                });
    }

    private void setupCommentsList() {
        // Inicializa la lista de comentarios
        commentList = new ArrayList<>();

        // Inicializa el adaptador con la lista de comentarios
        commentAdapter = new CommentAdapter(commentList); // Cambia 'new Comment' por 'new CommentAdapter'

        // Configura el RecyclerView con un LinearLayoutManager y el adaptador
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentAdapter);

        // Cargar los comentarios desde Firebase
        loadComments();
    }

    private void loadComments() {
        db.collection("comments")
                .whereEqualTo("recipeId", recipeId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    commentList.clear(); // Asegúrate de que la lista está limpia antes de añadir nuevos comentarios
                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) { // Cambia 'var' por 'DocumentSnapshot'
                        Comment comment = documentSnapshot.toObject(Comment.class);
                        if (comment != null) {
                            commentList.add(comment);
                        }
                    }
                    commentAdapter.notifyDataSetChanged(); // Notifica al adapter que los datos han cambiado
                });
    }

    private void setupListeners() {
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                submitRating(rating);
            }
        });

        submitCommentButton.setOnClickListener(v -> submitComment());
    }

    private void submitRating(float rating) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> ratingData = new HashMap<>();
        ratingData.put("recipeId", recipeId);
        ratingData.put("userId", userId);
        ratingData.put("rating", rating);

        db.collection("ratings")
                .add(ratingData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Valoración enviada", Toast.LENGTH_SHORT).show();
                    loadAverageRating(); // Recargar la valoración promedio
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al enviar la valoración", Toast.LENGTH_SHORT).show());
    }

    private void submitComment() {
        String commentText = commentEditText.getText().toString().trim();
        if (!commentText.isEmpty()) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Comment comment = new Comment(recipeId, userId, commentText, System.currentTimeMillis());//error

            db.collection("comments")
                    .add(comment)//error
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Comentario enviado", Toast.LENGTH_SHORT).show();
                        commentEditText.setText("");
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al enviar el comentario", Toast.LENGTH_SHORT).show());
        }
    }
}