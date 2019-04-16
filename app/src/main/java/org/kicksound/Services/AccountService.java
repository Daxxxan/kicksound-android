package org.kicksound.Services;

import org.kicksound.Models.Account;
import org.kicksound.Models.Login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccountService {
    @POST("accounts")
    Call<Account> createAccount(@Body Account account);

    @POST("accounts/login")
    Call<Login> loginAccount(@Body Login login);

    @GET("accounts/me")
    Call<Account> accessTokenExist(@Header("Authorization") String authorization);

    @GET("accounts/{id}")
    Call<Account> getUserById(@Header("Authorization") String authorization, @Path("id") String id);
}
