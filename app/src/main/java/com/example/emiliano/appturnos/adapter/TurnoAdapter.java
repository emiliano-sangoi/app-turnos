package com.example.emiliano.appturnos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.emiliano.appturnos.R;
import com.example.emiliano.appturnos.backend.Turno;

import java.util.ArrayList;

/**
 * Created by emi88 on 10/28/17.
 */

public class TurnoAdapter extends ArrayAdapter<Turno> {

    private ArrayList<Turno> turnos;

    public TurnoAdapter(Context context, ArrayList<Turno> turnos) {
        super(context, 0, turnos);
        this.turnos = turnos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Turno turno = getItem(position);

        // si no se reusa la vista, inflar:
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_turno, parent, false);
        }

        // actualizar elementos visuales:
        TextView tvTitulo = (TextView) convertView.findViewById(R.id.tvTitulo);
        if(turno.getAfiliacion() != null){
            tvTitulo.setText( turno.getAfiliacion().getFechaIni() );
        }else{
            tvTitulo.setText( "Sin obra social." );
        }

/*
        TextView tvDescripcion = (TextView) convertView.findViewById(R.id.tvDescripcion);
        tvDescripcion.setText( turno.getDescripcion() );*/

        return convertView;

    }

    @Override
    public int getCount() {
        return this.turnos.size();
    }
}
