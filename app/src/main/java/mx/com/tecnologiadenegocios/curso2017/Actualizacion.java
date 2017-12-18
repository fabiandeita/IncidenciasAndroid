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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.com.tecnologiadenegocios.curso2017.model.Emergencia;
import mx.com.tecnologiadenegocios.curso2017.model.Estado;
import mx.com.tecnologiadenegocios.curso2017.model.Tramo;
import mx.com.tecnologiadenegocios.curso2017.R;
import mx.com.tecnologiadenegocios.curso2017.helper.BDHelper;
import mx.com.tecnologiadenegocios.curso2017.model.Carretera;
import mx.com.tecnologiadenegocios.curso2017.model.Subtramo;

/**
 * Created by Ing.Sergio Martínez on 22/06/2015.
 */
public class Actualizacion extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
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
    private String[] columnasBD = new String[] {"_id", "textoSuperior"};
    private MatrixCursor cursor = new MatrixCursor(columnasBD);
    private String[] desdeEstasColumnas = {"textoSuperior"};
    //String[] desdeEstasColumnas = {"imagen", "textoSuperior"};
    private int[] aEstasViews = {R.id.textView_superior};
    //int[] aEstasViews = {R.id.imageView_imagen, R.id.textView_superior};
    private SimpleCursorAdapter adapter;
    private Emergencia currentEmergecnia = new Emergencia();
    private BDHelper bdHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Toast.makeText(getApplicationContext(), "Inicio", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_actualizacion);

        switchAuto = (Switch)findViewById(R.id.switchAutos);
        switchCamion = (Switch)findViewById(R.id.switchCamiones);
        switchTodoTipo = (Switch)findViewById(R.id.switchTodoTipo);

        opcCarretera.add("Seleccione");
        opcTramos.add("Seleccione");
        opcSubtramos.add("Seleccione");
        seleccione = (TextView)findViewById(R.id.txtSeleccione);
        seleccione.setVisibility(View.INVISIBLE);
        loadSpinners();


        loadBotones();

    }

    public void loadSpinners() {
        BDHelper bdHelper = new BDHelper(this);
        //Toast.makeText(getApplicationContext(), "Se llena el Spinner", Toast.LENGTH_SHORT).show();
        opcEstado.add("Seleccione");
        for (Estado estado : bdHelper.findEstados()) {
            if (!estado.getNombre().equals("Distrito Federal"))
                opcEstado.add(estado.getNombre());
        }


        bdHelper.closeConnection();


        //Bindding Spinner
        spEntidad = (Spinner)findViewById(R.id.spEntidad);
        spCarretera = (Spinner)findViewById(R.id.spCarretera);
        spTramo = (Spinner)findViewById(R.id.spTramo);
        spSubtramo = (Spinner)findViewById(R.id.spSubtramo);

        lvEmergencias = (ListView) findViewById(R.id.lvIncidencias);
        lvEmergencias.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                //Toast.makeText(getApplicationContext(), "ViewList: ", Toast.LENGTH_SHORT).show();
                Intent galeriaForm = new Intent(Actualizacion.this, AccionConcepto.class);
                galeriaForm.putExtra("icveIncidencia", emergenciaList.get(position).getIcve());
                startActivityForResult(galeriaForm, 1);

            }
        });
        //lvEmergencias.setOnItemSelectedListener(this);
        //se agrega el listener
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), parent.toString() + position + id, Toast.LENGTH_SHORT).show();

            switch (parent.getId()) {
                case (R.id.spEntidad):
                    BDHelper bdHelper = new BDHelper(this);
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

                    if(position != 0){
                        emergenciaList = bdHelper.findEmergenciasByEntidad(position);

                        if(!emergenciaList.isEmpty())
                            seleccione.setVisibility(View.VISIBLE);
                        else
                            seleccione.setVisibility(View.INVISIBLE);
                        cursor = new MatrixCursor(columnasBD);
                        for(int c = 0;  c < emergenciaList.size(); c++)
                            cursor.addRow(new Object[] {c, emergenciaList.get(c).getFechaCreacion() + "  -  " + emergenciaList.get(c).getDescripcion()});
                            //cursor.addRow(new Object[] {"0", R.drawable.logo_tecnologia, eme.getDescripcion()});

                        lvEmergencias.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 40 * cursor.getCount()));
                        adapter = new SimpleCursorAdapter(this, R.layout.entrada, cursor, desdeEstasColumnas, aEstasViews, 0);

                        lvEmergencias.setAdapter(adapter);
                        lvEmergencias.setClickable(true);

                    }
                    bdHelper.closeConnection();
                    aaCarretera = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcCarretera);
                    spCarretera.setAdapter(aaCarretera);
                    break;
                case (R.id.spCarretera):
                    bdHelper = new BDHelper(this);
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
                        if(!emergenciaList.isEmpty())
                            seleccione.setVisibility(View.VISIBLE);
                        else
                            seleccione.setVisibility(View.INVISIBLE);
                        cursor = new MatrixCursor(columnasBD);
                        for(Emergencia eme : emergenciaList)
                            cursor.addRow(new Object[] {"0", eme.getDescripcion()});
                        //cursor.addRow(new Object[] {"0", R.drawable.logo_tecnologia, eme.getDescripcion()});

                        lvEmergencias.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 40 * cursor.getCount()));
                        adapter = new SimpleCursorAdapter(this, R.layout.entrada, cursor, desdeEstasColumnas, aEstasViews, 0);

                        lvEmergencias.setAdapter(adapter);
                    }


                    aaTramo = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcTramos);
                    spTramo.setAdapter(aaTramo);
                    bdHelper.closeConnection();
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
                        if(!emergenciaList.isEmpty())
                            seleccione.setVisibility(View.VISIBLE);
                        else
                            seleccione.setVisibility(View.INVISIBLE);
                        cursor = new MatrixCursor(columnasBD);
                        for(Emergencia eme : emergenciaList)
                            cursor.addRow(new Object[] {"0", eme.getDescripcion()});
                        //cursor.addRow(new Object[] {"0", R.drawable.logo_tecnologia, eme.getDescripcion()});

                        lvEmergencias.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 40 * cursor.getCount()));
                        adapter = new SimpleCursorAdapter(this, R.layout.entrada, cursor, desdeEstasColumnas, aEstasViews, 0);

                        lvEmergencias.setAdapter(adapter);
                    }
                    bdHelper.closeConnection();

                    aaSubtramo = new ArrayAdapter<String>(this, R.layout.textview_spinne, opcSubtramos);
                    spSubtramo.setAdapter(aaSubtramo);
                    break;

                case (R.id.spSubtramo):
                    bdHelper = new BDHelper(this);

                    if(position != 0){
                        opcLVEmergencias.clear();
                        emergenciaList = bdHelper.findEmergenciasBySubtramo(subtramoList.get(position-1).getIcve());
                        if(!emergenciaList.isEmpty())
                            seleccione.setVisibility(View.VISIBLE);
                        else
                            seleccione.setVisibility(View.INVISIBLE);
                        cursor = new MatrixCursor(columnasBD);
                        for(Emergencia eme : emergenciaList)
                            cursor.addRow(new Object[] {"0", eme.getDescripcion()});
                        //cursor.addRow(new Object[] {"0", R.drawable.logo_tecnologia, eme.getDescripcion()});

                        lvEmergencias.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 40 * cursor.getCount()));
                        adapter = new SimpleCursorAdapter(this, R.layout.entrada, cursor, desdeEstasColumnas, aEstasViews, 0);

                        lvEmergencias.setAdapter(adapter);

                    }
                    bdHelper.closeConnection();
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
                Intent altaForm = new Intent(Actualizacion.this, Inicio.class);
                startActivity(altaForm);
                //Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();

            }
        });

        //boton alta
        findViewById(R.id.btnAlta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Actualizacion.this, Alta.class);
                startActivity(altaForm);
                //Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();

            }
        });

        //Boton actualizacion
        findViewById(R.id.btnReportes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Actualizacion.this, Reportes.class);
                startActivity(altaForm);


            }
        });



        //Boton Mapa
        findViewById(R.id.btnMapa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Actualizacion.this, Mapa.class);
                startActivity(altaForm);

            }
        });



        //Boton Confguracion
        findViewById(R.id.btnConfiguracion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Actualizacion.this, Configuracion.class);
                startActivity(altaForm);

            }
        });
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
