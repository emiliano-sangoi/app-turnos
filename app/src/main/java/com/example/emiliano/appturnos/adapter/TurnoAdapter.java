package apps.sangoi.emiliano.app_pronosticos.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import apps.sangoi.emiliano.app_pronosticos.R;
import apps.sangoi.emiliano.app_pronosticos.database.Ubicacion;

/**
 * Created by emi88 on 10/28/17.
 */

public class UbicacionAdapter extends ArrayAdapter<Ubicacion> {

    private ArrayList<Ubicacion>ubicaciones;

    public UbicacionAdapter(Context context, ArrayList<Ubicacion> ubicaciones) {
        super(context, 0, ubicaciones);
        this.ubicaciones = ubicaciones;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Ubicacion ubicacion = getItem(position);

        // si no se reusa la vista, inflar:
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ubicacion, parent, false);
        }

        // actualizar elementos visuales:
        TextView tvTitulo = (TextView) convertView.findViewById(R.id.tvTitulo);
        tvTitulo.setText( ubicacion.getTitulo() );

        TextView tvDescripcion = (TextView) convertView.findViewById(R.id.tvDescripcion);
        tvDescripcion.setText( ubicacion.getDescripcion() );

        return convertView;

    }

    @Override
    public int getCount() {
        return this.ubicaciones.size();
    }
}
