package com.example.wisetraveljournal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class JournalList extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_IMAGE_REQUEST_CODE = 101;
    private static final String PREFS_NAME = "ThemePrefs";
    private static final String PREF_THEME = "AppTheme";

    private RecyclerView journalRecyclerView;
    private JournalAdapter journalAdapter;
    private ArrayList<JournalEntry> journalList;

    RecyclerView recyclerView;
    ArrayList<String> journal_id, journal_title, journal_date, journal_description;
    MyDatabaseHelper myDB;
    CustomAdapter customAdapter;


    private ImageView themeToggleIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

        recyclerView = findViewById(R.id.journalRecyclerView);
        myDB = new MyDatabaseHelper(JournalList.this);
        journal_id = new ArrayList<>();
        journal_title = new ArrayList<>();
        journal_date = new ArrayList<>();
        journal_description = new ArrayList<>();

        customAdapter = new CustomAdapter(this, journal_id, journal_title, journal_date, journal_description);
        recyclerView.setAdapter(customAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(JournalList.this));


        displayData();

        themeToggleIcon = findViewById(R.id.icon1);
        loadTheme();



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_journals);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Intent homeIntent = new Intent(this, MainActivity.class);
                startActivity(homeIntent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_journals) {
                return true;
            } else if (item.getItemId() == R.id.nav_camera) {
                openCamera();
                return true;
            } else if (item.getItemId() == R.id.nav_settings) {
                Intent settingsIntent = new Intent(this, Settings.class);
                startActivity(settingsIntent);
                finish();
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

    void displayData() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                journal_id.add(cursor.getString(0));
                journal_title.add(cursor.getString(1));
                journal_date.add(cursor.getString(2));
                journal_description.add(cursor.getString(3));
            }
        }
    }
}
