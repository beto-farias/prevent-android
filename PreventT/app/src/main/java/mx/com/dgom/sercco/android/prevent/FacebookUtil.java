package mx.com.dgom.sercco.android.prevent;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Created by beto on 10/24/15.
 */
public class FacebookUtil {

    private static final String TAG = "FacebookUtil";
    private static final String PREVEN_T_URL = "http://2gom.com.mx";
    private static final String PREVEN_T_LOGO_URL = "http://notei.com.mx/test/wwwPrevenT/images/logo_provicional.png";


    /**
     * Instalación de prevenT
     * @param ctx
     */
    public static void shareInstalledPrevenT(Activity ctx){
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("PrevenT")
                .setContentDescription("He instalado la aplicación PrevenT")
                .setContentUrl(Uri.parse(PREVEN_T_URL))
                .setImageUrl(Uri.parse(PREVEN_T_LOGO_URL))
                .build();

        shareLinkContent(ctx,linkContent);
    }


    /**
     * Compartir Delito
     * @param ctx
     * @param text
     */
    public static void shareDelitoPrevenT(Activity ctx, String text){
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("PrevenT")
                .setContentDescription(text)
                .setContentUrl(Uri.parse(PREVEN_T_URL))
                .setImageUrl(Uri.parse(PREVEN_T_LOGO_URL))
                .build();

        shareLinkContent(ctx,linkContent);
    }

    /**
     * Método que publica un ShareContent
     * @param ctx
     * @param linkContent
     */
    private static void shareLinkContent(Activity ctx, ShareContent linkContent){
        CallbackManager callbackManager;
        FacebookSdk.sdkInitialize(ctx);
        callbackManager = CallbackManager.Factory.create();
        final ShareDialog shareDialog = new ShareDialog(ctx);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d(TAG, "success");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "error");
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "cancel");
            }
        });


        if (shareDialog.canShow(ShareLinkContent.class)) {
            shareDialog.show(linkContent);
        }
    }
}
