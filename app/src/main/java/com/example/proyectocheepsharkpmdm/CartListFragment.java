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

/**
 * La activity del fragmento que contiene la lista de juegos que hay en el carrito de
 * compras
 *
 * @author Jorge Jose Dumitrache Chust
 * @version 1.0
 *
 */
public class CartListFragment extends Fragment {

    Button button;
    NavController navController;
    TextView nojuegos;
    List<String> cartGameIds = new ArrayList<>();
    Button comprarTodo;


    ImageButton carritoButton;

    /**
     * El constructor por defecto de la clase
     */
    public CartListFragment() {
        // Required empty public constructor
    }
    private void updateCartUI(List<CheapShark.Deal> dealsList) {
        double totalPrice = dealsList.stream()
                .mapToDouble(deal -> Double.parseDouble(deal.salePrice))
                .sum();

        comprarTodo.setText(String.format("%.2f€ en total", totalPrice));

        if (dealsList.isEmpty()) {
            nojuegos.setVisibility(View.VISIBLE);
            comprarTodo.setVisibility(View.INVISIBLE);
        } else {
            nojuegos.setVisibility(View.INVISIBLE);
            comprarTodo.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        comprarTodo = view.findViewById(R.id.CompraCarrito);
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


                comprarTodo.setText((String.format("%.2f€ en total", filteredDeals.stream().mapToDouble(f -> Double.parseDouble(f.salePrice)).sum())));

                updateCartUI(filteredDeals);

                if (filteredDeals.isEmpty()){
                    nojuegos.setVisibility(View.VISIBLE);
                    comprarTodo.setVisibility(View.INVISIBLE);
                }else{
                    nojuegos.setVisibility(View.INVISIBLE);
                    comprarTodo.setVisibility(View.VISIBLE);
                }

            }
        });

        comprarTodo.setOnClickListener(view1 -> {
            // Limpiar la lista de IDs del carrito
            cartGameIds.clear();

            // Guardar los cambios en el archivo local
            saveCartGameIds();

            // Limpiar la lista del adaptador y notificar los cambios
            dealsAdapter.setDealsList(new ArrayList<>());



            // Actualizar la visibilidad de los elementos
            nojuegos.setVisibility(View.VISIBLE);
            comprarTodo.setVisibility(View.INVISIBLE);

            // Actualizar la UI del carrito después de vaciarlo
            updateCartUI(new ArrayList<>());

        });


        // Opcional: Si es necesario, realiza una nueva solicitud para datos específicos
        viewModel.fetchDeals("1", "15");
    }
    // Método para leer los IDs desde el archivo Carrito.txt
    /**
     * Carga la variable de lista de juegos con la lista de juegos que hay en el archivo local
     */
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
        }catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    // Método para guardar los IDs actualizados en el archivo Carrito.txt
    /**
     * El metodo que guarda los id's de la lista que tiene la clase en el archivo local
     */
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

    /**La clase del viewholder de los deals
     *
     *
     */
    static class DealViewHolder extends RecyclerView.ViewHolder {
        ViewholderContenidoCarritoBinding binding;

        /**El constructor del DealViewHolder
         *
         * @param binding
         */
        public DealViewHolder(@NonNull ViewholderContenidoCarritoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    // Adaptador para el RecyclerView

    /**El adaptador que coge la lista e inserta los datos en el viewholder segun la
     * posicion del deal en la lista
     *
     */
    class DealsAdapter extends RecyclerView.Adapter<DealViewHolder> {
        List<CheapShark.Deal> dealsList;

        /**
         *
         * El metodo que devuelve el viewholder inflado con el inflate
         *
         * @param parent The ViewGroup into which the new View will be added after it is bound to
         *               an adapter position.
         * @param viewType The view type of the new View.
         *
         * @return
         */
        @NonNull
        @Override
        public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DealViewHolder(ViewholderContenidoCarritoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        /**
         *
         * El metodo que rellena los view holders con la informacion de la
         * lista segun la posicion del objeto deal en la lista
         *
         * @param holder The ViewHolder which should be updated to represent the contents of the
         *        item at the given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
            CheapShark.Deal deal = dealsList.get(position);

            holder.binding.title.setText(deal.title);
            holder.binding.PrOriginal.setText((String.format("%s€", deal.normalPrice)));
            holder.binding.PrDesc.setText((String.format("%s€", deal.salePrice)));
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


                // Actualizar la UI del carrito después de eliminar el juego
                updateCartUI(dealsList);

                if (dealsList.isEmpty()) {
                    nojuegos.setVisibility(View.VISIBLE);  // Mostrar mensaje si el carrito está vacío
                    comprarTodo.setVisibility(View.INVISIBLE);
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

        /**
         * EL metodo que devuelve el tamaño de la lista de juegos
         * @return devuelve el tamaño de la lista
         */
        @Override
        public int getItemCount() {
            return dealsList == null ? 0 : dealsList.size();
        }





        /**
         * El metodo que inicializa la deal list con la de la api
         */
        void setDealsList(List<CheapShark.Deal> dealsList) {
            this.dealsList = dealsList;
            notifyDataSetChanged();
        }
    }
}
