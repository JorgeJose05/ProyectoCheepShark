package com.example.proyectocheepsharkpmdm;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

//http://itunes.apple.com/search?term=beatles
public class Itunes {



        class Respuesta {
            List<Contenido> results;
        }

        class Contenido {
            String artistName;
            String trackName;
            String artworkUrl100;
        }




        public static Api api = new Retrofit.Builder()
                .baseUrl("https://itunes.apple.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class);

        public interface Api {
            @GET("search/")
            default Call<Respuesta> buscar(@Query("term") String ignored) {
                return buscarPorTermino("nirvana");
            }

            @GET("search/")
            Call<Respuesta> buscarPorTermino(@Query("term") String term);

        }




}
