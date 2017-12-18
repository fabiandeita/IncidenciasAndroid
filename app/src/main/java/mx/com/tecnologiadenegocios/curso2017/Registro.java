package mx.com.tecnologiadenegocios.curso2017;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import mx.com.tecnologiadenegocios.curso2017.R;

/**
 * Created by Ing.Sergio Martínez on 22/06/2015.
 */
public class Registro extends ActionBarActivity {
    private WebView webView;

    private String urlServicios = "";
    private String urlWEB = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        loadPreferences();
        webView = (WebView) findViewById(R.id.webViewRegistro);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setInitialScale(100);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        webView.loadUrl(urlWEB + "/Curso/RegistroUsuario.iface");
        //Toast.makeText(getApplicationContext(), urlWEB + "/Emergencias/RegistroUsuario.iface", Toast.LENGTH_SHORT).show();
        loadBotones();

    }

    public void loadPreferences(){
        SharedPreferences preferencias = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        urlServicios = (preferencias.getString("urlServicios", ""));
        urlWEB = (preferencias.getString("urlWEB", ""));
        //Toast.makeText(getApplicationContext(), "Carga preferencias", Toast.LENGTH_SHORT).show();
    }
    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public void loadBotones(){

        //boton inicio
        findViewById(R.id.btnInicio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Registro.this, Inicio.class);
                startActivity(altaForm);

            }
        });


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
            Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.usoandroid);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}