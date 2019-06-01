package org.kicksound.Utils.Class;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.kicksound.R;
import org.kicksound.Services.AccountService;

import java.io.File;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileUtil {

    public static void displayCircleImageWithUri(Context context, Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .fitCenter()
                .error(R.drawable.kicksound_logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }

    public static void displayCircleImage(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .fitCenter()
                .error(R.drawable.kicksound_logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }

    public static void pickImageFromGallery(Context context, Activity activity, int PICK_IMAGE_FROM_GALLERY) {
        if(FileUtil.allowAccessToGallery(activity, context)) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activity.startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_GALLERY);
        }
    }

    public static boolean allowAccessToGallery(Activity activity, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        } else {
            return true;
        }
    }

    public static void displayPicture(final String container, String fileName, final ImageView imageView, final Context context) {
        String path = context.getString(R.string.kicksound_dns) + container + "/" + fileName;
        FileUtil.displayCircleImage(context, path, imageView);
    }

    public static void uploadFile(Uri fileUri, final Context context, String type) {
        File file = new File(FileUtil.getPath(fileUri, context));

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",file.getName(),requestFile);

        RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                .uploadFile(
                        HandleAccount.userAccount.getAccessToken(),
                        multipartBody,
                        type)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toasty.error(context, context.getString(R.string.error_upload_image), Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    @SuppressLint("NewApi")
    public static String getPath(Uri uri, Context context) {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        if (needToCheckUri && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:", "");
                }
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                switch (type) {
                    case "image":
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "video":
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "audio":
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        break;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    return cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                Log.e("on getPath", "Exception", e);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
