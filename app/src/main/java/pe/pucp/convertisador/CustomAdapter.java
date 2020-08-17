package pe.pucp.convertisador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {
    private ArrayList dataSet;
    Context mContext;
    // View lookup cache
    private static class ViewHolder {
        TextView txtMoneda;
        TextView txtEquivalencia;
        CheckBox checkBox;
    }
    public CustomAdapter(ArrayList data, Context context) {
        super(context, R.layout.listview_row, data);
        this.dataSet = data;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return dataSet.size();
    }
    @Override
    public TipoCambio getItem(int position) {
        return (TipoCambio) dataSet.get(position);
    }
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_row, parent, false);
            viewHolder.txtMoneda = (TextView) convertView.findViewById(R.id.txtMoneda);
            viewHolder.txtEquivalencia = (TextView) convertView.findViewById(R.id.txtEquivalencia);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        TipoCambio item = getItem(position);
        viewHolder.txtMoneda.setText(item.Moneda);
        viewHolder.txtEquivalencia.setText(String.valueOf(item.Equivalencia) );
        //viewHolder.txtEquivalencia.setText(String.valueOf(item.Ver) ); //auxiliar depuracion
        boolean b=false;
        if(item.Ver==1){b=true;}
        viewHolder.checkBox.setChecked(b);
        return result;
    }
}