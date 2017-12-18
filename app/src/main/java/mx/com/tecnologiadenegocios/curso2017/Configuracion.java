package mx.com.tecnologiadenegocios.curso2017;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import mx.com.tecnologiadenegocios.curso2017.model.Estado;
import mx.com.tecnologiadenegocios.curso2017.R;
import mx.com.tecnologiadenegocios.curso2017.helper.BDHelper;
import mx.com.tecnologiadenegocios.curso2017.model.Carretera;
import mx.com.tecnologiadenegocios.curso2017.model.CategoriaEmergencia;
import mx.com.tecnologiadenegocios.curso2017.model.CausaEmergencia;
import mx.com.tecnologiadenegocios.curso2017.model.Danos;
import mx.com.tecnologiadenegocios.curso2017.model.Subtramo;
import mx.com.tecnologiadenegocios.curso2017.model.TipoEmergencia;
import mx.com.tecnologiadenegocios.curso2017.model.Tramo;

/**
 * Created by Ing.Sergio Martínez on 22/06/2015.
 */
public class Configuracion extends ActionBarActivity {
    private boolean catalogosSincronizados  = false;
    private EditText etUsuario, etContrasenia, etServicios, etReportes;


    private HttpResponse response;
    private HttpClient httpCLient;
    private HttpPost httpPost;

    public String jsonResult;
    private String urlServicios = "";
    private String urlWEB = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Toast.makeText(getApplicationContext(), "Inicio", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_configuracion);

        etUsuario = (EditText)findViewById(R.id.etUsuario);
        etContrasenia = (EditText)findViewById(R.id.etContrasenia);
        etServicios = (EditText)findViewById(R.id.etServicios);
        etReportes = (EditText)findViewById(R.id.etReportes);


