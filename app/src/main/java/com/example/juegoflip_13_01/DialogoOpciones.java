package com.example.juegoflip_13_01;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.fragment.app.DialogFragment;

public class DialogoOpciones extends DialogFragment implements DialogInterface.OnClickListener {
    EditText etNombre;
    RadioButton rbFacil,rbMedio,rbDificil;
    DialogoOpcionesListener listener;
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState){
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getActivity());
        //atamos el cuadro de dialogo al layout "layoutOpciones"
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        dialogo.setView(inflater.inflate(R.layout.layout_opciones,null));
        //ponemos un titulo
        dialogo.setMessage(getResources().getString(R.string.titOpciones));
        dialogo.setPositiveButton("Aceptar",this);
        dialogo.setNegativeButton("Cancelar",this);
        return dialogo.create();
    }
    //gracias a implementar hhacemos este metodo sino habria que hacerlo en los this del dialogo
    @Override
    public void onClick(DialogInterface dialog, int which) {
    //cogemos los elementos
        etNombre=((Dialog)dialog).findViewById(R.id.etNombre);
        rbFacil=((Dialog)dialog).findViewById(R.id.rbFacil);
        rbMedio=((Dialog)dialog).findViewById(R.id.rbMedio);
        rbDificil=((Dialog)dialog).findViewById(R.id.rbDificil);
        int velocidad=0;
        String nombre=etNombre.getText().toString().trim();
        if(nombre.length()==0){
            nombre="Guest";
        }
        //Fijamos el retardo en funcion de los radiobutton
        if(rbFacil.isChecked()) velocidad=60;
        if(rbMedio.isChecked()) velocidad=40;
        if(rbDificil.isChecked()) velocidad=20;
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                listener.onDialogoOpcionesListener(nombre,velocidad);
                break;
             case DialogInterface.BUTTON_NEGATIVE:
                 getActivity().finish();
        }

    }

    //Necesito construir esta interfaz, nombre da igual
    public interface DialogoOpcionesListener{
        public void onDialogoOpcionesListener(String nombre,int velocidad);
    }

    //metodo obligatorio para atarlo a la clase que lo llama
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            listener=(DialogoOpcionesListener)context;
        }catch (Exception ex){

        }
    }
}
