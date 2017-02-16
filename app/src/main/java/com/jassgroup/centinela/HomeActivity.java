package com.jassgroup.centinela;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jassgroup.acceso.SignUpActivity;
import com.jassgroup.acceso.clases.Usuario;
import com.jassgroup.centinela.fragments.ListadoListaNegraFragment;
import com.jassgroup.centinela.fragments.ListadoListaNegraVehiculosFragment;
import com.jassgroup.centinela.fragments.ListadoPersonaFragment;
import com.jassgroup.centinela.fragments.ListadoVehiculoFragment;
import com.jassgroup.centinela.fragments.RegistroVisitaFragment;
import com.jassgroup.centinela.fragments.RegistroVisitaVehiculoFragment;
import com.jassgroup.database.GestorDatabase;
import com.jassgroup.database.ScriptDatabaseUsuario;
import com.jassgroup.master.BaseFirebaseActivity;
import com.jassgroup.profile.EditarProfileActivity;
import com.jassgroup.centinela.fragments.InicioFragment;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

public class HomeActivity extends BaseFirebaseActivity {

    private static final int PROFILE_SETTING = 1;

    //toolbar de la aplicacion
    private Toolbar toolbar;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private Bundle savedInstanceStateApp;

    private static final String KEY_CICLE = "ESTADO_FRAGMENT";

    private Usuario miUsuario;

    //Fragments
    Fragment fInicio;
    Fragment fRegistroVisita;
    Fragment fListaPersona;
    Fragment fListaNegra;
    Fragment fListaVehiculo;
    Fragment fListaNegraVehiculo;
    Fragment fRegistroVisitaVehiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.setTAG("actHome");
        this.setCadenaDialogo("Creando Home");

