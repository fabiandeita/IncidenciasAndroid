package mx.com.tecnologiadenegocios.curso2017;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import mx.com.tecnologiadenegocios.curso2017.model.Emergencia;
import mx.com.tecnologiadenegocios.curso2017.R;
import mx.com.tecnologiadenegocios.curso2017.helper.BDHelper;

/**
 * Created by Ing.Sergio Martínez on 22/06/2015.
 */
public class Mapa extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private List<Emergencia> listaEmergencias = new ArrayList<>();
    private BDHelper bdHelper;
    private CameraUpdate mCamera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        setUpMapIfNeeded();
        loadBotones();
    }


    public void loadBotones(){

        //boton inicio
        findViewById(R.id.btnInicio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Mapa.this, Inicio.class);
                startActivity(altaForm);
                //Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();

            }
        });

        //boton alta
        findViewById(R.id.btnAlta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Mapa.this, Alta.class);
                startActivity(altaForm);
                // Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();

            }
        });

        //Boton actualizacion
        findViewById(R.id.btnActualizacion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Mapa.this, Actualizacion.class);
                startActivity(altaForm);


            }
        });



        //Boton Reportes
        findViewById(R.id.btnReportes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Mapa.this, Reportes.class);
                startActivity(altaForm);

            }
        });



        //Boton Confguracion
        findViewById(R.id.btnConfiguracion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Mapa.this, Configuracion.class);
                startActivity(altaForm);

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //Toast.makeText(getApplicationContext(), "Inicia carga", Toast.LENGTH_SHORT).show();
        bdHelper = new BDHelper(this);
        for(Emergencia eme : bdHelper.findEmergencias()) {
            if (eme.getLatitud() != null && eme.getLongitud() != null){
                Toast.makeText(getApplicationContext(), eme.getLatitud().toString(), Toast.LENGTH_SHORT).show();
                mMap.addMarker(new MarkerOptions().position(new LatLng(eme.getLatitud(), eme.getLongitud())).title(eme.getDescripcion()));
            }else
                Toast.makeText(getApplicationContext(), eme.getDescripcion() + "--- coordenadas nulas", Toast.LENGTH_SHORT).show();
        }
        mCamera = CameraUpdateFactory.newLatLngZoom(new LatLng(19.380754, -99.176883), 4);
        mMap.animateCamera(mCamera);
    }

}
