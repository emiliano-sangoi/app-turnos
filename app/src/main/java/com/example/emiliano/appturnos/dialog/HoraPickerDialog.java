package com.example.emiliano.appturnos.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.emiliano.appturnos.R;
import com.example.emiliano.appturnos.activity.Wizard3Activity;
import com.example.emiliano.appturnos.backend.HorarioAtencion;

import java.util.ArrayList;

/**
 * Created by emi88 on 12/15/17.
 */

public class HoraPickerDialog extends DialogFragment {

    private HorarioAtencion  horarioAtencionSeleccionado;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Wizard3Activity wizard3Activity = (Wizard3Activity) getActivity();

        final HorarioAtencion[] horariosAtencion = wizard3Activity.getHorarios();
        String[] horarios = new String[ horariosAtencion.length ];
        ArrayList<String>items = new ArrayList<>();
        for(int i=0;i<horariosAtencion.length;i++){
            items.add(horariosAtencion[i].toString());
        }

        builder
                .setTitle("Seleccione un horario")
                .setSingleChoiceItems(items.toArray(new String[items.size()]), -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        horarioAtencionSeleccionado = horariosAtencion[which];
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(wizard3Activity.getApplicationContext(), "Selecciono " + horarioAtencionSeleccionado.getIdHorarioAtencion(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                 })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Wizard3Activity wizard3Activity = (Wizard3Activity) getActivity();
        if (wizard3Activity instanceof DialogInterface.OnDismissListener) {
            wizard3Activity.onDismissHoraPicker();
            wizard3Activity.onDismiss(dialog);
        }
    }

    public HorarioAtencion getHorarioAtencionSeleccionado() {
        return horarioAtencionSeleccionado;
    }
}