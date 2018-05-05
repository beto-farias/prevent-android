package mx.com.dgom.sercco.android.prevent;

import android.app.Activity;
import android.widget.Toast;

import mx.com.dgom.sercco.android.prevent.act.R;

/**
 * Created by beto on 10/29/15.
 */
public class GUIUtils {

    public static void showNoInternetAccessToast(final Activity act){


        act.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(act, act.getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
            }
        });



    }
}
