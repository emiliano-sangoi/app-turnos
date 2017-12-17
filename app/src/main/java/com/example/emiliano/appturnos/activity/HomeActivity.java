package com.example.emiliano.appturnos.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emiliano.appturnos.R;
import com.example.emiliano.appturnos.backend.Usuario;

public class HomeActivity extends AppCompatActivity {

    /**
     * Usuario logueado
     */
    private Usuario usuario;

    //Componentes visuales:
    private ScrollView misTurnosContainer;
    private TextView txtView;


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



        //Componentes visuales:
        this.misTurnosContainer = (ScrollView) findViewById(R.id.misTurnosContainer);
        this.txtView = (TextView) findViewById(R.id.txtMsg);
        //this.txtView.setText(usuario.getNombres() + ", " + usuario.getApellidos());

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
}
