package com.example.emiliano.appturnos.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emiliano.appturnos.R;
import com.example.emiliano.appturnos.activity.HomeActivity;
import com.example.emiliano.appturnos.backend.APITurnosManager;
import com.example.emiliano.appturnos.backend.Turno;
import com.example.emiliano.appturnos.event.OnFinishCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by emi88 on 1/31/18.
 */

public class TurnoDialog extends DialogFragment {

    private Button btnDarDeBaja;
    private Button btnCerrar;
    private TextView tvFecha;
    private TextView tvEstado;
    private TextView tvClinica;
    private TextView tvClinicaDir;
    private TextView tvMedico;
    private TextView tvEspecialidad;
    private TextView tvAfiliacion;
    private View view;
    private SimpleDateFormat simpleDateFormat;


    private boolean borrar;
    public final static int OP_EDITAR = 1;
    public final static int OP_BORRAR = 2;
    private int op;
    private Turno turno;

    public TurnoDialog() {
        this.borrar = false;
        this.op = 0;

        Locale locale = new Locale("es", "AR");
        simpleDateFormat = new SimpleDateFormat("EEEE, d MMM yyyy HH:mm", locale);

    }



    public void initUI(){

         this.btnCerrar = (Button) view.findViewById(R.id.btnDialogCerrar);
         this.btnDarDeBaja = (Button) view.findViewById(R.id.btnDarDeBaja);

         //Fecha y hora:
         this.tvFecha = (TextView) view.findViewById(R.id.tvFecha);

         //Estado / tiempo restante:
         this.tvEstado = (TextView) view.findViewById(R.id.tvEstado);

         //Clinica:
         this.tvClinica = (TextView) view.findViewById(R.id.tvClinica);
         this.tvClinicaDir = (TextView) view.findViewById(R.id.tvClinicaDir);

         //Medico
         this.tvMedico = (TextView) view.findViewById(R.id.tvMedico);

         //Especialidad:
         this.tvEspecialidad = (TextView) view.findViewById(R.id.tvEspecialidad);


        //Afiliacion:
        this.tvAfiliacion = (TextView) view.findViewById(R.id.tvAfiliacion);

    }

    public void fillUI(){

        //Fecha y hora:
        Date horaIni = this.turno.getHorarioAtencion().getFechaHoraIniAsDate();
        this.tvFecha.setText(simpleDateFormat.format(horaIni) + " Hs.");

        //Clinica:
        this.tvClinica.setText(turno.getHorarioAtencion().getSanatorioNombre());
        this.tvClinicaDir.setText(turno.getHorarioAtencion().getSanatorioDireccion());

        //Medico
        this.tvMedico.setText(turno.getMedico().getApeNom());

        //Especialidad:
        this.tvEspecialidad.setText(turno.getEspecialidad().getNomEspecialidad());

        //Afiliacion
        String af = "Ninguna (Abona consulta)";
        if(turno.getAfiliacion() != null){
            af = turno.getAfiliacion().getNombre();
        }
        this.tvAfiliacion.setText(af);

    }

    public void calcularTiempoRestante(){

        Context context = getActivity();

        if(turno.isCancelado()){
            tvEstado.setText("Cancelado");
            tvEstado.setTextColor( context.getResources().getColor(R.color.colorInfo) );
            return;
        }

        //Tiempo restante
        Long dif = turno.getHorarioAtencion().getTiempoRestanteEnMilis();

        if(dif <= 0){
            tvEstado.setText( "Finalizado" );
            tvEstado.setTextColor( context.getResources().getColor(R.color.colorInfo) );

            return;
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

        tvEstado.setText( msg );
        tvEstado.setTextColor( color );
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        this.view = inflater.inflate(R.layout.turno_dialog, null);
        builder.setView( view );

        final Bundle data = getArguments();
        turno = (Turno) data.getSerializable("turno");

        //una vez creado el layout, buscar los componentes visuales y setearles el contenido:
        this.initUI();
        this.fillUI();
        this.calcularTiempoRestante();

        //action para Editar:
        this.btnDarDeBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(turno.isCancelado() || turno.isFinalizado()){
                Toast.makeText(getActivity().getApplicationContext(), "El turno no tiene vigencia.", Toast.LENGTH_SHORT).show();
            }else{

                //Anular turno:
                APITurnosManager apiTurnosManager = (APITurnosManager) data.getSerializable("apiTurnos");
                final Activity activity = getActivity();

                OnFinishCallback onFinishCallback = new OnFinishCallback(activity.getBaseContext()){

                    @Override
                    public void successAction(Object data) {

                        Turno turnoMod = (Turno) data;
                        turno.setFechaCancelacion( turnoMod.getFechaCancelacion() );
                        Toast.makeText(getActivity().getApplicationContext(), "El turno ha sido dado de baja satisfactoriamente.", Toast.LENGTH_SHORT).show();
                        calcularTiempoRestante();

                        HomeActivity homeActivity = (HomeActivity) activity;
                        int pos_turno = getArguments().getInt("pos_turno");
                        homeActivity.actualizarTurno(pos_turno, turno);

                    }

                    @Override
                    public void errorAction(String msg) {
                        super.errorAction(msg);
                    }
                };

                apiTurnosManager.bajaTurno(onFinishCallback, turno.getId());



            }
            }
        });


        //accion para cerrar:
        this.btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        return builder.create();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {

        //Realizar alguna accion:
        Activity activity = getActivity();
        if(activity instanceof DialogInterface.OnDismissListener){
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }

        super.onDismiss(dialog);

    }



    public int getOp() {
        return op;
    }

}
