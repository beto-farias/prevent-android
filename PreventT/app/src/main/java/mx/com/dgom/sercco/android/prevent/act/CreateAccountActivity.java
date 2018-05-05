package mx.com.dgom.sercco.android.prevent.act;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import mx.com.dgom.sercco.android.prevent.Controller;
import mx.com.dgom.sercco.android.prevent.GUIUtils;
import mx.com.dgom.sercco.android.prevent.to.UsuarioTO;
import mx.com.dgom.util.io.net.NoInternetException;

public class CreateAccountActivity extends Activity {

    TextView txtNombrePersona;
    TextView txtCorreoPersona;
    TextView txtPasswordPersona;
    TextView txtPasswordPersona2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        txtNombrePersona = (TextView) findViewById(R.id.txt_nombre_persona);
        txtCorreoPersona = (TextView) findViewById(R.id.txt_correo_persona);
        txtPasswordPersona = (TextView) findViewById(R.id.txt_password_persona);
        txtPasswordPersona2 = (TextView) findViewById(R.id.txt_password_persona_2);
    }

   public void crearCuentaAction(View v){
        if(txtNombrePersona.getText().length() == 0){
            Toast.makeText(getBaseContext(), getString(R.string.error_nombre_persona_vacio), Toast.LENGTH_SHORT).show();
            return;
        }

       if(txtCorreoPersona.getText().length() == 0){
           Toast.makeText(getBaseContext(), getString(R.string.error_correo_persona_vacio), Toast.LENGTH_SHORT).show();
           return;
       }

       if(txtPasswordPersona.getText().length() == 0){
           Toast.makeText(getBaseContext(), getString(R.string.error_password_persona_vacio), Toast.LENGTH_SHORT).show();
           return;
       }

       if(txtPasswordPersona2.getText().length() == 0){
           Toast.makeText(getBaseContext(), getString(R.string.error_password_persona_2_vacio), Toast.LENGTH_SHORT).show();
           return;
       }

       if(!txtPasswordPersona.getText().toString().equals(txtPasswordPersona2.getText().toString())){
           Toast.makeText(getBaseContext(), getString(R.string.error_password_persona_no_coincide), Toast.LENGTH_SHORT).show();
           return;
       }

       new RegistarUsuarioTask().execute(txtNombrePersona.getText().toString(), txtCorreoPersona.getText().toString(), txtPasswordPersona.getText().toString());
   }

    /**
     * Metodo que se llama una vez que se ha terminado el proceso de crear una cuenta nueva
     * @param usr
     */
    public void crearCuentaResponse(UsuarioTO usr){
        if(usr == null){
            Toast.makeText(getBaseContext(),R.string.error_usuario_existente, Toast.LENGTH_SHORT).show();
        }else {
            finish();
            overridePendingTransition(R.anim.flip_middle_in, R.anim.flip_middle_out);
        }
    }




    //-------------- TASK CLASS ----------------

    /**
     * Clase para registrar al usuario de manera asincrona
     */
    class RegistarUsuarioTask extends AsyncTask <String,Integer,UsuarioTO>{

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CreateAccountActivity.this);
            pd.setTitle(getString(R.string.registrando_usuario));
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected UsuarioTO doInBackground(String... strings) {
            Controller controller = new Controller(getBaseContext());
            UsuarioTO usr = null;
            try {
                usr = controller.registarNuevoUsuario(strings[0],strings[1],strings[2]);
            } catch (NoInternetException e) {
                e.printStackTrace();
                GUIUtils.showNoInternetAccessToast(CreateAccountActivity.this);
                return null;
            }
            return usr;
        }

        @Override
        protected void onPostExecute(UsuarioTO usuarioTO) {
            super.onPostExecute(usuarioTO);
            if(pd != null){
                pd.dismiss();
            }

            crearCuentaResponse(usuarioTO);
        }
    }
}
