package org.kicksound.Utils;

import android.content.Context;
import android.widget.EditText;

import org.kicksound.R;

import androidx.core.content.ContextCompat;

public class HandleEditText {
    public static String fieldIsEmpty(EditText editText, Context context) {
        String textInEditText = editText.getText().toString();
        if(textInEditText.matches("")) {
            editText.setHintTextColor(ContextCompat.getColor(context, R.color.colorBlackRed));
            return null;
        }

        editText.setHintTextColor(ContextCompat.getColor(context, R.color.colorGreyText));
        return textInEditText;
    }

    public static boolean passwordEqualPasswordVerification(EditText password, EditText passwordVerification, Context context) {
        if(password.getText().toString().equals(passwordVerification.getText().toString())) {
            return true;
        }
        passwordVerification.setText("");
        passwordVerification.setHintTextColor(ContextCompat.getColor(context, R.color.colorBlackRed));
        return false;
    }
}
