package com.example.emiliano.appturnos.validaciones;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by emi88 on 10/29/17.
 */

public class PasswordWatcher implements TextWatcher {

    private TextView component;


    public PasswordWatcher(EditText element) {
        this.component = element;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        //este es el texto que esta introduciendo el usuario:
        String txt = component.getText().toString();
        int n = txt.length();

        if(n != 0 && n > 4){
            component.setError("El campo Contraseña no puede quedar vacio y debe poseer a lo sumo 4 caracteres.");
        }else{
            component.setError(null);
        }

    }


}
