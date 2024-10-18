package com.example.wisetraveljournal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    private ArrayList journal_id, journal_title, journal_date, journal_description;
    CustomAdapter(Context context, ArrayList journal_id, ArrayList journal_title, ArrayList journal_date, ArrayList journal_description){

        this.context = context;
        this.journal_id = journal_id;
        this.journal_title = journal_title;
        this.journal_date = journal_date;
        this.journal_description = journal_description;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.journal_item, parent, false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.journalId.setText(String.valueOf(journal_id.get(position)));
        holder.journalTitle.setText(String.valueOf(journal_title.get(position)));
        holder.journalDate.setText(String.valueOf(journal_date.get(position)));
        holder.journalDescription.setText(String.valueOf(journal_description.get(position)));
    }

    @Override
    public int getItemCount() {
        return journal_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView journalId, journalTitle, journalDate, journalDescription;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            journalId = itemView.findViewById(R.id.journalId);
            journalTitle = itemView.findViewById(R.id.journalTitle);
            journalDate = itemView.findViewById(R.id.journalDate);
            journalDescription = itemView.findViewById(R.id.journalDescription);
        }
    }
}
