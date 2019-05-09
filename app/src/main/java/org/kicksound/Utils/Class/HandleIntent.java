package org.kicksound.Utils.Class;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class HandleIntent {
    public static void redirectToAnotherActivity(Context context, Class<?> className, View view) {
        Intent activityToStart = new Intent(context, className);
        view.getContext().startActivity(activityToStart);
    }

    public static void redirectToAnotherActivityWithExtra(Context context, Class<?> className, View view, String extraName, String extraValue) {
        Intent activityToStart = new Intent(context, className);
        activityToStart.putExtra(extraName, extraValue);
        view.getContext().startActivity(activityToStart);
    }
}
