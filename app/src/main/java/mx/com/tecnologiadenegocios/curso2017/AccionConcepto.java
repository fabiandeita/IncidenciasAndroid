package mx.com.tecnologiadenegocios.curso2017;

import android.content.Intent;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.tecnologiadenegocios.curso2017.model.Actividad;
import mx.com.tecnologiadenegocios.curso2017.model.Concepto;
import mx.com.tecnologiadenegocios.curso2017.model.Emergencia;
import mx.com.tecnologiadenegocios.curso2017.model.Imagen;
import mx.com.tecnologiadenegocios.curso2017.R;
import mx.com.tecnologiadenegocios.curso2017.helper.BDHelper;

/**
 * Created by Ing.Sergio Martínez on 22/06/2015.
 */
public class AccionConcepto extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private List<String> opcTransitabilidad = new ArrayList<>();
    private List<String> opcUnidad = new ArrayList<>();
    private List<String> opcAvance= new ArrayList<>();
    private List<Emergencia> emergenciaList = new ArrayList<>();
    private Spinner spTransitabilidad, spUnidad, spAvance;
    private ArrayAdapter<String> aaTransitabilidad, aaUnidad, aaAvance;
    private Switch swAutos, swCamiones, swTodoTipo;
    private TextView tvAccion;
    private EditText etConcepto, etCantidad;
    private BDHelper bdHelper;
    private ListView lvActividades, lvConceptos;
    private List<Actividad> actividadList = new ArrayList<>();
    private List<Concepto> conceptoList = new ArrayList<>();
    private String[] columnasBD = new String[] {"_id", "textoSuperior"};
    private String[] columnasBDConceptos = new String[] {"_id", "textoSuperior"};
    private MatrixCursor cursor = new MatrixCursor(columnasBD);
    private MatrixCursor cursorConcepto = new MatrixCursor(columnasBD);
    private String[] desdeEstasColumnas = {"textoSuperior"};
    private String[] desdeEstasColumnasConcepto = {"textoSuperior"};
    //String[] desdeEstasColumnas = {"imagen", "textoSuperior"};
    private int[] aEstasViews = {R.id.textView_superior};
    private int[] aEstasViewsConcepto = {R.id.textView_superior};
    //int[] aEstasViews = {R.id.imageView_imagen, R.id.textView_superior};
    private SimpleCursorAdapter adapter;
    private SimpleCursorAdapter adapterConcepto;
    private Concepto currentConcepto = new Concepto();
    private ArrayList<String> direccionesConcepto;
    private Emergencia currentEmergencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Toast.makeText(getApplicationContext(), "Inicio", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_accion_concepto);

        etConcepto = (EditText)findViewById(R.id.etConcepto);
        etCantidad = (EditText)findViewById(R.id.etCantidad);

        opcTransitabilidad.add("NULO");
        opcTransitabilidad.add("TOTAL");
        opcTransitabilidad.add("PROVISIONAL");
        opcTransitabilidad.add("PARCIAL");

        opcUnidad.add("ML");
        opcUnidad.add("M2");
        opcUnidad.add("M3");
        opcUnidad.add("Pieza");
        opcUnidad.add("Tonelada");
        opcUnidad.add("Kilogramos");
        opcUnidad.add("Litros");
        opcUnidad.add("Otro");

        opcAvance.add("0");
        opcAvance.add("5");
        opcAvance.add("10");
        opcAvance.add("15");
        opcAvance.add("20");
        opcAvance.add("25");
        opcAvance.add("30");
        opcAvance.add("35");
        opcAvance.add("40");
        opcAvance.add("45");
        opcAvance.add("50");
        opcAvance.add("55");
        opcAvance.add("60");
        opcAvance.add("65");
        opcAvance.add("70");
        opcAvance.add("75");
        opcAvance.add("80");
        opcAvance.add("85");
        opcAvance.add("90");
        opcAvance.add("95");
        opcAvance.add("100");


        spTransitabilidad = (Spinner)findViewById(R.id.spTransitabilidad);
        spTransitabilidad.setOnItemSelectedListener(this);
        aaTransitabilidad = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcTransitabilidad);
        spTransitabilidad.setAdapter(aaTransitabilidad);

        spUnidad = (Spinner)findViewById(R.id.spUnidad);
        spUnidad.setOnItemSelectedListener(this);
        aaUnidad = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcUnidad);
        spUnidad.setAdapter(aaUnidad);

        spAvance = (Spinner)findViewById(R.id.spAvance);
        spAvance.setOnItemSelectedListener(this);
        aaAvance = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcAvance);
        spAvance.setAdapter(aaAvance);

        swAutos = (Switch)findViewById(R.id.swAutos);
        swCamiones = (Switch)findViewById(R.id.swCamiones);
        swTodoTipo = (Switch)findViewById(R.id.swTodoTipo);

        swAutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.swAutos){
                    if(swAutos.isChecked()){
                        swAutos.setChecked(true);
                        swCamiones.setChecked(false);
                        swTodoTipo.setChecked(false);
                    }else
                        swTodoTipo.setChecked(false);
                }
            }
        });

        swCamiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.swCamiones){
                    if(swCamiones.isChecked()){
                        swAutos.setChecked(false);
                        swCamiones.setChecked(true);
                        swTodoTipo.setChecked(false);
                    }else
                        swTodoTipo.setChecked(false);
                }
            }
        });

        swTodoTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.swTodoTipo){
                    if(swTodoTipo.isChecked()){
                        swAutos.setChecked(true);
                        swCamiones.setChecked(true);
                        swTodoTipo.setChecked(true);
                    }else{
                        swAutos.setChecked(false);
                        swCamiones.setChecked(false);
                        swTodoTipo.setChecked(false);
                    }
                }
            }
        });
        tvAccion = (TextView)findViewById(R.id.tvAccion);

        lvConceptos = (ListView)findViewById(R.id.lvConceptos);
        lvActividades = (ListView)findViewById(R.id.lvActividades);

        lvActividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Actividad currentActividad = actividadList.get(position);
                tvAccion.setText(currentActividad.getDescripcion());
                spTransitabilidad.setSelection(currentEmergencia.getIcveTransito());
                swAutos.setChecked(currentEmergencia.getVehiculosTransitanAutos() == 0 ? false : true);
                swCamiones.setChecked(currentEmergencia.getVehiculosTransitanCamiones() == 0 ? false : true);
                swTodoTipo.setChecked(currentEmergencia.getVehiculosTransitanTodoTipo() == 0 ? false : true);

            }
        });

        lvConceptos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Concepto currentConcepto = conceptoList.get(position);
                etConcepto.setText(currentConcepto.getDescripcion());
                //Toast.makeText(getApplicationContext(), currentConcepto.getCantidad(), Toast.LENGTH_SHORT).show();
                ((EditText)findViewById(R.id.etCantidad)).setText(Integer.toString(currentConcepto.getCantidad()));
                spUnidad.setSelection(currentConcepto.getIcveUnidad());
                spAvance.setSelection(currentConcepto.getIporAvance() / 5);

            }
        });
        bdHelper = getConnection();
        currentEmergencia = (bdHelper.findIncidenciaById(getIntent().getExtras().getInt("icveIncidencia"))).get(0);
        fillActividades(getIntent().getExtras().getInt("icveIncidencia"));
        fillConceptos(getIntent().getExtras().getInt("icveIncidencia"));
        closeConnection();



        loadBotones();
    }

    public void fillConceptos(int icveIncidencia){
        conceptoList = bdHelper.findConceptoByIncidenciaOrder(icveIncidencia);
        cursorConcepto = new MatrixCursor(columnasBDConceptos);
        for(int i = 0; i < conceptoList.size(); i++)
            cursorConcepto.addRow(new Object[] {i,  conceptoList.get(i).getFechaCreacion() +" Avance: " + (conceptoList.get(i).getIporAvance() ) + "% " + " - " + conceptoList.get(i).getDescripcion()});

        lvConceptos.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 40 * cursorConcepto.getCount()));
        adapterConcepto = new SimpleCursorAdapter(this, R.layout.entrada, cursorConcepto, desdeEstasColumnasConcepto, aEstasViewsConcepto, 0);

        lvConceptos.setAdapter(adapterConcepto);
        lvConceptos.setClickable(true);
    }

    public void fillActividades(int icveIncidencia){
        actividadList = bdHelper.findActividadesByIncidencia(icveIncidencia);
        cursor = new MatrixCursor(columnasBD);
        for(int i = 0; i < actividadList.size(); i++)
            cursor.addRow(new Object[] {i, actividadList.get(i).getFechaCreacion() + " - " + actividadList.get(i).getDescripcion()});

        lvActividades.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 40 * cursor.getCount()));
        adapter = new SimpleCursorAdapter(this, R.layout.entrada, cursor, desdeEstasColumnas, aEstasViews, 0);

        lvActividades.setAdapter(adapter);
        lvActividades.setClickable(true);
    }

    public BDHelper getConnection(){
        return bdHelper = new BDHelper(this);
    }

    public void closeConnection(){
        bdHelper.closeConnection();
    }


    public boolean validarAccion(){
        if(tvAccion.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Favor de redactar la actividad.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean validarConcepto(){
        if(etConcepto.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Favor de redactar el concepto.", Toast.LENGTH_SHORT).show();
            return false;
        }
        currentConcepto.setDescripcion(etConcepto.getText().toString());
        try{
            currentConcepto.setCantidad(Integer.parseInt(etCantidad.getText().toString()));
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "La cantidad debe ser un número entero.", Toast.LENGTH_SHORT).show();
            return false;
        }
        /*
        if(spUnidad.getSelectedItemPosition() == 0){
            Toast.makeText(getApplicationContext(), "Seleccione la unidad.", Toast.LENGTH_SHORT).show();
            return false;
        }
        currentConcepto.setIcveUnidad(spUnidad.getSelectedItemPosition());
       /* if(spAvance.getSelectedItemPosition() == 0){
            Toast.makeText(getApplicationContext(), "Seleccione el avance.", Toast.LENGTH_SHORT).show();
            return false;
        }*/

        currentConcepto.setIporAvance(spAvance.getSelectedItemPosition() * 5);
        currentConcepto.setIcveUnidad(spUnidad.getSelectedItemPosition() );
        return true;
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()) {
            case (R.id.spTransitabilidad):
                if (position == 1) {
                    swAutos.setChecked(true);
                    swCamiones.setChecked(true);
                    swTodoTipo.setChecked(true);

                    swAutos.setEnabled(false);
                    swCamiones.setEnabled(false);
                    swTodoTipo.setEnabled(false);
                } else if (position == 0) {
                    swAutos.setChecked(false);
                    swCamiones.setChecked(false);
                    swTodoTipo.setChecked(false);

                    swAutos.setEnabled(false);
                    swCamiones.setEnabled(false);
                    swTodoTipo.setEnabled(false);
                } else {
                    swAutos.setEnabled(true);
                    swCamiones.setEnabled(true);
                    swTodoTipo.setEnabled(true);
                }
                break;
        }
    }

    public void loadBotones(){
        //Boton Agregar Acción
        findViewById(R.id.btnAgregarAccion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarAccion()){
                    bdHelper = getConnection();
                    Actividad actividad = new Actividad();
                    actividad.setIcveEmergencia(getIntent().getExtras().getInt("icveIncidencia"));
                    actividad.setDescripcion(tvAccion.getText().toString());
                    actividad.setTransito(spTransitabilidad.getSelectedItemPosition());
                    //DateFormat dt1 = DateFormat.getDateInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                    Date date = new Date();

                    actividad.setFechaCreacion(dateFormat.format(date));
                    bdHelper.insertarActividad(actividad);
                    Toast.makeText(getApplicationContext(), "Actividad agregada correctamente.", Toast.LENGTH_SHORT).show();
                    fillActividades(actividad.getIcveEmergencia());
                    bdHelper.updateIncidenciaEstado(getIntent().getExtras().getInt("icveIncidencia"), 2);
                    currentEmergencia.setIcveTransito(spTransitabilidad.getSelectedItemPosition());
                    currentEmergencia.setVehiculosTransitanAutos(swAutos.isChecked() ? 1:0);
                    currentEmergencia.setVehiculosTransitanCamiones(swCamiones.isChecked() ? 1 : 0);
                    currentEmergencia.setVehiculosTransitanTodoTipo(swTodoTipo.isChecked() ? 1 : 0);
                    bdHelper.updateIncidenciaTransito(currentEmergencia.getIcve(), currentEmergencia.getIcveTransito(), currentEmergencia.getVehiculosTransitanAutos()
                            , currentEmergencia.getVehiculosTransitanCamiones(), currentEmergencia.getVehiculosTransitanTodoTipo());
                    closeConnection();

                    tvAccion.setText("");
                    spTransitabilidad.setSelection(0);
                }
                //Terminar accion
                //finish();

            }
        });
        //btnRegresar
        findViewById(R.id.btnRegresar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Terminar accion
                finish();
            }
        });

        //boton inicio
        findViewById(R.id.btnInicio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(AccionConcepto.this, Inicio.class);
                startActivity(altaForm);
                //Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();

            }
        });

        //boton inicio
        findViewById(R.id.btnAlta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(AccionConcepto.this, Alta.class);
                startActivity(altaForm);
                //Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();

            }
        });

        //Boton actualizacion
        findViewById(R.id.btnActualizacion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(AccionConcepto.this, Inicio.class);
                startActivity(altaForm);


            }
        });

        //Boton mapa
        findViewById(R.id.btnMapa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(AccionConcepto.this, Mapa.class);
                startActivity(altaForm);

            }
        });

        //Boton Reportes
        findViewById(R.id.btnReportes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(AccionConcepto.this, Reportes.class);
                startActivity(altaForm);

            }
        });



        //Boton Confguracion
        findViewById(R.id.btnConfiguracion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(AccionConcepto.this, Configuracion.class);
                startActivity(altaForm);

            }
        });




        //Boton Galería
        findViewById(R.id.btnAdjuntarFotos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galeriaForm = new Intent(AccionConcepto.this, Galeria.class);
                //alterna0OrIncidencia1 = 0;
                //Bundle tunel = new Bundle();
                //tunel.putSerializable("incidencia", currentEmergecnia);
                //galeriaForm.putExtras(tunel);
                if(direccionesConcepto != null)
                    galeriaForm.putStringArrayListExtra("rutaImagenes", direccionesConcepto);
                Bundle tunel = new Bundle();
                galeriaForm.putExtras(tunel);
                startActivityForResult(galeriaForm, 1);

            }
        });




        //Boton AGREGAR CONCEPTO
        findViewById(R.id.btnAgregarConcepto).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bdHelper = getConnection();
                    if(validarConcepto()){


                        currentConcepto.setIcveEmergencia(getIntent().getExtras().getInt("icveIncidencia"));
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                        Date date = new Date();
                        currentConcepto.setFechaCreacion(dateFormat.format(date));


                       // bdHelper = getConnection();
                        Long id = bdHelper.insertarConcepto(currentConcepto);
                        Toast.makeText(getApplicationContext(), "Concepto agregado correctamente.", Toast.LENGTH_SHORT).show();

                       if( direccionesConcepto != null)
                        for(String s : direccionesConcepto) {
                            Imagen imagen = new Imagen();
                            imagen.setIcveConcepto(id.intValue());
                            imagen.setUrl(s);
                            bdHelper.insertarImagen(imagen);

                        }

                        fillConceptos(currentConcepto.getIcveEmergencia());


                        currentConcepto = new Concepto();
                        bdHelper.updateIncidenciaEstado(getIntent().getExtras().getInt("icveIncidencia"), 2);


                        etConcepto.setText("");
                        etCantidad.setText("");
                        spAvance.setSelection(0);
                        spUnidad.setSelection(0);


                    }
                    closeConnection();

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
                    direccionesConcepto = MBuddle.getStringArrayList("rutaImagenes");

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
