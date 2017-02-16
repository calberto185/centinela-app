package com.jassgroup.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jassgroup.acceso.SignUpActivity;
import com.jassgroup.acceso.clases.Usuario;
import com.jassgroup.database.GestorDatabase;
import com.jassgroup.database.ScriptDatabaseUsuario;
import com.jassgroup.internet.DetectorRed;
import com.jassgroup.master.BaseFirebaseActivity;
import com.jassgroup.centinela.HomeActivity;
import com.jassgroup.centinela.R;

public class EditarProfileActivity extends BaseFirebaseActivity {

    // iniciamos toolbar
    private Toolbar toolbar;

    //////////////////////
    private String apellidos;
    private String nombres;
    private String correo;
    private String ruc;
    private String clave;
    private String telefono;
    private String imagen;

    private EditText txtApellidos;
    private EditText txtNombres;
    private EditText txtCorreo;
    private EditText txtRuc;
    private EditText txtTelefono;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_profile);

        this.setTAG("actEditar");
        this.setCadenaDialogo("Actualizando datos de perfil");

        // Set a toolbar to replace the action bar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            this.setMiTitulo(this.getResources().getString(R.string.txt_registrar_cuenta));
            toolbar.setTitle(getMiTitulo());
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    EditarProfileActivity.this.onBackPressed();
                }
            });
        }

        txtApellidos = (EditText) findViewById(R.id.txtApellidos);
        txtNombres = (EditText) findViewById(R.id.txtNombres);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtRuc = (EditText) findViewById(R.id.txtRuc);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);

        setMiUser(FirebaseAuth.getInstance().getCurrentUser());
        if (this.getMiUser() != null) {
            // User is signed in

        } else {
            // No user is signed in
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }

        setmDatabase(getDatabase().getReference("usuarios"));

        iniciarPerfil();
    }

    private void iniciarPerfil(){
        apellidos = GestorDatabase.getInstance(getMiContexto()).obtenerValorUsuario(ScriptDatabaseUsuario.Column.APELLIDOS);
        nombres = GestorDatabase.getInstance(getMiContexto()).obtenerValorUsuario(ScriptDatabaseUsuario.Column.NOMBRES);
        correo = GestorDatabase.getInstance(getMiContexto()).obtenerValorUsuario(ScriptDatabaseUsuario.Column.CORREO);
        ruc = GestorDatabase.getInstance(getMiContexto()).obtenerValorUsuario(ScriptDatabaseUsuario.Column.RUC);
        clave = GestorDatabase.getInstance(getMiContexto()).obtenerValorUsuario(ScriptDatabaseUsuario.Column.CLAVE);
        telefono = GestorDatabase.getInstance(getMiContexto()).obtenerValorUsuario(ScriptDatabaseUsuario.Column.TELEFONO);
        imagen = GestorDatabase.getInstance(getMiContexto()).obtenerValorUsuario(ScriptDatabaseUsuario.Column.IMAGEN);

        txtApellidos.setText(apellidos);
        txtNombres.setText(nombres);
        txtCorreo.setText(correo);
        txtRuc.setText(ruc);
        txtTelefono.setText(telefono);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editar_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu_guardar) {

            guardarPerfil();

            return  true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void guardarPerfil(){


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
        if(txtApellidos.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Ingrese su apellidos completos , porfavor...!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(txtNombres.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Ingrese su apellidos completos , porfavor...!",Toast.LENGTH_SHORT).show();
            return;
        }


        ////obtenemos y validamos los datos de login
        apellidos = txtApellidos.getText().toString();
        nombres = txtNombres.getText().toString();
        telefono = txtTelefono.getText().toString();

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final Usuario miUsuario = new Usuario(apellidos,nombres,correo,clave,telefono,imagen,ruc);

        /*mDatabase.child(uid).child("apellidos").setValue(apellidos);
        mDatabase.child(uid).child("nombres").setValue(nombres);
        mDatabase.child(uid).child("telefono").setValue(telefono);*/

        showProgressDialog();

        getmDatabase().child(uid).setValue(miUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
                if (task.isSuccessful()) {
                    Log.d(getTAG(), "User profile updated.");
                    GestorDatabase.getInstance(getMiContexto()).actualizarUsuario(uid,miUsuario);

                    Toast.makeText(getApplicationContext(),
                            "Perfil actualizado...!",
                            Toast.LENGTH_LONG).show();
                }else{
                    Log.d(getTAG(), "User profile not updated.");
                    Toast.makeText(getApplicationContext(),
                            "Error intente en otro momento...!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
