package org.kicksound.Controllers.User;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.kicksound.Controllers.Tabs.TabActivity;
import org.kicksound.Models.ResetPassword;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleEditText;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Class.HandleToolbar;
import org.kicksound.Utils.Class.RetrofitManager;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        HandleToolbar.displayToolbar(this, R.string.change_password);
        resetPassword();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void resetPassword() {
        Button resetPassword = findViewById(R.id.button_validate_reset_password);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String password = HandleEditText.fieldIsEmpty(getPasswordEditText(), getApplicationContext());
                String passwordVerification = HandleEditText.fieldIsEmpty( getPasswordVerificationEditText(), getApplicationContext());

                if(password != null && passwordVerification != null) {
                    if(HandleEditText.passwordEqualPasswordVerification(getPasswordEditText(), getPasswordVerificationEditText(), getApplicationContext())) {
                        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                                .resetPassword(
                                        HandleAccount.userAccount.getAccessToken(),
                                        new ResetPassword(password)
                                )
                                .enqueue(new Callback<ResetPassword>() {
                                    @Override
                                    public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {
                                        if(response.code() == 204) {
                                            Toasty.info(getApplicationContext(), getString(R.string.password_modification_success), Toast.LENGTH_SHORT, true).show();
                                            HandleIntent.redirectToAnotherActivity(ResetPasswordActivity.this, TabActivity.class, v);
                                            resetEditTexts();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResetPassword> call, Throwable t) {
                                        Toasty.info(getApplicationContext(), getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
                                    }
                                });
                    }else {
                        Toasty.error(getApplicationContext(), getString(R.string.password_are_different), Toast.LENGTH_SHORT, true).show();
                    }
                } else {
                    Toasty.error(getApplicationContext(), getString(R.string.fill_all_fields), Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    private void resetEditTexts() {
        EditText password = getPasswordEditText();
        EditText passwordVerification = getPasswordVerificationEditText();

        password.setText("");
        passwordVerification.setText("");
    }

    private EditText getPasswordEditText() {
        return findViewById(R.id.new_password);
    }
    private EditText getPasswordVerificationEditText() {
        return findViewById(R.id.verify_new_password);
    }
}
