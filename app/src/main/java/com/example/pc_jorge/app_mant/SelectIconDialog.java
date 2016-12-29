package com.example.pc_jorge.app_mant;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Fragmento con diálogo de lista simple donde elegir el icono de la intervencion
 */
public class SelectIconDialog extends DialogFragment {

    public SelectIconDialog() {
    }

    static SelectIconDialog newInstance(String title){
        SelectIconDialog  fragment=new SelectIconDialog ();
        Bundle args=new Bundle();
        args.putString("title",title);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSingleListDialog();
    }

    /**
     * Crea un Diálogo con una lista de selección simple
     *
     * @return La instancia del diálogo
     */
    public AlertDialog createSingleListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final CharSequence[] items = new CharSequence[4];

        items[0] = "1";//Hidraulico
        items[1] = "2";//Mecanico
        items[2] = "3";//Electrico
        items[3] = "4";//Neumatico

        builder.setTitle("Diálogo De Lista").setIcon(android.R.drawable.alert_light_frame)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(
                                getActivity(),
                                "Seleccionaste:" + items[which],
                                Toast.LENGTH_SHORT)
                                .show();

                    }
                });

        return builder.create();
    }

}