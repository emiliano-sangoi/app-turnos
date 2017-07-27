package com.example.emiliano.appturnos;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/*
 * En esta actividad el usuario elije la obra social con la que realizara el turno.
 */
public class NuevoTurnoWizard1Activity extends AppCompatActivity {

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
     * Afiliaciones del usuario a distintas obras sociales
     */
    private Afiliacion[] afiliaciones;

    //COMPONENTES VISUALES:
    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;
    private CheckBox checkBoxPagarConsulta;
    private Button btnSig;
    private TextView titulo;


    //CONSTANTES
    public final int REQ_CODE_TO_W2 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_turno_wizard1);

        RequestQueue rq = Volley.newRequestQueue(this);
        this.apiTurnos = new APITurnosManager(rq);

        //Recuperar el usuario:
        Intent i = getIntent();
        this.usuario = (Usuario) i.getExtras().getSerializable("usuario");

        //Instanciar un nuevo turno
        this.turno = new Turno();

        //inicializar y asociar eventos a los componentes visuales:
        this.initUI();

        //obtiene las afiliaciones del backend:
        this.cargarAfiliaciones();
    }


    /**
     * Carga las OS desde el backend y actualiza el spinner.
     */
    private void cargarAfiliaciones() {

        OnFinishCallback callback = new OnFinishCallback(this) {

            /**
             * Actualiza la vista. En data se encuentra el objeto con los datos, normalmente traidos del modelo.
             *
             * @param data
             */
            @Override
            public void successAction(Object[] data) {

                //guardar afiliaciones
                afiliaciones = (Afiliacion[]) data;

                //cargar el spinner con las opciones
                cargarSpinner(data);
                setOSSeleccionada();

                //Habilitar el boton siguiente:
                btnSig.setEnabled(true);

            }
        };

        this.btnSig.setEnabled(false);
        this.apiTurnos.getAfiliaciones(callback, 12);

    }


    /**
     * Funcion auxiliar para cargar el contenido del Spinner con las OS.
     *
     * @param afiliaciones
     */
    private void cargarSpinner(Object[] afiliaciones) {

        this.spinnerAdapter = new ArrayAdapter(this, R.layout.my_spinner_layout, afiliaciones);
        // adapter.setDropDownViewResource(R.layout.my_spinner_layout);
        this.spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        this.spinner.setAdapter(this.spinnerAdapter);

    }


    public void setOSSeleccionada(){
        //setear el item seleccionado:
        if(turno.getEspecialidad() != null){

            for(int i=0; i<afiliaciones.length; i++){
                if(afiliaciones[i].getIdOs() == turno.getObraSocialId()){
                    this.spinner.setSelection(this.spinnerAdapter.getPosition(afiliaciones[i]));
                    break;
                }
            }

        }else{
            if(afiliaciones.length > 0){
                this.spinner.setSelection(this.spinnerAdapter.getPosition(afiliaciones[0]));
            }

        }
    }

    /**
     * Metodo ejecutado al presionar el boton cancelar
     *
     * @param view
     */
    public void onW1BtnCancelar(View view) {

        Intent i = new Intent(this, HomeActivity.class);
        setResult(Activity.RESULT_CANCELED, i);
        finish();

    }


    /**
     * Evento al presionar el boton siguiente
     *
     * @param view
     */
    public void onW1BtnSigClick(View view) {

        Intent i = new Intent(this, NuevoTurnoWizard2Activity.class);
        Bundle data = new Bundle();
        data.putSerializable("usuario", this.usuario);
        data.putSerializable("turno", this.turno);
        i.putExtras(data);
        startActivityForResult(i, REQ_CODE_TO_W2);

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
                Intent i = new Intent(this, HomeActivity.class);
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

        //componentes visuales
        this.spinner = (Spinner) findViewById(R.id.w1SpinnerAfiliaciones);
        this.checkBoxPagarConsulta = (CheckBox) findViewById(R.id.w1CKBoxPagarConsulta);

        //Eventos
        this.checkBoxPagarConsulta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinner.setEnabled(false);
                    //el usuario decide pagar la consulta:
                    turno.setObraSocialId(null);

                } else {
                    spinner.setEnabled(true);
                }
            }

        });

        //Cuando se seleccione un item del listado de afiliaciones a una obra social:
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Afiliacion afiliacion = afiliaciones[position];

                //Guardar datos de la obra social seleccionada:
                turno.setObraSocialId(afiliacion.getIdOs());
                turno.setObraSocialNombre(afiliacion.getNombre());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //el usuario decide pagar la consulta:
                turno.setObraSocialId(null);
            }
        });

        this.btnSig = (Button) findViewById(R.id.w1BtnSig);
        this.titulo = (TextView) findViewById(R.id.w1TxtTitulo);
        this.titulo.setText("Nuevo Turno (1/4)");

    }


}
