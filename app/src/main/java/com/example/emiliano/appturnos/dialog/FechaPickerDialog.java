package com.example.emiliano.appturnos.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.emiliano.appturnos.R;
import com.example.emiliano.appturnos.activity.Wizard3Activity;
import com.example.emiliano.appturnos.activity.WizardActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by emiliano on 19/05/17.
 */

public class FechaPickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private SimpleDateFormat formmater;
    private Date fechaSel;
    private Wizard3Activity wizard3Activity;
    private TextView tvFecha;

    public FechaPickerDialog() {
        this.formmater = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        fechaSel = null;

        Calendar calendario = Calendar.getInstance();
        calendario.set(year, month, dayOfMonth);
        fechaSel = calendario.getTime();
        tvFecha.setText(formmater.format(fechaSel));

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        wizard3Activity = (Wizard3Activity) getActivity();
        tvFecha = (TextView) wizard3Activity.findViewById(R.id.tvFechaSel);

        Bundle bundle = getArguments();

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
        return new DatePickerDialog(getActivity(), this, anio, mes, dia);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        //super.onDismiss(dialog);
        super.onDismiss(dialog);
        if (wizard3Activity instanceof DialogInterface.OnDismissListener) {
            wizard3Activity.onDismissFechaPicker();
            wizard3Activity.onDismiss(dialog);
        }
    }

    public Date getFechaSel() {
        return fechaSel;
    }
}
