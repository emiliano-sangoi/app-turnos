package com.example.emiliano.appturnos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

    private APITurnosManager apiTurnos;

    /*View components: */
    private EditText etUsername;
    private EditText etPassword;
    private Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.apiTurnos = new APITurnosManager(getApplicationContext());

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);

        //api.fetchPacientes();


    }

    public void onIngresar(View view){

        //Callback que se ejecuta cuando la peticion HTTP se completa:
        OnFinishCallback callback = new OnFinishCallback(this){

            @Override
            public void successAction() {

                Intent i = new Intent(this.getContext(), HomeActivity.class);
                i.putExtras(getData());

                startActivity(i);

            }
        };

        String username = this.etUsername.getText().toString();
        String password = this.etPassword.getText().toString();

        this.apiTurnos.login(callback, username, password);

    }




}
