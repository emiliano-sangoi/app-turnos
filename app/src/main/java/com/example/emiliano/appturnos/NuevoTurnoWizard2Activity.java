package com.example.emiliano.appturnos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * En esta actividad el usuario define la especialidad medica
 */

public class NuevoTurnoWizard2Activity extends AppCompatActivity {

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
     * Especialidades medicas
     */
    private Especialidad[] especialidades;

    //COMPONENTES VISUALES:
    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;
    private Button btnCancel;
    private Button btnAnt;
    private Button btnSig;
    private TextView titulo;
    private ProgressBar progressBar;

    //CONSTANTES:
    public final int REQ_CODE_TO_W3 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_turno_wizard2);

        RequestQueue rq = Volley.newRequestQueue(this);
        this.apiTurnos = new APITurnosManager(rq);

        //Recuperar el usuario:
        Intent i = getIntent();
        usuario = (Usuario) i.getExtras().getSerializable("usuario");
        turno = (Turno) i.getExtras().getSerializable("turno");

        //inicializar y asociar eventos a los componentes visuales:
        this.initUI();

        this.getEspecialidades();

        //Toast.makeText(this,"Especialidad: " + (turno.getEspecialidad()!= null ? turno.getEspecialidad().getIdEspecialidad() : "Null"), Toast.LENGTH_LONG).show();

    }

    /**
     * Carga las especialidades medicas desde el backend
     */
    private void getEspecialidades() {

        OnFinishCallback callback = new OnFinishCallback(this) {

            @Override
            public void successAction(Object[] data) {
                //cargar el spinner con las opciones
                especialidades = (Especialidad[]) data;

                cargarSpinner();
                setEspSeleccionada();

                //Habilitar el boton siguiente:
                btnSig.setEnabled(true);

                //Ocultar la barra de progreso:
                progressBar.setVisibility(View.GONE);

            }
        };

        //deshabilitar el boton mientras se realiza la peticion al servidor:
        this.btnSig.setEnabled(false);
        this.apiTurnos.getEspecialidades(callback);

    }

    /**
     * Llena el spinner con los valores obtenidos
     */
    private void cargarSpinner() {

        this.spinnerAdapter = new ArrayAdapter(this, R.layout.my_spinner_layout, this.especialidades);
        this.spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        this.spinner.setAdapter(this.spinnerAdapter);

    }


    public void setEspSeleccionada(){
        //setear el item seleccionado:
        if(turno.getEspecialidad() != null){

            for(int i=0; i<especialidades.length; i++){
                if(especialidades[i].getIdEspecialidad().equals(turno.getEspecialidad().getIdEspecialidad())){
                    this.spinner.setSelection(this.spinnerAdapter.getPosition(especialidades[i]));
                    break;
                }
            }

        }else{
            if(especialidades.length > 0){
                this.spinner.setSelection(this.spinnerAdapter.getPosition(especialidades[0]));
            }

        }
    }

    /**
     * Metodo ejecutado al presionar el boton cancelar
     *
     * @param view
     */
    public void onW2BtnCancelar(View view) {
        Intent i = new Intent(this, NuevoTurnoWizard1Activity.class);
        setResult(Activity.RESULT_CANCELED, i);
        finish();
    }


    /**
     * Evento al presionar el boton anterior
     *
     * @param view
     */
    public void onW2BtnAntClick(View view) {

        Especialidad esp = (Especialidad) spinner.getSelectedItem();
        this.turno.setEspecialidad(esp);
        //Log.i("ESP: " , esp.toString());


        Intent i = new Intent(this, NuevoTurnoWizard1Activity.class);
        Bundle data = new Bundle();
        data.putSerializable("turno", this.turno);
        i.putExtras(data);
        setResult(Activity.RESULT_OK, i);
        finish();

    }

    /**
     * Evento al presionar el boton siguiente
     *
     * @param view
     */
    public void onW2BtnSigClick(View view) {

        Especialidad esp = (Especialidad) spinner.getSelectedItem();
        this.turno.setEspecialidad(esp);

        Intent i = new Intent(this, NuevoTurnoWizard3Activity.class);
        Bundle data = new Bundle();
        data.putSerializable("usuario", this.usuario);
        data.putSerializable("turno", this.turno);
        i.putExtras(data);
        startActivityForResult(i, REQ_CODE_TO_W3);


        //Toast.makeText(this, "No se ha implementado todavia", Toast.LENGTH_LONG).show();
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
                Intent i = new Intent(this, NuevoTurnoWizard1Activity.class);
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
        switch (item.getItemId()) {
            case R.id.main_menu_item_salir:
                usuario.setLogueado(false);
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**********************************************************************************/
    // Iniciar componentes UI
    private void initUI() {

        this.spinner = (Spinner) findViewById(R.id.spinnerEsp);

        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                btnSig.setEnabled(true);

                //Guardar la especialidad medica:
                //turno.setEspecialidad(spinnerAdapter.getItem(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Boton siguiente:
        this.btnSig = (Button) findViewById(R.id.w2BtnSig);

        this.titulo = (TextView) findViewById(R.id.w2TxtTitulo);
        this.titulo.setText("Nuevo Turno (2/4)");
        this.progressBar = (ProgressBar) findViewById(R.id.w2IndeterminateBar);
        this.progressBar.setVisibility(View.VISIBLE);

    }


}
