package mx.com.dgom.sercco.android.prevent.act;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

import mx.com.dgom.sercco.android.prevent.Controller;
import mx.com.dgom.sercco.android.prevent.GUIUtils;
import mx.com.dgom.sercco.android.prevent.to.UsuarioTO;
import mx.com.dgom.util.io.net.NoInternetException;

public class LoginActivity extends Activity {

    private final String TAG = "LoginActivity";

    TextView txtUserName;
    TextView txtPassword;

    //Facebook
    //LoginButton loginButtonFb;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);


        txtUserName = (TextView) findViewById(R.id.txt_user_name);
        txtPassword = (TextView) findViewById(R.id.txt_password);

        //loginButtonFb = (LoginButton) findViewById(R.id.login_button_fb);
        //loginButtonFb.setReadPermissions(Arrays.asList("public_profile, email, user_friends"));


        // Callback registration
        //loginButtonFb.registerCallback(callbackManager, new FBRegisterCB());
    }


    /**
     * Accion del botón crear cuenta de correo electrónico
     *
     * @param v
     */
    public void crearCuentaAction(View v) {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.flip_middle_in, R.anim.flip_middle_out);
        finish();
    }

    // ---------- FACEBOOK -------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void loginAction(View v) {
        new LoginTask().execute(txtUserName.getText().toString(), txtPassword.getText().toString());
    }


    //--------------

    class LoginTask extends AsyncTask<String, Long, UsuarioTO> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(LoginActivity.this, getString(R.string.please_wait), getString(R.string.autenticating_user), false);
            pd.show();
        }

        @Override
        protected UsuarioTO doInBackground(String[] strings) {
            Controller controller = new Controller(getBaseContext());
            UsuarioTO usuario = null;
            try {
                usuario = controller.login(strings[0], strings[1]);
            } catch (NoInternetException e) {
                e.printStackTrace();
                GUIUtils.showNoInternetAccessToast(LoginActivity.this);
                return null;
            }
            return usuario;
        }

        @Override
        protected void onPostExecute(UsuarioTO usuario) {
            super.onPostExecute(usuario);
            if (pd != null) {
                pd.dismiss();
            }

            if (usuario != null) {
                //Toast.makeText(getBaseContext(), "Usuaurio " + usuario.getTxtUsuario(), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Usuaurio / password incorrecto ", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Clase para manejar el login con Facebook
     */
    class FBRegisterCB implements FacebookCallback<LoginResult> {


        @Override
        public void onSuccess(final LoginResult loginResult) {

            // App code
            // Toast.makeText(getBaseContext(), "Facebook login ok", Toast.LENGTH_LONG).show();
            Log.d(TAG, loginResult.toString());

            //Solicita los datos del usaurio
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject user, GraphResponse response) {
                    Log.v(TAG, response.toString());
                    Log.d(TAG, user.toString());

                    Log.d(TAG,user.optString("picture"));
/*
                    //Carga el profile del usuario
                    Uri profilePic = null;
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        profilePic = profile.getProfilePictureUri(200, 200);
                    }
*/
                    //Crea el usuario
                    String urlImg = user.optString("picture").replaceAll("\\\\","");
                    urlImg = urlImg.substring(urlImg.indexOf("https"), urlImg.indexOf("\","));

                    Log.d(TAG,"urlImg->" + urlImg + "<--");
                    UsuarioTO usr = new UsuarioTO(user.optString("name"), user.optString("email"), urlImg, Long.parseLong(user.optString("id")));

                    Controller controller = new Controller(getBaseContext());
                    try {
                        controller.saveUsuarioFB(usr);
                    } catch (NoInternetException e) {
                        e.printStackTrace();
                        GUIUtils.showNoInternetAccessToast(LoginActivity.this);
                    }

                    //LoginManager.getInstance().logInWithPublishPermissions(LoginActivity.this, Arrays.asList("publish_actions"));

                    //Comparte que se ha instalado la app
                    //FacebookUtil.shareInstalledPrevenT(LoginActivity.this);

                    //Termina la ventana de login
                    overridePendingTransition(R.anim.flip_middle_in, R.anim.flip_middle_out);
                    LoginActivity.this.finish(); //Termina la app de login

                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,picture.type(large)");

            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            // App code
            //Toast.makeText(getBaseContext(), "Facebook login cancel", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(FacebookException exception) {
            // App code
            Toast.makeText(getBaseContext(), "Facebook login exeption", Toast.LENGTH_LONG).show();
            exception.printStackTrace();
        }
    }


}
