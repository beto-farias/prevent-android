package mx.com.dgom.sercco.android.prevent.to;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by beto on 10/12/15.
 */
public class DelitoTO {

    private long id_num_delito;
    private long id_evento;
    private int id_tipo_delito;
    private int id_tipo_sub_delito;
    private String txt_resumen;
    private long fch_delito;
    private int num_victimas;
    private int num_delincuentes;
    private int num_likes;
    private boolean b_confirmado;
    private double num_latitud;
    private double num_longitud;
    private DireccionTO direccion = new DireccionTO();
    //private ArrayList<String> imageData = new ArrayList<String>();
    private String txt_descripcion_lugar;
    private DelitoMultimediaTO[] multimedia;
    private String txt_direccion;


    public String getTxt_direccion() {
        return txt_direccion;
    }

    public void setTxt_direccion(String txt_direccion) {
        this.txt_direccion = txt_direccion;
    }

    public DelitoMultimediaTO[] getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(DelitoMultimediaTO[] multimedia) {
        this.multimedia = multimedia;
    }

    public String getTxt_descripcion_lugar() {
        return txt_descripcion_lugar;
    }

    public void setTxt_descripcion_lugar(String txt_descripcion_lugar) {
        this.txt_descripcion_lugar = txt_descripcion_lugar;
    }

    public DireccionTO getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionTO direccion) {
        this.direccion = direccion;
    }

    public int getId_tipo_sub_delito() {
        return id_tipo_sub_delito;
    }

    public void setId_tipo_sub_delito(int id_tipo_sub_delito) {
        this.id_tipo_sub_delito = id_tipo_sub_delito;
    }

    public LatLng getLatLng() {

        return new LatLng(num_latitud, num_longitud);
    }

    public double getNum_latitud() {
        return num_latitud;
    }

    public void setNum_latitud(double num_latitud) {
        this.num_latitud = num_latitud;
    }

    public double getNum_longitud() {
        return num_longitud;
    }

    public void setNum_longitud(double num_longitud) {
        this.num_longitud = num_longitud;
    }

    public long getId_num_delito() {
        return id_num_delito;
    }

    public void setId_num_delito(long id_num_delito) {
        this.id_num_delito = id_num_delito;
    }

    public long getId_evento() {
        return id_evento;
    }

    public void setId_evento(long id_evento) {
        this.id_evento = id_evento;
    }

    public int getId_tipo_delito() {
        return id_tipo_delito;
    }

    public void setId_tipo_delito(int id_tipo_delito) {
        this.id_tipo_delito = id_tipo_delito;
    }

    public String getTxt_resumen() {
        return txt_resumen;
    }

    public void setTxt_resumen(String txt_resumen) {
        this.txt_resumen = txt_resumen;
    }

    public long getFch_delito() {
        return fch_delito;
    }

    public void setFch_delito(long fch_delito) {
        this.fch_delito = fch_delito;
    }

    public int getNum_victimas() {
        return num_victimas;
    }

    public void setNum_victimas(int num_victimas) {
        if (num_victimas < 0)
            return;
        this.num_victimas = num_victimas;
    }

    public int getNum_delincuentes() {
        return num_delincuentes;
    }

    public void setNum_delincuentes(int num_delincuentes) {
        if (num_delincuentes < 0)
            return;
        this.num_delincuentes = num_delincuentes;
    }

    public int getNum_likes() {
        return num_likes;
    }

    public void setNum_likes(int num_likes) {
        this.num_likes = num_likes;
    }

    public boolean isB_confirmado() {
        return b_confirmado;
    }

    public void setB_confirmado(boolean b_confirmado) {
        this.b_confirmado = b_confirmado;
    }

/*
    public void addImage(String b64) {
        imageData.add(b64);
    }

    public String getImageAt(int index) {
        return imageData.get(index);
    }

    public int imageDataSize() {
        return imageData.size();
    }

    public void removeImageAt(int index) {
        imageData.remove(index);
    }

    */
}
