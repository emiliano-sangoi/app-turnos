package com.example.emiliano.appturnos.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.emiliano.appturnos.R;
import com.example.emiliano.appturnos.backend.Turno;
import com.example.emiliano.appturnos.backend.Usuario;
import com.example.emiliano.appturnos.backend.APITurnosManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class WizardActivity extends AppCompatActivity {

    //Constantes
    public static final int REQUEST_W1 = 1;
    public static final int REQUEST_W2 = 2;
    public static final int REQUEST_W3 = 3;
    public static final int REQUEST_W4 = 4;
    public static final int RESULT_CREATED = 10;

    //Atributos
    protected Turno turno;
    protected Usuario usuario;
    protected Class<?> wSiguiente;
    protected Class<?> wAnterior;
    protected int requestCodeSig;
    protected APITurnosManager apiTurnos;
    protected SimpleDateFormat formatter;
    protected Integer pasoActual;
    private Integer totalPasos;

    //UI
    protected Button btnSiguiente;
    protected Button btnAnterior;
    protected Button btnCancelar;
    protected Button btnFinalizar;
    protected TextView tvTitulo;
    protected TextView tvSubtitulo;
    protected TextView tvDescripcion;
    protected ProgressBar pbProgreso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Recuperar el usuario:
        Intent i = getIntent();
        if(i.hasExtra("usuario")){
            this.usuario = (Usuario) i.getExtras().getSerializable("usuario");
        }

        //Recuperar el turno:
        if(i.hasExtra("turno")){
            this.turno = (Turno) i.getExtras().getSerializable("turno");
        }

        RequestQueue rq = Volley.newRequestQueue(this);
        this.apiTurnos = new APITurnosManager(rq);

        //formatter:
        this.formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        pasoActual = 0;
        totalPasos = 4;

    }

    /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ITERADORES <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

    /**
     * Invoca al siguiente wizard
     *
     */
    public void siguiente(){

        Intent i = new Intent(this, wSiguiente);
        Bundle data = new Bundle();
        data.putSerializable("usuario", this.usuario);
        data.putSerializable("turno", this.turno);
        i.putExtras(data);
        startActivityForResult(i, requestCodeSig);

    }

    /**
     * Vuelve al wizard anterior
     *
     * @param code
     */
    public void anterior(int code){

        Intent i = new Intent(this, wAnterior);
        Bundle data = new Bundle();
        data.putSerializable("turno", this.turno);
        i.putExtras(data);
        finalizar(RESULT_OK, i);

    }

    /**
     * Finaliza la actividad con el codigo especificado
     */
    public void finalizar(int code, Intent i){
        setResult(code, i);
        finish();
    }

    public void finalizar(int code){
        setResult(code);
        finish();
    }

    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> FIN ITERADORES >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */


    /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< OVERRIDE <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode){
            case RESULT_CREATED:
                finalizar(RESULT_CREATED);
                break;
            case RESULT_OK:
                //Actualizar turno:
                this.turno = (Turno) data.getExtras().getSerializable("turno");
                break;
            case RESULT_CANCELED:
                finalizar(RESULT_CANCELED);
                break;
        }


        //super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        finalizar(RESULT_OK);
    }

    // Menu principal
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main_menu, menu);
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

    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> FIN OVERRIDE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */


    /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< MANEJO UI <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

    /**
     * Funcion que se encarga de buscar y guardar las referencias a los componentes visuales
     */
    public void initUI(){
        //Botones y Textview:
        this.btnAnterior = (Button) findViewById(R.id.btnAnterior);
        this.btnSiguiente = (Button) findViewById(R.id.btnSiguiente);
        this.btnCancelar = (Button) findViewById(R.id.btnCancelar);

        // Textview
        this.tvTitulo = (TextView) findViewById(R.id.tvTitulo);
        this.tvTitulo.setText( getTitulo() );
        this.tvSubtitulo = (TextView) findViewById(R.id.tvSubtitulo);
        this.tvDescripcion = (TextView) findViewById(R.id.tvDescripcion);
        pbProgreso = (ProgressBar) findViewById(R.id.pbProgreso);

    }

    public void onClickBtnSiguiente(View view){
        siguiente();
    }

    public void onClickBtnAnterior(View view){
        anterior(RESULT_OK);
    }

    public void onClickBtnFinalizar(View view){
        finalizar(RESULT_CREATED);
    }

    public void onClickBotonCancelar(View view){
        finalizar(RESULT_CANCELED);
    }

    public void mostrarToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> FIN MANEJO UI >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */




    /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTERS & SETTERS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */

    public static String formatFechaHoraTurno(Date fechaTurno){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy H:mm");
        return simpleDateFormat.format(fechaTurno);
    }

    public Integer getTotalPasos() {
        return totalPasos;
    }

    public String getTitulo(){
        return getString(R.string.wTitulo) +  " (" + pasoActual + "/" + totalPasos + ")";
    }

    public void showProgressBar(){
        this.pbProgreso.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        this.pbProgreso.setVisibility(View.GONE);
    }

    /* <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< FIN GETTERS & SETTERS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< */
}
