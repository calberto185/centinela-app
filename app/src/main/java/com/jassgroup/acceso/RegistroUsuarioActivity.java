package com.jassgroup.acceso;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jassgroup.database.GestorDatabase;
import com.jassgroup.internet.DetectorRed;
import com.jassgroup.master.BaseFirebaseActivity;
import com.jassgroup.centinela.HomeActivity;
import com.jassgroup.centinela.R;
import com.jassgroup.acceso.clases.Usuario;

public class RegistroUsuarioActivity extends BaseFirebaseActivity implements View.OnClickListener  {

    //toolbar de la aplicacion
    private Toolbar toolbar;

    ///variables principales de la clase login
    private String apellidos;
    private String nombres;
    private String correo;
    private String clave;
    private String ruc;

    private EditText cajaApe;
    private EditText cajaNom;
    private EditText cajaCorreo;
    private EditText cajaRuc;
    private EditText cajaClave;

    private TextView txtLogin;

    private Button miBotonCrear;
    private CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        this.setTAG("actRegistroUsuario");
        this.setCadenaDialogo("Registrando usuario");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            this.setMiTitulo(this.getResources().getString(R.string.txt_registrar_cuenta));
            toolbar.setTitle(getMiTitulo());
            setSupportActionBar(toolbar);
            /*toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    EditarProfileActivity.this.onBackPressed();
                }
            });*/
        }


        miBotonCrear = (Button) findViewById(R.id.botonCrear);

        cajaApe = (EditText) findViewById(R.id.txtApellidos);
        cajaNom = (EditText) findViewById(R.id.txtNombres);
        cajaNom = (EditText) findViewById(R.id.txtNombres);
        cajaCorreo = (EditText) findViewById(R.id.txtCorreo);
        cajaRuc = (EditText) findViewById(R.id.txtRuc);
        cajaClave = (EditText) findViewById(R.id.txtClave);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        txtLogin = (TextView) findViewById(R.id.txtIniciarSesion);


        miBotonCrear.setOnClickListener(this);
        txtLogin.setOnClickListener(this);


        setMiUser(FirebaseAuth.getInstance().getCurrentUser());
        if (this.getMiUser() != null) {
            // User is signed in
            //logeo activo, se envia a pantalla principal
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            // No user is signed in
        }
        setmDatabase(getDatabase().getReference("usuarios"));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.botonCrear:
                //click boton ir a registrar
                RegistrarUsuario();
                break;
            case R.id.txtIniciarSesion:
                Intent intent = new Intent(RegistroUsuarioActivity.this, IniciarSesionActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void RegistrarUsuario(){
        DetectorRed cd = new DetectorRed(getApplicationContext());

        Boolean isInternetPresent = cd.estasConectadoInternet(); // true or false

        if(!isInternetPresent){
            //hay servicio de internet
            //no hay internet
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Esta Conectado a Internet...!", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        ///validamos si hay datos en los campos
        if(cajaApe.getText().toString().isEmpty()){
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Ingrese sus apellidos completos",Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }
        if(cajaNom.getText().toString().isEmpty()){
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Ingrese sus nombres completos",Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }
        if(cajaCorreo.getText().toString().isEmpty()){
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Ingrese su correo correctamente",Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }
        if(cajaRuc.getText().toString().isEmpty()){
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Ingrese su ruc correctamente",Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }
        if(cajaClave.getText().toString().isEmpty()){
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Ingrese su correctamente su clave, porfavor..!!",Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        ////obtenemos y validamos los datos de login
        apellidos = cajaApe.getText().toString();
        nombres = cajaNom.getText().toString();
        correo = cajaCorreo.getText().toString();
        ruc = cajaRuc.getText().toString();
        clave = cajaClave.getText().toString();

        registrarUsuario(apellidos,nombres,correo,ruc,clave,"---");

    }

    private void registrarUsuario(final String apellidos, final String nombres, final String correo,final String ruc, final String clave, final String telefono){

        showProgressDialog();

        getmAuth().createUserWithEmailAndPassword(correo, clave)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(getTAG(), "createUserWithEmail:onComplete:" + task.isSuccessful());

                        String uid = task.getResult().getUser().getUid();

                        Usuario miUsuario = new Usuario(apellidos,nombres,correo,clave,telefono,"---",ruc);

                        GestorDatabase.getInstance(getMiContexto()).EliminarUsuarios();
                        GestorDatabase.getInstance(getMiContexto()).registrarUsuario(uid,miUsuario);

                        getmDatabase().child(miUsuario.ruc).child(uid).setValue(miUsuario);
                        hideProgressDialog();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegistroUsuarioActivity.this, "No se pudo registrar usuario, intente en otro momento porfavor...!",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            //guardamos los demas datos

                            Intent intent = new Intent(RegistroUsuarioActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        // ...
                    }
                });


    }
}
