package org.kicksound.Utils.Class;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.kicksound.Controllers.Tabs.TabActivity;
import org.kicksound.Models.Login;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Enums.UserType;
import org.kicksound.Models.Account;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class HandleAccount {
    public static Account userAccount = new Account();

    public static UserType getUserType() {
        UserType userType;
        int accountUserType = userAccount.getType();
        if(accountUserType == 0){
            userType = UserType.USER;
        } else if(accountUserType == 1){
            userType = UserType.ARTIST;
        } else {
            userType = UserType.FAMOUS_ARTIST;
        }
        return userType;
    }

    public static void setUserParameters(String id, String firstname, String lastname, String email, int type, String accessToken, String picture) {
        userAccount.setId(id);
        userAccount.setFirstname(firstname);
        userAccount.setLastname(lastname);
        userAccount.setEmail(email);
        userAccount.setType(type);
        userAccount.setAccessToken(accessToken);
        userAccount.setPicture(picture);
    }

    public static void connection(String userMail, String userPassword, final View v, final ProgressBar loadingBar, final Activity finishActivity, final Context context) {
        if(userMail != null && userPassword != null) {
            loadingBar.setVisibility(View.VISIBLE);
            RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                    .loginAccount(new Login(userMail, userPassword))
                    .enqueue(new Callback<Login>() {
                        @Override
                        public void onResponse(Call<Login> call, Response<Login> response) {
                            if(response.code() == 200) {
                                if (response.body() != null) {
                                    SharedPreferences.Editor editor = v.getContext().getSharedPreferences(v.getContext().getString(R.string.USER_PREF), MODE_PRIVATE).edit();
                                    editor.putString(v.getContext().getString(R.string.userAccessToken), response.body().getId());
                                    editor.apply();
                                    setAccount(response.body().getUserId(), v.getContext(), v.getContext().getString(R.string.account_error), response.body().getId());
                                }
                                loadingBar.setVisibility(View.INVISIBLE);
                                HandleIntent.redirectToAnotherActivity(context, TabActivity.class, v);
                                finishActivity.finish();
                            } else {
                                Toasty.error(v.getContext(), v.getContext().getString(R.string.connection_failed), Toast.LENGTH_SHORT, true).show();
                                loadingBar.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<Login> call, Throwable t) {
                            Toasty.info(v.getContext(), v.getContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
                            loadingBar.setVisibility(View.INVISIBLE);
                        }
                    });
        } else {
            Toasty.error(v.getContext(), v.getContext().getString(R.string.fill_all_fields), Toast.LENGTH_SHORT, true).show();
        }
    }

    public static void setAccount(String userId, final Context context, final String errorMessage, final String accessToken) {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .getUserById(accessToken, userId)
                .enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if(response.code() == 200) {
                            HandleAccount.setUserParameters(response.body().getId(), response.body().getFirstname(), response.body().getLastname(), response.body().getEmail(), response.body().getType(), accessToken, response.body().getPicture());
                        }
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        Toasty.info(context, errorMessage, Toast.LENGTH_SHORT, true).show();
                    }
                });
    }
}
