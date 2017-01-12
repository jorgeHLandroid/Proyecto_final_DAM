package com.example.pc_jorge.app_mant.Dialogues;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.pc_jorge.app_mant.Activities.Formulario;
import com.example.pc_jorge.app_mant.R;

/**
 * Dialog_Alert_empty_fields con texto,icono,Aceptar y Cancelar.
 * Created by pc-Jorge on 25/08/2016.
 */
public class Dialog_Alert_empty_fields extends DialogFragment {
    public static Dialog_Alert_empty_fields newInstance (String title){
        Dialog_Alert_empty_fields dialogAlertemptyfields =new Dialog_Alert_empty_fields();
        Bundle args=new Bundle();
        args.putString("title",title);
        dialogAlertemptyfields.setArguments(args);
        return dialogAlertemptyfields;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String title = getArguments().getString("title");
        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.advertencia)
                .setTitle(title).setMessage(R.string.Debe_completar_todos_los_campos)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        ((Formulario)getActivity()).doPositiveClick();
                    }
                })
                /**.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        ((Formulario)getActivity()).doNegativeClick();
                    }
                })
                 */.create();

    }
}

