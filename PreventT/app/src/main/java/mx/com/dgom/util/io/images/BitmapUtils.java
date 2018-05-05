package mx.com.dgom.util.io.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class BitmapUtils {

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0)
            return 1;
        else
            return k;
    }


    /**
     * Carga una imagen en base 64
     *
     * @param uri
     * @param context
     * @return
     */
    public static String loadBitmapB64(Uri uri, Context context, int maxSize) throws IOException {
        byte[] data = loadBitmap2ArrayJPEG(uri, context, maxSize);
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;
    }

    public static byte[] loadBitmap2ArrayJPEG(Uri uri, Context context, int maxSize) throws FileNotFoundException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap;
        if (maxSize == -1) {
            bitmap = getBitmap(uri, context);
        } else {
            bitmap = getReducedBitmap(uri, context, maxSize);
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }


    public static Bitmap getBitmap(Uri uri, Context context) throws FileNotFoundException, IOException {
        InputStream input = context.getContentResolver().openInputStream(uri);

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();


        //Carga la imagen
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }


    /**
     * Recupera una imagen a un tamaño menor, tamaño de 350 pixeles
     *
     * @param uri
     * @param context
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Bitmap getReducedBitmap(Uri uri, Context context) throws FileNotFoundException, IOException {
        return getReducedBitmap(uri, context, 350);
    }

    /**
     * Recuoera un bitmap pero de menor tamaño
     *
     * @param uri
     * @param context
     * @param maxSize
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Bitmap getReducedBitmap(Uri uri, Context context, int maxSize) throws FileNotFoundException, IOException {

        //Detalles
        InputStream input = context.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;// optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional

        //Carga la imagen
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();


        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        //Determina si el alto es mayor o el ancho es mayor
        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        //Calcula la proporcion a reducir para la imagen correspondiente a 350 pixeles
        double ratio = (originalSize > maxSize) ? (originalSize / maxSize) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;// optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional

        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    /**
     * @param origenBitmap
     * @param degrees
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap origenBitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap rotatedBitmap = Bitmap.createBitmap(origenBitmap, 0, 0, origenBitmap.getWidth(), origenBitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }


    /**
     * Recupera un thumnail a partir de un video en internet
     *
     * @param videoPath
     * @return
     */
    public static Bitmap retriveVideoFrameFromVideo(String videoPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;

        mediaMetadataRetriever = new MediaMetadataRetriever();
        if (Build.VERSION.SDK_INT >= 14)
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
        else
            mediaMetadataRetriever.setDataSource(videoPath);
        bitmap = mediaMetadataRetriever.getFrameAtTime();

        if (mediaMetadataRetriever != null) {
            mediaMetadataRetriever.release();
        }

        return bitmap;
    }

}
