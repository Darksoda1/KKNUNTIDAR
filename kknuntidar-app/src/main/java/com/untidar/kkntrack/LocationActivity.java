package com.untidar.kkntrack;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private TextView tvLocation, tvLatitude, tvLongitude;
    private FusedLocationProviderClient fusedLocationClient;
    private ProgressBar progressBar;
    private View rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        getSupportActionBar().setTitle("Lokasi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkLocationPermission();
    }

    private void initViews() {
        tvLocation = findViewById(R.id.tvLocation);
        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        progressBar = findViewById(R.id.progressBar);
        rootLayout = findViewById(R.id.rootLayout);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        // wrapper kept for compatibility; delegate to fetchLocation
        fetchLocation();
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Quick UI feedback
        progressBar.setVisibility(View.VISIBLE);
        tvLocation.setText("Mencari lokasi...");
        final Snackbar searching = Snackbar.make(rootLayout, "Mencari lokasi...", Snackbar.LENGTH_INDEFINITE);
        searching.show();

        // Try last known location first (fast). If null, request a fresh location with timeout.
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            updateLocationUI(location);
                            searching.dismiss();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            // Request a fresh current location with timeout
                            final CancellationTokenSource cts = new CancellationTokenSource();

                            // Cancel request after 8 seconds to avoid long hangs on emulator
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    cts.cancel();
                                }
                            }, 8000);

                            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, cts.getToken())
                                    .addOnSuccessListener(LocationActivity.this, new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location loc) {
                                            if (loc != null) {
                                                updateLocationUI(loc);
                                                searching.dismiss();
                                            } else {
                                                onLocationFailed(searching);
                                            }
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        onLocationFailed(searching);
                                        progressBar.setVisibility(View.GONE);
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    onLocationFailed(searching);
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void updateLocationUI(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        tvLocation.setText("Lokasi berhasil ditemukan");
        tvLatitude.setText("Latitude: " + latitude);
        tvLongitude.setText("Longitude: " + longitude);

        Snackbar.make(rootLayout, "Lokasi ditemukan", Snackbar.LENGTH_SHORT).show();
    }

    private void onLocationFailed(final Snackbar searching) {
        searching.dismiss();
        tvLocation.setText("Lokasi tidak dapat ditemukan");
        Snackbar.make(rootLayout, "Gagal mendapatkan lokasi", Snackbar.LENGTH_LONG)
                .setAction("Coba lagi", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchLocation();
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                com.untidar.kkntrack.util.UiUtils.showSnack(this, "Izin lokasi diperlukan untuk fitur ini");
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
