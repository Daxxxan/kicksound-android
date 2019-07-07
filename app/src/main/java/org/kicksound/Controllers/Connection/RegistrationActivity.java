package org.kicksound.Controllers.Connection;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.kicksound.Controllers.Event.EventListAdapter;
import org.kicksound.Models.Account;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.RetrofitManager;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.kicksound.Utils.Class.HandleEditText.fieldIsEmpty;
import static org.kicksound.Utils.Class.HandleEditText.passwordEqualPasswordVerification;

public class RegistrationActivity extends AppCompatActivity {

    private Button registrationButton = null;
    private ProgressBar loadingBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationButton = findViewById(R.id.registrationButton);
        loadingBar = findViewById(R.id.loading_progress_register);

        registration();
    }

    private void registration() {
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String username = fieldIsEmpty(getUsernameEditText(), getApplicationContext());
                final String email = fieldIsEmpty(getEmailEditText(), getApplicationContext());
                final String password = fieldIsEmpty(getPasswordEditText(), getApplicationContext());
                String passwordVerification = fieldIsEmpty(getPasswordVerificationEditText(), getApplicationContext());

                if(username != null && email != null && password != null && passwordVerification != null) {
                    if(passwordEqualPasswordVerification(getPasswordEditText(), getPasswordVerificationEditText(), getApplicationContext())) {
                        loadingBar.setVisibility(View.VISIBLE);
                        registerUser(username, email, password, v);
                    } else {
                        Toasty.error(getApplicationContext(), getString(R.string.password_are_different), Toast.LENGTH_SHORT, true).show();
                    }
                } else {
                    Toasty.error(getApplicationContext(), getString(R.string.fill_all_fields), Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    private void registerUser(String username, final String email, final String password, final View v) {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .createAccount(new Account(username, email, password))
                .enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if(response.code() == 200) {
                            HandleAccount.connection(email, password, v, loadingBar, RegistrationActivity.this, RegistrationActivity.this);
                            Toasty.success(getApplicationContext(), getString(R.string.account_created), Toast.LENGTH_SHORT, true).show();
                        } else {
                            loadingBar.setVisibility(View.INVISIBLE);
                            Toasty.error(getApplicationContext(), getString(R.string.wrong_mail), Toast.LENGTH_SHORT, true).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        loadingBar.setVisibility(View.INVISIBLE);
                        Toasty.info(getApplicationContext(), getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
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

    private EditText getUsernameEditText() {
        return findViewById(R.id.username);
    }
}