        loadPreferences();
        loadBotones();





    }
    public void loadBotones(){

        //Sincropnizar incidenciasmx
        findViewById(R.id.btnSincronizarIncidencias).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Configuracion.this, Sincronizador.class);
                startActivity(altaForm);
            }
        });



        //Sincropnizador
        findViewById(R.id.btnSincronizar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catalogosSincronizados)
                    Toast.makeText(getApplicationContext(), "Catálogos Sincronizados", Toast.LENGTH_SHORT).show();
                else {
                    ProcesoSincronizacion proceso = new ProcesoSincronizacion();
                    proceso.execute();
                }
            }
        });

        //boton inicio
        findViewById(R.id.btnInicio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Configuracion.this, Inicio.class);
                startActivity(altaForm);
                //Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();

            }
        });

        //Boton actualizacion
        findViewById(R.id.btnActualizacion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Configuracion.this, Actualizacion.class);
                startActivity(altaForm);


            }
        });

        //Boton mapa
        findViewById(R.id.btnMapa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Configuracion.this, Mapa.class);
                startActivity(altaForm);

            }
        });

        //Boton Reportes
        findViewById(R.id.btnReportes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Configuracion.this, Reportes.class);
                startActivity(altaForm);

            }
        });

        //Boton Alta
        findViewById(R.id.btnAlta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Configuracion.this, Alta.class);
                startActivity(altaForm);

            }
        });
    }

    private class ProcesoSincronizacion extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog dialog;
        private HttpResponse response;
        private HttpClient httpCLient;       ;
        private HttpPost httpPost;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Configuracion.this);
            dialog.setTitle("Espere...");
            dialog.setMessage("Sincronizando catálogos");
            dialog.setCancelable(false);
            dialog.show();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(catalogosSincronizados)
                savePreferencesCatalogosSincronizados(true);
            else{
                //rutina para borrar la bd
            }
            if(dialog.isShowing())
                dialog.dismiss();
        }




        @Override
        protected Void doInBackground(Void... params) {
            //Toast.makeText(getApplicationContext(), "Ejecutara", Toast.LENGTH_SHORT).show();
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }






            httpCLient  = new DefaultHttpClient();


            try {

                //Sincronizacion de Entidades Federativas
                httpPost = new HttpPost(urlServicios + "/IncidenciasPruebas/web/Entidadfederativa_view.action");
                response = httpCLient.execute(httpPost);
                BDHelper dbHelper = new BDHelper(Configuracion.this);

                try {
                    dbHelper.dropDatabase();
                }catch(Exception e){
                }

                try {
                    dbHelper.createTables();
                }catch(Exception e){
                }


                InputStream r = getResources().openRawResource(R.raw.carretera);
                JSONObject object = new JSONObject(inputStreamToString(r).toString());
                for (int i = 0; i < object.getJSONArray("data").length(); i++) {  // **line 2**
                    JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(i);
                    dbHelper.insertarCarretera(new Carretera(childJSONObject.getInt("id"), childJSONObject.getString("nombre")));
                }
                dbHelper.closeConnection();

                dbHelper = new BDHelper(Configuracion.this);
                object = new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
                for (int i = 0; i < object.getJSONArray("data").length(); i++) {  // **line 2**
                    JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(i);
                    dbHelper.insertarEstado(new Estado(childJSONObject.getInt("entidadid"), childJSONObject.getString("nombre")));
                }
                dbHelper.closeConnection();


/*
                httpPost = new HttpPost(urlServicios + "/IncidenciasPruebas/web/Carretera_movil_view_entidad.action");
                response = httpCLient.execute(httpPost);
                object = new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
                dbHelper = new BDHelper(Configuracion.this);
                for (int i = 0; i < object.getJSONArray("data").length(); i++) {  // **line 2**
                    JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(i);
                    dbHelper.insertarCarretera(new Carretera(childJSONObject.getInt("id"), childJSONObject.getString("nombre")));
                }
                dbHelper.closeConnection();

                httpPost = new HttpPost(urlServicios + "/IncidenciasPruebas/web/Tramo_view.action");
                response = httpCLient.execute(httpPost);
                object = new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
                dbHelper = new BDHelper(Configuracion.this);
                for (int i = 0; i < object.getJSONArray("data").length(); i++) {  // **line 2**
                    JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(i);
                    JSONObject childJSONObjectC =  childJSONObject.getJSONObject("carretera");

                    dbHelper.insertarTramo(new Tramo(childJSONObject.getInt("tramoid"), childJSONObjectC.getInt("carreteraid"), childJSONObject.getString("origen") + "-" + childJSONObject.getString("destino")));

                }
                dbHelper.closeConnection();

                httpPost = new HttpPost(urlServicios + "/IncidenciasPruebas/web/Subtramo_movil_view_entidad_tramo.action");
                response = httpCLient.execute(httpPost);
                object = new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
                dbHelper = new BDHelper(Configuracion.this);
                for (int i = 0; i < object.getJSONArray("data").length(); i++) {  // **line 2**
                    JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(i);

                    dbHelper.insertarSubtramo(new Subtramo(childJSONObject.getInt("id"), childJSONObject.getInt("idTramo"),childJSONObject.getInt("idEstado"), childJSONObject.getString("kminicial"), childJSONObject.getString("kmfinal"), childJSONObject.getString("nombre")));
                }*/

                dbHelper = new BDHelper(Configuracion.this);
                r = getResources().openRawResource(R.raw.tramo);
                object = new JSONObject(inputStreamToString(r).toString());
                for (int i = 0; i < object.getJSONArray("data").length(); i++) {  // **line 2**
                    JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(i);
                    JSONObject childJSONObjectC =  childJSONObject.getJSONObject("carretera");
                    dbHelper.insertarTramo(new Tramo(childJSONObject.getInt("tramoid"), childJSONObjectC.getInt("carreteraid"), childJSONObject.getString("origen") + "-" + childJSONObject.getString("destino")));

                }
                dbHelper.closeConnection();

                dbHelper = new BDHelper(Configuracion.this);
                r = getResources().openRawResource(R.raw.subtramo);
                object = new JSONObject(inputStreamToString(r).toString());
                for (int i = 0; i < object.getJSONArray("data").length(); i++) {  // **line 2**
                    JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(i);
                    dbHelper.insertarSubtramo(new Subtramo(childJSONObject.getInt("id"), childJSONObject.getInt("idTramo"),childJSONObject.getInt("idEstado"), childJSONObject.getString("kminicial"), childJSONObject.getString("kmfinal"), childJSONObject.getString("nombre")));
                }
                dbHelper.closeConnection();
                //Daños causados
                httpPost = new HttpPost(urlServicios + "/IncidenciasPruebas/web/Dano_view.action");
                response = httpCLient.execute(httpPost);
                object = new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
                dbHelper = new BDHelper(Configuracion.this);
                for (int i = 0; i < object.getJSONArray("data").length(); i++) {  // **line 2**
                    JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(i);

                    dbHelper.insertarDanoCausado(new Danos(childJSONObject.getInt("danoid"), childJSONObject.getString("nombre")));
                }
                dbHelper.closeConnection();

                //Causa
                httpPost = new HttpPost(urlServicios + "/IncidenciasPruebas/web/Causaemergencia_view.action");
                response = httpCLient.execute(httpPost);
                object = new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
                dbHelper = new BDHelper(Configuracion.this);
                for (int i = 0; i < object.getJSONArray("data").length(); i++) {  // **line 2**
                    JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(i);

                    dbHelper.insertarCausaEmergencia(new CausaEmergencia(childJSONObject.getInt("causaid"), childJSONObject.getString("nombre")));
                }
                dbHelper.closeConnection();

                //Tipo Emergencia
                httpPost = new HttpPost(urlServicios + "/IncidenciasPruebas/web/Tipoemergencia_view.action");
                response = httpCLient.execute(httpPost);
                object = new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
                dbHelper = new BDHelper(Configuracion.this);
                for (int i = 0; i < object.getJSONArray("data").length(); i++) {  // **line 2**
                    JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(i);
                    JSONObject childJSONObjectCE = childJSONObject.getJSONObject("causaemergencia");

                    dbHelper.insertarTipoEmergencia(new TipoEmergencia(childJSONObject.getInt("tipoid"), childJSONObjectCE.getInt("causaid"), childJSONObject.getString("nombre")));
                }
                dbHelper.closeConnection();


                //Categoria
                httpPost = new HttpPost(urlServicios + "/IncidenciasPruebas/web/Categoria_view.action");
                response = httpCLient.execute(httpPost);
                object = new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
                dbHelper = new BDHelper(Configuracion.this);
                for (int i = 0; i < object.getJSONArray("data").length(); i++) {  // **line 2**
                    JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(i);
                    JSONObject childJSONObjectCE = childJSONObject.getJSONObject("tipoemergencia");

                    dbHelper.insertarCategoriaEmergencia(new CategoriaEmergencia(childJSONObject.getInt("categoriaid"), childJSONObjectCE.getInt("tipoid"), childJSONObject.getString("nombre")));
                }
                dbHelper.closeConnection();

                catalogosSincronizados  = true;
                //Toast.makeText(getApplicationContext(), "Catálogos sincronizados", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                catalogosSincronizados  = false;
                //Toast.makeText(getApplicationContext(), "Ocurrió un fallo en la sincronización,  intentelo de nuevo.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return null;
        }
    }

    private StringBuilder inputStreamToString(InputStream is){
        String rLine = "";
        StringBuilder answer =  new StringBuilder();
        BufferedReader rd =  new BufferedReader(new InputStreamReader(is));

        try{
            while((rLine = rd.readLine()) != null)
            {
                answer.append(rLine);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return answer;
    }

    public void loadPreferences(){
        SharedPreferences preferencias = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        catalogosSincronizados = preferencias.contains("catalogosSincronizados");
        urlServicios = (preferencias.getString("urlServicios", ""));
        urlWEB = (preferencias.getString("urlWEB", ""));

        etServicios.setText(urlServicios);
        etReportes.setText(urlWEB);
        etUsuario.setText(preferencias.getString("usuarioDefault", ""));
        etContrasenia.setText(preferencias.getString("contrasenaDefault", ""));
    }

    public void savePreferencesCatalogosSincronizados(boolean valor){
        SharedPreferences preferencias = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putBoolean("catalogosSincronizados ", valor);
        editor.commit();
        //Toast.makeText(getApplicationContext(), "Salva preferencias", Toast.LENGTH_SHORT).show();
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
