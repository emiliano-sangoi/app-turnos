package com.example.emiliano.appturnos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    //COMPONENTES VISUALES:

    //CONSTANTES:

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

        this.cargarAfiliaciones();

    }

    /**
     * Carga las especialidades medicas desde el backend
     */
    private void cargarAfiliaciones(){

        OnFinishCallback callback = new OnFinishCallback(this){

            @Override
            public void successAction(Object[] data) {
                //cargar el spinner con las opciones

            }
        };

        this.apiTurnos.getEspecialidades(callback);

    }


    /**
     * Metodo ejecutado al presionar el boton cancelar
     *
     * @param view
     */
    public void onW2BtnCancelar(View view){
        Intent i = new Intent(this, NuevoTurnoWizard1Activity.class);
        setResult(Activity.RESULT_CANCELED, i);
        finish();
    }


    /**
     * Evento al presionar el boton anterior
     *
     * @param view
     */
    public void onW2BtnAntClick(View view){

        Intent i = new Intent(this, NuevoTurnoWizard1Activity.class);
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
    public void onW2BtnSigClick(View view){
        Toast.makeText(this, "No se ha implementado todavia", Toast.LENGTH_LONG).show();
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

    }


}
