package org.kicksound.Controllers.Discovery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.kicksound.Controllers.Song.FavoriteMusics;
import org.kicksound.Models.MusicKind;
import org.kicksound.R;
import org.kicksound.Utils.Class.HandleIntent;

import java.util.List;

public class MusicKindAdapter extends RecyclerView.Adapter<MusicKindAdapter.ViewHolder> {

    private List<MusicKind> musicKindList;
    private Context context;

    public MusicKindAdapter(List<MusicKind> musicKindList, Context context) {
        this.musicKindList = musicKindList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_kind_item, parent, false);
        return new MusicKindAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.musicKindName.setText(musicKindList.get(position).getName());
        holder.item_card_music_kind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleIntent.redirectToAnotherActivityWithExtra(context, FavoriteMusics.class, v, "musicKindId", musicKindList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicKindList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView musicKindName;
        CardView item_card_music_kind;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            musicKindName = itemView.findViewById(R.id.musicKindName);
            item_card_music_kind = itemView.findViewById(R.id.item_card_music_kind);
        }
    }
}
