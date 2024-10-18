package com.example.wisetraveljournal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SetupJournal extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_IMAGE_REQUEST_CODE = 101;
    private static final String PREFS_NAME = "ThemePrefs";
    private static final String PREF_THEME = "AppTheme";

    EditText title_text, date_text, description_text, location_tag;
    private List<JournalEntry> journalList;
    Button saveButton;
    private ImageView themeToggleIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_journal);

        title_text = findViewById(R.id.title_text);
        date_text = findViewById(R.id.date_text);
        description_text = findViewById(R.id.description_text);
        location_tag = findViewById(R.id.location_tag);
        saveButton = findViewById(R.id.saveButton);
        journalList = new ArrayList<>();
        themeToggleIcon = findViewById(R.id.icon1);
        loadTheme();

        themeToggleIcon.setOnClickListener(v -> toggleTheme());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = title_text.getText().toString().trim();
                String date = date_text.getText().toString().trim();
                String description = description_text.getText().toString().trim();

                MyDatabaseHelper myDB = new MyDatabaseHelper(SetupJournal.this);
                myDB.addJournal(title, date, description);
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                Intent homeIntent = new Intent(this, MainActivity.class);
                startActivity(homeIntent);
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_journals) {
                launchJournalListActivity();
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

    private void launchJournalListActivity() {
        Log.d("SetupJournal", "Journal List Selected");
        Intent intent = new Intent(SetupJournal.this, JournalList.class);
        intent.putExtra("journalList", (ArrayList<JournalEntry>) journalList);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Log.e("SetupJournal", "Camera permission denied");
            }
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
    private void saveJournalEntry() {
        // Get values from fields
        String title = title_text.getText().toString();
        String date = date_text.getText().toString();
        String geoTag = location_tag.getText().toString();
        String description = description_text.getText().toString();

        // Simple validation
        if (title.isEmpty() || date.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Create new journal entry and add to list
            JournalEntry newJournal = new JournalEntry(title, date, geoTag, description);
            journalList.add(newJournal);

            // Clear fields after saving
            title_text.setText("");
            date_text.setText("");
            location_tag.setText("");
            description_text.setText("");

            Toast.makeText(this, "Journal entry saved!", Toast.LENGTH_SHORT).show();
        }
    }
}
