package org.kicksound.Controllers.Event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.kicksound.Controllers.Search.SearchListAdapter;
import org.kicksound.Models.Account;
import org.kicksound.Models.Event;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Services.EventService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleToolbar;
import org.kicksound.Utils.Class.RetrofitManager;

import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventParticipants extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_participants);

        String eventId = getIntent().getStringExtra("eventId");

        HandleToolbar.displayToolbar(this, R.string.participants);
        displayEventParticipants(eventId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayEventParticipants(String eventId) {
        RetrofitManager.getInstance().getRetrofit().create(EventService.class)
                .getEventParticipants(
                        HandleAccount.userAccount.getAccessToken(),
                        eventId)
                .enqueue(new Callback<List<Account>>() {
                    @Override
                    public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                        if(response.body() != null) {
                            if(response.body().size() == 0) {
                                TextView noParticipant = findViewById(R.id.noParticipant);
                                noParticipant.setVisibility(View.VISIBLE);
                            } else {
                                RecyclerView recyclerView = findViewById(R.id.participants_recycler_view);
                                SearchListAdapter adapter = new SearchListAdapter(response.body(), getApplicationContext());
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            }
                        } else {
                            TextView noParticipant = findViewById(R.id.noParticipant);
                            noParticipant.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Account>> call, Throwable t) {
                        Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }
}
