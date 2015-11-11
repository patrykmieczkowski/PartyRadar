package com.mieczkowskidev.partyradar;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.mieczkowskidev.partyradar.Fragments.RegisterFragment;
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

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getViews();
        startRegisterFragment();
    }

    @Override
    public void onBackPressed() {

        if (MODE == REGISTER) {
            MODE = LOGIN;
            // change to login
        } else {
            super.onBackPressed();
        }
    }

    private void getViews() {

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.login_coordinator_layout);
    }

    public void showSnackbar(String message) {

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

        snackbar.show();
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

    private void startRegisterFragment() {
        MODE = REGISTER;

        RegisterFragment fragment = new RegisterFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.login_placeholder, fragment);
        transaction.addToBackStack(null);

        transaction.commit();

    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginUser() {
        Log.d(TAG, "loginUser() called with: " + "");

        RestClient restClient = new RestClient();

        ServerInterface serverInterface = restClient.getRestAdapter().create(ServerInterface.class);

        serverInterface.loginUser(new User("patryk@partyradar.com", "patryk1"), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Log.d(TAG, "success() called with: " + "user = [" + user + "], response = [" + response + "]");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "failure() called with: " + "error = [" + error + "]");
            }
        });
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