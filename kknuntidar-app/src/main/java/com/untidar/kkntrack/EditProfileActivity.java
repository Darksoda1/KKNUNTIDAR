package com.untidar.kkntrack;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.untidar.kkntrack.database.DatabaseHelper;
import com.untidar.kkntrack.model.User;
import android.util.Log;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etFullName, etNim, etPhone;
    private Button btnSave, btnCancel;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Log.d("EditProfile", "EditProfileActivity started");

        getSupportActionBar().setTitle("Edit Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("KKNTrackPrefs", MODE_PRIVATE);

        loadCurrentUserData();
        setupClickListeners();
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etNim = findViewById(R.id.etNim);
        etPhone = findViewById(R.id.etPhone);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void loadCurrentUserData() {
        String userEmail = sharedPreferences.getString("userEmail", "");
        currentUser = databaseHelper.getUserByEmail(userEmail);

        if (currentUser != null) {
            etFullName.setText(currentUser.getFullName());
            etNim.setText(currentUser.getNim());
            etPhone.setText(currentUser.getPhone());
        }
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveProfile() {
        String fullName = etFullName.getText().toString().trim();
        String nim = etNim.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (fullName.isEmpty() || nim.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Mohon isi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUser != null) {
            currentUser.setFullName(fullName);
            currentUser.setNim(nim);
            currentUser.setPhone(phone);

            boolean success = databaseHelper.updateUser(currentUser);
            if (success) {
                // Update SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userName", fullName);
                editor.putString("userNim", nim);
                editor.apply();

                Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error: Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
