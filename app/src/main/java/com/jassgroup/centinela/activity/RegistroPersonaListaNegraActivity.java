package com.jassgroup.centinela.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jassgroup.acceso.SignUpActivity;
import com.jassgroup.centinela.HomeActivity;
import com.jassgroup.centinela.R;
import com.jassgroup.centinela.clases.ListaNegra;
import com.jassgroup.centinela.clases.Persona;
import com.jassgroup.master.BaseFirebaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bernett on 19/08/2016.
 */
public class RegistroPersonaListaNegraActivity extends BaseFirebaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private static final String TAG = "actRegistroPersona";
    private Persona miPersona;
    private String numdoc;
    private String nombres;
    private String apePaterno;
    private String apeMaterno;
    private String sexo;
    private String email;
    private String telefono;
    private String fecha;
    private String hora;
    private String ape;

    private EditText txtnumdoc;
    private EditText txtnom;
    private EditText txtapePaterno;
    private EditText txtapeMaterno;
    private SwitchCompat swsexo;
    private EditText txtemail;
    private EditText txttelefono;
    private Button btn_reg;

    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_persona);

        // Set a toolbar to replace the action bar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.txt_RegistrarPersona);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    RegistroPersonaListaNegraActivity.this.onBackPressed();
                }
            });
        }

        txtnumdoc = (EditText) findViewById(R.id.txtdni);
        txtnom = (EditText) findViewById(R.id.txtnom);
        txtapePaterno = (EditText) findViewById(R.id.txtapepat);
        txtapeMaterno = (EditText) findViewById(R.id.txtapemat);
        swsexo = (SwitchCompat) findViewById(R.id.tipo);
        txtemail = (EditText) findViewById(R.id.txtcorreo);
        txttelefono = (EditText) findViewById(R.id.txttelefono);
        btn_reg = (Button) findViewById(R.id.btn_regPersona);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        //asignar eventos
        swsexo.setOnCheckedChangeListener(this);
        btn_reg.setOnClickListener(this);

        //obtener datos del anterior activty
        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        numdoc = bundle.getString("numdoc");
        txtnumdoc.setText(numdoc);

        swsexo.setText("Femenino");

        setMiUser(FirebaseAuth.getInstance().getCurrentUser());
        if (this.getMiUser() != null) {
            // User is signed in

        } else {
            // No user is signed in
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }

        setmDatabase(getDatabase().getReference("personas"));

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            swsexo.setText("Masculino");
        } else {
            swsexo.setText("Femenino");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_regPersona) {

            nombres = txtnom.getText().toString();
            apePaterno = txtapePaterno.getText().toString();
            apeMaterno = txtapeMaterno.getText().toString();
            sexo = swsexo.getText().toString();
            email = txtemail.getText().toString();
            telefono = txttelefono.getText().toString();
            ape = apePaterno+" "+apeMaterno;

            //fecha y hora
            // (1) get today's date
            Date today = Calendar.getInstance().getTime();

            // (2) create a date "formatter" (the date format we want)
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

            // (3) create a new String using the date format we want
            fecha = formatoFecha.format(today);
            hora = formatoHora.format(today);


            if (!validar(nombres)) {
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
            }


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

    private void AgregarPersonaListaNegra(String numdoc, String apellidos, String nombres, String telefono, String imagen){

        //marca de tiempo
        long marcaTiempo = Calendar.getInstance().getTimeInMillis();
        String sMarcaTiempo = String.valueOf(marcaTiempo);

        //creamos el objeto asistencia
        ListaNegra miLista = new ListaNegra(numdoc,apellidos,nombres,telefono,imagen);

        showProgressDialog();
        setmDatabase(getDatabase().getReference("listanegra"));
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
        miPersona = new Persona(apematerno, apepaterno, correo, fecharegistro, horaregistro, imagen, nombres, numdoc, sexo, telefono, tipodoc,"0");

        showProgressDialog();



        getmDatabase().child(numdoc).setValue(miPersona).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        });
    }

}
