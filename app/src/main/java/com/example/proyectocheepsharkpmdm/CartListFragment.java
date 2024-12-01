package com.example.proyectocheepsharkpmdm;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class CartListFragment extends Fragment {

    Button button;
    NavController navController;

    public CartListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        button = view.findViewById(R.id.button);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //esto es lo necesario para cambiar entre fragments   navController.navigate(R.id.gameDetailFragment);

             //Esto es lo necesario para cambiar entre activitis
                Intent intent = new Intent(getContext(), MainActivity.class);

                // Iniciar la nueva actividad
                startActivity(intent);
            }
        });

    }
}