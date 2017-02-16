package com.jassgroup.centinela.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.jassgroup.centinela.R;
import com.jassgroup.centinela.clases.Asistencia;
import com.jassgroup.centinela.clases.Persona;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegistroPersonaActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

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

    private EditText txtnumdoc;
    private EditText txtnom;
    private EditText txtapePaterno;
    private EditText txtapeMaterno;
    private SwitchCompat swsexo;
    private EditText txtemail;
    private EditText txttelefono;
    private Button btn_reg;

    private ProgressDialog mProgressDialog;
    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private Context context;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

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
                    RegistroPersonaActivity.this.onBackPressed();
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
        context = this;
        //asignar eventos
        swsexo.setOnCheckedChangeListener(this);
        btn_reg.setOnClickListener(this);

        //obtener datos del anterior activty
        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        numdoc = bundle.getString("numdoc");
        txtnumdoc.setText(numdoc);

        swsexo.setText("Femenino");

        ///////////////////
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                //updateUI(user);
                // [END_EXCLUDE]
            }
        };

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

        } else {
            // No user is signed in
            // User is signed in
            //logeo activo, se envia a pantalla principal
            Intent intent = new Intent(RegistroPersonaActivity.this, RegistroVisitaActivity.class);
            startActivity(intent);
            finish();
        }

        //obtener valores de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("personas");

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

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Registrando Persona...!");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
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


    private void registrarPersona(String apematerno, String apepaterno, String correo, String fecharegistro, String horaregistro, String imagen, String nombres, String numdoc, String sexo, String telefono, String tipodoc) {

        //marca de tiempo
        long marcaTiempo = Calendar.getInstance().getTimeInMillis();
        String sMarcaTiempo = String.valueOf(marcaTiempo);

        //creamos el objeto asistencia
        miPersona = new Persona(apematerno, apepaterno, correo, fecharegistro, horaregistro, imagen, nombres, numdoc, sexo, telefono, tipodoc,"0");

        showProgressDialog();

        mDatabase.child(numdoc).setValue(miPersona).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),
                            "Se registro Correctamente...!",
                            Toast.LENGTH_LONG).show();
                    Bundle b = new Bundle();
                    // Storing data into bundle
                    b.putString("numdoc", miPersona.numdoc);
                    b.putString("apepaterno", miPersona.apepaterno);
                    b.putString("apematerno", miPersona.apematerno);
                    b.putString("nombres", miPersona.nombres);
                    Intent intent = new Intent(context, RegistroVisitaActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Error intente en otro momento...!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}