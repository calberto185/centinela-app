package com.jassgroup.centinela.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jassgroup.centinela.R;
import com.jassgroup.centinela.activity.BusquedaPersonaActivity;
import com.jassgroup.centinela.activity.BusquedaVehiculoActivity;
import com.jassgroup.centinela.adapters.ItemListaNegraAdapter;
import com.jassgroup.centinela.adapters.ItemListaNegraVehiculosAdapter;
import com.jassgroup.centinela.clases.ListaNegra;
import com.jassgroup.centinela.clases.ListaNegraVehiculos;
import com.jassgroup.database.GestorDatabase;
import com.jassgroup.database.ScriptDatabaseUsuario;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kenwhiston on 13/08/2016.
 */
public class ListadoListaNegraVehiculosFragment extends Fragment {

    private UltimateRecyclerView mRecycler;
    private ItemListaNegraVehiculosAdapter mAdapter;
    private ItemListaNegraVehiculosAdapter mAdapterSavedState;
    private List<ListaNegraVehiculos> sampleData = null;
    private List<ListaNegraVehiculos> sampleDataSaveState = null;
    private List<ListaNegraVehiculos> miListaTemp = null;
    ListaNegraVehiculos miListaNegraVehiculos;
    private Context miContexto = null;
    LinearLayoutManager linearLayoutManager;
    private static Toolbar toolbarUI;
    public static final String TAG = "PruebaFragment";
    private String idusuario=null;
    public String correo;
    private static final String KEY_ADAPTER_STATE = "KEY_ADAPTER_STATE";
    private static final String KEY_CICLE = "ESTADO_FRAGMENT";
    private ProgressBar progressBar;
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

    ////////////////////////////////////
    private String lastKey = null;
    private final static int QUERY_LIMIT = 20;
    private int page = 0;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "correo";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FloatingActionButton fabBtn;

    private boolean status_progress = false;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(miContexto);
            mProgressDialog.setMessage("Validando documento...!");
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

    public static ListadoListaNegraVehiculosFragment newInstance(String correo, String param2) {
        Log.d(KEY_CICLE, "newInstance");
        ListadoListaNegraVehiculosFragment fragment = new ListadoListaNegraVehiculosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, correo);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListadoListaNegraVehiculosFragment() {
        // Required empty public constructor
        Log.d(KEY_CICLE, "Required empty public constructor");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(KEY_CICLE, "onCreate");
        /*if (getArguments() != null) {
            correo = getArguments().getString("correo");
            //idusuario = getArguments().getString("idusuario");
        }*/

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

        mDatabase = database.getReference("listanegravehiculos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(KEY_CICLE, "onCreateView");

        View v =  inflater.inflate(R.layout.ly_fragments_listanegra_vehiculo, container, false);
        miContexto = container.getContext();
        if(v != null){
            fabBtn = (FloatingActionButton) v.findViewById(R.id.fabBtn);
            fabBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(miContexto, BusquedaVehiculoActivity.class);
                    miContexto.startActivity(intent);
                }
            });
            /****parte de recyclerview******/
            mRecycler = (UltimateRecyclerView) v.findViewById(R.id.ultimate_recycler_view_listanegra_vehiculos);
            mRecycler.setHasFixedSize(false);

            progressBar = (ProgressBar) v.findViewById(R.id.ultimate_progress);

