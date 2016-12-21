package com.example.pc_jorge.app_mant;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Clase que carga los datos de la base de datos en 2 ListView en 2 pestañas diferentes diferenciando asi entre
 * mantenimientos y averias.
 */
public class MostrarIntervenciones extends AppCompatActivity {

    TabHost MostrarActividadesTabHost;//TabHost que muestra los mantenimientos y averias
    ArrayList<Intervencion> arrayIntervenciones=new ArrayList<Intervencion>();//Array que almacenara los objetos
    ListView listViewMantenimientos,listViewAveria;
    Intervencion[] lista;
    String nombreBD="miBD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_intervenciones);

        MostrarActividadesTabHost = (TabHost) findViewById(R.id.mostrarActividades);//crea el TabHost
        MostrarActividadesTabHost.setup();//Se activa

        //Se crean las pestañas
        MostrarActividadesTabHost.addTab(MostrarActividadesTabHost.newTabSpec("tabMantenimiento")
                .setIndicator("Mantenimientos").setContent(R.id.pestMantenimiento));
        MostrarActividadesTabHost.addTab(MostrarActividadesTabHost.newTabSpec("tabAverias")
                .setIndicator("Averias").setContent(R.id.pestAveria));

        Log.d("","Pestañas creadas");



        /**Intenta rellenar las listas pero si no hay registros guardados en la base de datos
         * ni de mantenimientos(true) ni de averias(false) avisa al usuario
         */
        if (cargarListas(true) == null && cargarListas(false) == null) {
            Toast.makeText(getApplicationContext(), "No existe ningún registro en la base de datos", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Pulsa en la Intervencion que deses ver", Toast.LENGTH_SHORT).show();
        }
        //Cargara las lista de los mantenimietntos en el ListView si tienen contenido
        if (cargarListas(true) != null) {
            AdaptadorMostrarIntervenciones adaptadorMantenimiento = new AdaptadorMostrarIntervenciones(this, cargarListas(true));
            listViewMantenimientos = (ListView) findViewById(R.id.listViewMantenimientos);
            listViewMantenimientos.setAdapter(adaptadorMantenimiento);
            Log.d("","Escuchando el ListView");
            //Si pulsa sobre una intervencion abre el editor y le pasa como parametro el codigo de la intervencion
            listViewMantenimientos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    //codigo de la intervencion pulsada
                    int codigo = ((Intervencion) parent.getItemAtPosition(position)).getCodigo();
                    Log.d("","Codigo mandado dede Mostrar intervenciones: "+codigo);
                    //abro el Formulario y paso el codigo de la intervencion
                    abrirFormulario(codigo);


                }
            });
        }
        //Cargara las lista de averias en el ListView si tienen contenido
        if (cargarListas(false) != null) {
            AdaptadorMostrarIntervenciones adaptadorAveria = new AdaptadorMostrarIntervenciones(this, cargarListas(false));
            listViewAveria = (ListView) findViewById(R.id.listViewAverias);
            listViewAveria.setAdapter(adaptadorAveria);
            //Si pulsa sobre una averia abre el Formulario y le pasa como parametro el codigo de la intervencion
            listViewAveria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    //codigo de la intervencion pulsada
                    int codigo = ((Intervencion) parent.getItemAtPosition(position)).getCodigo();
                    //abro el editor y paso el codigo de la intervencion
                    abrirFormulario(codigo);
                }
            });
        }
    }

    /**
     * Abre el Formulario pasando como parametro el codigo de la Intervencion o de alta nueva
     */
    private void abrirFormulario(int codigo) {
        Intent intentAbrir=new Intent(this,Formulario.class);
        intentAbrir.putExtra("codigoIntervencion",codigo);
        startActivityForResult(intentAbrir,1);
    }

    /**COMENTO ESTO PORQUE GENERABA UNA EXCEPCION. INTENTAR ARREGLAR
     * Sobreescribo el metodo onResume para que cada vez que tenga el foco la activity
     * ejecute el metodo cargarlistas actualizando asi el contenido

     @Override
     protected void onResume(){
     super.onResume();
     this.onCreate(null);
     }
     */

    /**
     * Metodo para rellenar los listView con los datos almacenados en la base de datos.
     * Consulta la base de datos y instancia un objeto de tipo Intervencion por cada fila
     * almacenada en la tabla.
     * Los amacena en un ArrayList.
     *
     * Condicion: la clausula WHERE de la sentencia.
     * Argumentos de la condicion: que podemos dejar a null si no los necesitamos o bien pasarlos como parametros en un array.
     * Clausula GROUP BY que podemos dejar a null si no los necesitamos.
     * Clausula HAVING que podemos dejar a null si no los necesitamos.
     * Clausula ORDER BY que podemos dejar a null si no los necesitamos.
     *
     * @param tipo Indica true si el array que se quiere almacenar sera de Mantenimientos y false averias
     * @return Un array con los objetos Intervencion almacenados.
     */
    protected Intervencion[] cargarListas(boolean tipo){
        //Almacena las intervenciones
        ArrayList<Intervencion> listaIntervenciones=new ArrayList<Intervencion>();
        Intervencion[] listaIntervencionesADevolver= null;
        Log.d("","Crea el arrayList");
        boolean tipoIntervencion=tipo;
        //Cursor donde se almacena la informacion de la consulta
        Cursor cur=null;
        //Creamos un objeto de la clase auxiliar.
        MiBaseDatosHelper bdhelp =
                new MiBaseDatosHelper(this, nombreBD, null, 1);
        //Abrimos en modo lectura y escritura la base de datos
        SQLiteDatabase bd = bdhelp.getWritableDatabase();
        //Si la base de datos esta abierta.
        if(bd.isOpen()){
            Log.d("","Base de datos abierta");
            //Creo un array con los nombres de las columnas a consultar
            String[] misCampos = new String[] {"CODIGO" ,"NOMBRE","FECHA","TIPO","IMPORTE" ,
                    "KILOMETROS","DESCRIPCION","RUTAIMAGEN","DRAWABLEIMAGEID"};
            //Consulta que almacena en un cursos la informacion recibida.
            if (tipoIntervencion){
                Log.d("","Intenta ejecutar consulta para almacenar en el cursor");
                //Intervenciones de tipo MANTENIMIENTO
                cur = bd.query("INTERVENCIONES", misCampos, "TIPO='M'", null, null, null,null );
                Log.d("","Creado cursor para mantenimiento");
            }else{
                //Intervenciones de tipo AVERIA
                cur = bd.query("INTERVENCIONES", misCampos, "TIPO='A'", null, null, null, null);
                Log.d("","Creado cursor para averias");
            }
            //Recorre el cursor creando los objetos
            if (cur.moveToFirst()) {
                String nombre,fecha,tipoString,descripcion,rutaImagen;
                long importe;
                int codigo,kilometros,drawableImagenID,posicion=0;
                Log.d("","Intenta recorrer el cursor");
                //Recorre el cursor
                do {
                    codigo=cur.getInt(0);
                    nombre=cur.getString(1);
                    fecha=cur.getString(2);
                    tipoString=cur.getString(3);
                    importe=cur.getLong(4);
                    kilometros=cur.getInt(5);
                    descripcion=cur.getString(6);
                    rutaImagen=cur.getString(7);
                    drawableImagenID=cur.getInt(8);
                    listaIntervenciones.add(new Intervencion(codigo, nombre, fecha, tipoString,importe,
                            kilometros,descripcion,rutaImagen,drawableImagenID));
                    Log.d("","Codigo: "+listaIntervenciones.get(posicion).getCodigo()+
                            " nombre: "+listaIntervenciones.get(posicion).getNombre()+
                            " fecha: "+listaIntervenciones.get(posicion).getFecha()+
                            " tipoString: "+listaIntervenciones.get(posicion).getTipo()+
                            " importe: "+listaIntervenciones.get(posicion).getImporte()+
                            " Kilometros: "+listaIntervenciones.get(posicion).getKilometros()+
                            " descripcion: "+listaIntervenciones.get(posicion).getDescripcion()+
                            " rutaImagen: "+listaIntervenciones.get(posicion).getRutaImagen());
                    posicion++;
                } while(cur.moveToNext());
                //Si la lista no es nula se pasa a un array con el tamaño adecuado al numero de entradas de la Tabla
                if (listaIntervenciones!=null){
                    listaIntervencionesADevolver = listaIntervenciones.toArray(new Intervencion[listaIntervenciones.size()]);
                }
                bd.close();
            }
        }
        return listaIntervencionesADevolver;

    }


