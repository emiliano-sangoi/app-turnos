package com.example.emiliano.appturnos.backend;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.emiliano.appturnos.event.OnFinishCallback;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by emiliano on 05/07/17.
 * <p>
 * Tutorial:
 * https://kylewbanks.com/blog/tutorial-parsing-json-on-android-using-gson-and-volley
 * https://developer.android.com/training/volley/request.html
 * http://howtodoinjava.com/apache-commons/google-gson-tutorial-convert-java-object-to-from-json/
 * http://arnab.ch/blog/2013/08/asynchronous-http-requests-in-android-using-volley/
 */

public class APITurnosManager implements Serializable{

    private String host;
    private Integer port;
    private String basePath;

    private String EP_PACIENTES;
    private String EP_LOGIN;
    private String EP_OBRAS_SOCIALES;
    private String EP_ESPECIALIDADES;
    private String EP_MEDICOS;
    private String EP_TURNOS;

    private RequestQueue requestQueue;
    private Usuario usuario;
    private String ultimoError;
    private SimpleDateFormat formatter;
    private JsonParser jsonParser;
    private Response.ErrorListener defaultResponseErrorListener;
    private OnFinishCallback callback;

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
        //localhost:
        //this.host = "192.168.1.104";
        //this.port = 8080;

        //Prod:
        this.host = "www.emiliano-sangoi.com.ar";
        this.port = 80;
        this.basePath = "api-turnos/api/index.php";

        //URLs:
        EP_PACIENTES = this.getBaseUrl() + "/pacientes";
        EP_LOGIN = this.getBaseUrl() + "/login";
        EP_OBRAS_SOCIALES = this.getBaseUrl() + "/os";
        EP_ESPECIALIDADES = this.getBaseUrl() + "/especialidades/";
        EP_MEDICOS = this.getBaseUrl() + "/medicos";
        EP_TURNOS = this.getBaseUrl() + "/turno";

        this.formatter = new SimpleDateFormat("yMMdd");

        this.jsonParser = new JsonParser();