            linearLayoutManager = new LinearLayoutManager(miContexto);
            mRecycler.setLayoutManager(linearLayoutManager);
            mRecycler.setRecylerViewBackgroundColor(Color.parseColor("#CCCCCC"));
            mRecycler.addItemDecoration(new HorizontalDividerItemDecoration.Builder(miContexto).build());
            mRecycler.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            obtenerDatos(10,0,correo,"INI");
                        }
                    }, 1000);
                }
            });

            enableLoadMore();

            mRecycler.setDefaultSwipeToRefreshColorScheme(getResources().getColor(android.R.color.holo_blue_bright),
                    getResources().getColor(android.R.color.holo_green_light),
                    getResources().getColor(android.R.color.holo_orange_light),
                    getResources().getColor(android.R.color.holo_red_light));

            //cargamos estados anteriores del adapter
            //When we go to next fragment and return back here, the adapter is already present and populated.
            //Don't create it again in such cases. Hence the null check.
            if(mAdapter == null){
                //mAdapter = new FlagshipDeviceAdapter(getActivity());
                mAdapter = new ItemListaNegraVehiculosAdapter(new ArrayList<ListaNegraVehiculos>(),miContexto);
            }

            //Use the state retrieved in onCreate and set it on your views etc in onCreateView
            //This method is not called if the device is rotated when your fragment is on the back stack.
            //That's OK since the next time the device is rotated, we save the state we had retrieved in onCreate
            //instead of saving current state. See onSaveInstanceState for more details.
            if(sampleDataSaveState!= null){
                //mAdapter.onRestoreInstanceState(mAdapterSavedState);
                //mAdapter.setSampleData(sampleDataSaveState);
                mAdapter = new ItemListaNegraVehiculosAdapter(sampleDataSaveState,miContexto);
            }
            mRecycler.setAdapter(mAdapter);

        }
        return v;
    }

    private void enableLoadMore() {
        // StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(simpleRecyclerViewAdapter);
        // ultimateRecyclerView.addItemDecoration(headersDecor);
        mRecycler.setLoadMoreView(R.layout.custom_bottom_progressbar);

        mRecycler.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                status_progress = true;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        onLoadmore();
                        status_progress = false;
                    }
                }, 500);
            }
        });
    }

    private void onLoadmore() {
        obtenerDatos(10,mAdapter.getAdapterItemCount(),correo,"FIN");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(KEY_CICLE, "onSaveInstanceState");
        if(mAdapter != null){
            //This case is for when the fragment is at the top of the stack. onCreateView was called and hence there is state to save
            //mAdapterSavedState = mAdapter.onSaveInstanceState();
            sampleDataSaveState = mAdapter.getSampleData();
        }
        //However, remember that this method is called when the device is rotated even if your fragment is on the back stack.
        //In such cases, the onCreateView was not called, hence there is nothing to save.
        //Hence, we just re-save the state that we had retrieved in onCreate. We sort of relay the state from onCreate to onSaveInstanceState.
        outState.putSerializable(KEY_ADAPTER_STATE, (Serializable) sampleDataSaveState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(KEY_CICLE, "onActivityCreated");
        //someVarA = savedInstanceState.getInt("someVarA");
        //someVarB = savedInstanceState.getString("someVarB");
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_ADAPTER_STATE)){
            progressBar.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
            sampleDataSaveState = (List<ListaNegraVehiculos>) savedInstanceState.getSerializable(KEY_ADAPTER_STATE);
            mAdapter.setSampleData(sampleDataSaveState);
            mAdapter.notifyDataSetChanged();
        }else{
            if(mAdapter.getSampleData().size()==0){
                obtenerDatos(10,0,correo,"PRI");
            }
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(KEY_CICLE, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(KEY_CICLE, "onDestroy");
        super.onDestroy();
    }

    /****metodos de carga***/
    private void agregarListaInicio(List<ListaNegraVehiculos> temp){

        int count = 0,i=0;
        count = temp.size();

        for(i=count -1;i>=0;i--){
            if(!verificarDato(temp.get(i))){
                mAdapter.insert(temp.get(i),0);
            }
        }

    }

    private void agregarListaFinal(List<ListaNegraVehiculos> temp){
        for(ListaNegraVehiculos midato : temp){
            if(!verificarDato(midato)){
                mAdapter.insert(midato,mAdapter.getAdapterItemCount());
            }
        }
    }

    private boolean verificarDato(ListaNegraVehiculos midatoB){
        boolean band = false;
        for(ListaNegraVehiculos midato : mAdapter.getSampleData()){
            if(midato.placa.compareTo(midatoB.placa)==0){
                band = true;
                return band;
            }
        }
        return band;
    }

    private void obtenerDatos(final int limit, final int offset,final String correo, final String modocarga) {

        ValueEventListener miValueEventListener =
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        miListaTemp = new ArrayList<ListaNegraVehiculos>();
                        for (DataSnapshot miSnapshot: dataSnapshot.getChildren()) {
                            ListaNegraVehiculos miListaNegraVehiculos = miSnapshot.getValue(ListaNegraVehiculos.class);
                            miListaTemp.add(miListaNegraVehiculos);
                            lastKey = miListaNegraVehiculos.placa;
                            Log.i("ListaNegra", miListaNegraVehiculos.placa);
                        }

                        /////////////////////////////
                        if(modocarga.compareTo("PRI")==0){
                            agregarListaInicio(miListaTemp);
                            //simpleRecyclerViewAdapter.insert(moreNum+++ "  Refresh things", 0);
                            mRecycler.setRefreshing(false);
                            //mRecycler.scrollBy(0, -50);
                            linearLayoutManager.scrollToPosition(0);
//                          ultimateRecyclerView.setAdapter(simpleRecyclerViewAdapter);
                            //deshabilitamos la vista de la carga
                            progressBar.setVisibility(View.GONE);
                            mRecycler.setVisibility(View.VISIBLE);
                            mAdapter.notifyDataSetChanged();
                            Log.d("appprueba", "Llegue Aqui");
                        }else if(modocarga.compareTo("INI")==0){
                            agregarListaInicio(miListaTemp);

                            //simpleRecyclerViewAdapter.insert(moreNum+++ "  Refresh things", 0);
                            mRecycler.setRefreshing(false);
                            //mRecycler.scrollBy(0, -50);
                            linearLayoutManager.scrollToPosition(0);
//                        ultimateRecyclerView.setAdapter(simpleRecyclerViewAdapter);
                            mAdapter.notifyDataSetChanged();

                            Log.d("appprueba","Llegue Aqui");
                        }else if(modocarga.compareTo("FIN")==0){
                            agregarListaFinal(miListaTemp);
                            mAdapter.notifyDataSetChanged();
                            Log.d("appprueba", "Llegue Aqui");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                };

        String ruc = "";
        ruc = GestorDatabase.getInstance(miContexto).obtenerValorUsuario("ruc");

        if(modocarga.compareTo("PRI")==0){
            mDatabase.child(ruc).orderByChild("placa")
                    .addListenerForSingleValueEvent(miValueEventListener);
        }else if(modocarga.compareTo("INI")==0){
            mDatabase.child(ruc).orderByChild("placa")
                    .addListenerForSingleValueEvent(miValueEventListener);
        }else if(modocarga.compareTo("FIN")==0){
            if (lastKey != null) {
                mDatabase.child(ruc).orderByChild("placa")
                        .addListenerForSingleValueEvent(miValueEventListener);
            } else {
                mDatabase.child(ruc).orderByChild("placa")
                        .addListenerForSingleValueEvent(miValueEventListener);
            }
        }
    }
}
