package com.example.emiliano.appturnos.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.emiliano.appturnos.R;
import com.example.emiliano.appturnos.backend.HorarioAtencion;
import com.example.emiliano.appturnos.dialog.FechaPicker;
import com.example.emiliano.appturnos.dialog.FechaPickerDialog;
import com.example.emiliano.appturnos.dialog.HoraPickerDialog;
import com.example.emiliano.appturnos.event.OnFinishCallback;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.MonthAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Wizard3Activity extends WizardActivity implements DialogInterface.OnDismissListener, DatePickerDialog.OnDateSetListener{

    // Atributos
    private HorarioAtencion[] horarios;
    private HorarioAtencion[] horariosParaDiaSel;
    private HorarioAtencion horarioAtencionSel;
    private Date fechaTurnoSel;
    private Date fechaTurnoSelTemp;

    // UI
    private TextView tvFechaSel;
    private LinearLayout layoutHorario;
    private Button btnSeleccionarHoraTurno;
    private Button btnSeleccionarFechaTurno;
    private HoraPickerDialog dialogSelHora;
    //private FechaPickerDialog dialogSelFecha;

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

        //Obtener los horarios disponibles para el medico elegido
        getHorariosPorMedico();

    }

    @Override
    public void initUI() {

        super.initUI();

        btnSeleccionarFechaTurno = (Button) findViewById(R.id.btnSeleccionarFechaTurno);
        btnSeleccionarFechaTurno.setVisibility(Button.GONE);
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
            mostrarToast("No se selecciono ningún horario.");
        }

    }


    public void getHorariosPorMedico(){

        this.pbProgreso.setVisibility(View.VISIBLE);

        OnFinishCallback callback = new OnFinishCallback(this) {

            @Override
            public void successAction(Object[] data) {
                //cargar el spinner con las opciones
                horarios = (HorarioAtencion[]) data;

                if(horarios.length > 0){

                    btnSeleccionarFechaTurno.setVisibility(Button.VISIBLE);

                }else{
                    layoutHorario.setVisibility(LinearLayout.GONE);
                    mostrarToast("El Dr. seleccionado no posee ningún turno disponible en la fecha seleccionada.");
                }

                //Ocultar la barra de progreso:
                pbProgreso.setVisibility(View.GONE);

            }
        };

        //deshabilitar el boton mientras se realiza la peticion al servidor:
        this.apiTurnos.getHorariosPorMedico(callback, turno.getMedico().getIdUsuario());



    }

    /**
     * Funcion que se ejecuta cuando se presiona el boton "Seleccionar Fecha"
     * @param view
     */
    public void onClickCambiarFecha(View view){

        materialDatePicker();

        /*
        //Alternativa con cuadro de dialogo personalizado:
        dialogSelFecha = new FechaPickerDialog();
        dialogSelFecha.show(getFragmentManager(), "dialogSelFecha");
         */


    }

    /**
     * https://github.com/wdullaer/MaterialDateTimePicker
     */
    public void materialDatePicker(){

        fechaTurnoSelTemp = null;

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                if(fechaTurnoSelTemp != null){
                    fechaTurnoSel = fechaTurnoSelTemp;
                    horarioAtencionSel = null;
                    uiActualizarFechaSel();
                }

                fechaTurnoSelTemp = null;
            }
        });
        /*dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                onDateSet(view, year, monthOfYear, dayOfMonth);
                /*Calendar calendario = Calendar.getInstance();
                calendario.set(year, monthOfYear-1, dayOfMonth);
                fechaTurnoSelTemp = calendario.getTime();
            }
        });*/

        dpd.setSelectableDays( getHorariosAsCalendarArray() );
        dpd.setAccentColor( getResources().getColor(R.color.colorPrimary) );
        dpd.showYearPickerFirst(true);
        dpd.dismissOnPause(true);
        dpd.show(getFragmentManager(), "Datepickerdialog");



    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        /*Calendar fecha = Calendar.getInstance();
        fecha.set(year, monthOfYear - 1, dayOfMonth);
        fechaTurnoSel = fecha.getTime();
        btnSeleccionarHoraTurno.setVisibility(Button.VISIBLE);*/

        Calendar calendario = Calendar.getInstance();
        calendario.set(year, monthOfYear, dayOfMonth);
        fechaTurnoSelTemp = calendario.getTime();

    }

    /**
     * Funcion que se ejecuta cuando se presiona el boton "Seleccionar Fecha"
     *
     * @param view
     */
    public void onClickCambiarHora(View view){

        filtrarHorariosParaDiaSel();
        if(horariosParaDiaSel != null && horariosParaDiaSel.length>0){
            dialogSelHora = new HoraPickerDialog();
            dialogSelHora.show(getFragmentManager(), "DialogSelHora");
        }else{
            mostrarToast("No se encontro ningun horario en dia de la fecha.");
        }


    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    /*public void onDismissFechaPicker(){

        if(fechaTurnoSel != null){
            btnSeleccionarHoraTurno.setVisibility(Button.GONE);
            horarioAtencionSel = null;
            uiActualizarFechaSel();
            btnSeleccionarHoraTurno.setVisibility(Button.VISIBLE);

        }

    }*/

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
            btnSeleccionarHoraTurno.setVisibility(Button.VISIBLE);
        }else{
            tvFechaSel.setText(" - ");
        }

    }



    /**
     * Funcion auxiliar que filtra solo los horarios cuyo dia de inicio es el mismo que el dia seleccionado.
     */
    private void filtrarHorariosParaDiaSel() {
        if(fechaTurnoSel == null || horarios == null) {
            return;
        }

        ArrayList<HorarioAtencion> horariosParaFechaSel = new ArrayList<HorarioAtencion>();
        String fechaSelStr = formatter.format( fechaTurnoSel );
        boolean esElMismoDia;
        for(int i=0 ; i < horarios.length ; i++){
            esElMismoDia = formatter.format(horarios[i].getFechaHoraIniAsDate()).equals(fechaSelStr);
            //Log.i("Horario: ", formatter.format(horarios[i].getFechaHoraIniAsDate()) + " - Fecha Sel: " + fechaSelStr + " - Res: " + (esElMismoDia ? " = " : " !="));
            if( esElMismoDia ){
                horariosParaFechaSel.add(horarios[i]);
            }
        }

        horariosParaDiaSel = horariosParaFechaSel.toArray(new HorarioAtencion[0]);
    }



     /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTERS & SETTERS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

    public HorarioAtencion[] getHorariosParaDiaSel() {
        return horariosParaDiaSel;
    }

    public HorarioAtencion[] getHorarios() {
        return horarios;
    }

    public Calendar[] getHorariosAsCalendarArray(){

        Calendar[] fechas = new Calendar[0];

        if(horarios != null){

            fechas = new Calendar[ horarios.length ];
            for(int i=0 ; i < horarios.length ; i++){

                fechas[i] = horarios[i].getFechaHoraIniAsCalendar();
            }

        }
        return fechas;

    }


    /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< FIN GETTERS & SETTERS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */



}
