package com.example.proyectocheepsharkpmdm;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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

import com.bumptech.glide.Glide;
import com.example.proyectocheepsharkpmdm.R;
import com.example.proyectocheepsharkpmdm.databinding.ViewholderContenidoBinding;

import java.util.List;

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

        button = view.findViewById(R.id.btn_cart);
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

        ItunesViewModel itunesViewModel = new ViewModelProvider(this).get(ItunesViewModel.class);

        itunesViewModel.buscar("nirvana");

        itunesViewModel.respuestaMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Itunes.Respuesta>() {
            @Override
            public void onChanged(Itunes.Respuesta respuesta) {
                //respuesta.results.forEach(contenido -> Log.e("ABCD", contenido.artistName + ", " + contenido.trackName + ", " + contenido.artworkUrl100));

                //antes estaba el foreach de antes pero he puesto antes porque el .forEach es de otra api y prefiero jugarmela mas tarde
                for (Itunes.Contenido contenido : respuesta.results){
                    Log.e("ABCD", contenido.artistName + ", " + contenido.trackName + ", " + contenido.artworkUrl100);
                }
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_games);
        ContenidosAdapter contenidosAdapter = new ContenidosAdapter();
        recyclerView.setAdapter(contenidosAdapter);

        itunesViewModel.respuestaMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Itunes.Respuesta>() {
            @Override
            public void onChanged(Itunes.Respuesta respuesta) {
                contenidosAdapter.establecerListaContenido(respuesta.results);
                // respuesta.results.forEach(r -> Log.e("ABCD", r.artistName + ", " + r.trackName + ", " + r.artworkUrl100));
            }
        });

    }

    static class ContenidoViewHolder extends RecyclerView.ViewHolder{
        ViewholderContenidoBinding binding;

        public ContenidoViewHolder(@NonNull ViewholderContenidoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    //clase adaptador
    class ContenidosAdapter extends RecyclerView.Adapter<ContenidoViewHolder>{
            List<Itunes.Contenido> contenidoList;

            @NonNull
            @Override
            public ContenidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ContenidoViewHolder(ViewholderContenidoBinding.inflate(getLayoutInflater(), parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull ContenidoViewHolder holder, int position) {
                Itunes.Contenido contenido = contenidoList.get(position);

                holder.binding.title.setText(contenido.trackName);
                holder.binding.artist.setText(contenido.artistName);
                Glide.with(requireActivity()).load(contenido.artworkUrl100).into(holder.binding.artwork);
            }

            @Override
            public int getItemCount() {
                return contenidoList == null ? 0 : contenidoList.size();
            }

            void establecerListaContenido(List<Itunes.Contenido> contenidoList){
                this.contenidoList = contenidoList;
                notifyDataSetChanged();
            }
        }





}

