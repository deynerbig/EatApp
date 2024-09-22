package com.example.eatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.analytics.FirebaseAnalytics; // Importar Firebase Analytics
import com.google.firebase.FirebaseApp; // Importar Firebase App



public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics mfire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);  // Asegúrate de inicializar Firebase
        mfire = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "app_start");
        mfire.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.principal), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void login(View v) {
        EditText campo1 = this.findViewById(R.id.correo);
        String correo = campo1.getText().toString();
        EditText campo2 = this.findViewById(R.id.contrasenia);
        String contrasenia = campo2.getText().toString();
        System.out.println(correo + " " + contrasenia);
        if (correo.equals("c1") && contrasenia.equals("123")) {
            Intent i = new Intent(this, Principal1.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Error en las credenciales", Toast.LENGTH_SHORT).show();
        }
    }
    public void crearcuenta(View v){
        Intent i =new Intent (this,Registrousuario.class);
        startActivity(i);
    }
}