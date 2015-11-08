package com.mieczkowskidev.partyradar;

import com.mieczkowskidev.partyradar.Objects.User;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by Patryk Mieczkowski on 2015-11-08
 */
public interface ServerInterface {

//    @POST("register/")
//    Observable<User> registerUser(@Body User user);

    @POST("/register")
    void registerUserRetro(@Body User user, Callback<User> callback);

    @Headers("Authorization: Token 123")
    @GET("/logout")
    void logoutUser(Callback<Response> callback);
}
