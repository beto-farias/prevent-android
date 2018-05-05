package mx.com.dgom.sercco.android.prevent.act;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;

import mx.com.dgom.sercco.android.prevent.Controller;
import mx.com.dgom.sercco.android.prevent.GUIUtils;
import mx.com.dgom.sercco.android.prevent.adapter.TimelineListAdapter;
import mx.com.dgom.sercco.android.prevent.to.DelitoTO;
import mx.com.dgom.util.io.net.NoInternetException;

public class TimeLineActivity extends AppCompatActivity {
    private final String TAG = "TimeLineActivity";

    private ListView listDataDelitos;
    private int index = 0;
    TimelineListAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        listDataDelitos = (ListView) findViewById(R.id.list_data_delitos);
        listDataDelitos.setDivider(null);
        listDataDelitos.setDividerHeight(0);

        adapter = new TimelineListAdapter(this);
        listDataDelitos.setAdapter(adapter);


        listDataDelitos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
                DelitoTO res = (DelitoTO)adapter.getItem(position);

                Intent resultData = new Intent();
                resultData.putExtra("LATITUDE", res.getNum_latitud());
                resultData.putExtra("LONGITUDE", res.getNum_longitud());
                resultData.putExtra("TIPO_DELITO", res.getId_tipo_delito());
                resultData.putExtra("ID_EVENTO", res.getId_evento());
                resultData.putExtra("ID_NUM_DELITO", res.getId_num_delito());
                setResult(Activity.RESULT_OK, resultData);
                finish();
                overridePendingTransition(R.anim.flip_middle_in, R.anim.flip_middle_out);

            }
        });

        listDataDelitos.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    if (!working) { //Si ni está solicitando datos, pide más datos
                        new LoadDelitosTimeLineTask().execute(lastItem);
                    }
                }
            }
        });

        //new LoadDelitosTimeLineTask().execute(index);
    }


    public void addDelitos(DelitoTO[] delitos) {
        if (delitos != null) {
            adapter.addDelito(Arrays.asList(delitos));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean working = false;





    //-------------- CLASE PARA CARGAR DELITOS TIMELINE -------------

    class LoadDelitosTimeLineTask extends AsyncTask<Integer, Integer, DelitoTO[]> {
        private final String TAG = "LoadPinesTask";

        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            working = true;

            pd = new ProgressDialog(TimeLineActivity.this);
            pd.setTitle("Cargando datos");
            pd.setMessage(getString(R.string.espere_cargando_delitos));
            pd.show();
        }

        @Override
        protected DelitoTO[] doInBackground(Integer... integers) {

            Log.d(TAG, "Cargando delitos index " + integers[0]);
            Controller controller = new Controller(getBaseContext());
            DelitoTO[] delitos = new DelitoTO[0];
            try {
                delitos = controller.getDelitosTimeLine(integers[0]);
            } catch (NoInternetException e) {
                e.printStackTrace();
                GUIUtils.showNoInternetAccessToast(TimeLineActivity.this);
                return null;
            }
            return delitos;
        }

        @Override
        protected void onPostExecute(DelitoTO[] delitos) {
            super.onPostExecute(delitos);
            if (pd != null) {
                pd.dismiss();
            }

            if (delitos != null && delitos.length > 0) {
                addDelitos(delitos);
            }

            working = false;
        }
    }//Cierra clase
}
