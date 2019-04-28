package org.kicksound.Services;

import org.kicksound.Models.Account;
import org.kicksound.Models.Event;
import org.kicksound.Models.Login;
import org.kicksound.Models.Logout;
import org.kicksound.Models.ResetPassword;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AccountService {
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

    @GET("accounts/me")
    Call<Account> accessTokenExist(@Header("Authorization") String authorization);

    @GET("accounts/{id}")
    Call<Account> getUserById(@Header("Authorization") String authorization, @Path("id") String id);

    @Multipart
    @POST("Photos/{container}/upload")
    Call<ResponseBody> uploadEventImage(
            @Header("Authorization") String authorization,
            @Part MultipartBody.Part file,
            @Path("container") String container

    );
}
