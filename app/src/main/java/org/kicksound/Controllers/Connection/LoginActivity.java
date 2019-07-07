package org.kicksound.Controllers.Connection;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.kicksound.R;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;

import androidx.appcompat.app.AppCompatActivity;

import static org.kicksound.Utils.Class.HandleEditText.fieldIsEmpty;

public class LoginActivity extends AppCompatActivity {
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
                HandleIntent.redirectToAnotherActivity(LoginActivity.this, RegistrationActivity.class, v);
            }
        });
    }

    private void connection() {
        final Button connectionButton = findViewById(R.id.connection);
        final ProgressBar loadingBar = findViewById(R.id.loading_progress);
        connectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String userMail = fieldIsEmpty(getUserMailEditText(), getApplicationContext());
                String userPassword = fieldIsEmpty(getUserPasswordEditText(), getApplicationContext());
                HandleAccount.connection(userMail, userPassword, v, loadingBar, LoginActivity.this, LoginActivity.this);
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
