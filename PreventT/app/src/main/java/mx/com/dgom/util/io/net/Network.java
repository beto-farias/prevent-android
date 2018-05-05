package mx.com.dgom.util.io.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class Network {

	private static final int HTTP_TIME_OUT_CONN 	= 6000;
	private static final int HTTP_TIME_OUT_SOCKET 	= 7000;

	String encodigFrom	= "iso-8859-1";
	String encodingTo 	= "utf-8";

	private final String TAG = "Network";
    private Context ctx;

    public Network(Context ctx){
        this.ctx = ctx;
    }

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	/**
	 * Realiza una peticion GET hacia la direccion que se indique
	 * @param reqtUrl, URL para hacer el request
	 * @return String con la respuesta de la petición
	 * @throws NetworkException en caso de algun error
	 */
	public String doGetJson(String reqtUrl) throws NetworkException, NoInternetException{
        if(!isOnline()){
            throw new NoInternetException("No hay internet");
        }
		String path =  reqtUrl;
		long init = System.currentTimeMillis();
		String res = getRequest(path);
		long total = System.currentTimeMillis() - init;
		Log.d(TAG,"Tiempo de carga: " + total + "ms");
		System.out.println(res);
		try {
			res = new String(res.getBytes(encodigFrom), encodingTo);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(res);
		return res;
	}


	/**
	 * Realiza una peticion para descargar un archivo
	 * @param reqtUrl URL para realizar la petici�n
	 * @return Arreglo de bytes con el archivo descargado
	 * @throws NetworkException
	 */
	public byte[] getBytes(String reqtUrl) throws NetworkException, NoInternetException {
        if(!isOnline()){
            throw new NoInternetException("No hay internet");
        }
		String path =  reqtUrl; 

		byte[] res;
		URL url;
		try {
			url = new URL(path);

			URLConnection ucon = url.openConnection();
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			ByteArrayOutputStream baf = new ByteArrayOutputStream(1 * 1024);
			byte[] buf = new byte[512];

			int len;
			while( (len = bis.read(buf)) != -1){
				baf.write(buf, 0, len);
				Log.i(TAG, "downloaded bytes:" + baf.size());
			}
			res = baf.toByteArray();
			return res;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new NetworkException(e.getMessage(), e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new NetworkException(e.getMessage(), e);
		}
	}


	/**
	 * Realiza una peticion POST al servidor
	 * @param path, URL para hacer el post
	 * @param nameValuePairs, valores llave - valor que ser�n env�ados en el post
	 * @return String, con la respuesta de la petici�n
	 * @throws NetworkException
	 */
	public String doPostJson(String path, List<NameValuePair> nameValuePairs) throws NetworkException, NoInternetException {
        if(!isOnline()){
            throw new NoInternetException("No hay internet");
        }
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 

		HttpContext localContext = new BasicHttpContext();
		HttpClient client = new DefaultHttpClient();  
		HttpPost post = new HttpPost( path ); 

		//post.setHeader("content-type", "text/plain");
		//post.setHeader("Accept", "text/html");


		try {
			//nameValuePairs.add(new BasicNameValuePair("name", "value"));

			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            long initTime = System.currentTimeMillis();
			HttpResponse response = client.execute(post, localContext);
            long finishTime = System.currentTimeMillis() - initTime;
            Log.d(TAG,"Tiempo de respuesta del post " + finishTime + " ms");

			String jsonResponse = EntityUtils.toString(response.getEntity()); 

			//Verifica la respuesta
			if(response.getStatusLine().getStatusCode() != 200){
				throw new NetworkException("Error de servidor " + response.getStatusLine().getStatusCode() + " " + jsonResponse);
			}

			System.out.println(jsonResponse);
			return jsonResponse;
		} catch (Exception e) {
			e.printStackTrace();
			throw new NetworkException(e.getMessage(),e);
		}
	}




	//-----------------------------------------------------------------

	private String getRequest(String path) throws NetworkException, NoInternetException {
        if(!isOnline()){
            throw new NoInternetException("No hay internet");
        }
		Log.v(TAG,"Realizando petición GET " + path);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 

		HttpContext localContext = new BasicHttpContext();

		HttpGet request = new HttpGet( path ); 
		String urlString = request.getURI().toString();
		System.out.println(urlString);
		request.setHeader("Content-type", "application/html");

		//Time outs
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = HTTP_TIME_OUT_CONN;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = HTTP_TIME_OUT_SOCKET;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpClient client = new DefaultHttpClient(httpParameters);
		try{
			HttpResponse response = client.execute(request,localContext);

			String jsonResponse = EntityUtils.toString(response.getEntity()); 

			//Verifica la respuesta
			if(response.getStatusLine().getStatusCode() != 200){
				throw new NetworkException("Error de servidor " + response.getStatusLine().getStatusCode() + " " + jsonResponse);
			}

			System.out.println("Response: " + jsonResponse);
			return jsonResponse;
		}catch(Exception e){
			throw new NetworkException(e.getMessage(),e);
		}
	}

	/**
	 * M�todo que retorna una imagen a partir de una URL
	 * @param url URL para la descarga
	 * @return, bitmap con la imagen
	 */
	public Bitmap downloadBitmap(String url) throws NoInternetException {
        if(!isOnline()){
            throw new NoInternetException("No hay internet");
        }


		Log.d(TAG, url);

		HttpURLConnection urlConnection = null;
		try {
			URL uri = new URL(url);
			urlConnection = (HttpURLConnection) uri.openConnection();
			int statusCode = urlConnection.getResponseCode();
			if (statusCode != HttpStatus.SC_OK) {
				return null;
			}

			InputStream inputStream = urlConnection.getInputStream();
			if (inputStream != null) {
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(urlConnection != null){
				urlConnection.disconnect();
			}
			Log.w("ImageDownloader", "Error downloading image from " + url);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return null;
	}
}
