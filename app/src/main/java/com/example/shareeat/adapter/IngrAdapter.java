package com.example.shareeat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareeat.AddPlatActivity;
import com.example.shareeat.R;
import com.example.shareeat.modele.Ingredient;

import java.util.List;

public class IngrAdapter extends RecyclerView.Adapter<IngrAdapter.ViewHolder>{
    private List<Ingredient> ingredientList;
    private AddPlatActivity activity; //notre context

    public IngrAdapter(AddPlatActivity activity, List<Ingredient> ingredientList) {
        this.activity = activity;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_ingredient, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngrAdapter.ViewHolder holder, int position) {
        Ingredient item = ingredientList.get(position);
        holder.ingr.setText(item.getIngr()); //afficher le nom de l'ingredient
        holder.ingr.setChecked(intToBoolean(item.getStatus()));
    }

    //fonction qui va convertir un int en boolean afin d'utiliser ce boolean pour setChecked la CheckBox
    // return : true
    private boolean intToBoolean(int number) {
        return number!=0;
    }

    public void setIngredients (List<Ingredient> ingredientsList) {
        this.ingredientList = ingredientsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    //Modification de la classe ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox ingr;
        ViewHolder(View view) {
            super(view);
            ingr = view.findViewById(R.id.ingrCheckBox);
        }
    }

}
