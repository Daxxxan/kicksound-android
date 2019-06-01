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

import org.kicksound.Controllers.Search.SearchListAdapter;
import org.kicksound.Models.Account;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.RetrofitManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private static String CLASSIC_USER = "0";
    private SwipeRefreshLayout swipeRefreshUserFollowed = null;
    private EditText searchingBar = null;

    public SearchFragment() {}

    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_fragment, container, false);
        swipeRefreshUserFollowed = view.findViewById(R.id.swipeRefreshUserFollowed);
        searchingBar = view.findViewById(R.id.searching_bar);

        resetSearchingList(view);
        reload(view);
        displayFollowedUsers(view);
        searchingBar(view);

        return view;
    }

    private void resetSearchingList(final View view) {
        searchingBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0) {
                    displayFollowedUsers(view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void reload(final View view) {
        swipeRefreshUserFollowed.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayFollowedUsers(view);
                swipeRefreshUserFollowed.setRefreshing(false);
            }
        });
    }

    private void displayFollowedUsers(final View view) {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .getFollowedUsers(
                        HandleAccount.userAccount.getAccessToken(),
                        HandleAccount.userAccount.getId())
                .enqueue(new Callback<List<Account>>() {
                    @Override
                    public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                        if(response.code() == 200) {
                            RecyclerView recyclerView = view.findViewById(R.id.searching_recycler_view);
                            SearchListAdapter adapter = new SearchListAdapter(response.body(), getContext());
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Account>> call, Throwable t) {
                    }
                });
    }

    private void searchingBar(final View view) {
        ImageButton searchingButton = view.findViewById(R.id.searchingButton);

        searchingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(!searchingBar.getText().toString().matches("")) {
                    String finalSearchUserLike = "%" + searchingBar.getText() + "%";

                    RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                            .getUsersByUserName(
                                    HandleAccount.userAccount.getAccessToken(),
                                    finalSearchUserLike,
                                    CLASSIC_USER,
                                    HandleAccount.userAccount.getId())
                            .enqueue(new Callback<List<Account>>() {
                                @Override
                                public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                                    RecyclerView recyclerView = view.findViewById(R.id.searching_recycler_view);
                                    SearchListAdapter adapter = new SearchListAdapter(response.body(), getContext());
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                }

                                @Override
                                public void onFailure(Call<List<Account>> call, Throwable t) {
                                }
                            });
                }
            }
        });
    }
}
