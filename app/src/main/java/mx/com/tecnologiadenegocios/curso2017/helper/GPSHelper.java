package mx.com.tecnologiadenegocios.curso2017.helper;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import mx.com.tecnologiadenegocios.curso2017.R;

/**
 * Created by Ing.Sergio Martínez on 22/06/2015.
 */
public class GPSHelper extends ActionBarActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta);

    }
    public class MiLocationListener implements LocationListener
    {
        public void onLocationChanged(Location loc)
        {
            loc.getLatitude();
            loc.getLongitude();
            String coordenadas = "Mis coordenadas son: Latitud = " + loc.getLatitude() + " Longitud = " + loc.getLongitude();
            Toast.makeText(getApplicationContext(), coordenadas, Toast.LENGTH_LONG).show();
        }
        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(),"Gps Desactivado",Toast.LENGTH_SHORT ).show();
        }
        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(),"Gps Activo",Toast.LENGTH_SHORT ).show();
        }
        public void onStatusChanged(String provider, int status, Bundle extras){}
    }
}

