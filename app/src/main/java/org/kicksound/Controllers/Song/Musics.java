package org.kicksound.Controllers.Song;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.kicksound.R;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Class.HandleToolbar;
import org.kicksound.Utils.Enums.UserType;

public class Musics extends AppCompatActivity {

    public static Activity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musics);
        activity = this;

        HandleToolbar.displayToolbar(this, R.string.titles);
        displayFabAddSong();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayFabAddSong() {
        FloatingActionButton fabAddSong = findViewById(R.id.fabAddSong);
        FloatingActionMenu fabMenuSong = findViewById(R.id.fabMenuSong);
        FloatingActionButton fabArtistSongs = findViewById(R.id.fabArtistSongs);

        if(HandleAccount.getUserType() != UserType.USER) {
            fabMenuSong.setVisibility(View.VISIBLE);
            fabAddSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HandleIntent.redirectToAnotherActivity(getApplicationContext(), AddMusic.class, v);
                }
            });

            fabArtistSongs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HandleIntent.redirectToAnotherActivityWithExtra(getApplicationContext(), ArtistMusics.class,
                            v, "userId", HandleAccount.userAccount.getId());
                }
            });
        }
    }
}
