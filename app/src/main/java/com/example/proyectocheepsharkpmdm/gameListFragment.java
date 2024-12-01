package com.example.proyectocheepsharkpmdm;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.proyectocheepsharkpmdm.R;
public class gameListFragment extends Fragment {

    Button button;
    NavController navController;

    public gameListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button = view.findViewById(R.id.btn_genre_action);
        navController = Navigation.findNavController(view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //esto es lo necesario para cambiar entre fragments   navController.navigate(R.id.gameDetailFragment);

             //Esto es lo necesario para cambiar entre activitis
                Intent intent = new Intent(requireContext(), ActivityCarrito.class);

                // Iniciar la nueva actividad
                startActivity(intent);
            }
        });

    }
}

