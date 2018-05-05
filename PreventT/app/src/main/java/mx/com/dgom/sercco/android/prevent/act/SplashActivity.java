package mx.com.dgom.sercco.android.prevent.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import mx.com.dgom.sercco.android.prevent.Constantes;
import mx.com.dgom.sercco.android.prevent.Controller;
import mx.com.dgom.sercco.android.prevent.cloudservices.RegistrationIntentService;
import mx.com.dgom.util.shorcut.ShorcutUtils;
import mx.com.dgom.util.version.AppVersionTO;

public class SplashActivity extends Activity {

    private final String TAG = "SplashActivity";

    private static int SPLASH_TIME_OUT = 3000;

    //Push notification register
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    private void doRegisterDevice(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(Constantes.SENT_TOKEN_TO_SERVER, false);
                /*if (sentToken) {
                    Toast.makeText(context,"Token enviado",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context,"Token enviado error",Toast.LENGTH_LONG).show();
                }*/
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

    }


    /**
     * Verifica si está instalado Google Play Service
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,new IntentFilter(Constantes.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        //FIXME ya que duplica el acceso directo
        //ShorcutUtils.addShorcut(this,SplashActivity.class,Constantes.PREFS_NAME, getString(R.string.app_name));

        doRegisterDevice();


        final Controller controller = new Controller(getApplicationContext());

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {



                AppVersionTO version = controller.verificaVersion();

                if (version != null) {
                    showUpdateDialog(version);
                }else{
                    continueApp();
                }
            }
        }, SPLASH_TIME_OUT);


    }


    private void showUpdateDialog(final AppVersionTO version){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Actualización disponible");

        alertDialogBuilder.setMessage(getString(R.string.actualizacion, version.getId_version_aplicacion()))
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String url = version.getTxt_url();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        finish();

                    }
                });

    }

    private void continueApp(){
        Controller controller = new Controller(this);
        Intent i;

        //Dependiendo si ya se mostro el intro se selecciona la vista a mostrar
        if(controller.showIntro()){
            i = new Intent(SplashActivity.this, IntroActivity.class);
        }else{
            i = new Intent(SplashActivity.this, HomeActivity.class);
        }
        startActivity(i);
        overridePendingTransition(R.anim.flip_middle_in, R.anim.flip_middle_out);
        finish();

    }
}
