package org.kicksound.Controllers.Tabs.Fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.kicksound.R;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleFAB;
import org.kicksound.Utils.Enums.UserType;

import androidx.fragment.app.Fragment;

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

        if(HandleAccount.getUserType() != UserType.USER) {
            RelativeLayout eventView = view.findViewById(R.id.event_fragment);
            eventView.addView(
                    HandleFAB.createFabButtonOnBottomLeft(
                            getContext(),
                            R.drawable.ic_action_nplus,
                            getResources().getColor(R.color.colorGold),
                            ColorStateList.valueOf(getResources().getColor(R.color.colorGold))
                    )
            );
        }
        return view;
    }
}
