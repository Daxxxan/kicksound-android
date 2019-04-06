package org.kicksound.Controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.kicksound.Models.Account;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.RetrofitManager;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.kicksound.Utils.HandleEditText.fieldIsEmpty;
import static org.kicksound.Utils.HandleEditText.passwordEqualPasswordVerification;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registration();
    }

    private void registration() {
        final Button registrationButton = findViewById(R.id.registrationButton);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = fieldIsEmpty(getFirstnameEditText(), getApplicationContext());
                String lastname = fieldIsEmpty(getLastnameEditText(), getApplicationContext());
                String email = fieldIsEmpty(getEmailEditText(), getApplicationContext());
                String password = fieldIsEmpty(getPasswordEditText(), getApplicationContext());
                String passwordVerification = fieldIsEmpty(getPasswordVerificationEditText(), getApplicationContext());
                if(firstname != null && lastname != null && email != null && password != null && passwordVerification != null) {
                    if(passwordEqualPasswordVerification(getPasswordEditText(), getPasswordVerificationEditText(), getApplicationContext())) {
                        RetrofitManager.getInstance().setUrl(getString(R.string.URL_API));

                        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                                .createAccount(new Account(firstname, lastname, email, password))
                                .enqueue(new Callback<Account>() {
                                    @Override
                                    public void onResponse(Call<Account> call, Response<Account> response) {
                                        System.out.println("onResponse");
                                        System.out.println(response.body().toString());
                                    }

                                    @Override
                                    public void onFailure(Call<Account> call, Throwable t) {
                                        System.out.println("OnFailure");
                                        System.out.println(t.getMessage());
                                        System.out.println(call.request());
                                    }
                                });
                    } else {
                        Toasty.error(getApplicationContext(), getString(R.string.password_are_different), Toast.LENGTH_SHORT, true).show();
                    }
                } else {
                    Toasty.error(getApplicationContext(), getString(R.string.fill_all_fields), Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    private EditText getPasswordVerificationEditText() {
        return findViewById(R.id.password_verification);
    }

    private EditText getPasswordEditText() {
        return findViewById(R.id.password);
    }

    private EditText getEmailEditText() {
        return findViewById(R.id.email);
    }

    private EditText getLastnameEditText() {
        return findViewById(R.id.lastname);
    }

    private EditText getFirstnameEditText() {
        return findViewById(R.id.firstname);
    }
}
