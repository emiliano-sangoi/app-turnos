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

    private String host;
    private Integer port;

    private String EP_PACIENTES;
    private String EP_LOGIN;
    private String EP_OBRAS_SOCIALES;
    private String EP_ESPECIALIDADES;
    private String EP_MEDICOS;

    private RequestQueue requestQueue;
    private Usuario usuario;
    private String ultimoError;

    /**
     * Constructor
     *
     * @param requestQueue
     */
    public APITurnosManager(RequestQueue requestQueue) {

        //Inicializacion de variables:

        //cola de request HTTP (gestionada por Volley):
        this.requestQueue = requestQueue;

        //Ultimos error:
        this.ultimoError = new String();

        //Parametros de configuracion:
        this.host = "192.168.0.14";
        this.port = 8080;

        //URLs:
        EP_PACIENTES = this.getBaseUrl() + "/pacientes";
        EP_LOGIN = this.getBaseUrl() + "/login";
        EP_OBRAS_SOCIALES = this.getBaseUrl() + "/os";
        EP_ESPECIALIDADES = this.getBaseUrl() + "/especialidades";
        EP_MEDICOS = this.getBaseUrl() + "/medicos";


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

                            callback.successAction(usuario);

                            Log.i("USUARIO: ", response.toString());
                            //Log.i("ES PACIENTE: ", usuario.esPaciente() ? "Si" : "No");

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try{
                            switch (error.networkResponse.statusCode) {
                                case 401: //unauthorized
                                    callback.showToast("Usuario y/o contraseña incorrectos");
                                    break;
                                default:
                                    callback.showToast("Ocurrio un error desde la API.");
                                    ultimoError = error.toString();
                                    Log.e("LOGIN ERROR:", error.toString());
                            }

                        }catch (NullPointerException e){
                            callback.showToast("Ocurrio un error al intentar verificar las credenciales. Probablemente el servidor no se encuentre funcionando correctamente.");
                            e.printStackTrace();
                            Log.d("ERROR", e.getMessage());
                        }

                    }

                }
        );



       //StringRequest request = new StringRequest(Request.Method.POST, EP_LOGIN, onLoginSuccess, onLoginError) {


        //Log.i("HTTP METHOD", "" + request.getErrorListener()..getMethod());

        this.requestQueue.add(request);

    }


    public void getAfiliaciones(final OnFinishCallback callback, int id_paciente){

        String url = new String( EP_OBRAS_SOCIALES + "/afiliaciones/paciente/" + id_paciente);
        StringRequest request = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    /**
                     * Called when a response is received.
                     *
                     * @param response
                     */
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Afiliacion[] afiliaciones = gson.fromJson(response.toString(), Afiliacion[].class);

                        Log.i("AFILIACION", "" + afiliaciones.length);

                        callback.successAction(afiliaciones);

                    }
                },
                new Response.ErrorListener() {

                    /**
                     * Callback method that an error has been occurred with the
                     * provided error code and optional user-readable message.
                     *
                     * @param error
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.showToast("Ocurrio un error al realizar la consulta al servidor.");

                    }
                });


        this.requestQueue.add(request);

    }

    public void getEspecialidades(final OnFinishCallback callback){

        StringRequest request = new StringRequest(Request.Method.GET,
                EP_ESPECIALIDADES,
                new Response.Listener<String>() {
                    /**
                     * Called when a response is received.
                     *
                     * @param response
                     */
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Especialidad[] especialidades = gson.fromJson(response.toString(), Especialidad[].class);

                        //Log.i("ESPECIALIDADES", especialidades.length + "");
                        Log.i("ESPECIALIDADES", response.toString());

                        callback.successAction(especialidades);

                    }
                },
                new Response.ErrorListener() {

                    /**
                     * Callback method that an error has been occurred with the
                     * provided error code and optional user-readable message.
                     *
                     * @param error
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.showToast("Ocurrio un error al realizar la consulta al servidor.");
                    }
                });


        this.requestQueue.add(request);

    }

    public void getMedicos(final OnFinishCallback callback, Integer idEsp){

        StringRequest request = new StringRequest(Request.Method.GET,
                EP_MEDICOS + "/especialidad/" + idEsp,
                new Response.Listener<String>() {
                    /**
                     * Called when a response is received.
                     *
                     * @param response
                     */
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Medico[] medicos = gson.fromJson(response.toString(), Medico[].class);

                        Log.i("MEDICOS -> size = ", medicos.length + "");
                        //Log.i("MEDICOS ->", response.toString());

                        callback.successAction(medicos);

                    }
                },
                new Response.ErrorListener() {

                    /**
                     * Callback method that an error has been occurred with the
                     * provided error code and optional user-readable message.
                     *
                     * @param error
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.showToast("Ocurrio un error al realizar la consulta al servidor.");
                    }
                });


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



    //Getters & Setters:


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    /*
   La IP publica fue sacada desde la terminal con hostname -I
   Por ej: 192.168.0.61 (esta ip va cambiando)
   Luego se levanta la api (PHP Builtin Server) con:
              php -S 192.168.0.61:8080
    */
    public String getBaseUrl(){
        return "http://" + this.host + ":" + this.port + "/api";
    }
}
