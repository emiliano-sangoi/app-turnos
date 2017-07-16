package com.example.emiliano.appturnos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HomeActivity extends AppCompatActivity {

    /**
     * Usuario logueado
     */
    private Usuario usuario;

    //Componentes visuales:
    private ScrollView misTurnosContainer;
    private TextView txtView;

    //Constantes
    private final int REQ_NUEVO_TURNO = 8;


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
        }



        //Componentes visuales:
        this.misTurnosContainer = (ScrollView) findViewById(R.id.misTurnosContainer);
        this.txtView = (TextView) findViewById(R.id.txtMsg);
        //this.txtView.setText(usuario.getNombres() + ", " + usuario.getApellidos());

    }

    public void onNuevoTurnoClick(View view){

        Intent i = new Intent(this, NuevoTurnoWizard1Activity.class);
        Bundle data = new Bundle();
        data.putSerializable("usuario", this.usuario);
        i.putExtras(data);
        startActivityForResult(i, REQ_NUEVO_TURNO);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case Activity.RESULT_OK:
                Toast.makeText(this, "Turno creado correctamente!!!", Toast.LENGTH_LONG).show();

            case Activity.RESULT_CANCELED:
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
}
