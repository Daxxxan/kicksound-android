package org.kicksound.Services;

import org.kicksound.Models.Account;
import org.kicksound.Models.Login;
import org.kicksound.Models.Logout;
import org.kicksound.Models.ResetPassword;

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

    @POST("accounts/logout")
    Call<Logout> logout(@Header("Authorization") String authorization);

    @POST("accounts/reset-password")
    Call<ResetPassword> resetPassword(@Header("Authorization") String authorization, @Body ResetPassword resetPassword);

    @GET("accounts/me")
    Call<Account> accessTokenExist(@Header("Authorization") String authorization);

    @GET("accounts/{id}")
    Call<Account> getUserById(@Header("Authorization") String authorization, @Path("id") String id);
}
