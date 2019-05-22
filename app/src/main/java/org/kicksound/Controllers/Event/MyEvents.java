package org.kicksound.Controllers.Event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.kicksound.Controllers.Search.SearchListAdapter;
import org.kicksound.Models.Account;
import org.kicksound.Models.Event;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.RetrofitManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .getUserEvents(
                        HandleAccount.userAccount.getAccessToken(),
                        HandleAccount.userAccount.getId())
                .enqueue(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        RecyclerView recyclerView = findViewById(R.id.event_recycler_view);
                        EventListAdapter adapter = new EventListAdapter(response.body(), getApplicationContext());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, Throwable t) {

                    }
                });
    }
}
