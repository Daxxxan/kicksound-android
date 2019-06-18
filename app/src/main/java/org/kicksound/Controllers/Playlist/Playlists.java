package org.kicksound.Controllers.Playlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.kicksound.Models.Playlist;
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

public class Playlists extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);

        HandleToolbar.displayToolbar(this, R.string.playlists);
        createPlaylist();
        displayPlaylist();
    }

    private void displayPlaylist() {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .getPlaylist(
                        HandleAccount.userAccount.getAccessToken(),
                        HandleAccount.userAccount.getId()
                ).enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                RecyclerView recyclerView = findViewById(R.id.playlistRecyclerView);
                PlaylistAdapter adapter = new PlaylistAdapter(response.body(), getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    private void createPlaylist() {
        final ImageButton createPlaylist = findViewById(R.id.createPlaylist);
        final EditText createPlaylistEditText = findViewById(R.id.createPlaylistEditText);
        createPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!createPlaylistEditText.getText().toString().matches("")) {
                    createPlaylistRequest(createPlaylistEditText.getText().toString());
                    createPlaylistEditText.setText("");
                    displayPlaylist();
                }
            }
        });
    }

    private void createPlaylistRequest(String playlistName) {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .createPlaylist(
                        HandleAccount.userAccount.getAccessToken(),
                        HandleAccount.userAccount.getId(),
                        new org.kicksound.Models.Playlist(playlistName)
                ).enqueue(new Callback<org.kicksound.Models.Playlist>() {
            @Override
            public void onResponse(Call<org.kicksound.Models.Playlist> call, Response<org.kicksound.Models.Playlist> response) {
                Toasty.success(getApplicationContext(), getApplicationContext().getString(R.string.successfullyCreatePlaylist), Toast.LENGTH_SHORT, true).show();
            }

            @Override
            public void onFailure(Call<org.kicksound.Models.Playlist> call, Throwable t) {
                Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.createEventError), Toast.LENGTH_SHORT, true).show();
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
