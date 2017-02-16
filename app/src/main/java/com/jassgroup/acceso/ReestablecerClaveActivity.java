package com.jassgroup.acceso;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.jassgroup.internet.DetectorRed;
import com.jassgroup.master.BaseFirebaseActivity;
import com.jassgroup.centinela.HomeActivity;
import com.jassgroup.centinela.R;

public class ReestablecerClaveActivity extends BaseFirebaseActivity implements View.OnClickListener  {

    //toolbar de la aplicacion
    private Toolbar toolbar;

    ///variables principales de la clase login
    private String usuario;

    private EditText cajaUsuario;

    private Button miBotonRenovar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reestablecer_clave);

        this.setTAG("actRenovarClave");
        this.setCadenaDialogo("Enviando correo para proceso de renovacion de clave...!");

        //inicializando Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            this.setMiTitulo(this.getResources().getString(R.string.txt_renovarclave));
            toolbar.setTitle(getMiTitulo());
            setSupportActionBar(toolbar);
            /*toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    EditarProfileActivity.this.onBackPressed();
                }
            });*/
        }

        miBotonRenovar = (Button) findViewById(R.id.btn_renovarclave);
        cajaUsuario = (EditText) findViewById(R.id.txtUsuario);

        miBotonRenovar.setOnClickListener(this);


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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_renovarclave:
                //click boton ingresar
                renovarclave();
                break;
        }
    }

    private void renovarclave(){
        DetectorRed cd = new DetectorRed(getApplicationContext());

        Boolean isInternetPresent = cd.estasConectadoInternet(); // true or false

        if(!isInternetPresent){
            //hay servicio de internet
            //no hay internet
            Toast.makeText(getApplicationContext(),
                    "No Esta Conectado a Internet...!", Toast.LENGTH_SHORT).show();
            return;
        }

        ///validamos si hay datos en los campos
        if(cajaUsuario.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Ingreseo Correo, porfavor...!",Toast.LENGTH_SHORT).show();
            return;
        }

        ////obtenemos y validamos los datos de login
        usuario = cajaUsuario.getText().toString();

        showProgressDialog();

        getmAuth().sendPasswordResetEmail(usuario)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            Log.d(getTAG(), "Email sent.");
                            Toast.makeText(getApplicationContext(),
                                    "Correo enviado para renovacion de clave, reviselo porfavor...!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
