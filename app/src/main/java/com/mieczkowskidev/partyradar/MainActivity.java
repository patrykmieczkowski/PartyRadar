package com.mieczkowskidev.partyradar;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.mieczkowskidev.partyradar.Fragments.MainFragment;
import com.trnql.smart.activity.ActivityEntry;
import com.trnql.smart.base.SmartActivity;
import com.trnql.smart.location.AddressEntry;
import com.trnql.smart.location.LocationEntry;
import com.trnql.smart.weather.WeatherEntry;
import com.trnql.zen.core.AppData;

public class MainActivity extends SmartActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAppData().setApiKey("3bd5eb7e-64c7-4ff7-ad3b-f8e4ceb21e18");
        AppData.startAllServices(this);
//        setVisibleFragment(Constants.FRAGMENT_MAP);
    }

    @Override
    protected void onDestroy() {
        AppData.startAllServices(this);
        super.onDestroy();
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

    private void setVisibleFragment(int selectedFragment) {
        Log.d(TAG, "setVisibleFragment()");

        MainFragment newFragment = new MainFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.frame_layout, newFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }
}
