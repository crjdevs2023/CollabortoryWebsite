package com.example.wisetraveljournal;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate: Starting MainActivity");

        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "onCreate: setContentView completed");

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            Toast.makeText(this, "Connected to a WiFi network", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No internet connection, Go offline Mode.", Toast.LENGTH_SHORT).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Log.d(LOG_TAG, "onCreate: Applying window insets");
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button onlineButton = findViewById(R.id.Setupbutton);
        Button offlineButton = findViewById(R.id.offlineButton);

        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkStatus();
            }
        });
        offlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Offline Button Clicked!");
                Toast.makeText(MainActivity.this, "You are in Offline Mode", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), OfflineMode.class);
                startActivity(intent);
            }
        });
    }

    private void checkNetworkStatus() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            Toast.makeText(this, "You are in Online Mode", Toast.LENGTH_SHORT).show();
            Log.d("MainActivity", "New Journal Setup Clicked!");
            Intent intent = new Intent(this, SetupJournal.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}
