package org.kicksound.Controllers.Tabs.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.kicksound.Controllers.Playlist.Playlists;
import org.kicksound.Controllers.Song.FavoriteMusics;
import org.kicksound.R;
import org.kicksound.Utils.Class.HandleIntent;

import androidx.fragment.app.Fragment;

public class LibraryFragment extends Fragment {
    public LibraryFragment() {
        // Required empty public constructor
    }

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_fragment, container, false);

        setButtonsBehaviors(view);

        return view;
    }

    private void setButtonsBehaviors(final View view) {
        ImageButton playlists = view.findViewById(R.id.playlist);
        ImageButton songs = view.findViewById(R.id.songs);
        playlists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleIntent.redirectToAnotherActivity(getContext(), Playlists.class, view);
            }
        });

        songs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleIntent.redirectToAnotherActivity(getContext(), FavoriteMusics.class, view);
            }
        });
    }
}
