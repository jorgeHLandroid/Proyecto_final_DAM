package com.example.pc_jorge.app_mant.Activities;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc_jorge.app_mant.Dialogues.Dialog_Alert_empty_fields;
import com.example.pc_jorge.app_mant.Dialogues.Dialog_Iconos;
import com.example.pc_jorge.app_mant.Utilities.MiBaseDatosHelper;
import com.example.pc_jorge.app_mant.R;

import java.util.Calendar;

/**
 * Formulario con los datos que se almacenaran respecto a la intervencion.
 * Esta Activity se usa tanto para nuevas altas como para editar una intervencion ya creado.
 * Se recibe el argumento 'codigo' desde MainActivity, si es 0 sera un alta nueva, si recibe
 * otro codigo cargara los valores de los campos en el formulario para que puedan ser editados.
 */
public class Formulario extends AppCompatActivity implements View.OnClickListener{
    String nombreBD="miBD";
    static String fecha="Fecha";
    static TextView fechaClickableTextView;
    TextView descripcionTextView;
    RadioButton radioButtonMantenimiento,radioButtonAveria;
    Button  botonAceptar,botonCancelar;
    static ImageButton buttonIcon;
    //variables para insertar en la BD
    private String nombrePasado,fechaPasado,descripcionPasado,rutaImagenPasado,tipoPasado;
    private double importePasado;
    private int kilometrosPasado,drawableImageIdPasado,codigoPasado,codigoIntervencion;
    EditText nombreEditText,importeEditText,kilometrosEditText,descripcionEditText;
    //Indicara el tipo de icono de la intervencion es statico para accedera al metodo set desde otra clase.
    private static int iconSelection;

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

        botonAceptar=(Button)findViewById(R.id.button_AceptarFormulario);
        botonAceptar.setOnClickListener(this);
        botonCancelar=(Button)findViewById(R.id.button_Cancelar);
        botonCancelar.setOnClickListener(this);
        buttonIcon=(ImageButton)findViewById(R.id.imageButton_IconoFormulario);
        setIconSelection(R.drawable.question);//Setea el icono de la interrogacion en el buttonIcon
        buttonIcon.setOnClickListener(this);



