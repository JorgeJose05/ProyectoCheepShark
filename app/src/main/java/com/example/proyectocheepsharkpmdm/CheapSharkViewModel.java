package com.example.proyectocheepsharkpmdm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * La clase viewmodel que intercomunica la api con los fragments que observan su
 * mutablelivedata
 *
 * @author Jorge Jose Dumitrache Chust
 * @version 1.0
 *
 */
public class CheapSharkViewModel extends AndroidViewModel {

    // LiveData que contiene la lista de tratos
    MutableLiveData<List<CheapShark.Deal>> dealsLiveData = new MutableLiveData<>();

    /**
     * El metodo que carga la aplicacion que se le pasa cuando se crea al AndroidViewModel
     * @param application
     */
    public CheapSharkViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Método para buscar tratos en la API de CheapShark.
     * @param storeID ID de la tienda (por ejemplo, "1" para Steam).
     * @param upperPrice Precio máximo de las ofertas.
     */
    public void fetchDeals(String storeID, String upperPrice) {
        CheapShark.api.getDeals(storeID, upperPrice).enqueue(new Callback<List<CheapShark.Deal>>() {
            /**
             * El metodo que cada vez que se cambia deal list la actualiza
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(@NonNull Call<List<CheapShark.Deal>> call, @NonNull Response<List<CheapShark.Deal>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Actualizamos el LiveData con la lista de tratos
                    dealsLiveData.postValue(response.body());
                } else {
                    // Si hay un error en la respuesta, pasamos una lista vacía o null
                    dealsLiveData.postValue(null);
                }
            }

            /**
             * Este metodo se aasegura que si la lista tiene algun error al actulizarse no ponga ningun dato
             * @param call
             * @param t
             */
            @Override
            public void onFailure(@NonNull Call<List<CheapShark.Deal>> call, @NonNull Throwable t) {
                // En caso de fallo, pasamos null al LiveData
                dealsLiveData.postValue(null);
            }
        });
    }

    /**
     * Devuelve el LiveData que contiene la lista de tratos.
     * @return MutableLiveData con los datos de los tratos.
     */
    public MutableLiveData<List<CheapShark.Deal>> getDealsLiveData() {
        return dealsLiveData;
    }
}
