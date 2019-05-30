package org.kicksound.Services;

import org.kicksound.Models.Event;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventService {
    @GET("Events")
    Call<List<Event>> getNextEvents(@Query("filter[order]=") String by,
                                    @Query("filter[where][date][gt]=") Date date);

    @GET("Events")
    Call<List<Event>> getEventsByName(@Query("filter[order]=") String by,
                                      @Query("filter[where][and][1][date][gt]=") Date date,
                                      @Query("filter[where][and][2][title][like]=") String title);

    @GET("Events/{id}")
    Call<Event> getEventById(@Header("Authorization") String authorization,
                             @Path("id") String id);
}
