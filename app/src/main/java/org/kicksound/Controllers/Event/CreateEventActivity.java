package org.kicksound.Controllers.Event;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.kicksound.Controllers.Tabs.TabActivity;
import org.kicksound.Controllers.User.UserPicture;
import org.kicksound.Models.Event;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.FileUtil;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Class.RetrofitManager;

import java.io.File;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.kicksound.Utils.Class.HandleEditText.fieldIsEmpty;

public class CreateEventActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_FROM_GALLERY = 1;
    private ImageView eventImageView;
    private Uri selectedImage = null;
    private File eventPicture = null;
    private Event event = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        eventImageView = findViewById(R.id.eventPicture);
        createEvent();
        selectEventPicture();
    }

    private void createEvent() {
        Button createEventButton = findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String eventTitle = fieldIsEmpty(getEventTitleEditText(), getApplicationContext());
                String eventDescription = fieldIsEmpty(getEventDescription(), getApplicationContext());
                String nbTicketString = fieldIsEmpty(getEventTickerNumber(), getApplicationContext());

                if(eventTitle != null && eventDescription != null && nbTicketString != null) {
                    int nbTicket = Integer.parseInt(nbTicketString);

                    if(selectedImage != null) {
                        FileUtil.uploadFile(selectedImage, getApplicationContext(), "event");
                        event = new Event(eventTitle, eventDescription, nbTicket, eventPicture.getName());
                    } else {
                        event = new Event(eventTitle, eventDescription, nbTicket);
                    }

                    RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                            .createEvent(HandleAccount.userAccount.getAccessToken(),
                                    HandleAccount.userAccount.getId(),
                                    event)
                            .enqueue(new Callback<Event>() {
                                @Override
                                public void onResponse(Call<Event> call, Response<Event> response) {
                                    if(response.code() == 200) {
                                        HandleIntent.redirectToAnotherActivity(CreateEventActivity.this, TabActivity.class, v);
                                        Toasty.success(getApplicationContext(), v.getContext().getString(R.string.succes_create_event), Toast.LENGTH_SHORT, true).show();
                                    } else {
                                        Toasty.error(getApplicationContext(), v.getContext().getString(R.string.createEventError), Toast.LENGTH_SHORT, true).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Event> call, Throwable t) {
                                    Toasty.error(getApplicationContext(), v.getContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
                                }
                            });
                }
            }
        });
    }

    private void selectEventPicture() {
        Button addEventPicture = findViewById(R.id.addEventPicture);
        addEventPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.pickImageFromGallery(getApplicationContext(), CreateEventActivity.this, PICK_IMAGE_FROM_GALLERY);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            FileUtil.pickImageFromGallery(getApplicationContext(), CreateEventActivity.this, PICK_IMAGE_FROM_GALLERY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            eventPicture = new File(FileUtil.getPath(selectedImage, getApplicationContext()));

            Picasso.get().load(selectedImage).into(eventImageView);
        }
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
