package mx.com.dgom.util.version;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.google.gson.Gson;

import mx.com.dgom.util.io.net.Network;
import mx.com.dgom.util.io.net.NetworkException;
import mx.com.dgom.util.io.net.NoInternetException;

public class VersionChecker {

	// URL CHECADOR DE VERSIONES
	private static final String VERSION_URL = "http://version.2gom.com.mx/response/getVersionApp.php?";

	/**
	 * Verifica si la version instalada del APK es la más actual o no, en caso
	 * de regresar un String es que hay una nueva version
	 * 
	 * @return String con la URL para la descarga
	 * @throws NetworkException Error de conexion a la red
	 * @throws NameNotFoundException  Error de recuperacion del numero de version de la aplicacion del Manifest
	 */
	public static AppVersionTO verifyAppVersion(Context ctx, String idAplicacion, String tipoDispositivo) throws NetworkException, NameNotFoundException {
		Network net = new Network(ctx);
		Gson gson = new Gson();

		// Recupera la version del manifest
		int versionCode = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;

		String qs = VERSION_URL + "function=compareVersion" + "&idAplicacion=" + idAplicacion + "&version=" + versionCode + "&txtTipoDispositivo=" + tipoDispositivo;

		String jsonResponse = null;
		try {
			jsonResponse = net.doGetJson(qs);
		} catch (NoInternetException e) {
			e.printStackTrace();
			//GUIUtils.showNoInternetAccessToast(ctx.this);
			return null;
		}
		AppVersionTO[] apps = gson.fromJson(jsonResponse, AppVersionTO[].class);

		if (apps == null || apps.length == 0) {
			return null;
		}

		AppVersionTO app = apps[0];

		// Verifica la version actual con la version del servidor
		if (app.getId_version_aplicacion() > versionCode) {
			return app;
		}
		return null;
	}

}
