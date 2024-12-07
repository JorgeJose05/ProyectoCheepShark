package com.example.proyectocheepsharkpmdm;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class GameDetailFragment extends Fragment {
    Button tienda, carrito, metacriticScorebutton;
    ImageButton carritoButton;

    public GameDetailFragment() {
        // Requiere un constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el diseño del fragmento
        // Infla el diseño del fragmento
        View rootView = inflater.inflate(R.layout.fragment_game_detail, container, false);

        metacriticScorebutton = rootView.findViewById(R.id.metacriticScorebutton);

        tienda = rootView.findViewById(R.id.Tienda);
        carrito = rootView.findViewById(R.id.btn_cart);

        carritoButton = rootView.findViewById(R.id.carritoButton);

        if (carritoButton.getTag().equals("")){
            carritoButton.setBackgroundColor(Color.parseColor("#00FF00"));
            carritoButton.setTag("true");
        }else if( carritoButton.getTag().equals("true") ){
            carritoButton.setBackgroundColor(Color.parseColor("#00FF00"));
        }else{
            carritoButton.setBackgroundColor(Color.parseColor("#FF0000"));
            carritoButton.setTag("false");
        }

        carritoButton.setOnClickListener(v -> {
            if (carritoButton.getTag().equals("")){
                carritoButton.setBackgroundColor(Color.parseColor("#FF0000"));
                carritoButton.setTag("false");
            }else if( carritoButton.getTag().equals("true") ){
                carritoButton.setBackgroundColor(Color.parseColor("#FF0000"));
                carritoButton.setTag("false");
            }else{
                carritoButton.setBackgroundColor(Color.parseColor("#00FF00"));
                carritoButton.setTag("true");
            }
        });

        tienda.setOnClickListener(v -> {
            // Cambiar entre fragments
            //Quito esto porque sino cada vez que cambio de activity me lleva al fragment del navigation por defecto navController.navigate(R.id.gameDetailFragment);

            // Cambiar entre actividades
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
        });
        carrito.setOnClickListener(v -> {
            // Cambiar entre fragments
            //Quito esto porque sino cada vez que cambio de activity me lleva al fragment del navigation por defecto navController.navigate(R.id.gameDetailFragment);

            // Cambiar entre actividades
            Intent intent = new Intent(requireContext(), ActivityCarrito.class);
            startActivity(intent);
        });

        // Obtener el Bundle con los datos
        Bundle bundle = getArguments();
        if (bundle != null) {
            // Obtener los datos del Bundle
            String title = bundle.getString("title");
            String salePrice = bundle.getString("salePrice");
            String normalPrice = bundle.getString("normalPrice");
            String metacriticScore = bundle.getString("metacriticScore");
            String steamRatingText = bundle.getString("steamRatingText");
            String steamRatingCount = bundle.getString("steamRatingCount");
            String dealRating = bundle.getString("dealRating");
            String steamRatingPercent = bundle.getString("steamRatingPercent");
            String thumb = bundle.getString("foto");
            String steamAppID = bundle.getString("steamAppID");
            String fechasalida = bundle.getString("releaseDate");

            Log.e("id steam",steamAppID);

            // Obtener las vistas del XML
            TextView titleTextView = rootView.findViewById(R.id.tittle);
            TextView salePriceTextView = rootView.findViewById(R.id.salePrice);
            TextView normalPriceTextView = rootView.findViewById(R.id.normalPrice);
            TextView metacriticScoreTextView = rootView.findViewById(R.id.metacriticScorebutton);
            TextView steamRatingCountTextView = rootView.findViewById(R.id.steamratingcount);
            TextView steamRatingPercentTextView = rootView.findViewById(R.id.steamratingpercent);
            ImageView thumbImageView = rootView.findViewById(R.id.imageView2);
            TextView fecha = rootView.findViewById(R.id.fecha);

            // Asignar los valores a las vistas
            titleTextView.setText(title);
            salePriceTextView.setText(salePrice+'€');
            normalPriceTextView.setText(normalPrice+'€');
            metacriticScoreTextView.setText(metacriticScore);
            steamRatingCountTextView.setText("Numero de calificaciones:\n"+steamRatingCount);
            steamRatingPercentTextView.setText("Porcentage de calificaciones positivas:\n"+steamRatingPercent);

            // Si tienes un URL para la imagen (thumb), puedes cargarla con una librería como Glide o Picasso
             Glide.with(this).load(thumb).into(thumbImageView);

             //Asignar la fecha
             long fechaENMillis = Integer.parseInt(fechasalida) * 1000L;


            Date releaseDate = new Date(fechaENMillis);

// Crear un formateador de fecha para mostrar la fecha en el formato que desees
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(releaseDate);

            fecha.setText(formattedDate.toString());

            //color de metacritic
            if (Integer.parseInt(metacriticScore) <=50){
                metacriticScorebutton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff6874")));

            }else if(Integer.parseInt(metacriticScore) <=70){

                metacriticScorebutton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffbd3f")));
            }else{
                metacriticScorebutton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00ce7a")));
            }

        }

        return rootView;
    }
}
