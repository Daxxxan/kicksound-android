package org.kicksound.Controllers.Tabs.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.kicksound.Controllers.Connection.LoginActivity;
import org.kicksound.Controllers.Event.CreateEventActivity;
import org.kicksound.Controllers.Song.AddMusic;
import org.kicksound.Controllers.Song.ArtistMusics;
import org.kicksound.Controllers.User.ResetPasswordActivity;
import org.kicksound.Models.Logout;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.FileUtil;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Class.RetrofitManager;
import org.kicksound.Utils.Enums.UserType;

import java.io.File;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class UserFragment extends Fragment {

    private static final int PICK_IMAGE_FROM_GALLERY = 1;
    private Uri selectedImage = null;
    private File userPicture = null;
    ImageView userPic = null;

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

        loadUserPic(view);
        updateUserPic(view);
        resetPassword(view);
        logout(view);
        songButtonsIfIsArtist(view);

        return view;
    }

    private void updateUserPic(View view) {
        ImageButton updateUserPic = view.findViewById(R.id.updateUserPic);
        updateUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FileUtil.allowAccessToExternalStorage(getActivity(), getContext())) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_GALLERY);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_GALLERY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            userPicture = new File(FileUtil.getPath(selectedImage, getContext()));

            FileUtil.displayCircleImageWithUri(getContext(), selectedImage, userPic);
        }
    }

    private void songButtonsIfIsArtist(View view) {
        Button addSong = view.findViewById(R.id.addSong);
        Button artistSongs = view.findViewById(R.id.artistSongs);

        if(HandleAccount.getUserType() != UserType.USER) {
            addSong.setVisibility(View.VISIBLE);
            artistSongs.setVisibility(View.VISIBLE);

            addSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HandleIntent.redirectToAnotherActivity(getContext(), AddMusic.class, v);
                }
            });

            artistSongs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HandleIntent.redirectToAnotherActivityWithExtra(getContext(), ArtistMusics.class,
                            v, "userId", HandleAccount.userAccount.getId());
                }
            });
        }
    }

    private void loadUserPic(View view) {
        userPic = view.findViewById(R.id.user_pic);
        if(HandleAccount.userAccount.getPicture() != null){
            FileUtil.displayPicture("image", HandleAccount.userAccount.getPicture(), userPic, getContext());
        } else {
            Picasso.get().load(R.drawable.kicksound_logo).into(userPic);
        }
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
                                    HandleIntent.redirectToAnotherActivity(getContext(), LoginActivity.class, v);
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
