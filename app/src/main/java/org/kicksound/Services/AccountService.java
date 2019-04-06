package org.kicksound.Services;

import org.kicksound.Models.Account;
import org.kicksound.Models.Login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountService {
    @POST("accounts")
    Call<Account> createAccount(@Body Account account);

    @POST("accounts/login")
    Call<Login> loginAccount(@Body Login login);
}
