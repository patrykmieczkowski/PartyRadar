package com.mieczkowskidev.partyradar;

import com.mieczkowskidev.partyradar.Objects.User;

import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by Patryk Mieczkowski on 2015-11-08
 */
public interface ServerInterface {

    @POST("/register/")
    Observable<User> registerUser(@Body User user);
}