        ////////////////////////////////////////////
        Log.d(KEY_CICLE, "onCreate-ACT");
        savedInstanceStateApp = savedInstanceState;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            this.setMiTitulo(this.getResources().getString(R.string.app_name));
            toolbar.setTitle(getMiTitulo());
            setSupportActionBar(toolbar);
            /*toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    EditarProfileActivity.this.onBackPressed();
                }
            });*/
        }


        setMiUser(FirebaseAuth.getInstance().getCurrentUser());
        if (this.getMiUser() != null) {

        } else {
            // No user is signed in
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }

        setmDatabase(getDatabase().getReference("usuarios"));

        miUsuario = new Usuario(GestorDatabase.getInstance(getMiContexto()).obtenerValorUsuario(ScriptDatabaseUsuario.Column.APELLIDOS),
                GestorDatabase.getInstance(getMiContexto()).obtenerValorUsuario(ScriptDatabaseUsuario.Column.NOMBRES),
                GestorDatabase.getInstance(getMiContexto()).obtenerValorUsuario(ScriptDatabaseUsuario.Column.CORREO),
                GestorDatabase.getInstance(getMiContexto()).obtenerValorUsuario(ScriptDatabaseUsuario.Column.CLAVE),
                GestorDatabase.getInstance(getMiContexto()).obtenerValorUsuario(ScriptDatabaseUsuario.Column.RUC),
                GestorDatabase.getInstance(getMiContexto()).obtenerValorUsuario(ScriptDatabaseUsuario.Column.TELEFONO),
                GestorDatabase.getInstance(getMiContexto()).obtenerValorUsuario(ScriptDatabaseUsuario.Column.IMAGEN));

        IProfile profile = null;

        if (miUsuario.imagen.compareTo("---") == 0 || miUsuario.imagen.isEmpty()) {
            profile = new ProfileDrawerItem()
                    .withName(miUsuario.nombres + " ," + miUsuario.apellidos)
                    .withEmail(miUsuario.correo)
                    .withIcon(R.drawable.profile5);
        } else {
            // Create a few sample profile
            // NOTE you have to define the loader logic too. See the CustomApplication for more details
            profile = new ProfileDrawerItem()
                    .withName(miUsuario.nombres + " ," + miUsuario.apellidos)
                    .withEmail(miUsuario.correo)
                    .withIcon(Uri.parse(miUsuario.imagen));
        }


        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile,
                        new ProfileSettingDrawerItem()
                                .withName(getResources().getString(R.string.txt_drawer_perfil_config))
                                .withIcon(GoogleMaterial.Icon.gmd_settings)
                                .withIdentifier(800),
                        new ProfileSettingDrawerItem()
                                .withName(getResources().getString(R.string.txt_drawer_perfil_cerrar_sesion))
                                .withIcon(GoogleMaterial.Icon.gmd_exit_to_app)
                                .withIdentifier(900)
                )

                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        if (profile.getIdentifier() == 900) {
                            //cerrar sesion de la app
                            getmAuth().signOut();
                            Toast.makeText(HomeActivity.this, "Cerrando sesion de usuario...!",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeActivity.this, SignUpActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (profile.getIdentifier() == 800) {
                            Toast.makeText(HomeActivity.this, "Administrar Perfil",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeActivity.this, EditarProfileActivity.class);
                            startActivity(intent);
                            //finish();
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withTranslucentStatusBar(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.txt_drawer_home).withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(10).withSelectable(true),
                        new SectionDrawerItem().withName("Visitantes"),
                        new PrimaryDrawerItem().withName(R.string.txt_drawer_registrar).withIcon(GoogleMaterial.Icon.gmd_account_circle).withIdentifier(100).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.txt_drawer_consultar).withIcon(GoogleMaterial.Icon.gmd_search).withIdentifier(200).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.txt_drawer_programar).withIcon(GoogleMaterial.Icon.gmd_access_time).withIdentifier(300).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.txt_drawer_listanegra).withIcon(GoogleMaterial.Icon.gmd_report_problem).withIdentifier(400).withSelectable(true),
                        //new DividerDrawerItem(),
                        new SectionDrawerItem().withName("Vehículos"),
                        new PrimaryDrawerItem().withName(R.string.txt_drawer_registrar).withIcon(GoogleMaterial.Icon.gmd_directions_car).withIdentifier(500).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.txt_drawer_consultar).withIcon(GoogleMaterial.Icon.gmd_search).withIdentifier(600).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.txt_drawer_listanegra).withIcon(GoogleMaterial.Icon.gmd_report_problem).withIdentifier(700).withSelectable(true),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.txt_drawer_config).withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(1000).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.txt_drawer_ayuda).withIcon(GoogleMaterial.Icon.gmd_help).withIdentifier(1100).withSelectable(false)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem
                        if (drawerItem != null && drawerItem instanceof Nameable) {

                            if (drawerItem != null) {
                                Intent intent = null;
                                if (drawerItem.getIdentifier() == 10) {
                                    getSupportActionBar().setTitle(R.string.txt_drawer_home);
                                    if (savedInstanceStateApp == null) {
                                        //primera vez que carga la activity
                                        fInicio = InicioFragment.newInstance("valor1", "valor2");
                                        getSupportFragmentManager().
                                                beginTransaction().
                                                replace(R.id.fragment_container, fInicio, "TAGINICIO").
                                                commit();
                                        Log.d(KEY_CICLE, "new FragmentInicio - ACT");
                                    } else {
                                        //ya cargo anteriormente la activity, existe un estado
                                        fInicio = (InicioFragment) getSupportFragmentManager().findFragmentByTag("TAGINICIO");

                                        if (fInicio == null) {
                                            fInicio = InicioFragment.newInstance("valor1", "valor2");
                                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fInicio, "TAGINICIO").commit();
                                            Log.d(KEY_CICLE, "new FragmentInicio - CON ESTADO NO NULO - LOCAL - ACT");
                                        }
                                    }
                                } else if (drawerItem.getIdentifier() == 100) {
                                    getSupportActionBar().setTitle(R.string.txt_drawer_registrar);
                                    if (savedInstanceStateApp == null) {
                                        //primera vez que carga la activity
                                        fRegistroVisita = RegistroVisitaFragment.newInstance("valor1", "valor2");
                                        getSupportFragmentManager().
                                                beginTransaction().
                                                replace(R.id.fragment_container, fRegistroVisita, "TAGREGVISITA").
                                                commit();
                                        Log.d(KEY_CICLE, "new FragmentRegistroVisita- ACT");
                                    } else {
                                        //ya cargo anteriormente la activity, existe un estado
                                        fRegistroVisita = (RegistroVisitaFragment) getSupportFragmentManager().findFragmentByTag("TAGREGVISITA");

                                        if (fRegistroVisita == null) {
                                            fRegistroVisita = RegistroVisitaFragment.newInstance("valor1", "valor2");
                                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fRegistroVisita, "TAGREGVISITA").commit();
                                            Log.d(KEY_CICLE, "new FragmentRegistroVisita - CON ESTADO NO NULO - LOCAL - ACT");
                                        }
                                    }

                                } else if (drawerItem.getIdentifier() == 200) {
                                    getSupportActionBar().setTitle(R.string.txt_drawer_consultar);
                                    if (savedInstanceStateApp == null) {
                                        //primera vez que carga la activity
                                        fListaPersona = ListadoPersonaFragment.newInstance("valor1", "valor2");
                                        getSupportFragmentManager().
                                                beginTransaction().
                                                replace(R.id.fragment_container, fListaPersona, "TAGREGVISITA").
                                                commit();
                                        Log.d(KEY_CICLE, "new FragmentRegistroVisita- ACT");
                                    } else {
                                        //ya cargo anteriormente la activity, existe un estado
                                        fListaPersona = (ListadoPersonaFragment) getSupportFragmentManager().findFragmentByTag("TAGLISTAPER");

                                        if (fListaPersona == null) {
                                            fListaPersona = ListadoPersonaFragment.newInstance("valor1", "valor2");
                                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fListaPersona, "TAGLISTAPER").commit();
                                            Log.d(KEY_CICLE, "new ListadoPersonaFragment - CON ESTADO NO NULO - LOCAL - ACT");
                                        }
                                    }

                                } else if (drawerItem.getIdentifier() == 400) {
                                    getSupportActionBar().setTitle(R.string.txt_drawer_listanegra);
                                    if (savedInstanceStateApp == null) {
                                        //primera vez que carga la activity
                                        fListaNegra = ListadoListaNegraFragment.newInstance("valor1", "valor2");
                                        getSupportFragmentManager().
                                                beginTransaction().
                                                replace(R.id.fragment_container, fListaNegra, "TAGLISTANEGRA").
                                                commit();
                                        Log.d(KEY_CICLE, "new FragmentListaNegra- ACT");
                                    } else {
                                        //ya cargo anteriormente la activity, existe un estado
                                        fListaNegra = (ListadoListaNegraFragment) getSupportFragmentManager().findFragmentByTag("TAGLISTANEGRA");

                                        if (fListaNegra == null) {
                                            fListaNegra = ListadoListaNegraFragment.newInstance("valor1", "valor2");
                                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fListaNegra, "TAGLISTANEGRA").commit();
                                            Log.d(KEY_CICLE, "new ListadoNegra - CON ESTADO NO NULO - LOCAL - ACT");
                                        }
                                    }

                                } else if (drawerItem.getIdentifier() == 600) {
                                    getSupportActionBar().setTitle(R.string.txt_drawer_vehiculos);
                                    if (savedInstanceStateApp == null) {
                                        //primera vez que carga la activity
                                        fListaVehiculo = ListadoVehiculoFragment.newInstance("valor1", "valor2");
                                        getSupportFragmentManager().
                                                beginTransaction().
                                                replace(R.id.fragment_container, fListaVehiculo, "TAGLISTAVEHÍCULOS").
                                                commit();
                                        Log.d(KEY_CICLE, "new FragmentListaVehículos- ACT");
                                    } else {
                                        //ya cargo anteriormente la activity, existe un estado
                                        fListaVehiculo = (ListadoVehiculoFragment) getSupportFragmentManager().findFragmentByTag("TAGLISTAVEHÍCULOS");

                                        if (fListaVehiculo == null) {
                                            fListaVehiculo = ListadoVehiculoFragment.newInstance("valor1", "valor2");
                                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fListaVehiculo, "TAGLISTANEGRA").commit();
                                            Log.d(KEY_CICLE, "new ListadoVehículo - CON ESTADO NO NULO - LOCAL - ACT");
                                        }
                                    }

                                } else if (drawerItem.getIdentifier() == 700) {
                                    getSupportActionBar().setTitle(R.string.txt_drawer_vehiculosListaNegra);
                                    if (savedInstanceStateApp == null) {
                                        //primera vez que carga la activity
                                        fListaNegraVehiculo = ListadoListaNegraVehiculosFragment.newInstance("valor1", "valor2");
                                        getSupportFragmentManager().
                                                beginTransaction().
                                                replace(R.id.fragment_container, fListaNegraVehiculo, "TAGLISTAVEHÍCULOS").
                                                commit();
                                        Log.d(KEY_CICLE, "new FragmentListaNegraVehículos- ACT");
                                    } else {
                                        //ya cargo anteriormente la activity, existe un estado
                                        fListaNegraVehiculo = (ListadoListaNegraVehiculosFragment) getSupportFragmentManager().findFragmentByTag("TAGLISTANEGRAVEHÍCULOS");

                                        if (fListaNegraVehiculo == null) {
                                            fListaNegraVehiculo = ListadoListaNegraVehiculosFragment.newInstance("valor1", "valor2");
                                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fListaNegraVehiculo, "TAGLISTANEGRAVEHICULOS").commit();
                                            Log.d(KEY_CICLE, "new ListadoVehículoListaNegra - CON ESTADO NO NULO - LOCAL - ACT");
                                        }
                                    }

                                }else if (drawerItem.getIdentifier() == 500) {
                                    getSupportActionBar().setTitle(R.string.txt_drawer_registroVehiculos);
                                    if (savedInstanceStateApp == null) {
                                        //primera vez que carga la activity
                                        fRegistroVisitaVehiculo = RegistroVisitaVehiculoFragment.newInstance("valor1", "valor2");
                                        getSupportFragmentManager().
                                                beginTransaction().
                                                replace(R.id.fragment_container, fRegistroVisitaVehiculo, "TAGLISTAVEHÍCULOS").
                                                commit();
                                        Log.d(KEY_CICLE, "new FragmentListaNegraVehículos- ACT");
                                    } else {
                                        //ya cargo anteriormente la activity, existe un estado
                                        fRegistroVisitaVehiculo = (RegistroVisitaVehiculoFragment) getSupportFragmentManager().findFragmentByTag("TAGLISTANEGRAVEHÍCULOS");

                                        if (fRegistroVisitaVehiculo == null) {
                                            fRegistroVisitaVehiculo = RegistroVisitaVehiculoFragment.newInstance("valor1", "valor2");
                                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fRegistroVisitaVehiculo, "TAGLISTANEGRAVEHICULOS").commit();
                                            Log.d(KEY_CICLE, "new ListadoVehículoListaNegra - CON ESTADO NO NULO - LOCAL - ACT");
                                        }
                                    }

                                }


                                if (intent != null) {

                                }
                            }
                        }


                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withFireOnInitialOnClick(true)
                .build();

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(10, false);

            //set the active profile
            headerResult.setActiveProfile(profile);
        }

    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
