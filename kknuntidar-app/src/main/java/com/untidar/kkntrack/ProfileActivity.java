package com.untidar.kkntrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.untidar.kkntrack.util.UiUtils;
import androidx.appcompat.app.AppCompatActivity;
import com.untidar.kkntrack.database.DatabaseHelper;
import com.untidar.kkntrack.model.User;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvNim, tvEmail, tvPhone;
    private Button btnEditProfile, btnChangePassword;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("KKNTrackPrefs", MODE_PRIVATE);

        loadUserProfile();
        setupClickListeners();
    }

    private void initViews() {
        tvName = findViewById(R.id.tvName);
        tvNim = findViewById(R.id.tvNim);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
    }

    private void loadUserProfile() {
        String userEmail = sharedPreferences.getString("userEmail", "");
        currentUser = databaseHelper.getUserByEmail(userEmail);

        if (currentUser != null) {
            tvName.setText(currentUser.getFullName());
            tvNim.setText(currentUser.getNim());
            tvEmail.setText(currentUser.getEmail());
            tvPhone.setText(currentUser.getPhone());
        }
    }

    private void setupClickListeners() {
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d("ProfileActivity", "Edit Profile button clicked");
                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    startActivityForResult(intent, 100);
                } catch (Exception e) {
                    Log.e("ProfileActivity", "Error starting EditProfileActivity", e);
                    UiUtils.showSnack(ProfileActivity.this, "Error: " + e.getMessage());
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d("ProfileActivity", "Change Password button clicked");
                    startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
                } catch (Exception e) {
                    Log.e("ProfileActivity", "Error starting ChangePasswordActivity", e);
                    UiUtils.showSnack(ProfileActivity.this, "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadUserProfile(); // Refresh profile data
            UiUtils.showSnack(this, "Profil berhasil diperbarui");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_activity_stats) {
            showActivityStats();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showActivityStats() {
        String userEmail = sharedPreferences.getString("userEmail", "");
        int totalActivities = databaseHelper.getActivitiesCountByUser(userEmail);

    UiUtils.showSnackLong(this, "Total kegiatan Anda: " + totalActivities);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
