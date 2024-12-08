package com.example.proyectocheepsharkpmdm;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * La activity que contiene el fragment de la lista de videojuegos que estan en el carrito de compra
 *
 * @author Jorge Jose Dumitrache Chust
 * @version 1.0
 */
public class ActivityCarrito extends AppCompatActivity {

    /**
     * Este metodo al crearse la actividad pone contenido de la view como el activity_carrito.xml
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityCarrito), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}