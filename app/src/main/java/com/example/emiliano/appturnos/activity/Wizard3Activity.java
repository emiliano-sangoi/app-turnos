package com.example.emiliano.appturnos.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.emiliano.appturnos.R;
import com.example.emiliano.appturnos.backend.HorarioAtencion;
import com.example.emiliano.appturnos.dialog.FechaPickerDialog;
import com.example.emiliano.appturnos.dialog.HoraPickerDialog;
import com.example.emiliano.appturnos.event.OnFinishCallback;

import java.util.Date;

public class Wizard3Activity extends WizardActivity implements DialogInterface.OnDismissListener{

    // Atributos
    private HorarioAtencion[] horarios;
    private HorarioAtencion horarioAtencionSel;
    private Date fechaTurnoSel;

    // UI
    private TextView tvFechaSel;
    private LinearLayout layoutHorario;
    private Button btnSeleccionarHoraTurno;
    private HoraPickerDialog dialogSelHora;
    private FechaPickerDialog dialogSelFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard3);

        this.wAnterior = Wizard2Activity.class;
        this.wSiguiente = Wizard4Activity.class;
        this.requestCodeSig = REQUEST_W4;
        this.pasoActual = 3;

        //inicializar y asociar eventos a los componentes visuales:
        initUI();

    }

    @Override
    public void initUI() {

        super.initUI();

        btnSeleccionarHoraTurno = (Button) findViewById(R.id.btnSeleccionarHoraTurno);
        btnSeleccionarHoraTurno.setVisibility(Button.GONE);

        tvFechaSel = (TextView) findViewById(R.id.tvFechaSel);

        // layout
        this.layoutHorario = (LinearLayout) findViewById(R.id.layoutHorario);

        if(turno.getHorarioAtencion() != null){
            horarioAtencionSel = turno.getHorarioAtencion();
            fechaTurnoSel = horarioAtencionSel.getFechaHoraIniAsDate();
            tvFechaSel.setText( horarioAtencionSel.toString() );
            btnSeleccionarHoraTurno.setVisibility(Button.VISIBLE);
        }

    }

    @Override
    public void anterior(int code) {

        if( horarioAtencionSel != null){

            Date now = new Date();

            if(horarioAtencionSel.getFechaHoraIniAsDate().getTime() > now.getTime() ){

                turno.setHorarioAtencion( horarioAtencionSel );
                super.anterior(code);
                return;

            }else{
                mostrarToast("El horario del turno no puede ser anterior a la fecha y hora actual.");
            }

        }

        super.anterior(code);

    }

    @Override
    public void siguiente() {

        if( horarioAtencionSel != null){

            Date now = new Date();

            if(horarioAtencionSel.getFechaHoraIniAsDate().getTime() > now.getTime() ){

                turno.setHorarioAtencion( horarioAtencionSel );
                super.siguiente();

            }else{
                mostrarToast("El horario del turno no puede ser anterior a la fecha y hora actual.");
            }

        }else{
            mostrarToast("No se selecciono ningun horario.");
        }

    }


    public void getHorariosPorMedicoYDia(){

        this.pbProgreso.setVisibility(View.VISIBLE);

        OnFinishCallback callback = new OnFinishCallback(this) {

            @Override
            public void successAction(Object[] data) {
                //cargar el spinner con las opciones
                horarios = (HorarioAtencion[]) data;

                if(horarios.length > 0){

                    btnSeleccionarHoraTurno.setVisibility(Button.VISIBLE);

                }else{
                    mostrarToast("No existe ningùn horarios disponible en la fecha seleccionada.");
                }

                //Ocultar la barra de progreso:
                pbProgreso.setVisibility(View.GONE);

            }
        };

        //deshabilitar el boton mientras se realiza la peticion al servidor:
        this.apiTurnos.getHorariosPorMedicoYDia(callback, turno.getMedico().getIdUsuario(), fechaTurnoSel);



    }

    /**
     * Funcion que se ejecuta cuando se presiona el boton "Seleccionar Fecha"
     * @param view
     */
    public void onClickCambiarFecha(View view){

        dialogSelFecha = new FechaPickerDialog();
        dialogSelFecha.show(getFragmentManager(), "dialogSelFecha");

    }

    /**
     * Funcion que se ejecuta cuando se presiona el boton "Seleccionar Fecha"
     *
     * @param view
     */
    public void onClickCambiarHora(View view){

        dialogSelHora = new HoraPickerDialog();
        dialogSelHora.show(getFragmentManager(), "DialogSelHora");

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    public void onDismissFechaPicker(){

        if(dialogSelFecha.getFechaSel() != null){
            btnSeleccionarHoraTurno.setVisibility(Button.GONE);
            horarioAtencionSel = null;
            fechaTurnoSel = dialogSelFecha.getFechaSel();
            uiActualizarFechaSel();
            getHorariosPorMedicoYDia();

        }

    }

    public void onDismissHoraPicker(){
        if(dialogSelHora.getHorarioAtencionSeleccionado() != null){
            horarioAtencionSel = dialogSelHora.getHorarioAtencionSeleccionado();
            tvFechaSel.setText( horarioAtencionSel.toString() );
        }
    }

    public void uiActualizarFechaSel(){

        if(fechaTurnoSel != null){
            String strFechaTurno = formatter.format( fechaTurnoSel );
            this.tvFechaSel.setText( strFechaTurno );
        }else{
            tvFechaSel.setText(" - ");
        }

    }

    public HorarioAtencion[] getHorarios() {
        return horarios;
    }

}
