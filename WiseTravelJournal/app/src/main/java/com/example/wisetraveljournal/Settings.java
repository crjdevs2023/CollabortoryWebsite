package com.example.wisetraveljournal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Settings extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_IMAGE_REQUEST_CODE = 101;
    private static final String PREFS_NAME = "ThemePrefs";
    private static final String PREF_THEME = "AppTheme";

    Button devsBtn;

    private ImageView themeToggleIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        devsBtn = findViewById(R.id.AboutUs);

        devsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, DevsActivity.class);
                startActivity(intent);
            }
        });

        themeToggleIcon = findViewById(R.id.icon1);
        loadTheme();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_settings);


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_journals) {
                startActivity(new Intent(this, JournalList.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_camera) {
                openCamera();
                return true;
            } else if (item.getItemId() == R.id.nav_settings) {
                return true;
            } else {
                return false;
            }
        });



        themeToggleIcon.setOnClickListener(v -> toggleTheme());
    }

    private void loadTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int theme = sharedPreferences.getInt(PREF_THEME, AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(theme);
        updateThemeToggleIcon(theme);
    }

    private void updateThemeToggleIcon(int theme) {
        if (theme == AppCompatDelegate.MODE_NIGHT_YES) {
            themeToggleIcon.setImageResource(R.drawable.ic_mode_night);
        } else {
            themeToggleIcon.setImageResource(R.drawable.ic_mode_day);
        }
    }

    private void toggleTheme() {
        int currentTheme = AppCompatDelegate.getDefaultNightMode();
        if (currentTheme == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            updateThemeToggleIcon(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            updateThemeToggleIcon(AppCompatDelegate.MODE_NIGHT_NO);
        }

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_THEME, AppCompatDelegate.getDefaultNightMode());
        editor.apply();
    }

    private void openCamera() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST_CODE);
        } else {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
        }
    }
}
