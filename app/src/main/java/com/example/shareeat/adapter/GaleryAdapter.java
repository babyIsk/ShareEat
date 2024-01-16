package com.example.shareeat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareeat.OnItemListener;
import com.example.shareeat.R;

import java.util.ArrayList;

public class GaleryAdapter extends RecyclerView.Adapter<GaleryAdapter.ViewHolder> {

    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;

    public GaleryAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public GaleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.componant_galery_calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new ViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GaleryAdapter.ViewHolder holder, int position) {
        String dayText = daysOfMonth.get(position);

        // Vérifiez si la valeur de dayText est une chaîne vide
        if (!dayText.isEmpty()) {
            holder.dayOfMonth.setText(dayText);
            holder.itemView.setVisibility(View.VISIBLE);
        } else {
            // Si c'est une chaîne vide
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    //Modification de la classe ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView dayOfMonth;
        private final OnItemListener onItemListener;
        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            dayOfMonth = itemView.findViewById(R.id.tvCellDay);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(getAbsoluteAdapterPosition(), (String) dayOfMonth.getText());
        }
    }
}