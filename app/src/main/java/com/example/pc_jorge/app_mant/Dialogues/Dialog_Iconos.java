package com.example.pc_jorge.app_mant.Dialogues;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.example.pc_jorge.app_mant.Activities.Formulario;
import com.example.pc_jorge.app_mant.R;

/**
 * Created by PC-Jorge on 01/01/2017.
 */
public class Dialog_Iconos extends DialogFragment {
    private static final String TAG = Dialog_Iconos.class.getSimpleName();

    public Dialog_Iconos() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createLoginDialogo();
    }

    /**
     * Crea un diálogo con personalizado para comportarse
     * como formulario de login
     *
     * @return Diálogo
     */
    public AlertDialog createLoginDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_seleccionar_icono, null);


        builder.setView(v);

        ImageButton IBneumatica=(ImageButton)v.findViewById(R.id.imageButton_DialogIconoNeumatica);
        ImageButton IBelectrica=(ImageButton)v.findViewById(R.id.imageButton_DialogIconoElectrica);
        ImageButton IBhidraulica=(ImageButton)v.findViewById(R.id.imageButton_DialogIconoHidraulica);
        ImageButton IBmecanica=(ImageButton)v.findViewById(R.id.imageButton_DialogIconoMecanica);

        IBelectrica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Formulario.setIconSelection(R.drawable.electrica);
                dismiss();
            }
        });
        IBneumatica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Formulario.setIconSelection(R.drawable.neumatica);
                dismiss();

            }
        });
        IBhidraulica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Formulario.setIconSelection(R.drawable.hidraulica);
                dismiss();

            }
        });
        IBmecanica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Formulario.setIconSelection(R.drawable.mecanica);
                dismiss();

            }
        });
        return builder.create();
    }

}
