package com.example.emiliano.appturnos.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.emiliano.appturnos.R;

public class AcercaDeDialog extends DialogFragment{

    private Button btnCerrar;
    private TextView tvRepo;
    private View view;


    public void initUI(){

        this.btnCerrar = (Button) view.findViewById(R.id.btnDialogCerrar);
        this.tvRepo = (TextView) view.findViewById(R.id.tvRepo);
        this.tvRepo.setMovementMethod(LinkMovementMethod.getInstance());
        this.tvRepo.setClickable(true);
        String text = getActivity().getResources().getString(R.string.dialog_acerca_de_repo_link);
        this.tvRepo.setText(Html.fromHtml(text));

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        this.view = inflater.inflate(R.layout.acerca_de_dialog, null);
        builder.setView( view );

        initUI();

        //accion para cerrar:
        this.btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        return builder.create();

    }



}
