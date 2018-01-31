package com.example.emiliano.appturnos.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.emiliano.appturnos.backend.Afiliacion;
import com.example.emiliano.appturnos.event.OnFinishCallback;
import com.example.emiliano.appturnos.R;
import com.example.emiliano.appturnos.backend.Turno;

public class Wizard1Activity extends WizardActivity {

    // Atributos
    private Afiliacion[] afiliaciones;
    private Afiliacion afiliacionSel;

    // UI
    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;
    private CheckBox cbPagarConsulta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard1);

        this.turno = new Turno();
        this.turno.setUsuario(this.usuario);

        this.wAnterior = null;
        this.wSiguiente = Wizard2Activity.class;
        this.requestCodeSig = REQUEST_W2;
        this.pasoActual = 1;

        initUI();

        //obtener las afiliaciones desde el backend
        cargarAfiliaciones();
    }

    @Override
    public void initUI() {

        super.initUI();

        this.pbProgreso.setVisibility(View.VISIBLE);
        this.spinner = (Spinner) findViewById(R.id.spinnerAfiliaciones);
        this.cbPagarConsulta = (CheckBox) findViewById(R.id.cbPagarConsulta);

        //Eventos
        this.cbPagarConsulta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinner.setEnabled(false);
                    //el usuario decide pagar la consulta:
                    afiliacionSel = null;

                } else {
                    spinner.setEnabled(true);
                    afiliacionSel = (Afiliacion) spinner.getSelectedItem();
                }
            }

        });

    }

    @Override
    public void siguiente() {
        turno.setAfiliacion( afiliacionSel );
        super.siguiente();
    }

    /**
     * Carga las OS desde el backend y actualiza el spinner.
     */
    public void cargarAfiliaciones() {

        OnFinishCallback callback = new OnFinishCallback(this) {

            /**
             * Actualiza la vista. En data se encuentra el objeto con los datos, normalmente traidos del modelo.
             *
             * @param data
             */
            @Override
            public void successAction(Object[] data) {

                //guardar afiliaciones
                afiliaciones = (Afiliacion[]) data;

                if(afiliaciones.length > 0){
                    spinner.setVisibility(Spinner.VISIBLE);
                    spinnerAdapter = new ArrayAdapter(getContext(), R.layout.my_spinner_layout, afiliaciones);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                    spinner.setAdapter(spinnerAdapter);

                    if(turno.getAfiliacion() != null){
                        Integer pos = getPosEspecialidadEnSpinner( turno.getAfiliacion().getIdOs() );
                        if(pos != null){
                            spinner.setSelection( pos );
                        }else{
                            turno.setAfiliacion( null );
                        }
                    }


                }else{
                    spinner.setVisibility(Spinner.GONE);
                    mostrarToast("Ud. no tiene afiliaciones con ninguna obra social.");
                }

                //Ocultar la barra de progreso:
                pbProgreso.setVisibility(View.GONE);

            }
        };

        this.apiTurnos.getAfiliaciones(callback, usuario.getIdPaciente());

    }


    public Integer getPosEspecialidadEnSpinner(int idOs){
        Integer res = null;
        for(int i=0; i<afiliaciones.length; i++){
            if( afiliaciones[i].getIdOs() == idOs ){
                res = spinnerAdapter.getPosition( afiliaciones[i] );
                break;
            }
        }
        return res;
    }
}
