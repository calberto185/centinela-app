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
import com.jassgroup.centinela.R;
import com.jassgroup.centinela.clases.Persona;
import com.jassgroup.centinela.clases.Vehiculo;
import com.jassgroup.database.GestorDatabase;
import com.jassgroup.master.BaseFirebaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegistroVehiculoActivity extends BaseFirebaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private static final String TAG = "actRegistroVehiculo";
    private Vehiculo miVehiculo;
    private String numdoc;
    private String placa;
    private String nombres;
    private String apePaterno;
    private String apeMaterno;
    private String color;
    private String tipo;
    private String fecha;
    private String hora;

    private EditText txtnumdoc;
    private EditText txtplaca;
    private EditText txtapePaterno;
    private EditText txtapeMaterno;
    private SwitchCompat swtipo;
    private EditText txtcolor;
    private EditText txtombres;
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
        setContentView(R.layout.activity_registro_vehiculo);

        // Set a toolbar to replace the action bar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.txt_RegistrarVehiculo);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    RegistroVehiculoActivity.this.onBackPressed();
                }
            });
        }

        txtnumdoc = (EditText) findViewById(R.id.txtdni);
        txtplaca = (EditText) findViewById(R.id.txtplaca);
        swtipo = (SwitchCompat) findViewById(R.id.tipo);
        txtcolor = (EditText) findViewById(R.id.txtcolor);
        btn_reg = (Button) findViewById(R.id.btn_regVehiculo);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        context = this;
        //asignar eventos
        swtipo.setOnCheckedChangeListener(this);
        btn_reg.setOnClickListener(this);

        //obtener datos del anterior activty
        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        placa = bundle.getString("placa");
        txtplaca.setText(placa);

        swtipo.setText("Carro");

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
            Intent intent = new Intent(RegistroVehiculoActivity.this, RegistroVisitaActivity.class);
            startActivity(intent);
            finish();
        }

        //obtener valores de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("vehiculos");

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            swtipo.setText("Carro");
        } else {
            swtipo.setText("Moto");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_regPersona) {

            numdoc = txtnumdoc.getText().toString();
            placa = txtplaca.getText().toString();
            color = txtcolor.getText().toString();
            tipo = swtipo.getText().toString();

            //fecha y hora
            // (1) get today's date
            Date today = Calendar.getInstance().getTime();

            // (2) create a date "formatter" (the date format we want)
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

            // (3) create a new String using the date format we want
            fecha = formatoFecha.format(today);
            hora = formatoHora.format(today);


            if (!validar(placa)) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Ingrese Placa", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else if (!validar(color)) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Ingrese Color", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else if (!validar(tipo)) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Ingrese Tipo de Vehículo", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else if (!validar(numdoc)) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Ingrese DNI", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                this.registrarVehiculo(placa,tipo,color,numdoc);
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


    private void registrarVehiculo(String placa, String tipo, String color, String numdoc) {


        //creamos el objeto vehiculo
        miVehiculo = new Vehiculo(placa,tipo,color, numdoc);

        showProgressDialog();

        //empresavehiculo
        //1. actualizar el reference, empresavehiculo
        //2. mDatabase.child("ruc").child("placa").setValue

        mDatabase.child(placa).setValue(miVehiculo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Se registro Correctamente...!",Toast.LENGTH_LONG).show();
                    //
                    setmDatabase(getDatabase().getReference("empresasvehiculos"));
                    //
                    mDatabase.child(GestorDatabase.getInstance(getApplicationContext()).obtenerValorUsuario("ruc")).setValue(miVehiculo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideProgressDialog();
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),"Se registro Correctamente...!",Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Error intente en otro momento...!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Error intente en otro momento...!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}