package com.example.pc_jorge.app_mant;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Crear Fragment con ImageButton para elegir el icono de la intervencion
 * Crear procedimiento para borrar intervencion
 * Crear menu
 *
 *
 *
 *
 */

public class MainActivity extends Activity implements OnClickListener{

    String nombreBD="miBD";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton botonConsultar=(ImageButton)findViewById(R.id.imageButton_MainActivity);
        botonConsultar.setOnClickListener(this);

        ImageButton botonSalirMainActivity=(ImageButton)findViewById(R.id.imageButton_Salir_MainActivity);
        botonSalirMainActivity.setOnClickListener(this);

        ImageButton botonAnadirIntervencion =(ImageButton)findViewById(R.id.imageButton_Anadir_MainActivity);
        botonAnadirIntervencion.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        if (view.getId()==findViewById(R.id.imageButton_MainActivity).getId()) {
            abrirMostrarIntervenciones();
        } if (view.getId()==findViewById(R.id.imageButton_Salir_MainActivity).getId()){
            Cancelar();
        }if(view.getId()==findViewById(R.id.imageButton_Anadir_MainActivity).getId()){
            abrirFormulario(0);
        }
    }

    private void Cancelar() {
        System.exit(0);
        //finish();
    }



    private void abrirMostrarIntervenciones() {
        Intent intentAbrir = new Intent(MainActivity.this, MostrarIntervenciones.class);
        startActivity(intentAbrir);
    }

    //Abre el formulario pasandole el codigo
    private void abrirFormulario(int codigo) {
        Intent intentAbrir = new Intent(MainActivity.this, Formulario.class);
        intentAbrir.putExtra("codigoIntervencion", codigo);
        startActivity(intentAbrir);
    }
}
