package mx.com.dgom.sercco.android.prevent.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import mx.com.dgom.sercco.android.prevent.Controller;
import mx.com.dgom.sercco.android.prevent.act.PictureGalleryActivity;
import mx.com.dgom.sercco.android.prevent.act.R;
import mx.com.dgom.sercco.android.prevent.to.MultimediaTO;
import mx.com.dgom.util.io.images.BitmapUtils;
import mx.com.dgom.util.io.net.NoInternetException;

/**
 * Created by beto on 12/21/15.
 */
public class GaleryImageAdapter extends BaseAdapter {

    private final String TAG = "GaleryImageAdapter";

    private PictureGalleryActivity mContext;
    private List<MultimediaTO> data = new ArrayList<MultimediaTO>();



    public List<MultimediaTO> getData() {
        return data;
    }

    public void clearData(){
        data.clear();
    }


    public GaleryImageAdapter(PictureGalleryActivity c) {
        mContext = c;
    }

    public void addItem(MultimediaTO item) {
        data.add(item);
        this.notifyDataSetChanged();
    }

    public void addItem(MultimediaTO[] items) {
        data.addAll(Arrays.asList(items));
        this.notifyDataSetChanged();
    }

    public void removeItem(MultimediaTO item){
        data.remove(item);
        this.notifyDataSetChanged();

        if(data.size() == 0) {
            mContext.updateGUI();
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MultimediaTO item = (MultimediaTO) getItem(position);
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            int screenWidth = metrics.widthPixels;
            int screenHeight = metrics.heightPixels;

            int pictureSize;

            pictureSize = (screenWidth/2);

            imageView = new ImageView(mContext);
            imageView.setBackgroundResource(R.color.black);
            imageView.setLayoutParams(new GridView.LayoutParams(pictureSize, pictureSize));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(item.getTumnailBitmap(mContext));


        //Detector de gestos
        final GestureDetector gdt = new GestureDetector(new GestureListener(item));

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gdt.onTouchEvent(motionEvent);
                return true;
            }
        });


        return imageView;
    }


    //===================== CLASE QUE MANIPULA LOS GESTOS ==============================

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        MultimediaTO mto;

        public GestureListener(MultimediaTO mto){
            this.mto = mto;
        }


        @Override
        public void onLongPress(MotionEvent e) {
            Log.d(TAG, "onLongPress");
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            removeItem(mto);

            Log.d(TAG, "Double TAP");
            return true;

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

           /*
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Right to left
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Left to right
            }

            */

            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                //return false; // Bottom to top
            }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {

                Log.d(TAG, "Slide down picture");


                return false; // Top to bottom
            }
            return false;
        }
    }

}
