package mx.com.dgom.sercco.android.prevent.act;


import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.jar.*;

import mx.com.dgom.sercco.android.prevent.Constantes;
import mx.com.dgom.sercco.android.prevent.Controller;
import mx.com.dgom.sercco.android.prevent.GUIUtils;
import mx.com.dgom.sercco.android.prevent.NotUserLogeadoException;
import mx.com.dgom.sercco.android.prevent.Session;
import mx.com.dgom.sercco.android.prevent.adapter.LeftDrawerRecicleViewAdapter;
import mx.com.dgom.sercco.android.prevent.to.DelitoTO;
import mx.com.dgom.sercco.android.prevent.to.LeftDrawerItem;
import mx.com.dgom.sercco.android.prevent.to.UsuarioTO;
import mx.com.dgom.util.io.net.NoInternetException;
import mx.com.dgom.util.maps.MapUtils;
import mx.com.dgom.util.str.StringsUtils;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private static final String TAG = "HomeActivity";

    //Resultados de los intents
    private final int REPORTAR_DELITO_INTENT = 10;
    private final int TIME_LINE_INTENT = 20;

    private GoogleMap map;
    private Context ctx;
    private LocationManager locationManager;
    ProgressDialog pd;

    public static DelitoTO delitoMostradoDetalles; //Delito que está mostrando los detalles

    private HashMap<String, String> mHashMap = new HashMap<String, String>();
    private HashMap<Integer, List<Marker>> markersMap = new HashMap<Integer, List<Marker>>();

    private LatLng actualPosition;

    private ImageView imgPro;


    //Interfaz
    private View layoutDetalleDelitos;
    private TextView txtTituliDelito;
    private TextView txtNumDiaDelito;
    private TextView txtMesDelito;
    private TextView txtAnioDelito;
    private TextView txtFechaDelito;
    private TextView txtDistancia;
    private TextView txtDelitoDetalle;
    private TextView txtUsuarioReporta;
    private TextView txtVictimas;
    private TextView txtDelicuentes;
    private TextView txtLikes;
    private TextView txtFotos;
    private ImageView imgBadgeDelito;

    private ProgressBar proLoadingDelitos;
    private LinearLayout layoutLoadingData;
    private int delitosTask = 0;

    private FloatingActionButton fabButtonAgregar;


    //Listado de tiempos
    private int reporteDias = Constantes.REPORTE_1_DIA; //Seleccion por defecto de días, el menu superior podra cambiar los dias totales
    private LinearLayout layoutTimeSelector;
    private TextView txtSeleccion1Dia;
    private TextView txtSeleccion2Dia;
    private TextView txtSeleccion1Semana;
    private TextView txtSeleccion1Mes;
    private TextView txtSeleccion3Meses;


    private Toolbar toolbar;

    //Left Drawer
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    LeftDrawerRecicleViewAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;


    private int cantidadDelitosSeleccionados = 0; //Contador de delitos seleccionados
    private LinearLayout layoutHelpCamara;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa));
        mapFragment.getMapAsync(this);

        //Inicializa el GUI
        initGUI();
        setupTimeButtonsGUI();

        Constantes.initIcos(getResources());

        //Inicializa el leftdrawer
        initLeftDrawer();

        //Inicia el dialogo
        pd = new ProgressDialog(this);
        pd.setTitle(getString(R.string.localizando_usuario));
        pd.setMessage(getString(R.string.espere));
        pd.show();


        //Cuando se activa la pantalla se almacena la referencia
        Session.homeActivity = this;



    }


    @Override
    protected void onPause() {
        super.onPause();

        //Al salir de la pantalla se remueve la referencia
        Session.homeActivity = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Inicializa el GUI
        initGUI();
        setupTimeButtonsGUI();

        //Inicializa el leftdrawer
        initLeftDrawer();

        //habilita los gestos en el mapa
        if (map != null)
            map.getUiSettings().setAllGesturesEnabled(true);

        layoutTimeSelector.setVisibility(View.VISIBLE); //Muestra la seleccion de fechas


        //Actualiza la camara que ofrece ayuda
        updateHelp();
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed Called");

        if (layoutDetalleDelitos.getVisibility() == View.VISIBLE) {
            hideDetallesDelito();
            return;
        }
        super.onBackPressed();
    }


    public void updateHelp() {
        //Verifica si hay delitos seleccionados
        if (cantidadDelitosSeleccionados == 0) {
            //Muestra el monito
            layoutHelpCamara.setVisibility(View.VISIBLE);
        } else {
            //Oculta el monito
            layoutHelpCamara.setVisibility(View.GONE);
        }
    }


    //=================================== LEFT DRAWER ==================================================

    /**
     * Inicializa el drawer izquierdo de la pantalla
     */
    private void initLeftDrawer() {
        //Left drawer
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view); // Assigning the RecyclerView Object to the xml Viewqq
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        //Adapter del menu izquierdo
        initLeftDrawerAdapter();

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.open_left_drawer, R.string.close_left_drawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
                //Actualiza la camara que ofrece ayuda
                updateHelp();
            }
        }; // Drawer Toggle Object Made


        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();
    }


    /**
     * Crea el Adapter del usuario
     *
     * @param usr
     */
    private void createAdapterFromUser(UsuarioTO usr) {
        final Controller controller = new Controller(this);

        Bitmap bm = null;

        if (usr != null && usr.getUriPicImageStr() != null) {
            try {
                bm = controller.getProfilePictureBitmap(usr.getUriPicImageStr());
            } catch (NoInternetException e) {
                e.printStackTrace();
            }
        }

        if (bm != null) { //Crea un adapter con imagen descargada de internet
            mAdapter = new LeftDrawerRecicleViewAdapter(this, usr.getTxtUsuario(), bm);
        } else if (usr != null) { //Crea el adapte con la imagen de default y el nombre del suuario
            mAdapter = new LeftDrawerRecicleViewAdapter(this, usr.getTxtUsuario(), R.drawable.no_user);
        } else { //Crea el adapte con la imagen y el suuario de default
            mAdapter = new LeftDrawerRecicleViewAdapter(this, getString(R.string.no_user), R.drawable.no_user);
        }
    }

    /**
     * Inicializa el Left Drawer
     */
    private void initLeftDrawerAdapter() {
        final Controller controller = new Controller(this);
        UsuarioTO usr = controller.getUsuarioLocal();

        //Crea el adaptador del left drawer
        createAdapterFromUser(usr);

        //Opcion de login
        if (usr == null) {
            mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_LOGIN, -1, false, getString(R.string.login)));
        }

        /**
         * Estructura de los delitos,, que se desplegaran en el menú izquierdo
         */

        //Robo
        mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_CHECK_ROBO, Constantes.TIPO_DELITO_ROBO, true, getString(R.string.delito_robo), controller.isDelitoSelected(Constantes.TIPO_DELITO_ROBO)));
        //Homicidio
        mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_CHECK_HOMICIDIO, Constantes.TIPO_DELITO_HOMICIDIO, true, getString(R.string.delito_homicidio), controller.isDelitoSelected(Constantes.TIPO_DELITO_HOMICIDIO)));
        //Delitos sexuales
        mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_CHECK_SEXUAL, Constantes.TIPO_DELITO_SEXUAL, true, getString(R.string.delito_sexual), controller.isDelitoSelected(Constantes.TIPO_DELITO_SEXUAL)));
        //Enfrentamientos
        mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_CHECK_ENFRENTAMIENTOS, Constantes.TIPO_DELITO_ENFRENTAMIENTOS, true, getString(R.string.delito_enfrentamientos), controller.isDelitoSelected(Constantes.TIPO_DELITO_ENFRENTAMIENTOS)));
        //Extrorcion
        mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_CHECK_EXTORCION, Constantes.TIPO_DELITO_EXTORCION, true, getString(R.string.delito_extorcion), controller.isDelitoSelected(Constantes.TIPO_DELITO_EXTORCION)));
        //Prevencion
        mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_CHECK_PREVENCION, Constantes.TIPO_DELITO_PREVENCION, true, getString(R.string.delito_prevencion), controller.isDelitoSelected(Constantes.TIPO_DELITO_PREVENCION)));
        //Secuestro
        mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_CHECK_SECUESTRO, Constantes.TIPO_DELITO_SECUESTRO, true, getString(R.string.delito_secuestro), controller.isDelitoSelected(Constantes.TIPO_DELITO_SECUESTRO)));
        //Violencia (social se convierte a violencia)
        mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_CHECK_VIOLENCIA, Constantes.TIPO_DELITO_VIOLENCIA, true, getString(R.string.delito_violencia), controller.isDelitoSelected(Constantes.TIPO_DELITO_VIOLENCIA)));
        //Mercado ilegal
        mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_CHECK_MERCADO_NEGRO, Constantes.TIPO_DELITO_MERCADO_NEGRO, true, getString(R.string.delito_mercado_negro), controller.isDelitoSelected(Constantes.TIPO_DELITO_MERCADO_NEGRO)));


        //Opcion de reportar
        //mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_REPORTAR_DELITO, -1, false, getString(R.string.reportar_delito)));
        //Desapariciones
        //mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_CHECK_DESAPARICONES, Constantes.TIPO_DELITO_DESAPARICIONES, true, getString(R.string.delito_desapariciones), controller.isDelitoSelected(Constantes.TIPO_DELITO_DESAPARICIONES)));
        //Cibernetico
        //mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_CHECK_CIBERNETICO, Constantes.TIPO_DELITO_CIBERNETICOS, true, getString(R.string.delito_cibernetico), controller.isDelitoSelected(Constantes.TIPO_DELITO_CIBERNETICOS)));
        //Social
        //mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_CHECK_SOCIAL, Constantes.TIPO_DELITO_SOCIAL, true, getString(R.string.delito_social), controller.isDelitoSelected(Constantes.TIPO_DELITO_SOCIAL)));
        //Movimientos sociales
        //mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_CHECK_MOVIMIENTOS_SOCIALES, Constantes.TIPO_DELITO_MOVIMIENTOS_SOCIALES, true, getString(R.string.delito_movimientos_sociales), controller.isDelitoSelected(Constantes.TIPO_DELITO_MOVIMIENTOS_SOCIALES)));


        //Contabiliza la cantidad de delitos seleccionados al abrir la app
        cantidadDelitosSeleccionados = 0;
        for (int i = 0; i <= Constantes.CANT_MAXIMA_DELITOS; i++) {
            if (controller.isDelitoSelected(i)) {
                cantidadDelitosSeleccionados++;
            }
        }

        //Opcion de logout
        if (usr != null) {
            mAdapter.addItem(new LeftDrawerItem(LeftDrawerItem.MENU_LOGOUT, -1, false, getString(R.string.login_out)));
        }


    }

    /**
     * Metodo para manejar el click al tipo de evento del menu izquierdo
     *
     * @param item
     */
    public void onLeftDrawerClick(LeftDrawerItem item) {

        if (item == null) {
            return;
        }
        Controller controller = new Controller(this);


        //Opcion seleccionada del menu (left drawer)
        switch (item.getType()) {
            case LeftDrawerItem.MENU_LOGIN:
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.flip_middle_in, R.anim.flip_middle_out);
                break;
            case LeftDrawerItem.MENU_LOGOUT:

                controller.logoutUser();
                initLeftDrawer();
                initGUI(); //Actualiza la interfaz gráfica
                setupTimeButtonsGUI(); //Actualiza los botones del tiempo
                seleccionarTiempoReporte(Constantes.REPORTE_1_DIA);//Actualiza el reporte en la pantalla a 1 solo día
                cantidadDelitosSeleccionados = 0; //Indica que ya no hay delitos seleccionados

                Toast.makeText(this, R.string.logout_correcto, Toast.LENGTH_SHORT).show();
                break;
            case LeftDrawerItem.MENU_CHECK_HOMICIDIO:
                delitoClicked(item, Constantes.TIPO_DELITO_HOMICIDIO);
                mAdapter.notifyDataSetChanged();
                break;
            case LeftDrawerItem.MENU_CHECK_DESAPARICONES:
                delitoClicked(item, Constantes.TIPO_DELITO_DESAPARICIONES);
                mAdapter.notifyDataSetChanged();
                break;
            case LeftDrawerItem.MENU_CHECK_SECUESTRO:
                delitoClicked(item, Constantes.TIPO_DELITO_SECUESTRO);
                mAdapter.notifyDataSetChanged();
                break;
            case LeftDrawerItem.MENU_CHECK_MOVIMIENTOS_SOCIALES:
                delitoClicked(item, Constantes.TIPO_DELITO_MOVIMIENTOS_SOCIALES);
                mAdapter.notifyDataSetChanged();
                break;
            case LeftDrawerItem.MENU_CHECK_CIBERNETICO:
                delitoClicked(item, Constantes.TIPO_DELITO_CIBERNETICOS);
                mAdapter.notifyDataSetChanged();
                break;
            case LeftDrawerItem.MENU_CHECK_ENFRENTAMIENTOS:
                delitoClicked(item, Constantes.TIPO_DELITO_ENFRENTAMIENTOS);
                mAdapter.notifyDataSetChanged();
                break;
            case LeftDrawerItem.MENU_CHECK_EXTORCION:
                delitoClicked(item, Constantes.TIPO_DELITO_EXTORCION);
                mAdapter.notifyDataSetChanged();
                break;
            case LeftDrawerItem.MENU_CHECK_MERCADO_NEGRO:
                delitoClicked(item, Constantes.TIPO_DELITO_MERCADO_NEGRO);
                mAdapter.notifyDataSetChanged();
                break;
            case LeftDrawerItem.MENU_CHECK_ROBO:
                delitoClicked(item, Constantes.TIPO_DELITO_ROBO);
                mAdapter.notifyDataSetChanged();
                break;
            case LeftDrawerItem.MENU_CHECK_SEXUAL:
                delitoClicked(item, Constantes.TIPO_DELITO_SEXUAL);
                mAdapter.notifyDataSetChanged();
                break;
            case LeftDrawerItem.MENU_CHECK_VIOLENCIA:
                delitoClicked(item, Constantes.TIPO_DELITO_VIOLENCIA);
                mAdapter.notifyDataSetChanged();
                break;
            case LeftDrawerItem.MENU_CHECK_PREVENCION:
                delitoClicked(item, Constantes.TIPO_DELITO_PREVENCION);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }


    /**
     * Selecciona un nuevo delito para ser desplegado, antes de seleccionar valida que se cuenten con la licencia correcta
     * @param item
     * @param idDelito
     */
    private void delitoClicked(LeftDrawerItem item, int idDelito) {
        Controller controller = new Controller(this);
        item.setSelected(!item.isSelected());


        if (item.isSelected()) { //Si se selecciona el delito
            if (puedeAgregarNuevoDelito2Selection()) { //Valida si se puede agregar o no el delito a la selección
                cantidadDelitosSeleccionados++;
                new LoadPinesTask().execute(idDelito);
            } else {
                item.setSelected(!item.isSelected()); //desmarca el item
                Toast.makeText(this, getString(R.string.error_cantidad_delitos_seleccionados, "" + cantidadDelitosSeleccionados), Toast.LENGTH_LONG).show();
            }

        } else {
            cantidadDelitosSeleccionados--;
            if (cantidadDelitosSeleccionados < 0) {
                cantidadDelitosSeleccionados = 0;
            }
            removeMarkersDelitos(idDelito);
        }

        //Marca como seleccionado/des seleccionado el delito
        controller.setDelitoSelected(idDelito, item.isSelected());
    }


    /**
     * Indica si tiene permiso para agregar más delitos a la seleccion
     * @return
     */
    private boolean puedeAgregarNuevoDelito2Selection() {
        if (cantidadDelitosSeleccionados < Constantes.CANTIDAD_DELITOS_USUARIO_ANONIMO)
            return true;

        Controller c = new Controller(this);
        UsuarioTO usr = c.getUsuarioLocal();

        if (usr != null && usr.getUsuarioPro() == 1) {
            return true;
        }


        if (usr != null && usr.getUsuarioPro() == 0) {
            if (cantidadDelitosSeleccionados < Constantes.CANTIDAD_DELITOS_USUARIO_LOGEADO_NO_PRO)
                return true;
        }

        return false;

    }

    //======================================TERMINA LEFT DRAWER =============================================


    /**
     * Inicializa la vista de la pantalla
     */
    private void initGUI() {
        layoutDetalleDelitos = findViewById(R.id.detalle_delito_include);
        txtTituliDelito = (TextView) findViewById(R.id.txt_tipo_delito);
        txtFechaDelito = (TextView) findViewById(R.id.txt_fecha_delito);
        txtNumDiaDelito = (TextView) findViewById(R.id.txt_num_dia_delito);
        txtMesDelito = (TextView) findViewById(R.id.txt_mes_delito);
        txtAnioDelito = (TextView) findViewById(R.id.txt_anio_delito);
        txtDistancia = (TextView) findViewById(R.id.txt_distancia);
        txtDelitoDetalle = (TextView) findViewById(R.id.txt_delito_detalle);
        txtUsuarioReporta = (TextView) findViewById(R.id.txt_usuario_reporta);
        txtVictimas = (TextView) findViewById(R.id.txt_victimas);
        txtDelicuentes = (TextView) findViewById(R.id.txt_delincuentes);
        txtLikes = (TextView) findViewById(R.id.txt_likes);
        txtFotos = (TextView) findViewById(R.id.txt_fotos);

        imgBadgeDelito = (ImageView) findViewById(R.id.img_badge_delito);

        layoutDetalleDelitos.setVisibility(View.GONE);

        proLoadingDelitos = (ProgressBar) findViewById(R.id.pro_loading_delitos);
        layoutLoadingData = (LinearLayout) findViewById(R.id.layout_loading_data);
        layoutLoadingData.setVisibility(View.GONE);

        layoutTimeSelector = (LinearLayout) findViewById(R.id.layout_time_selector);
        txtSeleccion1Dia = (TextView) findViewById(R.id.txt_seleccion_1_dia);
        txtSeleccion2Dia = (TextView) findViewById(R.id.txt_seleccion_2_dia);
        txtSeleccion1Semana = (TextView) findViewById(R.id.txt_seleccion_1_semana);
        txtSeleccion1Mes = (TextView) findViewById(R.id.txt_seleccion_1_mes);
        txtSeleccion3Meses = (TextView) findViewById(R.id.txt_seleccion_3_meses);

        imgPro = (ImageView) findViewById(R.id.img_pro);
        imgPro.setVisibility(View.GONE);//oculta el botón de pro

        //FAV BUTTON
        fabButtonAgregar = (FloatingActionButton) findViewById(R.id.fab_button_agregar);

        layoutHelpCamara = (LinearLayout) findViewById(R.id.layout_help_camara);
    }


    /**
     * Remueve los marcadores del mapa
     *
     * @param idDelito
     */
    public void removeMarkersDelitos(int idDelito) {
        if (!markersMap.containsKey(idDelito)) {
            Log.d(TAG, "No existe el mapa de delitos " + idDelito);
            return;
        }

        for (Marker m : markersMap.get(idDelito)) {
            m.remove();
        }

        markersMap.get(idDelito).clear();//Borra todos los marcadores de la lista

    }

    /**
     * @param idTipoDelito
     * @param delitos
     */
    public void addDelitos(int idTipoDelito, DelitoTO[] delitos) {
        if (delitos == null) {
            return;
        }

        int count = 0;
        for (DelitoTO d : delitos) {
            addDelitos(idTipoDelito, d);
            count++;
            Log.i(TAG, "Cargando delito (tipo: " + idTipoDelito + ") " + count + " de " + delitos.length);
        }
    }

    /**
     * Metodo que pone el marker de un delito
     *
     * @param idTipoDelito
     * @param delito
     */
    public void addDelitos(int idTipoDelito, DelitoTO delito) {
        //valida que no exista el marker antes de agregarlo
        //TODO  -----


        // create marker
        MarkerOptions markerOption = new MarkerOptions().position(delito.getLatLng());//.title("DELITO");
        // Changing marker icon
        markerOption.icon(Constantes.icosBitmapDescriptors[idTipoDelito - 1]);
        //marker.snippet(d.getId_evento() + "" + d.getId_num_delito());
        // adding marker
        Marker m = map.addMarker(markerOption);
        //Genera la relacion entre los marcadores y el id del delito
        mHashMap.put(m.getId(), "idNumDelito/" + delito.getId_num_delito() + "/idEvento/" + delito.getId_evento());
        if (!markersMap.containsKey(idTipoDelito)) {
            markersMap.put(idTipoDelito, new ArrayList<Marker>());
        }

        //Agrega el marcador a la lista de delitos
        markersMap.get(idTipoDelito).add(m);


    }


    // ACCIONES ------------------------------------


    public void showFotosDelitoAction(View v) {
        showFotosDelito();
    }

    /**
     * Muestra el intent de las fotografías
     */
    private void showFotosDelito() {
        //Si no hay elementos multimedia no hace nada
        if (delitoMostradoDetalles.getMultimedia() == null || delitoMostradoDetalles.getMultimedia().length == 0) {
            return;
        }

        Intent intent = new Intent(HomeActivity.this, PictureSliderActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.flip_middle_in, R.anim.flip_middle_out);
    }

    public void addPointAction(View v) {
        Controller controller = new Controller(this);
        try {
            int res = controller.addPoint(delitoMostradoDetalles);
            //1 guardado ok, 0, no se puede por que es mio, -1 error, -2 ya se le dio like
            switch (res) {
                case 1:
                    int count = Integer.parseInt(txtLikes.getText().toString());
                    txtLikes.setText((++count) + "");
                    Toast.makeText(getBaseContext(), R.string.like_ok, Toast.LENGTH_LONG).show();
                    break;
                case 0:
                    Toast.makeText(getBaseContext(), R.string.error_like_mio, Toast.LENGTH_LONG).show();
                    break;
                case -1:
                    Toast.makeText(getBaseContext(), R.string.error_like_generico, Toast.LENGTH_LONG).show();
                    break;
                case -2:
                    Toast.makeText(getBaseContext(), R.string.error_like_ya_like, Toast.LENGTH_LONG).show();
                    break;
            }


        } catch (NotUserLogeadoException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), R.string.error_usuario_no_logeado, Toast.LENGTH_LONG).show();
        } catch (NoInternetException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), R.string.error_no_internet, Toast.LENGTH_LONG).show();
        }
    }

    public void closeDetailsAction(View v) {
        //Toast.makeText(this, "OnClick", Toast.LENGTH_LONG).show();
        hideDetallesDelito();
    }

    /**
     *
     */
    public void hideDetallesDelito() {

        layoutTimeSelector.setVisibility(View.VISIBLE); //Muestra la seleccion de fechas
        layoutDetalleDelitos.setVisibility(View.GONE);
        fabButtonAgregar.setVisibility(View.VISIBLE); //Muestra el fab icon de agregar

        //Habilita el drag del mapa
        //map.getUiSettings().setScrollGesturesEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.setOnMapClickListener(null); //Quita el listener de tocar el mapa
    }


    /**
     * Despliega la vista del detalle del delito
     * @param delito
     */
    public void showDetallesDelito(DelitoTO delito) {

        fabButtonAgregar.setVisibility(View.GONE); //Esconde el fab icon de agregar
        layoutTimeSelector.setVisibility(View.GONE); //Esconde la seleccion de fechas

        String tipoDelito = Constantes.getDelitoNombre(delito.getId_tipo_delito());
        String tiempoTranscurrido = StringsUtils.timeElapsedFormatDDText(delito.getFch_delito());
        String distancia = StringsUtils.distanceFormat(MapUtils.distance(actualPosition, delito.getLatLng()));

        Date date = new Date(delito.getFch_delito());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(delito.getFch_delito() * 1000);

        int diaMes = cal.get(Calendar.DAY_OF_MONTH);
        int mesNumero = cal.get(Calendar.MONTH);
        int anioNumero = cal.get(Calendar.YEAR);

        Log.d(TAG, "Calendar del delito " + cal.getTime());
        Log.d(TAG, "Fecha del delito " + date);
        Log.d(TAG, "Long " + delito.getFch_delito());

        txtTituliDelito.setText(tipoDelito);
        txtFechaDelito.setText(tiempoTranscurrido);
        txtNumDiaDelito.setText("" + diaMes);
        txtAnioDelito.setText("" + anioNumero);
        txtMesDelito.setText(Constantes.getNombreCortoMes(mesNumero));
        txtDelitoDetalle.setText((delito.getTxt_resumen() + "\n\n" + delito.getTxt_descripcion_lugar()));
        txtDelicuentes.setText("" + delito.getNum_delincuentes());
        txtVictimas.setText("" + delito.getNum_victimas());
        txtLikes.setText("" + delito.getNum_likes());
        txtFotos.setText("" + delito.getMultimedia().length);
        txtDistancia.setText(distancia + " de ti");
        imgBadgeDelito.setImageBitmap(Constantes.getIcoBadgeByDelitoMed(delito.getId_tipo_delito()));


        layoutDetalleDelitos.setVisibility(View.VISIBLE);

        //Deshabilita el drag del mapa
        //map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setAllGesturesEnabled(false);

        //Pone un listener para cuando se toca el mapa se cierre el detalle del delito
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                hideDetallesDelito();
            }
        });

    }


    private void setupTimeButtonsGUI() {
        Controller c = new Controller(this);
        UsuarioTO usr = c.getUsuarioLocal();
        boolean isPro = false;

        //Valida si el usuario es Pro o no
        if (usr != null && usr.getUsuarioPro() == 1) {
            isPro = true;
            imgPro.setVisibility(View.VISIBLE);
        }
        Drawable bgDisabled = getResources().getDrawable(R.drawable.time_button_bg_disabled);

        //Reset de color
        txtSeleccion1Dia.setTextColor(getResources().getColor(R.color.white));
        txtSeleccion2Dia.setTextColor(getResources().getColor(R.color.white));
        txtSeleccion1Semana.setTextColor(getResources().getColor(R.color.white));
        txtSeleccion1Mes.setTextColor(getResources().getColor(R.color.white));
        txtSeleccion3Meses.setTextColor(getResources().getColor(R.color.white));

        //Por ahora todos los usuarios tiene habilitado el pro
        isPro = true; //FIXME


        //Habilita los botones
        if (isPro) {
            txtSeleccion1Semana.setBackgroundResource(0);
            txtSeleccion1Mes.setBackgroundResource(0);
            txtSeleccion3Meses.setBackgroundResource(0);

            txtSeleccion1Semana.setEnabled(true);
            txtSeleccion1Mes.setEnabled(true);
            txtSeleccion3Meses.setEnabled(true);

        } else {
            txtSeleccion1Semana.setTextColor(getResources().getColor(R.color.gray));
            txtSeleccion1Mes.setTextColor(getResources().getColor(R.color.gray));
            txtSeleccion3Meses.setTextColor(getResources().getColor(R.color.gray));

            txtSeleccion1Semana.setEnabled(false);
            txtSeleccion1Mes.setEnabled(false);
            txtSeleccion3Meses.setEnabled(false);
        }

        txtSeleccion1Dia.setBackgroundResource(0);
        txtSeleccion2Dia.setBackgroundResource(0);

    }

    /**
     * Mètodo que selecciona la cantidad de dias que se mostrán en el mapa
     *
     * @param v
     */
    public void seleccionarTiempoReporte(View v) {
        int id = Integer.parseInt("" + v.getTag());
        seleccionarTiempoReporte(id);
    }

    private void seleccionarTiempoReporte(int id) {


        Controller c = new Controller(this);
        UsuarioTO usr = c.getUsuarioLocal();
        boolean isPro = false;

        if (usr != null && usr.getUsuarioPro() == 1) {
            isPro = true;
        }

        setupTimeButtonsGUI();

        //Limpia los delitos en la pantalla
        for (int i = 1; i <= Constantes.CANT_MAXIMA_DELITOS; i++) {
            removeMarkersDelitos(i);
        }


        Drawable bg = getResources().getDrawable(R.drawable.time_button_bg);

        switch (id) {
            case Constantes.REPORTE_1_DIA:
                reporteDias = Constantes.REPORTE_1_DIA;
                txtSeleccion1Dia.setBackground(bg);
                break;
            case Constantes.REPORTE_2_DIAS:
                reporteDias = Constantes.REPORTE_2_DIAS;
                txtSeleccion2Dia.setBackground(bg);
                break;
            case Constantes.REPORTE_1_SEMANA:
                reporteDias = Constantes.REPORTE_1_SEMANA;
                txtSeleccion1Semana.setBackground(bg);
                break;
            case Constantes.REPORTE_1_MES:
                reporteDias = Constantes.REPORTE_1_MES;
                txtSeleccion1Mes.setBackground(bg);
                break;
            case Constantes.REPORTE_3_MESES:
                reporteDias = Constantes.REPORTE_3_MESES;
                txtSeleccion3Meses.setBackground(bg);
                break;
        }

        //Actualiza la pantalla respecto a los pines a mostrar
        loadPinesDelitosSeleccionados();
    }


    /**
     * Metodo para mostrar la actividad de publicar delito
     *
     * @param v
     */
    public void publicarCrimenAction(View v) {
        //TODO REPORTAR EL DELITO EN FB
        //Valida si el usuario está registrado
        if (!new Controller(this).isUsuarioRegistrado()) {
            Toast.makeText(getBaseContext(), R.string.error_usuario_no_logeado_reportar, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(HomeActivity.this, ReportarDelitoActivity.class);
        intent.putExtra("LATITUDE", actualPosition.latitude);
        intent.putExtra("LONGITUDE", actualPosition.longitude);
        startActivityForResult(intent, REPORTAR_DELITO_INTENT);
        overridePendingTransition(R.anim.flip_middle_in, R.anim.flip_middle_out);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Si regresa de reportar delito valida si vienen los datos
        if (requestCode == REPORTAR_DELITO_INTENT) {
            if (resultCode == RESULT_OK) {
                resultadoReportarDelitoIntent(data);
            }
        }

        //Si regresa de reportar delito valida si vienen los datos
        if (requestCode == TIME_LINE_INTENT) {
            if (resultCode == RESULT_OK) {
                resultadoTimeLineIntent(data);
            }
        }
    }


    /**
     * Despliega el detalle del delito cuando viene del timeline
     * @param data
     */
    private void resultadoTimeLineIntent(Intent data) {
        double lat = data.getDoubleExtra("LATITUDE", 0);
        double lon = data.getDoubleExtra("LONGITUDE", 0);
        int idTipoDelito = data.getIntExtra("TIPO_DELITO", 0);
        long idEvento = data.getLongExtra("ID_EVENTO", 0);
        long idnumDelito = data.getLongExtra("ID_NUM_DELITO", 0);

        DelitoTO d = new DelitoTO();

        d.setId_evento(idEvento);
        d.setId_num_delito(idnumDelito);

        d.setNum_latitud(lat);
        d.setNum_longitud(lon);

        //Agrega el marcador del delito, aun ciando estos delitos no se esten mostrando
        addDelitos(idTipoDelito, d);

        //Centra el mapa en el delito seleccionado
        centerMap(d);

        //Muestra el delito
        //showDetallesDelito(d);
        new DelitoDetailTast().execute("idNumDelito/" + d.getId_num_delito() + "/idEvento/" + d.getId_evento());
    }

    /**
     * Metodo que agrega un nuevo pin al mapa en caso de terminar correctamente el intent de agregar delito
     *
     * @param data
     */
    private void resultadoReportarDelitoIntent(Intent data) {
        double lat = data.getDoubleExtra("LATITUDE", 0);
        double lon = data.getDoubleExtra("LONGITUDE", 0);
        int idTipoDelito = data.getIntExtra("TIPO_DELITO", 0);
        long idEvento = data.getLongExtra("ID_EVENTO", 0);
        long idnumDelito = data.getLongExtra("ID_NUM_DELITO", 0);

        Log.d(TAG, "Id tipo delito agregado " + idTipoDelito);

        //Crea el delito con la respuesta
        DelitoTO d = new DelitoTO();

        d.setId_evento(idEvento);
        d.setId_num_delito(idnumDelito);

        d.setNum_latitud(lat);
        d.setNum_longitud(lon);

        //Agrega el marcador del delito, aun cuando estos delitos no se esten mostrando
        addDelitos(idTipoDelito, d);

        //Centra el mapa en el delito agregado
        centerMap(d);
    }

    /**
     * Centra el mapa en un marcador
     *
     * @param d delito
     */
    public void centerMap(DelitoTO d) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(d.getLatLng(), 18);
        map.animateCamera(cameraUpdate);
    }

    //----------- OPCIONES DEL MENU --------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_action_time_line:
                showTimeLineActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Muestra la vental de time line
     */
    private void showTimeLineActivity() {
        Intent intent = new Intent(HomeActivity.this, TimeLineActivity.class);
        startActivityForResult(intent, TIME_LINE_INTENT);
        overridePendingTransition(R.anim.flip_middle_in, R.anim.flip_middle_out);
    }

    //--------------- MAP READY INTERFACE

    private final int PERMISSION_ACCESS_COARSE_LOCATION = 100;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);

            return;

        }

        map.setMyLocationEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                new DelitoDetailTast().execute(mHashMap.get(marker.getId()));

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
                map.animateCamera(cameraUpdate);
                return false;
            }
        });


        //Verifica si debe mostrar un delito

        String delitoId = getIntent().getStringExtra(Constantes.EXTRA_ID_DELITO);

        if(delitoId != null && delitoId.length() > 0){


            new DelitoDetailTast().execute(delitoId);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    onMapReady(this.map);

                } else {

                    Toast.makeText(this, "No se ha habilitado la geolocalización", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    /**
     * Carga los pines seleccionados
     */
    private void loadPinesDelitosSeleccionados() {
        Controller controller = new Controller(this);

        if (controller.isDelitoSelected(Constantes.TIPO_DELITO_MOVIMIENTOS_SOCIALES))
            new LoadPinesTask().execute(Constantes.TIPO_DELITO_MOVIMIENTOS_SOCIALES);

        if (controller.isDelitoSelected(Constantes.TIPO_DELITO_CIBERNETICOS))
            new LoadPinesTask().execute(Constantes.TIPO_DELITO_CIBERNETICOS);

        if (controller.isDelitoSelected(Constantes.TIPO_DELITO_DESAPARICIONES))
            new LoadPinesTask().execute(Constantes.TIPO_DELITO_DESAPARICIONES);

        if (controller.isDelitoSelected(Constantes.TIPO_DELITO_ENFRENTAMIENTOS))
            new LoadPinesTask().execute(Constantes.TIPO_DELITO_ENFRENTAMIENTOS);

        if (controller.isDelitoSelected(Constantes.TIPO_DELITO_EXTORCION))
            new LoadPinesTask().execute(Constantes.TIPO_DELITO_EXTORCION);

        if (controller.isDelitoSelected(Constantes.TIPO_DELITO_HOMICIDIO))
            new LoadPinesTask().execute(Constantes.TIPO_DELITO_HOMICIDIO);

        if (controller.isDelitoSelected(Constantes.TIPO_DELITO_MERCADO_NEGRO))
            new LoadPinesTask().execute(Constantes.TIPO_DELITO_MERCADO_NEGRO);

        if (controller.isDelitoSelected(Constantes.TIPO_DELITO_ROBO))
            new LoadPinesTask().execute(Constantes.TIPO_DELITO_ROBO);

        if (controller.isDelitoSelected(Constantes.TIPO_DELITO_SECUESTRO))
            new LoadPinesTask().execute(Constantes.TIPO_DELITO_SECUESTRO);

        if (controller.isDelitoSelected(Constantes.TIPO_DELITO_SEXUAL))
            new LoadPinesTask().execute(Constantes.TIPO_DELITO_SEXUAL);
    }


    // ------------------- LOCATION LISTENER INTERFACE ---------------------
    @Override
    public void onLocationChanged(Location location) {
        if (pd != null) {
            pd.dismiss();
        }

        LatLng myPos = new LatLng(location.getLatitude(), location.getLongitude());

        actualPosition = myPos; //Posicion actual del usuario

        loadPinesDelitosSeleccionados(); //Una vez que se tiene la localizacion del usuario se cargan los pines

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myPos, 12);
        map.animateCamera(cameraUpdate);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    /**
     * Método que despliega el cuadro de dialogo para ver el delito
     * @param delito
     */
    public void showAlertNewDelito(final DelitoTO delito){

        Log.d(TAG, "Nuevo delito reportado, mostrando dialogo");


        HomeActivity.this.runOnUiThread(new Runnable() {
            public void run() {

                new AlertDialog.Builder(Session.homeActivity)
                        .setTitle("Se ha reportado un nuevo delito")
                        .setMessage("Ver el deito")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {


                            public void onClick(DialogInterface dialog, int whichButton) {

                                showDetallesDelito(delito);
                                delitoMostradoDetalles = delito;
                                centerMap(delito);
                                addDelitos(delito.getId_tipo_delito(), delito);


                                //new DelitoDetailTast().execute(delito.getId_evento())


                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //No hace nada
                            }}).show();
            }
        });


    }


    //------------------------------------ CLASE PARA CARGAR DELITOS PINES -------------------------

    class LoadPinesTask extends AsyncTask<Integer, Integer, DelitoTO[]> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(getBaseContext());
            pd.setTitle("Cargando datos");
            pd.setMessage(getString(R.string.espere_cargando_delitos));
//            pd.show();

            delitosTask++;
            if (delitosTask > 0) {
                layoutLoadingData.setVisibility(View.VISIBLE);
            } else {
                layoutLoadingData.setVisibility(View.GONE);
            }
        }

        @Override
        protected DelitoTO[] doInBackground(Integer... integers) {
            Log.d(TAG, "Cargando pines de " + integers[0]);
            Controller controller = new Controller(getBaseContext());
            DelitoTO[] delitos = new DelitoTO[0];
            try {
                delitos = controller.getDelitosByTipo(integers[0], actualPosition, reporteDias);
            } catch (NoInternetException e) {
                e.printStackTrace();
                GUIUtils.showNoInternetAccessToast(HomeActivity.this);
                return null;
            }
            return delitos;
        }

        @Override
        protected void onPostExecute(DelitoTO[] delitos) {
            super.onPostExecute(delitos);
            if (pd != null) {
                pd.dismiss();
            }

            if (delitos != null && delitos.length > 0) {
                int tipoDelito = delitos[0].getId_tipo_delito();
                addDelitos(tipoDelito, delitos);
            }

            delitosTask--;
            if (delitosTask > 0) {
                layoutLoadingData.setVisibility(View.VISIBLE);
            } else {
                layoutLoadingData.setVisibility(View.GONE);
            }
        }
    }//Cierra clase


    //--------- CLASE PARA CARGAR El DETALLE DEL  DELITOS ----------------

    class DelitoDetailTast extends AsyncTask<String, String, DelitoTO> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //En caso que se esté desplegando un delito y ya haya uno
            //Se oculta el primero en lo que carga el segundo
            hideDetallesDelito();

            delitosTask++;
            if (delitosTask > 0) {
                layoutLoadingData.setVisibility(View.VISIBLE);
            } else {
                layoutLoadingData.setVisibility(View.GONE);
            }
        }

        @Override
        protected DelitoTO doInBackground(String... strings) {
            Log.d(TAG, "Cargando detalles de " + strings[0]);
            Controller controller = new Controller(getBaseContext());
            try {
                DelitoTO delito = controller.getDelitoDetails(strings[0]);
                return delito;
            } catch (NoInternetException e) {
                e.printStackTrace();
                GUIUtils.showNoInternetAccessToast(HomeActivity.this);
                return null;
            }

        }

        @Override
        protected void onPostExecute(DelitoTO delitoTO) {
            super.onPostExecute(delitoTO);
            if (delitoTO != null) {
                showDetallesDelito(delitoTO);
                delitoMostradoDetalles = delitoTO;
                addDelitos(delitoTO.getId_tipo_delito(),delitoTO);
                centerMap(delitoTO);
            }

            delitosTask--;
            if (delitosTask > 0) {
                layoutLoadingData.setVisibility(View.VISIBLE);
            } else {
                layoutLoadingData.setVisibility(View.GONE);
            }
        }
    }
}
