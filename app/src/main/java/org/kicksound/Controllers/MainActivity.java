package org.kicksound.Controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.kicksound.Controllers.Tabs.TabActivity;
import org.kicksound.Models.Account;
import org.kicksound.Models.Login;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Class.RetrofitManager;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.kicksound.Utils.Class.HandleEditText.fieldIsEmpty;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connection();
        registration();
    }

    private void registration() {
        final TextView registrationTextView = findViewById(R.id.register);
        registrationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleIntent.redirectToAnotherActivity(MainActivity.this, RegistrationActivity.class, v);
            }
        });
    }

    private void connection() {
        final Button connectionButton = findViewById(R.id.connection);
        connectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String userMail = fieldIsEmpty(getUserMailEditText(), getApplicationContext());
                String userPassword = fieldIsEmpty(getUserPasswordEditText(), getApplicationContext());
                if(userMail != null && userPassword != null) {
                    RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                            .loginAccount(new Login(userMail, userPassword))
                            .enqueue(new Callback<Login>() {
                                @Override
                                public void onResponse(Call<Login> call, Response<Login> response) {
                                    if(response.code() == 200) {
                                        if (response.body() != null) {
                                            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.USER_PREF), MODE_PRIVATE).edit();
                                            editor.putString(getString(R.string.userAccessToken), response.body().getId());
                                            editor.apply();
                                            setAccount(response.body().getUserId(), getApplicationContext(), getString(R.string.account_error), response.body().getId());
                                        }
                                        HandleIntent.redirectToAnotherActivity(MainActivity.this, TabActivity.class, v);
                                        finish();
                                    } else {
                                        Toasty.error(getApplicationContext(), getString(R.string.connection_failed), Toast.LENGTH_SHORT, true).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Login> call, Throwable t) {
                                    Toasty.info(getApplicationContext(), getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
                                }
                            });
                } else {
                    Toasty.error(getApplicationContext(), getString(R.string.fill_all_fields), Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    private void setAccount(String userId, final Context context, final String errorMessage, final String accessToken) {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .getUserById(accessToken, userId)
                .enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if(response.code() == 200) {
                            HandleAccount.setUserParameters(response.body().getId(), response.body().getFirstname(), response.body().getLastname(), response.body().getEmail(), response.body().getType(), accessToken);
                        }
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        Toasty.info(context, errorMessage, Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    private EditText getUserMailEditText() {
        return findViewById(R.id.email_edit_text);
    }

    private EditText getUserPasswordEditText() {
        return findViewById(R.id.password_edit_text);
    }
}
