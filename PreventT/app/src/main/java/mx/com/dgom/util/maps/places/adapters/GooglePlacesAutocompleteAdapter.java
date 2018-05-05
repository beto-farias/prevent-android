package mx.com.dgom.util.maps.places.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import mx.com.dgom.util.maps.places.PlaceTO;
import mx.com.dgom.util.maps.places.PlacesUtil;

/**
 * Created by Beto on 23/09/2015.
 */
public class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
    private ArrayList<PlaceTO> resultList;

    private LayoutInflater mInflater;
    private Context mContext;
    private int resource;
    private int mFieldId = 0;

    //public ArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
    public GooglePlacesAutocompleteAdapter(Context context, int resource, int textViewResourceId) {
        super(context, textViewResourceId);
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.mFieldId = textViewResourceId;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public PlaceTO getItem(int index) {
        return resultList.get(index);
    }

    //Control de tiempo para las tecleadas
    //http://stackoverflow.com/questions/13193990/android-howto-send-requests-to-the-google-api-when-a-user-pauses-typing

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(final CharSequence constraint) {
                final FilterResults filterResults = new FilterResults();
                // Retrieve the autocomplete results.
                resultList = PlacesUtil.autocomplete(constraint.toString());
                // Assign the data to the FilterResults
                filterResults.values = resultList;
                filterResults.count = resultList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }




    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        PlaceTO item = getItem(position);
        text.setText((CharSequence)item.getName());
        return view;
    }
}
