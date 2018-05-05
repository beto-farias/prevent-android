package mx.com.dgom.sercco.android.prevent.cloudservices;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import mx.com.dgom.sercco.android.prevent.Constantes;
import mx.com.dgom.sercco.android.prevent.Controller;
import mx.com.dgom.sercco.android.prevent.act.R;
import mx.com.dgom.util.io.net.NoInternetException;

/**
 * Created by beto on 12/2/15.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    //private static final String REGISTER_URL = "http://192.168.1.9/gcm/register.php";

    //private static final String KEY_TOKEN = "gcm_token";



    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                // [START register_for_gcm]
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.
                InstanceID instanceID = InstanceID.getInstance(this);

                String defaultSenderId = getString(R.string.gcm_defaultSenderId);
                String token = instanceID.getToken(defaultSenderId,GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                Log.i(TAG, "GCM Registration Token: " + token);

                // Si el token no se ha enviado al servidor, lo envía
                if (!sharedPreferences.getBoolean(Constantes.SENT_TOKEN_TO_SERVER, false)) {
                    if(sendRegistrationToServer(token)){
                        // You should store a boolean that indicates whether the generated token has been
                        // sent to your server. If the boolean is false, send the token to your server,
                        // otherwise your server should have already received the token.
                        sharedPreferences.edit().putBoolean(Constantes.SENT_TOKEN_TO_SERVER, true).apply();
                    }
                }


            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(Constantes.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Constantes.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     *  Ici nous allons envoyer le token de l'utilisateur au serveur
     *
     * @param token Le token
     */
    private boolean sendRegistrationToServer(String token) {
        Log.d(TAG,"Enviando token al server: " + token);
        Controller c = new Controller(getBaseContext());
        try {
            return c.registerPushDevice(token);
        } catch (NoInternetException e) {
            e.printStackTrace();
        }
        return false;
    }

}
