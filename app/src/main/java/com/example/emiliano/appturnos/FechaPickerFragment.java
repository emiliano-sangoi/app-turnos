package com.example.emiliano.appturnos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by emiliano on 19/05/17.
 */

public class FechaPickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    /**
     * Id del componente en el que se seteo la fecha
     */
    private int id;
    private SimpleDateFormat formmater;

    public FechaPickerFragment() {
        this.formmater = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Date selectedDate = null;

        TextView txvFecha = (TextView) getActivity().findViewById(id);
        Calendar calendario = Calendar.getInstance();
        calendario.set(year, month, dayOfMonth);
        selectedDate = calendario.getTime();
        txvFecha.setText(formmater.format(selectedDate));

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        Calendar calendario = Calendar.getInstance();
        int anio = 0;
        int mes = 0;
        int dia = 0;

        Date fecha = null;

        try{
            this.id = bundle.getInt("fecha_id");

            TextView tvFecha = (TextView) getActivity().findViewById(this.id);

            fecha = formmater.parse(tvFecha.getText().toString());

            calendario.setTime(fecha);

        }catch (Exception e){

        }

        anio = calendario.get(Calendar.YEAR);
        mes = calendario.get(Calendar.MONTH);
        dia = calendario.get(Calendar.DAY_OF_MONTH);

        // Devolver un nuevo objeto de tipo DatePickerDialog
        return new DatePickerDialog(getActivity(), this, anio, mes, dia);

    }
}
