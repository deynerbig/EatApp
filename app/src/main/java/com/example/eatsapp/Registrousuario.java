package com.example.eatsapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registrousuario extends AppCompatActivity {

    private EditText name, correo, password, password2;
    private Button registerButton;
    private FirebaseAuth mAuth; // Autenticación de Firebase
    private DatabaseReference mDatabase; // Referencia a la base de datos de Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrousuario);

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Inicializa Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Inicializa los elementos de la interfaz
        name = findViewById(R.id.name);
        correo = findViewById(R.id.email);
        password = findViewById(R.id.contrasenia_usuario);
        password2 = findViewById(R.id.reingreso_contra);
        registerButton = findViewById(R.id.butt); // Asegúrate de que este botón esté en tu layout

        // Configura el listener para el botón de registro
        registerButton.setOnClickListener(v -> registerUser());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.principal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void registerUser() {
        // Obtiene los datos ingresados
        String usuario = name.getText().toString().trim();
        String correos = correo.getText().toString().trim();
        String contraseni = password.getText().toString().trim();
        String contras = password2.getText().toString().trim();

        // Valida que los campos no estén vacíos
        if (usuario.isEmpty() || correos.isEmpty() || contraseni.isEmpty() || contras.isEmpty()) {
            Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Valida que las contraseñas coincidan
        if (!contraseni.equals(contras)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crea un nuevo usuario en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(correos, contraseni)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Si el registro es exitoso, guarda los datos del usuario en la base de datos
                        String userId = mAuth.getCurrentUser().getUid();
                        saveUserToDatabase(userId, usuario, correos);
                    } else {
                        // Si hay un error, muestra un mensaje
                        Toast.makeText(Registrousuario.this, "Error en el registro: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToDatabase(String userId, String name, String email) {
        User user = new User(name, email);
        mDatabase.child("users").child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Registrousuario.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                    // Aquí puedes navegar a otra actividad si deseas
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Registrousuario.this, "Error al guardar datos del usuario", Toast.LENGTH_SHORT).show();
                });
    }

    // Clase modelo para los datos del usuario
    public static class User {
        public String name;
        public String email;

        public User() {
            // Constructor vacío requerido para Firebase
        }

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }
}