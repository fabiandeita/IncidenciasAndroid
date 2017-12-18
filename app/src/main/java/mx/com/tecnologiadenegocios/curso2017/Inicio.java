package mx.com.tecnologiadenegocios.curso2017;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import mx.com.tecnologiadenegocios.curso2017.R;
import mx.com.tecnologiadenegocios.curso2017.helper.BDHelper;


public class Inicio extends ActionBarActivity {
    //private Intent alta = new Intent(Inicio.this, Alta.class);
    private EditText  txtusuario, txtContrasena;
    private boolean bdCargada = false;
    private String usuarioDefautl, contrasenaDefault;
    private boolean logeado = false, serverOut = true;;
    private String urlServicios = "";
    private String urlWEB = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setSesFlu();
        setContentView(R.layout.activity_inicio);
        txtusuario = (EditText) findViewById(R.id.txtUsuario);
        txtContrasena = (EditText) findViewById(R.id.txtPassword);
        loadPreferences();
        BDHelper dbHelper = new BDHelper(this);
        if(!bdCargada){
            //Se crea la BD
            dbHelper.createTables();
            savePreferencesBD(true);
            savePreferenciasIniciales("usuario","password");
        }
        loadPreferences();
        CopyReadAssets();
        loadBotones();
    }

    @Override
    public void onBackPressed() {
    }
    private boolean checkConnectivity()
    {
        boolean enabled = true;

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if ((info == null || !info.isConnected() || !info.isAvailable()))
        {
            enabled = false;
        }
        return enabled;
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

    public void loadBotones(){
        //boton acceder
        findViewById(R.id.btnacceder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usuario = txtusuario.getText().toString();
                String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString();

                if (usuario.isEmpty() || password.isEmpty())
                    Toast.makeText(getApplicationContext(), "Especifique el usuario o contraseña" , Toast.LENGTH_SHORT).show();
                else if(usuarioDefautl.equals(txtusuario.getText().toString()) && contrasenaDefault.equals(txtContrasena.getText().toString())){
                        Intent altaForm = new Intent(Inicio.this, Alta.class);
                        startActivity(altaForm);
                    }else if(checkConnectivity())
                        new LogeoTask().execute();
                    else
                        Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecta" , Toast.LENGTH_SHORT).show();

            }
        });

        //boton registro
        findViewById(R.id.btnregistro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Inicio.this, Registro.class);
                startActivity(altaForm);

            }
        });

        /*boton inicio
        findViewById(R.id.btnInicio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Inicio.this, Inicio.class);
                startActivity(altaForm);

            }
        });*/
    }

    public void CopyReadAssets() {
        AssetManager assetManager = getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(getFilesDir(), "manual.pdf");
        try {
            in = assetManager.open("manual.pdf");
            out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    public void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }




        private class LogeoTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                serverOut = false;
                HttpClient httpCLient = new DefaultHttpClient();
                String url = urlServicios + "/Incidencias/auth/admsegloginmovil.action?data="+ txtusuario.getText().toString();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse response = httpCLient.execute(httpGet);
                JSONObject object = new JSONObject(inputStreamToString(response.getEntity().getContent()).toString());
                JSONObject childJSONObject = object.getJSONArray("data").getJSONObject(0);
                if(childJSONObject.getString("cpassword").equals(txtContrasena.getText().toString()))
                    logeado = true;
                return null;
            }catch (Exception e){
                serverOut = true;

                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            logeado = false;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(logeado){
                savePreferenciasLogeo(txtusuario.getText().toString(), txtContrasena.getText().toString());
                Intent altaForm = new Intent(Inicio.this, Alta.class);
                startActivity(altaForm);
            }else if(!serverOut)
                Toast.makeText(getApplicationContext(), "Usuario o password incorrecto.", Toast.LENGTH_SHORT).show();
            else if(serverOut)
                Toast.makeText(getApplicationContext(), "Error al conectarse al servidor, intente nuevamente.", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadPreferences(){
        SharedPreferences preferencias = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        usuarioDefautl = (preferencias.getString("usuarioDefault", ""));
        contrasenaDefault = (preferencias.getString("contrasenaDefault", ""));
        txtusuario.setText(preferencias.getString("usuario", ""));
        bdCargada = preferencias.getBoolean("bdCargada", false);
        urlServicios = (preferencias.getString("urlServicios", ""));
        urlWEB = (preferencias.getString("urlWEB", ""));
        //Toast.makeText(getApplicationContext(), "Carga preferencias", Toast.LENGTH_SHORT).show();
    }

    public void savePreferencesUsuario(String usuario){
        SharedPreferences preferencias = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("usuario", usuario);
        editor.commit();
        //Toast.makeText(getApplicationContext(), "Salva preferencias", Toast.LENGTH_SHORT).show();
    }

    public void savePreferenciasIniciales(String usuario, String contrasena){
        SharedPreferences preferencias = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("usuarioDefault", usuario);
        editor.putString("contrasenaDefault", contrasena);
        editor.putString("urlServicios", "http://187.188.120.133");
        editor.putString("urlWEB", "http://187.188.120.156");

        editor.commit();
        //Toast.makeText(getApplicationContext(), "Salva preferencias", Toast.LENGTH_SHORT).show();
    }

    public void savePreferenciasLogeo(String usuario, String contrasena){
        SharedPreferences preferencias = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("usuario", usuario);
        editor.putString("contrasenaDefault", contrasena);
        editor.putString("usuarioDefault", usuario);
        editor.putBoolean("sesFlu", true);

        editor.commit();
        //Toast.makeText(getApplicationContext(), "Salva preferencias", Toast.LENGTH_SHORT).show();
    }

    public void setSesFlu(){
        SharedPreferences preferencias = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putBoolean("sesFlu", false);
        editor.commit();
        //Toast.makeText(getApplicationContext(), "Salva preferencias", Toast.LENGTH_SHORT).show();
    }
    public void savePreferencesBD(Boolean valor){
        SharedPreferences preferencias = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putBoolean("bdCargada", valor);
        editor.commit();
        //Toast.makeText(getApplicationContext(), "Salva preferencias", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.help) {
           /* Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.usoandroid);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            File file = new File("android.resource://" + getPackageName() + "/" + R.raw.usoandroid);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

            */
           /* File file = new File("android.resource://" + getPackageName() + "/" + getResources().getResourceEntryName(R.raw.usoandroid) + ".pdf");

            //Uri file = Uri.parse("android.resource://" + getPackageName() + "/" + getResources().getResourceEntryName(R.raw.usoandroid) + ".pdf");
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file),"application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            System.out.println((String.valueOf(file)));*/

            /*Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {

            }*/
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(
                    Uri.parse("file://" + getFilesDir() + "/manual.pdf"),
                    "application/pdf");

            startActivity(intent);


        }

        return super.onOptionsItemSelected(item);
    }
}
