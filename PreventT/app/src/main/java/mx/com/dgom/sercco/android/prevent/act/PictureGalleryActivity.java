package mx.com.dgom.sercco.android.prevent.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import mx.com.dgom.sercco.android.prevent.adapter.GaleryImageAdapter;
import mx.com.dgom.sercco.android.prevent.to.MultimediaTO;
import mx.com.dgom.util.io.images.BitmapUtils;

public class PictureGalleryActivity extends AppCompatActivity {

    private static final String TAG = "PictureGalleryActivity";

    private final int SELECT_PHOTO = 1;

    private Toolbar toolbar;
    private GridView gridview;
    private TextView txtNoPhotoSelected;

    public static GaleryImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_gallery);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        gridview = (GridView) findViewById(R.id.grid_fotos);
        txtNoPhotoSelected  = (TextView) findViewById(R.id.txt_no_photo_selected);

        if(adapter == null) {
            adapter = new GaleryImageAdapter(this);
        }
        gridview.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateGUI();
    }


    public void updateGUI(){
        gridview.setVisibility(View.GONE);
        txtNoPhotoSelected.setVisibility(View.GONE);

        if(adapter.getCount() == 0){
            txtNoPhotoSelected.setVisibility(View.VISIBLE);
        }else{
            gridview.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {

                        final Uri imageUri = imageReturnedIntent.getData();

                        MultimediaTO multimediaTO = new MultimediaTO(imageUri);
                        adapter.addItem(multimediaTO);

                }
        }
    }


    public void addPictureAction(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }







}