/** Aqui anadia manualmente registros sin pasar por la base de datos, sino deirectamente en el listview

 //Cargo varios objetos de ejemplo en el array
 arrayIntervenciones.add(new Intervencion("Cambio aceite","23/08/2016","Mantenimiento",40,180000,"Cambio " +
 "de aceite y filtro comprado en recambios REME",null,R.drawable.icono_mantenimiento));
 arrayIntervenciones.add(new Intervencion("Cambio discos","23/08/2015","Mantenimiento",40,180000,"Cambio " +
 "de discos y pastillas comprado en recambios REME",null,R.drawable.icono_mantenimiento));
 arrayIntervenciones.add(new Intervencion("Cambio techo","23/08/2016","Mantenimiento",40,180000,"Cambio " +
 "de techo por descolgarse",null,R.drawable.icono_mantenimiento));
 arrayIntervenciones.add(new Intervencion("Cambio gomas","23/08/2016","Mantenimiento",40,180000,"Cambio " +
 "de ruedas en Noreauto",null,R.drawable.icono_mantenimiento));
 arrayIntervenciones.add(new Intervencion("Cambio puerta","23/08/2016","Mantenimiento",40,180000,"Cambio " +
 "de aceite y filtro comprado en recambios REME",null,R.drawable.icono_mantenimiento));
 arrayIntervenciones.add(new Intervencion("Cambio faro izquierdo","23/08/2016","Mantenimiento",40,180000,"Cambio " +
 "de aceite y filtro comprado en recambios REME",null,R.drawable.icono_mantenimiento));
 arrayIntervenciones.add(new Intervencion("Cambio tusmulastoas","23/08/2016","Mantenimiento",40,180000,"Cambio " +
 "de aceite y filtro comprado en recambios REME",null,R.drawable.icono_mantenimiento));

 //Se convierte a un array para poderlo cargar en el adaptador
 lista = arrayIntervenciones.toArray(new Intervencion[arrayIntervenciones.size()]);


 //Cargo la listview con el array
 AdaptadorMostrarIntervenciones adaptadorMantenimientos=new AdaptadorMostrarIntervenciones(MostrarIntervenciones.this,lista);
 listViewMantenimientos=(ListView)findViewById(R.id.listViewMantenimientos);
 listViewMantenimientos.setAdapter(adaptadorMantenimientos);
 */
}



