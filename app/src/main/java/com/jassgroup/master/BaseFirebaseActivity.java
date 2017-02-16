package com.jassgroup.master;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by kenwhiston on 13/07/2016.
 */
public class BaseFirebaseActivity extends AppCompatActivity {

    private String TAG = "act_";

    private ProgressDialog mProgressDialog;

    private String cadenaDialogo;

    private Context miContexto;

    private String miTitulo;

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser miUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inicializamos contexto de la actividad
        setMiContexto(this);

        // [START initialize_auth]
        setmAuth(FirebaseAuth.getInstance());
        // [END initialize_auth]

        //obtener valores de la base de datos
        setDatabase(FirebaseDatabase.getInstance());

        // [START auth_state_listener]
        setmAuthListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(getTAG(), "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(getTAG(), "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                //updateUI(user);
                // [END_EXCLUDE]
            }
        });

    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();

        getmAuth().addAuthStateListener(getmAuthListener());

    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();

        if (getmAuthListener() != null) {
            getmAuth().removeAuthStateListener(getmAuthListener());
        }

        hideProgressDialog();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getCadenaDialogo());
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

    /**
     * @TAG
     * Etiqueta para log
     */
    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getCadenaDialogo() {
        return cadenaDialogo;
    }

    public void setCadenaDialogo(String cadenaDialogo) {
        this.cadenaDialogo = cadenaDialogo;
    }

    public Context getMiContexto() {
        return miContexto;
    }

    public void setMiContexto(Context miContexto) {
        this.miContexto = miContexto;
    }

    public String getMiTitulo() {
        return miTitulo;
    }

    public void setMiTitulo(String miTitulo) {
        this.miTitulo = miTitulo;
    }

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public void setmDatabase(DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public FirebaseAuth.AuthStateListener getmAuthListener() {
        return mAuthListener;
    }

    public void setmAuthListener(FirebaseAuth.AuthStateListener mAuthListener) {
        this.mAuthListener = mAuthListener;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    public FirebaseUser getMiUser() {
        return miUser;
    }

    public void setMiUser(FirebaseUser miUser) {
        this.miUser = miUser;
    }
}
