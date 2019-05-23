package org.kicksound.Controllers.Event;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.kicksound.Models.Event;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.FileUtil;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.RetrofitManager;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventView extends AppCompatActivity {

    private Event event = new Event();
    private TextView titleEventName = null;
    private ImageView eventPicture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);
        String eventId = getIntent().getStringExtra("eventId");

        titleEventName = findViewById(R.id.titleEventName);
        eventPicture = findViewById(R.id.event_pic);

        displayEvent(eventId);
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
                FileUtil.downloadFileAndDisplay("event", event.getPicture(), eventPicture, getApplicationContext());
                titleEventName.setText(event.getTitle());
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
