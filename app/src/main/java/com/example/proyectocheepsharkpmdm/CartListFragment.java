package com.example.proyectocheepsharkpmdm;

import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.example.proyectocheepsharkpmdm.databinding.ViewholderContenidoCarritoBinding;

import java.util.ArrayList;
import java.util.List;

public class CartListFragment extends Fragment {

    Button button;
    NavController navController;

    List<String> cartGameIds = new ArrayList<>();
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
        cartGameIds.add("1"); // Agrega los IDs de los juegos en el carrito
        cartGameIds.add("3064750");

        button = view.findViewById(R.id.button);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });

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
            }
        });

        // Opcional: Si es necesario, realiza una nueva solicitud para datos específicos
        viewModel.fetchDeals("1", "15");
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
