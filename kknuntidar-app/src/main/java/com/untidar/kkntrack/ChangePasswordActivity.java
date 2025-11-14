package com.untidar.kkntrack;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.untidar.kkntrack.util.UiUtils;
import androidx.appcompat.app.AppCompatActivity;
import com.untidar.kkntrack.database.DatabaseHelper;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnSave, btnCancel;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setTitle("Ubah Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("KKNTrackPrefs", MODE_PRIVATE);

        setupClickListeners();
    }

    private void initViews() {
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changePassword() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            UiUtils.showSnack(this, "Mohon isi semua field");
            return;
        }

        if (newPassword.length() < 6) {
            UiUtils.showSnack(this, "Password baru minimal 6 karakter");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            UiUtils.showSnack(this, "Konfirmasi password tidak cocok");
            return;
        }

        String userEmail = sharedPreferences.getString("userEmail", "");

        // Check current password
        if (!databaseHelper.checkUser(userEmail, currentPassword)) {
            UiUtils.showSnack(this, "Password saat ini salah");
            return;
        }

        // Update password
        boolean success = databaseHelper.updateUserPassword(userEmail, newPassword);
        if (success) {
            UiUtils.showSnack(this, "Password berhasil diubah");
            finish();
        } else {
            UiUtils.showSnack(this, "Gagal mengubah password");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
