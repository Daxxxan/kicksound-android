package org.kicksound.Controllers.Song;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import org.kicksound.Models.Account;
import org.kicksound.Models.Music;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Services.MusicService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleToolbar;
import org.kicksound.Utils.Class.RetrofitManager;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistMusics extends AppCompatActivity {
    private String userId = null;
    private MediaPlayer mediaPlayer = null;
    private Handler seekbarUpdateHandler = null;
    private Runnable updateSeekbar = null;
    private SeekBar seekBar = null;
    private TextView musicNameStarted = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_musics);
        userId = getIntent().getStringExtra("userId");
        HandleToolbar.displayToolbar(this, null);

        musicNameStarted = findViewById(R.id.musicNameStarted);

        setMediaPlayer();
        displayToolbarTitle();
        displayArtistMusics();
    }

    private void setMediaPlayer() {
        seekBar = findViewById(R.id.seekBar);
        mediaPlayer = new MediaPlayer();
        seekbarUpdateHandler = new Handler();
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarUpdateHandler.postDelayed(this, 1000);
            }
        };
    }

    private void displayToolbarTitle() {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .getUserById(
                        HandleAccount.userAccount.getAccessToken(),
                        userId
                ).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.body() != null)
                    getSupportActionBar().setTitle("Titres de " + response.body().getUsername());
                else
                    getSupportActionBar().setTitle("Titres");
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });
    }

    private void displayArtistMusics() {
        RetrofitManager.getInstance().getRetrofit().create(MusicService.class)
                .getMusicByArtistId(
                        HandleAccount.userAccount.getAccessToken(),
                        userId
                ).enqueue(new Callback<List<Music>>() {
            @Override
            public void onResponse(Call<List<Music>> call, Response<List<Music>> response) {
                RecyclerView recyclerView = findViewById(R.id.artistMusicsRecyclerView);
                MusicAdapter adapter = new MusicAdapter(response.body(), ArtistMusics.this, getApplicationContext(),
                        mediaPlayer, seekbarUpdateHandler,
                        updateSeekbar, seekBar, musicNameStarted);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<Music>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mediaPlayer.stop();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onPrepared(MediaPlayer player) {
        player.start();
    }
}
