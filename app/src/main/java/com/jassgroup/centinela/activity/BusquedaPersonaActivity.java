package com.jassgroup.centinela.activity;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jassgroup.acceso.SignUpActivity;
import com.jassgroup.centinela.R;
import com.jassgroup.centinela.clases.Persona;
import com.jassgroup.master.BaseFirebaseActivity;

public class BusquedaPersonaActivity extends BaseFirebaseActivity implements View.OnClickListener {

    // iniciamos toolbar
    private Toolbar toolbar;

    //declaramos las variables
    private EditText txtDni;
    private CoordinatorLayout coordinatorLayout;
    private Button botonSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_persona);

        this.setTAG("actEditar");
        this.setCadenaDialogo("Actualizando datos de perfil");

        // Set a toolbar to replace the action bar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            this.setMiTitulo(this.getResources().getString(R.string.txt_registrar_listanegra));
            toolbar.setTitle(getMiTitulo());
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    BusquedaPersonaActivity.this.onBackPressed();
                }
            });
        }

        txtDni = (EditText) findViewById(R.id.txtdni);
        botonSiguiente = (Button) findViewById(R.id.btn_sig);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        botonSiguiente.setOnClickListener(this);



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

    private boolean validarDocumento(String numDoc){
        boolean bandera = false;
        if (!numDoc.isEmpty()){
            bandera= true;
        }

        return  bandera;
    }

    @Override
    public void onClick(View v) {
        showProgressDialog();

        String numDoc = txtDni.getText().toString();
        if (v.getId() == R.id.btn_sig){
            if (!this.validarDocumento(numDoc)){
                hideProgressDialog();
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Ingrese numero de documento", Snackbar.LENGTH_LONG);
                snackbar.show();
                return;
            }

            getmDatabase().child(numDoc).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            hideProgressDialog();
                            // Get user value
                            Persona miPersona = dataSnapshot.getValue(Persona.class);
                            if(miPersona!=null){
                                //existe dni
                                // Creating Bundle object
                                Bundle b = new Bundle();
                                // Storing data into bundle
                                b.putString("numdoc", miPersona.numdoc);
                                b.putString("apepaterno", miPersona.apepaterno);
                                b.putString("apematerno", miPersona.apematerno);
                                b.putString("nombres",miPersona.nombres);
                                b.putString("telefono",miPersona.telefono);
                                Intent intent = new Intent(getMiContexto(), AgregarPersonaListaNegraActivy.class);
                                intent.putExtras(b);
                                startActivity(intent);
                            }else{
                                //no existe dni
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "No existe este numero de documento", Snackbar.LENGTH_LONG)
                                        .setAction("REGISTRAR", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Bundle b = new Bundle();
                                                b.putString("numdoc",txtDni.getText().toString());
                                                Intent intent = new Intent(getMiContexto(), RegistroPersonaListaNegraActivity.class);
                                                intent.putExtras(b);
                                                startActivity(intent);
                                            }
                                        });

                                snackbar.show();
                            }
                            // ...
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            hideProgressDialog();
                            Log.w(getTAG(), "getUser:onCancelled", databaseError.toException());
                        }
                    });

        }
    }
}
