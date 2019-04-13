package org.kicksound.Utils.Class;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HandleFAB {
    public static FloatingActionButton createFabButtonOnBottomLeft(Context context, int resourceId, int backgroundColorId, ColorStateList fabColor) {
        FloatingActionButton fabButtonCreateEvent = new FloatingActionButton(context);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(100, 100, 100, 100);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        fabButtonCreateEvent.setLayoutParams(layoutParams);
        fabButtonCreateEvent.setImageResource(resourceId);
        fabButtonCreateEvent.setBackgroundColor(backgroundColorId);
        fabButtonCreateEvent.setElevation(10);
        fabButtonCreateEvent.setFocusable(true);
        fabButtonCreateEvent.setBackgroundTintList(fabColor);

        return fabButtonCreateEvent;
    }
}
