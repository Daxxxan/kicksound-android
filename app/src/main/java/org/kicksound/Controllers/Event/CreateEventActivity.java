package org.kicksound.Controllers.Event;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.kicksound.Models.Event;
import org.kicksound.R;
import org.kicksound.Services.AccountService;
import org.kicksound.Utils.Class.HandleAccount;
import org.kicksound.Utils.Class.RetrofitManager;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.kicksound.Utils.Class.HandleEditText.fieldIsEmpty;

public class CreateEventActivity extends AppCompatActivity {

    private static final int PICK_FROM_FILE = 1;
    private static ImageView gallery_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        createEvent();
        //addPicture();
    }

    private void createEvent() {
        Button createEventButton = findViewById(R.id.createEventButton);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventTitle = fieldIsEmpty(getEventTitleEditText(), getApplicationContext());
                String eventDescription = fieldIsEmpty(getEventDescription(), getApplicationContext());
                String nbTicketString = fieldIsEmpty(getEventTickerNumber(), getApplicationContext());

                if(eventTitle != null && eventDescription != null && nbTicketString != null) {
                    int nbTicket = Integer.parseInt(nbTicketString);
                    RetrofitManager.getInstance().getRetrofit().create(AccountService.class)
                            .createEvent(HandleAccount.userAccount.getAccessToken(),
                                    HandleAccount.userAccount.getId(),
                                    new Event(eventTitle, eventDescription, nbTicket))
                            .enqueue(new Callback<Event>() {
                                @Override
                                public void onResponse(Call<Event> call, Response<Event> response) {
                                    if(response.code() == 200) {
                                        Toasty.success(getApplicationContext(), "OK", Toast.LENGTH_SHORT, true).show();
                                    } else {
                                        Toasty.error(getApplicationContext(), "NOK", Toast.LENGTH_SHORT, true).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Event> call, Throwable t) {
                                    Toasty.error(getApplicationContext(), "=(", Toast.LENGTH_SHORT, true).show();
                                }
                            });
                }
            }
        });
    }

    private EditText getEventTickerNumber() {
        return findViewById(R.id.ticketNumber);
    }

    private EditText getEventDescription() {
        return findViewById(R.id.descriptionEvent);
    }

    private EditText getEventTitleEditText() {
        return findViewById(R.id.eventName);
    }

    /*private void addPicture() {
        Button addEventPictureButton = findViewById(R.id.addEventPicture);
        gallery_image = findViewById(R.id.eventPicture);
        addEventPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectPicture = new Intent();
                selectPicture.setType("image/*");
                selectPicture.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(selectPicture, "Complete action using"), PICK_FROM_FILE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_FILE) {
            if (resultCode == RESULT_OK) {
                try {

                    Uri imageuri = data.getData();
                    //String real_Path = getRealPathFromUri(CreateEventActivity.this,imageuri);

                    Bitmap bitmap = decodeUri(CreateEventActivity.this, imageuri, 300);
                    if (bitmap != null)
                        gallery_image.setImageBitmap(bitmap);

                    else
                        Toast.makeText(CreateEventActivity.this,
                                "Error while decoding image.",
                                Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                    Toast.makeText(CreateEventActivity.this, "File not found.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static Bitmap decodeUri(Context context, Uri uri, final int requiredSize) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o2);
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }*/
}
