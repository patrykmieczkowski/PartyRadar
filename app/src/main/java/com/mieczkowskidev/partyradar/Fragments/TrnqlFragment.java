package com.mieczkowskidev.partyradar.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mieczkowskidev.partyradar.Constants;
import com.mieczkowskidev.partyradar.R;
import com.mieczkowskidev.partyradar.WeatherManager;
import com.trnql.smart.activity.ActivityEntry;
import com.trnql.smart.base.SmartFragment;
import com.trnql.smart.location.AddressEntry;
import com.trnql.smart.location.LocationEntry;
import com.trnql.smart.weather.WeatherEntry;
import com.trnql.zen.core.AppData;

/**
 * Created by Patryk Mieczkowski on 2015-11-11
 */
public class TrnqlFragment extends SmartFragment {

    private static final String TAG = TrnqlFragment.class.getSimpleName();

    private TextView locationText, temperatureText, weatherText;
    private ImageView weatherImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAppData().setApiKey("3bd5eb7e-64c7-4ff7-ad3b-f8e4ceb21e18");
        AppData.startAllServices(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trnql, container, false);

        getViews(view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppData.stopAllServices(getActivity());
    }


    @Override
    protected void smartAddressChange(AddressEntry address) {
        super.smartAddressChange(address);
        Log.d(TAG, "smartAddressChange() called with: " + "address = [" + address.toString() + "]");
        Constants.myAddress = address.toString();
        String location = address.toString();
        locationText.setText(location);

    }

    @Override
    protected void smartWeatherChange(WeatherEntry weather) {
        super.smartWeatherChange(weather);
        Log.d(TAG, "smartWeatherChange() called with: " + "weather = [" + weather.getWeatherSummaryAsString() + "]");
        String temperature = String.valueOf(weather.getCurrentTemp()) + "°C";
        String weatherS = weather.getCurrentConditionsDescriptionAsString();
        temperatureText.setText(temperature);
        weatherImage.setImageResource(WeatherManager.getDrawableForWeather(weatherS));
        weatherText.setText(weatherS);

    }

    private void getViews(View view) {

        locationText = (TextView) view.findViewById(R.id.location_text);
        weatherText = (TextView) view.findViewById(R.id.weather_text);
        temperatureText = (TextView) view.findViewById(R.id.temperature_text);
        weatherImage = (ImageView) view.findViewById(R.id.weather_image);


        Typeface myTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/timeless.ttf");
        locationText.setTypeface(myTypeface);
        weatherText.setTypeface(myTypeface);
        temperatureText.setTypeface(myTypeface);

    }


}
