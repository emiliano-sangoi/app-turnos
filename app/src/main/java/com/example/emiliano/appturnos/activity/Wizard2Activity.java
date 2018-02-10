package com.example.emiliano.appturnos.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.emiliano.appturnos.R;
import com.example.emiliano.appturnos.backend.Especialidad;
import com.example.emiliano.appturnos.backend.Medico;
import com.example.emiliano.appturnos.event.OnFinishCallback;

public class Wizard2Activity extends WizardActivity {

    //Atributos
    private Especialidad[] especialidades;
    private Medico[] medicos;
    private Especialidad especialidadSel;
    private Medico medicoSel;

    //UI
    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;
    private Spinner spinnerMedicos;
    private ArrayAdapter spinnerMedicosAdapter;
    private LinearLayout layoutMedicos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard2);

        this.wAnterior = Wizard1Activity.class;
        this.wSiguiente = Wizard3Activity.class;
        this.requestCodeSig = REQUEST_W3;
        this.pasoActual = 2;

        initUI();

        //Obtener las especialidades desde el backend
        getEspecialidades();
    }

    @Override
    public void siguiente() {

        if(medicoSel != null && especialidadSel != null){

            turno.setMedico(medicoSel);
            turno.setEspecialidad(especialidadSel);

            super.siguiente();
        }else{
            mostrarToast("Para avanzar debe selecciona un médico y una especialidad.");
        }

        //super.siguiente();
    }

    @Override
    public void anterior(int code) {

        if(medicoSel != null && especialidadSel != null){
            turno.setMedico(medicoSel);
            turno.setEspecialidad(especialidadSel);
        }

        super.anterior(code);
    }

    @Override
    public void initUI() {

        super.initUI();

        // Spinner de especialidades:
        spinner = (Spinner) findViewById(R.id.spinnerEspecialidades);
        // Spinner de medicos:
        spinnerMedicos = (Spinner) findViewById(R.id.spinnerMedicos);

        //Layout medicos:
        layoutMedicos = (LinearLayout) findViewById(R.id.layoutMedicos);
        layoutMedicos.setVisibility(LinearLayout.GONE);

    }

    /**
     * Carga las especialidades medicas desde el backend
     */
    private void getEspecialidades() {

        showProgressBar();

        OnFinishCallback callback = new OnFinishCallback(this) {

            @Override
            public void successAction(Object[] data) {

                hideProgressBar();

                //cargar el spinner con las opciones
                especialidades = (Especialidad[]) data;

                if(especialidades.length > 0){

                    spinnerAdapter = new ArrayAdapter(getContext(), R.layout.my_spinner_layout, especialidades);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                    spinner.setAdapter(spinnerAdapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            especialidadSel = (Especialidad) parent.getSelectedItem();
                            layoutMedicos.setVisibility(LinearLayout.GONE);
                            getMedicosConEspecialidad();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    /*
                    * Si hay guardada una especialidad, mostrarla como seleccionada en el spinner
                    * */
                    if(turno.getEspecialidad() != null){
                        Integer pos = getPosEspecialidadEnSpinner( turno.getEspecialidad().getIdEspecialidad() );
                        if(pos != null){
                            spinner.setSelection( pos );
                        }else{
                            turno.setEspecialidad( null );
                        }
                    }
                }

            }

            @Override
            public void errorAction(String msg) {
                showToast(msg);
                hideProgressBar();
            }
        };

        this.apiTurnos.getEspecialidades(callback);

    }

    /**
     * Carga las especialidades medicas desde el backend
     */
    private void getMedicosConEspecialidad(){

        showProgressBar();

        OnFinishCallback callback = new OnFinishCallback(this){

            @Override
            public void successAction(Object[] data) {

                hideProgressBar();

                //cargar el spinner con las opciones
                medicos = (Medico[]) data;

                if(medicos.length > 0){

                    layoutMedicos.setVisibility(LinearLayout.VISIBLE);

                    spinnerMedicosAdapter = new ArrayAdapter(getContext(), R.layout.my_spinner_layout, medicos);
                    spinnerMedicosAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                    spinnerMedicos.setAdapter( spinnerMedicosAdapter );

                    spinnerMedicos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            medicoSel = (Medico) parent.getSelectedItem();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    if(turno.getMedico() != null){
                        Integer pos = getPosMedicoEnSpinner( turno.getMedico().getIdUsuario() );
                        if(pos != null){
                            spinnerMedicos.setSelection( pos );
                        }else{
                            turno.setMedico( null );
                        }

                    }



                }else{
                    mostrarToast("No se encontro ningún médico disponible para la especialidad: " + especialidadSel.getNomEspecialidad());
                }

            }

            @Override
            public void errorAction(String msg) {
                hideProgressBar();
                showToast(msg);
            }
        };

        this.apiTurnos.getMedicos(callback, especialidadSel.getIdEspecialidad());

    }

    public Integer getPosEspecialidadEnSpinner(int idEsp){
        Integer res = null;
        for(int i=0; i<especialidades.length; i++){
            if( especialidades[i].getIdEspecialidad() == idEsp ){
                res = spinnerAdapter.getPosition( especialidades[i] );
                break;
            }
        }
        return res;
    }

    public Integer getPosMedicoEnSpinner(int idUsuario){
        Integer res = null;
        for(int i=0; i<medicos.length; i++){
            if( medicos[i].getIdUsuario() == idUsuario ){
                res = spinnerMedicosAdapter.getPosition( medicos[i] );
                break;
            }
        }
        return res;
    }

}
