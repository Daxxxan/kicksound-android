package org.kicksound.Controllers.Playlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

    private static final int DELETE_PLAYLIST = 0;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
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
        holder.dotsMenuPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePlaylist(holder, position, v);
            }
        });
    }

    private void deletePlaylist(final ViewHolder holder, final int position, final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(playlistList.get(position).getName())
                .setItems(R.array.deletePlaylist, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DELETE_PLAYLIST) {
                            delete(position, v);
                        }
                    }
                });

        builder.create();
        builder.show();
    }

    private void delete(int position, final View v) {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .deletePlaylist(
                    HandleAccount.userAccount.getAccessToken(),
                        HandleAccount.userAccount.getId(),
                        playlistList.get(position).getId()
                ).enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                Toasty.success(context, context.getString(R.string.deletePlaylist), Toast.LENGTH_SHORT, true).show();
                activity.finish();
                if (musicId != null) {
                    HandleIntent.redirectToAnotherActivityWithExtra(context, AddMusicToPlayList.class, v, "musicId", musicId);
                } else {
                    HandleIntent.redirectToAnotherActivity(context, Playlists.class, v);
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                Toasty.error(context, context.getString(R.string.updateEventError), Toast.LENGTH_SHORT, true).show();
            }
        });
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
        ImageButton dotsMenuPlaylist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            string_item_playlist = itemView.findViewById(R.id.string_item_playlist);
            item_card_playlist = itemView.findViewById(R.id.item_card_playlist);
            dotsMenuPlaylist = itemView.findViewById(R.id.dotsMenuPlaylist);
        }
    }
}
