package org.kicksound.Controllers.Search;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.kicksound.Controllers.Live.Live;
import org.kicksound.Controllers.Song.ArtistMusics;
import org.kicksound.Models.Account;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.FileUtil;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Class.HandleToolbar;
import org.kicksound.Utils.Class.RetrofitManager;
import org.kicksound.Utils.Enums.UserType;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSearched extends AppCompatActivity {

    private Account userSearched = new Account();
    private ImageView userPicImageView = null;
    private Button highlight = null;
    private TextView userSearchedUsername = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_searched);
        String userId = getIntent().getStringExtra("userId");

        userPicImageView = findViewById(R.id.searched_user_pic);
        highlight = findViewById(R.id.highlight);

        HandleToolbar.displayToolbar(this, null);
        displayUser(userId);
        artistMusics(userId);
        followOrUnfollowUser(userId);
        streaming(userId);
    }

    private void streaming(final String userId) {
        Button streaming = findViewById(R.id.streaming);
        streaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleIntent.redirectToAnotherActivityWithExtra(getApplicationContext(), Live.class, v, "userId", userId);
            }
        });
    }

    private void highlight(final String userId) {
        if(HandleAccount.getUserType() == UserType.FAMOUS_ARTIST && userSearched.getType() == 1) {
            highlight.setVisibility(View.VISIBLE);
            RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                    .getHighlightUserById(
                            HandleAccount.userAccount.getAccessToken(),
                            HandleAccount.userAccount.getId(),
                            userId
                    ).enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if(response.code() == 200) {
                        userCantHighlight(userId);
                    } else {
                        userCanHighlight(userId);
                    }
                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
                }
            });
        } else {
            highlight.setVisibility(View.GONE);
        }
    }

    private void userCantHighlight(final String userId) {
        highlight.setText("Ne plus mettre en avant");
        highlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                        .deleteHighlightByUserById(
                                HandleAccount.userAccount.getAccessToken(),
                                HandleAccount.userAccount.getId(),
                                userId
                        ).enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        Toasty.success(getApplicationContext(), getApplicationContext().getString(R.string.highlight), Toast.LENGTH_SHORT, true).show();
                        userCanHighlight(userId);
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
                    }
                });
            }
        });
    }

    private void userCanHighlight(final String userId) {
        highlight.setText("Mettre en avant");
        highlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                        .highlightUser(
                                HandleAccount.userAccount.getAccessToken(),
                                HandleAccount.userAccount.getId(),
                                userId
                        ).enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        Toasty.success(getApplicationContext(), getApplicationContext().getString(R.string.highlight), Toast.LENGTH_SHORT, true).show();
                        userCantHighlight(userId);
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayUser(final String userId) {
        userSearchedUsername = findViewById(R.id.userSearchedUsername);
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .getUserById(HandleAccount.userAccount.getAccessToken(), userId)
                .enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        userSearched = response.body();
                        FileUtil.displayPicture("image", userSearched.getPicture(), userPicImageView, getApplicationContext());
                        userSearchedUsername.setText(response.body().getUsername());
                        highlight(userId);
                        if(getSupportActionBar() != null)
                            getSupportActionBar().setTitle(userSearched.getUsername());
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    private void artistMusics(final String userId) {
        Button artistTitlesButton = findViewById(R.id.artistTitlesButton);
        artistTitlesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleIntent.redirectToAnotherActivityWithExtra(getApplicationContext(), ArtistMusics.class,
                        v, "userId", userId);
            }
        });
    }

    private void followOrUnfollowUser(final String userId) {
        final Button followUserButton = findViewById(R.id.followButton);

        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .getFollowedUserById(
                        HandleAccount.userAccount.getAccessToken(),
                        HandleAccount.userAccount.getId(),
                        userId)
                .enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if(response.code() == 200) {
                            unfollow(followUserButton, userId);
                        } else {
                            follow(followUserButton, userId);
                        }
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    private void follow(Button followUserButton, String userId) {
        followUserButton.setText(getApplicationContext().getText(R.string.follow));
        setOnClickFollow(followUserButton, userId);
    }

    private void setOnClickFollow(final Button followUserButton, final String userId) {
        followUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFollowTreatment(followUserButton ,userId);
            }
        });
    }

    private void setFollowTreatment(final Button followUserButton, final String userId) {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .followUser(
                        HandleAccount.userAccount.getAccessToken(),
                        HandleAccount.userAccount.getId(),
                        userId
                ).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                unfollow(followUserButton, userId);
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    private void unfollow(Button followUserButton, String userId) {
        followUserButton.setText(getApplicationContext().getText(R.string.unfollow));
        setOnClickUnfollow(followUserButton, userId);
    }

    private void setOnClickUnfollow(final Button followUserButton, final String userId) {
        followUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnfollowTreatment(followUserButton, userId);
            }
        });
    }

    private void setUnfollowTreatment(final Button followUserButton, final String userId) {
        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .unfollowUser(
                        HandleAccount.userAccount.getAccessToken(),
                        HandleAccount.userAccount.getId(),
                        userId
                ).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                follow(followUserButton, userId);
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toasty.error(getApplicationContext(), getApplicationContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}
