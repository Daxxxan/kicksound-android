package org.kicksound.Controllers.Song;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.isapanah.awesomespinner.AwesomeSpinner;

import org.kicksound.Models.Music;
import org.kicksound.Models.MusicKind;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Services.MusicKindService;
import org.kicksound.Utils.Class.FileUtil;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Class.HandleToolbar;
import org.kicksound.Utils.Class.RetrofitManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private EditText musicNameEditText = null;
    private String musicKindSelected = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        HandleToolbar.displayToolbar(this, R.string.importMusic);
        importMusic();
        addMusicKindSpinner();
        validateImportMusic();
    }

    private void addMusicKindSpinner() {
        RetrofitManager.getInstance().getRetrofit().create(MusicKindService.class)
                .getMusicKinds(HandleAccount.userAccount.getAccessToken())
                .enqueue(new Callback<List<MusicKind>>() {
                    @Override
                    public void onResponse(Call<List<MusicKind>> call, final Response<List<MusicKind>> response) {
                        List<String> musicKindName = new ArrayList<>();
                        for (MusicKind musicKind: response.body()) {
                            musicKindName.add(musicKind.getName());
                        }
                        AwesomeSpinner musicKindSpinner = findViewById(R.id.musicKindSpinner);
                        final ArrayAdapter<String> musicKindList = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, musicKindName);
                        musicKindSpinner.setAdapter(musicKindList);
                        musicKindSpinner.setSelectedItemHintColor(getResources().getColor(R.color.colorWhite));
                        musicKindSpinner.setBackgroundColor(getResources().getColor(R.color.colorWhiteBlack));
                        musicKindSpinner.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
                            @Override
                            public void onItemSelected(int position, String itemAtPosition) {
                                musicKindSelected = response.body().get(position).getId();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<List<MusicKind>> call, Throwable t) {

                    }
                });
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
        musicNameEditText = findViewById(R.id.musicNameEditText);

        validateAddMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(selectedMusic != null && musicKindSelected != null) {
                    String musicName = getMusicName();
                    Music newMusic = new Music(musicName, song.getName(), Calendar.getInstance().getTime(), musicKindSelected);

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
                            HandleIntent.redirectToAnotherActivityWithExtra(getApplicationContext(), ArtistMusics.class, v, "userId", HandleAccount.userAccount.getId());
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

    private String getMusicName() {
        if(musicNameEditText.getText().toString().matches("")) {
            return song.getName().substring(0, song.getName().indexOf("."));
        }

        return musicNameEditText.getText().toString();
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
