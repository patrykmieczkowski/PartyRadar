package com.mieczkowskidev.partyradar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int LOGIN = 0;
    private static final int REGISTER = 1;
    //set starting mode to LOGIN
    private static int MODE = LOGIN;

    private static final int VISIBLE = 0;
    private static final int GONE = 1;

    private Button loginButton;
    private TextView registerButton;
    private EditText loginEditText, emailEditText, passwordEditText, rePasswordEditText;
    private ImageView radarShortImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getViews();
        setListeners();
    }

    @Override
    public void onBackPressed() {

        if (MODE == REGISTER) {
            MODE = LOGIN;
            loginButton.setText(R.string.login_label_text);
            changeVisibility(loginEditText, GONE);
            changeVisibility(rePasswordEditText, GONE);
            changeVisibility(radarShortImage, GONE);
            changeVisibility(registerButton, VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    private void getViews() {

        loginEditText = (EditText) findViewById(R.id.login_edit_text);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        rePasswordEditText = (EditText) findViewById(R.id.re_password_edit_text);

        loginButton = (Button) findViewById(R.id.login_button);
        registerButton = (TextView) findViewById(R.id.register_button);
        radarShortImage = (ImageView) findViewById(R.id.radar_short_image);
    }

    private void setListeners() {

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MODE = REGISTER;
                loginButton.setText(R.string.new_register_button);
                changeVisibility(loginEditText, VISIBLE);
                changeVisibility(rePasswordEditText, VISIBLE);
                changeVisibility(radarShortImage, VISIBLE);
                changeVisibility(registerButton, GONE);
            }
        });
    }

    private void changeVisibility(View view, int visibility) {

        switch (visibility) {
            case VISIBLE:
                if (view.getVisibility() == View.GONE) {
                    view.setVisibility(View.VISIBLE);
                }
                break;
            case GONE:
                if (view.getVisibility() == View.VISIBLE) {
                    view.setVisibility(View.GONE);
                }
                break;
        }

    }
}