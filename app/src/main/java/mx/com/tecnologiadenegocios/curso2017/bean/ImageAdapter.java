package mx.com.tecnologiadenegocios.curso2017.bean;

/**
 * Created by Ing.Sergio Martínez on 22/06/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    int mGalleryItemBackground;
    private Context mContext;
    File[] images;
    File[] files;



    private List rutaImagenes = new ArrayList();
    public ImageAdapter(Context c) {
        mContext = c;

    }
    public int getCount() {
        return rutaImagenes.size();
    }
    public Object getItem(int position) {
        return images[position].getAbsolutePath();
    }
    public long getItemId(int position) {
        return position;
    }
    public String getAlbumName(int folderID) {
        return files[folderID].getName();
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = new ImageView(mContext);
        int targetW = 150;
        int targetH = 100;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        //BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 5;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(rutaImagenes.get(position).toString(), bmOptions);
        //imageView.setImageURI(outputFileUri);

        imageView.setImageBitmap(bitmap);
        return imageView;

    }

    public List getRutaImagenes() {
        return rutaImagenes;
    }

    public void setRutaImagenes(List rutaImagenes) {
        this.rutaImagenes = rutaImagenes;
    }
}
