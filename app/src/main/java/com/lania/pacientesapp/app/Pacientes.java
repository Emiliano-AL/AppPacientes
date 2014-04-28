package com.lania.pacientesapp.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class Pacientes extends Activity {

    private Spinner spnSexo;
    private Button btnGuardar;
    private Spinner spnGrupoSangre;
    private static final String LOGTAG = "LogsAndroid";
    private EditText txtNombre;
    private EditText txtEdad;
    private EditText txtPeso;
    private EditText txtAltura;

    private TextView lblPaciente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacientes);

        btnGuardar = (Button) findViewById(R.id.btnGuardarPaciente);
        spnSexo = (Spinner) findViewById(R.id.spnSexo);
        spnGrupoSangre = (Spinner) findViewById(R.id.spnTipoSangre);
        txtNombre = (EditText) findViewById(R.id.editNombre);
        txtEdad = (EditText) findViewById(R.id.editEdad);
        txtPeso = (EditText) findViewById(R.id.editPeso);
        txtAltura = (EditText) findViewById(R.id.editAltura);
        lblPaciente = (TextView) findViewById(R.id.txtInfoPaciente);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Aqui obtengo el valor de un spinner en especifico
                Log.i(LOGTAG, "Valor seleccionado " + String.valueOf(spnGrupoSangre.getSelectedItem()));
                //Aqui llamo a la clase asincrona que se encarga de conectarse al servicio web
                InsertarPacienteAsync task = new InsertarPacienteAsync();
                task.execute(txtNombre.getText().toString(), txtEdad.getText().toString(),
                        String.valueOf(spnSexo.getSelectedItem()), String.valueOf(spnGrupoSangre.getSelectedItem()),
                        txtPeso.getText().toString(), txtAltura.getText().toString());
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pacientes, menu);
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


    //clase asincrona para hacer el insert de un paciente
    private class InsertarPacienteAsync extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {
            boolean result = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://10.0.2.2/WebServiceRestAndroid/Pacientes/add");
            post.setHeader("content-type", "application/json");
            try
            {
                //Se construye el jason con los datos del paciente
                JSONObject paciente = new JSONObject();
                paciente.put("Nombre", params[0]);
                paciente.put("Edad", params[1]);
                paciente.put("Sexo", params[2]);
                paciente.put("TipoSangre", params[3]);
                paciente.put("Peso", params[4]);
                paciente.put("Altura", params[5]);

                StringEntity entity = new StringEntity(paciente.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());

                if(!respStr.equals("true"))
                    result = false;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error! algo salio mal :(", ex);
                result = false;
            }
            return result;
        }

        protected void onPostExecute(Boolean result) {
            if (result){
                AlertDialog.Builder alert = new AlertDialog.Builder(Pacientes.this);
                alert.setTitle(R.string.tituloApp);
                alert.setMessage("Paciente Agregado exitosamente :)");
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
