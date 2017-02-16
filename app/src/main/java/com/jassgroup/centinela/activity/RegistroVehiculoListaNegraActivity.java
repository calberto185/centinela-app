package com.jassgroup.centinela.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.jassgroup.acceso.SignUpActivity;
import com.jassgroup.centinela.R;
import com.jassgroup.centinela.clases.ListaNegra;
import com.jassgroup.centinela.clases.ListaNegraVehiculos;
import com.jassgroup.centinela.clases.Persona;
import com.jassgroup.centinela.clases.Vehiculo;
import com.jassgroup.master.BaseFirebaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bernett on 19/08/2016.
 */
public class RegistroVehiculoListaNegraActivity extends BaseFirebaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private static final String TAG = "actRegistroPersona";
    private Vehiculo miVehiculo;
    private String placa;
    private String color;
    private String tipo;
    private String numdoc;
    private String conductor;


    private EditText txtPlaca;
    private EditText txtColor;
    private EditText txtTipo;
    private EditText txtNumdoc;
    private EditText txtconductor;
    private Button btn_reg;

    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_vehiculo_listanegra);

        // Set a toolbar to replace the action bar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.txt_registrar_vehiculoListanegra);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    RegistroVehiculoListaNegraActivity.this.onBackPressed();
                }
            });
        }

        txtPlaca = (EditText) findViewById(R.id.txtplaca);
        txtTipo = (EditText) findViewById(R.id.txttipo);
        txtColor = (EditText) findViewById(R.id.txtcolor);
        txtNumdoc = (EditText) findViewById(R.id.txtnumdoc);
        txtconductor = (EditText) findViewById(R.id.txtconductor);
        btn_reg = (Button) findViewById(R.id.btn_regVehiculo);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        //asignar eventos
        btn_reg.setOnClickListener(this);

        //obtener datos del anterior activty
        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        numdoc = bundle.getString("numdoc");
        placa = bundle.getString("placa");
        color = bundle.getString("color");
        tipo = bundle.getString("tipovehiculo");
        conductor = bundle.getString("nombres")+" "+ bundle.getString("apellidos");

        txtNumdoc.setText(numdoc);
        txtPlaca.setText(placa);
        txtconductor.setText(conductor);
        txtColor.setText(color);
        if (tipo.equals("M")){
            txtTipo.setText("Moto");
        }else if (tipo.equals("C")){
            txtTipo.setText("Carro");
        }



        setMiUser(FirebaseAuth.getInstance().getCurrentUser());
        if (this.getMiUser() != null) {
            // User is signed in

        } else {
            // No user is signed in
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }

        setmDatabase(getDatabase().getReference("listanegravehiculos"));

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onClick(View v) {

        String conductor;
        String color;
        String tipo;
        String placa;
        String numdoc;
        String fecha;
        String hora;

        if (v.getId() == R.id.btn_regVehiculo) {

            conductor = txtconductor.getText().toString();
            color = txtColor.getText().toString();
            tipo = txtTipo.getText().toString();
            placa = txtPlaca.getText().toString();
            numdoc = txtNumdoc.getText().toString();

            //fecha y hora
            // (1) get today's date
            Date today = Calendar.getInstance().getTime();

            // (2) create a date "formatter" (the date format we want)
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

            // (3) create a new String using the date format we want
            fecha = formatoFecha.format(today);
            hora = formatoHora.format(today);

            AgregarVehiculoListaNegra(conductor,conductor,numdoc,placa,tipo,color,fecha);

            /*if (!validar(nombres)) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Ingrese Nombre", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else if (!validar(apePaterno)) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Ingrese Apellido paterno", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else if (!validar(apeMaterno)) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Ingrese Apellido materno", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else if (!validar(sexo)) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Seleccione el Sexo", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                this.registrarPersona(apePaterno, apeMaterno, email, fecha, hora, "", nombres, numdoc, (sexo.equals("Masculino") ? "M" : "F"), telefono, "1");
            }*/
        }
    }

    public boolean validar(String cadena) {

        boolean bandera = false;

        if (cadena == null) {
            bandera = false;
        } else if (cadena.isEmpty()) {
            bandera = false;
        } else {
            bandera = true;
        }

        return bandera;
    }

    private void AgregarVehiculoListaNegra(String apellidos, String nombres, String numdoc, String placa, String tipo, String color,String fecha){

        //marca de tiempo
        long marcaTiempo = Calendar.getInstance().getTimeInMillis();
        String sMarcaTiempo = String.valueOf(marcaTiempo);

        //creamos el objeto asistencia
        ListaNegraVehiculos miLista = new ListaNegraVehiculos(apellidos,nombres,numdoc,placa,tipo,color,fecha);

        showProgressDialog();
        setmDatabase(getDatabase().getReference("listanegravehiculos"));
        getmDatabase().child(numdoc).setValue(miLista).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),
                            "Se registro Correctamente...!",
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Error intente en otro momento...!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }




    private void registrarPersona(final String apematerno, final String apepaterno, String correo, String fecharegistro, String horaregistro, String imagen, final String nombres, final String numdoc, String sexo, final String telefono, String tipodoc) {

        //marca de tiempo
        long marcaTiempo = Calendar.getInstance().getTimeInMillis();
        String sMarcaTiempo = String.valueOf(marcaTiempo);

        //creamos el objeto asistencia
        //miVehiculo = new Persona(apematerno, apepaterno, correo, fecharegistro, horaregistro, imagen, nombres, numdoc, sexo, telefono, tipodoc,"0");

        showProgressDialog();



        /*getmDatabase().child(numdoc).setValue(miPersona).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
                if (task.isSuccessful()) {
                    AgregarPersonaListaNegra(numdoc,ape,nombres,telefono,"");
                    Toast.makeText(getApplicationContext(),
                            "Se registro Correctamente...!",
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Error intente en otro momento...!", Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }

}
