package com.example.juegoflip_13_01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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
            botones.get(i).setOnClickListener(this);
        }
    }
    //***************************************************
    public void cargarOpciones(){
        velocidad=getIntent().getIntExtra(MainActivity.VELOCIDAD,40);
        nombre=getIntent().getStringExtra(MainActivity.NOMBRE);
        tvNombre.setText(nombre);
        fase=1;
        contadorBotones=1;//ya veremos
        progreso=0;
        tvFase.setText(String.format(getResources().getString(R.string.fase),fase));
        tvProgreso.setText(String.format(getResources().getString(R.string.progreso),progreso));
    }

    public void iniciar(View w){
        numerarBotones(desordenarBotones());
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

    }
}
