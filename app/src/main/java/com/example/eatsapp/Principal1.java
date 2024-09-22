package com.example.eatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class Principal1 extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal1);

        drawerLayout = findViewById(R.id.principal);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.openn, R.string.closee);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Otros métodos de navegación...

    // Método que navega a la actividad Publicar_Receta
    public void login1(View view) {
        Intent intent = new Intent(this, Publicar_Receta.class);
        startActivity(intent);
    }

    // Método que navega a la actividad Historial
    public void login2(View view) {
        Intent intent = new Intent(this, Historial.class);
        startActivity(intent);
    }

    // Método que navega a la actividad HistorialFav
    public void login3(View view) {
        Intent intent = new Intent(this, Historial_Favoritos.class);
        startActivity(intent);
    }

    // Método que navega a la actividad Registro_Receta
    public void login4(View view) {
        Intent intent = new Intent(this, Registro_Receta.class);
        startActivity(intent);
    }

    // Métodos para manejar el clic en el menú lateral
    public void login5(View view) {
        // Navega a otra actividad o realiza una acción
    }

    public void login6(View view) {
        // Navega a otra actividad o realiza una acción
    }
}