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
public class Reportes  extends ActionBarActivity {
    private WebView webView;
    private String urlServicios = "";
    private String urlWEB = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);
        loadPreferences();
        //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
        webView = (WebView) findViewById(R.id.webViewReportes);
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
        webView.loadUrl(urlWEB + "/Emergencias/ReportesEstadisticos.iface");
        loadBotones();
    }

    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public void loadPreferences(){
        SharedPreferences preferencias = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        urlServicios = (preferencias.getString("urlServicios", ""));
        urlWEB = (preferencias.getString("urlWEB", ""));
        //Toast.makeText(getApplicationContext(), "Carga preferencias", Toast.LENGTH_SHORT).show();
    }
    public void loadBotones(){

        //boton inicio
        findViewById(R.id.btnInicio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Reportes.this, Inicio.class);
                startActivity(altaForm);
                //Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();

            }
        });

        //boton alta
        findViewById(R.id.btnAlta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Reportes.this, Alta.class);
                startActivity(altaForm);
                //Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();

            }
        });

        //Boton actualizacion
        findViewById(R.id.btnActualizacion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Reportes.this, Actualizacion.class);
                startActivity(altaForm);


            }
        });



        //Boton Mapa
        findViewById(R.id.btnMapa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Reportes.this, Mapa.class);
                startActivity(altaForm);

            }
        });



        //Boton Confguracion
        findViewById(R.id.btnConfiguracion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent altaForm = new Intent(Reportes.this, Configuracion.class);
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