package pe.pucp.convertisador;

//manejo de listas
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cambiador {
    //propiedades

    //constructor

    //metodos

    //obtiene lista de tipos de cambio de web service
    public List<TipoCambio> ObtieneTC(Context pContexto)
    {
        final List<TipoCambio> mr; //declarada final para ser acceible dentor de lcases internas
        TipoCambio tc;

        mr= new ArrayList<TipoCambio>();

        //devuelve dummies
        //tc= new TipoCambio( "USD", 3.54);
        //mr.add(tc);
        //tc= new TipoCambio( "EUR", 4.8);
        //mr.add(tc);

        final RequestQueue requestQueue = Volley.newRequestQueue(pContexto);

        //String url = "https://openexchangerates.org/api/latest.json?app_id=9cd53ca94fca45d6b3d1c50b60c8463f&symbols=PEN,EUR,COP,CLP";
        String url = "https://openexchangerates.org/api/latest.json?app_id=9cd53ca94fca45d6b3d1c50b60c8463f";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null,  new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        TipoCambio mtc;
                        String key="";
                        double valor=0.0;
                        try {
                            //tasa = response.getJSONObject("rates").getDouble("PEN");
                            //editor.putString("PEN", tasa.toString());
                            JSONObject tasas = response.getJSONObject("rates");
                            Iterator<String> iter = tasas.keys();
                            while (iter.hasNext()) {
                                //obtiene moneda y tipo de cambio
                                key = iter.next();
                                valor = tasas.getDouble(key);
                                //crea objeto de respuesta
                                mtc = new TipoCambio(key,valor);
                                //adiciona a respuesta
                                mr.add(mtc);
                                //Log.d("keys", key);
                            }

                        } catch (JSONException e) {
                            //e.printStackTrace();
                            Log.e("JSON", e.getMessage() );
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JSON", error.toString());
                    }
                });
        requestQueue.add(jsonObjectRequest);
        return(mr);
    }


    public void ActualizaTC(List<TipoCambio> ptcs)
    {

    }

    //lee lista de monedas de base de datos
    public List<TipoCambio> LeeMonedas()
    {
        List<TipoCambio> mr;
        TipoCambio tc;

        mr= new ArrayList<TipoCambio>();

        //devuelve dummies
        tc= new TipoCambio( "USD", 3.54);
        tc.Ver=1;
        mr.add(tc);
        tc= new TipoCambio( "EUR", 4.8);
        tc.Ver=1;
        mr.add(tc);
        tc= new TipoCambio( "YEN", 2.5);
        tc.Ver=0;
        mr.add(tc);

        return(mr);
    }

}
