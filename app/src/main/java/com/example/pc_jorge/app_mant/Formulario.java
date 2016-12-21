package com.example.pc_jorge.app_mant;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.Calendar;

/**
 * Formulario con los datos que se almacenaran respecto a la intervencion.
 * Esta Activity se usa tanto para nuevas altas como para editar una intervencion ya creado.
 * Se recibe el argumento 'codigo' desde MainActivity, si es 0 sera un alta nueva, si recibe
 * otro codigo cargara los valores de los campos en el formulario para que puedan ser editados.
 */
public class Formulario extends AppCompatActivity implements View.OnClickListener {
    String nombreBD="miBD";
    static String fecha="Fecha";
    static TextView fechaClickableTextView;
    TextView descripcionTextView;
    RadioButton radioButtonMantenimiento,radioButtonAveria;
    //variables para insertar en la BD
    private String nombrePasado,fechaPasado,descripcionPasado,rutaImagenPasado,tipoPasado;
    private double importePasado;
    private int kilometrosPasado,drawableImageIdPasado,codigoPasado,codigoIntervencion;
    EditText nombreEditText,importeEditText,kilometrosEditText,descripcionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario);

        descripcionTextView = (TextView)findViewById(R.id.textView_Descripcion_Formulario);
        fechaClickableTextView = (TextView)findViewById(R.id.textView_FechaFormulario);
        fechaClickableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });
        nombreEditText=(EditText)findViewById((R.id.editText_NombreFormulario));
        importeEditText=(EditText)findViewById((R.id.editText_Importe));
        kilometrosEditText=(EditText)findViewById((R.id.editText_Kilometros));
        descripcionEditText=(EditText)findViewById((R.id.editTextDescripcionFormulario));
        radioButtonAveria=(RadioButton)findViewById(R.id.radioButton_Averia);
        radioButtonMantenimiento=(RadioButton)findViewById(R.id.radioButton_Mantenimiento);

        Button botonAceptar=(Button)findViewById(R.id.button_AceptarFormulario);
        botonAceptar.setOnClickListener(this);
        Button botonCancelar=(Button)findViewById(R.id.button_Cancelar);
        botonCancelar.setOnClickListener(this);

        //Recojo el codigo pasado por el listView
        Intent intentRecibir=getIntent();
        codigoIntervencion= (Integer) intentRecibir.getExtras().get("codigoIntervencion");
        Log.d("","Codigo recibido por el Formulario: "+codigoIntervencion);
        //Si el codigo es 0 significa un alta, cualquier otro numero indica el codigo de la intervención a editar
        if (codigoIntervencion!=0) {
            cargarIntervencion(codigoIntervencion);
        }
    }

    //Escucha el boton Aceptar, Cancelar,Fecha.
    @Override
    public void onClick(View view) {
        if(view.getId()== findViewById(R.id.button_AceptarFormulario).getId()) {
            if (codigoIntervencion==0){
                guardarIntervencionNueva();
                //abrirDialogo();
            }else{
                guardarIntervencionEditada(codigoIntervencion);
            }
        }if(view.getId()==findViewById(R.id.button_Cancelar).getId()){
            finish();
        }if(view.getId()==findViewById(R.id.textView_FechaFormulario).getId()){
            showDatePickerDialog(view);
        }
    }

    /**Recupera la información de la intervencion pulsada en el ListView a partir de su codigo y
     * graba la informacion en el formulario
     *
     * @param codigoIntervencion Codigo de la intervencion
     */
    private void cargarIntervencion(int codigoIntervencion) {
        //Cursor donde se almacena la informacion de la consulta
        Cursor cur=null;
        //Creamos un objeto de la clase auxiliar.
        MiBaseDatosHelper bdhelp =
                new MiBaseDatosHelper(this, nombreBD, null, 1);
        //Abrimos en modo lectura y escritura la base de datos 'MiBD'
        SQLiteDatabase bd = bdhelp.getWritableDatabase();
        //Si la base de datos est� abierta.
        if(bd.isOpen()) {
            //Creo un array con los nombres de las columnas a consultar
            String[] misCampos = new String[]{"CODIGO", "NOMBRE", "FECHA", "TIPO", "IMPORTE",
                    "KILOMETROS", "DESCRIPCION", "RUTAIMAGEN", "DRAWABLEIMAGEID"};
            //Consulta que almacena en un cursos la informacion recibida.
            cur = bd.query("INTERVENCIONES", misCampos, "CODIGO="+codigoIntervencion, null, null, null,null );
            //Recoge la informacion de la intervencion y la carga en los elementos del formulario
            if (cur.moveToFirst()) {
                String nombre,fecha,tipoString,descripcion,rutaImagen;
                long importe;
                int codigo,kilometros,drawableImagenID;
                codigo=cur.getInt(0);
                nombre=cur.getString(1);
                fecha=cur.getString(2);
                tipoString=cur.getString(3);
                importe=cur.getLong(4);
                kilometros=cur.getInt(5);
                descripcion=cur.getString(6);
                rutaImagen=cur.getString(7);
                drawableImagenID=cur.getInt(8);
                //Cargo los valores
                fechaClickableTextView.setText(String.valueOf(fecha));
                nombreEditText.setText(String.valueOf(nombre));
                importeEditText.setText(String.valueOf(importe));
                kilometrosEditText.setText(String.valueOf(kilometros));
                descripcionEditText.setText(String.valueOf(descripcion));
            }
        }
    }

    /*
                                                                                                    Tengo que comprobar si necesito
                                                                                                    los metodos doPositiveClick y
                                                                                                    doNegtiveClick
                                                                                                     */
    public void doPositiveClick(){
        Toast.makeText(this, "Ha pulsado OK", Toast.LENGTH_SHORT).show();
    }
    public void doNegativeClick(){
        Toast.makeText(this, "Ha pulsado Cancelar", Toast.LENGTH_SHORT).show();
    }

    /**
     * Consulta todos los registros de la base de datos para comprobar si existen
     * Parametros de bd.query:
     * Nombre de la tabla.
     * Array con los nombres de campos.
     * Condición: la clausula WHERE de la sentencia.
     * Argumentos de la condicion: que podemos dejar a null si no los necesitamos o bien pasarlos como parometros en un array.
     * Clausula GROUP BY que podemos dejar a null si no los necesitamos.
     * Clausula HAVING que podemos dejar a null si no los necesitamos.
     * Clausula ORDER BY que podemos dejar a null si no los necesitamos.
     *
     * @return true si existen registros, false si no existen
     */
    protected int consultarCodigoBaseDatos(){
        int codigoNuevaIntervencion=0;//Almacena el codigo que se usara al grabar una intervencion nueva
        //Creamos un objeto de la clase auxiliar.
        MiBaseDatosHelper bdhelp =
                new MiBaseDatosHelper(this, nombreBD, null, 1);
        //Abrimos en modo lectura y escritura la base de datos 'MiBD'
        SQLiteDatabase bd = bdhelp.getWritableDatabase();
        //Si la base de datos esta abierta.
        if(bd.isOpen()){
            //Creo un array con los nombres de las columnas a consultar
            String[] misCampos = new String[] {"CODIGO"};
            //Consulta que almacena la informacion recibida
            Cursor cur = bd.query("INTERVENCIONES", misCampos, null, null, null, null, null);
            //Recorre el cursor para comprobrar cual es el siguiente codigo disponible
            if (cur.moveToFirst()) {
                int codigoConsultar;
                do {
                    codigoConsultar=cur.getInt(0);
                    if (codigoConsultar>=codigoNuevaIntervencion){codigoNuevaIntervencion=codigoConsultar+1;}//El siguiente codigo al ultimo usado estara disponible
                } while(cur.moveToNext());
                bd.close();
            }else{
                return codigoNuevaIntervencion=1;//No existen registros, el primer codigo sera 1
            }
        }
        return codigoNuevaIntervencion;
    }

    /**
     * Metodo que comprueba que se han introducido todos los campos requeridos en el formulario
     * y los graba en la base de datos
     */
    public void guardarIntervencionNueva(){
        String nombre,descripcion,fecha,tipo;
        double importe;
        int kilometros,codigo;
        //Se recuperan los datos del formulario
        codigo=consultarCodigoBaseDatos();
        fecha=fechaClickableTextView.getText().toString();
        nombre=nombreEditText.getText().toString();
        importe=Double.parseDouble(importeEditText.getText().toString());
        kilometros=Integer.parseInt(kilometrosEditText.getText().toString());
        descripcion=descripcionEditText.getText().toString();
        if(radioButtonMantenimiento.isChecked()){
            tipo="M";
        }else{
            tipo="A";
        }
        //Creamos un objeto de la clase auxiliar MiBaseDatosHelper
        MiBaseDatosHelper bdhelp =
                new MiBaseDatosHelper(this, nombreBD, null, 1);
        //Abrimos en modo lectura y escritura la base de datos 'MiBD'
        SQLiteDatabase bd = bdhelp.getWritableDatabase();
        //Si la base de datos esta abierta.
        if(bd.isOpen()){
            //Creamos el objeto ContentValues con los valores a actualizar del registro.
            ContentValues miRegistro = new ContentValues();
            miRegistro.put("FECHA", fecha);
            miRegistro.put("NOMBRE", nombre);
            miRegistro.put("TIPO",tipo);
            miRegistro.put("IMPORTE", importe);
            miRegistro.put("KILOMETROS",kilometros );
            miRegistro.put("DESCRIPCION", descripcion);
            //Creamos el registro
            almacenarEnBaseDatos(codigo,nombre,fecha,tipo,importe,kilometros,descripcion,null,R.drawable.icono_mantenimiento);
        }
    }

    /**
     * Almacena la informacion introducida en el formulario de edicion de una intervencion y
     * actualiza el registro que corresponde al codigo de la intervencion
     */
    protected void guardarIntervencionEditada(int codigoIntervencion){
        String nombre,descripcion,fecha,tipo;
        double importe;
        int kilometros,codigo=codigoIntervencion;
        //Se recuperan los datos del formulario
        fecha=fechaClickableTextView.getText().toString();
        nombre=nombreEditText.getText().toString();
        importe=Double.parseDouble(importeEditText.getText().toString());
        kilometros=Integer.parseInt(kilometrosEditText.getText().toString());
        descripcion=descripcionEditText.getText().toString();
        if(radioButtonMantenimiento.isChecked()){
            tipo="M";
        }else{
            tipo="A";
        }
        //Creamos un objeto de la clase auxiliar MiBaseDatosHelper
        MiBaseDatosHelper bdhelp =
                new MiBaseDatosHelper(this, nombreBD, null, 1);
        //Abrimos en modo lectura y escritura la base de datos 'MiBD'
        SQLiteDatabase bd = bdhelp.getWritableDatabase();
        //Si la base de datos esta abierta.
        if(bd.isOpen()){
            //Creamos el objeto ContentValues con los valores a actualizar del registro.
            ContentValues miRegistro = new ContentValues();
            miRegistro.put("FECHA", fecha);
            miRegistro.put("NOMBRE", nombre);
            miRegistro.put("TIPO",tipo);
            miRegistro.put("IMPORTE", importe);
            miRegistro.put("KILOMETROS",kilometros );
            miRegistro.put("DESCRIPCION", descripcion);
            //Actualizamos el registro.
            bd.update("INTERVENCIONES", miRegistro, "CODIGO="+codigo, null);
            bd.close();
        }
    }

    /**
     * Crea si no esta creada la base de datos, la abre y almacena los registros pasados por parametros
     * @param codigo
     * @param nombre
     * @param fecha
     * @param tipo
     * @param importe
     * @param kilometros
     * @param descripcion
     * @param rutaImagen
     * @param drawableImageId ID del icono que aparece en el Listview
     */
    protected void almacenarEnBaseDatos(int codigo,String nombre,String fecha,String tipo,
                                        double importe,int kilometros,String descripcion,String rutaImagen,int drawableImageId){
        this.codigoPasado =codigo;
        this.nombrePasado=nombre;
        this.fechaPasado=fecha;
        this.tipoPasado=tipo;
        this.importePasado=importe;
        this.kilometrosPasado=kilometros;
        this.descripcionPasado=descripcion;
        this.rutaImagenPasado=rutaImagen;
        this.drawableImageIdPasado=drawableImageId;
        //Creamos un objeto de la clase auxiliar MiBaseDatosHelper
        MiBaseDatosHelper bdhelp =
                new MiBaseDatosHelper(this, nombreBD, null, 1);
        Log.d("","BASE DE DATOS CREADA");
        //Abrimos en modo lectura y escritura la base de datos 'MiBD'
        SQLiteDatabase bd = bdhelp.getWritableDatabase();
        Log.d("","BASE DE DATOS INTENTA ABRIR");
        //Si la base de datos esta abierta.
        if(bd.isOpen()){
            Log.d("","BASE DE DATOS ABIERTA");
            //Creamos el objeto ContentValues con los valores del registro.
            ContentValues miRegistro = new ContentValues();
            miRegistro.put("CODIGO", codigoPasado);
            miRegistro.put("NOMBRE", nombrePasado);
            miRegistro.put("FECHA", fechaPasado);
            miRegistro.put("TIPO", tipoPasado);
            miRegistro.put("IMPORTE", importePasado);
            miRegistro.put("KILOMETROS", kilometrosPasado);
            miRegistro.put("DESCRIPCION", descripcionPasado);
            miRegistro.put("RUTAIMAGEN", rutaImagenPasado);
            miRegistro.put("DRAWABLEIMAGEID", drawableImageIdPasado);
            //Insertamos el registro.
            bd.insert("INTERVENCIONES", null, miRegistro);
            bd.close();
            Toast.makeText(this, "Intervención guardada: "+codigoPasado, Toast.LENGTH_SHORT).show();
            Log.d("","BASE DE DATOS CERRADA");
        }
    }

    //Abre el calendario creando un objeto de la clase anidada DatePickerFragment()
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "Elija la fecha de la intervención");
    }
    //Abre un dialogFragment con icono .Definido en la clase Fragment
    public void abrirDialogo(){
        Fragment dialogFragment = Fragment
                .newInstance("¿Has pulsado Aceptar.¿Deacuerdo??");
        dialogFragment.show(this.getFragmentManager(), "dialog");

    }



    //Actualiza TextView con la fecha devuelta por el DatePicker
    public static void setFecha(String fechaPasada) {
        fecha = fechaPasada;
        fechaClickableTextView.setText(fecha);

    }

    /**
     * Clase anidada. Crea un Fragment de tipo DatePicker para que el usuario eliga la fecha de la intervencion
     */

    public static class  DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        /**
         * Setea la fecha seleccionda por el usuario
         * @param view
         * @param year
         * @param month
         * @param day
         */

        public void onDateSet(DatePicker view, int year, int month, int day) {
            int mYear = year;
            int mMonth = month;
            int mDay = day;
            String fecha= new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mDay).append("/").append(mMonth + 1).append("/")
                    .append(mYear).append(" ").toString();
            Toast toast1 =
                    Toast.makeText(this.getContext(),
                            "Fecha: "+fecha, Toast.LENGTH_SHORT);
            toast1.show();
            setFecha(fecha);
        }
    }
}

