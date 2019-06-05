package org.kicksound.Services;

import org.kicksound.Models.Account;
import org.kicksound.Models.Event;
import org.kicksound.Models.Ticket;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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

    @GET("Events/{id}/tickets")
    Call<List<Ticket>> userParticipateToEvent(@Header("Authorization") String authorization,
                                      @Path("id") String id,
                                      @Query("filter[where][accountId]=") String accountId);

    @GET("Events/{id}/participants")
    Call<List<Account>> getEventParticipants(
            @Header("Authorization") String authorization,
            @Path("id") String eventId);


    @POST("Events/{id}/tickets")
    Call<Ticket> participateToEvent(@Header("Authorization") String authorization,
                                    @Path("id") String id,
                                    @Body Ticket ticket);

    @DELETE("Events/{id}/tickets")
    Call<List<Ticket>> deleteEventParticipation(@Header("Authorization") String authorization,
                                              @Path("id") String id,
                                              @Query("filter[where][accountId]=") String accountId);
}
