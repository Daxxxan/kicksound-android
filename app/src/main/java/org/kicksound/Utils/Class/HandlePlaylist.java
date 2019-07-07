package org.kicksound.Utils.Class;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.kicksound.Controllers.Playlist.PlaylistAdapter;
import org.kicksound.Models.Playlist;
import org.kicksound.R;
import org.kicksound.Services.AccountService;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HandlePlaylist {
    public static void createPlaylist(ImageButton createPlaylist, final EditText createPlaylistEditText, final Context context, final TextView noPlaylistCreated, final RecyclerView recyclerView, final String musicId, final Activity activity) {
        createPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!createPlaylistEditText.getText().toString().matches("")) {
                    createPlaylistRequest(createPlaylistEditText.getText().toString(), context);
                    createPlaylistEditText.setText("");
                    displayPlaylist(noPlaylistCreated, recyclerView, context, musicId, activity);
                }
            }
        });
    }

    public static void createPlaylistRequest(String playlistName, final Context context) {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .createPlaylist(
                        HandleAccount.userAccount.getAccessToken(),
                        HandleAccount.userAccount.getId(),
                        new org.kicksound.Models.Playlist(playlistName)
                ).enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                Toasty.success(context, context.getString(R.string.successfullyCreatePlaylist), Toast.LENGTH_SHORT, true).show();
            }

            @Override
            public void onFailure(Call<org.kicksound.Models.Playlist> call, Throwable t) {
                Toasty.error(context, context.getString(R.string.createEventError), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    public static void displayPlaylist(final TextView noPlaylistCreated, final RecyclerView recyclerView, final Context context, final String musicId, final Activity activity) {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .getPlaylist(
                        HandleAccount.userAccount.getAccessToken(),
                        HandleAccount.userAccount.getId()
                ).enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if(response.body() != null){
                    if(response.body().size() == 0) {
                        noPlaylistCreated.setVisibility(View.VISIBLE);
                    } else {
                        PlaylistAdapter adapter = new PlaylistAdapter(response.body(), context, musicId, activity);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    }
                } else {
                    noPlaylistCreated.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Toasty.error(context, context.getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
