package org.kicksound.Controllers.Tabs.Fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.kicksound.Controllers.Statics.StaticObjects;
import org.kicksound.R;

import androidx.fragment.app.Fragment;

public class EventsFragment extends Fragment {

    public EventsFragment() {
        // Required empty public constructor
    }

    public static EventsFragment newInstance() {
        EventsFragment fragment = new EventsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_fragment, container, false);

        if(StaticObjects.userAccount.getType() == 1) {
            RelativeLayout eventView = view.findViewById(R.id.event_fragment);
            FloatingActionButton fabButtonCreateEvent = new FloatingActionButton(getContext());

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(32, 32, 32, 32);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            fabButtonCreateEvent.setLayoutParams(layoutParams);
            fabButtonCreateEvent.setImageResource(R.drawable.ic_action_nplus);
            fabButtonCreateEvent.setBackgroundColor(getResources().getColor(R.color.colorGold));
            fabButtonCreateEvent.setElevation(10);
            fabButtonCreateEvent.setFocusable(true);
            fabButtonCreateEvent.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGold)));

            eventView.addView(fabButtonCreateEvent);
        }
        return view;
    }
}
