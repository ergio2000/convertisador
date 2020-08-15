package pe.pucp.convertisador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //auxiliar de visualizacion de spinner
    //private Spinner spn;
    List<TipoCambio> mlstspn= new ArrayList<TipoCambio>(){};//lista de monedas para spinner

    //auxiliar de base de datos
    SQLiteDatabase db;

    //oncreate se encarga de inicializar la base de datos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //verifica/crea base de datos y tabla
        final DbHandler dbHandler = new DbHandler(getApplicationContext());
        db = dbHandler.getWritableDatabase();
        db.close();

        //adiciona listener de spinner
        final Spinner spn = findViewById(R.id.spn_unidades);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                final TextInputLayout et_unidades1 = findViewById(R.id.tf_unidades1);
                final TextInputLayout et_unidades2 = findViewById(R.id.tf_unidades2);

                try{
                    TipoCambio mtc3 ; //auxiliar
                    mtc3=(TipoCambio)parent.getItemAtPosition(pos);//objeto en la posicion, incluye conversion de tipo
                    //actualiza texto origen
                    et_unidades1.setHint( mtc3.TextoOrigen );
                    //actualiza texto destino
                    et_unidades2.setHint( mtc3.TextoDestino );
                }
                catch (Exception e)
                {
                    Log.wtf("Error spinner item selected",e.toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //adiciona evento click al boton de conversion
        final Button btn_convertir = findViewById(R.id.btn_convertir);
        btn_convertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //obtiene item seleccionado en spinner
                    //si es celsius o fahrenheit calcula especial
                    //para los demas casos usa campo equivalencia

                    final Spinner spn = findViewById(R.id.spn_unidades);
                    final TextInputLayout et_unidades1 = findViewById(R.id.tf_unidades1);
                    final TextInputLayout et_unidades2 = findViewById(R.id.tf_unidades2);

                    TipoCambio mtc3 ; //auxiliar
                    mtc3=(TipoCambio)spn.getSelectedItem();//objeto seleccionado
                    double resultado = 0.0; //auxiliar que contiene el resultado

                    //obtiene numero ingresado por el usuario
                    double num = Double.parseDouble(et_unidades1.getEditText().getText().toString());
                    //evaluacion especifica y general
                    switch( mtc3.Moneda)
                    {
                        case "CELSIUS":
                            //f=9/5 x c + 32
                            resultado = (9.0/5.0)*num +32.0;
                            break;
                        case "FAHRENHEIT":
                            //c = (f-32) x 5/9
                            resultado = (num -32.0)*(5.0/9.0);
                            break;
                        default:
                            resultado = num * mtc3.Equivalencia;
                            break;
                    }

                    //asigna resultado
                    et_unidades2.getEditText().setText(Double.toString(resultado));

                } catch (NumberFormatException e) {
                    Snackbar.make(findViewById(android.R.id.content), "Error en la entrada de datos", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        //deshabilita boton
        btn_convertir.setEnabled(false);

    }

    //onstart se encarga de actualizar todos los tipos de cambio visualizados en la base de datos
    // leer solo los tipos de cmabio que el suario desea visualizar
    //y refrescar el spinner
    @Override
    protected void onStart()
    {
        super.onStart();

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //String url = "https://openexchangerates.org/api/latest.json?app_id=9cd53ca94fca45d6b3d1c50b60c8463f&symbols=PEN,EUR,COP,CLP";
        String url = "https://openexchangerates.org/api/latest.json?app_id=9cd53ca94fca45d6b3d1c50b60c8463f";

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
                            Log.wtf("cuenta mr in", String.valueOf( mtcs.size()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //actualiza equivalencias en base datos
                        Cambiador mcamb= new Cambiador();
                        mcamb.ActualizaTC(mtcs, getApplicationContext());

                        //lee lista de monedas de bd
                        mtcs=mcamb.LeeMonedas(getApplicationContext());
                        Log.wtf("cuenta mtcs ", String.valueOf( mtcs.size()));


                        //actualiza spinner
                        final TextInputLayout et_unidades1 = findViewById(R.id.tf_unidades1);
                        final TextInputLayout et_unidades2 = findViewById(R.id.tf_unidades2);
                        final Spinner spn = findViewById(R.id.spn_unidades);

                        //inicializa lista de spinner
                        List<TipoCambio> mlstspn= new ArrayList<TipoCambio>(){};
                        //adiciona conversiones pre-definidas en parejas
                        mtc= new TipoCambio("CELSIUS","C --> F","Temperatura en Celsius","Temperatura en Fahrenheit");
                        mtc.Equivalencia=0;
                        mlstspn.add(mtc);
                        mtc= new TipoCambio("FAHRENHEIT","F --> C","Temperatura en Fahrenheit","Temperatura en Celsius");
                        mtc.Equivalencia=0;
                        mlstspn.add(mtc);
                        mtc= new TipoCambio("Kg","Kg --> Lb","Peso en kilos","Peso en libras");
                        mtc.Equivalencia=2.20462;
                        mlstspn.add(mtc);
                        mtc= new TipoCambio("Lb","Lb --> Kg","Peso en libras","Peso en kilos");
                        mtc.Equivalencia=1.0/2.20462;
                        mlstspn.add(mtc);
                        mtc= new TipoCambio("m","m --> foot","Distancia en metros","Distancia en pies");
                        mtc.Equivalencia=3.28084;
                        mlstspn.add(mtc);
                        mtc= new TipoCambio("foot","foot --> m","Distancia en pies","Distancia en metros");
                        mtc.Equivalencia=1.0/3.28084;
                        mlstspn.add(mtc);
                        //adiciona monedas
                        for(TipoCambio mtc2:mtcs)
                        {
                            //conversion de monedas directa
                            mtc= new TipoCambio(mtc2.Moneda,"Soles --> "+mtc2.Moneda,"Moneda en Soles","Moneda en " + mtc2.Moneda);
                            mtc.Equivalencia = 1.0/mtc2.Equivalencia;
                            mlstspn.add(mtc);
                            //conversion de monedas inversa
                            mtc= new TipoCambio("Sol",mtc2.Moneda+" --> Soles","Moneda en "+mtc2.Moneda,"Moneda en Soles" );
                            mtc.Equivalencia = mtc2.Equivalencia;
                            mlstspn.add(mtc);
                        }

                        //establece lista en spinner
                        ArrayAdapter<TipoCambio> spnadap =
                                new ArrayAdapter<TipoCambio>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, mlstspn);
                        spnadap.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                        spn.setAdapter(spnadap);

                        //selecciona 1er elemento
                        spn.setSelection(0);

                        //habilita boton de conversion
                        final Button btn_convertir = findViewById(R.id.btn_convertir);
                        btn_convertir.setEnabled(true);

                        //libera
                        mcamb=null;
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volley onerror", error.toString());
                    }
                });

        requestQueue.add(jsonObjectRequest);

        Log.wtf("onstart", "fin");
    }

}