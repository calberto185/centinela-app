package com.jassgroup.acceso;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.jassgroup.master.BaseFirebaseActivity;
import com.jassgroup.centinela.HomeActivity;
import com.jassgroup.centinela.R;

public class SignUpActivity extends BaseFirebaseActivity implements View.OnClickListener  {

    private Button miBotonIngresar;
    private Button miBotonRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.setTAG("actSignUp");
        this.setCadenaDialogo("SignUp");

        miBotonIngresar = (Button) findViewById(R.id.botonIniciarSesion);
        miBotonRegistro = (Button) findViewById(R.id.botonEmpezar);

        miBotonIngresar.setOnClickListener(this);
        miBotonRegistro.setOnClickListener(this);

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
        Intent intent = null;
        switch (v.getId()) {
            case R.id.botonIniciarSesion:
                //click boton ir a iniciar sesion
                intent = new Intent(this, IniciarSesionActivity.class);
                startActivity(intent);
                //finish();
                break;
            case R.id.botonEmpezar:
                //click boton ir a registrar
                intent = new Intent(this, RegistroUsuarioActivity.class);
                startActivity(intent);
                //finish();
                break;
        }
    }

}
