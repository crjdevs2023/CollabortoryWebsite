package com.example.testpdfgenerator;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ExportPdfActivity extends AppCompatActivity {

    Button generatePDFbtn;
    DatabaseHelper dbHelper;
    private static final int PERMISSION_REQUEST_CODE = 200;
    int pageHeight = 1120;
    int pagewidth = 792;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_pdf);

        generatePDFbtn = findViewById(R.id.idBtnGeneratePDF);
        dbHelper = new DatabaseHelper(this);

        // Check for permissions
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        generatePDFbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });
    }

    private void generatePDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint title = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();

        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(15);
        title.setColor(ContextCompat.getColor(this, R.color.black));

        canvas.drawText("User Data Export", 209, 80, title);

        Cursor cursor = dbHelper.getData();
        int yPosition = 200;
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String age = cursor.getString(2);

                // Draw data in the PDF
                canvas.drawText("ID: " + id, 100, yPosition, title);
                canvas.drawText("Name: " + name, 200, yPosition, title);
                canvas.drawText("Age: " + age, 400, yPosition, title);

                yPosition += 30;
            } while (cursor.moveToNext());
        }

        cursor.close();
        pdfDocument.finishPage(myPage);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "UserData.pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(ExportPdfActivity.this, "PDF generated successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ExportPdfActivity.this, "Failed to generate PDF", Toast.LENGTH_SHORT).show();
        }

        pdfDocument.close();
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }
}
