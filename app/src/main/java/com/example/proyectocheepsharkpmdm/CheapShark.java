/*
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
*/
package com.example.proyectocheepsharkpmdm;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class CheapShark {

    // Clase con solo los campos necesarios
    public class Deal {
        String title;
        String storeID;
        String gameID;
        String salePrice;
        String normalPrice;
        String releaseDate;
        String metacriticScore;
        String steamRatingText;
        String steamRatingCount;
        String steamRatingPercent;
        String steamAppID;
        String dealRating;
        String thumb;
    }

    // Configuración de Retrofit para la API de CheapShark
    public static Api api = new Retrofit.Builder()
            .baseUrl("https://www.cheapshark.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api.class);

    // Interface de la API
    public interface Api {
        /**
         * Obtiene las ofertas filtradas por el ID de la tienda y el precio máximo.
         * @param storeID El ID de la tienda (por ejemplo, "1" para Steam).
         * @param upperPrice Precio máximo de las ofertas.
         * @return Lista de ofertas (`Deal`).
         */
        @GET("api/1.0/deals")
        Call<List<Deal>> getDeals(@Query("storeID") String storeID, @Query("upperPrice") String upperPrice);
    }
}
