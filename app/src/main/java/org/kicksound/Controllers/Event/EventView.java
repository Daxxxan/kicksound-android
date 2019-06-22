package org.kicksound.Controllers.Event;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;

import org.kicksound.Models.Event;
import org.kicksound.Models.EventTicketsCount;
import org.kicksound.Models.Ticket;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Services.EventService;
import org.kicksound.Utils.Class.FileUtil;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Class.HandleToolbar;
import org.kicksound.Utils.Class.RetrofitManager;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventView extends AppCompatActivity {

    private Event event = new Event();
    private Uri selectedImage = null;
    private File eventPictureFile = null;
    private EditText titleEventName = null;
    private TextView titleEventDate = null;
    private EditText eventDescription = null;
    private EditText eventTicketNumber = null;
    private ImageView eventPicture = null;
    private FloatingActionButton validateEventModification = null;
    private TextView titleEventNameTextView = null;
    private TextView eventDescriptionTextView = null;
    private TextView eventTicketNumberTextView = null;
    private Button ticketButton = null;
    private ImageButton modifyEventPicture = null;

    private Date date = null;
    private DateFormat dateFormat = new SimpleDateFormat("dd / MM / yyyy");

    private Boolean titleState = false;
    private Boolean dateState = false;
    private Boolean ticketsState = false;
    private Boolean descriptionState = false;
    private Boolean pictureState = false;

    private static final int PICK_IMAGE_FROM_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        String eventId = getIntent().getStringExtra("eventId");

        HandleToolbar.displayToolbar(this, null);
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
        modifyEventPicture = findViewById(R.id.modifyEventPicture);

        titleEventNameTextView = findViewById(R.id.titleEventNameTextView);
        eventDescriptionTextView = findViewById(R.id.event_descriptionTextView);
        eventTicketNumberTextView = findViewById(R.id.ticketNumberEventTextView);

        ticketButton = findViewById(R.id.ticketButton);

        setValidateEventModificationBehavior();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            FileUtil.pickImageFromGallery(getApplicationContext(), EventView.this, PICK_IMAGE_FROM_GALLERY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            eventPictureFile = new File(FileUtil.getPath(selectedImage, getApplicationContext()));

            FileUtil.displayCircleImageWithUri(getApplicationContext(), selectedImage, eventPicture);

            if(!eventPictureFile.getName().equals(event.getPicture())) {
                pictureState = true;
            } else {
                pictureState = false;
            }

            displayFabIfEventHasBeenModified();
        }
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

                if(pictureState) {
                    FileUtil.uploadFile(selectedImage, getApplicationContext(), "image");
                    event.setPicture(eventPictureFile.getName());
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
        RetrofitManager.getInstance().getRetrofit().create(EventService.class)
                .getEventById(
                        HandleAccount.userAccount.getAccessToken(),
                        eventId
                ).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                event = response.body();
                makeViewsVisible(String.valueOf(event.getAccountId()));
                FileUtil.displayPicture("image", event.getPicture(), eventPicture, getApplicationContext());
                eventDate();
                eventTitle(String.valueOf(event.getAccountId()));
                eventDescription(String.valueOf(event.getAccountId()));
                eventTicketNumber(String.valueOf(event.getAccountId()));
                modifyEventPicture(String.valueOf(event.getAccountId()));
                displayTicketButton(eventId, String.valueOf(event.getAccountId()));
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    private void makeViewsVisible(String creatorId) {
        if(HandleAccount.userAccount.getId().equals(creatorId)) {
            titleEventName.setVisibility(View.VISIBLE);
            eventDescription.setVisibility(View.VISIBLE);
            eventTicketNumber.setVisibility(View.VISIBLE);
        } else {
            titleEventNameTextView.setVisibility(View.VISIBLE);
            eventDescriptionTextView.setVisibility(View.VISIBLE);
            eventTicketNumberTextView.setVisibility(View.VISIBLE);
        }
    }

    private void eventTicketNumber(String creatorId) {
        if(HandleAccount.userAccount.getId().equals(creatorId)) {
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
        } else {
            eventTicketNumberTextView.setText(String.valueOf(event.getTicketsNumber()));
        }
    }

    private void eventDescription(String creatorId) {
        if(HandleAccount.userAccount.getId().equals(creatorId)) {
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
        } else {
            eventDescriptionTextView.setText(event.getDescription());
        }
    }

    private void modifyEventPicture(String creatorId) {
        if(HandleAccount.userAccount.getId().equals(creatorId)) {
            modifyEventPicture.setVisibility(View.VISIBLE);
            modifyEventPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FileUtil.pickImageFromGallery(getApplicationContext(), EventView.this, PICK_IMAGE_FROM_GALLERY);
                }
            });
        }
    }

    private void displayTicketButton(String eventId, String creatorId) {
        if(HandleAccount.userAccount.getId().equals(creatorId)) {
            getParticipants(eventId);
        } else {
            userParticipate(eventId, creatorId);
        }
    }

    private void eventParticipation(final String eventId, final String creatorId) {
        RetrofitManager.getInstance().getRetrofit().create(EventService.class)
                .getNumberOfTicketsBought(
                        HandleAccount.userAccount.getAccessToken(),
                        eventId
                ).enqueue(new Callback<EventTicketsCount>() {
            @Override
            public void onResponse(Call<EventTicketsCount> call, Response<EventTicketsCount> response) {
                if(response.body() != null && event.getTicketsNumber() == response.body().getCount()) {
                    ticketButton.setText(getApplicationContext().getString(R.string.notEnoughTickets));
                    ticketButton.setEnabled(false);
                } else {
                    ticketButton.setText(getApplicationContext().getString(R.string.participateToEvent));
                    participate(eventId, creatorId);
                }
            }

            @Override
            public void onFailure(Call<EventTicketsCount> call, Throwable t) {
                Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    private void userParticipate(final String eventId, final String creatorId) {
        RetrofitManager.getInstance().getRetrofit().create(EventService.class)
                .userParticipateToEvent(
                        HandleAccount.userAccount.getAccessToken(),
                        eventId,
                        HandleAccount.userAccount.getId()
                ).enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                if(response.body() == null || response.body().isEmpty()) {
                    eventParticipation(eventId, creatorId);
                } else {
                    ticketButton.setText(getApplicationContext().getString(R.string.stopParticipate));
                    cancelEventParticipation(eventId, creatorId);
                }
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    private void cancelEventParticipation(final String eventId, final String creatorId) {
        ticketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitManager.getInstance().getRetrofit().create(EventService.class)
                        .deleteEventParticipation(
                                HandleAccount.userAccount.getAccessToken(),
                                eventId,
                                HandleAccount.userAccount.getId()
                        )
                        .enqueue(new Callback<List<Ticket>>() {
                            @Override
                            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                                ticketButton.setText(getApplicationContext().getString(R.string.participateToEvent));
                                eventParticipation(eventId, creatorId);
                                Toasty.success(getApplicationContext(), getApplicationContext().getString(R.string.dontParticipateToEvent), Toast.LENGTH_SHORT, true).show();

                            }

                            @Override
                            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                                Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.updateEventError), Toast.LENGTH_SHORT, true).show();
                            }
                        });
            }
        });
    }

    private void participate(final String eventId, final String creatorId) {
        ticketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitManager.getInstance().getRetrofit().create(EventService.class)
                        .participateToEvent(
                                HandleAccount.userAccount.getAccessToken(),
                                eventId,
                                new Ticket(15.8, HandleAccount.userAccount.getId())
                        ).enqueue(new Callback<Ticket>() {
                    @Override
                    public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                        if(response.code() == 200) {
                            ticketButton.setText(getApplicationContext().getString(R.string.stopParticipate));
                            cancelEventParticipation(eventId, creatorId);
                            Toasty.success(getApplicationContext(), getApplicationContext().getString(R.string.participateToEventText), Toast.LENGTH_SHORT, true).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Ticket> call, Throwable t) {
                        Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.updateEventError), Toast.LENGTH_SHORT, true).show();
                    }
                });
            }
        });
    }

    private void getParticipants(final String eventId) {
        ticketButton.setText(getApplicationContext().getString(R.string.showParticipants));
        ticketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleIntent.redirectToAnotherActivityWithExtra(getApplicationContext(), EventParticipants.class, v, "eventId", eventId);
            }
        });
    }

    private void eventTitle(String creatorId) {
        if(HandleAccount.userAccount.getId().equals(creatorId)) {
            titleEventName.setText(event.getTitle());
            if(getSupportActionBar() != null)
                getSupportActionBar().setTitle(event.getTitle());
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
        } else {
            titleEventNameTextView.setText(event.getTitle());
            if(getSupportActionBar() != null)
                getSupportActionBar().setTitle(event.getTitle());
        }
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
        return titleState || dateState || descriptionState || ticketsState || pictureState;
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
