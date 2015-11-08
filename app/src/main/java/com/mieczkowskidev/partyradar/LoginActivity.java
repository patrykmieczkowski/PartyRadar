package com.mieczkowskidev.partyradar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mieczkowskidev.partyradar.Objects.User;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

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
            clearEditText();
            emailEditText.requestFocus();
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
                if (MODE == LOGIN) {
                    loginFlow();
                } else {
                    registerFlow();
                }
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
                clearEditText();
                loginEditText.requestFocus();
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

    private void clearEditText() {
        loginEditText.setText("");
        loginEditText.setError(null);
        emailEditText.setText("");
        emailEditText.setError(null);
        passwordEditText.setText("");
        passwordEditText.setError(null);
        rePasswordEditText.setText("");
        rePasswordEditText.setError(null);

    }

    private void loginFlow() {
        Toast.makeText(this, "Hello login here", Toast.LENGTH_SHORT).show();
        logoutUser();
        startMainActivity();
    }

    private void registerFlow() {
        Log.d(TAG, "registerFlow()");

        getDataFromFormula();
    }

    private void getDataFromFormula() {
        Log.d(TAG, "getDataFromFormula()");

        boolean cancel = false;
        View focusView = null;

        if (LoginManager.getEditTextText(rePasswordEditText)) {
            Log.i(TAG, "Re password is empty");
            rePasswordEditText.setError(getString(R.string.formula_empty_error));
            cancel = true;
            focusView = rePasswordEditText;
        }

        if (LoginManager.getEditTextText(passwordEditText)) {
            Log.i(TAG, "Password is empty");
            passwordEditText.setError(getString(R.string.formula_empty_error));
            cancel = true;
            focusView = passwordEditText;
        }

        if (LoginManager.getEditTextText(emailEditText)) {
            Log.i(TAG, "Email is empty");
            emailEditText.setError(getString(R.string.formula_empty_error));
            cancel = true;
            focusView = emailEditText;
        }

        if (LoginManager.getEditTextText(loginEditText)) {
            Log.i(TAG, "Login is empty");
            loginEditText.setError(getString(R.string.formula_empty_error));
            cancel = true;
            focusView = loginEditText;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Log.d(TAG, "check password match");

            if (!passwordEditText.getText().toString().equals(rePasswordEditText.getText().toString())) {
                Log.i(TAG, "Different passwords");
                passwordEditText.setError(getString(R.string.formula_password_error));
                rePasswordEditText.setError(getString(R.string.formula_password_error));
                cancel = true;
                focusView = passwordEditText;
            }

            if (cancel) {
                focusView.requestFocus();
            } else {
                Log.d(TAG, "formula is valid!");
                createUser();
            }
        }
    }

    private void createUser() {
        Log.d(TAG, "createUser()");

        String username = loginEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        User user = new User(username, email, password);

        registerUserOnServer(user);
    }

    private void registerUserOnServer(User user) {
        Log.d(TAG, "registerUserOnServer()");

        RestClient restClient = new RestClient();

        ServerInterface serverInterface = restClient.getRestAdapter().create(ServerInterface.class);

        serverInterface.registerUserRetro(user, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Log.d(TAG, "success() called with: " + "user = [" + user + "], response = [" + response + "]");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "failure() called with: " + "error = [" + error.getUrl() + "]");

            }
        });

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        ServerInterface serverInterface = retrofit.create(ServerInterface.class);
//
//        Call<Response> call = serverInterface.logoutUser();
//        call.enqueue(new Callback<Response>() {
//            @Override
//            public void onResponse(Response<Response> response, Retrofit retrofit) {
//                Log.d(TAG, "onResponse registerUserOnServer: " + response.message() + ", " + response.code());
//                Log.d(TAG, "onResponse registerUserOnServer: " + retrofit.baseUrl().url().toString());
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.e(TAG, "onFailure registerUserOnServer " + t.getMessage() + ", " + t.getCause());
//
//            }
//        });
//        Call<User> call = serverInterface.registerUserRetro(user);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Response<User> response, Retrofit retrofit) {
//                Log.d(TAG, "onResponse registerUserOnServer: " + response.message() + ", " + response.code());
//                Log.d(TAG, "onResponse registerUserOnServer: " + retrofit.baseUrl().url().toString());
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.e(TAG, "onFailure registerUserOnServer " + t.getMessage() + ", " + t.getCause());
//
//            }
//        });

    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void logoutUser() {
        Log.d(TAG, "logoutUser() called with: " + "");

        RestClient restClient = new RestClient();

        ServerInterface serverInterface = restClient.getRestAdapter().create(ServerInterface.class);

        serverInterface.logoutUser(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(TAG, "success() called with: " + "response = [" + response + "], response2 = [" + response2 + "]");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "failure() called with: " + error.getMessage() + ", " + error.getResponse() + ", error = [" + error.getUrl() + "]");
            }
        });
    }
}