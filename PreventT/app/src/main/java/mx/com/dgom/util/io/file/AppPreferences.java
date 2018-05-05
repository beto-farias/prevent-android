package mx.com.dgom.util.io.file;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {

	public static final String PREFS_NAME = "MyPrefsFile";
	
	
	
	
	public static void saveString(String key, String value, Context ctx) {
		SharedPreferences sharedpreferences = ctx.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getString(String key, Context ctx) {
		SharedPreferences sharedpreferences = ctx.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE);
		String res = sharedpreferences.getString(key, null);
		return res;
	}

	public static void saveLong(String key, long value, Context ctx) {
		SharedPreferences sharedpreferences = ctx.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static long getLong(String key, Context ctx) {
		SharedPreferences sharedpreferences = ctx.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE);
		long res = sharedpreferences.getLong(key, -1);
		return res;
	}

	public static void saveBoolean(String key, boolean value, Context ctx) {
		SharedPreferences sharedpreferences = ctx.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static boolean getBoolean(String key, Context ctx) {
		SharedPreferences sharedpreferences = ctx.getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE);
		boolean res = sharedpreferences.getBoolean(key, false);
		return res;
	}

}
