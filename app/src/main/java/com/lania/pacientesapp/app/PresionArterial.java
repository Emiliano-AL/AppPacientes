package com.lania.pacientesapp.app;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.Calendar;

public class PresionArterial extends Activity {

    Button btnShowHora;
    Button btnShowFecha;
    Button btnSavePresion;
    private int myYear, myMonth, myDay, myHour, myMinute;
    static final int ID_DATEPICKER = 0;
    static final int ID_TIMEPICKER = 1;
    EditText txtSistolica;
    EditText txtDiastolica;

    String date;
    String time;
    RadioGroup rgArm;
    String valueChk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presion_arterial);

        btnShowHora = (Button)findViewById(R.id.btnHora);
        btnShowFecha = (Button)findViewById(R.id.btnFecha);
        btnSavePresion = (Button)findViewById(R.id.btnSavePresion);
        rgArm = (RadioGroup)findViewById(R.id.rGarm);
        txtSistolica = (EditText)findViewById(R.id.txtSistolica);
        txtDiastolica = (EditText)findViewById(R.id.txtDiastonica);

        btnShowHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Muestro la ventana de la fecha actual
                final Calendar c = Calendar.getInstance();
                myHour = c.get(Calendar.HOUR_OF_DAY);
                myMinute = c.get(Calendar.MINUTE);
                showDialog(ID_TIMEPICKER);
            }
        });

        btnShowFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //muestro la ventana con la hora actual
                final Calendar c = Calendar.getInstance();
                myYear = c.get(Calendar.YEAR);
                myMonth = c.get(Calendar.MONTH);
                myDay = c.get(Calendar.DAY_OF_MONTH);
                showDialog(ID_DATEPICKER);
            }
        });

        btnSavePresion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Obtengo el valor del radio button, en base al id del radioButton seleccionado
                valueChk = ((RadioButton)findViewById(rgArm.getCheckedRadioButtonId())).getText().toString();
                Log.i("ServicioRest", "Los valores que tenemos, fecha: " + date + " hora: "+ time + " Y el valor del check es.. " + valueChk);
                PresionArterialAsync task = new PresionArterialAsync();
                task.execute("1",txtSistolica.getText().toString(), txtDiastolica.getText().toString(), valueChk, time, date);

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch(id){
            case ID_DATEPICKER:
                //Toast.makeText(PresionArterial.this, "- onCreateDialog -", Toast.LENGTH_LONG).show();
                return new DatePickerDialog(this, myDateSetListener, myYear, myMonth, myDay);
            case ID_TIMEPICKER:
                //Toast.makeText(PresionArterial.this, "- onCreateDialog(ID_TIMEPICKER) -", Toast.LENGTH_LONG).show();
                return new TimePickerDialog(this, myTimeSetListener, myHour, myMinute, false);
            default:
                return null;
        }
    }

    private DatePickerDialog.OnDateSetListener myDateSetListener  = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date = String.valueOf(year) + "-" + String.valueOf(monthOfYear+1) + "-" + String.valueOf(dayOfMonth);
            Log.i("ServicioRest", "El valor de la fecha es... " + date);
            //Toast.makeText(PresionArterial.this, date, Toast.LENGTH_LONG).show();
        }
    };

    private TimePickerDialog.OnTimeSetListener myTimeSetListener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            time = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
            Log.i("ServicioRest", "El valor de la hora es..." + time);
            //Toast.makeText(PresionArterial.this, time,Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.presion_arterial, menu);
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

    private class PresionArterialAsync extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {
            boolean result = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://10.0.2.2/WebServiceRestAndroid/Presion/add");
            post.setHeader("content-type", "application/json");
            try
            {
                //Se construye el jason con los datos de la presion arterial
                JSONObject arterial = new JSONObject();
                arterial.put("PacienteId", params[0]);
                arterial.put("Sistolica", params[1]);
                arterial.put("Diastolica", params[2]);
                arterial.put("Brazo", params[3]);
                arterial.put("Hora", params[4]);
                arterial.put("Fecha", params[5]);

                StringEntity entity = new StringEntity(arterial.toString());
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
                AlertDialog.Builder alert = new AlertDialog.Builder(PresionArterial.this);
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
