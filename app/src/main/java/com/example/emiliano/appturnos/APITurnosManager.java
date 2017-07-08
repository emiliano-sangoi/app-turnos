package com.example.emiliano.appturnos;

import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by emiliano on 05/07/17.
 * <p>
 * Tutorial:
 * https://kylewbanks.com/blog/tutorial-parsing-json-on-android-using-gson-and-volley
 * https://developer.android.com/training/volley/request.html
 * http://howtodoinjava.com/apache-commons/google-gson-tutorial-convert-java-object-to-from-json/
 * http://arnab.ch/blog/2013/08/asynchronous-http-requests-in-android-using-volley/
 */

public class APITurnosManager {

    /*
     La IP publica fue sacada desde la terminal con hostname -I
     Por ej: 192.168.0.61 (esta ip va cambiando)
     Luego se levanta la api (PHP Builtin Server) con:
                php -S 192.168.0.61:8080
      */
    //public String endpoint_pacientes="http://192.168.0.61:8080/api/pacientes/1";
    public String base = "http://192.168.0.67:8080/api";
    private final String EP_PACIENTES = base + "/pacientes";
    private final String EP_LOGIN = base + "/login";

    private RequestQueue requestQueue;
    private Context context;
    private Usuario usuario;
    private String ultimoError;

    public APITurnosManager(Context context) {

        //Inicializacion de variables:
        //contexto:
        this.context = context;
        //cola de request HTTP (gestionada por Volley):
        this.requestQueue = Volley.newRequestQueue(this.context);
        //Ultimos error:
        this.ultimoError = new String();


    }


    /**
     * Permite loguear un usuario en caso de que el usuario y contraseña suministrado correspondan a un usuario existente.
     * EL objeto callback permite ejecutar alguna accion cuando se reciba la respuesta del servidor.
     *
     * @param callback
     * @param username
     * @param password
     */
    public void login(final OnFinishCallback callback, String username, String password) {

        this.ultimoError = "";

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);



        JsonObjectRequest request = new JsonObjectRequest(EP_LOGIN, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                            Gson gson = new Gson();

                            usuario = gson.fromJson(response.toString(), Usuario.class);
                            Bundle data = new Bundle();
                            data.putSerializable("usuario", usuario);
                            callback.setData(data);
                            callback.successAction();

                            Log.i("USUARIO: ", response.toString());

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        switch (error.networkResponse.statusCode){
                            case 401: //unauthorized
                                Toast.makeText(callback.getContext(), "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(callback.getContext(),
                                        "Ocurrio un error desde la API. HTTTP Code: " + error.networkResponse.statusCode,
                                        Toast.LENGTH_LONG).show();
                                ultimoError = error.toString();
                                Log.e("LOGIN ERROR:", error.toString());
                        }
                    }

                }
        );



       //StringRequest request = new StringRequest(Request.Method.POST, EP_LOGIN, onLoginSuccess, onLoginError) {


        //Log.i("HTTP METHOD", "" + request.getErrorListener()..getMethod());

        this.requestQueue.add(request);

    }

    public void fetchPacientes() {

        StringRequest request = new StringRequest(Request.Method.GET, EP_PACIENTES, onPacientesLoaded, onPacientesError);

        this.requestQueue.add(request);

    }


    //CALLBACKS:

    //--------------------------------------------------------------------------------------------
    //LOGIN
    private Response.Listener<JSONObject> onLoginSuccess = new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
            Gson gson = new Gson();

            //usuario = gson.fromJson(response, Usuario.class);

            Log.i("USUARIO: ", response.toString());
            //Log.i("USUARIO: ", usuario.toString());
        }


    };

    private Response.ErrorListener onLoginError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {


            ultimoError = error.toString();
            Log.e("LOGIN ERROR:", error.toString());
        }


    };

    //--------------------------------------------------------------------------------------------
    //PACIENTES
    private final Response.Listener<String> onPacientesLoaded = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            Log.i("Pacientes", response);

            Gson gson = new Gson();
            Usuario[] lista = gson.fromJson(response, Usuario[].class);

            Log.i("Cantidad: ", lista.length + "");
        }

    };

    private final Response.ErrorListener onPacientesError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("Pacientes", error.toString());
        }
    };

    private final Response.ErrorListener onResponseError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("ERROR: ", error.toString());
        }
    };


    //--------------------------------------------------------------------------------------------
    //MEDICOS

}
