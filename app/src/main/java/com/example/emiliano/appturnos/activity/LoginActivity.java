package com.example.emiliano.appturnos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.emiliano.appturnos.event.OnFinishCallback;
import com.example.emiliano.appturnos.R;
import com.example.emiliano.appturnos.backend.Usuario;
import com.example.emiliano.appturnos.backend.APITurnosManager;
import com.example.emiliano.appturnos.validaciones.PasswordWatcher;
import com.example.emiliano.appturnos.validaciones.UsuarioWatcher;

public class LoginActivity extends Activity {

    private APITurnosManager apiTurnos;

    /*View components: */
    private EditText etUsername;
    private EditText etPassword;
    private TextView txtGithub;
    private Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        RequestQueue rq = Volley.newRequestQueue(this);
        this.apiTurnos = new APITurnosManager(rq);

        initUI();
    }

    public void initUI(){

        etUsername = (EditText) findViewById(R.id.etUsername);
        etUsername.addTextChangedListener(new UsuarioWatcher(etUsername));
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword.addTextChangedListener(new PasswordWatcher(etPassword));

        //Repo Github
        this.txtGithub = (TextView) findViewById(R.id.txtGithub);
        this.txtGithub.setMovementMethod(LinkMovementMethod.getInstance());
        this.txtGithub.setClickable(true);
        String text = getResources().getString(R.string.repo_github_link);
        this.txtGithub.setText(Html.fromHtml(text));

        btnIngresar = (Button) findViewById(R.id.btnIngresar);

    }

    public void onIngresar(View view){

        //Callback que se ejecuta cuando la peticion HTTP se completa:
        OnFinishCallback callback = new OnFinishCallback(this){

            @Override
            public void successAction(Object data) {

                Intent i = new Intent(this.getContext(), HomeActivity.class);
                Usuario usuario = (Usuario) data;
                Bundle bundle = new Bundle();
                bundle.putSerializable("usuario", usuario);

                i.putExtras(bundle);

                startActivity(i);

            }

            @Override
            public void errorAction(String msg) {
                showToast(msg);
            }
        };

        String username = this.etUsername.getText().toString();
        String password = this.etPassword.getText().toString();

        this.apiTurnos.login(callback, username, password);

    }




}
