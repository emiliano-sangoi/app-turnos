package com.example.emiliano.appturnos;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/*
 * En esta actividad el usuario elije la obra social con la que realizara el turno.
 */
public class NuevoTurnoWizard1Activity extends AppCompatActivity {

    private APITurnosManager apiTurnos;
    private Spinner spinnerAfiliaciones;
    private CheckBox checkBoxPagarConsulta;

    private Usuario usuario;
    //private RadioGroup RGroup;
    //private RadioButton rbPagarConsulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_turno_wizard1);

        RequestQueue rq = Volley.newRequestQueue(this);
        this.apiTurnos = new APITurnosManager(rq);

        //Recuperar el usuario:
        Intent i = getIntent();
        usuario = (Usuario) i.getExtras().getSerializable("usuario");

        cargarAfiliaciones();

        //componentes visuales
        this.spinnerAfiliaciones = (Spinner) findViewById(R.id.w1SpinnerAfiliaciones);
        this.checkBoxPagarConsulta = (CheckBox) findViewById(R.id.w1CKBoxPagarConsulta);

        this.checkBoxPagarConsulta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    spinnerAfiliaciones.setEnabled(false);
                }else{
                    spinnerAfiliaciones.setEnabled(true);
                }
            }
        });

    }

    /**
     * Carga las OS desde el backend y actualiza el spinner.
     */
    private void cargarAfiliaciones(){

        OnFinishCallback callback = new OnFinishCallback(this){

            /**
             * Actualiza la vista. En data se encuentra el objeto con los datos, normalmente traidos del modelo.
             *
             * @param data
             */
            @Override
            public void successAction(Object[] data) {
                //cargar el spinner con las opciones
                cargarSpinner(data);

            }
        };

        this.apiTurnos.getAfiliaciones(callback, 12);

    }


    /**
     * Funcion auxiliar para cargar el contenido del Spinner con las OS.
     * @param afiliaciones
     */
    private void cargarSpinner(Object[] afiliaciones){

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.my_spinner_layout, afiliaciones);
        adapter.setDropDownViewResource(R.layout.my_spinner_layout);
        this.spinnerAfiliaciones.setAdapter(adapter);

    }

    /**
     * Metodo ejecutado al presionar el boton cancelar
     *
     * @param view
     */
    public void onW1BtnCancelar(View view){
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
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




}
