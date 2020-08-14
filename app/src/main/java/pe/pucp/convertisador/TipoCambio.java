package pe.pucp.convertisador;

public class TipoCambio {
    //propiedades
    public String Moneda;
    public double Equivalencia;
    public int Ver; //1:ver , 0:ocultar

    public String TextoSpinner;
    public String TextoOrigen;
    public String TextoDestino;

    //constructor
    TipoCambio()//constructor general
    {
        Moneda="";
        Equivalencia=0.0;
        Ver=0;
        TextoSpinner="";
        TextoOrigen="";
        TextoDestino="";
    }
    TipoCambio(String pMoneda, double pEquivalencia)//constructor para monedas de web service
    {
        Moneda=pMoneda;
        Equivalencia=pEquivalencia;
        Ver=0;
        TextoSpinner="";
        TextoOrigen="";
        TextoDestino="";
    }
    TipoCambio(String pMoneda, String pTextoSpinner, String pTextoOrigen, String pTextoDestino)//constructor para spinner
    {
        Moneda=pMoneda;
        Equivalencia=0.0;
        Ver=0;
        TextoSpinner=pTextoSpinner;
        TextoOrigen=pTextoOrigen;
        TextoDestino=pTextoDestino;
    }

    //metodos

    @Override
    public String toString() {
        return TextoSpinner;
    }
}
