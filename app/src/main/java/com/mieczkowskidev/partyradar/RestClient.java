package com.mieczkowskidev.partyradar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Patryk Mieczkowski on 2015-11-08
 */
public class RestClient {

    private RestAdapter restAdapter;

    public RestClient(){
        Gson gson = new GsonBuilder().create();

        restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(Constants.BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();
    }

    public RestAdapter getRestAdapter(){

        return restAdapter;
    }
}
