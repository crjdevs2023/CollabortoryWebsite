package com.example.wisetraveljournal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    private List<JournalEntry> journalList;

    // Constructor
    public JournalAdapter(List<JournalEntry> journalList) {
        this.journalList = journalList;
    }

    // Create view holder class
    public static class JournalViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView, dateView, geoTagView, descriptionView;

        public JournalViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.journalTitle);
            dateView = itemView.findViewById(R.id.journalDate);
            descriptionView = itemView.findViewById(R.id.journalDescription);
        }
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.journal_item, parent, false);
        return new JournalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        JournalEntry currentJournal = journalList.get(position);
        holder.titleView.setText(currentJournal.getTitle());
        holder.dateView.setText(currentJournal.getDate());
        holder.geoTagView.setText(currentJournal.getGeoTag());
        holder.descriptionView.setText(currentJournal.getDescription());
    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }
}
