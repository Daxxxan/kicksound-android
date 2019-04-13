package org.kicksound.Utils.Class;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class HandleIntent {
    public static void redirectToAnotherActivity(Context context, Class<?> className, View view) {
        Intent activityToStart = new Intent(context, className);
        view.getContext().startActivity(activityToStart);
    }
}
