package com.untidar.kkntrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private LinearLayout layoutChangePassword, layoutAbout, layoutHelp, layoutLogout;
    private TextView tvUserInfo;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Pengaturan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        sharedPreferences = getSharedPreferences("KKNTrackPrefs", MODE_PRIVATE);

        setupUserInfo();
        setupClickListeners();
    }

    private void initViews() {
        tvUserInfo = findViewById(R.id.tvUserInfo);
        layoutChangePassword = findViewById(R.id.layoutChangePassword);
        layoutAbout = findViewById(R.id.layoutAbout);
        layoutHelp = findViewById(R.id.layoutHelp);
        layoutLogout = findViewById(R.id.layoutLogout);
    }

    private void setupUserInfo() {
        String userName = sharedPreferences.getString("userName", "User");
        String userEmail = sharedPreferences.getString("userEmail", "");
        tvUserInfo.setText(userName + "\n" + userEmail);
    }

    private void setupClickListeners() {
        layoutChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
            }
        });

        layoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });

        layoutHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpDialog();
            }
        });

        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Tentang Aplikasi")
                .setMessage("KKN Track Universitas Tidar\nVersi 1.0\n\nAplikasi untuk mencatat dan melacak kegiatan KKN mahasiswa Universitas Tidar.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Bantuan")
                .setMessage("Cara menggunakan aplikasi:\n\n1. Login dengan akun Anda\n2. Tambah kegiatan di menu Log Kegiatan\n3. Lihat laporan di menu Laporan\n4. Cek pengumuman terbaru\n5. Gunakan fitur lokasi untuk tracking\n\nJika ada masalah, hubungi admin.")
                .setPositiveButton("OK", null)
                .show();
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
