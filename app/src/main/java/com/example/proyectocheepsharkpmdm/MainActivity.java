package com.example.proyectocheepsharkpmdm;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Clase principal donde empiza la aplicacion y que contiene el fragment de el nav_graph de
 * la lista de juegos
 *
 * @author Jorge Jose Dumitrache Chust
 * @version 1.0
 *
 */
public class MainActivity extends AppCompatActivity {


    /**
     * Metodo on createDe la clase principal y que desencadena todo el proyecto
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}