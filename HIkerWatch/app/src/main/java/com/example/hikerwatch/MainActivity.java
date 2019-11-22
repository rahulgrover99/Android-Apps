package com.example.hikerwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView latTextView;
    TextView longTextView;
    TextView accTextView;
    TextView altitudeTextView;
    TextView addressTextView;




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void updateLocationInfo(Location location){
        latTextView = findViewById(R.id.latTextView);
        longTextView = findViewById(R.id.longTextView);
        altitudeTextView = findViewById(R.id.altitudeTextView);
        accTextView = findViewById(R.id.accTextView);
        addressTextView = findViewById(R.id.addressTextView);

        latTextView.setText("Latitude: " + Double.toString(location.getLatitude()));
        longTextView.setText("Longitude: "+Double.toString(location.getLongitude()));
        altitudeTextView.setText("Altitude: "+Double.toString(location.getAltitude()));
        accTextView.setText("Accuracy: "+Double.toString(location.getAccuracy()));
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        try {
            String address = "Could not find Address";
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses!=null && addresses.size()>0) {
//                Log.i("Address : ", addresses.get(0).toString());
                 address = "";

                if (addresses.get(0).getSubThoroughfare() != null) {

                    address += addresses.get(0).getSubThoroughfare() + "\n";

                }

                if (addresses.get(0).getThoroughfare() != null) {

                    address += addresses.get(0).getThoroughfare() + "\n";

                }

                if (addresses.get(0).getLocality() != null) {

                    address += addresses.get(0).getLocality() + "\n";

                }

                if (addresses.get(0).getPostalCode() != null) {

                    address += addresses.get(0).getPostalCode() + "\n";

                }

                if (addresses.get(0).getCountryName() != null) {

                    address += addresses.get(0).getCountryName();

                }

            }
            addressTextView.setText("Address :\n " + address);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        Log.i("Latitude : ", Double.toString(location.getLatitude()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (Build.VERSION.SDK_INT < 23) {
            startListening();
        }
        else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation!=null) {
                    updateLocationInfo(lastKnownLocation);
                }

            }
        }
    }
}
