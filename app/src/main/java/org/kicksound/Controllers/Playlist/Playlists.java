package org.kicksound.Controllers.Playlist;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.kicksound.R;
import org.kicksound.Utils.Class.HandlePlaylist;
import org.kicksound.Utils.Class.HandleToolbar;

public class Playlists extends AppCompatActivity {

    ImageButton createPlaylist = null;
    EditText createPlaylistEditText = null;
    TextView noPlaylistCreated = null;
    RecyclerView playlistRecyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);

        createPlaylist = findViewById(R.id.createPlaylist);
        createPlaylistEditText = findViewById(R.id.createPlaylistEditText);
        noPlaylistCreated = findViewById(R.id.noPlaylistCreated);
        playlistRecyclerView = findViewById(R.id.playlistRecyclerView);

        HandleToolbar.displayToolbar(this, R.string.playlists);
        HandlePlaylist.createPlaylist(createPlaylist, createPlaylistEditText, getApplicationContext(), noPlaylistCreated, playlistRecyclerView, null, this);
        HandlePlaylist.displayPlaylist(noPlaylistCreated, playlistRecyclerView, getApplicationContext(), null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
