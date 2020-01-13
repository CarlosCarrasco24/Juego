package com.example.juegoflip_13_01;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import static android.icu.lang.UCharacter.toUpperCase;

public class MainActivity extends BaseActivity implements DialogoOpciones.DialogoOpcionesListener {
    public static final String VELOCIDAD="v",NOMBRE="n";
    public static final int REQ_PLAY=100;
    String nombre;
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
        Intent i=new Intent(this,JuegoActivity.class);
        nombre=toUpperCase(nombre.charAt(0)+nombre.substring(1));
        i.putExtra(NOMBRE,nombre);
        i.putExtra(VELOCIDAD,velocidad);
        Log.d("DATOS----------:::::::::---->","nombre="+nombre+", velocidad="+velocidad);
        startActivityForResult(i,REQ_PLAY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
