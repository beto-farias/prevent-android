package mx.com.dgom.sercco.android.prevent.act;





import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mx.com.dgom.sercco.android.prevent.Constantes;
import mx.com.dgom.sercco.android.prevent.adapter.PictureSliderPageAdapter;
import mx.com.dgom.sercco.android.prevent.to.DelitoMultimediaTO;
import mx.com.dgom.sercco.android.prevent.to.MultimediaTO;
import mx.com.dgom.util.io.file.FileResources;
import mx.com.dgom.util.io.images.BitmapUtils;

public class PictureSliderActivity extends Activity {

    private final String  TAG = "PictureSliderActivity";

    private PictureSliderPageAdapter adapter;
    private ViewPager mViewPager;
    private int actualAngle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_picture_slider);

        //Intent intent = getIntent();


        adapter = new PictureSliderPageAdapter(this,HomeActivity.delitoMostradoDetalles.getId_num_delito(), HomeActivity.delitoMostradoDetalles.getId_evento());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                actualAngle = 0;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Asigna solamente las imagenes las imagenes
        List<DelitoMultimediaTO> data = new ArrayList<DelitoMultimediaTO>();
        DelitoMultimediaTO to;
        for(int i=0; i < HomeActivity.delitoMostradoDetalles.getMultimedia().length; i++){
            to = HomeActivity.delitoMostradoDetalles.getMultimedia()[i];

            if(to.getId_tipo_multimedia() == Constantes.TIPO_MULTIMEDIA_FOTO || to.getId_tipo_multimedia() == Constantes.TIPO_MULTIMEDIA_VIDEO){
                data.add(to);
            }
        }

        //Transforma la lista en un arreglo
        DelitoMultimediaTO[] arr =  data.toArray(new DelitoMultimediaTO[data.size()]);
        adapter.addItem(arr);
    }



    public void rotateLeftAction(View v){
        Log.d(TAG, "rotate left " + actualAngle);

        ImageView imageView = (ImageView)  mViewPager.findViewWithTag("photo_tag" + mViewPager.getCurrentItem());
        actualAngle = actualAngle -90;
        int pivotX = imageView.getDrawable().getBounds().width()/2;
        int pivotY = imageView.getDrawable().getBounds().height()/2;
        Matrix matrix = new Matrix();
        imageView.setScaleType(ImageView.ScaleType.MATRIX);   //required
        matrix.postRotate((float) actualAngle, pivotX, pivotY);
        imageView.setImageMatrix(matrix);
        //imageView.setScaleType(ImageView.ScaleType.CENTER);
    }

    public void rotateRightAction(View v){
        Log.d(TAG, "rotate Right " + actualAngle);
        ImageView imageView = (ImageView)  mViewPager.findViewWithTag("photo_tag" + mViewPager.getCurrentItem());


        actualAngle = actualAngle + 90;
        int pivotX = imageView.getDrawable().getBounds().width()/2;
        int pivotY = imageView.getDrawable().getBounds().height()/2;
        Matrix matrix = new Matrix();
        imageView.setScaleType(ImageView.ScaleType.MATRIX);   //required
        matrix.postRotate((float) actualAngle, pivotX, pivotY);
        imageView.setImageMatrix(matrix);
        //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    public void saveImageAction(View v){
        Log.d(TAG, "rotate Right");
        Toast.makeText(this,R.string.guardando_archivo,Toast.LENGTH_SHORT).show();
        new SaveFileTask().execute(null);

    }


    class SaveFileTask extends AsyncTask{

        Bitmap bm;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ImageView imageView = (ImageView) mViewPager.findViewWithTag("photo_tag" + mViewPager.getCurrentItem());
            imageView.buildDrawingCache();
            bm = imageView.getDrawingCache();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            String filename = UUID.randomUUID().toString() + ".jpg";
            FileResources.saveBitmapExternalStorage("prevenT", filename, bm);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText(getBaseContext(),R.string.archivo_guardado,Toast.LENGTH_SHORT).show();
        }
    }
}
