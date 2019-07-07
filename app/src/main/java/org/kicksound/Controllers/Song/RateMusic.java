package org.kicksound.Controllers.Song;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import org.kicksound.Models.Mark;
import org.kicksound.Models.Music;
import org.kicksound.R;
import org.kicksound.Services.MusicService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleToolbar;
import org.kicksound.Utils.Class.RetrofitManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateMusic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_music);
        String musicId = getIntent().getStringExtra("musicId");

        HandleToolbar.displayToolbar(this, R.string.rateThisSong);
        validateRate(musicId);
    }

    private void validateRate(final String musicId) {
        Button addMark = findViewById(R.id.addMark);
        final RatingBar musicRatingBar = findViewById(R.id.musicRatingBar);
        addMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicRatingBar.getRating() != 0) {
                    RetrofitManager.getInstance().getRetrofit().create(MusicService.class)
                            .addMark(
                                    HandleAccount.userAccount.getAccessToken(),
                                    musicId,
                                    new Mark(musicRatingBar.getRating(), musicId)
                            ).enqueue(new Callback<Mark>() {
                        @Override
                        public void onResponse(Call<Mark> call, Response<Mark> response) {
                            Log.e("rate", String.valueOf(response.code()));
                            if(response.code() == 200) {
                                Toasty.success(getApplicationContext(), getApplicationContext().getString(R.string.newNote), Toast.LENGTH_SHORT, true).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Mark> call, Throwable t) {
                            Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
                        }
                    });
                }
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
