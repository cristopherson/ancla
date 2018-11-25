package com.prototype.ancla;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;

import static com.prototype.ancla.AnclaValueEventListener.ANCLA_USER_EVENT;
import static com.prototype.ancla.AnclaValueEventListener.ANCLA_USER_ID;

public class MainActivity extends AppCompatActivity {
    public static String LOG_ANCLA_TAG = "LogAncla";
    public static final int ANCLA_LOCATION_PERMISSIONS_REQUEST = 99;

    private NotificationManagerCompat mNotificationManagerCompat;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference messageRef = ref.child("messages");
    private int notificationCounter = 0;
    private FloodEventContainer events = new FloodEventContainer();
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, MainActivity.LOG_ANCLA_TAG)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap();
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (checkLocationPermission()) {
            Log.d(MainActivity.LOG_ANCLA_TAG, "Location permissions already granted");
            requestCurrentLocation();
        } else {
            Log.d(MainActivity.LOG_ANCLA_TAG, "Location permissions are not granted yet");
        }

        if (messageRef != null) {
            messageRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(MainActivity.LOG_ANCLA_TAG, "MainActivity events");

                    if (!events.parseDataSnapshot(dataSnapshot)) {
                        Log.d(MainActivity.LOG_ANCLA_TAG, "DataSnapshot parsing failed");
                    } else {
                        String notificationMessage = events.getNewEventMessage();

                        if (notificationMessage.compareTo("") != 0) {
                            mBuilder.setContentTitle("Alerta Ancla")
                                    .setContentText(notificationMessage);

                            mNotificationManagerCompat.notify(notificationCounter++, mBuilder.build());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Log.d(MainActivity.LOG_ANCLA_TAG, "messageRef is null");
        }
    }

    public void openMap() {
        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.putExtra(MapsActivity.ANCLA_PARCELABLE_EVENTS_ID, events);

        startActivity(mapIntent);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        ANCLA_LOCATION_PERMISSIONS_REQUEST);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ANCLA_LOCATION_PERMISSIONS_REQUEST);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ANCLA_LOCATION_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        requestCurrentLocation();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    public void requestCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(MainActivity.LOG_ANCLA_TAG, "Can not get current location");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d(MainActivity.LOG_ANCLA_TAG, "Getting current location");

        Task<Location> task = mFusedLocationClient.getLastLocation();

        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            FloodEvent event = new FloodEvent(AnclaValueEventListener.ANCLA_USER_ID, AnclaValueEventListener.ANCLA_USER_EVENT,
                                    FloodEvent.ANCLA_EVENT_SAFE, location.getLatitude(), location.getLongitude());

                            Log.d(MainActivity.LOG_ANCLA_TAG, "User event: " + event.toString());
                            events.addEvent(AnclaValueEventListener.ANCLA_USER_ID, event);
                        } else {
                            Log.d(MainActivity.LOG_ANCLA_TAG, "location is null");
                        }
                    }


                });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(MainActivity.LOG_ANCLA_TAG, "Failure to get location: " + e.getMessage());
            }
        });

        task.addOnCanceledListener(this, new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.d(MainActivity.LOG_ANCLA_TAG, "Location operation has been cancelled ");
            }
        });

        task.addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Log.d(MainActivity.LOG_ANCLA_TAG, "task has been completed");
            }
        });
    }
}
