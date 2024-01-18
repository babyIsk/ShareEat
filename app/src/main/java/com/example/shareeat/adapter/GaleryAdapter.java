package com.example.shareeat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareeat.OnItemListener;
import com.example.shareeat.R;
import com.example.shareeat.modele.Plat;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GaleryAdapter extends RecyclerView.Adapter<GaleryAdapter.ViewHolder> {

    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    private List<Plat> listeRecettesUtilisateur;
    private List<Plat> recettesPourChaqueJour;
    private Context context;

    public GaleryAdapter(Context context, ArrayList<String> daysOfMonth, List<Plat> recettesPourChaqueJour, OnItemListener onItemListener) {
        this.context = context;
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.listeRecettesUtilisateur = new ArrayList<>();
        this.recettesPourChaqueJour = recettesPourChaqueJour;
    }

    @NonNull
    @Override
    public GaleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.componant_galery_calendar_cell, parent, false);
        Log.d("GaleryAdapter", "onCreateViewHolder: View created successfully");

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
            int recettePosition = position;
            Log.d("GaleryAdapter", "Position dans la liste recettesPourChaqueJour : " + recettePosition);

            // Vérifiez si l'utilisateur a posté un plat pour ce jour
            if (recettePosition >= 0 && recettePosition < recettesPourChaqueJour.size()) {
                Plat plat = recettesPourChaqueJour.get(recettePosition);
                if (plat != null && plat.getImageUrl() != null) {
                    Log.d("GaleryAdapter", "Plat non null : " + plat);
                    Picasso.get().load(plat.getImageUrl()).into(holder.imageRecette);
                } else {
                    Log.e("GaleryAdapter", "Plat est null ou son URL est null pour la position : " + recettePosition);
                    // Faites quelque chose pour gérer cette situation, par exemple, afficher une image par défaut ou ne rien faire.
                }
                //if (plat != null && plat.isAPostePlat()) {
                    //holder.itemView.setVisibility(View.VISIBLE);
                    //Picasso.get().load(plat.getImageUrl()).into(holder.imageRecette);
                    //return;
                //}
            }
        }

        // Si aucune condition n'est remplie, masquez la vue
        holder.itemView.setVisibility(View.GONE);
    }

    public void setlisteRecettesUtilisateur(List<Plat> recettes) {
        this.listeRecettesUtilisateur = recettes;
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    //Modification de la classe ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView dayOfMonth;
        private final OnItemListener onItemListener;
        private final ImageView imageRecette;
        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            dayOfMonth = itemView.findViewById(R.id.tvCellDay);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);

            imageRecette = itemView.findViewById(R.id.imgPlatCell);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(getAbsoluteAdapterPosition(), (String) dayOfMonth.getText());
        }
    }
}