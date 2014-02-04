package com.example.rssrodalies;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jesus on 29/01/14.
 */

public class LineasAdapter extends ArrayAdapter<Linea>{
    private List<Linea> lineas;
    private Context ctx;

    ViewHolder holder = null;

    public LineasAdapter(Context context, int resource, List<Linea> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.lineas = objects;
    }

    static class ViewHolder {
        TextView nombre;
        TextView estado;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        //ViewHolder holder = null;

        if (view == null)
        {
            LayoutInflater inflater = ((Activity) this.ctx).getLayoutInflater();
            view = inflater.inflate(R.layout.lineas_adapter, null);

            holder = new ViewHolder();
            holder.nombre = (TextView) view.findViewById(R.id.nombre);
            holder.estado = (TextView) view.findViewById(R.id.estado);

            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        /* Se obtiene un objeto "Linea" del adapter (la lista), si no es null
        * se asignan sus propiedades a las cajas de texto */
        Linea linea = getItem(position);
        if (linea != null)
        {
            if (holder.nombre != null && holder.estado != null)
            {
                holder.nombre.setText(linea.getNombre()); //Asigna el nombre de la linea

                /* Por defecto el estado de una linea es un string vacio.
                * Si no existe incidencia para una linea, se añade el texto de "normalidad en la linea
                * y se colorea de color verde.
                * Si se existe una incidencia para una linea, se añade a la caja de texto y se
                * colorea de color rojo */
                if(linea.getEstado() == ""){
                    holder.estado.setText(ctx.getResources().getString(R.string.normalitat));
                    holder.estado.setTextColor(Color.parseColor("#538900"));
                }
                else
                {
                    holder.estado.setText(linea.getEstado());
                    holder.estado.setTextColor(Color.parseColor("#BA0000"));
                }
            }
        }
        return view;
    }

}
