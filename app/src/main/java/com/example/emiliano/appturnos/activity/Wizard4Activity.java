package com.example.emiliano.appturnos.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.emiliano.appturnos.R;

public class Wizard4Activity extends WizardActivity {

    //UI
    private TextView tvFecha;
    private TextView tvMedico;
    private TextView tvEspecialidad;
    private TextView tvAfiliacion;
    private TextView tvClinica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard4);

        this.wAnterior = Wizard3Activity.class;
        this.wSiguiente = null;
        pasoActual = getTotalPasos();

        initUI();

        cargarUI();
    }

    @Override
    public void initUI() {

        super.initUI();

        tvFecha = (TextView) findViewById(R.id.tvFecha);
        tvMedico = (TextView) findViewById(R.id.tvMedico);
        tvEspecialidad = (TextView) findViewById(R.id.tvEspecialidad);
        tvAfiliacion = (TextView) findViewById(R.id.tvAfiliacion);
        tvClinica = (TextView) findViewById(R.id.tvClinica);

    }

    @Override
    public void onClickBtnFinalizar(View view) {
        mostrarToast("No implementado");
    }

    /**
     * Carga los valores seteados en el formulario en la interfaz grafica.
     */
    private void cargarUI(){

        //fecha del turno
        if(turno.getHorarioAtencion() != null){
            tvFecha.setText( WizardActivity.formatFechaHoraTurno(turno.getHorarioAtencion().getFechaHoraIniAsDate()) );
            //tvClinica.setText( turno.getSanatorioNombre() + " - " + turno.getSanatorioDireccion() );
        }

        //medico del turno
        if(turno.getMedico() != null){
            tvMedico.setText( turno.getMedico().toString() );
        }

        //especialidad
        if(turno.getEspecialidad() != null){
            tvEspecialidad.setText( turno.getEspecialidad().getNomEspecialidad() );
        }

        //obra social
        if(turno.getAfiliacion() != null){
            tvAfiliacion.setText( turno.getAfiliacion().getNombre() );
        }else{
            tvAfiliacion.setText( "Particular ($250) " );
        }




    }

}
