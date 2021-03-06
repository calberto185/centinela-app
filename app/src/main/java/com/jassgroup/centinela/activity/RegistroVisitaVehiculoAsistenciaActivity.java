package com.jassgroup.centinela.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import com.jassgroup.master.BaseFirebaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class RegistroVisitaVehiculoAsistenciaActivity extends BaseFirebaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    //tag de la actividad
    private static final String TAG = "actRegistroAsistenciaVehiculos";

    //contexto de la actividad
    private Context contexto;

    //variables de la aplicacion
    private Map<String,String> mMap;
    private ProgressDialog pDialog;

    // iniciamos toolbar
    private Toolbar toolbar;
    String titulo = "CheckInApp";

    ////////////////////
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    private ProgressDialog mProgressDialog;

    private Context context = this;
    //////////////////////
    private DatabaseReference mDatabase;

    private String numDoc;
    private String placa;
    private String conductor;
    private String color;
    private String tipoAsistencia;
    private String fecha;
    private String hora;
    private String fechahora;
    private String tipoVehiculo;

    private EditText txtNumDoc;
    private EditText txtConductor;
    private EditText txtPlaca;
    private EditText txtColor;
    private SwitchCompat txtTipoAsistencia;
    private EditText txtFechaHora;
    private SwitchCompat txtTipoVehiculo;
    private Button btn_registrar;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_visita_vehiculo_asistencia);

        // Set a toolbar to replace the action bar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            titulo = this.getResources().getString(R.string.txt_VgrabarvisitaVehiculo);
            toolbar.setTitle(titulo);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    RegistroVisitaVehiculoAsistenciaActivity.this.onBackPressed();
                }
            });
        }

        txtNumDoc = (EditText) findViewById(R.id.txtdni);
        txtConductor = (EditText) findViewById(R.id.txtconductor);
        txtPlaca = (EditText) findViewById(R.id.txtplaca);
        txtColor= (EditText) findViewById(R.id.txtcolor);
        txtFechaHora = (EditText) findViewById(R.id.txtfecha);
        btn_registrar = (Button) findViewById(R.id.btn_registrar);
        txtTipoAsistencia = (SwitchCompat) findViewById(R.id.tipoingreso);
        txtTipoVehiculo = (SwitchCompat) findViewById(R.id.tipovehiculo);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        btn_registrar.setOnClickListener(this);
        txtTipoVehiculo.setOnCheckedChangeListener(this);
        txtTipoAsistencia.setOnCheckedChangeListener(this);

        // get the Intent that started this Activity
        Intent in = getIntent();
        //get the Bundle that stores the data of this Activity
        Bundle b = in.getExtras();
        //getting data from bundle
        numDoc = b.getString("numdoc");
        conductor = b.getString("apepaterno")+" "+b.getString("apematerno")+" ,"+b.getString("nombres");

        // (1) get today's date
        Date today = Calendar.getInstance().getTime();

        // (2) create a date "formatter" (the date format we want)
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

        SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // (3) create a new String using the date format we want
        fecha = formatoFecha.format(today);
        hora = formatoHora.format(today);

        fechahora = formatoFechaHora.format(today);
        txtNumDoc.setText(numDoc);
        txtConductor.setText(conductor);
        txtFechaHora.setText(fechahora);


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
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                //updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

        } else {
            // No user is signed in
            // User is signed in
            //logeo activo, se envia a pantalla principal
            Intent intent = new Intent(RegistroVisitaVehiculoAsistenciaActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }

        //obtener valores de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mDatabase = database.getReference("asistenciavehiculos");

    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

        hideProgressDialog();
    }
    // [END on_stop_remove_listener]

    //FALTA EL COORDINATOR LAYOUT

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Registrando asistencia vehículo...!");
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

    @Override
    public void onClick(View v) {
/*
        area = txtArea.getText().toString();
        motivo = txtAsunto.getText().toString();
        autorizado = txtAutorizado.getText().toString();
        tipo = sw_tipo.getText().toString();

        if (v.getId()== R.id.btn_registrar){

            if (!validar(area)){
                //Toast.makeText(getApplicationContext(),"Bien",Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Ingrese Area", Snackbar.LENGTH_LONG);
                snackbar.show();
            }else if(!validar(motivo)){
                //Toast.makeText(getApplicationContext(),"Bien",Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Ingrese Motivo", Snackbar.LENGTH_LONG);
                snackbar.show();
            }else{
                //ok
                this.registrarAsistencia(numDoc, area, motivo, autorizado, fecha, hora, (tipo.equals("Ingreso")?"I":"S"));
            }
        }*/

    }

    private void registrarAsistencia(String numdoc, String area, String motivo, String autorizado, String fecha, String hora, String tipo){

        //marca de tiempo
        long marcaTiempo = Calendar.getInstance().getTimeInMillis();
        String sMarcaTiempo = String.valueOf(marcaTiempo);

        //creamos el objeto asistencia
        Asistencia miAsistencia = new Asistencia(area,autorizado,fecha,hora,motivo,numdoc,tipo);

        showProgressDialog();

        mDatabase.child(sMarcaTiempo).setValue(miAsistencia).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    //validar cajas de texto
    public boolean validar(String cadena){

        boolean bandera=false;

        if(cadena==null){
            bandera = false;
        }else if(cadena.isEmpty()){
            bandera = false;
        }else{
            bandera = true;
        }

        return bandera;
    }

    //validar switch
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){

        }else{

        }
    }
}
