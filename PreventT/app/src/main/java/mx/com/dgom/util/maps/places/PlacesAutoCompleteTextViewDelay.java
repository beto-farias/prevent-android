//http://stackoverflow.com/questions/13193990/android-howto-send-requests-to-the-google-api-when-a-user-pauses-typing

package mx.com.dgom.util.maps.places;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by Beto on 23/09/2015.
 */
public class PlacesAutoCompleteTextViewDelay extends AutoCompleteTextView {

    // initialization
    int threshold;
    int delay = 750;
    Handler handler = new Handler();
    Runnable run;

    // constructor
    public PlacesAutoCompleteTextViewDelay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        // get threshold
        threshold = this.getThreshold();

        // perform filter on null to hide dropdown
        doFiltering(null, keyCode);

        // stop execution of previous handler
        handler.removeCallbacks(run);

        // creation of new runnable and prevent filtering of texts which length
        // does not meet threshold
        run = new Runnable() {
            public void run() {
                if (text.length() > threshold) {
                    doFiltering(text, keyCode);
                }
            }
        };

        // restart handler
        handler.postDelayed(run, delay);
    }

    // starts the actual filtering
    private void doFiltering(CharSequence text, int keyCode) {
        super.performFiltering(text, keyCode);
    }

}
