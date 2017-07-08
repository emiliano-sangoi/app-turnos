package com.example.emiliano.appturnos;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by emiliano on 07/07/17.
 */

public class OnFinishCallback {

    private Context context;
    private Bundle data;

    public OnFinishCallback(Context context) {

        this.context = context;
        this.data = new Bundle();

    }

    /**
     * Actualiza la vista
     */
    public void successAction(){
        return;
    }

    /**
     * Se debe ejecutar en caso de que haya algun error
     */
    public void errorAction(){
        return;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }
}
