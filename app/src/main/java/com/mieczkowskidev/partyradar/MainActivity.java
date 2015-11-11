package com.mieczkowskidev.partyradar;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.mieczkowskidev.partyradar.Fragments.MainFragment;
import com.trnql.smart.activity.ActivityEntry;
import com.trnql.smart.base.SmartActivity;
import com.trnql.smart.location.AddressEntry;
import com.trnql.smart.location.LocationEntry;
import com.trnql.smart.weather.WeatherEntry;
import com.trnql.zen.core.AppData;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setVisibleFragment(Constants.FRAGMENT_MAP);
//        showMapFragment();
    }



//    private void setVisibleFragment(int selectedFragment) {
//        Log.d(TAG, "setVisibleFragment()");
//
//        MainFragment newFragment = new MainFragment();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//        transaction.replace(R.id.frame_layout, newFragment);
//        transaction.addToBackStack(null);
//
//// Commit the transaction
//        transaction.commit();
//    }
//
//    private void showMapFragment() {
//        Log.d(TAG, "showMapFragment()");
//
//        MapFragment newFragment = new MapFragment();
//        com.mieczkowskidev.partyradar.Fragments.MapFragment a = com.mieczkowskidev.partyradar.Fragments.MapFragment.newInstance();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//        transaction.replace(R.id.frame_layout, newFragment);
//        transaction.addToBackStack(null);
//
//// Commit the transaction
//        transaction.commit();
//    }

    
}
