package com.untidar.kkntrack;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.untidar.kkntrack.util.UiUtils;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.untidar.kkntrack.adapter.ActivityAdapter;
import com.untidar.kkntrack.database.DatabaseHelper;
import com.untidar.kkntrack.model.Activity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityLogActivity extends AppCompatActivity implements ActivityAdapter.OnActivityClickListener {

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_CAMERA = 101;
    private static final int REQUEST_GALLERY = 102;

    private RecyclerView recyclerView;
    private ActivityAdapter adapter;
    private FloatingActionButton fabAdd;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private List<Activity> activities;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_log);

        getSupportActionBar().setTitle("Log Kegiatan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("KKNTrackPrefs", MODE_PRIVATE);

        setupRecyclerView();
        loadActivities();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddActivityDialog();
            }
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadActivities() {
        String userEmail = sharedPreferences.getString("userEmail", "");
        activities = databaseHelper.getActivitiesByUser(userEmail);
        adapter = new ActivityAdapter(activities, this);
        recyclerView.setAdapter(adapter);

        if (activities.isEmpty()) {
            UiUtils.showSnackLong(this, "Belum ada kegiatan. Tambahkan kegiatan pertama Anda!");
        }
    }

    private void showAddActivityDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_activity, null);
        dialog.setContentView(view);

        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etDescription = view.findViewById(R.id.etDescription);
        EditText etDate = view.findViewById(R.id.etDate);
        EditText etTime = view.findViewById(R.id.etTime);
        EditText etLocation = view.findViewById(R.id.etLocation);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnAddPhoto = view.findViewById(R.id.btnAddPhoto);
        ImageView ivPhoto = view.findViewById(R.id.ivPhoto);

        // Set current date and time as default
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        etDate.setText(dateFormat.format(calendar.getTime()));
        etTime.setText(timeFormat.format(calendar.getTime()));

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(etDate);
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(etTime);
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoOptions(ivPhoto);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String date = etDate.getText().toString().trim();
                String time = etTime.getText().toString().trim();
                String location = etLocation.getText().toString().trim();

                if (title.isEmpty() || description.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty()) {
                    UiUtils.showSnack(ActivityLogActivity.this, "Mohon isi semua field");
                    return;
                }

                String userEmail = sharedPreferences.getString("userEmail", "");
                Activity activity = new Activity(title, description, date, time, location, userEmail, currentPhotoPath);
                long result = databaseHelper.addActivity(activity);

                if (result != -1) {
                    UiUtils.showSnack(ActivityLogActivity.this, "Kegiatan berhasil ditambahkan");
                    dialog.dismiss();
                    loadActivities();
                    currentPhotoPath = null; // Reset photo path
                } else {
                    UiUtils.showSnack(ActivityLogActivity.this, "Gagal menambahkan kegiatan");
                }
            }
        });

        dialog.show();
    }

    private void showPhotoOptions(ImageView imageView) {
        String[] options = {"Ambil Foto", "Pilih dari Galeri"};

        new AlertDialog.Builder(this)
                .setTitle("Pilih Foto")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        checkCameraPermission();
                    } else {
                        openGallery();
                    }
                })
                .show();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                UiUtils.showSnack(this, "Error creating image file");
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.untidar.kkntrack.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "KKN_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_CAMERA) {
                UiUtils.showSnack(this, "Foto berhasil diambil");
            } else if (requestCode == REQUEST_GALLERY && data != null) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    currentPhotoPath = saveImageToInternalStorage(bitmap);
                    UiUtils.showSnack(this, "Foto berhasil dipilih");
                } catch (IOException e) {
                    UiUtils.showSnack(this, "Error loading image");
                }
            }
        }
    }

    private String saveImageToInternalStorage(Bitmap bitmap) {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "KKN_" + timeStamp + ".jpg";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(storageDir, imageFileName);

            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String date = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
                    editText.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    editText.setText(time);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    @Override
    public void onActivityClick(Activity activity) {
        Intent intent = new Intent(this, ActivityDetailActivity.class);
        intent.putExtra("activity_id", activity.getId());
        startActivity(intent);
    }

    @Override
    public void onActivityEdit(Activity activity) {
        showEditActivityDialog(activity);
    }

    @Override
    public void onActivityDelete(Activity activity) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Kegiatan")
                .setMessage("Apakah Anda yakin ingin menghapus kegiatan ini?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    boolean success = databaseHelper.deleteActivity(activity.getId());
                    if (success) {
                        UiUtils.showSnack(ActivityLogActivity.this, "Kegiatan berhasil dihapus");
                        loadActivities();
                    } else {
                        UiUtils.showSnack(ActivityLogActivity.this, "Gagal menghapus kegiatan");
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void showEditActivityDialog(Activity activity) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_activity, null);
        dialog.setContentView(view);

        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etDescription = view.findViewById(R.id.etDescription);
        EditText etDate = view.findViewById(R.id.etDate);
        EditText etTime = view.findViewById(R.id.etTime);
        EditText etLocation = view.findViewById(R.id.etLocation);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        // Pre-fill with existing data
        etTitle.setText(activity.getTitle());
        etDescription.setText(activity.getDescription());
        etDate.setText(activity.getDate());
        etTime.setText(activity.getTime());
        etLocation.setText(activity.getLocation());
        btnSave.setText("Update");

        etDate.setOnClickListener(v -> showDatePicker(etDate));
        etTime.setOnClickListener(v -> showTimePicker(etTime));

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String time = etTime.getText().toString().trim();
            String location = etLocation.getText().toString().trim();

                if (title.isEmpty() || description.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty()) {
                UiUtils.showSnack(ActivityLogActivity.this, "Mohon isi semua field");
                return;
            }

            activity.setTitle(title);
            activity.setDescription(description);
            activity.setDate(date);
            activity.setTime(time);
            activity.setLocation(location);

            boolean success = databaseHelper.updateActivity(activity);
            if (success) {
                UiUtils.showSnack(ActivityLogActivity.this, "Kegiatan berhasil diperbarui");
                dialog.dismiss();
                loadActivities();
            } else {
                UiUtils.showSnack(ActivityLogActivity.this, "Gagal memperbarui kegiatan");
            }
        });

        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                UiUtils.showSnack(this, "Izin kamera diperlukan untuk mengambil foto");
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
