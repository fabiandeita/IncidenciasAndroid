package mx.com.tecnologiadenegocios.curso2017;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
/*import android.widget.GridView;*/
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mx.com.tecnologiadenegocios.curso2017.R;
import mx.com.tecnologiadenegocios.curso2017.bean.ImageAdapter;

/**
 * Created by Ing.Sergio Martínez on 22/06/2015.
 */
public class Galeria extends ActionBarActivity {
    Button botonTomarFoto, btnAgregarFoto;
    final static int cons = 0;
    Bitmap foto;
 /*   private GridView gridView;*/
    ImageView imageView;
    private int fuente = 0;//o Camara
    private String name = Environment.getExternalStorageDirectory() + "/test.jpg";
    private Uri outputFileUri;
    private String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private File photoFile;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap bitmap;
    private ImageAdapter imageAdapter;
    private ArrayList<String> rutaImages = new ArrayList<>();
    private Gallery galeria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);
        botonTomarFoto = (Button)findViewById(R.id.botonTomtaFoto);
        btnAgregarFoto = (Button)findViewById(R.id.btnAgregarFoto);

    /*    gridView = (GridView)findViewById(R.id.gridView);*/
        galeria = (Gallery)findViewById(R.id.gallery);
        imageAdapter = new ImageAdapter(getApplicationContext());
   /*     gridView.setAdapter(imageAdapter);*/
        galeria.setAdapter(imageAdapter);

        loadButtonOnClicListener();
        if(!permisos())
            Toast.makeText(getApplicationContext(), "No se ha detectado tarjeta SD, ingrese una.", Toast.LENGTH_SHORT).show();

        //Bundle MBuddle = this.getIntent().getExtras().getStringArrayList();
        //MBuddle.getStringArrayList("rutaImagenes");
        if(this.getIntent().getExtras().containsKey("rutaImagenes"))
            for(String dir : this.getIntent().getExtras().getStringArrayList("rutaImagenes") ){
                rutaImages.add(dir);
                imageAdapter.setRutaImagenes(rutaImages);
               /* gridView.setAdapter(imageAdapter);*/
                galeria.setAdapter(imageAdapter);
            }
    }

    public boolean permisos(){
        //Comprobamos el estado de la memoria externa (tarjeta SD)
        String estado = Environment.getExternalStorageState();
        boolean sdDisponible = false;
        boolean sdAccesoEscritura = false;
        if (estado.equals(Environment.MEDIA_MOUNTED))
        {
            sdDisponible = true;
            sdAccesoEscritura = true;
        }
        else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            sdDisponible = true;
            sdAccesoEscritura = false;
            return false;
        }
        else
        {
            sdDisponible = false;
            sdAccesoEscritura = false;
            return false;
        }
        try {
            File imagesFolder = new File(Environment.getExternalStorageDirectory(), "ImagenesIncidencia");
            imagesFolder.mkdirs();
            if (imagesFolder.isDirectory()) {
                File foto = new File(imagesFolder, "myPicName.jpg");
                outputFileUri = Uri.fromFile(foto);
            }
            else
                return false;
            return true;
        }catch(Exception e) {

            return false;
        }
    }
    public void loadButtonOnClicListener(){
        botonTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fuente = 0;
                dispatchTakePictureIntent();
            }
        });

        btnAgregarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fuente = 1;
                Intent capture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(capture, cons);
            }
        });

        //Boton Reportes
        findViewById(R.id.btnRegresar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("rutaImagenes",rutaImages);
                if (getParent() == null) {
                    setResult(Activity.RESULT_OK, data);
                } else {
                    getParent().setResult(Activity.RESULT_OK,data);
                }
                finish();

            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException{
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory(), "ImagenesIncidencia");
        //File storageDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(fuente == 0) {
                if (data != null) {
                    if (data.hasExtra("data")) {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imageView.setImageBitmap(imageBitmap);
                    }
                }else{
                    //Contiene la ruta de la ultima imagen
                    //photoFile.getAbsolutePath()
                    rutaImages.add(photoFile.getAbsolutePath());
                    imageAdapter.setRutaImagenes(rutaImages);
             /*       gridView.setAdapter(imageAdapter);*/
                    galeria.setAdapter(imageAdapter);
                    photoFile = null;
                }
            }else if(fuente == 1){
                Uri selectedImage = data.getData();
                //Toast.makeText(getApplicationContext(), getPath(selectedImage), Toast.LENGTH_SHORT).show();
                rutaImages.add(getPath(selectedImage));
                imageAdapter.setRutaImagenes(rutaImages);
               /* gridView.setAdapter(imageAdapter);*/
                galeria.setAdapter(imageAdapter);
            }
        }
        //galleryAddPic();
    }


    @Override
    public void onBackPressed() {
    }

    private String getPath(Uri uri) {
        String[] projection = { android.provider.MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.help) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + getFilesDir() + "/manual.pdf"),
                    "application/pdf");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);

    }




}