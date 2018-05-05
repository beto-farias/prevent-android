package mx.com.dgom.util.maps.places;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Beto on 23/09/2015.
 */
public class PlacesUtil {

    private static final String LOG_TAG = "GoogleP Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyCx2YGC-kONIHF9IBvHqoZv2JeMi18hODE";


    /**
     * Método para recuperar las direcciones del auto complete
     * @param input texto a buscar
     * @return Lista de coincidencias
     */
    public static ArrayList autocomplete(String input) {
        ArrayList<PlaceTO> resultList = new ArrayList<PlaceTO>();

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:mx");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            System.out.println(sb.toString());

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            PlaceTO pto;
            // Extract the Place descriptions from the results
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println(predsJsonArray.getJSONObject(i).getString("place_id"));
                System.out.println("============================================================");
                pto = new PlaceTO();
                pto.setName(predsJsonArray.getJSONObject(i).getString("description"));
                pto.setPlaceId(predsJsonArray.getJSONObject(i).getString("place_id"));
                resultList.add(pto);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }


    /**
     * Recupera la latitud y longitud de los Places de Google
     * @param placeID
     * @return
     */
    public static List<LatLng> details(String placeID){
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&placeid=" + placeID);
            //sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            System.out.println(sb.toString());

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }

            JSONObject jsonObj = new JSONObject(jsonResults.toString());

            return parse(jsonObj);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return new ArrayList<LatLng>();
    }


    /**
     * REcupera la lista de la latitud y longitud de los objetos recibidos en el JObject
     * @param jObject, Objeto Json con los datos
     * @return Lista de latLon
     */
    private static List<LatLng> parse(JSONObject jObject){

        LatLng latLong = null;

        Double lat = Double.valueOf(0);
        Double lng = Double.valueOf(0);

        HashMap<String, String> hm = new HashMap<String, String>();
        List<LatLng> list = new ArrayList<LatLng>();

        try {

            lat = (Double)jObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lat");
            lng = (Double)jObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lng");

            System.out.println(lat);
            System.out.println(lng);
            System.out.println("============================================================");

            latLong = new LatLng(lat,lng);

        } catch (JSONException e) {
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

        //hm.put("lat", Double.toString(lat));
        //hm.put("lng", Double.toString(lng));

        list.add(latLong);
        return list;
    }
}
