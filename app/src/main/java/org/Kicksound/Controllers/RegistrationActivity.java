package org.Kicksound.Controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.Kicksound.R;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import static org.Kicksound.Utils.HandleEditText.fieldIsEmpty;
import static org.Kicksound.Utils.HandleEditText.passwordEqualPasswordVerification;

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
                        //HTTP request to create account
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
