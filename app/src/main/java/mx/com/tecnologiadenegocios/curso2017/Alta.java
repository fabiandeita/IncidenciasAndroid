package mx.com.tecnologiadenegocios.curso2017;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import mx.com.tecnologiadenegocios.curso2017.model.Emergencia;
import mx.com.tecnologiadenegocios.curso2017.model.Estado;
import mx.com.tecnologiadenegocios.curso2017.model.Imagen;
import mx.com.tecnologiadenegocios.curso2017.model.TipoEmergencia;
import mx.com.tecnologiadenegocios.curso2017.model.Tramo;
import mx.com.tecnologiadenegocios.curso2017.R;
import mx.com.tecnologiadenegocios.curso2017.helper.BDHelper;
import mx.com.tecnologiadenegocios.curso2017.model.Carretera;
import mx.com.tecnologiadenegocios.curso2017.model.CategoriaEmergencia;
import mx.com.tecnologiadenegocios.curso2017.model.CausaEmergencia;
import mx.com.tecnologiadenegocios.curso2017.model.Croquis;
import mx.com.tecnologiadenegocios.curso2017.model.Danos;
import mx.com.tecnologiadenegocios.curso2017.model.Subtramo;

/**
 * Created by Ing.Sergio Martínez on 22/06/2015.
 */
public class Alta extends ActionBarActivity implements OnItemSelectedListener {
    private Spinner spEntidad, spCarretera, spTramo, spSubtramo, spDano, spCausa, spTipoIncidencia, spTransitabilidad, spCategoria;
    private ArrayAdapter<String> aaEntidad, aaCarretera, aaTramo, aaSubtramo, aaDano, aaCausa, aaTipoIncidencia, aaTransitabilidad, aaCategoria;
    private Switch switchAuto, switchCamion, switchTodoTipo, switchRutaAlternativa;

    private List<Carretera> carreteraList = new ArrayList<>();
    private List<Tramo> tramoList = new ArrayList<>();
    private List<Subtramo> subtramoList = new ArrayList<>();
    private List<String> opcEstado = new ArrayList<>();
    private List<String> opcCarretera = new ArrayList<>();
    private List<String> opcTramos = new ArrayList<>();
    private List<String> opcSubtramos = new ArrayList<>();
    private List<String> opcDanosCausados = new ArrayList<>();
    private List<String> opcCausaEmergencia = new ArrayList<>();
    private List<String> opcTipoEmergencia = new ArrayList<>();
    private List<String> opcTransitabilidad = new ArrayList<>();
    private List<String> opcCategoria = new ArrayList<>();

    private EditText etKmInicial;
    private EditText etKmFinal;
    private EditText etAltitud;
    private EditText etLatitud;
    private EditText etLongitud;

    /*private EditText etDiaP;
    private EditText etMesP;
    private EditText etAnioP;*/
    private EditText etDiaD;
    private EditText etMesD;
    private EditText etAnioD;

