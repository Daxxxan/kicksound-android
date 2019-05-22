package org.kicksound.Controllers.Tabs.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.kicksound.Controllers.Event.CreateEventActivity;
import org.kicksound.Controllers.Event.MyEvents;
import org.kicksound.R;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Enums.UserType;

public class EventsFragment extends Fragment {

    public EventsFragment() {
        // Required empty public constructor
    }

    public static EventsFragment newInstance() {
        return new EventsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_fragment, container, false);

        displayFabIfUserIsAnArtist(view);

        return view;
    }

    private void displayFabIfUserIsAnArtist(View view) {
        if(HandleAccount.getUserType() != UserType.USER) {
            FloatingActionMenu famEvent = view.findViewById(R.id.famEvent);
            FloatingActionButton fabEvent = view.findViewById(R.id.fabCreateEvent);
            FloatingActionButton fabDisplayEvents = view.findViewById(R.id.fabDisplayEvents);

            famEvent.showMenu(true);

            fabEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HandleIntent.redirectToAnotherActivity(getContext(), CreateEventActivity.class, v);
                }
            });

            fabDisplayEvents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HandleIntent.redirectToAnotherActivity(getContext(), MyEvents.class, v);
                }
            });
        }
    }
}
