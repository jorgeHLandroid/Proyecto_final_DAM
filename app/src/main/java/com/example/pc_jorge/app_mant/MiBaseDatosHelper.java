package com.example.pc_jorge.app_mant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Clase auxiliar para trabajar con base de datos SQLite
 * @author Jorge
 *
 */
public class MiBaseDatosHelper extends SQLiteOpenHelper{

    String nombreBD="miBD";

    public MiBaseDatosHelper(Context context, String name,
                             CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE INTERVENCIONES(CODIGO INT(2),NOMBRE VARCHAR(25)," +
                "FECHA VARCHAR(10),TIPO VARCHAR (1),IMPORTE DOUBLE(7,2), KILOMETROS INT," +
                "DESCRIPCION VARCHAR (100),RUTAIMAGEN VARCHAR(100),DRAWABLEIMAGEID INT(2))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Se elimina la version anterior
        db.execSQL("DROP TABLE IF EXIST INTERVENCIONES");
        //Se actualiza a la nueva version
        db.execSQL("CREATE TABLE INTERVENCIONES(CODIGO INT(2),NOMBRE VARCHAR(25)," +
                "FECHA VARCHAR(10),TIPO VARCHAR (1),IMPORTE DOUBLE(7,2), KILOMETROS INT," +
                "DESCRIPCION VARCHAR (100),RUTAIMAGEN VARCHAR(100),DRAWABLEIMAGEID INT(2));");
    }

    public String getNombreBD() {
        return nombreBD;
    }
}

