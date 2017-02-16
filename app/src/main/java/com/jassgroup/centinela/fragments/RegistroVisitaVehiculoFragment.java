package com.jassgroup.centinela.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jassgroup.centinela.R;
import com.jassgroup.centinela.activity.RegistroPersonaActivity;
import com.jassgroup.centinela.activity.RegistroVehiculoActivity;
import com.jassgroup.centinela.activity.RegistroVisitaActivity;
import com.jassgroup.centinela.activity.RegistroVisitaVehiculoAsistenciaActivity;
import com.jassgroup.centinela.clases.Asistencia;
import com.jassgroup.centinela.clases.AsistenciaVehiculos;
import com.jassgroup.centinela.clases.Persona;
import com.jassgroup.database.GestorDatabase;

/**
 * Created by kenwhiston on 22/07/2016.
 */
public class RegistroVisitaVehiculoFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that
    //tag de la actividad
    private static final String TAG = "fragRegistro";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //declaramos las variables
    private EditText txtPlaca;
    private CoordinatorLayout coordinatorLayout;
    private Button botonSiguiente;


    private Context context;
    ////////////////////
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    private ProgressDialog mProgressDialog;

    //////////////////////
    private DatabaseReference mDatabase;


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Validando Placa...!");
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

    public RegistroVisitaVehiculoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroVisitaVehiculoFragment newInstance(String param1, String param2) {
        RegistroVisitaVehiculoFragment fragment = new RegistroVisitaVehiculoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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
        // [END auth_state_listener]

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            //logeo activo, se envia a pantalla principal

        } else {
            // No user is signed in
        }

        //obtener valores de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mDatabase = database.getReference("vehiculos");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_registro_visita_vehiculo, container, false);

        context = container.getContext();

        txtPlaca = (EditText)v.findViewById(R.id.txtplaca);
        botonSiguiente = (Button) v.findViewById(R.id.btn_sig);
        coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorLayout);

        botonSiguiente.setOnClickListener(this);


        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

        String Placa = txtPlaca.getText().toString();
        if (v.getId() == R.id.btn_sig){
            if (!this.validarDocumento(Placa)){
                hideProgressDialog();
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Ingrese número de Placa", Snackbar.LENGTH_LONG);
                snackbar.show();
                return;
            }

            mDatabase.child(GestorDatabase.getInstance(context).obtenerValorUsuario("ruc")).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            hideProgressDialog();
                            // Get user value
                            AsistenciaVehiculos miVehiculoAsistencia = dataSnapshot.getValue(AsistenciaVehiculos.class);
                            if(miVehiculoAsistencia!=null){
                                //existe placa y registrar asistencia
                                // Creating Bundle object
                                Bundle b = new Bundle();
                                // Storing data into bundle
                                b.putString("numdoc", miVehiculoAsistencia.numdoc);
                                b.putString("apenom", miVehiculoAsistencia.apenom);
                                b.putString("color", miVehiculoAsistencia.color);
                                b.putString("fecha", miVehiculoAsistencia.fecha);
                                b.putString("hora", miVehiculoAsistencia.hora);
                                b.putString("placa", miVehiculoAsistencia.placa);
                                b.putString("tipo", miVehiculoAsistencia.tipo);
                                b.putString("tipovehiculo", miVehiculoAsistencia.tipovehiculo);
                                Intent intent = new Intent(context, RegistroVisitaVehiculoAsistenciaActivity.class);
                                intent.putExtras(b);
                                startActivity(intent);

                            }else{
                                //no existe placa registrar vehiculo
                                Snackbar snackbar = Snackbar
                                        .make(coordinatorLayout, "No existe Vehículo..!", Snackbar.LENGTH_LONG)
                                        .setAction("REGISTRAR", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Bundle b = new Bundle();
                                                b.putString("placa",txtPlaca.getText().toString());
                                                Intent intent = new Intent(context, RegistroVehiculoActivity.class);
                                                intent.putExtras(b);
                                                startActivity(intent);

                                                /*Bundle b = new Bundle();
                                                b.putString("numdoc",txtDni.getText().toString());
                                                Intent intent = new Intent(context, RegistroPersonaActivity.class);
                                                intent.putExtras(b);
                                                startActivity(intent);*/
                                            }
                                        });

                                snackbar.show();
                            }
                            // ...
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            hideProgressDialog();
                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        }
                    });

        }
    }
}
