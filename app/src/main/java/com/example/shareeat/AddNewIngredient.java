package com.example.shareeat;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.core.content.ContextCompat;

import com.example.shareeat.adapter.IngrAdapter;
import com.example.shareeat.modele.ConnexionBD;
import com.example.shareeat.modele.Ingredient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.SQLException;
import java.util.List;

public class AddNewIngredient extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private EditText newIngrText;
    private Button newIngrSaveButton;
    private ConnexionBD db;


    public static AddNewIngredient newInstance() {
        return new AddNewIngredient();
    }


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.component_add_new_ingredient, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        newIngrText = getView().findViewById(R.id.addNewIngredient);
        newIngrSaveButton = getView().findViewById(R.id.btnSave);

        boolean isUpdated = false;

        try {
            db = new ConnexionBD();


            final Bundle bundle = getArguments();
            if (bundle != null ) {
                isUpdated = true;
                String ingr = bundle.getString("ingr");
                newIngrText.setText(ingr);
                if (ingr.length()>0) {
                    newIngrSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.vert_shareeat));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        newIngrText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    newIngrSaveButton.setEnabled(false);
                    newIngrSaveButton.setTextColor(Color.GRAY);
                }else {
                    newIngrSaveButton.setEnabled(true);
                    newIngrSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.vert_shareeat));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final boolean finalIsUpdated = isUpdated;
        // savoir si on souhaite modifier un ingredient déjà existant ou créer un nouvel ingrédient
        newIngrSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = newIngrText.getText().toString();
                if (finalIsUpdated) {
                    try {
                        // Update les ingredients existants (si besoin)
                        List<Ingredient> ingrs = db.getTousIngredients();
                        for (Ingredient i : ingrs)
                            i.getId();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    // Crée un nouvel ingrédient
                    if (!text.isEmpty()) {
                        try {
                            db.ajouterIngr(text);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                dismiss();
            }
        });
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener)activity).handleDialogClose(dialog);// Notifie à AddPlatActivity la mise à jour de la liste
        } else {

        }
    }
}
