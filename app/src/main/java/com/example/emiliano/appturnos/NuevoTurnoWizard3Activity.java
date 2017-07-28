package com.example.emiliano.appturnos;

import android.app.Activity;
import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NuevoTurnoWizard3Activity extends AppCompatActivity {

    /**
     * Interfaz para realizar consultas al backend
     */
    private APITurnosManager apiTurnos;

    /**
     * Usuario logueado
     */
    private Usuario usuario;

    /**
     * Turno que se ira creando en las disitintas etapas
     */
    private Turno turno;

    /**
     * Medicos disponibles para la especialidad elegida por el usuario.
     */
    private Medico[] medicos;

    /**
     * Permite crear fechas a partir de strings
     */
    private SimpleDateFormat formatter;

    /**
     * Ultima fecha seleccionada
     */
    private Date ultimaFechaSel;

    /**
     * Date picker
     */
    private FechaPickerFragment datePicker;

    //COMPONENTES VISUALES:
    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;
    private Button btnSig;
    private Button btnPrev;
    private TextView titulo;
    private ProgressBar progressBar;
    private EditText fechaTurno;

    //CONSTANTES:


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_turno_wizard3);

        RequestQueue rq = Volley.newRequestQueue(this);
        this.apiTurnos = new APITurnosManager(rq);

        //Recuperar el usuario:
        Intent i = getIntent();
        usuario = (Usuario) i.getExtras().getSerializable("usuario");
        turno = (Turno) i.getExtras().getSerializable("turno");

        //formatter:
        this.formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        //inicializar y asociar eventos a los componentes visuales:
        this.initUI();

        this.getMedicosConEspecialidad();
        //Especialidad tmp = this.turno.getEspecialidad();
        //Toast.makeText(this, "" + (tmp != null ? tmp : "es null") , Toast.LENGTH_LONG).show();

    }

    /**
     * Carga las especialidades medicas desde el backend
     */
    private void getMedicosConEspecialidad(){

        OnFinishCallback callback = new OnFinishCallback(this){

            @Override
            public void successAction(Object[] data) {
                //cargar el spinner con las opciones
                medicos = (Medico[]) data;

                cargarSpinner();
                setMedicoSel();
                setFechaSel();

                //Ocultar la barra de progreso:
                progressBar.setVisibility(View.GONE);


            }
        };

        this.apiTurnos.getMedicos(callback, 8);
        //this.apiTurnos.getMedicos(callback, this.turno.getEspecialidad().getIdEspecialidad());

    }

    /**
     * Llena el spinner con los valores obtenidos
     */
    private void cargarSpinner(){

        this.spinnerAdapter = new ArrayAdapter(this, R.layout.my_spinner_layout, this.medicos);
        this.spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        this.spinner.setAdapter(this.spinnerAdapter);

    }

    private void setMedicoSel(){
        //setear el item seleccionado:
        if(turno.getMedico() != null){

            for(int i=0; i<medicos.length; i++){
                if(medicos[i].getIdUsuario() == turno.getMedico().getIdUsuario()){
                    this.spinner.setSelection(this.spinnerAdapter.getPosition(medicos[i]));
                    break;
                }
            }

        }else{
            if(medicos.length > 0){
                this.spinner.setSelection(this.spinnerAdapter.getPosition(medicos[0]));
            }

        }
    }

    /**
     * Funcion que setea la fecha definida en el turno o la fecha actual si no se ha definido ninguna.
     */
    private void setFechaSel(){

        if(this.turno.getDia() != null){
            String strFecha = this.formatter.format(this.turno.getDia());
            this.fechaTurno.setText(strFecha);
        }else{
            //Setear fecha actual:
            String strFecha = this.formatter.format(Calendar.getInstance().getTime());
            this.fechaTurno.setText(strFecha);
        }
    }

    /**
     * Metodo ejecutado al presionar el boton cancelar
     *
     * @param view
     */
    public void onW3BtnCancelar(View view){
        Intent i = new Intent(this, NuevoTurnoWizard2Activity.class);
        setResult(Activity.RESULT_CANCELED, i);
        finish();
    }


    /**
     * Evento al presionar el boton anterior
     *
     * @param view
     */
    public void onW3BtnAntClick(View view){

        //Guardar medico y fecha del turno
        this.guardarMedicoEnTurno();

        if(this.guardarFechaEnTurno()) {

            Intent i = new Intent(this, NuevoTurnoWizard2Activity.class);
            Bundle data = new Bundle();
            data.putSerializable("turno", this.turno);
            i.putExtras(data);
            setResult(Activity.RESULT_OK, i);
            finish();
            //Toast.makeText(this, formatter.format(turno.getDia()), Toast.LENGTH_LONG).show();

        }

    }

    /**
     * Evento al presionar el boton siguiente
     * @param view
     */
    public void onW3BtnSigClick(View view){

        //Guardar medico y fecha del turno
        this.guardarMedicoEnTurno();
        if(this.guardarFechaEnTurno()){
            Toast.makeText(this, "No se ha implementado todavia", Toast.LENGTH_LONG).show();
        }



    }


    public void w3CambiarFechaTurnoClick(View view){

        Bundle arg = new Bundle();
        arg.putInt("fecha_id", R.id.w3TxtFechaTurno);
        datePicker.setArguments(arg);
        datePicker.show(getFragmentManager(), "datePicker");

    }


    /**
     * Funcion auxiliar que guarda la fecha en el turno
     *
     * @return
     */
    private boolean guardarFechaEnTurno(){

        //turno.setEspecialidad(spinnerAdapter.getItem(position));
        String fechaStr = fechaTurno.getText().toString();

        try {
            Date diaTurno = formatter.parse(fechaStr);
            Toast.makeText(this, fechaStr, Toast.LENGTH_LONG).show();
            turno.setDia(diaTurno);
            return true;
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Error en el formato de la fecha ingresado. El formato requerido es: dd/mm/yyyy", Toast.LENGTH_LONG).show();
        }

        return false;

    }

    /**
     * Funcion auxiliar que guarda el medico seleccionado en el turno
     */
    private void guardarMedicoEnTurno(){
        Medico m = (Medico) spinner.getSelectedItem();
        this.turno.setMedico(m);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case Activity.RESULT_OK:
                //Actualizar turno:
                this.turno = (Turno) data.getExtras().getSerializable("turno");
                //Toast.makeText(this, turno.getObraSocialId() + "", Toast.LENGTH_LONG).show();
                break;

            case Activity.RESULT_CANCELED:
                //volver a la principal sin actualizar nada
                Intent i = new Intent(this, NuevoTurnoWizard2Activity.class);
                setResult(Activity.RESULT_CANCELED, i);
                finish();
        }


    }

    /**********************************************************************************/
    // Menu principal

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_menu_item_salir:
                usuario.setLogueado(false);
                startActivity( new Intent(this, LoginActivity.class) );
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**********************************************************************************/
    // Iniciar componentes UI
    private void initUI(){

        this.spinner = (Spinner) findViewById(R.id.spinnerMedicos);

        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //accion a realizar al seleccionar un medico...

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Botones y Textview:
        this.titulo = (TextView) findViewById(R.id.w3TxtTitulo);
        this.titulo.setText("Nuevo Turno (3/4)");
        this.progressBar = (ProgressBar) findViewById(R.id.w3IndeterminateBar);
        this.progressBar.setVisibility(View.VISIBLE);
        this.fechaTurno = (EditText) findViewById(R.id.w3TxtFechaTurno);

        //DatePicker
        this.datePicker = new FechaPickerFragment();


    }



}
