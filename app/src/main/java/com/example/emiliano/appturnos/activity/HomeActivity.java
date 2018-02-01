package com.example.emiliano.appturnos.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.emiliano.appturnos.R;
import com.example.emiliano.appturnos.adapter.TurnoAdapter;
import com.example.emiliano.appturnos.backend.APITurnosManager;
import com.example.emiliano.appturnos.backend.Turno;
import com.example.emiliano.appturnos.backend.Usuario;
import com.example.emiliano.appturnos.dialog.TurnoDialog;
import com.example.emiliano.appturnos.event.OnFinishCallback;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {

    /**
     * Usuario logueado
     */
    private Usuario usuario;

    //Componentes visuales:
    private ListView listViewMisTurnos;
    private ListAdapter listViewMisTurnosAdapter;
    private TextView txtMsg;
    private ProgressBar pbProgreso;
    private ArrayList<Turno> misTurnos;
    private TurnoAdapter turnoAdapter;
    private APITurnosManager apiTurnos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Recuperar el usuario:
        Intent i = getIntent();
        Bundle b = i.getExtras();

        if( i.getExtras().getSerializable("usuario") != null) {
            usuario = (Usuario) i.getExtras().getSerializable("usuario");

            //cambiar su estado a logueado:
            usuario.setLogueado(true);

            //Toast.makeText(this,"ES PACIENTE: " + (usuario.esPaciente() ? "Si" : "No"), Toast.LENGTH_LONG).show();
        }

        RequestQueue rq = Volley.newRequestQueue(this);
        this.apiTurnos = new APITurnosManager(rq);

        initUI();

        getTurnos();

    }

    public void initUI(){

        //Componentes visuales:
        this.listViewMisTurnos = (ListView) findViewById(R.id.listadoTurnos);
        this.txtMsg = (TextView) findViewById(R.id.txtMsg);
        pbProgreso = (ProgressBar) findViewById(R.id.pbProgreso);

        if(misTurnos  == null || misTurnos.size() ==  0){
            this.listViewMisTurnos.setVisibility(ListView.GONE);
            this.txtMsg.setVisibility(TextView.VISIBLE);
        }

    }



    public void getTurnos(){

        OnFinishCallback callback = new OnFinishCallback(this) {

            /**
             * Actualiza la vista. En data se encuentra el objeto con los datos, normalmente traidos del modelo.
             *
             * @param data
             */
            @Override
            public void successAction(Object[] data) {

                hideProgressBar();

                //guardar afiliaciones
                Turno[] turnos = (Turno[]) data;
                misTurnos = new ArrayList<Turno>(Arrays.asList(turnos));

                actualizarListadoTurnos();

            }

            @Override
            public void errorAction(String msg) {
                super.errorAction(msg);
                hideProgressBar();
            }
        };

        showProgressBar();
        this.apiTurnos.getTurnosPorPaciente(callback, usuario.getIdPaciente());

    }

    public void actualizarListadoTurnos(){

        if(misTurnos.size() > 0){
            this.listViewMisTurnos.setVisibility(ListView.VISIBLE);
            this.txtMsg.setVisibility(TextView.GONE);
        }else{
            this.listViewMisTurnos.setVisibility(ListView.GONE);
            this.txtMsg.setVisibility(TextView.VISIBLE);
        }

        if(turnoAdapter == null){

            this.turnoAdapter = new TurnoAdapter(this, this.misTurnos);
            this.listViewMisTurnos.setAdapter( this.turnoAdapter );
            this.listViewMisTurnos.setOnItemClickListener(new OnTurnoItemClick(this));
            return;

        }

        //Obtener
        turnoAdapter.clear();
        turnoAdapter.addAll( this.misTurnos );

        //Actualizar adapter
        turnoAdapter.notifyDataSetChanged();

    }

    public void onNuevoTurnoClick(View view){

        Intent i = new Intent(this, Wizard1Activity.class);
        Bundle data = new Bundle();
        data.putSerializable("usuario", this.usuario);
        i.putExtras(data);
        startActivityForResult(i, WizardActivity.REQUEST_W1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case WizardActivity.RESULT_OK:
                break;
            case WizardActivity.RESULT_CREATED:
                Toast.makeText(this, "Turno creado correctamente!!!", Toast.LENGTH_LONG).show();
                break;
            case WizardActivity.RESULT_CANCELED:
                Toast.makeText(this, "La creacion del turno ha sido cancelada.", Toast.LENGTH_LONG).show();
        }

    }



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

    @Override
    public void onBackPressed() {
        return;
    }

    public void showProgressBar(){
        this.pbProgreso.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        this.pbProgreso.setVisibility(View.GONE);
    }



    /**
     * Evento que se ejecuta al hacer tap sobre una ubicacion del listado
     */
    private class OnTurnoItemClick implements AdapterView.OnItemClickListener {

        private AppCompatActivity context;

        public OnTurnoItemClick(AppCompatActivity context) {

            this.context = context;

        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            //Obtener la ubicacion clickeada:
            Turno turno = (Turno) adapterView.getItemAtPosition(i);

            //instanciar un cuadro de dialogo que muestra info de un turno:
            TurnoDialog turnoDialog = new TurnoDialog();
            turnoDialog.show( context.getFragmentManager(), "TurnoInfo");

        }



    }

}
