package com.jassgroup.acceso;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import android.support.annotation.NonNull;
import com.jassgroup.acceso.clases.Usuario;
import com.jassgroup.database.GestorDatabase;
import com.jassgroup.internet.DetectorRed;
import com.jassgroup.master.BaseFirebaseActivity;
import com.jassgroup.centinela.HomeActivity;
import com.jassgroup.centinela.R;

public class IniciarSesionActivity extends BaseFirebaseActivity implements View.OnClickListener  {

    private CoordinatorLayout coordinatorLayout;

    //toolbar de la aplicacion
    private Toolbar toolbar;

    ///variables principales de la clase login
    private String usuario;
    private String clave;
    private String estado;
    private String apenom;
    private String correo;
    private String iduser;

    private EditText cajaUsuario;
    private EditText cajaClave;

    private Button miBotonIngresar;
    private TextView miTextoRegistro;
    private TextView miTextoOlvido;

    private Usuario miUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        this.setTAG("actIniciarSesion");
        this.setCadenaDialogo("Comprobando credenciales");

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        //inicializando Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            this.setMiTitulo(this.getResources().getString(R.string.txt_iniciar_sesion));
            toolbar.setTitle(getMiTitulo());
            setSupportActionBar(toolbar);
            /*toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    EditarProfileActivity.this.onBackPressed();
                }
            });*/
        }

        miBotonIngresar = (Button) findViewById(R.id.btn_guardar);
        miTextoRegistro = (TextView) findViewById(R.id.texto_registro_ahora);
        miTextoOlvido = (TextView) findViewById(R.id.texto_olvido_clave);
        cajaUsuario = (EditText) findViewById(R.id.txtUsuario);
        cajaClave = (EditText) findViewById(R.id.txtClave);

        miBotonIngresar.setOnClickListener(this);
        miTextoRegistro.setOnClickListener(this);
        miTextoOlvido.setOnClickListener(this);

        setMiUser(FirebaseAuth.getInstance().getCurrentUser());
        if (this.getMiUser() != null) {
            // User is signed in
            //logeo activo, se envia a pantalla principal
            Intent intent = new Intent(IniciarSesionActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // No user is signed in
        }

        setmDatabase(getDatabase().getReference("usuarios"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_guardar:
                //click boton ingresar
                validarAcceso();
                break;
            case R.id.texto_registro_ahora:
                //click boton ir a registrar
                Intent intent = new Intent(this, RegistroUsuarioActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.texto_olvido_clave:
                //click boton ir a registrar
                Intent intent2 = new Intent(this, ReestablecerClaveActivity.class);
                startActivity(intent2);
                //finish();
                break;
        }
    }

    private void validarAcceso(){
        DetectorRed cd = new DetectorRed(getApplicationContext());

        Boolean isInternetPresent = cd.estasConectadoInternet(); // true or false

        if(!isInternetPresent){
            //hay servicio de internet
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No Esta Conectado a Internet...!", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        ///validamos si hay datos en los campos
        if(cajaUsuario.getText().toString().isEmpty()){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Ingrese su Usuario o Correo, porfavor...!", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        if(cajaClave.getText().toString().isEmpty()){
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Ingrese su Clave, porfavor...!", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        ////obtenemos y validamos los datos de login
        usuario = cajaUsuario.getText().toString();
        clave = cajaClave.getText().toString();

        showProgressDialog();

        getmAuth().signInWithEmailAndPassword(usuario, clave)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(getTAG(), "signInWithEmail:onComplete:" + task.isSuccessful());
                        //hideProgressDialog();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            hideProgressDialog();
                            Log.w(getTAG(), "signInWithEmail", task.getException());
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "Inicio de sesion fallido...!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }else{

                            setMiUser(FirebaseAuth.getInstance().getCurrentUser());

                            getmDatabase().child(getMiUser().getUid()).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // Get user value
                                            hideProgressDialog();
                                            miUsuario = dataSnapshot.getValue(Usuario.class);

                                            GestorDatabase.getInstance(getMiContexto()).EliminarUsuarios();

                                            GestorDatabase.getInstance(getMiContexto()).registrarUsuario(getMiUser().getUid(),miUsuario);

                                            Intent intent = new Intent(IniciarSesionActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();

                                            // ...
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            hideProgressDialog();
                                            Log.w(getTAG(), "getUser:onCancelled", databaseError.toException());
                                        }
                                    });

                        }

                        // ...
                    }
                });
        ///////////////////////////////////////////////
    }

}
