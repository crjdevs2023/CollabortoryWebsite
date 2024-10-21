package com.example.testpdfgenerator;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper dbHelper;
    ArrayList<UserModel> userList;
    UserDataAdapter userDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        recyclerView = findViewById(R.id.idListView);
        dbHelper = new DatabaseHelper(this);

        // Initialize data list and adapter
        userList = new ArrayList<>();
        loadDataFromDatabase();
        userDataAdapter = new UserDataAdapter(this, userList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userDataAdapter);
    }

    // Fetch data from SQLite database and populate the userList
    private void loadDataFromDatabase() {
        Cursor cursor = dbHelper.getData();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String age = cursor.getString(2);
                userList.add(new UserModel(id, name, age));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
