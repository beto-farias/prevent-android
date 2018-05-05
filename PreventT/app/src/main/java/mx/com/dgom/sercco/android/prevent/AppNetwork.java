package mx.com.dgom.sercco.android.prevent;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import mx.com.dgom.sercco.android.prevent.to.DelitoTO;
import mx.com.dgom.sercco.android.prevent.to.UsuarioTO;
import mx.com.dgom.util.io.net.Network;
import mx.com.dgom.util.io.net.NetworkException;
import mx.com.dgom.util.io.net.NoInternetException;
import mx.com.dgom.util.io.net.to.NetResponse;
import mx.com.dgom.util.str.StringsUtils;

/**
 * Created by beto on 10/12/15.
 */
public class AppNetwork {

    private static final String TAG = "AppNetwork";

    //private static final String END_POINT = "http://192.168.0.13/wwwPrevente/preventServices/";
    //private static final String END_POINT = "http://notei.com.mx/test/wwwPrevenT/preventServices/";
    private static final String END_POINT = "http://app.prevent-delito.com/preventServices/";
    private static final String END_POINT_DELITO_BY_TIPO = "getAllDelitos/";
    private static final String END_POINT_DETALLE_DELITO = "getDetallesDelito/";
    private static final String END_POINT_DELITO_TIMELINE = "getAllDelitosFull/";
    private static final String END_POINT_CREATE_USER = "saveUsuario/";
    private static final String END_POINT_LIKE_DELITO = "points/";
    private static final String END_POINT_CREATE_USER_FB = "saveUserWithFacebook/";
    private static final String END_POINT_LOGIN_USER = "loginUser/";
    private static final String END_POINT_REPORTAR_DELITO = "saveDelito/";
    private static final String END_POINT_REGISTER_DEVICE ="registerDevice/";
    private static final String END_POINT_ADD_DELITO_MULTIMEDIA ="saveMultimedia/";
    //public static final String END_PONT_DELITOS_IMAGENES = "http://notei.com.mx/test/wwwPrevenT/multimedia/delitos/";
    public static final String END_PONT_DELITOS_IMAGENES = "https://prevent-delito.com/multimedia/delitos/";


    private Gson gson = new Gson();

    private Context ctx;
    Network net;

    public AppNetwork(Context ctx) {
        this.ctx = ctx;
        net = new Network(ctx);
    }

    /**
     * Mètodo para recuperar los delitos en una posicion especifica y a una distancia
     *
     * @param idTipo
     * @param idUsuario -1 si no está logeado
     * @param pos
     */
    public DelitoTO[] getDelitosByTipo(int idTipo, long idUsuario, int opcionDias,  LatLng pos) throws NetworkException, NoInternetException {

        if(pos == null){
            Log.d(TAG, "La posicion del usuario es nula no se puede recuperar los delitos cercanos");
            return new DelitoTO[0];
        }

        String url = END_POINT + END_POINT_DELITO_BY_TIPO + "tipoDelito/" + idTipo + "/idUsuario/" + idUsuario + "/time/" + opcionDias + "/lat/" + pos.latitude + "/lon/" + pos.longitude;

        Log.d(TAG, url);

        String json = net.doGetJson(url);

        DelitoTO[] res = gson.fromJson(json, DelitoTO[].class);

        return res;
    }


    /**
     * Recupera los datos de un delito
     *
     * @param req
     * @return
     * @throws NetworkException
     */
    public DelitoTO getDelitoDetails(String req) throws NetworkException, NoInternetException {


        String url = END_POINT + END_POINT_DETALLE_DELITO + req;

        Log.d(TAG, url);

        String json = net.doGetJson(url);

        try {
            DelitoTO res = gson.fromJson(json, DelitoTO.class);
            return res;
        } catch (JsonSyntaxException jse) {
            jse.printStackTrace();
            return null;
        }
    }

