package org.kicksound.Controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.kicksound.R;
import org.kicksound.Utils.HandleIntent;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

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
            public void onClick(View v) {
                String userMail = fieldIsEmpty(getUserMailEditText(), getApplicationContext());
                String userPassword = fieldIsEmpty(getUserPasswordEditText(), getApplicationContext());
                if(userMail != null && userPassword != null) {
                    //Allow user connection
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
