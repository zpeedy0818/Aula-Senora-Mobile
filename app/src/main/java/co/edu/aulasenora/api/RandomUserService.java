package co.edu.aulasenora.api;

import co.edu.aulasenora.models.RandomUserResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RandomUserService {

    // Obtener un solo usuario
    @GET("api/")
    Call<RandomUserResponse> getRandomUser();

    // Obtener múltiples usuarios usando parámetros de query
    // Ejemplo de uso: getRandomUsers(10, "us,gb")
    @GET("api/")
    Call<RandomUserResponse> getRandomUsers(
            @Query("results") int results,
            @Query("nat") String nationality
    );
}
