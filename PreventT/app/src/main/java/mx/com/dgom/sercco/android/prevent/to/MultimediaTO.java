package mx.com.dgom.sercco.android.prevent.to;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.IOException;
import java.net.URI;

import mx.com.dgom.util.io.images.BitmapUtils;

/**
 * Created by beto on 12/23/15.
 */
public class MultimediaTO {

    private Uri imageUri;
    private Bitmap thumnail;


    public boolean equals(Object o){
        if(!( o instanceof MultimediaTO)){
            return false;
        }

        MultimediaTO m = (MultimediaTO) o;
        return m.imageUri == imageUri;
    }

    public MultimediaTO(Uri uri){
        this.imageUri = uri;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }


    public Bitmap getTumnailBitmap(Context ctx){

        if(thumnail != null)
            return thumnail;

        try {
            thumnail = BitmapUtils.getReducedBitmap(imageUri, ctx, 400);
            return thumnail;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getBase64(Context ctx) {
        try {
            thumnail = null;
            return BitmapUtils.loadBitmapB64(imageUri, ctx, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
