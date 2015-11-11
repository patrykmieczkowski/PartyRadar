package com.mieczkowskidev.partyradar.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mieczkowskidev.partyradar.R;
import com.trnql.smart.activity.ActivityEntry;
import com.trnql.smart.base.SmartFragment;
import com.trnql.smart.location.AddressEntry;
import com.trnql.smart.location.LocationEntry;
import com.trnql.smart.weather.WeatherEntry;
import com.trnql.zen.core.AppData;

/**
 * Created by Patryk Mieczkowski on 2015-11-11.
 */
public class TrnqlFragment extends SmartFragment {

    private static final String TAG = TrnqlFragment.class.getSimpleName();

    private TextView textView1, textView2;

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

        Log.d(TAG, address.getCountryCode() + ", " + address.getCountryName());
    }

    @Override
    protected void smartActivityChange(ActivityEntry userActivity) {
        super.smartActivityChange(userActivity);
        Log.d(TAG, "smartActivityChange() called with: " + "userActivity = [" + userActivity.getActivityString() + "]");
    }

    @Override
    protected void smartWeatherChange(WeatherEntry weather) {
        super.smartWeatherChange(weather);
        Log.d(TAG, "smartWeatherChange() called with: " + "weather = [" + weather.getWeatherSummaryAsString() + "]");
        String temp = "Temp: " + String.valueOf(weather.getCurrentTemp()) + ", odczuwalna: " + String.valueOf(weather.getFeelsLikeTemp());
        String calaPogoda = "Calosc: " +  weather.getWeatherSummaryAsString();
        textView1.setText(temp);
        textView2.setText(calaPogoda);
    }

    @Override
    protected void smartIsHighAccuracy(boolean isHighAcc) {
        super.smartIsHighAccuracy(isHighAcc);
        Log.d(TAG, "smartIsHighAccuracy() called with: " + "isHighAcc = [" + isHighAcc + "]");
    }

    @Override
    protected void smartLatLngChange(LocationEntry location) {
        super.smartLatLngChange(location);
        Log.d(TAG, "smartLatLngChange() called with: " + "location = [" + location.getLatLng().toString() + "]");
    }

    private void getViews(View view) {

        textView1 = (TextView) view.findViewById(R.id.textView);
        textView2 = (TextView) view.findViewById(R.id.textView2);
    }


}
