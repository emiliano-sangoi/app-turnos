package com.example.emiliano.appturnos.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.emiliano.appturnos.R;
import com.example.emiliano.appturnos.activity.HomeActivity;
import com.example.emiliano.appturnos.backend.Turno;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by emi88 on 10/28/17.
 */

public class TurnoAdapter extends ArrayAdapter<Turno> {

    private ArrayList<Turno> turnos;
    private Context context;

    public TurnoAdapter(Context context, ArrayList<Turno> turnos) {
        super(context, 0, turnos);
        this.context = context;
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
        TextView tvFecha = (TextView) convertView.findViewById(R.id.tvFecha);
        TextView tvHora = (TextView) convertView.findViewById(R.id.tvHora);
        TextView tvTiempoRestante = (TextView) convertView.findViewById(R.id.tvTiempoRestante);

        //Fecha y hora del turno:
        Date horaIni = turno.getHorarioAtencion().getFechaHoraIniAsDate();

        //Fecha:
        Locale locale = new Locale("es", "AR");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, d MMM yyyy", locale);
        tvFecha.setText(simpleDateFormat.format(horaIni));

        //Hora:
        simpleDateFormat = new SimpleDateFormat("HH:mm", locale);
        tvHora.setText(simpleDateFormat.format(horaIni) + " Hs.");

        if(turno.isCancelado()){
            tvTiempoRestante.setText("Cancelado");
            tvTiempoRestante.setTextColor( context.getResources().getColor(R.color.colorInfo) );
            return convertView;
        }

        //Tiempo restante
        Long dif = turno.getHorarioAtencion().getTiempoRestanteEnMilis();

        if(dif <= 0){
            tvTiempoRestante.setText("Finalizado");
            tvTiempoRestante.setTextColor( context.getResources().getColor(R.color.colorInfo) );
            return convertView;
        }

        long difMeses = (long) (TimeUnit.MILLISECONDS.toDays( dif ) / 30);
        long difDias = TimeUnit.MILLISECONDS.toDays( dif );
        long difHoras = TimeUnit.MILLISECONDS.toHours( dif );
        long difMinutos = TimeUnit.MILLISECONDS.toMinutes( dif );
        String msg = "";
        int color = 0;

        if(difMeses >= 1 ){
            msg = "En " + Long.toString(difMeses) + " meses.";
            color = context.getResources().getColor(R.color.colorSuccess);
        }else if(difDias >= 1 ){
            msg = "En " + Long.toString(difDias) + " dias.";
            color = context.getResources().getColor(R.color.colorSuccess);
        }else if(difHoras >= 1 ){
            msg = "En " + Long.toString(difHoras) + " horas.";
            color = context.getResources().getColor(R.color.colorWarning);
        }else{
            msg = "En " + Long.toString(difMinutos) + " minutos.";
            color = context.getResources().getColor(R.color.colorDanger);
        }

        tvTiempoRestante.setText( msg );
        tvTiempoRestante.setTextColor( color );
        return convertView;

    }



    @Override
    public int getCount() {
        return this.turnos.size();
    }

    private int decodificar(int id){
        return Integer.parseInt(getContext().getResources().getString( id ));
    }
}
