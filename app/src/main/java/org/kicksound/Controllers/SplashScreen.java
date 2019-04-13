package org.kicksound.Controllers;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import org.kicksound.Controllers.Statics.StaticObjects;
import org.kicksound.Controllers.Tabs.TabActivity;
import org.kicksound.Models.Account;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Class.RetrofitManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        RetrofitManager.getInstance().setUrl(getString(R.string.URL_API));

        Thread timer = new Thread() {
            public void run() {
                SharedPreferences user_pref = getSharedPreferences(getString(R.string.USER_PREF), MODE_PRIVATE);
                String userAccessToken = user_pref.getString(getString(R.string.userAccessToken), null);

                if (userAccessToken != null) {
                    RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                            .accessTokenExist(userAccessToken)
                            .enqueue(new Callback<Account>() {
                                @Override
                                public void onResponse(Call<Account> call, Response<Account> response) {
                                    if(response.code() == 200) {
                                        HandleAccount.setUserParameters(response.body().getId(), response.body().getFirstname(), response.body().getLastname(), response.body().getEmail(), response.body().getType());
                                        HandleIntent.redirectToAnotherActivity(SplashScreen.this, TabActivity.class, findViewById(R.id.splash_screen));
                                    } else {
                                        HandleIntent.redirectToAnotherActivity(SplashScreen.this, MainActivity.class, findViewById(R.id.splash_screen));
                                    }
                                }

                                @Override
                                public void onFailure(Call<Account> call, Throwable t) {
                                    Toasty.info(getApplicationContext(), getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
                                }
                            });
                } else {
                    HandleIntent.redirectToAnotherActivity(SplashScreen.this, MainActivity.class, findViewById(R.id.splash_screen));
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
