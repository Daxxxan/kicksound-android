package org.kicksound.Controllers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.kicksound.Models.Login;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.HandleIntent;
import org.kicksound.Utils.RetrofitManager;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.kicksound.Utils.HandleEditText.fieldIsEmpty;

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
                                            Login.getLogin().setId(response.body().getId());
                                            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.USER_PREF), MODE_PRIVATE).edit();
                                            editor.putString(getString(R.string.userAccessToken), response.body().getId());
                                            editor.apply();
                                        }
                                        HandleIntent.redirectToAnotherActivity(MainActivity.this, News.class, v);
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

    private EditText getUserMailEditText() {
        return findViewById(R.id.email_edit_text);
    }

    private EditText getUserPasswordEditText() {
        return findViewById(R.id.password_edit_text);
    }
}
