package com.lania.pacientesapp.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Historial extends Activity {

    private ListView lstHistorial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        lstHistorial = (ListView) findViewById(R.id.lstHistorial);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.historial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {
        private String[] historial;
        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet del = new HttpGet("http://10.0.2.2/WebServiceRestAndroid/Presion/Get");
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);
                historial = new String[respJSON.length()];
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    String fecha = obj.getString("Fecha");
                    String hora = obj.getString("Hora");
                    String glucosa = obj.getString("Glucosa");

                    historial[i] = "" + fecha + " - " + hora + " - " + glucosa;
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error! :(", ex);
                resul = false;
            }
            return resul;
        }

        protected void onPostExecute(Boolean result) {
            if (result)
            {
                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(Historial.this, android.R.layout.simple_list_item_1, historial);
                lstHistorial.setAdapter(adaptador);
            }
        }
    }
}
