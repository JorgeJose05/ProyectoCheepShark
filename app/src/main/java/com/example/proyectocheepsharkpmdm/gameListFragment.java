package com.example.proyectocheepsharkpmdm;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectocheepsharkpmdm.databinding.ViewholderContenidoJuegosBinding;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * La clase del fragmento que contiene la lista de juegos de la tienda
 *
 * @author Jorge Jose Dumitrache Chust
 * @version 1.0
 *
 *
 */
public class GameListFragment extends Fragment {

    Button random;
    Button button;
    NavController navController;

    List<CheapShark.Deal> dealsList;  // Lista de juegos de la API
    List<Integer> Carrito;
    File carritofile;

    /**
     * Constructor por defecto de la clase
     */
    public GameListFragment() {
        // Required empty public constructor
    }


    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return devuelve la view del fragmento
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        carritofile = new File(getContext().getFilesDir(), "Carrito.txt");
        return inflater.inflate(R.layout.fragment_game_list, container, false);
    }

    /**
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Carrito = new ArrayList<>();
        try {
            // Leer todas las líneas del archivo
            List<String> lines = Files.readAllLines(carritofile.toPath());

            // Limpiar la lista Carrito antes de cargar los nuevos IDs
            Carrito.clear();

            // Agregar los IDs leídos a la lista Carrito
            for (String line : lines) {
                Carrito.add(Integer.parseInt(line.trim())); // Asegúrate de que los IDs sean enteros
            }
        } catch (IOException e) {
            Log.e("Archivo", "Error al leer el archivo", e);
        }

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
                bundle.putString("releaseDate",randomDeal.releaseDate);
                bundle.putString("title", randomDeal.title);
                bundle.putString("salePrice", randomDeal.salePrice);
                bundle.putString("normalPrice", randomDeal.normalPrice);
                bundle.putString("steamRatingPercent",randomDeal.steamRatingPercent);
                bundle.putString("metacriticScore", randomDeal.metacriticScore);
                bundle.putString("steamRatingText", randomDeal.steamRatingText);
                bundle.putString("steamRatingCount", randomDeal.steamRatingCount);
                bundle.putString("dealRating", randomDeal.dealRating);
                bundle.putString("steamAppID",randomDeal.steamAppID);
                bundle.putString("foto", randomDeal.thumb);  // Pasa los datos que necesites
                navController.navigate(R.id.gameDetailFragment, bundle);

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
        cheapSharkViewModel.fetchDeals("1", "150");
    }

    /**La clase del viewholder de los deals
     *
     *
     */
    static class DealViewHolder extends RecyclerView.ViewHolder {
        ViewholderContenidoJuegosBinding binding;

        /**El constructor del DealViewHolder
         *
         * @param binding
         */
        public DealViewHolder(@NonNull ViewholderContenidoJuegosBinding binding) {
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
         * El metodo que devuelve el viewholder inflado con el inflate
         *
         * @param parent The ViewGroup into which the new View will be added after it is bound to
         *               an adapter position.
         * @param viewType The view type of the new View.
         *
         * @return devuelve el viewholder de deal ya rellenado con el inflater
         */
        @NonNull
        @Override
        public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DealViewHolder(ViewholderContenidoJuegosBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        /**
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
            // Asegúrate de que steamAppID no sea null ni vacío antes de intentar convertirlo
            if (deal.steamAppID != null && !deal.steamAppID.isEmpty()) {
                if(Carrito.contains(Integer.parseInt(deal.steamAppID))){
                    holder.binding.carritoButton.setBackgroundColor(Color.parseColor("#ff0000"));
                }else{
                    holder.binding.carritoButton.setBackgroundColor(Color.parseColor("#00ff00"));
                }
            }else{
                Log.e("Steamid malo", deal.title + deal.steamAppID);
            }


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
                bundle.putString("steamAppID",deal.steamAppID);
                bundle.putString("foto", deal.thumb);  // Pasa los datos que necesites
                navController.navigate(R.id.gameDetailFragment, bundle);
            });


//            try {
//                carritofile.createNewFile();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
            FileWriter pluma;
            try {
               pluma =  new FileWriter(carritofile,true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Clic en el botón para mostrar un Toast
            holder.binding.carritoButton.setOnClickListener(v -> {

                if (((ColorDrawable) holder.binding.carritoButton.getBackground()).getColor() == Color.parseColor("#FF0000")) {
                    // Si el color es rojo (ya está en el carrito), lo cambiamos a verde
                    holder.binding.carritoButton.setBackgroundColor(Color.parseColor("#00FF00"));

                    // Eliminar el steamAppID del archivo
                    removeSteamAppIDFromFile(carritofile, deal.steamAppID);
                    Toast.makeText(requireContext(), "Eliminado del carrito", Toast.LENGTH_SHORT).show();
                } else {
                    // Si el color es verde (no está en el carrito), lo cambiamos a rojo
                    holder.binding.carritoButton.setBackgroundColor(Color.parseColor("#FF0000"));

                    // Añadir el steamAppID al archivo
                    addSteamAppIDToFile(carritofile, deal.steamAppID);
                    Toast.makeText(requireContext(), "Añadido al carrito", Toast.LENGTH_SHORT).show();
                }
            });



        }

        /**El metodo que añade el steamappid del deal del juego al archivo con los tratos en el carrito
         *
         * @param file
         * @param steamAppID
         */
        private void addSteamAppIDToFile(File file, String steamAppID) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(steamAppID + "\n");  // Agregar el ID al archivo
                writer.close();
            } catch (IOException e) {
                Log.e("Archivo", "Error al escribir en el archivo", e);
            }
        }

        /**
         * El metodo que elimina un id de la lista del carrito que esta guardada en un archivo
         * @param file
         * @param steamAppID
         */
        private void removeSteamAppIDFromFile(File file, String steamAppID) {
            try {
                // Leer todas las líneas del archivo
                List<String> lines = Files.readAllLines(file.toPath());

                // Filtrar la lista para eliminar la línea con el ID específico
                List<String> updatedLines = new ArrayList<>();
                for (String line : lines) {
                    if (!line.contains(steamAppID)) {
                        updatedLines.add(line);  // Mantener todas las líneas excepto la que contiene el ID
                    }
                }

                // Escribir las líneas actualizadas de vuelta al archivo
                Files.write(file.toPath(), updatedLines);

            } catch (IOException e) {
                Log.e("Archivo", "Error al eliminar el ID del archivo", e);
            }
        }

        /**
         * EL metodo que devuelve el tamaño de la lista de juegos
         * @return
         */
        @Override
        public int getItemCount() {
            return dealsList == null ? 0 : dealsList.size();
        }

        /**
         * El metodo que inicializa la deal list con la de la api
         * @param dealsList
         */
        void setDealsList(List<CheapShark.Deal> dealsList) {
            this.dealsList = dealsList;
            notifyDataSetChanged();
        }
    }
}