        defaultResponseErrorListener = new ResponseErrorListener(this);

    }

    private JsonArray parseData(String response){
        JsonElement jsonElement = jsonParser.parse( response.toString() );
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();

        return jsonArray;
    }

    public JsonElement parseMsg(String response){
        JsonElement jsonElement = jsonParser.parse( response.toString() );
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement msgJsonObject = jsonObject.get("msg");

        return msgJsonObject;
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

        Log.i("EP LOGIN",  EP_LOGIN);

        JsonObjectRequest request = new JsonObjectRequest(EP_LOGIN, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                            Gson gson = new Gson();

                            JsonArray jsonArray = parseData( response.toString() );
                            String jsonUsuario = jsonArray.get(0).getAsJsonObject().toString();

                            usuario = gson.fromJson( jsonUsuario , Usuario.class);

                            callback.successAction(usuario);

                            Log.i("USUARIO: ", response.toString());

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try{
                            switch (error.networkResponse.statusCode) {
                                case 401: //unauthorized
                                    callback.errorAction("Usuario y/o contraseña incorrectos");
                                    break;
                                case 403: //unauthorized
                                    callback.errorAction("Usuario y/o contraseña incorrectos");
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

        this.requestQueue.add(request);

    }

    public void nuevoTurno(final OnFinishCallback callback, Turno turno) {

        this.ultimoError = "";
        this.setCallback(callback);

        Map<String, String> params = turno.getHttpPostParams();

        String url = new String( EP_TURNOS + "/nuevo");

        Log.i("EP TURNO",  EP_TURNOS);

        JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        JsonArray jsonArray = parseData( response.toString() );
                        String jsonUsuario = jsonArray.get(0).getAsJsonObject().toString();

                        callback.successAction(jsonUsuario);

                        Log.i("TURNO CREADO: ", response.toString());

                    }

                },
                defaultResponseErrorListener
        );

        this.requestQueue.add(request);
    }

    public void bajaTurno(final OnFinishCallback callback, final Integer idTurno) {

        this.ultimoError = "";
        this.setCallback(callback);

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", idTurno.toString());

        String url = new String( EP_TURNOS + "/baja");

        JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        JsonArray jsonArray = parseData( response.toString() );
                        String jsonTurno = jsonArray.get(0).getAsJsonObject().toString();

                        //Convertir a un objeto turno:
                        Gson gson = new Gson();
                        Turno turno = gson.fromJson(jsonTurno, Turno.class);

                        callback.successAction(turno);

                        Log.i("TURNO ANULADO: ", response.toString());

                    }

                },
                defaultResponseErrorListener
        );

        this.requestQueue.add(request);
    }

    public void getTurnosPorPaciente(final OnFinishCallback callback, final Integer idPaciente){

        String url = EP_TURNOS + "/paciente/" + idPaciente;
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
                        ArrayList<Turno>turnos = new ArrayList<>();

                        JsonArray jsonArray = parseData(response);
                        Iterator<JsonElement> iterator = jsonArray.iterator();
                        while (iterator.hasNext()){

                            JsonObject jsonTurno = iterator.next().getAsJsonObject();
                            Turno turno = gson.fromJson(jsonTurno.toString(), Turno.class);
                            turnos.add(turno);

                        }

                        Log.i("TURNOS PACIENTE" + idPaciente + ": ", "" + turnos.size());

                        callback.successAction( turnos.toArray(new Turno[turnos.size()]) );

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

                        JsonArray jsonArray = parseData(response);
                        String jsonAfiliaciones = jsonArray.toString();

                        Afiliacion[] afiliaciones = gson.fromJson( jsonAfiliaciones, Afiliacion[].class);

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

                        JsonArray jsonArray = parseData(response);
                        String jsonEspecialidades = jsonArray.toString();

                        Especialidad[] especialidades = gson.fromJson(jsonEspecialidades, Especialidad[].class);

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

                        JsonArray jsonArray = parseData(response);
                        String jsonMedicos = jsonArray.toString();

                        Medico[] medicos = gson.fromJson(jsonMedicos, Medico[].class);

                        Log.i("MEDICOS -> size = ", medicos.length + "");

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

    public void getHorariosPorMedico(final OnFinishCallback callback, Integer idMedico){


        String url = this.getBaseUrl() + "/horarios/medico/" + idMedico;

        Log.i("HORARIOS -> ", "getHorariosPorMedico(...) -> Obteniendo horarios de " + url);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    /**
                     * Called when a response is received.
                     *
                     * @param response
                     */
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        JsonArray jsonArray = parseData(response);
                        String jsonHorarios = jsonArray.toString();

                        HorarioAtencion[] horariosAtencion = gson.fromJson(jsonHorarios, HorarioAtencion[].class);

                        Log.i("HORARIOS -> ", "getHorariosPorMedicoYDia() -> Cant. registros obtenidos: " + horariosAtencion.length);

                        callback.successAction(horariosAtencion);

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


    //CALLBACKS:

    //--------------------------------------------------------------------------------------------

    // Getters & Setter

    public String getUltimoError() {
        return ultimoError;
    }

    public void setUltimoError(String ultimoError) {
        this.ultimoError = ultimoError;
    }

    public OnFinishCallback getCallback() {
        return callback;
    }

    public void setCallback(OnFinishCallback callback) {
        this.callback = callback;
    }

    /*
       La IP publica fue sacada desde la terminal con hostname -I
       Por ej: 192.168.0.61 (esta ip va cambiando)
       Luego se levanta la api (PHP Builtin Server) con:
                  php -S 192.168.0.61:8080
        */
    public String getBaseUrl(){
        return "http://" + this.host + ":" + this.port + "/" + this.basePath;
    }

    // convert from UTF-8 -> internal Java String format
    public static String convertFromUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("ISO-8859-1"), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }
}
