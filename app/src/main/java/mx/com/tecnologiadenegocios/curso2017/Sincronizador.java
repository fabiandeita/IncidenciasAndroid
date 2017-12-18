package mx.com.tecnologiadenegocios.curso2017;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import org.apache.http.client.HttpClient;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import mx.com.tecnologiadenegocios.curso2017.model.Actividad;
import mx.com.tecnologiadenegocios.curso2017.model.Concepto;
import mx.com.tecnologiadenegocios.curso2017.model.Emergencia;
import mx.com.tecnologiadenegocios.curso2017.model.Estado;
import mx.com.tecnologiadenegocios.curso2017.model.Imagen;
import mx.com.tecnologiadenegocios.curso2017.model.Tramo;
import mx.com.tecnologiadenegocios.curso2017.R;
import mx.com.tecnologiadenegocios.curso2017.helper.BDHelper;
import mx.com.tecnologiadenegocios.curso2017.model.Carretera;
import mx.com.tecnologiadenegocios.curso2017.model.Croquis;
import mx.com.tecnologiadenegocios.curso2017.model.Subtramo;

/**
 * Created by Ing.Sergio Martínez on 22/06/2015.
 */
public class Sincronizador extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spEntidad, spCarretera, spTramo, spSubtramo;
    private ListView lvEmergencias;
    private TextView seleccione;
    private ArrayAdapter<String> aaEntidad, aaCarretera, aaTramo, aaSubtramo;

    private Switch switchAuto, switchCamion, switchTodoTipo, switchRutaAlternativa;
    private List<Carretera> carreteraList = new ArrayList<>();
    private List<Tramo> tramoList = new ArrayList<>();
    private List<Subtramo> subtramoList = new ArrayList<>();
    private List<String> opcEstado = new ArrayList<>();
    private List<String> opcCarretera = new ArrayList<>();
    private List<String> opcTramos = new ArrayList<>();
    private List<String> opcSubtramos = new ArrayList<>();
    private List<String> opcLVEmergencias = new ArrayList<>();
    private List<Emergencia> emergenciaList = new ArrayList<>();
    private String[] columnasBD = new String[] {"_id", "imagen", "textoSuperior"};
    private MatrixCursor cursor = new MatrixCursor(columnasBD);
    private String[] desdeEstasColumnas = {"imagen", "textoSuperior"};
    //String[] desdeEstasColumnas = {"imagen", "textoSuperior"};
    private int[] aEstasViews = {R.id.imageView_imagen, R.id.textView_superior};
    //int[] aEstasViews = {R.id.imageView_imagen, R.id.textView_superior};
    private SimpleCursorAdapter adapter;
    private Emergencia currentEmergecnia = new Emergencia();
    private BDHelper bdHelper;
    private String urlServicios = "";
    private List<String> errorSincronizacion = new ArrayList<>();
    private JSONObject emergenciaJSON;
    private int select = 0, position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Toast.makeText(getApplicationContext(), "Inicio", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sincronizador);

        opcCarretera.add("Seleccione");
        opcTramos.add("Seleccione");
        opcSubtramos.add("Seleccione");
        seleccione = (TextView)findViewById(R.id.txtSeleccione);
        seleccione.setVisibility(View.INVISIBLE);
        loadSpinners();
        loadPreferences();
        loadBotones();
        ((Button)findViewById(R.id.btnSincronizar)).setVisibility(View.INVISIBLE);
        refillIncidencias();


    }

    public BDHelper getBdHelper(){
        return bdHelper = new BDHelper(this);
    }

    public void closeBdHelper(){
        if(bdHelper != null)
            bdHelper.closeConnection();
    }
    public void loadPreferences(){
        SharedPreferences preferencias = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        urlServicios = (preferencias.getString("urlServicios", ""));

        //Toast.makeText(getApplicationContext(), "Carga preferencias", Toast.LENGTH_SHORT).show();
    }
    public void loadSpinners() {
        bdHelper = getBdHelper();
        //Toast.makeText(getApplicationContext(), "Se llena el Spinner", Toast.LENGTH_SHORT).show();
        opcEstado.add("Seleccione");
        for (Estado estado : bdHelper.findEstados()) {
            if (!estado.getNombre().equals("Distrito Federal"))
                opcEstado.add(estado.getNombre());
        }


        closeBdHelper();


        //Bindding Spinner
        spEntidad = (Spinner)findViewById(R.id.spEntidad);
        spCarretera = (Spinner)findViewById(R.id.spCarretera);
        spTramo = (Spinner)findViewById(R.id.spTramo);
        spSubtramo = (Spinner)findViewById(R.id.spSubtramo);

        lvEmergencias = (ListView) findViewById(R.id.lvIncidencias);

        lvEmergencias.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent galeriaForm = new Intent(Sincronizador.this, Detalle.class);
                galeriaForm.putExtra("icveIncidencia", emergenciaList.get(position).getIcve());
                galeriaForm.putExtra("descripcion", emergenciaList.get(position).getDescripcion());
                startActivityForResult(galeriaForm, 1);

            }
        });



        spEntidad.setOnItemSelectedListener(this);
        spCarretera.setOnItemSelectedListener(this);
        spTramo.setOnItemSelectedListener(this);
        spSubtramo.setOnItemSelectedListener(this);

        //lvEmergencias.setOnItemSelectedListener(this);

        aaEntidad = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcEstado);
        spEntidad.setAdapter(aaEntidad);





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void refillIncidencias(int position){
        bdHelper = new BDHelper(this);
        if(position != 0)
            emergenciaList = bdHelper.findEmergenciasByEntidad(position );
        else
            emergenciaList = bdHelper.findAllEmergencias();
        //Toast.makeText(getApplicationContext(), "Tamaño 1: "+emergenciaList.size(), Toast.LENGTH_SHORT).show();

        //String[] columnasBD = new String[] {"_id", "imagen", "textoSuperior"};


        if(!emergenciaList.isEmpty()) {
            seleccione.setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.btnSincronizar)).setVisibility(View.VISIBLE);
        }
        else{
            ((Button)findViewById(R.id.btnSincronizar)).setVisibility(View.INVISIBLE);
            seleccione.setVisibility(View.INVISIBLE);
        }

        cursor = new MatrixCursor(columnasBD);
        for(int c = 0;  c < emergenciaList.size(); c++) {
            switch (emergenciaList.get(c).getIcveEstado()){
                case (0):
                    cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_rojo, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion() });
                    break;
                case (2):
                    cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_amarillo, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion() });
                    break;
                case (1):
                    cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_verde, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion()});
                    break;
                default:
                    cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_rojo, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion()});
            }
        }

        //cursor.addRow(new Object[] {"0", R.drawable.logo_tecnologia, eme.getDescripcion()});

        lvEmergencias.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 90 * cursor.getCount()));
        adapter = new SimpleCursorAdapter(this, R.layout.entrada2, cursor, desdeEstasColumnas, aEstasViews, 0);

        lvEmergencias.setAdapter(adapter);
        lvEmergencias.setClickable(true);
        closeBdHelper();
    }

    public void refillIncidencias(){
        bdHelper = new BDHelper(this);
        emergenciaList = bdHelper.findAllEmergencias();
        //Toast.makeText(getApplicationContext(), "Tamaño 1: "+emergenciaList.size(), Toast.LENGTH_SHORT).show();

        //String[] columnasBD = new String[] {"_id", "imagen", "textoSuperior"};


        if(!emergenciaList.isEmpty()) {
            seleccione.setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.btnSincronizar)).setVisibility(View.VISIBLE);
        }
        else{
            ((Button)findViewById(R.id.btnSincronizar)).setVisibility(View.INVISIBLE);
            seleccione.setVisibility(View.INVISIBLE);
        }

        cursor = new MatrixCursor(columnasBD);
        for(int c = 0;  c < emergenciaList.size(); c++) {
            switch (emergenciaList.get(c).getIcveEstado()){
                case (0):
                    cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_rojo, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion() });
                    break;
                case (2):
                    cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_amarillo, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion() });
                    break;
                case (1):
                    cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_verde, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion()});
                    break;
                default:
                    cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_rojo, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion()});
            }
        }

        //cursor.addRow(new Object[] {"0", R.drawable.logo_tecnologia, eme.getDescripcion()});

        lvEmergencias.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 90 * cursor.getCount()));
        adapter = new SimpleCursorAdapter(this, R.layout.entrada2, cursor, desdeEstasColumnas, aEstasViews, 0);

        lvEmergencias.setAdapter(adapter);
        lvEmergencias.setClickable(true);
        closeBdHelper();
    }
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), parent.toString() + position + id, Toast.LENGTH_SHORT).show();

        switch (parent.getId()) {
            case (R.id.spEntidad):
                bdHelper = getBdHelper();
                opcCarretera.clear();
                opcTramos.clear();
                opcSubtramos.clear();
                opcCarretera.add("Seleccione");
                opcTramos.add("Seleccione");
                opcSubtramos.add("Seleccione");
                carreteraList = bdHelper.findCarreterasByEstado(opcEstado.get(spEntidad.getSelectedItemPosition()));
                for (Carretera carretera : carreteraList) {
                    opcCarretera.add(carretera.getNombre());
                }
                this.position = position;
                if(position != 0){
                    //opcLVEmergencias.clear();
                    emergenciaList = bdHelper.findEmergenciasByEntidad(position );
                    if(!emergenciaList.isEmpty()) {
                        seleccione.setVisibility(View.VISIBLE);
                        ((Button) findViewById(R.id.btnSincronizar)).setVisibility(View.VISIBLE);
                    }
                    else{
                        ((Button)findViewById(R.id.btnSincronizar)).setVisibility(View.INVISIBLE);
                        seleccione.setVisibility(View.INVISIBLE);
                    }
                    refillIncidencias(position);

                        //cursor.addRow(new Object[] {"0", R.drawable.logo_tecnologia, eme.getDescripcion()});

                    lvEmergencias.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 90 * cursor.getCount()));
                    adapter = new SimpleCursorAdapter(this, R.layout.entrada2, cursor, desdeEstasColumnas, aEstasViews, 0);

                    lvEmergencias.setAdapter(adapter);
                    lvEmergencias.setClickable(true);

                }
                closeBdHelper();
                aaCarretera = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcCarretera);
                spCarretera.setAdapter(aaCarretera);
                break;
            case (R.id.spCarretera):
                bdHelper = getBdHelper();
                currentEmergecnia.setIcveSubtramo(0);
                opcTramos.clear();
                opcSubtramos.clear();
                opcTramos.add("Seleccione");
                opcSubtramos.add("Seleccione");
                tramoList = bdHelper.findTramosByCarreteras(opcEstado.get(spEntidad.getSelectedItemPosition()), opcCarretera.get(spCarretera.getSelectedItemPosition()));
                for (Tramo tramo : tramoList) {
                    opcTramos.add(tramo.getNombre());
                }


                if(position != 0){
                    opcLVEmergencias.clear();
                    emergenciaList = bdHelper.findEmergenciasByCarretera(carreteraList.get(position-1).getIcve());
                    if(!emergenciaList.isEmpty()) {
                        seleccione.setVisibility(View.VISIBLE);
                        ((Button) findViewById(R.id.btnSincronizar)).setVisibility(View.VISIBLE);
                    }
                    else{
                        ((Button)findViewById(R.id.btnSincronizar)).setVisibility(View.INVISIBLE);
                        seleccione.setVisibility(View.INVISIBLE);
                    }
                    if(!emergenciaList.isEmpty())
                        seleccione.setVisibility(View.VISIBLE);
                    else
                        seleccione.setVisibility(View.INVISIBLE);
                    cursor = new MatrixCursor(columnasBD);
                    for(int c = 0;  c < emergenciaList.size(); c++) {
                        switch (emergenciaList.get(c).getIcveEstado()){
                            case (0):
                                cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_rojo, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion() });
                                break;
                            case (2):
                                cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_amarillo, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion() });
                                break;
                            case (1):
                                cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_verde, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion()});
                                break;
                            default:
                                cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_rojo, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion()});
                        }
                    }
                    //cursor.addRow(new Object[] {"0", R.drawable.logo_tecnologia, eme.getDescripcion()});

                    lvEmergencias.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 90 * cursor.getCount()));
                    adapter = new SimpleCursorAdapter(this, R.layout.entrada2, cursor, desdeEstasColumnas, aEstasViews, 0);

                    lvEmergencias.setAdapter(adapter);
                    lvEmergencias.setClickable(true);
                }


                aaTramo = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcTramos);
                spTramo.setAdapter(aaTramo);
                closeBdHelper();
                break;
            case (R.id.spTramo):
                bdHelper = new BDHelper(this);
                currentEmergecnia.setIcveSubtramo(0);
                opcSubtramos.clear();
                subtramoList.clear();
                subtramoList = bdHelper.findSubtramosByTramo(opcEstado.get(spEntidad.getSelectedItemPosition()), opcCarretera.get(spCarretera.getSelectedItemPosition()), opcTramos.get(spTramo.getSelectedItemPosition()));

                opcSubtramos.add("Seleccione");

                for (Subtramo subtramo : subtramoList) {
                    opcSubtramos.add(subtramo.getNombre());
                }


                if(position != 0){
                    opcLVEmergencias.clear();
                    emergenciaList = bdHelper.findEmergenciasByTramo(tramoList.get(position-1).getIcve());
                    if(!emergenciaList.isEmpty()) {
                        seleccione.setVisibility(View.VISIBLE);
                        ((Button) findViewById(R.id.btnSincronizar)).setVisibility(View.VISIBLE);
                    }
                    else{
                        ((Button)findViewById(R.id.btnSincronizar)).setVisibility(View.INVISIBLE);
                        seleccione.setVisibility(View.INVISIBLE);
                    }
                    cursor = new MatrixCursor(columnasBD);
                    for(int c = 0;  c < emergenciaList.size(); c++) {
                        switch (emergenciaList.get(c).getIcveEstado()){
                            case (0):
                                cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_rojo, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion() });
                                break;
                            case (2):
                                cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_amarillo, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion() });
                                break;
                            case (1):
                                cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_verde, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion()});
                                break;
                            default:
                                cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_rojo, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion()});
                        }
                    }

                    lvEmergencias.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 90 * cursor.getCount()));
                    adapter = new SimpleCursorAdapter(this, R.layout.entrada2, cursor, desdeEstasColumnas, aEstasViews, 0);

                    lvEmergencias.setAdapter(adapter);
                    lvEmergencias.setClickable(true);
                }
                closeBdHelper();

                aaSubtramo = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcSubtramos);
                spSubtramo.setAdapter(aaSubtramo);
                break;

            case (R.id.spSubtramo):
                bdHelper = new BDHelper(this);

                if(position != 0){
                    opcLVEmergencias.clear();
                    emergenciaList = bdHelper.findEmergenciasBySubtramo(subtramoList.get(position-1).getIcve());
                    if(!emergenciaList.isEmpty()) {
                        seleccione.setVisibility(View.VISIBLE);
                        ((Button) findViewById(R.id.btnSincronizar)).setVisibility(View.VISIBLE);
                    }
                    else{
                        ((Button)findViewById(R.id.btnSincronizar)).setVisibility(View.INVISIBLE);
                        seleccione.setVisibility(View.INVISIBLE);
                    }
                    cursor = new MatrixCursor(columnasBD);
                    for(int c = 0;  c < emergenciaList.size(); c++) {
                        switch (emergenciaList.get(c).getIcveEstado()){
                            case (0):
                                cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_rojo, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion() });
                                break;
                            case (2):
                                cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_amarillo, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion() });
                                break;
                            case (1):
                                cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_verde, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion()});
                                break;
                            default:
                                cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_rojo, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion()});
                        }
                    }
                    lvEmergencias.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 90 * cursor.getCount()));
                    adapter = new SimpleCursorAdapter(this, R.layout.entrada2, cursor, desdeEstasColumnas, aEstasViews, 0);

                    lvEmergencias.setAdapter(adapter);
                    lvEmergencias.setClickable(true);

                }
                closeBdHelper();
                break;
            case (R.id.lvIncidencias):
                Toast.makeText(getApplicationContext(), "ViewList: ", Toast.LENGTH_SHORT).show();
                break;
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void loadBotones(){

        //boton inicio
        findViewById(R.id.btnInicio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Sincronizador.this, Inicio.class);
                startActivity(altaForm);

            }
        });

        //boton alta
        findViewById(R.id.btnAlta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Sincronizador.this, Alta.class);
                startActivity(altaForm);


            }
        });

        //Boton reportes
        findViewById(R.id.btnReportes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Sincronizador.this, Reportes.class);
                startActivity(altaForm);


            }
        });



        //Boton Mapa
        findViewById(R.id.btnMapa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Sincronizador.this, Mapa.class);
                startActivity(altaForm);

            }
        });

        //btn Return
        findViewById(R.id.btnReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Sincronizador.this, Configuracion.class);
                startActivity(altaForm);
            }
        });

        //Boton Actualizacion
        findViewById(R.id.btnActualizacion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Sincronizador.this, Actualizacion.class);
                startActivity(altaForm);

            }
        });

        //Boton Sincronizar
        findViewById(R.id.btnSincronizar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcesoSincronizacion proceso = new ProcesoSincronizacion();
                proceso.execute();

            }
        });
    }

    public HashMap<String, Object> emergenciaToHashMap(Emergencia emergencia){
        HashMap<String, Object> diccionario = new HashMap<String, Object>();
        if(emergencia.getCcve() != 0)
            diccionario.put("emergenciaid", emergencia.getCcve());
        diccionario.put("accionesrealizadas", emergencia.getAccionesRealizadas());
        diccionario.put("activo", 1);
        diccionario.put("altitud", emergencia.getAltitud() == null ? 0 : emergencia.getAltitud());
        diccionario.put("categoriaid", emergencia.getIcveCategoriaEmergencia());
        diccionario.put("danoid", emergencia.getIcveDanos());
        diccionario.put("descripcion", emergencia.getDescripcion());
        diccionario.put("fechadefinitiva", emergencia.getFechaCreacion());
        diccionario.put("fechadefinitiva", emergencia.getFechaCreacion());
        diccionario.put("fechaprovisional", emergencia.getFechaCreacion());
        diccionario.put("kmfinal", emergencia.getKmFinal());
        diccionario.put("kminicial", emergencia.getKmInicial());
        diccionario.put("latitud", emergencia.getLatitud() == null ? 0 : emergencia.getLatitud());
        diccionario.put("longitud", emergencia.getLongitud() == null ? 0 : emergencia.getLongitud());
        diccionario.put("rutaalternativa", emergencia.getRutaAlternativa());
        diccionario.put("subtramoid", emergencia.getIcveSubtramo());
        diccionario.put("tipoid", emergencia.getIcveTipoEmergencia());
        diccionario.put("transito", emergencia.getIcveTransito());
        diccionario.put("vehiculostransitanautos", emergencia.getVehiculosTransitanAutos());
        diccionario.put("vehiculostransitancamiones", emergencia.getVehiculosTransitanCamiones());
        diccionario.put("vehiculostransitantodotipo", emergencia.getVehiculosTransitanTodoTipo());
        diccionario.put("emergencia", 0);
        return diccionario;
    }
    private class ProcesoSincronizacion extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog dialog;
        private HttpResponse response;
        private HttpClient httpCLient;
        private HttpPost httpPost;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Sincronizador.this);
            dialog.setTitle("Espere...");
            dialog.setMessage("Sincronizando incidencias con el servidor");
            dialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(dialog.isShowing())
                dialog.dismiss();
            refillIncidencias(position);
        }




        @Override
        protected Void doInBackground(Void... params) {
            //Toast.makeText(getApplicationContext(), "Ejecutara", Toast.LENGTH_SHORT).show();
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            HashMap<String, Object> diccionario = new HashMap<String, Object>();
            String url;
            int ccve = 0, icve = 0;
            Boolean objectJSON1 = false, imagenesIncidencia = true;
            List<NameValuePair> pairs;
            try {
                for(int c = 0;  c < emergenciaList.size(); c++) {
                    Emergencia emergencia = emergenciaList.get(c);
                    icve = emergencia.getIcve();

                    if(emergencia.getCcve() != 0){
                        ccve = emergencia.getCcve();
                        url = urlServicios + "/IncidenciasPruebas/web/Emergencia_update.action";

                        emergenciaJSON = new JSONObject(emergenciaToHashMap(emergencia));

                        pairs = new ArrayList<NameValuePair>();
                        pairs.add(new BasicNameValuePair("data", emergenciaJSON.toString()));

                        HttpClient httpCLient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(url);
                        httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
                        HttpResponse response = httpCLient.execute(httpPost);


                    }else if(emergencia.getIcveEstado() != 1){
                        url = urlServicios + "/IncidenciasPruebas/web/Emergencia_create.action";
                        emergenciaJSON = new JSONObject(emergenciaToHashMap(emergencia));

                        pairs = new ArrayList<NameValuePair>();
                        pairs.add(new BasicNameValuePair("data1", emergenciaJSON.toString()));

                        HttpClient httpCLient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(url);

                        httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
                        HttpResponse response = httpCLient.execute(httpPost);

                        JSONObject object = new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
                        JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(0);
                        ccve = childJSONObject.getInt("emergenciaid");
                        if(object.getBoolean("success") == true){
                            bdHelper = getBdHelper();
                            bdHelper.updateIncidenciaCCVE(icve, ccve);
                            closeBdHelper();
                        }
                    }
                    bdHelper = getBdHelper();
                    try {
                        for (Imagen imagen : bdHelper.findImagenIncidencia(icve)) {
                            if (imagen.getCcve() == 0) {
                                url = urlServicios + "/IncidenciasPruebas/web/uploadFileToDB.action";
                                DefaultHttpClient httpClient2 = new DefaultHttpClient();
                                HttpPost httpPost2 = new HttpPost(url);
                                httpPost2.setHeader("Content-Type", "multipart/form-data; boundary=\"0xKhTmLbOuNdArY\"");//No tocar
                                File file = new File(imagen.getUrl());

                                if (file.exists())
                                    file.toString();
                                FileBody fileBody = new FileBody(file, "multipart/form-data"); // here is line 221

                                MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
                                //multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                                multipartEntity.addBinaryBody("filesToUpload", new File(imagen.getUrl()), ContentType.APPLICATION_OCTET_STREAM, "FotoConcepto.jpeg");
                                multipartEntity.setBoundary("0xKhTmLbOuNdArY");//No tocar
                                httpPost2.setEntity(multipartEntity.build());
                                HttpResponse httpResponse = httpClient2.execute(httpPost2);
                                JSONObject object = new JSONObject(inputStreamToString(httpResponse.getEntity().getContent()).toString());
                                boolean success = object.getBoolean("success");
                                JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(0);
                                int ccveImagen = childJSONObject.getInt("imagenid");
                                //


                                url = urlServicios + "/IncidenciasPruebas/web/Emergenciaimagen_create.action";
                                JSONObject emergenciaImagen = new JSONObject();
                                emergenciaImagen.put("emergenciaid", ccve);

                                JSONObject imagenJSON = new JSONObject();
                                imagenJSON.put("imagenid", ccveImagen);
                                emergenciaImagen.put("imagen", imagenJSON);

                                pairs = new ArrayList<NameValuePair>();
                                pairs.add(new BasicNameValuePair("data", emergenciaImagen.toString()));

                                HttpClient httpCLient = new DefaultHttpClient();
                                HttpPost httpPost = new HttpPost(url);

                                httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
                                HttpResponse response = httpCLient.execute(httpPost);

                                JSONObject objectRespuesta = new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
                                if (object.getBoolean("success") == true) {
                                    bdHelper.updateImagenCCVE(imagen.getIcve(), ccveImagen);
                                } else
                                    imagenesIncidencia = false;
                            }
                        }
                    }catch(Exception e){
                        errorSincronizacion.add(e.toString());
                        imagenesIncidencia = false;
                    }

                    try{
                        for(Croquis croquis : bdHelper.findCroquisByEmergencia(icve)){
                            if(croquis.getCcve() == 0){
                                url = urlServicios + "/IncidenciasPruebas/web/uploadFileToDB.action";
                                DefaultHttpClient httpClient2 = new DefaultHttpClient();
                                HttpPost httpPost2 = new HttpPost(url);
                                httpPost2.setHeader("Content-Type", "multipart/form-data; boundary=\"0xKhTmLbOuNdArY\"");//No tocar
                                File file = new File(croquis.getUrl());
                                if (file.exists())
                                    file.toString();
                                MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
                                multipartEntity.addBinaryBody("filesToUpload", new File(croquis.getUrl()), ContentType.APPLICATION_OCTET_STREAM, "FotoConcepto.jpeg");
                                multipartEntity.setBoundary("0xKhTmLbOuNdArY");//No tocar
                                httpPost2.setEntity(multipartEntity.build());
                                HttpResponse httpResponse = httpClient2.execute(httpPost2);
                                JSONObject object = new JSONObject(inputStreamToString(httpResponse.getEntity().getContent()).toString());
                                boolean success = object.getBoolean("success");
                                JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(0);
                                int ccveImagen = childJSONObject.getInt("imagenid");
                                if(success) {
                                    //Horchatisa
                                    url = urlServicios + "/IncidenciasPruebas/web/Emergencia_update.action";

                                    emergenciaJSON = new JSONObject(emergenciaToHashMap(emergencia));
                                    emergenciaJSON.put("croquisimagenid", ccveImagen);

                                    pairs = new ArrayList<NameValuePair>();
                                    pairs.add(new BasicNameValuePair("data", emergenciaJSON.toString()));

                                    HttpClient httpCLient = new DefaultHttpClient();
                                    HttpPost httpPost = new HttpPost(url);
                                    httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
                                    HttpResponse response = httpCLient.execute(httpPost);

                                    object = new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
                                    success = object.getBoolean("success");
                                    childJSONObject = object.getJSONArray("data").getJSONObject(0);
                                    int ccveCroquis = childJSONObject.getInt("croquisimagenid");
                                    if(success)
                                        bdHelper.updateCroquisCCVE(croquis.getIcve(), ccveCroquis);
                                }
                            }
                        }
                    }catch(Exception e){
                        errorSincronizacion.add(e.toString());
                        imagenesIncidencia = false;
                    }
                        closeBdHelper();
                    //if(emergencia.getIcveEstado() != 1)
                    if(toSAccion(icve, ccve) && toSConcepto(icve, ccve) && imagenesIncidencia) {
                            bdHelper = getBdHelper();
                            bdHelper.updateIncidenciaEstado(icve, 1);
                            closeBdHelper();


                    }
                }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(), "Ocurrió un fallo en la sincronización,  intentelo de nuevo. Error: " + , Toast.LENGTH_SHORT).show();
                errorSincronizacion.add(e.toString());
            }
            return null;

    }

        private boolean toSAccion(int icve, int ppid){
            bdHelper = getBdHelper();
            JSONObject actividadJSON;
            String url;
            int ccve;
            boolean success = false;
            for(Actividad actividad : bdHelper.findActividadesByIncidencia(icve)){

                try {
                    actividadJSON = new JSONObject();
                    url = urlServicios + "/IncidenciasPruebas/web/Accion_create.action";
                    if(actividad.getCcve() == 0) {
                        actividadJSON.put("accion", actividad.getDescripcion());
                        actividadJSON.put("emergenciaid", ppid);
                        actividadJSON.put("transito", actividad.getTransito());
                        actividadJSON.put("activo", 1);

                        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                        pairs.add(new BasicNameValuePair("data", actividadJSON.toString()));

                        HttpClient httpCLient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(url);

                        //httpPost1.setEntity(new StringEntity("data1", encoded));
                        httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
                        HttpResponse response = httpCLient.execute(httpPost);

                        JSONObject object = new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
                        JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(0);
                        ccve = childJSONObject.getInt("accionid");
                        success = object.getBoolean("success");

                        if(success) {
                            bdHelper = getBdHelper();
                            bdHelper.updateActividadCCVE(icve, ccve);
                            closeBdHelper();
                        }
                    }else{/*
                        url = urlServicios + "/Incidencias/web/Accion_update.action";
                        actividadJSON.put("accionid", actividad.getCcve());
                        actividadJSON.put("accion", actividad.getDescripcion());
                        actividadJSON.put("emergenciaid", ppid);
                        actividadJSON.put("transito", actividad.getDescripcion());
                        actividadJSON.put("activo", actividad.getDescripcion());

                        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                        pairs.add(new BasicNameValuePair("data", actividadJSON.toString()));

                        HttpClient httpCLient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(url);

                        //httpPost1.setEntity(new StringEntity("data1", encoded));
                        httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
                        HttpResponse response = httpCLient.execute(httpPost);
                        */
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    return false;
                }
            }



            closeBdHelper();
            return true;
        }

        private boolean toSConcepto(int icve, int ccve){
            bdHelper = getBdHelper();
            JSONObject conceptoJSON;
            String url;
            boolean successAltaConcepto = true, successAltaImagen = false, successAltaImagenConcepto = false;
            for(Concepto concepto : bdHelper.findConceptoByIncidencia(icve)){
                try {
                    conceptoJSON = new JSONObject();
                    if(concepto.getCcve() == 0) {
                        successAltaConcepto = true;
                        url = urlServicios + "/IncidenciasPruebas/web/Actividades_create.action";
                        conceptoJSON.put("activo", 1);
                        conceptoJSON.put("emergenciaid", ccve);
                        conceptoJSON.put("avance", concepto.getIporAvance());
                        conceptoJSON.put("cantidad", concepto.getCantidad());
                        JSONObject empresa = new JSONObject();
                        empresa.put("empresaid", 1);
                        conceptoJSON.put("nombre", concepto.getDescripcion());
                        conceptoJSON.put("unidad", concepto.getIcveUnidad());
                        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                        pairs.add(new BasicNameValuePair("data", conceptoJSON.toString()));
                        HttpClient httpCLient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(url);
                        //httpPost1.setEntity(new StringEntity("data1", encoded));
                        httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
                        HttpResponse response = httpCLient.execute(httpPost);

                        JSONObject object = new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
                        JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(0);
                        int ccveConcepto = childJSONObject.getInt("actividadid");
                        concepto.setCcve(ccveConcepto);
                        successAltaConcepto = object.getBoolean("success");
                        if(successAltaConcepto)
                            bdHelper.updateConceptoCCVE(concepto.getIcve(), ccveConcepto);
                    }

                    url = urlServicios + "/IncidenciasPruebas/web/uploadFileToDB.action";
                    String urlMergeFor = urlServicios +"/IncidenciasPruebas/web/Actividadimagen_create.action";

                    try
                    {
                            for(Imagen imagen :  bdHelper.findImagenConcepto(concepto.getIcve())) {
                                if(imagen.getCcve() == 0) {
                                    //Mandada por los pomos
                                    DefaultHttpClient httpClient2 = new DefaultHttpClient();
                                    HttpPost httpPost2 = new HttpPost(url);
                                    httpPost2.setHeader("Content-Type", "multipart/form-data; boundary=\"0xKhTmLbOuNdArY\"");//No tocar
                                    File file = new File(imagen.getUrl());


                                    if (file.exists())
                                        file.toString();
                                    FileBody fileBody = new FileBody(file, "multipart/form-data"); // here is line 221

                                    MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();

                                    //multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                                    multipartEntity.addBinaryBody("filesToUpload", new File(imagen.getUrl()), ContentType.APPLICATION_OCTET_STREAM, "FotoConcepto.jpeg");
                                    multipartEntity.setBoundary("0xKhTmLbOuNdArY");//No tocar
                                    httpPost2.setEntity(multipartEntity.build());

                                    HttpResponse httpResponse = httpClient2.execute(httpPost2);
                                    JSONObject object = new JSONObject(inputStreamToString(httpResponse.getEntity().getContent()).toString());
                                    boolean success = object.getBoolean("success");
                                    JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(0);
                                    int ccveImagen = childJSONObject.getInt("imagenid");
                                    //
                                    bdHelper.updateImagenCCVE(imagen.getIcve(), ccveImagen);

                                    //Horchataso
                                    conceptoJSON = new JSONObject();
                                    conceptoJSON.put("emergenciaimagenid", ccve);

                                    JSONObject actividades = new JSONObject();
                                    actividades.put("actividadid", concepto.getCcve());

                                    JSONObject imagen2 = new JSONObject();
                                    imagen2.put("imagenid", ccveImagen);

                                    conceptoJSON.put("actividades", actividades);
                                    conceptoJSON.put("imagen", imagen2);

                                    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                                    pairs.add(new BasicNameValuePair("data", conceptoJSON.toString()));

                                    HttpClient httpCLient = new DefaultHttpClient();
                                    HttpPost httpPost = new HttpPost(urlMergeFor);

                                    //httpPost1.setEntity(new StringEntity("data1", encoded));
                                    httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
                                    HttpResponse response = httpCLient.execute(httpPost);

                                    object = new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
                                    childJSONObject = object.getJSONArray("data").getJSONObject(0);
                                    //ccve = childJSONObject.getInt("actividadid");
                                    successAltaImagenConcepto = object.getBoolean("success");


                                }
                            }

                            if(successAltaConcepto && successAltaImagen && successAltaImagenConcepto) {

                                bdHelper.updateConceptoCCVE(icve, ccve);

                            }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        closeBdHelper();
                    }


                    /*
                        url = urlServicios + "/Incidencias/web/Actividades_update.action";
                        conceptoJSON.put("actividadid", concepto.getCcve());
                        conceptoJSON.put("activo", 1);
                        conceptoJSON.put("emergenciaid", ccve);
                        conceptoJSON.put("avance", concepto.getIporAvance());
                        conceptoJSON.put("cantidad", concepto.getCantidad());
                        JSONObject empresa = new JSONObject();
                        empresa.put("empresaid", 1);
                        conceptoJSON.put("empresa", empresa);
                        conceptoJSON.put("nombre", concepto.getDescripcion());
                        conceptoJSON.put("unidad", concepto.getIcveUnidad());

                        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                        pairs.add(new BasicNameValuePair("data", conceptoJSON.toString()));

                        HttpClient httpCLient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(url);

                        //httpPost1.setEntity(new StringEntity("data1", encoded));
                        httpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
                        HttpResponse response = httpCLient.execute(httpPost);
                        */
                        //Subir imágenes

                    //closeBdHelper();
                }catch(Exception e){
                    e.printStackTrace();
                    closeBdHelper();
                    return false;
                }
            }



            closeBdHelper();
            return true;
        }

    private StringBuilder inputStreamToString(InputStream is)
    {
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader rd = new BufferedReader( new InputStreamReader(is) );
        try
        {
            while( (line = rd.readLine()) != null )
            {
                stringBuilder.append(line);
            }
        }
        catch( IOException e)
        {
            e.printStackTrace();
        }

        return stringBuilder;
    }
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
