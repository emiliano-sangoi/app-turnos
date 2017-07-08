package com.example.emiliano.appturnos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HomeActivity extends AppCompatActivity {

private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Recuperar el usuario:
        Intent i = getIntent();
        Usuario usuario = (Usuario) i.getExtras().getSerializable("usuario");
        usuario.setLogueado(true);

        //Componentes visuales:
        this.txtView = (TextView) findViewById(R.id.txtMsg);
        this.txtView.setText(usuario.getNombres() + ", " + usuario.getApellidos());


    }


}
