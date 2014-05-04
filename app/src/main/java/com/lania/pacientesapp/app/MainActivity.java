package com.lania.pacientesapp.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button btnPacientes;
    private Button btnGlucosa;
    private Button btnHistorial;
    private Button btnAcerca;
    private Button btnPresion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPacientes = (Button) findViewById(R.id.btnPacientes);
        btnGlucosa = (Button) findViewById(R.id.btnGlucosa);
        btnHistorial = (Button) findViewById(R.id.btnHistorial);
        btnAcerca = (Button) findViewById(R.id.btnAcercaDe);
        btnPresion = (Button) findViewById(R.id.btnPresion);

        btnPacientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarPacientesView();
            }
        });

        btnGlucosa.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mostrarGlucosaView();
            }
        });

        btnHistorial.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mostrarHistorialView();
            }
        });

        btnAcerca.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mostrarbtnAcercaView();
            }
        });

        btnPresion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarPresionView();
            }
        });

    }

    public void mostrarPacientesView(){
        Intent i = new Intent(this, Pacientes.class);
        startActivity(i);
    }

    public void mostrarGlucosaView(){
        Intent i = new Intent(this, Glucosa.class);
        startActivity(i);
    }

    public void mostrarHistorialView(){

    }

    public void mostrarbtnAcercaView(){

    }

    public void mostrarPresionView(){
        Intent i = new Intent(this, PresionArterial.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

}
