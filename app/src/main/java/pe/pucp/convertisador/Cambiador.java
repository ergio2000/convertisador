package pe.pucp.convertisador;

//manejo de listas
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Cambiador {
    //propiedades

    //constructor

    //metodos
public void ccc(Context pContexto)
{
    RequestQueue requestQueue = Volley.newRequestQueue(pContexto);
    String url = "https://openexchangerates.org/api/latest.json?app_id=9cd53ca94fca45d6b3d1c50b60c8463f&symbols=PEN,EUR,COP,CLP";

    RequestFuture<JSONObject> future = RequestFuture.newFuture();
    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, future, future);

    // If you want to be able to cancel the request:
    future.setRequest(requestQueue.add(request));

    // Otherwise:
    requestQueue.add(request);

    try {
        JSONObject response = future.get(10,TimeUnit.SECONDS);
        // do something with response
    } catch (InterruptedException e) {
        // handle the error
    } catch (ExecutionException e) {
        // handle the error
    } catch (TimeoutException e) {
        //e.printStackTrace();
        Log.wtf("volley timeout exception", e.toString());
    }

}

    //obtiene lista de tipos de cambio de web service
    public List<TipoCambio> ObtieneTC(Context pContexto)
    {
        final List<TipoCambio> mr= new ArrayList<TipoCambio>();
        //final List<TipoCambio> mr; //declarada final para ser acceible dentor de lcases internas
        TipoCambio tc;

        //mr= new ArrayList<TipoCambio>();

        //devuelve dummies
        //tc= new TipoCambio( "USD", 3.54);
        //mr.add(tc);
        //tc= new TipoCambio( "EUR", 4.8);
        //mr.add(tc);

        RequestQueue requestQueue = Volley.newRequestQueue(pContexto);

        String url = "https://openexchangerates.org/api/latest.json?app_id=9cd53ca94fca45d6b3d1c50b60c8463f&symbols=PEN,EUR,COP,CLP";
        //String url = "https://openexchangerates.org/api/latest.json?app_id=9cd53ca94fca45d6b3d1c50b60c8463f";
        //Log.wtf("url",url);

        JSONObject response= new JSONObject();

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        //JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), future, future);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, future, future);

        try {
            Log.wtf("puntos","5");
            requestQueue.add(request);
            Log.wtf("puntos","5b");
            response = future.get(10, TimeUnit.SECONDS); // this will block
            //response = future.get(); // this will block
            Log.wtf("puntos","6");
        } catch (InterruptedException e) {
            // exception handling
            Log.wtf("volley interrupted exception", e.toString());
        } catch (ExecutionException e) {
            // exception handling
            Log.wtf("volley execution exception", e.toString());
        } catch (TimeoutException e) {
            //e.printStackTrace();
            Log.wtf("volley timeout exception", e.toString());
        }
        //requestQueue.add(request);

        Log.wtf("puntos","7");
        TipoCambio mtc;
        String key="";
        double valor=0.0;
        try {
            //tasa = response.getJSONObject("rates").getDouble("PEN");
            //editor.putString("PEN", tasa.toString());
            JSONObject tasas = response.getJSONObject("rates");
            //Log.wtf("cuenta", String.valueOf( tasas.length() ));

            Iterator<String> iter = tasas.keys();
            while (iter.hasNext()) {
                //obtiene moneda y tipo de cambio
                key = iter.next();
                valor = tasas.getDouble(key);
                //crea objeto de respuesta
                mtc = new TipoCambio(key,valor);
                //adiciona a respuesta
                mr.add(mtc);
                //Log.wtf("keys", key);
            }
            Log.wtf("cuenta mr in", String.valueOf( mr.size()));

        } catch (JSONException e) {
            //e.printStackTrace();
            Log.wtf("volley json exception", e.getMessage() );
        }


        Log.wtf("cuenta mr 2", String.valueOf( mr.size()));
        return(mr);
    }

    public List<TipoCambio> ObtieneTCBAK(Context pContexto)
    {
        final List<TipoCambio> mr= new ArrayList<TipoCambio>();
        //final List<TipoCambio> mr; //declarada final para ser acceible dentor de lcases internas
        TipoCambio tc;

        //mr= new ArrayList<TipoCambio>();

        //devuelve dummies
        //tc= new TipoCambio( "USD", 3.54);
        //mr.add(tc);
        //tc= new TipoCambio( "EUR", 4.8);
        //mr.add(tc);

        final RequestQueue requestQueue = Volley.newRequestQueue(pContexto);

        //String url = "https://openexchangerates.org/api/latest.json?app_id=9cd53ca94fca45d6b3d1c50b60c8463f&symbols=PEN,EUR,COP,CLP";
        String url = "https://openexchangerates.org/api/latest.json?app_id=9cd53ca94fca45d6b3d1c50b60c8463f";
        //Log.wtf("url",url);

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
                            Log.wtf("cuenta 0", "1");
                            JSONObject tasas = response.getJSONObject("rates");
                            Log.wtf("cuenta 0", "2");

                            Iterator<String> iter = tasas.keys();
                            while (iter.hasNext()) {
                                //obtiene moneda y tipo de cambio
                                key = iter.next();
                                valor = tasas.getDouble(key);
                                //crea objeto de respuesta
                                mtc = new TipoCambio(key,valor);
                                //adiciona a respuesta
                                mr.add(mtc);
                                //Log.wtf("keys", key);
                            }
                            Log.wtf("cuenta mr in", String.valueOf( mr.size()));

                        } catch (JSONException e) {
                            //e.printStackTrace();
                            Log.wtf("JSON 1", e.getMessage() );
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("JSON 2", error.toString());
                    }
                });
        requestQueue.add(jsonObjectRequest);


        Log.wtf("cuenta mr 2", String.valueOf( mr.size()));
        return(mr);
    }
