package com.example.pc_jorge.app_mant.Auxiliar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc_jorge.app_mant.Intervencion;
import com.example.pc_jorge.app_mant.R;

/**
 * Created by pc-Jorge on 23/08/2016.
 * Adaptador para inflar con las intervenciones  los listview
 */
public class AdaptadorMostrarIntervenciones extends ArrayAdapter<Intervencion> {

    public AdaptadorMostrarIntervenciones(Context context, Intervencion[] lista) {
        super(context, R.layout.plantilla_listview_intervenciones, lista);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView=convertView;
        //Se infla con el layout de plantilla
        listItemView=inflater.inflate(R.layout.plantilla_listview_intervenciones,parent,false);
        //Obteniendo la instancia de la intervención
        Intervencion intervencion=getItem(position);
        //Se instancia los texview y la imageview y se cargan los valores.
        TextView nombre = (TextView)listItemView.findViewById(R.id.textView_NombreIntervencion_ListView);
        TextView fecha = (TextView)listItemView.findViewById(R.id.textView_Fecha_ListView);
        TextView kilometros = (TextView)listItemView.findViewById(R.id.textView_Kmtrs_listView);
        TextView descripcion = (TextView)listItemView.findViewById(R.id.textView_DescripcionListView);
        ImageView icono = (ImageView) listItemView.findViewById(R.id.imageViewIcono);

        nombre.setText(intervencion.getNombre());
        fecha.setText(intervencion.getFecha());
        kilometros.setText(intervencion.getKilometros());
        descripcion.setText(intervencion.getDescripcion());
        icono.setImageResource(intervencion.getDrawableImagenID());

        return listItemView;
    }
}

