package com.mieczkowskidev.partyradar.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mieczkowskidev.partyradar.Objects.User;
import com.mieczkowskidev.partyradar.R;
import com.mieczkowskidev.partyradar.RestClient;
import com.mieczkowskidev.partyradar.ServerInterface;

import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Patryk Mieczkowski on 2015-11-08.
 */
public class MapFragment extends Fragment {

    private static final String TAG = MapFragment.class.getSimpleName();

    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        button = (Button) view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        return view;
    }

    private void login(){

        RestClient restClient = new RestClient();

        ServerInterface serverInterface = restClient.getRestAdapter().create(ServerInterface.class);

        serverInterface.loginUser(new User("test@partyradar.com", "test123"), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Log.d(TAG, "success() called with: " + "user = [" + user + "], response = [" + response + "]");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "failure() called with: " + "error = [" + error + "] " + error.getUrl() + ",  " + error.getResponse());
            }
        });
    }
}
