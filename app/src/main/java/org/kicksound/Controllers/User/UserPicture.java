package org.kicksound.Controllers.User;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.kicksound.R;
import org.kicksound.Utils.Class.FileUtil;

import java.io.File;

public class UserPicture extends AppCompatActivity {

    private static final int PICK_IMAGE_FROM_GALLERY = 1;
    private Uri selectedImage = null;
    private File userPicture = null;
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
            public void onClick(View v) {

            }
        });
    }

    private void selectUserPicture() {
        Button addUserPictureButton = findViewById(R.id.addUserPicButton);
        addUserPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allowAccessToGallery();
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_GALLERY);
            }
        });
    }

    private void allowAccessToGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            userPicture = new File(FileUtil.getPath(selectedImage, getApplicationContext()));

            Picasso.get().load(selectedImage).into(userImageView);
            userPictureButton.setVisibility(View.VISIBLE);
        } else if(selectedImage == null) {
            userPictureButton.setVisibility(View.GONE);
        }
    }
}
