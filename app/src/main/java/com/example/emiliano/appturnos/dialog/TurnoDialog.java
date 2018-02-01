package com.example.emiliano.appturnos.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emiliano.appturnos.R;

/**
 * Created by emi88 on 1/31/18.
 */

public class TurnoDialog extends DialogFragment {

    private Button btnEditar;
    private Button btnBorrar;
    private Button btnCerrar;
    private TextView tvDialogTituloValor;
    private TextView tvDialogDescValor;
    private TextView tvDialogLatValor;
    private TextView tvDialogLngValor;
    private View view;

    private boolean borrar;
    public final static int OP_EDITAR = 1;
    public final static int OP_BORRAR = 2;
    private int op;


    public TurnoDialog() {
        this.borrar = false;
        this.op = 0;
    }



    public void initUI(){


//        this.btnEditar = view.findViewById(R.id.btnDialogEditar);
//        this.btnBorrar = view.findViewById(R.id.btnDialogBorrar);
         this.btnCerrar = (Button) view.findViewById(R.id.btnDialogCerrar);
//        this.tvDialogTituloValor = view.findViewById(R.id.tvDialogTituloValor);
//        this.tvDialogDescValor = view.findViewById(R.id.tvDialogDescValor);
//        this.tvDialogLatValor = view.findViewById(R.id.tvDialogLatValor);
//        this.tvDialogLngValor = view.findViewById(R.id.tvDialogLngValor);
//
//        //rellenar elementos:
//        this.tvDialogTituloValor.setText( ubicacion.getTitulo() );
//        this.tvDialogDescValor.setText( ubicacion.getDescripcion() );
//        this.tvDialogLatValor.setText( Double.toString( ubicacion.getLatitud() ) );
//        this.tvDialogLngValor.setText( Double.toString( ubicacion.getLongitud() ) );

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        this.view = inflater.inflate(R.layout.turno_dialog, null);
        builder.setView( view );

        //una vez creado el layout, buscar los componentes visuales y setearles el contenido:
        this.initUI();

        //action para Editar:
//        this.btnEditar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                op = OP_EDITAR;
//                getDialog().dismiss();
//            }
//        });

        //accion para borrar:
//        this.btnBorrar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if( borrar == false ){
//                    btnBorrar.setText("CONFIRMAR");
//                    String msg = "Presione nuevamente para confirmar el borrado";
//                    Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                    borrar = true;
//                }else{
//                    op = OP_BORRAR;
//                    getDialog().dismiss();
//                }
//            }
//        });
//
        //accion para cerrar:
        this.btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().cancel();
            }
        });

        return builder.create();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {

        //Realizar alguna accion:
//        Activity activity = getActivity();
//        if(activity instanceof DialogInterface.OnDismissListener){
//            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
//        }
//        //context.onDismissUbicacionDialog();
//        super.onDismiss(dialog);

    }

    public int getOp() {
        return op;
    }

}
