package org.kicksound.Services;

import org.kicksound.Models.Account;
import org.kicksound.Models.Event;
import org.kicksound.Models.Login;
import org.kicksound.Models.Logout;
import org.kicksound.Models.ResetPassword;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccountService {
    @GET("accounts/me")
    Call<Account> accessTokenExist(@Header("Authorization") String authorization);

    @GET("accounts/{id}")
    Call<Account> getUserById(@Header("Authorization") String authorization, @Path("id") String id);

    @GET("accounts")
    Call<List<Account>> getUsersByUserName(@Header("Authorization") String authorization,
                                           @Query("filter[where][and][0][username][like]=") String username,
                                           @Query("filter[where][and][1][type][neq]=") String classicUser);

    @GET("Photos/{container}/download/{file}")
    Call<ResponseBody> downloadFile(
            @Header("Authorization") String authorization,
            @Path("container") String container,
            @Path("file") String file
    );

    @POST("accounts")
    Call<Account> createAccount(@Body Account account);

    @POST("accounts/login")
    Call<Login> loginAccount(@Body Login login);

    @POST("accounts/logout")
    Call<Logout> logout(@Header("Authorization") String authorization);

    @POST("accounts/reset-password")
    Call<ResetPassword> resetPassword(@Header("Authorization") String authorization, @Body ResetPassword resetPassword);

    @POST("accounts/{id}/events")
    Call<Event> createEvent(@Header("Authorization") String authorization, @Path("id") String id, @Body Event event);

    @Multipart
    @POST("Photos/{container}/upload")
    Call<ResponseBody> uploadFile(
            @Header("Authorization") String authorization,
            @Part MultipartBody.Part file,
            @Path("container") String container

    );

    @PATCH("accounts/{id}")
    Call<Account> updatePictureName(@Header("Authorization") String authorization, @Path("id") String id, @Body Account account);
}
