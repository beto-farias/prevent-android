package mx.com.dgom.sercco.android.prevent.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mx.com.dgom.sercco.android.prevent.AppNetwork;
import mx.com.dgom.sercco.android.prevent.Constantes;
import mx.com.dgom.sercco.android.prevent.Controller;
import mx.com.dgom.sercco.android.prevent.act.R;
import mx.com.dgom.sercco.android.prevent.act.VideoPlayerActivity;
import mx.com.dgom.sercco.android.prevent.to.DelitoMultimediaTO;
import mx.com.dgom.util.io.net.NoInternetException;

/**
 * Created by beto on 12/23/15.
 */
public class PictureSliderPageAdapter extends PagerAdapter {


    private static final String TAG = "PictureSliderPageAdapte";

    Context mContext;
    LayoutInflater mLayoutInflater;
    private List<DelitoMultimediaTO> data = new ArrayList<DelitoMultimediaTO>();
    Controller controller ;
    long numDelito;
    long ide;


    public PictureSliderPageAdapter(Context context, long numDelito, long ide) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Pone los datos del las variables de session
        //addItem(SessionVars.multimediaList);

        this.numDelito = numDelito;
        this.ide = ide;
        controller = new Controller(context);
    }

    public void addItem(DelitoMultimediaTO item) {
        data.add(item);
        this.notifyDataSetChanged();
    }

    public void addItem(DelitoMultimediaTO[] items) {
        data.addAll(Arrays.asList(items));
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final DelitoMultimediaTO item = data.get(position);

        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        container.addView(itemView);

        //Obtiene el image view del layout
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);


        //Pone foto temporal
       // imageView.setImageResource(R.drawable.p_bg_temp_photo);
        imageView.setTag("photo_tag" + position);

        switch ((int)item.getId_tipo_multimedia()){
            case Constantes.TIPO_MULTIMEDIA_FOTO:

                //new LoadPictureTask(imageView).execute(item.getTxt_archivo());

                String url = AppNetwork.END_PONT_DELITOS_IMAGENES + numDelito +  ide + "/" + item.getTxt_archivo();

                Log.d(TAG,url);



                Picasso.with(this.mContext)
                        .load(url)
                        .placeholder(R.drawable.progress_animation)
                        //.centerCrop()
                        .into(imageView);


                imageView.setOnClickListener(null);
                //imageView.setImageResource(R.drawable.p_bg_temp_photo);

                break;

            case Constantes.TIPO_MULTIMEDIA_VIDEO:
                new LoadVideoThumnailTask(imageView).execute(item.getTxt_archivo());

                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setImageResource(R.drawable.p_icon_bg_video);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = AppNetwork.END_PONT_DELITOS_IMAGENES + numDelito +  ide + "/" + item.getTxt_archivo();

                        Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                        intent.putExtra(Constantes.VIDEO_URL,url);
                        mContext.startActivity(intent);
                    }
                });
                break;
        }

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }


    //------------------------------------------------------------------------------------
    class LoadPictureTask extends AsyncTask<String, String, Bitmap>{

        private ImageView iv;

        public LoadPictureTask(ImageView iv){
            this.iv = iv;
        }

        @Override
        protected Bitmap doInBackground(String[] fileName) {
            try {
                Bitmap res = controller.getDelitoPictureBitmap(fileName[0], numDelito, ide);
                return res;
            } catch (NoInternetException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap != null)
                iv.setImageBitmap(bitmap);
        }
    }

    //------------------------------------------------------------------------------------
    class LoadVideoThumnailTask extends AsyncTask<String, String, Bitmap>{

        private ImageView iv;

        public LoadVideoThumnailTask(ImageView iv){
            this.iv = iv;
        }

        @Override
        protected Bitmap doInBackground(String[] fileName) {
            try {
                Bitmap res = controller.retriveVideoFrameFromVideo(fileName[0], numDelito, ide);//
                System.out.println(res);
                return res;
            } catch (NoInternetException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap != null)
                iv.setImageBitmap(bitmap);
        }



    }
}

