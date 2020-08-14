package pe.pucp.convertisador;

//manejo de listas
import java.util.ArrayList;
import java.util.List;

public class Cambiador {
    //propiedades

    //constructor

    //metodos

    //obtiene lista de tipos de cambio de web service
    public List<TipoCambio> ObtieneTC()
    {
        List<TipoCambio> mr;
        TipoCambio tc;

        mr= new ArrayList<TipoCambio>();

        //devuelve dummies
        tc= new TipoCambio( "USD", 3.54);
        mr.add(tc);
        tc= new TipoCambio( "EUR", 4.8);
        mr.add(tc);

        return(mr);
    }

    //actualiza equivalencias en base de datos
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