// se utiliza el metodo de leer solo las monedas que el usuario desea visualizar (se espera pocas)
    //luego se reescriben todas,etablciendo la opcion de visualizar
    public void ActualizaTC(List<TipoCambio> ptcs, Context pContexto)
    {
        SQLiteDatabase db;
        TipoCambio mtc;
        ContentValues values = new ContentValues();
        final DbHandler dbHandler = new DbHandler( pContexto);
        db = dbHandler.getWritableDatabase();

        try {
            //lee monedas con visualizar=1

            //borra todas la monedas
            db.delete("equivalencias", null, null);

            int i = 0;

            //adiciona todas las nuevas monedas
            final ListIterator<TipoCambio> li = ptcs.listIterator();
            while (li.hasNext()) {
                //lee objeto
                mtc = li.next();
                //crea lista de valores
                values = new ContentValues();
                values.put("id", i);
                values.put("moneda", mtc.Moneda);
                values.put("equivalencia", mtc.Equivalencia);
                if (i < 10) {
                    values.put("ver", 1);
                } else {
                    values.put("ver", 0);
                }

                i=i+1;
                //inserta en base de datos
                db.insertOrThrow("equivalencias", null, values);
                //Log.d("insertando", mtc.Moneda);
            }


            //actualiza monedas con visualizar = 1

            //prueba
            //values = new ContentValues();
            //i=i+1;
            //values.put("id", i);
            //values.put("moneda", "priebamon");
            //values.put("equivalencia", ptcs.size());
            //values.put("ver", 0);
            ////inserta en base de datos
            //db.insertOrThrow("equivalencias", null, values);

            //libera
            db.close();

        }catch(Exception e)
        {
            System.out.println("Error Cambiador.Actualiza " + e.getMessage());
        }


    }

    //lee lista de monedas de base de datos
    public List<TipoCambio> LeeMonedas(Context pContexto)
    {
        List<TipoCambio> mr;
        TipoCambio tc;

        mr= new ArrayList<TipoCambio>();

        //devuelve dummies
        /*
        tc= new TipoCambio( "USD", 3.54);
        tc.Ver=1;
        mr.add(tc);
        tc= new TipoCambio( "EUR", 4.8);
        tc.Ver=1;
        mr.add(tc);
        tc= new TipoCambio( "YEN", 2.5);
        tc.Ver=0;
        mr.add(tc);
        */

        SQLiteDatabase db;
        TipoCambio mtc;
        final DbHandler dbHandler = new DbHandler( pContexto);
        db = dbHandler.getReadableDatabase();

        try {
            //lee monedas con visualizar=1

            String[] campos = new String[] {"moneda", "equivalencia"};
            String[] args = new String[] {"1"};

            Cursor c = db.query("EQUIVALENCIAS", campos, "ver=?", args, null, null, null);

            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String mimoneda= c.getString(0);
                    int miequivalencia = c.getInt(1);

                    //crea objeto
                    tc= new TipoCambio( mimoneda, miequivalencia);
                    tc.Ver=1;
                    //adiciona a coleccion
                    mr.add(tc);

                } while(c.moveToNext());
            }

            //actualiza monedas con visualizar = 1
        }catch(Exception e)
        {
            System.out.println("Error " + e.getMessage());
        }

        db.close();

        return(mr);
    }

    public void creabd(Context pContexto)
    {
        SQLiteDatabase db;
        final DbHandler dbHandler = new DbHandler( pContexto);
        db = dbHandler.getReadableDatabase();
    }




}
