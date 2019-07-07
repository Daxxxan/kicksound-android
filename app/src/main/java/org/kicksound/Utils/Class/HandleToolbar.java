package org.kicksound.Utils.Class;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.kicksound.R;

public class HandleToolbar {
    public static void displayToolbar(Activity activity, Integer stringId) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        ((AppCompatActivity)activity).setSupportActionBar(toolbar);

        if(((AppCompatActivity)activity).getSupportActionBar() != null) {
            ((AppCompatActivity)activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)activity).getSupportActionBar().setDisplayShowHomeEnabled(true);

            if(stringId != null)
                ((AppCompatActivity)activity).getSupportActionBar().setTitle(stringId);
        }
    }
}
