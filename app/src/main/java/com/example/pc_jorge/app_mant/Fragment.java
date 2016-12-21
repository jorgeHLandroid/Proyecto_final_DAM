package com.example.pc_jorge.app_mant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Fragment con texto,icono,Aceptar y Cancelar.
 * Created by pc-Jorge on 25/08/2016.
 */


public class Fragment extends DialogFragment {
    static Fragment newInstance (String title){
        Fragment fragment=new Fragment();
        Bundle args=new Bundle();
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String title = getArguments().getString("title");
        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.icono_mantenimiento)
                .setTitle(title)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        ((Formulario)getActivity()).doPositiveClick();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        ((Formulario)getActivity()).doNegativeClick();
                    }
                }).create();

    }
}

