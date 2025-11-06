package com.untidar.kkntrack;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.untidar.kkntrack.adapter.AnnouncementAdapter;
import com.untidar.kkntrack.model.Announcement;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AnnouncementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        getSupportActionBar().setTitle("Pengumuman");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        setupRecyclerView();
        loadAnnouncements();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        announcements.add(new Announcement("Selamat Datang di KKN Track",
                "Selamat datang di aplikasi KKN Track Universitas Tidar. Gunakan aplikasi ini untuk mencatat kegiatan KKN Anda.",
                "01/01/2024"));
        announcements.add(new Announcement("Panduan Penggunaan",
                "Silakan baca panduan penggunaan aplikasi di menu bantuan atau hubungi admin jika ada pertanyaan.",
                "02/01/2024"));
        announcements.add(new Announcement("Update Aplikasi",
                "Aplikasi telah diperbarui dengan fitur-fitur terbaru. Pastikan Anda menggunakan versi terbaru.",
                "03/01/2024"));

        adapter = new AnnouncementAdapter(announcements);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
