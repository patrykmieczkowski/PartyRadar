package com.mieczkowskidev.partyradar;

import com.google.gson.JsonElement;
import com.mieczkowskidev.partyradar.Objects.Event;
import com.mieczkowskidev.partyradar.Objects.User;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by Patryk Mieczkowski on 2015-11-08
 */
public interface ServerInterface {

    @Headers("Content-Type: application/json")
    @POST("/register")
    void registerUser(@Body User user, Callback<User> callback);

    @GET("/logout")
    void logoutUser(@Header("Authorization") String token, Callback<Response> callback);

    @POST("/login")
    void loginUser(@Body User user, Callback<JsonElement> callback);

    @GET("/get-posts")
    void getPosts(@Header("Authorization") String token,
                  @Query("lat") Double lat,
                  @Query("lon") Double lon,
                  @Query("radius") int radius,
                  @Query("time_offset") int time_offset,
                  Callback<JsonElement> callback);

    @Multipart
    @POST("/submit-post")
    void createPost(@Header("Authorization") String token,
                    @Part("photo") TypedFile photo,
                    @Part("description") String description,
                    @Part("lat") Double lat,
                    @Part("lon") Double lon,
                    Callback<Response> callback);
}