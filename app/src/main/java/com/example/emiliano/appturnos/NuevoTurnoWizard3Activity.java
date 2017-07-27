package com.example.emiliano.appturnos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

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

    //COMPONENTES VISUALES:
    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;
    private Button btnSig;
    private Button btnPrev;
    private TextView titulo;

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

                //Se selecciona un elemento en caso de que se encuentre seteado una especialidad en el turno:
                //setSelectedItem();


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

        Medico m = (Medico) spinner.getSelectedItem();
        this.turno.setMedico(m);

        Intent i = new Intent(this, NuevoTurnoWizard2Activity.class);
        Bundle data = new Bundle();
        data.putSerializable("turno", this.turno);
        i.putExtras(data);
        setResult(Activity.RESULT_OK, i);
        finish();

    }

    /**
     * Evento al presionar el boton siguiente
     * @param view
     */
    public void onW3BtnSigClick(View view){
        Toast.makeText(this, "No se ha implementado todavia", Toast.LENGTH_LONG).show();
    }


    public void w3CambiarFechaTurnoClick(View view){

        FechaPickerFragment datePicker = new FechaPickerFragment();

        Bundle arg = new Bundle();
        arg.putInt("fecha_id", R.id.w3TxtFechaTurno);
        datePicker.setArguments(arg);

        datePicker.show(getFragmentManager(), "datePicker");



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

                //Guardar la especialidad medica:
                turno.setMedico(medicos[position]);
                //turno.setEspecialidad(spinnerAdapter.getItem(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Toast.makeText(this, (turno == null ? "es null" : "no es null"), Toast.LENGTH_LONG).show();

        if(turno.getEspecialidad() != null) {

            /*for(int i=0; i<spinner.getCount(); i++){
                if(spinner.getItemAtPosition(i).equals(turno.getEspecialidad().toString())){
                    spinner.setSelection(i);
                    break;
                }
            }*/

           /* String nom = turno.getEspecialidad().getNomEspecialidad(); //the value you want the position for

            ArrayAdapter myAdap = (ArrayAdapter) this.spinner.getAdapter(); //cast to an ArrayAdapter

            int spinnerPosition = myAdap.getPosition(nom);

//set the default according to value
            this.spinner.setSelection(spinnerPosition);*/

            //Toast.makeText(this, ("Id especialidad: " + turno.getEspecialidad().getIdEspecialidad()), Toast.LENGTH_LONG).show();

            //int pos = spinnerAdapter.getPosition(turno.getEspecialidad());

            //spinner.setSelection(pos);
            //spinnerAdapter.notifyDataSetChanged();

        }


        //Botones y Textview:
        this.titulo = (TextView) findViewById(R.id.w3TxtTitulo);
        this.titulo.setText("Nuevo Turno (3/4)");



    }



}
