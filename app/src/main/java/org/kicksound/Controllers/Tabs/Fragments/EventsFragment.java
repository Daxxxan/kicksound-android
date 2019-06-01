package org.kicksound.Controllers.Tabs.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.kicksound.Controllers.Event.CreateEventActivity;
import org.kicksound.Controllers.Event.EventListAdapter;
import org.kicksound.Controllers.Event.MyEvents;
import org.kicksound.Models.Event;
import org.kicksound.R;
import org.kicksound.Services.EventService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Class.RetrofitManager;
import org.kicksound.Utils.Enums.UserType;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText searchingBar = null;

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
        recyclerView = view.findViewById(R.id.searching_recycler_view_event);
        searchingBar = view.findViewById(R.id.searching_bar_event);

        resetSearchingList(view);
        displayEvents(view);
        displayFabIfUserIsAnArtist(view);
        searchingBarEvent(view);

        return view;
    }

    private void resetSearchingList(final View view) {
        searchingBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0) {
                    displayEvents(view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void displayEvents(final View view) {
        RetrofitManager.getInstance().getRetrofit().create(EventService.class)
                .getNextEvents(
                        "date",
                        Calendar.getInstance().getTime()
                )
                .enqueue(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        EventListAdapter adapter = new EventListAdapter(response.body(), getContext());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                    }
                });
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

    private void searchingBarEvent(final View view) {
        ImageButton searchingButton = view.findViewById(R.id.searching_button_event);

        searchingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!searchingBar.getText().toString().matches("")) {
                    String finalSearchEventLike = "%" + searchingBar.getText() + "%";
                    displayEventsByName(v, finalSearchEventLike);
                }
            }
        });
    }

    private void displayEventsByName(final View view, String title) {
        RetrofitManager.getInstance().getRetrofit().create(EventService.class)
                .getEventsByName(
                        "date",
                        Calendar.getInstance().getTime(),
                        title
                )
                .enqueue(new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        EventListAdapter adapter = new EventListAdapter(response.body(), getContext());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                    }
                });
    }
}
