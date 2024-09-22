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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroDatosUs extends AppCompatActivity {

    private EditText edad, peso, altura;
    private Button btnRegistrar; // Botón para registrar los datos
    private DatabaseReference mDatabase; // Referencia a la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_datos_us);

        // Inicializar Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Inicializar elementos de la interfaz
        edad = findViewById(R.id.etEdad);
        peso = findViewById(R.id.etPeso);
        altura = findViewById(R.id.etAltura);
        btnRegistrar = findViewById(R.id.buttonn); // Asegúrate de que el ID sea correcto

        // Configurar el listener del botón
        btnRegistrar.setOnClickListener(v -> registrarDatos());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void registrarDatos() {
        // Obtener los datos ingresados por el usuario
        String redad = edad.getText().toString().trim();
        String pesos = peso.getText().toString().trim();
        String tamanio = altura.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (redad.isEmpty() || pesos.isEmpty() || tamanio.isEmpty()) {
            Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardar los datos en Firebase
        String userId = "user_id_placeholder"; // Cambia esto por el ID de usuario actual, si lo tienes
        UserData userData = new UserData(redad, pesos, tamanio);
        mDatabase.child("users").child(userId).setValue(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegistroDatosUs.this, "Datos registrados con éxito", Toast.LENGTH_SHORT).show();
                    // Aquí puedes redirigir a otra actividad si lo deseas
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegistroDatosUs.this, "Error al registrar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Clase modelo para los datos del usuario
    public static class UserData {
        public String edad;
        public String peso;
        public String altura;

        public UserData() {
            // Constructor vacío requerido para Firebase
        }

        public UserData(String edad, String peso, String altura) {
            this.edad = edad;
            this.peso = peso;
            this.altura = altura;
        }
    }
}