    public DelitoTO[] getDelitosTimeLine(int index) throws NetworkException, NoInternetException {

        String url = END_POINT + END_POINT_DELITO_TIMELINE + "limitMin/" + index + "/numberRow/" + 25;

        Log.d(TAG, url);

        String json = net.doGetJson(url);

        DelitoTO[] res = gson.fromJson(json, DelitoTO[].class);

        return res;
    }

    public NetResponse loginUsuario(String user, String pass) throws NetworkException, NoInternetException {

        String url = END_POINT + END_POINT_LOGIN_USER + "username/" + user + "/password/" + pass;
        Log.d(TAG, url);
        try {
            String json = net.doGetJson(url);
            NetResponse res = gson.fromJson(json, NetResponse.class);
            return res;
        } catch (JsonParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * Registra un nuevo usuario en la plataforma
     *
     * @param nombre
     * @param correo
     * @param password
     * @return
     * @throws NetworkException
     */
    public NetResponse registarNuevoUsuario(String nombre, String correo, String password) throws NetworkException, NoInternetException {

        String url = END_POINT + END_POINT_CREATE_USER;
        Log.d(TAG, url);
        try {
            List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();

            valuePairs.add(new BasicNameValuePair("nombre", nombre));
            valuePairs.add(new BasicNameValuePair("email", correo));
            valuePairs.add(new BasicNameValuePair("password", password));
            valuePairs.add(new BasicNameValuePair("repeatPassword", password));

            String json = net.doPostJson(url, valuePairs);
            NetResponse res = gson.fromJson(json, NetResponse.class);
            return res;
        } catch (JsonParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * Agrega un elemento multimedia al delito
     * @param idNumDelito
     * @param idEvento
     * @param txtBase64
     * @param idTipoMultimedia
     * @return
     * @throws NetworkException
     * @throws NoInternetException
     */
    public NetResponse publicarDelitoAddMultimedia(long idNumDelito, long idEvento, String txtBase64, int idTipoMultimedia) throws NetworkException, NoInternetException{
        String url = END_POINT + END_POINT_ADD_DELITO_MULTIMEDIA;
        Log.d(TAG, url);
        try {
            List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();

            valuePairs.add(new BasicNameValuePair("idNumDelito", idNumDelito + ""));
            valuePairs.add(new BasicNameValuePair("idEvento", idEvento + ""));
            valuePairs.add(new BasicNameValuePair("idTipoMultimedia", idTipoMultimedia + ""));
            valuePairs.add(new BasicNameValuePair("txtBase64", txtBase64));

            String json = net.doPostJson(url, valuePairs);
            NetResponse res = gson.fromJson(json, NetResponse.class);
            return res;
        } catch (JsonParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public NetResponse registerPushDevice(String token) throws NetworkException, NoInternetException{
        String url = END_POINT + END_POINT_REGISTER_DEVICE;
        Log.d(TAG, url);
        try {
            List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();

            valuePairs.add(new BasicNameValuePair("regId", token));
            valuePairs.add(new BasicNameValuePair("tipoDispositivo", "Android"));
            valuePairs.add(new BasicNameValuePair("version", "1.0")); //TODO

            String json = net.doPostJson(url, valuePairs);
            NetResponse res = gson.fromJson(json, NetResponse.class);
            return res;
        } catch (JsonParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * Almacena el suaurio pero usa los datos de FB
     *
     * @param usr usaurio para almacenar
     * @return
     * @throws NetworkException
     */
    public NetResponse saveUsuarioFB(UsuarioTO usr) throws NetworkException, NoInternetException {

        String url = END_POINT + END_POINT_CREATE_USER_FB;
        Log.d(TAG, url);
        try {
            List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();

            valuePairs.add(new BasicNameValuePair("fullName", URLEncoder.encode(usr.getTxtUsuario())));
            valuePairs.add(new BasicNameValuePair("email", usr.getTxtEmail()));
            valuePairs.add(new BasicNameValuePair("id", usr.getFbUser() + ""));
            valuePairs.add(new BasicNameValuePair("picture", usr.getUriPicImageStr()));

            String json = net.doPostJson(url, valuePairs);
            NetResponse res = gson.fromJson(json, NetResponse.class);
            return res;
        } catch (JsonParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public NetResponse addPoint(DelitoTO delito, Long idUser) throws NetworkException, NoInternetException {
        String url = END_POINT + END_POINT_LIKE_DELITO;
        Log.d(TAG, url);
        try {
            List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();

            valuePairs.add(new BasicNameValuePair("idNumDelito", "" + delito.getId_num_delito()));
            valuePairs.add(new BasicNameValuePair("idEvento", "" + delito.getId_evento()));
            valuePairs.add(new BasicNameValuePair("idUsuario", "" + idUser));

            String json = net.doPostJson(url, valuePairs);
            NetResponse res = gson.fromJson(json, NetResponse.class);
            return res;
        } catch (JsonSyntaxException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @param delitoReporte
     * @param idUsuario
     * @param anonimo
     * @return
     * @throws NetworkException
     * @throws NoInternetException
     */
    public NetResponse addDelito(DelitoTO delitoReporte, long idUsuario, boolean anonimo) throws NetworkException, NoInternetException {

        String url = END_POINT + END_POINT_REPORTAR_DELITO;
        Log.d(TAG, url);
        try {
            List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();

            valuePairs.add(new BasicNameValuePair("idTipoDelito", "" + delitoReporte.getId_tipo_delito()));
            valuePairs.add(new BasicNameValuePair("idSubTipoDelito", "" + delitoReporte.getId_tipo_sub_delito()));

            String fecha = StringsUtils.dateFormatYYYYMMDD(delitoReporte.getFch_delito());


            valuePairs.add(new BasicNameValuePair("fchEvento", fecha));
            valuePairs.add(new BasicNameValuePair("txtDescripcion", delitoReporte.getTxt_resumen()));
            valuePairs.add(new BasicNameValuePair("numVictimas", "" + delitoReporte.getNum_victimas()));
            valuePairs.add(new BasicNameValuePair("numDelincuentes", "" + delitoReporte.getNum_delincuentes()));

            valuePairs.add(new BasicNameValuePair("txtPais", delitoReporte.getDireccion().getTxtPais()));
            valuePairs.add(new BasicNameValuePair("txtEstado", delitoReporte.getDireccion().getTxtEstado()));
            valuePairs.add(new BasicNameValuePair("txtMunicipio", delitoReporte.getDireccion().getTxtMunicipio()));
            valuePairs.add(new BasicNameValuePair("txtColonia", delitoReporte.getDireccion().getTxtColonia()));
            valuePairs.add(new BasicNameValuePair("txtCalle", delitoReporte.getDireccion().getTxtCalle()));
            valuePairs.add(new BasicNameValuePair("txtNumero", delitoReporte.getDireccion().getTxtNumero()));
            valuePairs.add(new BasicNameValuePair("txtNumeroInterior", ""));
            valuePairs.add(new BasicNameValuePair("numLatitud", delitoReporte.getDireccion().getNumLatitud()));
            valuePairs.add(new BasicNameValuePair("numLongitud", delitoReporte.getDireccion().getNumLongitud()));
            valuePairs.add(new BasicNameValuePair("txtDescripcionLugar", delitoReporte.getDireccion().getDireccion()));

            valuePairs.add(new BasicNameValuePair("idTipoMultimedia", ""));
            valuePairs.add(new BasicNameValuePair("txtArchivo", ""));
            valuePairs.add(new BasicNameValuePair("txtNombreArchivo", ""));

            valuePairs.add(new BasicNameValuePair("idUsuario", "" + idUsuario));
            valuePairs.add(new BasicNameValuePair("bAnonimo", "" + anonimo));


            String json = net.doPostJson(url, valuePairs);
            NetResponse res = gson.fromJson(json, NetResponse.class);
            return res;
        } catch (JsonSyntaxException ex) {
            ex.printStackTrace();
        }
        return null;
    }



}
