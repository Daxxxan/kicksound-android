package org.kicksound.Utils.Class;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import org.kicksound.R;

import java.io.IOException;

public class MusicUtil {
    public static void loadMusic(String musicName, final Context context, final Activity activity,
                                 final MediaPlayer mediaPlayer, final Handler seekbarUpdateHandler,
                                 final Runnable updateSeekbar, final SeekBar seekBar) {
        String url = context.getString(R.string.kicksound_dns) + "music/" + musicName;
        final ImageButton play = activity.findViewById(R.id.play);
        final ImageButton pause = activity.findViewById(R.id.pause);
        final ProgressBar progressBarLoadMusic = activity.findViewById(R.id.progressBarLoadMusic);

        resetMediaPlayer(mediaPlayer, play, pause, seekbarUpdateHandler, updateSeekbar);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mediaPlayer.setAudioAttributes(audioAttributes);
        try {
            progressBarLoadMusic.setVisibility(View.VISIBLE);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();

            onPrepared(mediaPlayer, progressBarLoadMusic, play, pause, seekbarUpdateHandler, updateSeekbar, activity, seekBar);
        } catch (IOException e) {
            progressBarLoadMusic.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    private static void onPrepared(final MediaPlayer mediaPlayer, final ProgressBar progressBarLoadMusic, final ImageButton play,
                                   final ImageButton pause, final Handler seekbarUpdateHandler, final Runnable updateSeekbar,
                                   final Activity activity, final SeekBar seekBar) {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBarLoadMusic.setVisibility(View.GONE);
                start(mediaPlayer, play, pause, seekbarUpdateHandler, updateSeekbar);
                play(mp, play, pause, seekbarUpdateHandler, updateSeekbar);
                pause(mp, play, pause, seekbarUpdateHandler, updateSeekbar);
                seekbar(activity, mp, seekBar);
            }
        });
    }

    private static void resetMediaPlayer(MediaPlayer mediaPlayer, ImageButton play, ImageButton pause, final Handler seekbarUpdateHandler, final Runnable updateSeekbar) {
        mediaPlayer.reset();
        seekbarUpdateHandler.removeCallbacks(updateSeekbar);
        resetPlayPauseButton(play, pause);
    }

    private static void resetPlayPauseButton(ImageButton play, ImageButton pause) {
        play.setVisibility(View.VISIBLE);
        pause.setVisibility(View.GONE);
    }

    private static void play(final MediaPlayer mediaPlayer, final ImageButton play, final ImageButton pause, final Handler seekbarUpdateHandler, final Runnable updateSeekbar) {
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(mediaPlayer, play, pause, seekbarUpdateHandler, updateSeekbar);
            }
        });
    }

    private static void start(MediaPlayer mediaPlayer, ImageButton play, ImageButton pause, final Handler seekbarUpdateHandler, final Runnable updateSeekbar) {
        mediaPlayer.start();
        seekbarUpdateHandler.postDelayed(updateSeekbar, 0);
        play.setVisibility(View.GONE);
        pause.setVisibility(View.VISIBLE);
    }

    private static void pause(final MediaPlayer mediaPlayer, final ImageButton play, final ImageButton pause, final Handler seekbarUpdateHandler, final Runnable updateSeekbar) {
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                seekbarUpdateHandler.removeCallbacks(updateSeekbar);
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
            }
        });
    }

    private static void seekbar(Activity activity, final MediaPlayer mediaPlayer, final SeekBar seekBar) {
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                    mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}
