package com.example.juegoflip_13_01;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static java.lang.Character.toUpperCase;




public class MainActivity extends BaseActivity implements DialogoOpciones.DialogoOpcionesListener {
    public static final String VELOCIDAD="v",NOMBRE="n",FASE="d";
    public static final int REQ_PLAY=100;
    String nombre;
    int fase,velocidad;
    private boolean sdDisponible = false;
    private boolean sdAccesoEscritura = false;
    String mensaje = "";
    String linea;

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
    public void onDialogoOpcionesListener(String nombre, int velocidad,int fase) {
        Intent i=new Intent(this,JuegoActivity.class);
        nombre=toUpperCase(nombre.charAt(0))+nombre.substring(1);
        Log.d("DATOS----------:::::::::---->","antes de buscar"+nombre);
        buscar();
        cargar();
        Log.d("DATOS----------:::::::::---->",""+mensaje);
        if(nombre.equalsIgnoreCase("Guest")){
            Log.d("DATOS----------:::::::::---->","en guest");
            i.putExtra(NOMBRE,nombre);
            i.putExtra(VELOCIDAD,velocidad);
            i.putExtra(FASE,fase);
            Log.d("DATOS----------:::::::::---->","nombre="+nombre+", velocidad="+velocidad+"fase="+fase);
            startActivityForResult(i,REQ_PLAY);
        }else{
            Log.d("DATOS----------:::::::::---->","EN UN NOMBRE");
        i.putExtra(NOMBRE,nombre);
        i.putExtra(VELOCIDAD,velocidad);
        i.putExtra(FASE,fase);
        Log.d("DATOS----------:::::::::---->","nombre="+nombre+", velocidad="+velocidad+"fase="+fase);
        startActivityForResult(i,REQ_PLAY);
        }
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
            guardar();
            alerta.setMessage(String.format(getResources().getString(R.string.finJuego),nombre,fase));
            alerta.setPositiveButton("Ok",null);
            alerta.show();
        }
        else {
            Toast.makeText(this,"El usuario cancelo",Toast.LENGTH_LONG).show();
        }
    }
    public void buscar(){
        String estado = Environment.getExternalStorageState();
        if (estado.equals(Environment.MEDIA_MOUNTED))
        {
            sdDisponible = true;
            sdAccesoEscritura = true;
        }
        else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            sdDisponible = true;
            sdAccesoEscritura = false;
        }
        else
        {
            sdDisponible = false;
            sdAccesoEscritura = false;
        }
    }
    public void cargar() {
        buscar();
        String nombreFichero= ""+nombre;
        Log.d("DATOS----------:::::::::---->","EnCARGAR"+nombreFichero);
        if(nombreFichero!=null) {
            Log.d("DATOS----------:::::::::---->","ENCONTRE FICHERO");
            if (sdDisponible == true && sdAccesoEscritura == true) {
                try {
                    File ruta_sd = getExternalFilesDir(null);
                    File f = new File(ruta_sd.getAbsolutePath(), nombreFichero);

                        BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

                        do {
                            linea = fin.readLine();
                            Log.d("DATOS LINEA--->",linea);
                            System.out.println(linea);
                            if (linea == null) break;
                            nombre = linea + "\n";
                            velocidad = Integer.parseInt(linea+"\n");
                            fase=Integer.parseInt(linea+"\n");
                            Log.d("DATOS---->",mensaje);
                        } while (linea != null);

                } catch (Exception ex) {
                    Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.errorSD), Toast.LENGTH_LONG).show();
            }

        }else{

        }
    }

    public void guardar() {
        buscar();
        String nombreFichero=nombre.trim();
        if(sdDisponible==true && sdAccesoEscritura==true){
            if(nombre.trim().length()>0){
                try{
                    File ruta_sd = getExternalFilesDir(null);
                    File f=new File(ruta_sd.getAbsolutePath(),nombreFichero);
                    OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));
                    fout.write(nombre.trim()+"\n");
                    fout.write(velocidad+"\n");
                    fout.write(fase+"\n");
                    fout.close();
                }catch (Exception ex)
                {
                    Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
                }
            }else{
                Toast.makeText(this,getResources().getString(R.string.errorVacio),Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,getResources().getString(R.string.errorSD),Toast.LENGTH_LONG).show();
        }
    }
}
