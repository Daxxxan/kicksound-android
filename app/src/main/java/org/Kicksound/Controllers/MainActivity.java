package org.Kicksound.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.Kicksound.Exceptions.UserConnectionException;
import org.Kicksound.R;
import org.Kicksound.Utils.HandleIntent;

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
                try {
                    String userMail = fieldIsEmpty(getUserMail());
                    String userPassword = fieldIsEmpty(getUserPassword());
                    System.out.println("User mail: " + userMail + ", User password: " + userPassword);
                    //Http request /User/login
                } catch (UserConnectionException ex) {
                    //Afficher un message d'erreur a l'utilisateur
                    Log.e("Connection failed", ex.getMessage());
                }
            }
        });
    }

    private String fieldIsEmpty(String field) throws UserConnectionException {
        if(field.matches("")) {
            throw new UserConnectionException("You need to fill the field");
        }
        return field;
    }

    private String getUserMail() {
        EditText userMailEditText = findViewById(R.id.email_edit_text);
        return userMailEditText.getText().toString();
    }

    private String getUserPassword() {
        EditText userPasswordEditText = findViewById(R.id.password_edit_text);
        return userPasswordEditText.getText().toString();
    }
}
