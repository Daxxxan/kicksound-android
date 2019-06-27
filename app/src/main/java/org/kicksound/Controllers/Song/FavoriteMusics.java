package org.kicksound.Controllers.Song;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.kicksound.Models.Music;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Services.PlaylistService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleToolbar;
import org.kicksound.Utils.Class.RetrofitManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteMusics extends AppCompatActivity {

    private MediaPlayer mediaPlayer = null;
    private Handler seekbarUpdateHandler = null;
    private Runnable updateSeekbar = null;
    private SeekBar seekBar = null;
    private TextView musicNameStarted = null;
    private ImageButton forward = null;
    private ImageButton rewind = null;
    private ProgressBar progressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musics);

        String playlistId = getIntent().getStringExtra("playlistId");

        HandleToolbar.displayToolbar(this, R.string.titles);
        displayFavoriteMusics(playlistId);
        setMediaPlayer();
        setMusicComponents();
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

    private void setMusicComponents() {
        musicNameStarted = findViewById(R.id.musicNameStarted);
        forward = findViewById(R.id.forward);
        rewind = findViewById(R.id.rewind);
        progressBar = findViewById(R.id.progressBarLoadFavoriteMusic);
    }

    private void displayFavoriteMusics(String playlistId) {
        if(playlistId == null ){
            getFavoritesMusics();
        } else {
            getMusicsFromPlaylist(playlistId);
        }
    }

    private void getMusicsFromPlaylist(String playlistId) {
        RetrofitManager.getInstance().getRetrofit().create(PlaylistService.class)
                .getMusicFromPlaylist(
                        HandleAccount.userAccount.getAccessToken(),
                        playlistId
                ).enqueue(new Callback<List<Music>>() {
            @Override
            public void onResponse(Call<List<Music>> call, Response<List<Music>> response) {
                RecyclerView recyclerView = findViewById(R.id.songsRecyclerView);
                MusicAdapter adapter = new MusicAdapter(response.body(), FavoriteMusics.this, getApplicationContext(),
                        mediaPlayer, seekbarUpdateHandler, updateSeekbar, seekBar, musicNameStarted, forward, rewind, progressBar);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<Music>> call, Throwable t) {
                Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    private void getFavoritesMusics() {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .getArtistFavoriteMusics(
                        HandleAccount.userAccount.getAccessToken(),
                        HandleAccount.userAccount.getId()
                ).enqueue(new Callback<List<Music>>() {
            @Override
            public void onResponse(Call<List<Music>> call, Response<List<Music>> response) {
                RecyclerView recyclerView = findViewById(R.id.songsRecyclerView);
                MusicAdapter adapter = new MusicAdapter(response.body(), FavoriteMusics.this, getApplicationContext(),
                        mediaPlayer, seekbarUpdateHandler, updateSeekbar, seekBar, musicNameStarted, forward, rewind, progressBar);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<Music>> call, Throwable t) {
                Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            seekbarUpdateHandler.removeCallbacks(updateSeekbar);
            updateSeekbar = null;
            seekbarUpdateHandler = null;
            mediaPlayer.stop();
            mediaPlayer.release();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
