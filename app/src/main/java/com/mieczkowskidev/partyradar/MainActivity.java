package com.mieczkowskidev.partyradar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mieczkowskidev.partyradar.Dialogs.CreateEventDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    static final int REQUEST_TAKE_PHOTO = 1;

    private FloatingActionButton addButton;
    private CoordinatorLayout coordinatorLayout;
    private ImageView partyInfoPhoto;
    private RelativeLayout partyInfoLayout;
    private String path;
    private CreateEventDialog createEventDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getViews();
        setListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            createEventDialog = new CreateEventDialog(this);
            createEventDialog.show();
            loadImage();
        }
    }


//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        showSnackbar("Created!");
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                logoutUser();
                break;
        }
        return true;
    }

    private void getViews() {

        addButton = (FloatingActionButton) findViewById(R.id.floating_add_button);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator);
        partyInfoLayout = (RelativeLayout) findViewById(R.id.party_info_layout);
        partyInfoPhoto = (ImageView) findViewById(R.id.party_info_photo);
    }

    private void setListeners() {

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureFromCamera();
            }
        });
    }

    private void takePictureFromCamera() {
        Log.d(TAG, "takePictureFromCamera()");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        Log.d(TAG, "file:" + image.getAbsolutePath());
        path = image.getAbsolutePath();
        return image;
    }


    private void loadImage() {
        Log.d(TAG, "loadImage(): " + path);
    }

    public void showPartyInfoLayout(String imageUrl) {

        if (partyInfoLayout.getVisibility() == View.GONE) {
            partyInfoLayout.setVisibility(View.VISIBLE);
        }

        Picasso.with(this)
                .load(Constants.BASE_URL + imageUrl)
                .error(R.drawable.radar_short)
                .placeholder(R.drawable.progress_image)
                .into(partyInfoPhoto);
    }

    public void hidePartyInfoLayout() {

        if (partyInfoLayout.getVisibility() == View.VISIBLE) {
            partyInfoLayout.setVisibility(View.GONE);
        }
    }

    public void showFAB() {

        if (addButton.getVisibility() == View.GONE) {
            addButton.setVisibility(View.VISIBLE);
        }
    }

    public void hideFAB() {

        if (addButton.getVisibility() == View.VISIBLE) {
            addButton.setVisibility(View.GONE);
        }
    }

    public void showSnackbar(String message) {

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public void createPostOnServer() {
        Log.d(TAG, "createPostOnServer()");

        RestClient restClient = new RestClient();

        ServerInterface serverInterface = restClient.getRestAdapter().create(ServerInterface.class);
//
//        File photoFile = new File(path);
//        TypedFile photoTypedFile = new TypedFile("file:", photoFile);

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Bitmap b = BitmapFactory.decodeFile(path);
        Bitmap out = Bitmap.createScaledBitmap(b, 300, 400, false);

        File file = new File(dir, "resize.png");

        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {
        }

        final File photoFiles = new File(file.getAbsolutePath());
        TypedFile photoo = new TypedFile("file:", photoFiles);

        serverInterface.createPost(photoo, "party", Constants.myPosition.latitude, Constants.myPosition.longitude,
                new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        Log.d(TAG, "success() called with: " + "response = [" + response.getStatus() + "], response2 = [" + response2.getStatus() + "]");
                        createEventDialog.dismiss();
                        showSnackbar("Success! Added new event");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getKind() == RetrofitError.Kind.NETWORK || error.getResponse() == null) {
                            Log.e(TAG, "error register with null");
                            showSnackbar("Connection error!");
                        } else {
                            Log.e(TAG, "failure() called with: " + "error = [" + error + "]");
                            createEventDialog.dismiss();
                            showSnackbar("An error occurred, please try again!");
                        }
                    }
                });

    }

    private void logoutUser() {
        Log.d(TAG, "logoutUser()");

        RestClient restClient = new RestClient();

        ServerInterface serverInterface = restClient.getRestAdapter().create(ServerInterface.class);

        serverInterface.logoutUser(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(TAG, "success() called with: " + "response = [" + response + "], response2 = [" + response2 + "]");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getKind() == RetrofitError.Kind.NETWORK || error.getResponse() == null) {
                    Log.e(TAG, "error register with null");
                    showSnackbar("Connection error!");
                } else {
                    Log.e(TAG, "failure() called with: " + "error = [" + error + "]");
                    showSnackbar("Connection error!");
                }
            }
        });

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
