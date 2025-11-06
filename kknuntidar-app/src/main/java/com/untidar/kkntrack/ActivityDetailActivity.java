package com.untidar.kkntrack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.untidar.kkntrack.database.DatabaseHelper;
import com.untidar.kkntrack.model.Activity;
import java.io.File;

public class ActivityDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDescription, tvDate, tvTime, tvLocation;
    private ImageView ivPhoto;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_detail);

        getSupportActionBar().setTitle("Detail Kegiatan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        databaseHelper = new DatabaseHelper(this);

        int activityId = getIntent().getIntExtra("activity_id", -1);
        if (activityId != -1) {
            loadActivityDetail(activityId);
        }
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvLocation = findViewById(R.id.tvLocation);
        ivPhoto = findViewById(R.id.ivPhoto);
    }

    private void loadActivityDetail(int activityId) {
        Activity activity = databaseHelper.getActivityById(activityId);
        if (activity != null) {
            tvTitle.setText(activity.getTitle());
            tvDescription.setText(activity.getDescription());
            tvDate.setText(activity.getDate());
            tvTime.setText(activity.getTime());
            tvLocation.setText(activity.getLocation());

            // Load photo if exists
            if (activity.getPhotoPath() != null && !activity.getPhotoPath().isEmpty()) {
                File photoFile = new File(activity.getPhotoPath());
                if (photoFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(activity.getPhotoPath());
                    ivPhoto.setImageBitmap(bitmap);
                    ivPhoto.setVisibility(View.VISIBLE);
                } else {
                    ivPhoto.setVisibility(View.GONE);
                }
            } else {
                ivPhoto.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
