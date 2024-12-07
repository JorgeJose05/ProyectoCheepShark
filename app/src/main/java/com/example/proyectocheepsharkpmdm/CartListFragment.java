package com.example.proyectocheepsharkpmdm;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.proyectocheepsharkpmdm.databinding.ViewholderContenidoCarritoBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CartListFragment extends Fragment {

    Button button;
    NavController navController;
    TextView nojuegos;
    List<String> cartGameIds = new ArrayList<>();

    ImageButton carritoButton;

    public CartListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carritoButton = view.findViewById(R.id.carritoButton);
        nojuegos = view.findViewById(R.id.NoJuegos);
        button = view.findViewById(R.id.button);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });

        loadCartGameIds();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        DealsAdapter dealsAdapter = new DealsAdapter();
        recyclerView.setAdapter(dealsAdapter);

        // Aquí puedes usar un ViewModel si los datos del carrito se gestionan globalmente
        CheapSharkViewModel viewModel = new ViewModelProvider(this).get(CheapSharkViewModel.class);

        // Supongamos que tienes una lista separada para los juegos en el carrito
        viewModel.getDealsLiveData().observe(getViewLifecycleOwner(), new Observer<List<CheapShark.Deal>>() {
            @Override
            public void onChanged(List<CheapShark.Deal> deals) {
                List<CheapShark.Deal> filteredDeals = new ArrayList<>();

                // Filtrar las ofertas por ID, para mostrar solo las que están en el carrito
                for (CheapShark.Deal deal : deals) {
                    if (cartGameIds.contains(deal.steamAppID)) {
                        filteredDeals.add(deal);
                    }
                }

                // Aquí filtras o ajustas la lista para mostrar solo los elementos del carrito
                dealsAdapter.setDealsList(filteredDeals);

                if (filteredDeals.isEmpty()){
                    nojuegos.setVisibility(View.VISIBLE);
                }else{
                    nojuegos.setVisibility(View.INVISIBLE);
                }

            }
        });



        // Opcional: Si es necesario, realiza una nueva solicitud para datos específicos
        viewModel.fetchDeals("1", "15");
    }
    // Método para leer los IDs desde el archivo Carrito.txt
    private void loadCartGameIds() {
        try {
            // Leer todas las líneas del archivo
            List<String> lines = Files.readAllLines(new java.io.File(getContext().getFilesDir(), "Carrito.txt").toPath());

            // Limpiar la lista antes de cargar los nuevos datos
            cartGameIds.clear();

            // Agregar los IDs leídos a la lista cartGameIds
            for (String line : lines) {
                cartGameIds.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace(); // Manejo de errores si no se puede leer el archivo
        }
    }

    // Método para guardar los IDs actualizados en el archivo Carrito.txt
    private void saveCartGameIds() {
        try {
            FileOutputStream fos = new FileOutputStream(new File(getContext().getFilesDir(), "Carrito.txt"));
            for (String id : cartGameIds) {
                fos.write((id + "\n").getBytes(StandardCharsets.UTF_8)); // Escribir en el archivo
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class DealViewHolder extends RecyclerView.ViewHolder {
        ViewholderContenidoCarritoBinding binding;

        public DealViewHolder(@NonNull ViewholderContenidoCarritoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class DealsAdapter extends RecyclerView.Adapter<DealViewHolder> {
        List<CheapShark.Deal> dealsList;

        @NonNull
        @Override
        public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DealViewHolder(ViewholderContenidoCarritoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
            CheapShark.Deal deal = dealsList.get(position);

            holder.binding.title.setText(deal.title);
            holder.binding.PrOriginal.setText(deal.normalPrice+'€');
            holder.binding.PrDesc.setText(deal.salePrice+'€');
            Glide.with(requireActivity()).load(deal.thumb).into(holder.binding.foto);


            holder.binding.PrOriginal.setPaintFlags(holder.binding.PrOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            // Acción de eliminar juego del carrito
            holder.binding.carritoButtonC.setOnClickListener(v -> {
                String gameId = deal.steamAppID;

                // Eliminar el juego de la lista de IDs del carrito
                cartGameIds.remove(gameId);

                // Actualizar el archivo Carrito.txt con la lista modificada
                saveCartGameIds();

                // Notificar al RecyclerView para que se actualice
                dealsList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dealsList.size());

                if (dealsList.isEmpty()) {
                    nojuegos.setVisibility(View.VISIBLE);  // Mostrar mensaje si el carrito está vacío
                }
            });

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
                bundle.putString("steamAppID",deal.steamAppID);
                bundle.putString("foto", deal.thumb);  // Pasa los datos que necesites
                navController.navigate(R.id.gameDetailFragment, bundle);
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
