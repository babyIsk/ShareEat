package com.example.shareeat.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareeat.AddNewIngredient;
import com.example.shareeat.AddPlatActivity;
import com.example.shareeat.R;
import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.Ingredient;

import java.sql.SQLException;
import java.util.List;

public class IngrAdapter extends RecyclerView.Adapter<IngrAdapter.ViewHolder>{
    private List<Ingredient> ingredientList;
    private AddPlatActivity activity; //notre context
    public ConnexionBD db;
    private FragmentManager fragmentManager;

    public IngrAdapter(ConnexionBD db, AddPlatActivity activity, FragmentManager fragmentManager, List<Ingredient> ingredientList) {
        this.db = db;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
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
        holder.ingr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    try {
                        db.updateStatusIngr(item.getId(), 1);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        db.updateStatusIngr(item.getId(), 0);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
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

    public Context getContext() {
        return activity;
    }

    public void editItem(int position){
        Ingredient item = ingredientList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("ingredient", item.getIngr());
        AddNewIngredient fragment = AddNewIngredient.newInstance();

        fragment.setArguments(bundle);
        fragment.show(fragmentManager, AddNewIngredient.TAG);
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
