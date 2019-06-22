package org.kicksound.Controllers.Song;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.kicksound.Models.Music;
import org.kicksound.R;
import org.kicksound.Utils.Class.MusicUtil;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private List<Music> musicList;
    private Activity activity;
    private Context context;
    private MediaPlayer mediaPlayer;
    private Handler seekbarUpdateHandler;
    private Runnable updateSeekbar;
    private SeekBar seekBar;
    private TextView musicNameStarted;

    public MusicAdapter(List<Music> musicList, Activity activity, Context context, MediaPlayer mediaPlayer, Handler seekbarUpdateHandler, Runnable updateSeekbar, SeekBar seekBar, TextView musicNameStarted) {
        this.musicList = musicList;
        this.activity = activity;
        this.context = context;
        this.mediaPlayer = mediaPlayer;
        this.seekbarUpdateHandler = seekbarUpdateHandler;
        this.updateSeekbar = updateSeekbar;
        this.seekBar = seekBar;
        this.musicNameStarted = musicNameStarted;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item, parent, false);
        return new MusicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.itemMusicName.setText(musicList.get(position).getTitle());
        holder.itemArtistName.setText(musicList.get(position).getAccounts().getUsername());
        holder.musicItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicUtil.loadMusic(musicList.get(position).getLocation(), context, activity, mediaPlayer, seekbarUpdateHandler, updateSeekbar, seekBar);
                musicNameStarted.setText(musicList.get(position).getLocation());
                musicNameStarted.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                musicNameStarted.setSingleLine(true);
                musicNameStarted.setMarqueeRepeatLimit(5);
                musicNameStarted.setSelected(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemMusicName;
        TextView itemArtistName;
        CardView musicItem;
        ConstraintLayout constraintBackground;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemMusicName = itemView.findViewById(R.id.itemMusicName);
            itemArtistName = itemView.findViewById(R.id.itemArtistName);
            musicItem = itemView.findViewById(R.id.item_card_music);
            constraintBackground = itemView.findViewById(R.id.constraintBackground);
        }
    }
}
