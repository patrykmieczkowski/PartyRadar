package com.mieczkowskidev.partyradar.Fragments;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mieczkowskidev.partyradar.R;

/**
 * Created by Patryk Mieczkowski on 2015-11-10.
 */
public class MapFragment extends SupportMapFragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private final static String TAG = MapFragment.class.getSimpleName();

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();

        return fragment;
    }

    private GoogleApiClient googleApiClient;
    private Location currentLocation;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        initListeners();
        addMarkers();
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        currentLocation = LocationServices
                .FusedLocationApi
                .getLastLocation(googleApiClient);

        initCamera(currentLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG, "lat: " + latLng.latitude + ", long: " + latLng.longitude);



    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        MarkerOptions options = new MarkerOptions().position(latLng);
        options.title("onMapClicked marker");

        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.party_marker));
        getMap().addMarker(options);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private void initListeners() {
        getMap().setOnMarkerClickListener(this);
        getMap().setOnMapLongClickListener(this);
        getMap().setOnInfoWindowClickListener(this);
        getMap().setOnMapClickListener(this);

    }

    private void initCamera(Location location) {
        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(location.getLatitude(),
                        location.getLongitude()))
                .zoom(16f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        getMap().animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);

        getMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        getMap().setTrafficEnabled(true);
        getMap().setMyLocationEnabled(true);
//        getMap().getUiSettings().setZoomControlsEnabled(true);
    }

    private void addMarkers() {
        Log.d(TAG, "addMarkers()");

        getMap().addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.party_marker))
                .position(new LatLng(50.1083610639509, 19.960406720638275))
                .title("Party party 1"));

        getMap().addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.party_marker))
                .position(new LatLng(50.1089181910328, 19.965937435626984))
                .title("Party party 2"));

        getMap().addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.party_marker))
                .position(new LatLng(50.06186446897023, 19.937465451657772))
                .title("Main square party!"));

        getMap().addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.party_marker))
                .position(new LatLng(50.05021416554237, 19.9314821138978))
                .title("Mateczny party"));
    }
}
