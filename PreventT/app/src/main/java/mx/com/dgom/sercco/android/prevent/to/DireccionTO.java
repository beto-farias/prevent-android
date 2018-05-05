package mx.com.dgom.sercco.android.prevent.to;

import android.location.Address;

/**
 * Created by beto on 10/29/15.
 */
public class DireccionTO {

    private String txtPais ="";
    private String txtEstado ="";
    private String txtMunicipio ="";
    private String txtColonia ="";
    private String txtCalle ="";
    private String txtNumero ="";
    private String txtNumeroInterior ="";
    private String numLatitud ="0";
    private String numLongitud ="0";


    public void setAddress(Address address){
        setTxtPais(address.getCountryName());
        setTxtCalle(address.getThoroughfare());
        setTxtColonia(address.getSubLocality());
        setTxtEstado(address.getAdminArea());
        setTxtMunicipio(address.getLocality());
        setTxtNumero("N/A");
        setTxtNumeroInterior("N/A");
        setNumLatitud("" + address.getLatitude());
        setNumLongitud("" + address.getLongitude());
    }

    public String getTxtPais() {
        return txtPais;
    }

    public void setTxtPais(String txtPais) {
        this.txtPais = txtPais;
    }

    public String getTxtEstado() {
        return txtEstado;
    }

    public void setTxtEstado(String txtEstado) {
        this.txtEstado = txtEstado;
    }

    public String getTxtMunicipio() {
        return txtMunicipio;
    }

    public void setTxtMunicipio(String txtMunicipio) {
        this.txtMunicipio = txtMunicipio;
    }

    public String getTxtColonia() {
        return txtColonia;
    }

    public void setTxtColonia(String txtColonia) {
        this.txtColonia = txtColonia;
    }

    public String getTxtCalle() {
        return txtCalle;
    }

    public void setTxtCalle(String txtCalle) {
        this.txtCalle = txtCalle;
    }

    public String getTxtNumero() {
        return txtNumero;
    }

    public void setTxtNumero(String txtNumero) {
        this.txtNumero = txtNumero;
    }

    public String getTxtNumeroInterior() {
        return txtNumeroInterior;
    }

    public void setTxtNumeroInterior(String txtNumeroInterior) {
        this.txtNumeroInterior = txtNumeroInterior;
    }

    public String getNumLatitud() {
        return numLatitud;
    }

    public void setNumLatitud(String numLatitud) {
        this.numLatitud = numLatitud;
    }

    public String getNumLongitud() {
        return numLongitud;
    }

    public void setNumLongitud(String numLongitud) {
        this.numLongitud = numLongitud;
    }

    public String getDireccion() {
        return txtCalle + " " + txtNumero + ", col. " + txtColonia + " " + txtMunicipio + ", edo " + txtEstado ;
    }
}
