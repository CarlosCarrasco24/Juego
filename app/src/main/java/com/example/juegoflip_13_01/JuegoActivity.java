package com.example.juegoflip_13_01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class JuegoActivity extends BaseActivity implements View.OnClickListener {

    private int velocidad,progreso,fase,contadorBotones;
    private String nombre;
    private ArrayList<Button>botones;
    private Button boton11,boton12,boton13,boton21,boton22,boton23;
    private TextView tvNombre,tvProgreso,tvFase;
    private ProgressBar barra;
    private ProgresoBarra miProgreso;
    private boolean sdDisponible = false;
    private boolean sdAccesoEscritura = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        setModoInmersivo();
        cargarViews();
        ponerListener();
        cargarOpciones();
    }
    private void cargarViews(){
        tvNombre=findViewById(R.id.tvNombre);
        tvProgreso=findViewById(R.id.tvProgreso);
        tvFase=findViewById(R.id.tvFase);

        barra=findViewById(R.id.progressBar);
        barra.setScaleY(3f);
        botones=new ArrayList<>();
        boton11=findViewById(R.id.btn11);
        botones.add(boton11);
        boton12=findViewById(R.id.btn12);
        botones.add(boton12);
        boton13=findViewById(R.id.btn13);
        botones.add(boton13);
        boton21=findViewById(R.id.btn21);
        botones.add(boton21);
        boton22=findViewById(R.id.btn22);
        botones.add(boton22);
        boton23=findViewById(R.id.btn23);
        botones.add(boton23);

    }

    //----------------------
    public void ponerListener(){
        for(int i=0;i<botones.size();i++){
            botones.get(i).setEnabled(false);
            botones.get(i).setOnClickListener(this);
        }
    }
    //***************************************************
    public void cargarOpciones(){
        velocidad=getIntent().getIntExtra(MainActivity.VELOCIDAD,40);
        nombre=getIntent().getStringExtra(MainActivity.NOMBRE);
        tvNombre.setText(nombre);
        fase=getIntent().getIntExtra(MainActivity.FASE,0);
        contadorBotones=1;//ya veremos
        progreso=0;
        tvFase.setText(String.format(getResources().getString(R.string.fase),fase));
        tvProgreso.setText(String.format(getResources().getString(R.string.progreso),progreso));
    }
    public void volver(View v){
        finish();
    }


    public void iniciar(View v){
        numerarBotones(desordenarBotones());
        miProgreso=new ProgresoBarra();
        miProgreso.execute(velocidad);
        ((Button)v).setEnabled(false);

    }
    public void numerarBotones(ArrayList<Integer>al){
        for(int i=0;i<botones.size();i++){
            botones.get(i).setEnabled(true);
            botones.get(i).setText(""+al.get(i));
        }
    }

    public ArrayList<Integer>desordenarBotones(){
        ArrayList<Integer>al=new ArrayList<>();
        for(int i=1;i<=6;i++){
            al.add(i);
        }
        Collections.shuffle(al);
        return al;
    }
    @Override
    public void onClick(View v) {
        Button botonPulsado=(Button)v;
        if(Integer.valueOf(botonPulsado.getText().toString())==contadorBotones){
            contadorBotones++;
            botonPulsado.setEnabled(false);
            if(contadorBotones>6){
                miProgreso.cancel(true);
            }
        }
    }
    public void salir(){
        Intent i =new Intent();
        i.putExtra(MainActivity.FASE,fase);
        i.putExtra(MainActivity.NOMBRE,nombre);
        i.putExtra(MainActivity.VELOCIDAD,velocidad);
        setResult(RESULT_OK,i);
        finish();
    }
    //---------------------------------------------
    //implementaremos nuestra clase con asyntask
    private class ProgresoBarra extends AsyncTask<Integer,Integer,Integer>{

        @Override
        protected Integer doInBackground(Integer... integers) {
            while(progreso<100){
                SystemClock.sleep(integers[0]);
                miProgreso.publishProgress(progreso);
                progreso++;
                if(isCancelled())break;
            }
            return progreso;
        }
        @Override
        protected void onProgressUpdate(Integer...valores){
            super.onProgressUpdate(valores);
            barra.setProgress(valores[0]);
            tvProgreso.setText(String.format(getResources().getString(R.string.progreso),valores[0]));
        }
        //Si pierdo se ejecuta el postExecute es decir la barra llego al 100 o el hilo termina y no ha sido cancelado
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            salir();
        }
        //si he cancelado es porque antes de llegar a 100 he logrado dar a todos los botones
        //en ese caso reseteo las variable incremento la fase y vueelvo a empezar
        @Override
        protected void onCancelled(Integer integer){
            super.onCancelled(integer);
            if(contadorBotones==7){
                miProgreso=new ProgresoBarra();
                numerarBotones(desordenarBotones());
                tvFase.setText(String.format(getResources().getString(R.string.fase),++fase));
                progreso=0;
                tvProgreso.setText(String.format(getResources().getString(R.string.progreso),progreso));
                contadorBotones=1;
                barra.setProgress(progreso);
                velocidad=(velocidad-5)<0?1:velocidad-5;
                miProgreso.execute(velocidad);
            }
        }
    }
}
