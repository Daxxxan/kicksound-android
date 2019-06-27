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

public class AddMusicToPlayList extends AppCompatActivity {

    RecyclerView recyclerViewAddToPlaylist = null;
    ImageButton createPlaylist = null;
    EditText createPlaylistEditText = null;
    TextView noPlaylistCreated = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music_to_play_list);
        String musicId = getIntent().getStringExtra("musicId");

        recyclerViewAddToPlaylist = findViewById(R.id.recyclerViewAddToPlaylist);
        createPlaylist = findViewById(R.id.createPlaylistButton);
        createPlaylistEditText = findViewById(R.id.createPlaylistOnAddMusicToPlaylist);
        noPlaylistCreated = findViewById(R.id.noPlaylistCreatedPlaylist);

        HandleToolbar.displayToolbar(this, R.string.addToPlaylist);
        HandlePlaylist.createPlaylist(createPlaylist, createPlaylistEditText, getApplicationContext(), noPlaylistCreated, recyclerViewAddToPlaylist, musicId, this);
        HandlePlaylist.displayPlaylist(noPlaylistCreated, recyclerViewAddToPlaylist, getApplicationContext(), musicId, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
