package com.mieczkowskidev.partyradar.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mieczkowskidev.partyradar.Constants;
import com.mieczkowskidev.partyradar.LoginActivity;
import com.mieczkowskidev.partyradar.LoginManager;
import com.mieczkowskidev.partyradar.Objects.User;
import com.mieczkowskidev.partyradar.R;
import com.mieczkowskidev.partyradar.RestClient;
import com.mieczkowskidev.partyradar.ServerInterface;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

/**
 * Created by Patryk Mieczkowski on 2015-11-11
 */
public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    private Button loginButton, registerButton;
    private EditText emailEditText, passwordEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        getViews(view);
        setListeners();
        fillFormula();
        return view;
    }

    private void getViews(View view) {

        emailEditText = (EditText) view.findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) view.findViewById(R.id.password_edit_text);

        registerButton = (Button) view.findViewById(R.id.register_fragment_button);
        loginButton = (Button) view.findViewById(R.id.login_button);
    }

    private void setListeners() {

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFlow();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity) getActivity()).startRegisterFragment();
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

    }

    private void fillFormula() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getString(R.string.shared_preferences_user), Context.MODE_PRIVATE);

        emailEditText.setText(sharedPreferences.getString(("email"), ""));
        passwordEditText.setText(sharedPreferences.getString(("password"), ""));

    }

    private void loginFlow() {
        Log.d(TAG, "loginFlow()");

        getDataFromFormula();
    }

    private void getDataFromFormula() {
        Log.d(TAG, "getDataFromFormula()");

        boolean cancel = false;
        View focusView = null;


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

        if (cancel) {
            focusView.requestFocus();

        } else {
            Log.d(TAG, "check password match");
            View focusView2 = null;

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
                loginUser();
            }
        }
    }

    private void loginUser() {
        Log.d(TAG, "loginUser()");

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        LoginManager.saveDataToSharedPreferences(getActivity(), email, password);

        User user = new User(email, password);

        loginUserOnServer(user);
    }

    private void loginUserOnServer(User user) {
        Log.d(TAG, "loginUserOnServer()");

        RestClient restClient = new RestClient();

        ServerInterface serverInterface = restClient.getRestAdapter().create(ServerInterface.class);

        serverInterface.loginUser(user, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                Log.d(TAG, "loginUserOnServer success(): " + response.getStatus() + ", " + response.getReason());

                Log.d(TAG, jsonElement.toString());
                JsonObject mainObject = jsonElement.getAsJsonObject();
                String status = mainObject.get("status").toString();
                String token = mainObject.get("token").toString();
                Log.d(TAG, "status: " + status + ", token: " + token);

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getString(R.string.shared_preferences_user), Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("token", token.replaceAll("\"","")).apply();

                ((LoginActivity) getActivity()).startMainActivity();
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getKind() == RetrofitError.Kind.NETWORK || error.getResponse() == null) {
                    Log.e(TAG, "error register with null");
                    showSnackbarInLoginActivity(getString(R.string.connection_error));
                } else {
                    Log.e(TAG, "loginUserOnServer failure() called with: " + "error = [" + error.getUrl() + "]");
                    String errorString = String.valueOf(error.getResponse().getStatus())
                            + ", " + String.valueOf(error.getResponse().getReason());
                    Log.e(TAG, "loginUserOnServer failure() called with: " + errorString);
                    showSnackbarInLoginActivity(errorString);
                }
            }
        });

    }

    private void showSnackbarInLoginActivity(String message) {

        ((LoginActivity) getActivity()).showSnackbar(message);

    }
}


