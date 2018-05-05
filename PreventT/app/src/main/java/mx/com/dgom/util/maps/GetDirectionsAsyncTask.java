package mx.com.dgom.util.maps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Map;

import mx.com.dgom.sercco.android.prevent.act.R;

public class GetDirectionsAsyncTask extends AsyncTask<Map<String, String>, Object, ArrayList<LatLng>>
{
	public static final String USER_CURRENT_LAT = "user_current_lat";
	public static final String USER_CURRENT_LONG = "user_current_long";
	public static final String DESTINATION_LAT = "destination_lat";
	public static final String DESTINATION_LONG = "destination_long";
	public static final String DIRECTIONS_MODE = "directions_mode";
	private Activity activity;
	private Exception exception;
	private ProgressDialog progressDialog;

	public GetDirectionsAsyncTask(Activity activity)
	{
		super();
		this.activity = activity;
	}

	public void onPreExecute()
	{
		progressDialog = new ProgressDialog(activity);
		progressDialog.setMessage("Calculating directions");
		progressDialog.show();
	}

	
	@Override
	public void onPostExecute(ArrayList<LatLng> result)
	{
		progressDialog.dismiss();
		if (exception == null){
			//FIXME
			//activity.handleGetDirectionsResult(result);
		}
		else{
			processException();
		}
	}

	@Override
	protected ArrayList<LatLng> doInBackground(Map<String, String>... params)
	{
		Map<String, String> paramMap = params[0];
		try
		{
			LatLng fromPosition = new LatLng(Double.valueOf(paramMap.get(USER_CURRENT_LAT)) , Double.valueOf(paramMap.get(USER_CURRENT_LONG)));
			LatLng toPosition = new LatLng(Double.valueOf(paramMap.get(DESTINATION_LAT)) , Double.valueOf(paramMap.get(DESTINATION_LONG)));
			GMapV2Direction md = new GMapV2Direction();
			Document doc = md.getDocument(fromPosition, toPosition, paramMap.get(DIRECTIONS_MODE));
			ArrayList<LatLng> directionPoints = md.getDirection(doc);
			
			int distance = md.getDistanceValue(doc);
			md.getDistanceText(doc);
			md.getDurationValue(doc);
			md.getDurationText(doc);
			
			return directionPoints;
		}
		catch (Exception e)
		{
			exception = e;
			return null;
		}
	}

	private void processException()
	{
		Toast.makeText(activity, activity.getString(R.string.error_when_retrieving_data), Toast.LENGTH_LONG).show();
	}
}
