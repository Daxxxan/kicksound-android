package org.kicksound.Controllers.Event;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.kicksound.Models.Event;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.RetrofitManager;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.kicksound.Utils.Class.HandleEditText.fieldIsEmpty;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        createEvent();
    }

    private void createEvent() {
        Button createEventButton = findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventTitle = fieldIsEmpty(getEventTitleEditText(), getApplicationContext());
                String eventDescription = fieldIsEmpty(getEventDescription(), getApplicationContext());
                String nbTicketString = fieldIsEmpty(getEventTickerNumber(), getApplicationContext());

                if(eventTitle != null && eventDescription != null && nbTicketString != null) {
                    int nbTicket = Integer.parseInt(nbTicketString);
                    RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                            .createEvent(HandleAccount.userAccount.getAccessToken(),
                                    HandleAccount.userAccount.getId(),
                                    new Event(eventTitle, eventDescription, nbTicket))
                            .enqueue(new Callback<Event>() {
                                @Override
                                public void onResponse(Call<Event> call, Response<Event> response) {
                                    if(response.code() == 200) {
                                        Toasty.success(getApplicationContext(), "OK", Toast.LENGTH_SHORT, true).show();
                                    } else {
                                        Toasty.error(getApplicationContext(), "NOK", Toast.LENGTH_SHORT, true).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Event> call, Throwable t) {
                                    Toasty.error(getApplicationContext(), "=(", Toast.LENGTH_SHORT, true).show();
                                }
                            });
                }
            }
        });
    }

    private EditText getEventTickerNumber() {
        return findViewById(R.id.ticketNumber);
    }

    private EditText getEventDescription() {
        return findViewById(R.id.descriptionEvent);
    }

    private EditText getEventTitleEditText() {
        return findViewById(R.id.eventName);
    }
}
