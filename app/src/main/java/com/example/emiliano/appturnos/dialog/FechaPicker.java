package com.example.emiliano.appturnos.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.example.emiliano.appturnos.R;
import com.example.emiliano.appturnos.activity.Wizard3Activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by emi88 on 12/17/17.
 */

public class FechaPicker extends DatePickerDialog implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    private SimpleDateFormat formmater;
    private Date fechaSel;
    private Wizard3Activity wizard3Activity;
    private TextView tvFecha;

    public FechaPicker(@NonNull Context context) {
        super(context);


        formmater = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        wizard3Activity = (Wizard3Activity) getContext();
        tvFecha = (TextView) wizard3Activity.findViewById(R.id.tvFechaSel);

        Calendar calendario = Calendar.getInstance();
        int anio = 0;
        int mes = 0;
        int dia = 0;

        Date fecha = null;

        try{

            if(tvFecha.getText().toString().length() > 0) {
                fecha = formmater.parse(tvFecha.getText().toString());
            }else{
                fecha = new Date();
            }

            calendario.setTime(fecha);

        }catch (Exception e){

        }

        anio = calendario.get(Calendar.YEAR);
        mes = calendario.get(Calendar.MONTH);
        dia = calendario.get(Calendar.DAY_OF_MONTH);

        //Vaciar horarios



        // Devolver un nuevo objeto de tipo DatePickerDialog
       // return new DatePickerDialog(getContext(), this, anio, mes, dia);
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }
}
