package mx.com.dgom.util.shorcut;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import mx.com.dgom.sercco.android.prevent.act.R;

/**
 * Created by beto on 10/29/15.
 */
public class ShorcutUtils {

    private static void addShortcut(Context ctx, Class actividad,String appName) {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(ctx, actividad);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(ctx, R.drawable.ic_launcher));

        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        ctx.sendBroadcast(addIntent);
    }

    /**
     * Crea el icono en el escritorio
     * @param ctx
     * @param actividad
     * @param prefName
     */
    public static void addShorcut(Context ctx, Class actividad, String prefName,String appName){
        SharedPreferences sp = ctx.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        if(!sp.getBoolean("IS_ICON_CREATED", false)){
            addShortcut(ctx,actividad, appName);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("IS_ICON_CREATED", true);
            editor.commit();
        }
    }
}
