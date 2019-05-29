package org.kicksound.Controllers.Event;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;

import org.kicksound.Controllers.Tabs.TabActivity;
import org.kicksound.Models.Event;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.FileUtil;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Class.RetrofitManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventView extends AppCompatActivity {

    private Event event = new Event();
    private EditText titleEventName = null;
    private TextView titleEventDate = null;
    private EditText eventDescription = null;
    private EditText eventTicketNumber = null;
    private ImageView eventPicture = null;
    private FloatingActionButton validateEventModification = null;

    private Date date = null;
    private DateFormat dateFormat = new SimpleDateFormat("dd / MM / yyyy");

    private Boolean titleState = false;
    private Boolean dateState = false;
    private Boolean ticketsState = false;
    private Boolean descriptionState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        String eventId = getIntent().getStringExtra("eventId");

        setViewComponents();
        displayEvent(eventId);
    }

    private void setViewComponents() {
        titleEventName = findViewById(R.id.titleEventName);
        titleEventDate = findViewById(R.id.titleEventDate);
        eventPicture = findViewById(R.id.event_pic);
        eventDescription = findViewById(R.id.event_description);
        eventTicketNumber = findViewById(R.id.ticketNumberEvent);
        validateEventModification = findViewById(R.id.validateEventModification);

        setValidateEventModificationBehavior();
    }

    private void setValidateEventModificationBehavior() {
        validateEventModification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(titleState) {
                    event.setTitle(titleEventName.getText().toString());
                }

                if(dateState) {
                    event.setDate(date);
                }

                if(ticketsState) {
                    event.setTicketsNumber(Integer.parseInt(eventTicketNumber.getText().toString()));
                }

                if(descriptionState) {
                    event.setDescription(eventDescription.getText().toString());
                }

                RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                        .updateEvent(
                                HandleAccount.userAccount.getAccessToken(),
                                HandleAccount.userAccount.getId(),
                                event.getId(),
                                event
                        ).enqueue(new Callback<Event>() {
                    @Override
                    public void onResponse(Call<Event> call, Response<Event> response) {
                        if(response.code() == 200) {
                            HandleIntent.redirectToAnotherActivity(EventView.this, MyEvents.class, v);
                            Toasty.success(getApplicationContext(), v.getContext().getString(R.string.eventModificationSuccess), Toast.LENGTH_SHORT, true).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Event> call, Throwable t) {
                        Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.updateEventError), Toast.LENGTH_SHORT, true).show();
                    }
                });
            }
        });
    }

    private void displayEvent(final String eventId) {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .getEventById(
                        HandleAccount.userAccount.getAccessToken(),
                        HandleAccount.userAccount.getId(),
                        eventId
                ).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                event = response.body();
                FileUtil.displayPicture("event", event.getPicture(), eventPicture, getApplicationContext());
                eventDate();
                eventTitle();
                eventDescription();
                eventTicketNumber();
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    private void eventTicketNumber() {
        eventTicketNumber.setText(String.valueOf(event.getTicketsNumber()));
        eventTicketNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ticketsState = textChanged(String.valueOf(event.getTicketsNumber()), s);
                displayFabIfEventHasBeenModified();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void eventDescription() {
        eventDescription.setText(event.getDescription());
        eventDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                descriptionState = textChanged(event.getDescription(), s);
                displayFabIfEventHasBeenModified();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void eventTitle() {
        titleEventName.setText(event.getTitle());
        titleEventName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                titleState = textChanged(event.getTitle(), s);
                displayFabIfEventHasBeenModified();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void eventDate() {
        titleEventDate.setText(dateFormat.format(event.getDate()));
        titleEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEventDatePickerDialog();
            }
        });
    }

    private Boolean eventHasBeenModified() {
        if(titleState || dateState || descriptionState || ticketsState ) {
            return true;
        }
        return false;
    }

    private Boolean textChanged(String eventAttribute, CharSequence s) {
        boolean state = false;

        if(!eventAttribute.equals(s.toString())) {
            state = true;
        }

        return state;
    }

    private void displayFabIfEventHasBeenModified() {
        if(eventHasBeenModified()) {
            validateEventModification.setVisibility(View.VISIBLE);
        } else {
            validateEventModification.setVisibility(View.GONE);
        }
    }

    private void setEventDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(EventView.this,  R.style.Theme_AppCompat_DayNight_Dialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        date = calendar.getTime();
                        titleEventDate.setText(dateFormat.format(date));

                        dateState = !dateFormat.format(date).equals(dateFormat.format(event.getDate()));

                        displayFabIfEventHasBeenModified();
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
