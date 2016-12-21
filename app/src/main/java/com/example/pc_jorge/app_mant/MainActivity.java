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
 * Abre listas y formulario y almacena informacion en la base de datos. Queda que aparezca activo
 * el radio button cuando se edita, que no se queden campos en blanco
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

        ImageButton botonAñadirIntervencion =(ImageButton)findViewById(R.id.imageButton_Añadir_MainActivity);
        botonAñadirIntervencion.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        if (view.getId()==findViewById(R.id.imageButton_MainActivity).getId()) {
            abrirMostrarIntervenciones();
        } if (view.getId()==findViewById(R.id.imageButton_Salir_MainActivity).getId()){
            Cancelar();
        }if(view.getId()==findViewById(R.id.imageButton_Añadir_MainActivity).getId()){
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
        startActivityForResult(intentAbrir, 1);
    }
}
