package com.example.juegoflip_13_01;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import static java.lang.Character.toUpperCase;




public class MainActivity extends BaseActivity implements DialogoOpciones.DialogoOpcionesListener {
    public static final String VELOCIDAD="v",NOMBRE="n",FASE="d";
    public static final int REQ_PLAY=100;
    String nombre,nombre1;
    int fase,velocidad,fase1,velocidad1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }
    public void opciones(View w){
        mostrarDialogo();
    }

    public void mostrarDialogo(){
        DialogFragment dialogo= new DialogoOpciones(); //hereda en DialogoOpciones de DialogFragment, es un DialogFrangment personalizado
        dialogo.setCancelable(false); //No se puede cerrar la ventana, tiene que elegir un boton ya que la ventana no se cerrara.
        dialogo.show(getSupportFragmentManager(),"DialogoOpciones");
    }


    @Override
    public void onDialogoOpcionesListener(String nombre, int velocidad) {
        final Intent i=new Intent(this,JuegoActivity.class);
        nombre=toUpperCase(nombre.charAt(0))+nombre.substring(1);
        SharedPreferences prefs = getSharedPreferences("Juego",Context.MODE_PRIVATE);
        nombre1=prefs.getString("nombre","");
        velocidad1=prefs.getInt("velocidad",0);
        fase1=prefs.getInt("fase",0);
        Log.d("DATOSPREFERENCES>","nombre="+nombre1+", velocidad="+velocidad1+", fase="+fase1);
        if(nombre.equalsIgnoreCase(nombre1) && !nombre1.equalsIgnoreCase("Guest")){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Se encontro una partida guardada");
            builder.setPositiveButton("Seguir", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    i.putExtra(NOMBRE,nombre1);
                    i.putExtra(VELOCIDAD,velocidad1);
                    i.putExtra(FASE,fase1);
                    startActivityForResult(i,REQ_PLAY);
                }
            });
            final String finalNombre = nombre;
            final int velocidadFinal= velocidad;
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    i.putExtra(NOMBRE, finalNombre);
                    i.putExtra(VELOCIDAD,velocidadFinal);
                    startActivityForResult(i,REQ_PLAY);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        i.putExtra(NOMBRE,nombre);
        i.putExtra(VELOCIDAD,velocidad);
        Log.d("DATOS----------:::::::::---->","nombre="+nombre+", velocidad="+velocidad);
        startActivityForResult(i,REQ_PLAY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_PLAY && resultCode==RESULT_OK){
            AlertDialog.Builder alerta=new AlertDialog.Builder(this);
            nombre=data.getStringExtra(NOMBRE);
            fase=data.getIntExtra(FASE,0);
            velocidad=data.getIntExtra(VELOCIDAD,0);
            Log.d("DATOS GUARDAR---->","nombre="+nombre+", velocidad="+velocidad+"fase="+fase);
            alerta.setMessage(String.format(getResources().getString(R.string.finJuego),nombre,fase));
            alerta.setPositiveButton("Ok",null);
            alerta.show();
            SharedPreferences prefs = getSharedPreferences("Juego", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("nombre", nombre);
            editor.putInt("fase",fase);
            editor.putInt("velocidad",velocidad);
            editor.commit();
        }
        else {
            Toast.makeText(this,"El usuario cancelo",Toast.LENGTH_LONG).show();
        }
    }
}
