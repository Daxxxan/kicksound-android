package org.kicksound.Controllers.Playlist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.kicksound.Controllers.Song.FavoriteMusics;
import org.kicksound.Models.Music;
import org.kicksound.Models.Playlist;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Class.RetrofitManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private List<Playlist> playlistList;
    private Context context;
    private String musicId;
    private Activity activity;

    public PlaylistAdapter(List<Playlist> playlistList, Context context, String musicId, Activity activity) {
        this.playlistList = playlistList;
        this.context = context;
        this.musicId = musicId;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        return new PlaylistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.string_item_playlist.setText(playlistList.get(position).getName());

        if (musicId != null) {
            holder.item_card_playlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addMusicToPlaylist(musicId, position);
                }
            });
        } else {
            holder.item_card_playlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HandleIntent.redirectToAnotherActivityWithExtra(context, FavoriteMusics.class, v, "playlistId", playlistList.get(position).getId());
                }
            });
        }
    }

    private void addMusicToPlaylist(String musicId, int position) {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .addMusicToPlaylist(
                        HandleAccount.userAccount.getAccessToken(),
                        HandleAccount.userAccount.getId(),
                        playlistList.get(position).getId(),
                        musicId
                ).enqueue(new Callback<Music>() {
            @Override
            public void onResponse(Call<Music> call, Response<Music> response) {
                if (response.code() == 200) {
                    Toasty.success(context, context.getString(R.string.eventModificationSuccess), Toast.LENGTH_SHORT, true).show();
                    activity.finish();
                }
            }

            @Override
            public void onFailure(Call<Music> call, Throwable t) {
                Toasty.error(context, context.getString(R.string.updateEventError), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView string_item_playlist;
        CardView item_card_playlist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            string_item_playlist = itemView.findViewById(R.id.string_item_playlist);
            item_card_playlist = itemView.findViewById(R.id.item_card_playlist);
        }
    }
}
