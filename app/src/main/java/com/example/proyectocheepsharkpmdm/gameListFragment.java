package com.example.proyectocheepsharkpmdm;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectocheepsharkpmdm.databinding.ViewholderContenidoCarritoBinding;
import com.example.proyectocheepsharkpmdm.databinding.ViewholderContenidoJuegosBinding;

import java.util.List;
import java.util.Random;

public class GameListFragment extends Fragment {

    Button random;
    Button button;
    NavController navController;

    List<CheapShark.Deal> dealsList;  // Lista de juegos de la API

    public GameListFragment() {
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

        random = view.findViewById(R.id.btn_random);
        button = view.findViewById(R.id.btn_cart);
        navController = Navigation.findNavController(view);

        button.setOnClickListener(v -> {
            // Cambiar entre fragments
           //Quito esto porque sino cada vez que cambio de activity me lleva al fragment del navigation por defecto navController.navigate(R.id.gameDetailFragment);

            // Cambiar entre actividades
            Intent intent = new Intent(requireContext(), ActivityCarrito.class);
            startActivity(intent);
        });

        random.setOnClickListener(v -> {
            if (dealsList != null && !dealsList.isEmpty()) {
                // Elegir un juego aleatorio
                Random randomizer = new Random();
                int randomIndex = randomizer.nextInt(dealsList.size());
                CheapShark.Deal randomDeal = dealsList.get(randomIndex);

                // Crear un Bundle para pasar el ID del juego al siguiente fragmento
                Bundle bundle = new Bundle();
                bundle.putString("game_id", randomDeal.gameID);  // Pasa el ID del juego aleatorio

                // Navegar al fragmento de detalle con el Bundle
                navController.navigate(R.id.gameDetailFragment, bundle);
            } else {
                // Mostrar mensaje si no hay juegos disponibles
                Toast.makeText(requireContext(), "No hay juegos disponibles", Toast.LENGTH_SHORT).show();
            }
        });

        // Usar el CheapSharkViewModel
        CheapSharkViewModel cheapSharkViewModel = new ViewModelProvider(this).get(CheapSharkViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_games);
        // Cambiar a GridLayoutManager con 2 columnas
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        DealsAdapter dealsAdapter = new DealsAdapter();
        recyclerView.setAdapter(dealsAdapter);

        // Observar los cambios en el LiveData
        cheapSharkViewModel.getDealsLiveData().observe(getViewLifecycleOwner(), new Observer<List<CheapShark.Deal>>() {
            @Override
            public void onChanged(List<CheapShark.Deal> deals) {
                if (deals != null) {
                    dealsList = deals;  // Guardar la lista de juegos
                    dealsAdapter.setDealsList(deals);
                }
            }
        });

        // Realizar la solicitud
        cheapSharkViewModel.fetchDeals("1", "15");
    }

    static class DealViewHolder extends RecyclerView.ViewHolder {
        ViewholderContenidoJuegosBinding binding;

        public DealViewHolder(@NonNull ViewholderContenidoJuegosBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    // Adaptador para el RecyclerView
    class DealsAdapter extends RecyclerView.Adapter<DealViewHolder> {
        List<CheapShark.Deal> dealsList;

        @NonNull
        @Override
        public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DealViewHolder(ViewholderContenidoJuegosBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
            CheapShark.Deal deal = dealsList.get(position);

            holder.binding.title.setText(deal.title);
            holder.binding.PrOriginal.setText(deal.normalPrice+'€');
            holder.binding.PrDesc.setText(deal.salePrice+'€');

            // Configura el tachado en el texto
            holder.binding.PrOriginal.setPaintFlags(holder.binding.PrOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            Glide.with(requireActivity()).load(deal.thumb).into(holder.binding.foto);


            // Clic en el LinearLayout
            holder.binding.getRoot().setOnClickListener(v -> {
                // Navegar al siguiente fragmento con datos
                Bundle bundle = new Bundle();
                bundle.putString("releaseDate",deal.releaseDate);
                bundle.putString("title", deal.title);
                bundle.putString("salePrice", deal.salePrice);
                bundle.putString("normalPrice", deal.normalPrice);
                bundle.putString("steamRatingPercent",deal.steamRatingPercent);
                bundle.putString("metacriticScore", deal.metacriticScore);
                bundle.putString("steamRatingText", deal.steamRatingText);
                bundle.putString("steamRatingCount", deal.steamRatingCount);
                bundle.putString("dealRating", deal.dealRating);
                bundle.putString("foto", deal.thumb);  // Pasa los datos que necesites
                navController.navigate(R.id.gameDetailFragment, bundle);
            });

            // Clic en el botón para mostrar un Toast
            holder.binding.carritoButton.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Botón Carrito clickeado", Toast.LENGTH_SHORT).show();
            });


        }

        @Override
        public int getItemCount() {
            return dealsList == null ? 0 : dealsList.size();
        }

        void setDealsList(List<CheapShark.Deal> dealsList) {
            this.dealsList = dealsList;
            notifyDataSetChanged();
        }
    }
}