        //Recojo el codigo pasado por el listView
        Intent intentRecibir=getIntent();
        codigoIntervencion= (Integer) intentRecibir.getExtras().get("codigoIntervencion");
                                                                                                    Log.d("","Codigo recibido por el Formulario: "+codigoIntervencion);
        //Si el codigo es 0 significa un alta, cualquier otro numero indica el codigo de la intervención a editar
        if (codigoIntervencion!=0) {
            cargarIntervencion(codigoIntervencion);
        }
    }





    /**
     * Escucha el boton Aceptar, Cancelar,Fecha.
     * @param view Instancia del view que se hizo click.
     */
    @Override
    public void onClick(View view) {
        if(view.getId()== findViewById(R.id.button_AceptarFormulario).getId()) {
            if (codigoIntervencion==0){
                guardarIntervencionNueva();
                //abrirDialogoAdvertencia();
                //abrirDialogoIconos();
            }else{
                guardarIntervencionEditada(codigoIntervencion);
            }
        }if(view.getId()==findViewById(R.id.button_Cancelar).getId()){
            finish();
        }if(view.getId()==findViewById(R.id.textView_FechaFormulario).getId()){
            showDatePickerDialog(view);
        }
        if (view.getId() == findViewById(R.id.imageButton_IconoFormulario).getId()) {
            abrirDialogoIconos();
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
                setIconSelection(drawableImagenID);
                //Activa un radiobutton u otro segun sea Averia o Intervencion
                if(tipoString.equals("A")){
                    radioButtonAveria.setChecked(true);
                }else{
                    radioButtonMantenimiento.setChecked(true);
                }
            }
        }
    }

   //Recoge la opcion elegida en el DialogFragment
    public void doPositiveClick(){

    }
    public void doNegativeClick(){

    }

    /**
     * Consulta los codigos de las intervenciones almacenadas en la BD. Selecciona el primer numero disponible
     * de menor a mayor para asignarlo a una nueva intervencion.
     * Si la BD no tiene ningun registro el primer numero que se asiga sera el 1 dejando el 0 para
     * indicar que se trata de un alta nueva.
     *
     * Parametros de bd.query:
     * Nombre de la tabla.
     * Array con los nombres de campos.
     * Condición: la clausula WHERE de la sentencia.
     * Argumentos de la condicion: que podemos dejar a null si no los necesitamos o bien pasarlos como parometros en un array.
     * Clausula GROUP BY que podemos dejar a null si no los necesitamos.
     * Clausula HAVING que podemos dejar a null si no los necesitamos.
     * Clausula ORDER BY que podemos dejar a null si no los necesitamos.
     *
     * @return Codigo disponible para la nueva intervencion
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
        String nombre,importe,kilometros,descripcion,fecha,tipo;
        double importeDouble;
        int kilometrosInt,codigo,iconSelection;
        //Se recuperan los datos del formulario
        codigo=consultarCodigoBaseDatos();
        fecha=fechaClickableTextView.getText().toString();
        nombre=nombreEditText.getText().toString();
        importe=importeEditText.getText().toString();
        kilometros=kilometrosEditText.getText().toString();
        descripcion=descripcionEditText.getText().toString();
        iconSelection=getIconSelection();
        if(radioButtonMantenimiento.isChecked()){
            tipo="M";
        }else{
            tipo="A";
        }
        //Solo grabara la intervencion si se validan todos los campos
        String texto[]={fecha,nombre,kilometros,importe};
        String textoInformativo[]={getString(R.string.Introduce_la_fecha),
                getString(R.string.Introduce_el_nombre),
                getString(R.string.Introduce_coste),
                getString(R.string.Introduce_Klmtrs)};
        if(validar(texto,textoInformativo )) {
            importeDouble=Double.parseDouble(importeEditText.getText().toString());
            kilometrosInt=Integer.parseInt(kilometrosEditText.getText().toString());
            //Creamos un objeto de la clase auxiliar MiBaseDatosHelper
            MiBaseDatosHelper bdhelp =
                    new MiBaseDatosHelper(this, nombreBD, null, 1);
            //Abrimos en modo lectura y escritura la base de datos 'MiBD'
            SQLiteDatabase bd = bdhelp.getWritableDatabase();
            //Si la base de datos esta abierta.
            if (bd.isOpen()) {
                //Creamos el registro
                almacenarEnBaseDatos(codigo, nombre, fecha, tipo, importeDouble, kilometrosInt, descripcion, null, iconSelection);
            }
            Toast.makeText(this, "Guardado con exito", Toast.LENGTH_SHORT).show();
            finish();//Cierro el Formulario
        }
    }



    /**
     * Almacena la informacion introducida en el formulario de edicion de una intervencion y
     * actualiza el registro que corresponde al codigo de la intervencion
     */
    protected void guardarIntervencionEditada(int codigoIntervencion){
        String nombre,importe,kilometros,descripcion,fecha,tipo;
        double importeDouble;
        int kilometrosInt,codigo=codigoIntervencion,iconSelection;
        //Se recuperan los datos del formulario
        fecha=fechaClickableTextView.getText().toString();
        nombre=nombreEditText.getText().toString();
        importe=importeEditText.getText().toString();
        kilometros=kilometrosEditText.getText().toString();
        descripcion=descripcionEditText.getText().toString();
        iconSelection=getIconSelection();
        if(radioButtonMantenimiento.isChecked()){
            tipo="M";
        }else{
            tipo="A";
        }

        //Solo grabara la intervencion si se validan todos los campos
        String texto[]={fecha,nombre,kilometros,importe};
        String textoInformativo[]={getString(R.string.Introduce_la_fecha),
                getString(R.string.Introduce_el_nombre),
                getString(R.string.Introduce_coste),
                getString(R.string.Introduce_Klmtrs)};
        if(validar(texto,textoInformativo )) {
            //Casting para grabarlo en la base de datos
            importeDouble = Double.parseDouble(importeEditText.getText().toString());
            kilometrosInt = Integer.parseInt(kilometrosEditText.getText().toString());
            //Creamos un objeto de la clase auxiliar MiBaseDatosHelper
            MiBaseDatosHelper bdhelp =
                    new MiBaseDatosHelper(this, nombreBD, null, 1);
            //Abrimos en modo lectura y escritura la base de datos 'MiBD'
            SQLiteDatabase bd = bdhelp.getWritableDatabase();
            //Si la base de datos esta abierta.
            if (bd.isOpen()) {
                //Creamos el objeto ContentValues con los valores a actualizar del registro.
                ContentValues miRegistro = new ContentValues();
                miRegistro.put("FECHA", fecha);
                miRegistro.put("NOMBRE", nombre);
                miRegistro.put("TIPO", tipo);
                miRegistro.put("IMPORTE", importeDouble);
                miRegistro.put("KILOMETROS", kilometrosInt);
                miRegistro.put("DESCRIPCION", descripcion);
                miRegistro.put("DRAWABLEIMAGEID",iconSelection);
                //Actualizamos el registro.
                bd.update("INTERVENCIONES", miRegistro, "CODIGO=" + codigo, null);
                bd.close();
                Toast.makeText(Formulario.this, "Editado con exito", Toast.LENGTH_SHORT).show();
                finish();//Cierra el formulario
            }
        }
    }

    /**
     * Recibe 2 Arrays para comprobar que los String del Array1 no estan vacios y no coinciden con
     * los textos informativos almacenados en el Array2
     * @param ArrayString1 String obtenidos de los EditText
     * @param ArrayString2 String con los textos informativos
     * @return true si Array1 no contiene ningun String vacio ni coincide con frase informativa
     */
    public boolean validar(String[] ArrayString1,String[] ArrayString2){
        String[] texto=ArrayString1;
        String[] textoInformativo=ArrayString2;
        for(int i=0; i<texto.length;i++){
            String textoString=texto[i].replaceAll("\\s+", "");
            for(int y=0;y<textoInformativo.length;y++){
                String textoInformativoString=textoInformativo[y].replaceAll("\\s+", "");
                if(textoString.equals("")||textoString.equals(textoInformativoString)){
                                                                                                    Log.d("","Ha encontrado vacio o texto inf:  "+textoString);
                    //Toast.makeText(Formulario.this, getString(R.string.Debe_completar_todos_los_campos), Toast.LENGTH_SHORT).show();
                    abrirDialogoAdvertencia();
                    return false;
                }
            }
        }
        return true;
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
                                                                                                    Log.d("","Intervención guardada: "+codigoPasado);
                                                                                                    Log.d("","BASE DE DATOS CERRADA");
        }
    }

    /**
     * Carga la imagen que recibe como parametro en el ImageButton del icono de la
     * intervencion. Utilizo el metodo .setTag() para almacenar el id y disponer de el en el
     * .getIconSelection()
     * @param icon id del recurso tipo Drawable.
     */
    public static void setIconSelection(int icon){
        buttonIcon.setImageResource(icon);
        buttonIcon.setTag(icon);
    }

    /**
     * Retorna el recurso de tipo Drawable que esta cargado en el IconButton.
     * @return el id del recurso de tipo Drawable.
     */
    public int getIconSelection(){
        Integer icon=(Integer)buttonIcon.getTag();
        return icon;
    }


    //Abre el calendario creando un objeto de la clase anidada DatePickerFragment()
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "Elija la fecha de la intervención");
    }
    //Abre un dialogFragment con icono .Definido en la clase Dialog_Alert_empty_fields
    public void abrirDialogoAdvertencia(){
        Dialog_Alert_empty_fields dialogDialogAlertemptyfields = Dialog_Alert_empty_fields
                .newInstance("Error al completar el Formulario.");
        dialogDialogAlertemptyfields.show(this.getFragmentManager(), "dialog");

    }

    //Abre dialogo de iconos
    public void abrirDialogoIconos(){
        Dialog_Iconos dialog=new Dialog_Iconos();
        dialog.setStyle(DialogFragment.STYLE_NORMAL,R.style.Theme_Dialog_Translucent);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "Elige un icono");

        //Dialog_Iconos dialog=new Dialog_Iconos();

    }



    //Actualiza TextView con la fecha devuelta por el DatePicker
    public static void setFecha(String fechaPasada) {
        fecha = fechaPasada;
        fechaClickableTextView.setText(fecha);
    }

    /**
     * Clase anidada. Crea un Dialog_Alert_empty_fields de tipo DatePicker para que el usuario eliga la fecha de la intervencion.
     *
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

