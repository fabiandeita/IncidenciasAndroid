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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.com.tecnologiadenegocios.curso2017.helper.BDHelper;
import mx.com.tecnologiadenegocios.curso2017.model.Actividad;
import mx.com.tecnologiadenegocios.curso2017.model.Concepto;
import mx.com.tecnologiadenegocios.curso2017.R;

/**
 * Created by Ing.Sergio Martínez on 22/06/2015.
 */
public class Detalle extends ActionBarActivity {

    private ListView lvConceptos, lvAcciones;
    private List<String> opcLVEmergencias = new ArrayList<>();
    private String[] columnasBD = new String[] {"_id", "imagen", "textoSuperior"};
    private MatrixCursor cursor = new MatrixCursor(columnasBD);
    private MatrixCursor cursorAccion = new MatrixCursor(columnasBD);
    private String[] desdeEstasColumnas = {"imagen", "textoSuperior"};
    //String[] desdeEstasColumnas = {"imagen", "textoSuperior"};
    private int[] aEstasViews = {R.id.imageView_imagen, R.id.textView_superior};
    private BDHelper bdHelper;
    private List<Concepto> conceptoList = new ArrayList<>();
    private List<Actividad> accionList = new ArrayList<>();
    private SimpleCursorAdapter adapter, adapterAccion;
    private int icveIcidencia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_incidencia);
        lvConceptos = (ListView)findViewById(R.id.lvConceptos);
        lvAcciones = (ListView)findViewById(R.id.lvAcciones);

        icveIcidencia = getIntent().getExtras().getInt("icveIncidencia");
        ((TextView)findViewById(R.id.txtIncidenciaDescr)).setText("Incidencia: " + getIntent().getExtras().getString("descripcion"));

        bdHelper = new BDHelper(this);
        conceptoList = bdHelper.findConceptoByIncidencia(icveIcidencia);
        accionList = bdHelper.findActividadesByIncidencia(icveIcidencia);

        cursor = new MatrixCursor(columnasBD);
        for(int c = 0;  c < conceptoList.size(); c++) {
            if (conceptoList.get(c).getCcve() > 0)
                cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_verde, "  -  " + conceptoList.get(c).getDescripcion() });
            else
                cursor.addRow(new Object[]{c, R.mipmap.iconos_admiracion_amarillo, conceptoList.get(c).getDescripcion() });

        }

        cursorAccion = new MatrixCursor(columnasBD);
        for(int c = 0;  c < accionList.size(); c++) {
            if (accionList.get(c).getCcve() > 0)
                cursorAccion.addRow(new Object[]{c, R.mipmap.iconos_admiracion_verde, "  -  " + accionList.get(c).getDescripcion() });
            else
                cursorAccion.addRow(new Object[]{c, R.mipmap.iconos_admiracion_amarillo, accionList.get(c).getDescripcion() });

        }

        //cursor.addRow(new Object[] {"0", R.drawable.logo_tecnologia, eme.getDescripcion()});

        lvConceptos.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 90 * cursor.getCount()));
        adapter = new SimpleCursorAdapter(this, R.layout.entrada2, cursor, desdeEstasColumnas, aEstasViews, 0);

        lvAcciones.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, 90 * cursorAccion.getCount()));
        adapterAccion = new SimpleCursorAdapter(this, R.layout.entrada2, cursorAccion, desdeEstasColumnas, aEstasViews, 0);

        lvConceptos.setAdapter(adapter);
        lvConceptos.setClickable(true);

        lvAcciones.setAdapter(adapterAccion);
        lvAcciones.setClickable(true);
        closeBdHelper();
        loadBotones();
    }

    public void closeBdHelper(){
        if(bdHelper != null)
            bdHelper.closeConnection();
    }
    public void loadBotones(){

        //boton inicio
        findViewById(R.id.btnInicio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Detalle.this, Inicio.class);
                startActivity(altaForm);

            }
        });

        //boton alta
        findViewById(R.id.btnAlta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Detalle.this, Alta.class);
                startActivity(altaForm);


            }
        });

        //Boton reportes
        findViewById(R.id.btnReportes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Detalle.this, Reportes.class);
                startActivity(altaForm);


            }
        });



        //Boton Mapa
        findViewById(R.id.btnMapa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Detalle.this, Mapa.class);
                startActivity(altaForm);

            }
        });

        //btn Return

        //Boton Actualizacion
        findViewById(R.id.btnActualizacion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Detalle.this, Actualizacion.class);
                startActivity(altaForm);

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
