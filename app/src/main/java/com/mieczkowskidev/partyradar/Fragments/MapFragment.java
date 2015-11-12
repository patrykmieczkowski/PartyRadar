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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mieczkowskidev.partyradar.Constants;
import com.mieczkowskidev.partyradar.Deserializer.EventDeserializer;
import com.mieczkowskidev.partyradar.LoginManager;
import com.mieczkowskidev.partyradar.MainActivity;
import com.mieczkowskidev.partyradar.Objects.Event;
import com.mieczkowskidev.partyradar.Objects.User;
import com.mieczkowskidev.partyradar.R;
import com.mieczkowskidev.partyradar.RestClient;
import com.mieczkowskidev.partyradar.ServerInterface;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.GsonConverter;

/**
 * Created by Patryk Mieczkowski on 2015-11-10.
 */
public class MapFragment extends SupportMapFragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private final static String TAG = MapFragment.class.getSimpleName();

    private HashMap<Marker, Event> markerEventHashMap = new HashMap<>();

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
        if (currentLocation != null) {
            initCamera(currentLocation);
            Constants.myPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            downloadEvents();
        } else {
            ((MainActivity) getActivity()).showSnackbar("There was a problem with localization. Reset all sensors and try again!");
        }
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

        ((MainActivity) getActivity()).hidePartyInfoLayout();
        ((MainActivity) getActivity()).showFAB();

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

//        MarkerOptions options = new MarkerOptions().position(latLng);
//        options.title("onMapClicked marker");
//
//        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.party_marker));
//        getMap().addMarker(options);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        ((MainActivity) getActivity()).showPartyInfoLayout(markerEventHashMap.get(marker).getPhoto());
        ((MainActivity) getActivity()).hideFAB();

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
        getMap().getUiSettings().setMapToolbarEnabled(true);
//        getMap().getUiSettings().setZoomControlsEnabled(true);
    }

    public LatLng getLocations() {
        return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
    }

    public Double getLatitude() {
        return currentLocation.getLatitude();
    }

    public Double getLongitude() {
        return currentLocation.getLongitude();
    }

    private void downloadEvents() {
        Log.d(TAG, "downloadEvents()");

        RestClient restClient = new RestClient();

        ServerInterface serverInterface = restClient.getRestAdapter().create(ServerInterface.class);

        String info = "Lat: " + String.valueOf(currentLocation.getLatitude());
        Log.d(TAG, info);

        String token = LoginManager.getTokenFromShared(getActivity());
        Log.d(TAG, token);
        serverInterface.getPosts(token, currentLocation.getLatitude(), currentLocation.getLongitude(),
                50, 10, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        Log.d(TAG, "downloadEvents success() called with: response = [" + response.getStatus() + "]");

                        List<Event> list = new ArrayList<>();

                        JsonObject mainObject = jsonElement.getAsJsonObject();
                        JsonArray eventArray = mainObject.get("posts").getAsJsonArray();

                        for (JsonElement jsonElementoo : eventArray) {

                            int id = jsonElementoo.getAsJsonObject().get("id").getAsInt();
                            String user = jsonElementoo.getAsJsonObject().get("user").getAsString();
                            String photo = jsonElementoo.getAsJsonObject().get("photo").getAsString();
                            String description = jsonElementoo.getAsJsonObject().get("description").getAsString();
                            Double lat = jsonElementoo.getAsJsonObject().get("lat").getAsDouble();
                            Double lon = jsonElementoo.getAsJsonObject().get("lon").getAsDouble();
                            String created = jsonElementoo.getAsJsonObject().get("created").getAsString();
                            String modified = jsonElementoo.getAsJsonObject().get("modified").getAsString();

                            Event event = new Event(id, user, photo, description, lat, lon, created, modified);
                            Log.d(TAG, event.toString());
                            list.add(event);
                        }

                        addEventMarkers(list);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "downloadEvents failure() called with: " + "error = [" + error + "]");

                    }
                });
    }

    public void addEventMarkers(List<Event> eventList) {
        Log.d(TAG, "addEventMarkers() called with: " + "eventList size = [" + eventList.size() + "]");

        for (Event event : eventList) {
            Marker marker = getMap().addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.party_marker))
                    .position(new LatLng(event.getLat(), event.getLon()))
                    .title(event.getDescription() + "\n" + event.getUser()));

            markerEventHashMap.put(marker, event);
        }
    }

//    private void addMarkers() {
//        Log.d(TAG, "addMarkers()");
//
//        getMap().addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.party_marker))
//                .position(new LatLng(50.1083610639509, 19.960406720638275))
//                .title("Party party 1"));
//
//        getMap().addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.party_marker))
//                .position(new LatLng(50.1089181910328, 19.965937435626984))
//                .title("Party party 2"));
//
//        getMap().addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.party_marker))
//                .position(new LatLng(50.06186446897023, 19.937465451657772))
//                .title("Main square party!"));
//
//        getMap().addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.party_marker))
//                .position(new LatLng(50.05021416554237, 19.9314821138978))
//                .title("Mateczny party"));
//    }
}
