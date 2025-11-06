package com.untidar.kkntrack;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.untidar.kkntrack.adapter.ActivityAdapter;
import com.untidar.kkntrack.database.DatabaseHelper;
import com.untidar.kkntrack.model.Activity;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private TextView tvTotalActivities;
    private RecyclerView recyclerView;
    private ActivityAdapter adapter;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        getSupportActionBar().setTitle("Laporan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("KKNTrackPrefs", MODE_PRIVATE);

        setupRecyclerView();
        loadReport();
    }

    private void initViews() {
        tvTotalActivities = findViewById(R.id.tvTotalActivities);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadReport() {
        String userEmail = sharedPreferences.getString("userEmail", "");
        List<Activity> activities = databaseHelper.getActivitiesByUser(userEmail);

        tvTotalActivities.setText("Total Kegiatan: " + activities.size());

        adapter = new ActivityAdapter(activities);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
