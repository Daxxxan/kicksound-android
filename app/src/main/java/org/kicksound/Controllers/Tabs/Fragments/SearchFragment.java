package org.kicksound.Controllers.Tabs.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import org.kicksound.Controllers.Search.SearchListAdapter;
import org.kicksound.Models.Account;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.RetrofitManager;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private static String CLASSIC_USER = "0";

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
        searchingBar(view);

        return view;
    }

    private void searchingBar(final View view) {
        ImageButton searchingButton = view.findViewById(R.id.searchingButton);
        final EditText searchingBar = view.findViewById(R.id.searching_bar);

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
                                    System.out.println("Nop");
                                }
                            });
                }
            }
        });
    }
}
