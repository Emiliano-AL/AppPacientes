package com.lania.pacientesapp.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class Glucosa extends Activity {

    private Button btnSave;
    private Spinner spsnTiempo;
    private EditText txtGlucosa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glucosa);

        btnSave = (Button)findViewById(R.id.btnGuardarGlucosa);
        spsnTiempo = (Spinner)findViewById(R.id.spnTiempo);
        txtGlucosa = (EditText)findViewById(R.id.txtGlucosa);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlucosaAsync task = new GlucosaAsync();
                task.execute(txtGlucosa.getText().toString(), String.valueOf(spsnTiempo.getSelectedItem()));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.glucosa, menu);
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


    private class GlucosaAsync extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {
            boolean result = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://10.0.2.2/WebServiceRestAndroid/Pacientes/add");
            post.setHeader("content-type", "application/json");
            try
            {
                //Se construye el jason con los datos del paciente
                JSONObject glucosa = new JSONObject();
                glucosa.put("Glucosa", params[0]);
                glucosa.put("Tiempo", params[1]);

                StringEntity entity = new StringEntity(glucosa.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());

                if(!respStr.equals("true"))
                    result = false;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error! algo salio mal :(", ex);
                result = false;
            }
            return result;
        }

        protected void onPostExecute(Boolean result) {
            if (result){
                AlertDialog.Builder alert = new AlertDialog.Builder(Glucosa.this);
                alert.setTitle(R.string.tituloApp);
                alert.setMessage("Datos guardados exitosamente :)");
                alert.setCancelable(false);
                alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertD = alert.create();
                alertD.show();
            }
        }

    }

}
