package org.kicksound.Controllers.Discovery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import org.kicksound.Controllers.Search.SearchListAdapter;
import org.kicksound.Controllers.Song.ArtistMusics;
import org.kicksound.Controllers.Song.MusicAdapter;
import org.kicksound.Models.Account;
import org.kicksound.Models.MusicKind;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Services.MusicKindService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleToolbar;
import org.kicksound.Utils.Class.RetrofitManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Discover extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        HandleToolbar.displayToolbar(this, R.string.discover);
        displayMusicKind();
        displayUnknownArtists();
    }

    private void displayUnknownArtists() {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .getUnknownArtistsByArtistFollowed(
                        HandleAccount.userAccount.getAccessToken(),
                        HandleAccount.userAccount.getId()
                ).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                RecyclerView recyclerView = findViewById(R.id.unknownArtistsRecyclerView);
                SearchListAdapter adapter = new SearchListAdapter(response.body(), getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {

            }
        });
    }

    private void displayMusicKind() {
        RetrofitManager.getInstance().getRetrofit().create(MusicKindService.class)
                .getMusicKinds(
                        HandleAccount.userAccount.getAccessToken()
                ).enqueue(new Callback<List<MusicKind>>() {
            @Override
            public void onResponse(Call<List<MusicKind>> call, Response<List<MusicKind>> response) {
                RecyclerView recyclerView = findViewById(R.id.musicKindRecylerView);
                MusicKindAdapter adapter = new MusicKindAdapter(response.body(), getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            }

            @Override
            public void onFailure(Call<List<MusicKind>> call, Throwable t) {
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
