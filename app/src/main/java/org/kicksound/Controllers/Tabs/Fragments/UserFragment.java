package org.kicksound.Controllers.Tabs.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.kicksound.Controllers.MainActivity;
import org.kicksound.Controllers.ResetPasswordActivity;
import org.kicksound.Models.Logout;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Class.RetrofitManager;

import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class UserFragment extends Fragment {
    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_fragment, container, false);

        resetPassword(view);
        logout(view);

        return view;
    }

    private void resetPassword(final View view) {
        Button resetPasswordButton = view.findViewById(R.id.reset_password);
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleIntent.redirectToAnotherActivity(getContext(), ResetPasswordActivity.class, v);
            }
        });
    }

    private void logout(final View view) {
        Button logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                        .logout(HandleAccount.userAccount.getAccessToken())
                        .enqueue(new Callback<Logout>() {
                            @Override
                            public void onResponse(Call<Logout> call, Response<Logout> response) {
                                if(response.code() == 204) {
                                    SharedPreferences prefAccessToken = getActivity().getSharedPreferences(getString(R.string.USER_PREF), MODE_PRIVATE);
                                    prefAccessToken.edit().clear().apply();
                                    HandleIntent.redirectToAnotherActivity(getContext(), MainActivity.class, v);
                                    getActivity().finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Logout> call, Throwable t) {
                                Toasty.info(view.getContext(), getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
                            }
                        });
            }
        });
    }
}
