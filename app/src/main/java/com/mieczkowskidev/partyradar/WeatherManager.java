package com.mieczkowskidev.partyradar;

/**
 * Created by Patryk Mieczkowski on 2015-11-12
 */
public class WeatherManager {

    public static int getDrawableForWeather (String weather){

        switch (weather){
            case "Cloudy":
                return R.drawable.w_cloudy;
            case "Chance of Rain":
                return R.drawable.w_rainy;
            case "Rain":
                return R.drawable.w_rainy;
            case "Mostly Cloudy":
                return R.drawable.w_cloudy;
            case "Sunny":
                return R.drawable.w_sunny;
            default:
                return R.drawable.w_sunny;
        }
    }
}
