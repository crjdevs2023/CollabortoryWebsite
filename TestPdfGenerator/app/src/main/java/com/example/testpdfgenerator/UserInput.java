package com.example.testpdfgenerator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UserInput extends AppCompatActivity {

    EditText nameEdt, ageEdt;
    Button saveDataBtn;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        // Initialize views
        nameEdt = findViewById(R.id.idEdtName);
        ageEdt = findViewById(R.id.idEdtAge);
        saveDataBtn = findViewById(R.id.idBtnSave);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Save data to SQLite database
        saveDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdt.getText().toString();
                String age = ageEdt.getText().toString();

                if (!name.isEmpty() && !age.isEmpty()) {
                    boolean isInserted = dbHelper.insertData(name, age);
                    if (isInserted) {
                        Toast.makeText(UserInput.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                        nameEdt.setText("");
                        ageEdt.setText("");
                    } else {
                        Toast.makeText(UserInput.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserInput.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
