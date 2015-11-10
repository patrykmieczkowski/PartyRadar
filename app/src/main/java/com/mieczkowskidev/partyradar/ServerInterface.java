package com.mieczkowskidev.partyradar;

import com.mieczkowskidev.partyradar.Objects.User;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by Patryk Mieczkowski on 2015-11-08
 */
public interface ServerInterface {

    @Headers("Content-Type: application/json")
    @POST("/register")
    void registerUser(@Body User user, Callback<User> callback);

    @Headers("Authorization: Token 123")
    @GET("/logout")
    void logoutUser(Callback<Response> callback);

    @POST("/login")
    void loginUser(@Body User user, Callback<User> callback);
}