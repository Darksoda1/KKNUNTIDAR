package com.untidar.kkntrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.untidar.kkntrack.util.UiUtils;
import androidx.appcompat.app.AppCompatActivity;
import com.untidar.kkntrack.database.DatabaseHelper;
import com.untidar.kkntrack.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etNim, etEmail, etPhone, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        databaseHelper = new DatabaseHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etNim = findViewById(R.id.etNim);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void registerUser() {
        String fullName = etFullName.getText().toString().trim();
        String nim = etNim.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (fullName.isEmpty() || nim.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            UiUtils.showSnack(this, "Mohon isi semua field");
            return;
        }

        if (!password.equals(confirmPassword)) {
            UiUtils.showSnack(this, "Password tidak cocok");
            return;
        }

        if (databaseHelper.checkUserExists(email)) {
            UiUtils.showSnack(this, "Email sudah terdaftar");
            return;
        }

        User user = new User(fullName, nim, email, phone, password);
        long result = databaseHelper.addUser(user);

        if (result != -1) {
            UiUtils.showSnack(this, "Registrasi berhasil");
            finish();
        } else {
            UiUtils.showSnack(this, "Registrasi gagal");
        }
    }
}
