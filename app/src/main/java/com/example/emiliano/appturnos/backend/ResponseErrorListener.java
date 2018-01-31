package com.example.emiliano.appturnos.backend;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonElement;

import java.io.UnsupportedEncodingException;

/**
 * Created by emi88 on 1/29/18.
 */

public class ResponseErrorListener implements Response.ErrorListener {

    private APITurnosManager apiTurnosManager;

    public ResponseErrorListener(APITurnosManager apiTurnosManager) {
        this.apiTurnosManager = apiTurnosManager;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        try{

            String msgError = new String();
            switch (error.networkResponse.statusCode) {
                default:
                    try {
                        String response = new String(error.networkResponse.data, "utf-8");
                        JsonElement msg = this.apiTurnosManager.parseMsg(response);
                        msgError = msg.getAsString();
                        this.apiTurnosManager.getCallback().errorAction( msgError );


                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        this.apiTurnosManager.getCallback().showToast("Ocurrio un error al intentar procesar la solicitud.");
                    }

                    this.apiTurnosManager.setUltimoError(msgError);

            }

        }catch (NullPointerException e){
            this.apiTurnosManager.getCallback().showToast("Ocurrio un error al intentar procesar la solicitud. Probablemente el servidor no se encuentre funcionando correctamente.");
            e.printStackTrace();
            Log.d("ERROR", e.getMessage());
        }

    }

}
