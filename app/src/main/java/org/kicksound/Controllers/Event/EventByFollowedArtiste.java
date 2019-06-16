package org.kicksound.Controllers.Event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.kicksound.Models.Event;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleToolbar;
import org.kicksound.Utils.Class.RetrofitManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventByFollowedArtiste extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_by_followed_artiste);

        HandleToolbar.displayToolbar(this, R.string.eventByFollowedArtist);
        displayEventByFollowedArtists();
    }

    private void displayEventByFollowedArtists() {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .getEventByFollowedUser(
                        HandleAccount.userAccount.getAccessToken(),
                        HandleAccount.userAccount.getId()
                ).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if(response.body() != null) {
                    if(response.body().size() == 0) {
                        TextView noEventCreatedByFollowedArtists = findViewById(R.id.noEventCreatedByFollowedArtists);
                        noEventCreatedByFollowedArtists.setVisibility(View.VISIBLE);
                    } else {
                        RecyclerView recyclerView = findViewById(R.id.followed_artists_events_recycler_view);
                        EventListAdapter adapter = new EventListAdapter(response.body(), getApplicationContext());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                } else {
                    TextView noEventCreatedByFollowedArtists = findViewById(R.id.noEventCreatedByFollowedArtists);
                    noEventCreatedByFollowedArtists.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
