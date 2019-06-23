package org.kicksound.Controllers.Song;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import org.kicksound.R;
import org.kicksound.Utils.Class.HandleToolbar;

public class Musics extends AppCompatActivity {

    public static Activity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musics);
        activity = this;

        HandleToolbar.displayToolbar(this, R.string.titles);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
