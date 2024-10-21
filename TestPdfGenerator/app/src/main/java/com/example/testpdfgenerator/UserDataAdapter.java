package com.example.testpdfgenerator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.UserViewHolder> {

    private ArrayList<UserModel> userList;
    private Context context;

    public UserDataAdapter(Context context, ArrayList<UserModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel user = userList.get(position);

        // Bind data to views
        holder.idTextView.setText("ID: " + user.getId());
        holder.nameTextView.setText("Name: " + user.getName());
        holder.ageTextView.setText("Age: " + user.getAge());

        // Export PDF button click listener for each card
        holder.exportBtn.setOnClickListener(v -> {
            generatePDF(user.getId(), user.getName(), user.getAge());
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, nameTextView, ageTextView;
        Button exportBtn;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            ageTextView = itemView.findViewById(R.id.ageTextView);
            exportBtn = itemView.findViewById(R.id.exportBtn);
        }
    }

    // Method to generate a PDF for a specific user
    private void generatePDF(String id, String name, String age) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint title = new Paint();

        // Set page dimensions
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(792, 1120, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Set up title and text
        title.setTextSize(18);
        title.setColor(ContextCompat.getColor(context, R.color.black));
        canvas.drawText("User Data Export", 209, 80, title);
        title.setTextSize(15);
        canvas.drawText("ID: " + id, 100, 200, title);
        canvas.drawText("Name: " + name, 100, 250, title);
        canvas.drawText("Age: " + age, 100, 300, title);

        pdfDocument.finishPage(page);

        // Save the PDF file
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "User_" + id + ".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "PDF for User " + id + " exported.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to export PDF.", Toast.LENGTH_SHORT).show();
        }
        pdfDocument.close();
    }
}

