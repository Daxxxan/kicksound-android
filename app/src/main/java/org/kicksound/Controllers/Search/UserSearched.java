package org.kicksound.Controllers.Search;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.kicksound.Models.Account;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.FileUtil;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.RetrofitManager;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSearched extends AppCompatActivity {

    private Account userSearched = new Account();
    private ImageView userPicImageView = null;
    private TextView usernameTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_searched);
        String userId = getIntent().getStringExtra("userId");

        userPicImageView = findViewById(R.id.searched_user_pic);
        usernameTextView = findViewById(R.id.searched_username);

        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .getUserById(HandleAccount.userAccount.getAccessToken(), userId)
                .enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        userSearched = response.body();
                        FileUtil.downloadFileAndDisplay("user", userSearched.getPicture(), userPicImageView, getApplicationContext());
                        usernameTextView.setText(userSearched.getUsername());
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }
}
