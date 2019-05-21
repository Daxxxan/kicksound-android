package org.kicksound.Controllers.User;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.kicksound.Controllers.Tabs.TabActivity;
import org.kicksound.Models.Account;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.FileUtil;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.HandleIntent;
import org.kicksound.Utils.Class.RetrofitManager;

import java.io.File;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPicture extends AppCompatActivity {

    private static final int PICK_IMAGE_FROM_GALLERY = 1;
    private Uri selectedImage = null;
    private File userPicture = null;
    private Account account = null;
    private ImageView userImageView;
    private Button userPictureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_picture);
        userImageView = findViewById(R.id.userPicture);
        userPictureButton = findViewById(R.id.validateUserPicture);

        selectUserPicture();
        validateUserPicture();
    }

    private void validateUserPicture() {
        userPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(selectedImage != null) {
                    FileUtil.uploadFile(selectedImage, getApplicationContext(), "user");
                    account = new Account(userPicture.getName());

                    RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                            .updatePictureName(
                                    HandleAccount.userAccount.getAccessToken(),
                                    HandleAccount.userAccount.getId(),
                                    account
                            ).enqueue(new Callback<Account>() {
                        @Override
                        public void onResponse(Call<Account> call, Response<Account> response) {
                            if(response.code() == 200) {
                                HandleAccount.userAccount.setPicture(userPicture.getName());
                                HandleIntent.redirectToAnotherActivity(UserPicture.this, TabActivity.class, v);
                                Toasty.success(getApplicationContext(), v.getContext().getString(R.string.success_picture_user), Toast.LENGTH_SHORT, true).show();
                            } else {
                                Toasty.error(getApplicationContext(), v.getContext().getString(R.string.createEventError), Toast.LENGTH_SHORT, true).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Account> call, Throwable t) {
                            Toasty.error(getApplicationContext(), v.getContext().getString(R.string.connexion_error), Toast.LENGTH_SHORT, true).show();
                        }
                    });
                }
            }
        });
    }

    private void selectUserPicture() {
        Button addUserPictureButton = findViewById(R.id.addUserPicButton);
        addUserPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.pickImageFromGallery(getApplicationContext(), UserPicture.this, PICK_IMAGE_FROM_GALLERY);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            FileUtil.pickImageFromGallery(getApplicationContext(), UserPicture.this, PICK_IMAGE_FROM_GALLERY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            userPicture = new File(FileUtil.getPath(selectedImage, getApplicationContext()));

            FileUtil.displayCircleImageWithUri(getApplicationContext(), selectedImage, userImageView);
            userPictureButton.setVisibility(View.VISIBLE);
        } else if(selectedImage == null) {
            userPictureButton.setVisibility(View.GONE);
        }
    }
}
