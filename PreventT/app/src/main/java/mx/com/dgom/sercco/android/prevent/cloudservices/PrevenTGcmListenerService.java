package mx.com.dgom.sercco.android.prevent.cloudservices;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;

import java.util.Random;

import mx.com.dgom.sercco.android.prevent.Constantes;
import mx.com.dgom.sercco.android.prevent.Controller;
import mx.com.dgom.sercco.android.prevent.Session;
import mx.com.dgom.sercco.android.prevent.act.HomeActivity;
import mx.com.dgom.sercco.android.prevent.act.R;
import mx.com.dgom.sercco.android.prevent.act.SplashActivity;
import mx.com.dgom.sercco.android.prevent.to.DelitoTO;
import mx.com.dgom.sercco.android.prevent.to.NotificacionDelitoEvento;
import mx.com.dgom.sercco.android.prevent.to.UsuarioTO;
import mx.com.dgom.util.io.net.NoInternetException;
import mx.com.dgom.util.io.net.to.NetResponse;
import mx.com.dgom.util.str.StringsUtils;

/**
 * Created by beto on 12/2/15.
 */
public class PrevenTGcmListenerService extends GcmListenerService {

    private static final String TAG = "PreTGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from       SenderID of the sender.
     * @param bundleData Data bundle containing message data as key/value pairs.
     *                   For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle bundleData) {

        Gson json = new Gson();
        NotificacionDelitoEvento nde;

     //   String message = bundleData.getString("message");//No se usa
     //   String title = bundleData.getString("title");//No se usa
        String data = bundleData.getString("data");

        Log.d(TAG, "FromFrom: " + from);
     //   Log.d(TAG, "Message: " + message);
        Log.d(TAG, "Data: " + data);

        try {
            nde = json.fromJson(data, NotificacionDelitoEvento.class);
            sendNotification(nde);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param nde GCM message received.
     */
    private void sendNotification(NotificacionDelitoEvento nde) {
        Controller cnt = new Controller(getApplicationContext());
        try {
            //Verifica si el mismo usuario es el que realizo el reporte y no muestra la notificacion
            UsuarioTO usr = cnt.getUsuarioLocal();
            if (usr != null && usr.getIdUsuario() == nde.getId_usuario()) {
                Log.d(TAG, "El mensaje fue enviado por el mismo usuario");
                return;
            }

            DelitoTO dto = cnt.getDelitoDetails(nde.getId_num_delito(), nde.getId_evento());

            if (dto == null) {
                Log.d(TAG, "El delito llego nulo");
                return;
            }

            //TODO, verifica la distancia con el delito si es mayor no se muestra la notificación

            long distancia = 1000;


            if (Session.homeActivity != null) {
                Session.homeActivity.showAlertNewDelito(dto);
            } else {


                String title = "Nuevo delito reportado";
                String message = "Se ha reportado un nuevo delito de " + Constantes.getDelitoNombre(dto.getId_tipo_delito()); // + StringsUtils.distanceFormat(distancia) + " de ti";

                //Pantalla que se va a mostrar si le da click a la notificación
                Intent intent = new Intent(this, HomeActivity.class);

                intent.putExtra(Constantes.EXTRA_ID_DELITO, "idNumDelito/" + dto.getId_num_delito() + "/idEvento/" + dto.getId_evento());


                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // Genera un numero aleatorio para la notificación
                notificationManager.notify(new Random().nextInt(9999), notificationBuilder.build());
            }
        }catch(NoInternetException e){
            e.printStackTrace();
        }

    }
}
