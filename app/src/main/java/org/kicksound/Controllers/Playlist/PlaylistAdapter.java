package org.kicksound.Controllers.Playlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.kicksound.Models.Playlist;
import org.kicksound.R;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private List<org.kicksound.Models.Playlist> playlistList;
    private Context context;

    public PlaylistAdapter(List<Playlist> playlistList, Context context) {
        this.playlistList = playlistList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        return new PlaylistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.string_item_playlist.setText(playlistList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView string_item_playlist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            string_item_playlist = itemView.findViewById(R.id.string_item_playlist);
        }
    }
}
