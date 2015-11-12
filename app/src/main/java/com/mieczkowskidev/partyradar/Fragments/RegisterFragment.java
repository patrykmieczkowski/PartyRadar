package com.mieczkowskidev.partyradar.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mieczkowskidev.partyradar.LoginActivity;
import com.mieczkowskidev.partyradar.LoginManager;
import com.mieczkowskidev.partyradar.MainActivity;
import com.mieczkowskidev.partyradar.Objects.User;
import com.mieczkowskidev.partyradar.R;
import com.mieczkowskidev.partyradar.RestClient;
import com.mieczkowskidev.partyradar.ServerInterface;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by Patryk Mieczkowski on 2015-11-08.
 */
public class RegisterFragment extends Fragment {

    private static final String TAG = RegisterFragment.class.getSimpleName();

    private Button registerButton;
    private EditText loginEditText, emailEditText, passwordEditText, rePasswordEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        getViews(view);
        setListeners();
        return view;
    }

    private void getViews(View view) {

        loginEditText = (EditText) view.findViewById(R.id.login_edit_text_reg);
        emailEditText = (EditText) view.findViewById(R.id.email_edit_text_reg);
        passwordEditText = (EditText) view.findViewById(R.id.password_edit_text_reg);
        rePasswordEditText = (EditText) view.findViewById(R.id.re_password_edit_text_reg);

        registerButton = (Button) view.findViewById(R.id.register_button);
    }

    private void setListeners() {

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerFlow();
            }
        });

        loginEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEditText.setError(null);
            }
        });
        emailEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEditText.setError(null);
            }
        });
        passwordEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordEditText.setError(null);
            }
        });
        rePasswordEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rePasswordEditText.setError(null);
            }
        });
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
            View focusView2 = null;

            if (!passwordEditText.getText().toString().equals(rePasswordEditText.getText().toString())) {
                Log.i(TAG, "Different passwords");
                passwordEditText.setError(getString(R.string.formula_password_error));
                rePasswordEditText.setError(getString(R.string.formula_password_error));
                cancel = true;
                focusView2 = passwordEditText;
            }

            if (!LoginManager.isPasswordValid(passwordEditText.getText().toString())) {
                Log.i(TAG, "Password to short");
                passwordEditText.setError("Password to short");
                cancel = true;
                focusView2 = passwordEditText;
            }

            if (!LoginManager.isValidEmail(emailEditText.getText().toString())) {
                Log.i(TAG, "Bad Email");
                emailEditText.setError("This is not a proper email address");
                cancel = true;
                focusView2 = emailEditText;
            }

            if (cancel) {
                focusView2.requestFocus();
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

        serverInterface.registerUser(user, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Log.d(TAG, "registerUserOnServer success(): " + response.getStatus() + ", " + response.getReason());
                Log.d(TAG, "registerUserOnServer success(): " + response.getUrl());
                showSnackbarInLoginActivity("User created! - zapraszamy dalej");

            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getKind() == RetrofitError.Kind.NETWORK || error.getResponse() == null) {
                    Log.e(TAG, "error register with null");
                    showSnackbarInLoginActivity(getString(R.string.connection_error));
                } else {
                    Log.e(TAG, "registerUserOnServer failure() called with: " + "error = [" + error.getUrl() + "]");
                    String errorString = String.valueOf(error.getResponse().getStatus())
                            + ", " + String.valueOf(error.getResponse().getReason());
                    Log.e(TAG, "registerUserOnServer failure() called with: " + errorString);
                    showSnackbarInLoginActivity(getString(R.string.connection_error));
                }
            }
        });

    }

    private void showSnackbarInLoginActivity(String message) {

        ((LoginActivity) getActivity()).showSnackbar(message);

    }
}


