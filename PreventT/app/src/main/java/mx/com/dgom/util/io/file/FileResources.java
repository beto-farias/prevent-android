package mx.com.dgom.util.io.file;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileResources {
	
	/**
	 * @return Number of bytes available on external storage
	 */
	public static long getExternalStorageAvailableSpace() {
		long availableSpace = -1L;
		try {
			StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
			statFs.restat(Environment.getExternalStorageDirectory().getPath());
			availableSpace = (long) statFs.getAvailableBlocks() * (long) statFs.getBlockSize();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return availableSpace;
	}
	
	
	public static boolean fileExistsInExternalStorage(String fileName) {
		File extStore = Environment.getExternalStorageDirectory();
		File file = new File(extStore,   fileName);
		return file.exists();
	}


	public static Bitmap loadBitmapFromExternalStorage(String fileName){
		File extStore = Environment.getExternalStorageDirectory();
		File file = new File(extStore, fileName);

		if(!file.exists()){
			return null;
		}
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	public static Bitmap loadBitmapFromPath(String fileName){
		//File extStore = Environment.getExternalStorageDirectory();
		File file = new File(fileName);

		if(!file.exists()){
			return null;
		}
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static byte[] loadBitmapBytesFromExternalStorage(String filename ){
		return loadBitmapBytesFromExternalStorage(filename, 50);
	}
	
	public static byte[] loadBitmapBytesFromExternalStorage(String filename,int quality ){
		Bitmap bm = loadBitmapFromExternalStorage(filename);
		
		bm = Bitmap.createScaledBitmap(bm, bm.getWidth()/8, bm.getHeight()/16, false);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bm.compress(Bitmap.CompressFormat.PNG, quality, baos); //bm is the bitmap object   
		byte[] b = baos.toByteArray();
		return b;
	}
	
	public static void copyFile(String src, String dst) throws IOException {
		File origen = new File(src);
		File destino = new File(dst);
		copyFile(origen, destino);
	}
	
	public static String saveBitmapExternalStorage(String folder, String fileName, Bitmap bmp) {
		try {
			File extStore = Environment.getExternalStorageDirectory();
			String fileSaveAs =  folder ;

            //Crea los directorios
            File file = new File(extStore,fileSaveAs);
            file.mkdirs();

            //Guarda el archivo
            fileSaveAs =  folder + "/" +  fileName;
            file = new File(extStore,fileSaveAs);



			FileOutputStream out = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
			return fileSaveAs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void saveFileExternalStorage(String fileName,String folder, byte[] baf) throws Exception{
		File extStore = Environment.getExternalStorageDirectory();
		File file = new File(extStore, folder);
		
		//Si no existe el dir lo crea
		if(!file.exists()){
			file.mkdirs();
		}
		
		file = new File(extStore, folder + "/" + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(baf);
		fos.close();
	}


	private static void copyFile(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		
		File path = new File(dst.getParent());
		if(!path.exists()){
			path.mkdirs();
		}
		
	    OutputStream out = new FileOutputStream(dst);
	
	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	}
	
}
