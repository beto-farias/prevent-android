package mx.com.dgom.util.str;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

public class StringsUtils {

	private final static String DATE_FORMAT_DD_MM_YY = "dd/MM/yy";
	private final static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	private final static String DATE_FORMAT_DD_MM_YY_HH_MM_SS = "dd/MM/yy hh:mm:ss";
	private final static String DATE_FORMAT_HH_MM_SS = "H:mm:ss";
	private final static String DECIMAL_FORMAT = "#,##0.#";


	private static final SimpleDateFormat sdf = new SimpleDateFormat();
	private static final DecimalFormat sdecf = new DecimalFormat();


	public static String dateFormatDDMMYY(long date) {
		sdf.applyPattern(DATE_FORMAT_DD_MM_YY);
		return sdf.format(new Date(date));
	}

    public static String dateFormatYYYYMMDD(long date){
        sdf.applyPattern(DATE_FORMAT_YYYY_MM_DD);
        return sdf.format(new Date(date));
    }

	public static String dateFormatDDMMYYHHMMSS(long date) {
		sdf.applyPattern(DATE_FORMAT_DD_MM_YY_HH_MM_SS);
		return sdf.format(new Date(date));
	}

	public static String timeElapsedFormatHHMMSS(long millis) {
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;

		String time = String.format("%02d:%02d:%02d", hour, minute, second);
		return time;
	}
	
	public static String timeElapsedFormatHHMM(long millis) {
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;

		String time = String.format("%02d:%02d", hour, minute);
		return time;
	}
	
	public static String timeElapsedFormatHHMMSSMinimal(long millis) {
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;

		String time ;
		if(hour > 0){
			time = String.format("%02d:%02d:%02d horas", hour, minute, second);
		}if(minute > 0){
			time = String.format("%02d:%02d minutos",  minute, second);
		}else{
			time = String.format("%02d segundos", second);
		}
		return time;
	}

    public static String timeElapsedFormatDD(long mills){
        long diff = System.currentTimeMillis() - (mills * 1000);
        System.out.println("diff " + diff);
        int diffInDays = (int) ((diff) / (1000 * 60 * 60 * 24));
        return decimalFormat(diffInDays) + " días";
    }

	public static String timeElapsedFormatDDText(long mills){
		long diff = System.currentTimeMillis() - (mills * 1000);
		System.out.println("diff " + diff);
		int diffInDays = (int) ((diff) / (1000 * 60 * 60 * 24));

		if(diffInDays < 0){
			return "Dentro de " + decimalFormat(Math.abs(diffInDays)) + " días";
		}

		switch(diffInDays) {
			case 0:
				return "Hoy";
			case 1:
				return "Ayer";
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				return "En esta semana";
			default:
				return decimalFormat(diffInDays) + " días";
		}
	}
	
	public static String distanceFormat(double d){
		String res = " m";
		if(d/1000>=1){
			res = " km.";
			d = d / 1000;
		}
		
		res = decimalFormat(d) + res;
		return res;
	}
	
	public static String speedFormat(float speed) {
		return decimalFormat(speed * 3.6) + " km/h";
	}

	public static String decimalFormat(double d){
		sdecf.applyPattern(DECIMAL_FORMAT);
		return sdecf.format(d);
	}

	public static String Sha1(String data){
		String sha1 = "";
		try
		{
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(data.getBytes("UTF-8"));
			sha1 = byteToHex(crypt.digest());
		}
		catch(NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return sha1;
	}

	public static String byteToHex(final byte[] hash)
	{
		Formatter formatter = new Formatter();
		for (byte b : hash)
		{
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
