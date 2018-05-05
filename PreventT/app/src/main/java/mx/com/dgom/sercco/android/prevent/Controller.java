package mx.com.dgom.sercco.android.prevent;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.List;

import mx.com.dgom.sercco.android.prevent.to.DelitoTO;
import mx.com.dgom.sercco.android.prevent.to.MultimediaTO;
import mx.com.dgom.sercco.android.prevent.to.UsuarioTO;
import mx.com.dgom.util.io.images.BitmapUtils;
import mx.com.dgom.util.io.net.Network;
import mx.com.dgom.util.io.net.NetworkException;
import mx.com.dgom.util.io.net.NoInternetException;
import mx.com.dgom.util.io.net.to.NetResponse;
import mx.com.dgom.util.version.AppVersionTO;
import mx.com.dgom.util.version.VersionChecker;

/**
 * Created by beto on 10/12/15.
 */
public class Controller {

    private final String TAG = "Controller";

    protected Context ctx;

    Gson gson = new Gson();

    public Controller(Context ctx) {
        this.ctx = ctx;
    }


    public AppVersionTO verificaVersion() {
        String tipoDispositivo = "Android";
        //TODO poner el id de la app
        String idAplication = "";
        try {
            AppVersionTO vc = VersionChecker.verifyAppVersion(ctx, idAplication, tipoDispositivo);
            return vc;
        } catch (Exception e) {
            Toast.makeText(ctx, "Error al verificar la versión", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Recupera los delitos de un tipo especifico
     *
     * @param idTipo     tipo de delito
     * @param pos        Punto de bùsqueda
     * @param opcionDias parametro de cantidad de dias
     * @return Arreglo de puntos encontrados
     */
    public DelitoTO[] getDelitosByTipo(int idTipo, LatLng pos, int opcionDias) throws NoInternetException {
        AppNetwork net = new AppNetwork(ctx);
        try {
            DelitoTO[] res = net.getDelitosByTipo(idTipo, getUserId(), opcionDias, pos);
            return res;
        } catch (NetworkException e) {
            e.printStackTrace();
        }

        return new DelitoTO[0];
    }


    /**
     * Recupera los detalles del delito
     * @param numDelito, numero del delito
     * @param idEvento, id del evento
     * @return Delito to
     * @throws NoInternetException
     */
    public DelitoTO getDelitoDetails(long numDelito, long idEvento) throws NoInternetException {
        return getDelitoDetails("idNumDelito/" + numDelito + "/idEvento/" + idEvento);
    }

    /**
     * Recupera los detalles del delito
     *
     * @param req "idNumDelito/" + d.getId_num_delito() + "/idEvento/" + d.getId_evento()
     * @return
     */
    public DelitoTO getDelitoDetails(String req) throws NoInternetException {
        AppNetwork net = new AppNetwork(ctx);
        try {
            DelitoTO res = net.getDelitoDetails(req);
            return res;
        } catch (NetworkException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Mètodo que agrega un like al delito
     *
     * @param delitoMostradoDetalles
     * @return 1 guardado ok, 0 no se puede por que es mio, -1 error cuaquiera, -2 ya se le dio like
     */
    public int addPoint(DelitoTO delitoMostradoDetalles) throws NotUserLogeadoException, NoInternetException {

        Long idUser = getUserId();
        if (idUser < 1) {
            throw new NotUserLogeadoException("El usuario no esta logeado");
        }

        AppNetwork net = new AppNetwork(ctx);
        try {
            NetResponse res = net.addPoint(delitoMostradoDetalles, idUser);
            if (res != null) {
                return res.getCode(); //1 guardado ok, 0, no se puede por que es mio, -1 error, -2 ya se le dio like
            }
        } catch (NetworkException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public DelitoTO[] getDelitosTimeLine(int index) throws NoInternetException {
        AppNetwork net = new AppNetwork(ctx);
        try {
            DelitoTO[] res = net.getDelitosTimeLine(index);
            return res;
        } catch (NetworkException e) {
            e.printStackTrace();
        }

        return new DelitoTO[0];
    }

    public UsuarioTO login(String usr, String pass) throws NoInternetException {
        Gson gson = new Gson();
        AppNetwork net = new AppNetwork(ctx);
        try {
            NetResponse netRes = net.loginUsuario(usr, pass);
            if (netRes != null && netRes.getCode() == 1) { //Usuario valido
                String data = netRes.getData();

                //FIXME
                data = data.replaceAll("\"usuarioPro\":\"\"","\"usuarioPro\":0");

                Log.d(TAG, "Data -> " + data);

                UsuarioTO res = gson.fromJson(data, UsuarioTO.class);
                //Guardar el usuario
                saveUsuarioLocal(res, Constantes.LOGIN_MAIL);
                return res;
            }
        } catch (NetworkException e) {
            Log.e(TAG, "Error de Network");
            e.printStackTrace();
        } catch (JsonParseException ex) {
            Log.e(TAG, "Error de parceo de GSON");
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
     */
    public UsuarioTO registarNuevoUsuario(String nombre, String correo, String password) throws NoInternetException {

        AppNetwork net = new AppNetwork(ctx);
        try {
            NetResponse netRes = net.registarNuevoUsuario(nombre, correo, password);
            if (netRes != null && netRes.getCode() == 1) { //Usuario valido
                String data = netRes.getData();
                Log.d(TAG, "Data -> " + data);
                UsuarioTO res = gson.fromJson(netRes.getData(), UsuarioTO.class);

                //Guardar el usuario
                saveUsuarioLocal(res, Constantes.LOGIN_MAIL);
                return res;
            }
        } catch (NetworkException e) {
            Log.e(TAG, "Error de Network");
            e.printStackTrace();
        } catch (JsonParseException ex) {
            Log.e(TAG, "Error de parceo de GSON");
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * Agrega un reporte al repositorio
     *
     * @param delitoReporte
     * @param anonimo
     * @return
     * @throws NoInternetException
     */
    public DelitoTO publicarDelito(DelitoTO delitoReporte, List<MultimediaTO> images, boolean anonimo) throws NoInternetException {

        long userId = getUserId();

        AppNetwork net = new AppNetwork(ctx);
        try {
            NetResponse netRes = net.addDelito(delitoReporte, userId, anonimo);
            if (netRes != null && netRes.getCode() == 1) {
                DelitoTO delito = gson.fromJson(netRes.getData(), DelitoTO.class);

                for(int i = 0 ; images != null &&  i < images.size(); i++){
                    try {
                        publicarDelitoAddFoto(delito.getId_num_delito(), delito.getId_evento(),images.get(i).getBase64(ctx));
                    }catch (Exception e) {
                        Log.e(TAG, "Error de Network");
                        e.printStackTrace();
                    }
                }
                return delito;
            }
        } catch (NetworkException e) {
            Log.e(TAG, "Error de Network");
            e.printStackTrace();
        } catch (JsonParseException ex) {
            Log.e(TAG, "Error de parceo de GSON");
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Agrega una fotografia a un delito
     * @param idNumDelito
     * @param idEvento
     * @param datab64
     * @throws NoInternetException
     */
    public void publicarDelitoAddFoto(long idNumDelito, long idEvento, String datab64)throws NoInternetException{
        Log.d(TAG, "Agregando fotografía");
        if(datab64 == null){
            Log.d(TAG, "B54 null");
        }
        AppNetwork net = new AppNetwork(ctx);

        try {
            NetResponse netRes = net.publicarDelitoAddMultimedia(idNumDelito, idEvento, datab64, 1);
            if (netRes != null && netRes.getCode() == 1) {
                Log.d(TAG,"Se ha agregado la foto");
            }
        } catch (NetworkException e) {
            Log.e(TAG, "Error de Network");
            e.printStackTrace();
        } catch (JsonParseException ex) {
            Log.e(TAG, "Error de parceo de GSON");
            ex.printStackTrace();
        }
    }


    /**
     * Recupera el usuario almacendado en la app
     * @return usuario
     */
    public UsuarioTO getUsuarioLocal() {

        //Valida si el usuario está logeado
        int userLogeado = isUsuarioLogeado();
        if (userLogeado == Constantes.LOGIN_SIN_LOGIN) {
            return null;
        }

        UsuarioTO usr = new UsuarioTO();
        usr.setTxtUsuario(getUserName());
        usr.setIdUsuario(getUserId());
        //FIXME
        //usr.setUsuarioPro(getIsUsuarioPro());
        if (getUserPic() != null) {
            usr.setUriPicImageStr(getUserPic());
        }

        usr.setFbUser(getUserFBId());

        return usr;
    }

    /**
     * Logout del usuario, borra todos los datos almacenados
     */
    public void logoutUser() {
        SharedPreferences settings = ctx.getSharedPreferences(Constantes.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.remove(Constantes.PREFS_TIPO_LOGIN); //Tipo de login (local FB p TW)
        editor.remove(Constantes.PREFS_USER_ID); //Id del usaurio
        editor.remove(Constantes.PREFS_USER_NAME); //Nombre del usuario
        editor.remove(Constantes.PREFS_USER_PIC_URL); //Imagen
        editor.remove(Constantes.PREFS_USER_FB_ID); //Si está logeado con FB

        //Deselecciona los delitos
        for(int i=0; i <= Constantes.CANT_MAXIMA_DELITOS; i++) {
            editor.remove(Constantes.PREFS_TIPO_DELITO_ + i);
        }
        editor.commit();
    }

    /**
     * Guarda el usuario registrado con FB
     *
     * @param usr
     */
    public void saveUsuarioFB(UsuarioTO usr) throws NoInternetException {

        //Guardar el usuario
        AppNetwork net = new AppNetwork(ctx);
        try {
            NetResponse netRes = net.saveUsuarioFB(usr);//Envia el suaurio de FB al server
            if (netRes != null && netRes.getCode() == 1) { //Usuario valido
                String data = netRes.getData();
                Log.d(TAG, "Data -> " + data);
                UsuarioTO res = gson.fromJson(netRes.getData(), UsuarioTO.class);

                //Guardar el usuario
                saveUsuarioLocal(res, Constantes.LOGIN_FB);
            }
        } catch (NetworkException e) {
            Log.e(TAG, "Error de Network");
            e.printStackTrace();
        } catch (JsonParseException ex) {
            Log.e(TAG, "Error de parceo de GSON");
            ex.printStackTrace();
        } catch (Exception ex) {
            Log.e(TAG, "Error ");
            ex.printStackTrace();
        }

    }

    /**
     * Registra el device para notificaciones push
     *
     * @param token
     * @return
     * @throws NoInternetException
     */
    public boolean registerPushDevice(String token) throws NoInternetException {
        AppNetwork net = new AppNetwork(ctx);
        try {
            NetResponse netRes = net.registerPushDevice(token);//Envia el detalle del dispositivo
            if (netRes != null && netRes.getCode() == 1) { //Registro correcto
                String data = netRes.getData();
                Log.d(TAG, "Data -> " + data);
                return true;
            }
            return false;
        } catch (NetworkException e) {
            Log.e(TAG, "Error de Network");
            e.printStackTrace();
        } catch (JsonParseException ex) {
            Log.e(TAG, "Error de parceo de GSON");
            ex.printStackTrace();
        } catch (Exception ex) {
            Log.e(TAG, "Error ");
            ex.printStackTrace();
        }

        return false;
    }


    //------------------------------------------------

    /**
     * Indica que ya no se debe mostrar el intro
     */
    public void hideIntro(){
        SharedPreferences settings = ctx.getSharedPreferences(Constantes.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Constantes.PREF_SHOW_INTRO, false);
        editor.commit();
    }


    /**
     * Indica si se debe mostar o no el intro de la app
     * @return true si se debe mostrar
     */
    public boolean showIntro(){
        SharedPreferences settings = ctx.getSharedPreferences(Constantes.PREFS_NAME, 0);
        if(settings.contains(Constantes.PREF_SHOW_INTRO)){
            return settings.getBoolean(Constantes.PREF_SHOW_INTRO, true);
        }

        return true; //Si debe mostar el intro
    }


    /**
     * Guarda los datos del usuario en el dispositivo local
     *
     * @param usr
     * @param tipoLogin
     */
    private void saveUsuarioLocal(UsuarioTO usr, int tipoLogin) {
        if (usr == null) {
            return;
        }

        SharedPreferences settings = ctx.getSharedPreferences(Constantes.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putLong(Constantes.PREFS_USER_ID, usr.getIdUsuario()); //Id del usuario
        editor.putInt(Constantes.PREFS_TIPO_LOGIN, tipoLogin); //Tipo de login (local FB p TW)
        editor.putString(Constantes.PREFS_USER_NAME, usr.getTxtUsuario()); //Nombre del usuario

        //FIXME
        //editor.putInt(Constantes.PREF_USER_PRO, usr.getUsuarioPro()); //Es usuario Pro

        if (usr.getUriPicImageStr() != null) {
            editor.putString(Constantes.PREFS_USER_PIC_URL, usr.getUriPicImageStr()); //Imagen
        }

        if (usr.getFbUser() != null) {
            editor.putLong(Constantes.PREFS_USER_FB_ID, usr.getFbUser()); //Si está logeado con FB
        }

        editor.commit();
    }

    //Verifica si un delito está selecionado
    public boolean isDelitoSelected(int idDelito) {
        SharedPreferences settings = ctx.getSharedPreferences(Constantes.PREFS_NAME, 0);
        return settings.getBoolean(Constantes.PREFS_TIPO_DELITO_ + idDelito, false);
    }

    /**
     * Registra si un delito está o no seleccionado
     *
     * @param idDelito Id del delito
     * @param estado   true / false
     */
    public void setDelitoSelected(int idDelito, boolean estado) {
        SharedPreferences settings = ctx.getSharedPreferences(Constantes.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Constantes.PREFS_TIPO_DELITO_ + idDelito, estado);
        editor.commit();
    }

    /**
     * VERIFICA SI EL USUARIO ESTÁ LOGEADO DE ALGUNA MANERA
     *
     * @return
     */
    private int isUsuarioLogeado() {
        SharedPreferences settings = ctx.getSharedPreferences(Constantes.PREFS_NAME, 0);
        return settings.getInt(Constantes.PREFS_TIPO_LOGIN, 0);
    }

    private String getUserName() {
        SharedPreferences settings = ctx.getSharedPreferences(Constantes.PREFS_NAME, 0);
        return settings.getString(Constantes.PREFS_USER_NAME, "Anonimo");
    }

    private long getUserId() {
        SharedPreferences settings = ctx.getSharedPreferences(Constantes.PREFS_NAME, 0);
        return settings.getLong(Constantes.PREFS_USER_ID, -1);
    }

    private String getUserPic() {
        SharedPreferences settings = ctx.getSharedPreferences(Constantes.PREFS_NAME, 0);
        return settings.getString(Constantes.PREFS_USER_PIC_URL, null);
    }

    private int getIsUsuarioPro() {
        SharedPreferences settings = ctx.getSharedPreferences(Constantes.PREFS_NAME, 0);
        return settings.getInt(Constantes.PREF_USER_PRO, 0);
    }

    private Long getUserFBId() {
        SharedPreferences settings = ctx.getSharedPreferences(Constantes.PREFS_NAME, 0);
        return settings.getLong(Constantes.PREFS_USER_FB_ID, -1);
    }


    public Bitmap getProfilePictureBitmap(String uriPicImage) throws NoInternetException {
        Network net = new Network(ctx);
        Bitmap res = net.downloadBitmap(uriPicImage);
        return res;
    }


    public Bitmap getDelitoPictureBitmap(String nombreArchivo, long numdelito, long ide) throws NoInternetException {
        String url = AppNetwork.END_PONT_DELITOS_IMAGENES + numdelito +  ide + "/" + nombreArchivo;
        Network net = new Network(ctx);
        Bitmap res = net.downloadBitmap(url);
        return res;
    }

    /**
     * Carga el thumnail del video
     * @param fileName
     * @param numDelito
     * @param ide
     * @return
     * @throws NoInternetException
     */
    public Bitmap retriveVideoFrameFromVideo(String fileName, long numDelito, long ide) throws NoInternetException{
        String url = AppNetwork.END_PONT_DELITOS_IMAGENES + numDelito +  ide + "/" + fileName;
        Bitmap res = BitmapUtils.retriveVideoFrameFromVideo(url);
        return res;
    }



    /**
     * Indica si el usaurio se encuentra registrado en la plataforma y autentificado en el equipo
     *
     * @return
     */
    public boolean isUsuarioRegistrado() {
        return getUserId() > 0;
    }


}
