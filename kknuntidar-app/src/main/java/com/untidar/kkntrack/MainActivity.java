package com.untidar.kkntrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.untidar.kkntrack.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcome, tvTotalActivities, tvLastActivity;
    private CardView cardProfile, cardActivityLog, cardReport, cardAnnouncement, cardLocation;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        sharedPreferences = getSharedPreferences("KKNTrackPrefs", MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);

        setupUserInfo();
        setupClickListeners();
        loadDashboardData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData(); // Refresh data when returning to main activity
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvTotalActivities = findViewById(R.id.tvTotalActivities);
        tvLastActivity = findViewById(R.id.tvLastActivity);
        cardProfile = findViewById(R.id.cardProfile);
        cardActivityLog = findViewById(R.id.cardActivityLog);
        cardReport = findViewById(R.id.cardReport);
        cardAnnouncement = findViewById(R.id.cardAnnouncement);
        cardLocation = findViewById(R.id.cardLocation);
    }

    private void setupUserInfo() {
        String userName = sharedPreferences.getString("userName", "User");
        tvWelcome.setText("Selamat Datang, " + userName);
    }

    private void loadDashboardData() {
        String userEmail = sharedPreferences.getString("userEmail", "");
        int totalActivities = databaseHelper.getActivitiesCountByUser(userEmail);
        String lastActivity = databaseHelper.getLastActivityByUser(userEmail);

        tvTotalActivities.setText("Total Kegiatan: " + totalActivities);
        tvLastActivity.setText("Kegiatan Terakhir: " + (lastActivity != null ? lastActivity : "Belum ada"));
    }

    private void setupClickListeners() {
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        cardActivityLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityLogActivity.class));
            }
        });

        cardReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReportActivity.class));
            }
        });

        cardAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AnnouncementActivity.class));
            }
        });

        cardLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            showLogoutDialog();
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya", (dialog, which) -> logout())
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Keluar Aplikasi")
                .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                .setPositiveButton("Ya", (dialog, which) -> finishAffinity())
                .setNegativeButton("Tidak", null)
                .show();
    }
}
