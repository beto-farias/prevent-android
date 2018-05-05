package mx.com.dgom.util.maps;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
//import android.location.

public class MapUtils {

	/**
	 * Recupera la distancia en metrosentre los punto a y b
	 * @param a
	 * @param b
	 * @return
	 */
	public static double distance(LatLng a, LatLng b) {
		if(a == null || b == null){
			return 0;
		}
		return distance(a.latitude, a.longitude, b.latitude, b.longitude);
	}

	/**
	 * Recuopera la distancia en metros
	 * @param lat_a
	 * @param lng_a
	 * @param lat_b
	 * @param lng_b
	 * @return
	 */
	public static double distance(double lat_a, double lng_a, double lat_b, double lng_b) {
		double earthRadius = 3958.75;
		double latDiff = Math.toRadians(lat_b - lat_a);
		double lngDiff = Math.toRadians(lng_b - lng_a);
		double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) + Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) * Math.sin(lngDiff / 2)
				* Math.sin(lngDiff / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = earthRadius * c;

		int meterConversion = 1609;

		return distance * meterConversion;
	}


	/**
	 * Método que regresa opciones de direcciones a partir de un string
	 * @param strAddress, direccion 
	 * @param ctx
	 * @return
	 * @throws IOException
	 */
	public static List<Address> getLocationFromAddress(String strAddress, Context ctx) throws IOException{
		Geocoder coder = new Geocoder(ctx);
		List<Address> addressList;
		addressList = coder.getFromLocationName(strAddress,1);
		if (addressList == null) {
			return null;
		}
		return addressList;
	}

	/**
	 * Recupera una direccion a partir de las coordenadas
	 * @param params
	 * @param mContext
	 * @return
	 */
	public static String getAddress(LatLng params, Context mContext){
		Geocoder geocoder = new Geocoder(mContext);
		double latitude = params.latitude;
		double longitude = params.longitude;

		List<Address> addresses = null;
		String addressText="";

		try {
			addresses = geocoder.getFromLocation(latitude, longitude,1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(addresses != null && addresses.size() > 0 ){
			Address address = addresses.get(0);

			//addressText = String.format("%s, %s, %s",
			addressText = String.format("%s, %s",
					address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
							//address.getLocality(),
							address.getCountryName());
		}
		
		return addressText;
	}


	/**
	 * Recupera
	 * @param params
	 * @param mContext
	 * @return
	 */
	public static List<Address> getAddressList(Context mContext,LatLng params, int numMaxResults ){
		Geocoder geocoder = new Geocoder(mContext);
		double latitude = params.latitude;
		double longitude = params.longitude;

		List<Address> addresses = null;
		String addressText="";

		try {
			addresses = geocoder.getFromLocation(latitude, longitude,numMaxResults);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return addresses;

	}
	
	
	/**
	 * Agrega un marcador al mapa
	 * @param position
	 * @param title
	 * @param snippet
	 * @return
	 */
	public static Marker addMarker(LatLng position, String title, String snippet,GoogleMap map){

		MarkerOptions mo = new MarkerOptions();
		mo.position(position);
		if(title != null){
			mo.title(title);
		}
		if(snippet != null){
			mo.snippet(snippet);
		}
		Marker marker = map.addMarker(mo);

		return marker;
	}
}
