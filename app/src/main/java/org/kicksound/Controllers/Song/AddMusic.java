package org.kicksound.Controllers.Song;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.kicksound.Models.Music;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.FileUtil;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Class.HandleToolbar;
import org.kicksound.Utils.Class.RetrofitManager;
import org.w3c.dom.Text;

import java.io.File;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMusic extends AppCompatActivity {

    private ImageButton importMusicButton = null;
    private Uri selectedMusic = null;
    private File song = null;
    private TextView musicNameTextView = null;
    private Button validateAddMusicButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        HandleToolbar.displayToolbar(this, R.string.importMusic);
        importMusic();
        validateImportMusic();
    }

    private void importMusic() {
        importMusicButton = findViewById(R.id.importMusicButton);
        importMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.pickMusicFromDownloadFolder(getApplicationContext(), AddMusic.this, 2);
            }
        });
    }

    private void validateImportMusic() {
        validateAddMusicButton = findViewById(R.id.validateAddMusicButton);
        validateAddMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(selectedMusic != null) {
                    Music newMusic = new Music(song.getName(), song.getName(), Calendar.getInstance().getTime());

                    FileUtil.uploadFile(selectedMusic, getApplicationContext(), "music");
                    RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                            .createMusic(
                                    HandleAccount.userAccount.getAccessToken(),
                                    HandleAccount.userAccount.getId(),
                                    newMusic
                            ).enqueue(new Callback<Music>() {
                        @Override
                        public void onResponse(Call<Music> call, Response<Music> response) {
                            if(response.code() == 200) {
                                Toasty.success(getApplicationContext(), getApplicationContext().getString(R.string.successfullyAddMusic), Toast.LENGTH_SHORT, true).show();
                            }
                            finish();
                            Musics.activity.finish();
                            HandleIntent.redirectToAnotherActivity(getApplicationContext(), Musics.class, v);
                        }

                        @Override
                        public void onFailure(Call<Music> call, Throwable t) {
                            Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.createEventError), Toast.LENGTH_SHORT, true).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            selectedMusic = data.getData();
            song = new File(FileUtil.getPath(selectedMusic, getApplicationContext()));

            musicNameTextView = findViewById(R.id.musicNameTextView);
            musicNameTextView.setText(song.getName());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
