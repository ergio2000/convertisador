package pe.pucp.convertisador;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Configurar extends AppCompatActivity {

    ArrayList dataModels;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar);
        Log.wtf("Configurar","OnCreate");

        //establece listener de evento onclick de boton
        final Button btn = findViewById(R.id.btn_act_tc);
        btn.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        Log.wtf("Configurar","OnClickBoton Actualizar Tipo Cambio");
                        //lee tipo de cambio --webservice
                        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        String url = getString(R.string.url_webservice);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, url, null,  new Response.Listener<JSONObject>()
                                {
                                    @Override
                                    public void onResponse(JSONObject response)
                                    {

                                        //lee response y crea lista de tipocambio
                                        List<TipoCambio> mtcs= new ArrayList<TipoCambio>();
                                        TipoCambio mtc;
                                        String key="";
                                        double valor=0.0;
                                        try {
                                            JSONObject tasas = response.getJSONObject("rates");
                                            //Log.wtf("tasas", tasas.toString());
                                            //crea lista de tipocambio
                                            Iterator<String> iter = tasas.keys();
                                            while (iter.hasNext()) {
                                                //obtiene moneda y tipo de cambio
                                                key = iter.next();
                                                valor = tasas.getDouble(key);
                                                //crea objeto de respuesta
                                                mtc = new TipoCambio(key,valor);
                                                //adiciona a respuesta
                                                mtcs.add(mtc);
                                                //Log.wtf("keys", key);
                                            }
                                            Log.wtf("cuenta TipoCambio WebService mtcs Configurar", String.valueOf( mtcs.size()));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        //actualiza equivalencias en base datos
                                        Cambiador mcamb= new Cambiador();
                                        mcamb.ActualizaTC(mtcs, getApplicationContext());

                                        //libera
                                        mcamb=null;

                                        //actualiza layout
                                        ActualizaLista();

                                    }

                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("VolleyOnError ActTipCam", error.toString());
                                    }
                                });

                        requestQueue.add(jsonObjectRequest);

                        Log.wtf("onclick ActTipCam", "fin");
                    }
                }
        );

    }

    @Override
    protected void onStart() {
        super.onStart();

        //actualiza lista
        ActualizaLista();
    }

    public void ActualizaLista()
    {
        List<TipoCambio> mtcs= new ArrayList<TipoCambio>();
        TipoCambio mtc;
        final ListView lv = findViewById(R.id.lvtc);

        Log.wtf("ActualizaLista","Inicio");

        //lee los tipos de cambio de la base de datos
        Cambiador mcamb= new Cambiador();
        mtcs=mcamb.LeeMonedas(getApplicationContext(),-1);
        Log.wtf("cuenta tipos de cambio todos ver=-1 de base de datos mtcs ", String.valueOf( mtcs.size()));

        //borra elementos de la lista
        lv.setAdapter(null);

        //crea lista auxiliar
        dataModels = new ArrayList(mtcs);
        /*
        dataModels.add(new TipoCambio("Apple Pie", 1.2));
        dataModels.add(new TipoCambio("Banana Bread", 2.5));
        dataModels.add(new TipoCambio("Cupcake",10.0));
        */
        //adiciona tipo de cambio a la lista
        adapter = new CustomAdapter(dataModels, getApplicationContext());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                TipoCambio dataModel= (TipoCambio) dataModels.get(position);
                if(dataModel.Ver==0){dataModel.Ver=1;}
                else{dataModel.Ver=0;}
                //actualiza bd
                Log.wtf("actualiza","==>"+ dataModel.Moneda+"+"+dataModel.Ver);
                Cambiador cmbd= new Cambiador();
                cmbd.ActualizaVer(dataModel,getApplicationContext());
                //libera
                cmbd=null;
                adapter.notifyDataSetChanged();
            }
        });
        //libera
        mcamb=null;
        Log.wtf("ActualizaLista","fin");
    }

}