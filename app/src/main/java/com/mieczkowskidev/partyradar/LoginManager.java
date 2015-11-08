package com.mieczkowskidev.partyradar;

import android.widget.EditText;

/**
 * Created by Patryk Mieczkowski on 2015-11-08
 */
public class LoginManager {

    public static boolean getEditTextText(EditText editText) {

        return editText.getText().toString().trim().equalsIgnoreCase("");
    }

    public static boolean isEmailValid(String email) {

        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {

        return password.length() > 4;
    }
}
