
package com.example.proyectocheepsharkpmdm;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * La clase Cheapshark que tiene la clase de los tratos sobre juegos la llamada a la api  y el metodo para que devuelva la
 * lista de juegos segun el id de la tienda y segun el precio
 *
 * @author Jorge Jose Dumitrache Chust
 * @version 1.0
 */
public class CheapShark {

    // Clase con solo los campos necesarios

    /**
     * El constructor de los tratos de videojuegos que hay en la api
     */
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
    /**
     * La llamada a la api de cheapshark con retrofit
     */
    public static Api api = new Retrofit.Builder()
            .baseUrl("https://www.cheapshark.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api.class);

    // Interface de la API

    /**
     * La interfaz que implementara la clase de la api de cheapsark con el metodo para buscar segun el id de la tienda y el precio maximo
     */
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
