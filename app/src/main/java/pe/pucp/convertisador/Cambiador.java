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

    // se utiliza el metodo de leer solo las monedas que el usuario desea visualizar (se espera pocas)
    // luego se reescriben todas,estableciendo la opcion de visualizar
    public void ActualizaTC(List<TipoCambio> ptcs, Context pContexto)
    {
        SQLiteDatabase db;
        TipoCambio mtc;
        TipoCambio mtc2;
        ContentValues values = new ContentValues();
        List<TipoCambio> monedasSi;
        final DbHandler dbHandler = new DbHandler( pContexto);
        db = dbHandler.getWritableDatabase();

        try {
            //lee monedas con visualizar=1
            monedasSi=LeeMonedas(pContexto,1);
            Log.d("ActualizaTC monedasSi:", String.valueOf(monedasSi.size() ));
            ListIterator<TipoCambio> limsi = monedasSi.listIterator(); //iterador para busqueda

            //borra todas la monedas
            db.delete("equivalencias", null, null);

            int i = 0;//auxiliar de indice
            //adiciona todas las nuevas monedas
            // y actualiza monedas con visualizar = 1
            final ListIterator<TipoCambio> li = ptcs.listIterator();
            while (li.hasNext()) {
                //lee objeto
                mtc = li.next();
                //crea lista de valores
                values = new ContentValues();
                values.put("id", i);
                values.put("moneda", mtc.Moneda);
                values.put("equivalencia", mtc.Equivalencia);

                //actualiza filtro
                int mVer=0;
                //busca si moneda esta en lista de visualizar
                limsi = monedasSi.listIterator();
                while(limsi.hasNext())
                {
                    //lee objeto monedas si
                    mtc2=limsi.next();
                    //Log.d("ActualizaTC iters", mtc.Moneda+"-"+mtc2.Moneda);
                    //si existe actualiza ver y sale
                    if(mtc.Moneda.equals(mtc2.Moneda))
                    {
                        mVer=1;
                        //Log.d("ActualizaTC monSi=1:", mtc.Moneda);
                        break;
                    }
                }
                //auxiliar test
                //if(i<=10){mVer=1;}

                //actualiza ver
                values.put("ver", mVer);

                //inserta en base de datos
                db.insertOrThrow("equivalencias", null, values);
                //Log.d("insertando", mtc.Moneda);

                //incrementa id
                i=i+1;
            }

            //libera
            db.close();

        }catch(Exception e)
        {
            System.out.println("Error Cambiador.Actualiza " + e.getMessage());
        }


    }

    //lee lista de monedas de base de datos
    //pfiltro segun campo ver  0: no ver   1:ver   -1:todos
    public List<TipoCambio> LeeMonedas(Context pContexto, int pFiltro)
    {
        List<TipoCambio> mr;
        TipoCambio tc;

        mr= new ArrayList<TipoCambio>();

        //prueba: devuelve dummies
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
            //campos de consulta
            Cursor c;
            String[] campos = new String[] {"moneda", "equivalencia","ver"};
            String[] args = new String[] {"1"}; //filtro por defecto
            String where="ver=?";
            String orderby="ver desc, moneda asc";

            //pregunta si existe filtro
            if(pFiltro==-1)
            {
                //sin filtro: todos
                c = db.query("EQUIVALENCIAS", campos, null, null, null, null, orderby);
            }
            else
            {
                //con filtro
                args[0]= String.valueOf(pFiltro);
                c = db.query("EQUIVALENCIAS", campos, where, args, null, null, orderby);
            }

            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    String mimoneda= c.getString(0);
                    int miequivalencia = c.getInt(1);
                    int miver = c.getInt(2);

                    //crea objeto
                    tc= new TipoCambio( mimoneda, miequivalencia);
                    tc.Ver=miver;
                    //adiciona a coleccion
                    mr.add(tc);

                } while(c.moveToNext());
            }

            //libera
            c.close();

            //actualiza monedas con visualizar = 1
        }catch(Exception e)
        {
            System.out.println("Error LeeMonedas" + e.getMessage());
        }

        db.close();

        return(mr);
    }

    public void creabd(Context pContexto)
    {
        SQLiteDatabase db;
        final DbHandler dbHandler = new DbHandler( pContexto);
        db = dbHandler.getWritableDatabase();
        db.close();
    }

    //actualiza el campo ver de la momeda seleccionada
    public void ActualizaVer(TipoCambio pTC, Context pContexto)
    {
        SQLiteDatabase db;
        final DbHandler dbHandler = new DbHandler( pContexto);
        db = dbHandler.getWritableDatabase();

        try {
            //campos de actualizacion
            Log.d("monedaActualiza",pTC.Moneda+"-"+pTC.Ver);
            String s="update equivalencias set ver="+ String.valueOf( pTC.Ver ) +" where moneda='"+pTC.Moneda+"'";
            Log.d("monedaActualiza sql",s);
            db.execSQL(s);

        }catch(Exception e)
        {
            System.out.println("Error ActualizaVer" + e.getMessage());
        }

        db.close();

    }


}