    private String query = "";
    private Subtramo subtramoActual = new Subtramo();
    private Emergencia currentEmergecnia = new Emergencia();
    private Button btnCcapturaGPS, btnVerAdjuntarFotos, btnCroquis;
    private ArrayList<String> direccionesRutaAlterna;
    private ArrayList<String> direccionesIncidencia;
    private int alterna0OrIncidencia1 = 5;
    private boolean catalogosSincronizados = false, sesFlu = false;
    private BDHelper bdHelper;
    private Calendar cal = new GregorianCalendar();
    private Date fella = cal.getTime();
    public DatePicker dateprov;
    public DatePicker datedef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta);

        loadPreferences();
            /*etDiaP = (EditText) findViewById(R.id.etDiaP);
            etMesP = (EditText) findViewById(R.id.etMesP);
            etAnioP = (EditText) findViewById(R.id.etAnioP);*/
            /*etDiaD = (EditText) findViewById(R.id.etDiaD);
            etMesD = (EditText) findViewById(R.id.etMesD);
            etAnioD = (EditText) findViewById(R.id.etAnioD);*/

            dateprov = (DatePicker) findViewById(R.id.dateprov);
            datedef = (DatePicker) findViewById(R.id.datedef);

          /*  etDiaD.setText(Integer.toString(cal.get(cal.DATE)));
            etMesD.setText(Integer.toString(cal.get(cal.MONTH)+1));
            etAnioD.setText(Integer.toString(cal.get(cal.YEAR)));*/

        Calendar c = Calendar.getInstance();
        int anop = c.get(Calendar.YEAR);
        int mesp = c.get(Calendar.MONTH);
        int diap = c.get(Calendar.DAY_OF_MONTH);

        dateprov.init(anop, mesp, diap, null);
        datedef.init(anop, mesp+1 , diap, null);

           /* etDiaP.setText(Integer.toString(cal.get(cal.DATE)));
            etMesP.setText(Integer.toString(cal.get(cal.MONTH)+1));
            etAnioP.setText(Integer.toString(cal.get(cal.YEAR)));*/

            btnCcapturaGPS = (Button) findViewById(R.id.btnCapturarGPS);
            btnCroquis = (Button) findViewById(R.id.btnCroquis);
            btnVerAdjuntarFotos = (Button) findViewById(R.id.btnVerAdjuntarFotos);
            switchRutaAlternativa = (Switch) findViewById(R.id.switchRutaAlternativa);
            loadBotones();

            etKmInicial = (EditText) findViewById(R.id.etKmInicial);
            etKmFinal = (EditText) findViewById(R.id.etKmFinal);

            //aaTransitabilidad = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcTransitabilidad);
            //spTransitabilidad.setAdapter(aaTransitabilidad);

            //GPS
            LocationManager milocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener milocListener = new MiLocationListener();
            milocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, milocListener);

            etAltitud = (EditText) findViewById(R.id.txtAltitud);
            etLatitud = (EditText) findViewById(R.id.txtLatitud);
            etLongitud = (EditText) findViewById(R.id.txtLongitud);

            switchAuto = (Switch) findViewById(R.id.switchAutos);
            switchCamion = (Switch) findViewById(R.id.switchCamiones);
            switchTodoTipo = (Switch) findViewById(R.id.switchTodoTipo);

            switchAuto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.switchAutos) {
                        if (switchAuto.isChecked()) {
                            switchAuto.setChecked(true);
                            switchCamion.setChecked(false);
                            switchTodoTipo.setChecked(false);
                        } else
                            switchTodoTipo.setChecked(false);
                    }
                }
            });

            switchCamion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.switchCamiones) {
                        if (switchCamion.isChecked()) {
                            switchAuto.setChecked(false);
                            switchCamion.setChecked(true);
                            switchTodoTipo.setChecked(false);
                        } else
                            switchTodoTipo.setChecked(false);
                    }
                }
            });

            switchTodoTipo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.switchTodoTipo) {
                        if (switchTodoTipo.isChecked()) {
                            switchAuto.setChecked(true);
                            switchCamion.setChecked(true);
                            switchTodoTipo.setChecked(true);
                        } else {
                            switchAuto.setChecked(false);
                            switchCamion.setChecked(false);
                            switchTodoTipo.setChecked(false);
                        }
                    }
                }
            });


    }


    public void loadSpinners(){
        BDHelper bdHelper = new BDHelper(this);
        //Toast.makeText(getApplicationContext(), "Se llena el Spinner", Toast.LENGTH_SHORT).show();
        opcEstado.add("Seleccione");
        for(Estado estado : bdHelper.findEstados()){
            if(!estado.getNombre().equals("Distrito Federal"))
            opcEstado.add(estado.getNombre());
        }
        /*
        for(Carretera carretera : bdHelper.findCarreteras()){
            opcCarretera.add(carretera.getNombre());
        }

        for(Tramo tramo : bdHelper.findTramos()){
            opcTramos.add(tramo.getNombre());
        }

        for(Subtramo subtramo : bdHelper.findSubtramos()){
            opcSubtramos.add(subtramo.getNombre());
        }
        */
        opcDanosCausados.add("SELECCIONE");
        for(Danos danos : bdHelper.findDanosCausados()){
            opcDanosCausados.add(danos.getNombre());
        }

        opcCausaEmergencia.add("SELECCIONE");
        for(CausaEmergencia causa : bdHelper.findCausasEmergencia()){
            opcCausaEmergencia.add(causa.getNombre());
        }
        /*
        for(TipoEmergencia tipoEmergencia : bdHelper.findTiposEmergencia()){
            opcTipoEmergencia.add(tipoEmergencia.getNombre());
        }
        */
        opcTransitabilidad.add("SELECCIONE");
        opcTransitabilidad.add("NULO");
        opcTransitabilidad.add("TOTAL");
        opcTransitabilidad.add("PROVISIONAL");
        opcTransitabilidad.add("PARCIAL");


        bdHelper.closeConnection();

        //Bindding Spinner
        spEntidad = (Spinner)findViewById(R.id.spEntidad);
        spCarretera = (Spinner)findViewById(R.id.spCarretera);
        spTramo = (Spinner)findViewById(R.id.spTramo);
        spSubtramo = (Spinner)findViewById(R.id.spSubtramo);
        spDano = (Spinner)findViewById(R.id.spDano);
        spCausa = (Spinner)findViewById(R.id.spCausa);
        spTipoIncidencia = (Spinner)findViewById(R.id.spTipoIncidencia);
        spTransitabilidad = (Spinner)findViewById(R.id.spTransitabilidad);
        spCategoria = (Spinner)findViewById(R.id.spCategoria);

        //se agrega el listener
        spEntidad.setOnItemSelectedListener(this);
        spCarretera.setOnItemSelectedListener(this);
        spTramo.setOnItemSelectedListener(this);
        spSubtramo.setOnItemSelectedListener(this);
        spDano.setOnItemSelectedListener(this);
        spCausa.setOnItemSelectedListener(this);
        spTipoIncidencia.setOnItemSelectedListener(this);
        spTransitabilidad.setOnItemSelectedListener(this);
        spCategoria.setOnItemSelectedListener(this);

        aaEntidad = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcEstado);
        spEntidad.setAdapter(aaEntidad);
        /*
        aaCarretera = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcCarretera);
        spCarretera.setAdapter(aaCarretera);

        aaTramo = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcTramos);
        spTramo.setAdapter(aaTramo);

        aaSubtramo = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcSubtramos);
        spSubtramo.setAdapter(aaSubtramo);
        */
        aaDano = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcDanosCausados);
        spDano.setAdapter(aaDano);

        aaCausa = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcCausaEmergencia);
        spCausa.setAdapter(aaCausa);

        //aaTipoIncidencia = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcTipoEmergencia);
        //spTipoIncidencia.setAdapter(aaTipoIncidencia);

        aaTransitabilidad = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcTransitabilidad);
        spTransitabilidad.setAdapter(aaTransitabilidad);


    }
    public void loadPreferences(){
        SharedPreferences preferencias = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        catalogosSincronizados = preferencias.contains("catalogosSincronizados");
        sesFlu = preferencias.getBoolean("sesFlu", false);
        loadSpinners();
    }
    public class MiLocationListener implements LocationListener
    {
        public void onLocationChanged(Location loc)
        {
            try {
                etLatitud.setText(Double.toString(loc.getLatitude()));
                etLongitud.setText(Double.toString(loc.getLongitude()));
                etAltitud.setText(Double.toString(loc.getAltitude()));
                //loc.getLongitude();
                //String coordenadas = "Mis coordenadas son: Latitud = " + loc.getLatitude() + " Longitud = " + loc.getLongitude();
                //Toast.makeText(getApplicationContext(), coordenadas, Toast.LENGTH_LONG).show();
            }
            catch(Exception e){

            }
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

    public boolean validarEmergencia(){
        if(spEntidad == null){
            Toast.makeText( getApplicationContext(),"Descargue los catálogos.",Toast.LENGTH_LONG ).show();
            return false;
        }else if(spEntidad.getSelectedItemPosition() == 0){
            Toast.makeText( getApplicationContext(),"Seleccione una entidad",Toast.LENGTH_SHORT ).show();
            return false;
        }else if(spCarretera.getSelectedItemPosition() == 0){
            Toast.makeText( getApplicationContext(),"Seleccione una carretera",Toast.LENGTH_SHORT ).show();
            return false;
        }else if(spTramo.getSelectedItemPosition() == 0){
            Toast.makeText( getApplicationContext(),"Seleccione una tramo",Toast.LENGTH_SHORT ).show();
            return false;
        }else if(currentEmergecnia.getIcveSubtramo() == 0){
            Toast.makeText( getApplicationContext(),"Seleccione una subtramo",Toast.LENGTH_SHORT ).show();
            return false;
        }else if( ( (EditText) findViewById(R.id.etKmInicial)).getText().toString().isEmpty()){
            Toast.makeText( getApplicationContext(),"Especifique el kilometro inicial",Toast.LENGTH_SHORT ).show();
            return false;
        }else if( ( (EditText) findViewById(R.id.etKmFinal)).getText().toString().isEmpty()){
            Toast.makeText( getApplicationContext(),"Especifique el kilometro final",Toast.LENGTH_SHORT ).show();
            return false;
        }else if(spDano.getSelectedItemPosition() == 0){
            Toast.makeText( getApplicationContext(),"Seleccione el tipo de daño",Toast.LENGTH_SHORT ).show();
            return false;
        }else if(spCausa.getSelectedItemPosition() == 0){
            Toast.makeText( getApplicationContext(),"Seleccione la causa",Toast.LENGTH_SHORT ).show();
            return false;
        }else if(spTipoIncidencia.getSelectedItemPosition() == 0){
            Toast.makeText( getApplicationContext(),"Seleccione el tpo de incidencia",Toast.LENGTH_SHORT ).show();
            return false;
        }else if(spTransitabilidad.getSelectedItemPosition() == 0){
            Toast.makeText( getApplicationContext(),"Seleccione el tpo de transitabilidad",Toast.LENGTH_SHORT ).show();
            return false;
        }


        if( !(Double.parseDouble(( (EditText) findViewById(R.id.etKmInicial)).getText().toString().replace("+",".")) >= Double.parseDouble(subtramoActual.getKmInicial())) ||
                !(Double.parseDouble(( (EditText) findViewById(R.id.etKmInicial)).getText().toString().replace("+",".")) <= Double.parseDouble(subtramoActual.getKmFinal()))){
            Toast.makeText( getApplicationContext(),"Kilometro inicial fuera del rango del subtramo",Toast.LENGTH_SHORT ).show();
            return false;
        }else
            currentEmergecnia.setKmInicial(((EditText) findViewById(R.id.etKmInicial)).getText().toString().replace("+", "."));

        if(!( Double.parseDouble(( (EditText) findViewById(R.id.etKmFinal)).getText().toString().replace("+",".")) >= Double.parseDouble(subtramoActual.getKmInicial())) &&
                !(Double.parseDouble(( (EditText) findViewById(R.id.etKmFinal)).getText().toString().replace("+",".")) <= Double.parseDouble(subtramoActual.getKmFinal()))){
            Toast.makeText( getApplicationContext(),"Kilometro final fuera del rango del subtramo",Toast.LENGTH_SHORT ).show();
            return false;
        }else
            currentEmergecnia.setKmFinal(((EditText) findViewById(R.id.etKmFinal)).getText().toString().replace("+", "."));

        //if(currentEmergecnia.getLatitud().isNaN() ||currentEmergecnia.getLongitud().isNaN())
          //  Toast.makeText( getApplicationContext(),"Capture el GPS",Toast.LENGTH_SHORT ).show();

        if(((EditText)findViewById(R.id.etDescripcion)).getText().toString().isEmpty()    ) {
            Toast.makeText(getApplicationContext(), "Redacte una descripción", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            currentEmergecnia.setDescripcion(((EditText)findViewById(R.id.etDescripcion)).getText().toString());

        if(spTransitabilidad.getSelectedItemPosition() != 2) {//Total
            try {
                if(Integer.parseInt(String.valueOf(dateprov.getDayOfMonth())) > 31 || Integer.parseInt(String.valueOf(dateprov.getDayOfMonth())) == 0) {
                    Toast.makeText(getApplicationContext(), "Revise el día de la apertura provisional.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "Revise el día de la apertura provisional.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return false;
            }
            try {

                if(Integer.parseInt(String.valueOf(dateprov.getMonth())) > 12 || Integer.parseInt(String.valueOf(dateprov.getMonth())) == 0){
                    Toast.makeText(getApplicationContext(), "Revise el mes de la apertura provisional.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "Revise el mes de la apertura provisional.", Toast.LENGTH_SHORT).show();
                return false;
            }
            try {

                if(Integer.parseInt(String.valueOf(dateprov.getYear())) < 2015 || Integer.parseInt(String.valueOf(dateprov.getYear())) > 2030){
                    Toast.makeText(getApplicationContext(), "Revise el año de la apertura provisional.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "Revise el año de la apertura provisional.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return false;
            }

            currentEmergecnia.setFechaProvisional(String.valueOf(dateprov.getYear()) + "-" + String.valueOf(dateprov.getMonth()) + "-" + String.valueOf(dateprov.getDayOfMonth()) + " 12:12:00:0");
            try {
                if(Integer.parseInt(String.valueOf(datedef.getDayOfMonth())) > 31 || Integer.parseInt(String.valueOf(datedef.getDayOfMonth())) == 0){
                    Toast.makeText(getApplicationContext(), "Revise el día de la apertura definitiva.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "Revise el día de la apertura definitiva.", Toast.LENGTH_SHORT).show();
                return false;
            }
            try {
                if(Integer.parseInt(String.valueOf(datedef.getMonth())) > 12 || Integer.parseInt(String.valueOf(datedef.getMonth())) == 0){
                    Toast.makeText(getApplicationContext(), "Revise el mes de la apertura definitiva.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "Revise el mes de la apertura definitiva.", Toast.LENGTH_SHORT).show();
                return false;
            }
            try {
                if(Integer.parseInt(String.valueOf(datedef.getYear())) < 2015 || Integer.parseInt(String.valueOf(datedef.getYear())) > 2030){
                    Toast.makeText(getApplicationContext(), "Revise el año de la apertura definitiva.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "Revise el año de la apertura definitiva.", Toast.LENGTH_SHORT).show();
                return false;
            }
            currentEmergecnia.setFechaDefinitiva(String.valueOf(datedef.getYear()) + "-" + String.valueOf(datedef.getMonth()) + "-" + String.valueOf(datedef.getDayOfMonth()) + " 12:12:00:0");

        }
        if(spTransitabilidad.getSelectedItemPosition() == 1) {//Nula
            try {

                currentEmergecnia.setFechaProvisional(String.valueOf(dateprov.getDayOfMonth()) + "-" + String.valueOf(dateprov.getMonth()) + "-" + String.valueOf(dateprov.getYear()));
            }catch(Exception e){
                Toast.makeText( getApplicationContext(),"Especifique una fecha válida de apertura provisional",Toast.LENGTH_SHORT ).show();
                e.printStackTrace();
                return false;
            }



            try {
                currentEmergecnia.setFechaDefinitiva(String.valueOf(datedef.getYear()) + "-" + String.valueOf(datedef.getMonth()) + "-" + String.valueOf(datedef.getDayOfMonth()));
            }catch(Exception e){
                Toast.makeText( getApplicationContext(),"Especifique una fecha válida de apertura definitiva",Toast.LENGTH_SHORT ).show();
                return false;
            }
        }

        if(((EditText)findViewById(R.id.etAcciones)).getText().toString().isEmpty()    ) {
            Toast.makeText(getApplicationContext(), "Redacte las acciones realizadas", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            currentEmergecnia.setAccionesRealizadas(((EditText)findViewById(R.id.etAcciones)).getText().toString());

        if(direccionesIncidencia == null) {
            Toast.makeText(getApplicationContext(), "Cargue la imágenes de la incidencia", Toast.LENGTH_SHORT).show();
            return false;
        }




        return true;
    }

    public BDHelper getBDMannager(){
        bdHelper = new BDHelper(this);
        return bdHelper;
    }

    public void closeConnection(){
        bdHelper.closeConnection();;
    }
    public void loadBotones(){

        //boton inicio
        findViewById(R.id.btnCGuardarIncidencia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(validarEmergencia()){
                   currentEmergecnia.setIcveTransito(spTransitabilidad.getSelectedItemPosition()+1);

                   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                   Date date = new Date();
                   currentEmergecnia.setFechaCreacion(dateFormat.format(date));
                   if(spCategoria.getVisibility() == View.VISIBLE)
                       currentEmergecnia.setIcveCategoriaEmergencia(spCategoria.getSelectedItemPosition() + 1);
                   currentEmergecnia.setIcveCausaEmergencia(spCausa.getSelectedItemPosition() + 2);
                   currentEmergecnia.setIcveDanos(spDano.getSelectedItemPosition() + 2);
                   if(switchRutaAlternativa.isChecked()) {
                       currentEmergecnia.setRutaAlternativa(1);
                        //Carga croquis
                   }
                   else
                       currentEmergecnia.setRutaAlternativa(0);

                   if(switchAuto.isChecked())
                       currentEmergecnia.setVehiculosTransitanAutos(1);
                   else
                       currentEmergecnia.setVehiculosTransitanAutos(0);

                   if(switchCamion.isChecked())
                       currentEmergecnia.setVehiculosTransitanCamiones(1);
                   else
                       currentEmergecnia.setVehiculosTransitanCamiones(0);

                   if(switchTodoTipo.isChecked()) {
                       currentEmergecnia.setVehiculosTransitanTodoTipo(1);
                       currentEmergecnia.setVehiculosTransitanCamiones(1);
                       currentEmergecnia.setVehiculosTransitanAutos(1);
                   }
                   try{
                       getBDMannager();
                       currentEmergecnia.setIcveEstado(0);//Estado de actualizacion
                       currentEmergecnia.setCcve(0);//ID del servidor
                       Long id = bdHelper.insertarEmergencia(currentEmergecnia);

                       //Toast.makeText(getApplicationContext(), "ICVE: " + id, Toast.LENGTH_SHORT).show();


                       if(switchRutaAlternativa.isChecked() && direccionesRutaAlterna != null){
                           for(String ruta : direccionesRutaAlterna){
                               Croquis croquis = new Croquis();
                               croquis.setIcveEmergencia(Integer.parseInt(Long.toString(id)));
                               croquis.setUrl(ruta);
                               bdHelper.insertarCroquis(croquis);
                               //Toast.makeText(getApplicationContext(), "Croquis insertado: " + bdHelper.insertarCroquis(croquis), Toast.LENGTH_SHORT).show();
                           }
                       }

                       for(String ruta : direccionesIncidencia){
                           Imagen imagen = new Imagen();
                           imagen.setIcveEmergencia(Integer.parseInt(Long.toString(id)));
                           imagen.setUrl(ruta);
                           bdHelper.insertarImagen(imagen);
                           //Toast.makeText(getApplicationContext(), "Imágen insertada: " + bdHelper.insertarImagen(imagen), Toast.LENGTH_SHORT).show();
                       }
                       Toast.makeText(getApplicationContext(), "Se ha almacenado la incidencia", Toast.LENGTH_SHORT).show();

                       spEntidad.setSelection(0);

                       spDano.setSelection(0);
                       ((EditText)findViewById(R.id.etDescripcion)).setText("");
                       ((EditText)findViewById(R.id.etAcciones)).setText("");

                       spTransitabilidad.setSelection(0);
                       spCausa.setSelection(0);
                       spTipoIncidencia.setSelection(0);



                       /*etDiaD.setText(Integer.toString(cal.get(cal.DATE)));
                       etMesD.setText(Integer.toString(cal.get(cal.MONTH)+1));
                       etAnioD.setText(Integer.toString(cal.get(cal.YEAR)));*/

                      /* etDiaP.setText(Integer.toString(cal.get(cal.DATE)));
                       etMesP.setText(Integer.toString(cal.get(cal.MONTH)+1));
                       etAnioP.setText(Integer.toString(cal.get(cal.YEAR)));*/
                       Calendar c = Calendar.getInstance();
                       int anop = c.get(Calendar.YEAR);
                       int mesp = c.get(Calendar.MONTH);
                       int diap = c.get(Calendar.DAY_OF_MONTH);

                       dateprov.init(anop, mesp, diap, null);
                       datedef.init(anop, mesp+1, diap, null);

                       etKmInicial.setText("");
                       etKmFinal.setText("");
                       direccionesRutaAlterna.clear();
                       direccionesIncidencia.clear();


                       closeConnection();

                   }catch(Exception e){
                       //Toast.makeText(getApplicationContext(), "Error al almacenar la incidencia", Toast.LENGTH_SHORT).show();
                       e.printStackTrace();
                   }







                   Toast.makeText( getApplicationContext(),"Se ha almacenado la incidencia",Toast.LENGTH_SHORT ).show();
               }
            }
        });

        //etDiaP
      /*  findViewById(R.id.etDiaP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDiaP.setText("");

            }
        });
        //etDiaD
        findViewById(R.id.etMesP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMesP.setText("");
            }
        });

        //etMesP
       findViewById(R.id.etAnioP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAnioP.setText("");


            }
        });
        //etMesD
        findViewById(R.id.etDiaD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDiaD.setText("");

            }
        });

        //etAnioP
       findViewById(R.id.etMesD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMesD.setText("");
            }
        });

        //etAnioD
        findViewById(R.id.etAnioD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAnioD.setText("");
            }
        });
*/

        //boton inicio
        findViewById(R.id.switchRutaAlternativa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchRutaAlternativa.isChecked())
                    btnCroquis.setVisibility(View.VISIBLE);
                else
                    btnCroquis.setVisibility(View.INVISIBLE);


            }
        });

        //boton inicio
        findViewById(R.id.btnInicio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Alta.this, Inicio.class);
                startActivity(altaForm);
                //Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();

            }
        });

        //Boton actualizacion
        findViewById(R.id.btnActualizacion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Alta.this, Actualizacion.class);
                startActivity(altaForm);


            }
        });

        //Boton mapa
        findViewById(R.id.btnMapa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Alta.this, Mapa.class);
                startActivity(altaForm);

            }
        });

        //Boton Reportes
        findViewById(R.id.btnReportes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Alta.this, Reportes.class);
                startActivity(altaForm);

            }
        });



        //Boton Confguracion
        findViewById(R.id.btnConfiguracion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Alta.this, Configuracion.class);
                startActivity(altaForm);

            }
        });


        //Boton Captura GPS
        btnCcapturaGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        currentEmergecnia.setAltitud(Double.parseDouble(etAltitud.getText().toString()));
                        currentEmergecnia.setLatitud(Double.parseDouble(etLatitud.getText().toString()));
                        currentEmergecnia.setLongitud(Double.parseDouble(etLongitud.getText().toString()));
                        btnCcapturaGPS.setText("GPS Capturado");
                        Toast.makeText(getApplicationContext(), "GPS Capturado", Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Error al obtener el GPS", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();;
                }
            }
        });

        //Boton Galería
        findViewById(R.id.btnCroquis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galeriaForm = new Intent(Alta.this, Galeria.class);
                alterna0OrIncidencia1 = 0;
                Bundle tunel = new Bundle();
                tunel.putSerializable("incidencia", currentEmergecnia);
                if(direccionesRutaAlterna != null)
                    galeriaForm.putStringArrayListExtra("rutaImagenes", direccionesRutaAlterna);
                galeriaForm.putExtras(tunel);
                startActivityForResult(galeriaForm, 1);

            }
        });


        //Boton Adjuntar Fotos Incidencia
        btnVerAdjuntarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galeriaForm = new Intent(Alta.this, Galeria.class);
                alterna0OrIncidencia1 = 1;
                Bundle tunel = new Bundle();
                tunel.putSerializable("incidencia", currentEmergecnia);
                if(direccionesIncidencia != null)
                    galeriaForm.putStringArrayListExtra("rutaImagenes", direccionesIncidencia);
                galeriaForm.putExtras(tunel);
                startActivityForResult(galeriaForm, 1);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle parametros = data.getExtras();
        Bundle extras = data.getExtras();
        if (resultCode == RESULT_OK)
            if(parametros != null) {
                Bundle MBuddle = data.getExtras();
                if(alterna0OrIncidencia1 == 0) {
                    direccionesRutaAlterna = MBuddle.getStringArrayList("rutaImagenes");
                   // for (String dir : direccionesRutaAlterna) {
                   // //    Toast.makeText(getApplicationContext(), "Imágen almacenada: "+dir, Toast.LENGTH_SHORT).show();
                   // }

                }
                else {
                    direccionesIncidencia = MBuddle.getStringArrayList("rutaImagenes");
                    //for (String dir : direccionesIncidencia) {
                       // Toast.makeText(getApplicationContext(), "Imágen almacenada" + dir, Toast.LENGTH_SHORT).show();
                   // }
                }
            }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int seleccionado;
        switch(parent.getId()){
            case(R.id.spEntidad):
                BDHelper bdHelper = new BDHelper(this);
                opcCarretera.clear();
                opcTramos.clear();
                opcSubtramos.clear();
                //opcEstado.add("Seleccione");
                opcCarretera.add("Seleccione");
                opcTramos.add("Seleccione");
                opcSubtramos.add("Seleccione");
                carreteraList = bdHelper.findCarreterasByEstado(opcEstado.get(spEntidad.getSelectedItemPosition()));
                for(Carretera carretera : carreteraList){
                    opcCarretera.add(carretera.getNombre());
                }
                bdHelper.closeConnection();
                currentEmergecnia.setIcveSubtramo(0);
                aaCarretera = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcCarretera);
                spCarretera.setAdapter(aaCarretera);

                break;
            case(R.id.spCarretera):
                bdHelper = new BDHelper(this);
                currentEmergecnia.setIcveSubtramo(0);
                opcTramos.clear();
                opcSubtramos.clear();
                //opcCarretera.add("Seleccione");
                opcTramos.add("Seleccione");
                opcSubtramos.add("Seleccione");
                tramoList = bdHelper.findTramosByCarreteras(opcEstado.get(spEntidad.getSelectedItemPosition()), opcCarretera.get(spCarretera.getSelectedItemPosition()));
                for(Tramo tramo : tramoList){
                    opcTramos.add(tramo.getNombre());
                }
                if(!carreteraList.isEmpty() && position > 1)
                    currentEmergecnia.setIcveCarretera(carreteraList.get(position-1).getIcve());

                aaTramo = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcTramos);
                spTramo.setAdapter(aaTramo);
                bdHelper.closeConnection();
                break;
            case(R.id.spTramo):
                bdHelper = new BDHelper(this);
                currentEmergecnia.setIcveSubtramo(0);
                opcSubtramos.clear();
                subtramoList.clear();
                opcSubtramos.add("Seleccione");
                subtramoList = bdHelper.findSubtramosByTramo(opcEstado.get(spEntidad.getSelectedItemPosition()), opcCarretera.get(spCarretera.getSelectedItemPosition()), opcTramos.get(spTramo.getSelectedItemPosition()));
                //Tramo currenTramo = bdHelper.findTramoByCarreteraTramo(opcCarretera.get(spCarretera.getSelectedItemPosition()), opcTramos.get(spTramo.getSelectedItemPosition())).get(0);
                //currentEmergecnia.setIcveTramo(currenTramo.getIcve());
                for(Subtramo subtramo : subtramoList){
                    opcSubtramos.add(subtramo.getNombre());
                }
                if(!tramoList.isEmpty() && position > 1)
                    currentEmergecnia.setIcveTramo(tramoList.get(position-1).getIcve());
                aaSubtramo = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcSubtramos);
                spSubtramo.setAdapter(aaSubtramo);

                bdHelper.closeConnection();
                break;

            case(R.id.spSubtramo):
                if(!subtramoList.isEmpty()  && position > 0) {
                    subtramoActual = subtramoList.get(spSubtramo.getSelectedItemPosition()-1);
                    currentEmergecnia.setIcveSubtramo(subtramoActual.getIcve());
                    if (subtramoActual != null)
                        if (subtramoActual.getKmInicial() != null && subtramoActual.getKmFinal() != null) {
                            etKmInicial.setText(subtramoActual.getKmInicial().replace(".", "+"));
                            etKmFinal.setText(subtramoActual.getKmFinal().replace(".", "+"));
                        }

                }
                break;

            case(R.id.spCausa):
                opcTipoEmergencia.clear();
                bdHelper = new BDHelper(this);
                opcTipoEmergencia.add("SELECCIONE");
                for(TipoEmergencia tipoEmergencia : bdHelper.findTiposEmergenciaByCausa(opcCausaEmergencia.get(spCausa.getSelectedItemPosition())))
                    opcTipoEmergencia.add(tipoEmergencia.getNombre());
                aaTipoIncidencia = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcTipoEmergencia);
                spTipoIncidencia.setAdapter(aaTipoIncidencia);
                bdHelper.closeConnection();
                break;

            case(R.id.spTipoIncidencia):
                opcCategoria.clear();
                bdHelper = new BDHelper(this);
                if(opcTipoEmergencia.size() != 1 && spTipoIncidencia.getSelectedItemPosition() != 0) {
                    TipoEmergencia tipo = bdHelper.findTipoEmergenciaByNombre(opcTipoEmergencia.get(spTipoIncidencia.getSelectedItemPosition())).get(0);
                    currentEmergecnia.setIcveTipoEmergencia(tipo.getIcve());
                    opcCategoria.add("SELECCIONE");
                    for (CategoriaEmergencia categoria : bdHelper.findCategoriaEmergenciaByTipo(opcTipoEmergencia.get(spTipoIncidencia.getSelectedItemPosition())))
                        opcCategoria.add(categoria.getNombre());
                    aaCategoria = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcCategoria);
                    spCategoria.setAdapter(aaCategoria);
                    bdHelper.closeConnection();
                    if (!opcCategoria.isEmpty()) {
                        findViewById(R.id.spCategoria).setVisibility(View.VISIBLE);
                        findViewById(R.id.txtCategoria).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.spCategoria).setVisibility(View.INVISIBLE);
                        findViewById(R.id.txtCategoria).setVisibility(View.INVISIBLE);
                    }
                }
                break;

            case(R.id.spTransitabilidad):
                //aaTransitabilidad.clear();
                switchAuto.setChecked(false);
                switchCamion.setChecked(false);
                switchTodoTipo.setChecked(false);
                switchAuto.setEnabled(true);
                switchCamion.setEnabled(true);
                switchTodoTipo.setEnabled(true);
                if(spTransitabilidad.getSelectedItemPosition() == 1) {//Nula
                    //se desabilitan todos
                    switchAuto.setChecked(false);
                    switchCamion.setChecked(false);
                    switchTodoTipo.setChecked(false);
                    switchAuto.setEnabled(false);
                    switchCamion.setEnabled(false);
                    switchTodoTipo.setEnabled(false);
                    //se habilitan las fechas de apertura provisional definitiva
                    /*etDiaD.setEnabled(true);
                    etMesD.setEnabled(true);
                    etAnioD.setEnabled(true);*/
                    dateprov.setEnabled(true);
                    datedef.setEnabled(true);
                    /*etDiaP.setEnabled(true);
                    etMesP.setEnabled(true);
                    etAnioP.setEnabled(true);*/
                    //
                    switchRutaAlternativa.setVisibility(View.VISIBLE);
                    switchRutaAlternativa.setChecked(false);
                }else if(spTransitabilidad.getSelectedItemPosition() == 2) {
                    //se desablitan las fechas de apertura provisional definitiva
                   /* etDiaD.setEnabled(false);
                    etMesD.setEnabled(false);
                    etAnioD.setEnabled(false);*/
                    dateprov.setEnabled(false);
                    datedef.setEnabled(false);
                    /*etDiaP.setEnabled(false);
                    etMesP.setEnabled(false);
                    etAnioP.setEnabled(false);*/
                    //se habilitan todos
                    switchAuto.setChecked(true);
                    switchCamion.setChecked(true);
                    switchTodoTipo.setChecked(true);
                    switchAuto.setEnabled(false);
                    switchCamion.setEnabled(false);
                    switchTodoTipo.setEnabled(false);

                    btnCroquis.setVisibility(View.INVISIBLE);
                    //switchRutaAlternativa.setEnabled(false);
                    //switchRutaAlternativa.setChecked(false);

                }else{
                    switchRutaAlternativa.setEnabled(true);
                    switchRutaAlternativa.setChecked(false);
                    btnCroquis.setVisibility(View.INVISIBLE);

                /*    etDiaD.setEnabled(true);
                    etMesD.setEnabled(true);
                    etAnioD.setEnabled(true);*/
                    dateprov.setEnabled(true);
                    dateprov.setEnabled(true);
                   /* etDiaP.setEnabled(true);
                    etMesP.setEnabled(true);
                    etAnioP.setEnabled(true);*/
                    break;
                /*else if(spTransitabilidad.getSelectedItemPosition() == 2) {//provisional
                }else if(spTransitabilidad.getSelectedItemPosition() == 3) {//Parcial
                }*/
                }
                break;
            default:

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
