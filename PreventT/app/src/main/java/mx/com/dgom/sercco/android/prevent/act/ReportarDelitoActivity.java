package mx.com.dgom.sercco.android.prevent.act;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import mx.com.dgom.sercco.android.prevent.Constantes;
import mx.com.dgom.sercco.android.prevent.Controller;
import mx.com.dgom.sercco.android.prevent.GUIUtils;
import mx.com.dgom.sercco.android.prevent.to.DelitoTO;
import mx.com.dgom.sercco.android.prevent.to.MultimediaTO;
import mx.com.dgom.util.io.images.BitmapUtils;
import mx.com.dgom.util.io.net.NoInternetException;
import mx.com.dgom.util.maps.MapUtils;

public class ReportarDelitoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "ReportarDelitoActivity";

    private static final int ESTADO_SELECCIONAR_MAPA = 0;
    private static final int ESTADO_SELECCIONAR_DELITO = 1;
    private static final int ESTADO_ENVIAR_DELITO_INCOMPLETO = 2;
    private static final int ESTADO_SELECCIONAR_SUB_DELITO = 3;
    private static final int ESTADO_SELECCIONAR_DETALLES = 4;
    private static final int ESTADO_ENVIAR_DELITO = 5;

    private DelitoTO delitoReporte = new DelitoTO(); //Variable para almacenar los valores del delito

    private GoogleMap map;
    private Toolbar toolbar;
    private LatLng userLocalization;
    private LatLng centerDelito;
    private Marker delitoMarker;

    LinearLayout layoutSeleccioneDelito;
    LinearLayout layoutEnviaDelitoIncompleto;
    LinearLayout layoutSeleccioneSubDelito;
    LinearLayout layoutDetalleDelito;
    //LinearLayout layoutImgGalery;
    LinearLayout layoutHelpCamaraArrastraMapa;

    FloatingActionButton fabButton;
    FloatingActionButton fabButtonReportar;

    ImageView icoTarget;
    TextView txtTitleWizard;


    private TableLayout tblSubDelitos;


    TextView txtAddVictimas;
    TextView txtAddDelincuentes;
    TextView txtFechaDelito;

    TextView txtResumenDelito;

    private int estadoWizard = 0;

    private DatePickerDialog datePickerDialog;
    private Calendar newCalendar = Calendar.getInstance();
    private long fechaDelito = System.currentTimeMillis();

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar_delito);

        ctx = this;

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa));
        mapFragment.getMapAsync(this);

        fabButton = (FloatingActionButton) findViewById(R.id.fab_button_map);
        icoTarget = (ImageView) findViewById(R.id.img_ico_target);
        txtTitleWizard = (TextView) findViewById(R.id.txt_title_wizard);

        layoutSeleccioneDelito = (LinearLayout) findViewById(R.id.layout_seleccion_delito);
        layoutEnviaDelitoIncompleto = (LinearLayout) findViewById(R.id.layout_envia_delito_incompleto);
        layoutSeleccioneSubDelito = (LinearLayout) findViewById(R.id.layout_seleccion_sub_delito);
        layoutDetalleDelito = (LinearLayout) findViewById(R.id.layout_detalle_delito);
        layoutHelpCamaraArrastraMapa = (LinearLayout) findViewById(R.id.layout_help_camara_arrastra_mapa);

        tblSubDelitos = (TableLayout) findViewById(R.id.tbl_sub_delitos);

        txtResumenDelito = (TextView) findViewById(R.id.txt_resume_delito);
        txtAddVictimas = (TextView) findViewById(R.id.txt_add_vicimas);
        txtAddDelincuentes = (TextView) findViewById(R.id.txt_add_delincuentes);
        txtFechaDelito = (TextView) findViewById(R.id.txt_fecha_delito);
        //layoutImgGalery = (LinearLayout) findViewById(R.id.layout_img_galery);//Galeria de imagenes
        fabButtonReportar = (FloatingActionButton) findViewById(R.id.fab_button_reportar);

        //Inicializa la vista con la fecha de hoy
        txtFechaDelito.setText(newCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (newCalendar.get(Calendar.MONTH) + 1) + "/" + newCalendar.get(Calendar.YEAR));


        double lat = getIntent().getDoubleExtra("LATITUDE", 0);
        double lon = getIntent().getDoubleExtra("LONGITUDE", 0);

        userLocalization = new LatLng(lat, lon);

        updateGUIWizard();
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed Called");

        if(estadoWizard == ESTADO_SELECCIONAR_MAPA) { //Termina la actividad
            super.onBackPressed();  // optional depending on your needs
            if(PictureGalleryActivity.adapter != null) {
                PictureGalleryActivity.adapter.clearData(); //Limpia las fotos si el usuario cancela la publicacion
            }
            return;
        }

        regresarPasoPrevio(); //Regresa en 1 paso el wizard
    }



    public void showDatePickerAction(View v) {
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fechaDelito = newCalendar.getTimeInMillis();
                txtFechaDelito.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        //La fecha máxima es hoy
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        //La fecha minima es el hoy menos 10 días
        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 10 * 24 * 60 * 60 * 1000);
        datePickerDialog.show();
    }

    /**
     * Paso siguiente del wizard
     *
     * @param v
     */
    public void nextAction(View v) {
        estadoWizard++;
        updateGUIWizard();

        //Cambia del mapa a seleccionar delito
        if (estadoWizard == ESTADO_SELECCIONAR_DELITO) {
            centerDelito = map.getCameraPosition().target;
            if (delitoMarker != null) {
                delitoMarker.remove();
            }

            MarkerOptions markerOption = new MarkerOptions().position(centerDelito);
            // Changing marker icon
            BitmapDescriptor bm = BitmapDescriptorFactory.fromResource(R.drawable.icon_pin_new_crime);
            markerOption.icon(bm);
            delitoMarker = map.addMarker(markerOption);
        }

        if (estadoWizard == ESTADO_ENVIAR_DELITO) { //Terminado
            publicarDelito();
        }
    }

    /**
     * Paso anterior del wizard
     *
     * @param v
     */
    public void prevAction(View v) {
        regresarPasoPrevio();
    }

    private void regresarPasoPrevio(){
        estadoWizard--;
        updateGUIWizard();
    }


    /**
     * Método que reporta un delito
     */
    private void publicarDelito() {
        if (txtResumenDelito.getText().length() == 0) {
            Toast.makeText(getBaseContext(), R.string.error_publicar_falta_texto, Toast.LENGTH_SHORT).show();
            estadoWizard--;
            updateGUIWizard();
            return;
        }

        delitoReporte.setTxt_resumen(txtResumenDelito.getText().toString());
        delitoReporte.setFch_delito(fechaDelito);
        new PublicarDelitoTask().execute();
        updateGUIWizard();
    }



    public void publicarDelitoIncompleto(View v) {
        delitoReporte.setTxt_resumen("");
        delitoReporte.setFch_delito(fechaDelito);
        delitoReporte.setId_tipo_sub_delito(1);

        Log.d(TAG,"Latitude " + delitoReporte.getLatLng().latitude + " Long: " + delitoReporte.getLatLng().longitude);

        new PublicarDelitoTask().execute();
        updateGUIWizard();
    }


    /**
     * Método que selecciona el delito
     *
     * @param v
     */
    public void delitoSeleccionadoAction(View v) {

        String tag = (String) v.getTag();


        switch (tag) {
            case "homicidio":
                delitoReporte.setId_tipo_delito(Constantes.TIPO_DELITO_HOMICIDIO);
                break;
            case "cibernetico":
                delitoReporte.setId_tipo_delito(Constantes.TIPO_DELITO_CIBERNETICOS);
                break;
            case "extorcion":
                delitoReporte.setId_tipo_delito(Constantes.TIPO_DELITO_EXTORCION);
                break;
            case "desapariciones":
                delitoReporte.setId_tipo_delito(Constantes.TIPO_DELITO_DESAPARICIONES);
                break;
            case "robo":
                delitoReporte.setId_tipo_delito(Constantes.TIPO_DELITO_ROBO);
                break;
            case "enfrentamientos":
                delitoReporte.setId_tipo_delito(Constantes.TIPO_DELITO_ENFRENTAMIENTOS);
                break;
            case "sexual":
                delitoReporte.setId_tipo_delito(Constantes.TIPO_DELITO_SEXUAL);
                break;
            case "mercado_negro":
                delitoReporte.setId_tipo_delito(Constantes.TIPO_DELITO_MERCADO_NEGRO);
                break;
            case "secuestro":
                delitoReporte.setId_tipo_delito(Constantes.TIPO_DELITO_SECUESTRO);
                break;
            case "movimientos_sociales":
                delitoReporte.setId_tipo_delito(Constantes.TIPO_DELITO_MOVIMIENTOS_SOCIALES);
                break;
            case "violencia":
                delitoReporte.setId_tipo_delito(Constantes.TIPO_DELITO_VIOLENCIA);
                break;
            case "prevencion":
                delitoReporte.setId_tipo_delito(Constantes.TIPO_DELITO_PREVENCION);
                break;

        }


        avanzarWizard();
    }

    /**
     * Método que selecciona el sub delito
     *
     * @param v
     */
    public void subDelitoSeleccionadoAction(View v) {
        int idSubTipoDelito = (Integer)v.getTag();
        delitoReporte.setId_tipo_sub_delito(idSubTipoDelito);
        avanzarWizard();
    }

    private void avanzarWizard(){
        estadoWizard++;
        updateGUIWizard();
    }


    public void addVictimaAction(View v) {
        delitoReporte.setNum_victimas(delitoReporte.getNum_victimas() + 1);
        txtAddVictimas.setText("" + delitoReporte.getNum_victimas());
    }

    public void minVictimaAction(View v) {
        delitoReporte.setNum_victimas(delitoReporte.getNum_victimas() - 1);

        txtAddVictimas.setText("" + delitoReporte.getNum_victimas());
    }

    public void addDelincuenteAction(View v) {
        delitoReporte.setNum_delincuentes(delitoReporte.getNum_delincuentes() + 1);
        txtAddDelincuentes.setText("" + delitoReporte.getNum_delincuentes());
    }

    public void minDelincuenteAction(View v) {
        delitoReporte.setNum_delincuentes(delitoReporte.getNum_delincuentes() - 1);

        txtAddDelincuentes.setText("" + delitoReporte.getNum_delincuentes());
    }


    private final int SELECT_PHOTO = 1;

    public void addPictureAction(View v) {
        Intent intent = new Intent(ReportarDelitoActivity.this,PictureGalleryActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.flip_middle_in, R.anim.flip_middle_out);

         /*
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        */
    }


    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();


                        Bitmap selectedImage = BitmapUtils.getReducedBitmap(imageUri, getBaseContext(),400);
                        String b64 = BitmapUtils.loadBitmapB64(imageUri, getBaseContext(), 600);
                        Log.d(TAG, "Iage size: " + b64.length());

                        //delitoReporte.addImage(b64);

                        ImageView iv = new ImageView(getBaseContext());
                        iv.setImageBitmap(selectedImage);

                        //Detector de gestos
                        final GestureDetector gdt = new GestureDetector(new GestureListener(iv));

                        iv.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                gdt.onTouchEvent(motionEvent);
                                return true;
                            }
                        });


                        int paddingPixel = 5;
                        float density = getApplicationContext().getResources().getDisplayMetrics().density;
                        int paddingDp = (int) (paddingPixel * density);

                        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250,250);
                        iv.setLayoutParams(params);

                        iv.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);

                        //Agrega la foto a la galeria
                        layoutImgGalery.addView(iv);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    */


    /**
     * Método que controla la vista del reporte de delitos
     */
    private void updateGUIWizard() {
        layoutHelpCamaraArrastraMapa.setVisibility(View.GONE);
        layoutSeleccioneDelito.setVisibility(View.GONE);
        layoutEnviaDelitoIncompleto.setVisibility(View.GONE);
        layoutSeleccioneSubDelito.setVisibility(View.GONE);
        layoutDetalleDelito.setVisibility(View.GONE);
        fabButton.setVisibility(View.GONE);
        fabButtonReportar.setVisibility(View.GONE);
        icoTarget.setVisibility(View.GONE);
        txtTitleWizard.setVisibility(View.GONE);

        //Deshbilita el movimiento del mapa
        if(map != null) {
            map.getUiSettings().setScrollGesturesEnabled(false);
        }


        switch (estadoWizard) {
            case ESTADO_SELECCIONAR_MAPA:
                txtTitleWizard.setVisibility(View.VISIBLE);
                fabButton.setVisibility(View.VISIBLE);
                icoTarget.setVisibility(View.VISIBLE);
                layoutHelpCamaraArrastraMapa.setVisibility(View.VISIBLE);
                //hbilita el movimiento del mapa
                if(map != null) {
                    map.getUiSettings().setScrollGesturesEnabled(true);
                }
                break;
            case ESTADO_SELECCIONAR_DELITO:
                layoutSeleccioneDelito.setVisibility(View.VISIBLE);
                break;
            case ESTADO_ENVIAR_DELITO_INCOMPLETO:
                layoutEnviaDelitoIncompleto.setVisibility(View.VISIBLE);
                break;
            case ESTADO_SELECCIONAR_SUB_DELITO:
                poblarSubDelitos();
                layoutSeleccioneSubDelito.setVisibility(View.VISIBLE);
                break;
            case ESTADO_SELECCIONAR_DETALLES:
                layoutDetalleDelito.setVisibility(View.VISIBLE);
                fabButtonReportar.setVisibility(View.VISIBLE);
                break;
        }
    }




    /**
     * Metodo que crea la tabla de subdelitos
     */
    private void poblarSubDelitos() {
        int idTipoDelito = delitoReporte.getId_tipo_delito();
        String[] subDelitos = Constantes.getSubDelitosById(idTipoDelito);

        //No hay subdelitos
        if(subDelitos.length == 0){
            avanzarWizard();
            return;
        }


        TableRow rowIco = null;
        TableRow rowText = null;
        TextView tv;

        int paddingPixel = 20;
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        int paddingDp = (int) (paddingPixel * density);

        Bitmap icoDelito = Constantes.getIcoBadgeByDelitoLarge(idTipoDelito);

        //Quita todos los elementos de la tabla
        tblSubDelitos.removeAllViews();

        for (int i = 0; i < subDelitos.length ; i++) {
            if (i == 0 || i % 2 == 0) { //2 columnas de subdelitos
                rowIco = new TableRow(getBaseContext());

                rowText = new TableRow(getBaseContext());

                tblSubDelitos.addView(rowIco);
                tblSubDelitos.addView(rowText);
            }

            ImageView iv = new ImageView(getBaseContext());
            iv.setImageBitmap(icoDelito);
            iv.setPadding(paddingDp, paddingDp, paddingDp, 0);
            iv.setTag(i + 1);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subDelitoSeleccionadoAction(view);
                }
            });

            tv = new TextView(getBaseContext());
            tv.setTextColor(getResources().getColor(R.color.white));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.ps_16));
            tv.setGravity(Gravity.CENTER);
            tv.setText(subDelitos[i]);

            rowIco.addView(iv);
            rowText.addView(tv);
        }
    }

    // ----------------- INTERFACE DE MAPAS ------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLocalization, 14);
        map.animateCamera(cameraUpdate);
    }


    //---------------------------------- CLASE PARA PUBLICAR UN DELITO ------------------------

    /**
     * Clase que reporta el delito en backTread
     */
    class PublicarDelitoTask extends AsyncTask<Object, Object, DelitoTO> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ReportarDelitoActivity.this);
            pd.setTitle(getString(R.string.please_wait));
            pd.setMessage(getString(R.string.reporta_reportando_espere));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected DelitoTO doInBackground(Object[] objects) {
            Controller controller = new Controller(ctx);

            List<Address> addressList = MapUtils.getAddressList(ctx, centerDelito, 1);

            if (addressList.size() > 0) {
                Address address = addressList.get(0);
                delitoReporte.getDireccion().setAddress(address);
            }

            try {
                List <MultimediaTO> data = null;
                if (PictureGalleryActivity.adapter != null){
                    data = PictureGalleryActivity.adapter.getData();
                }
                DelitoTO res = controller.publicarDelito(delitoReporte, data, true);//FIXME tomar preguntar si es anonimo el reporte
                if(PictureGalleryActivity.adapter != null) {
                    PictureGalleryActivity.adapter.clearData();
                }
                return res;
            } catch (NoInternetException e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(DelitoTO res) {
            super.onPostExecute(res);
            if (pd != null) {
                pd.dismiss();
            }
            if (res == null) {
                GUIUtils.showNoInternetAccessToast(ReportarDelitoActivity.this);
            }

            if (res != null) {
                Toast.makeText(ctx, getString(R.string.reporta_wizard_ok), Toast.LENGTH_LONG).show();
                overridePendingTransition(R.anim.flip_middle_in, R.anim.flip_middle_out);
                Intent resultData = new Intent();
                resultData.putExtra("LATITUDE", res.getNum_latitud());
                resultData.putExtra("LONGITUDE", res.getNum_longitud());
                resultData.putExtra("TIPO_DELITO", res.getId_tipo_delito());
                resultData.putExtra("ID_EVENTO", res.getId_evento());
                resultData.putExtra("ID_NUM_DELITO", res.getId_num_delito());
                setResult(Activity.RESULT_OK, resultData);
                finish();
                overridePendingTransition(R.anim.flip_middle_in, R.anim.flip_middle_out);

            } else {
                Toast.makeText(ctx, getString(R.string.reporta_wizard_fail), Toast.LENGTH_LONG).show();
                estadoWizard = ESTADO_SELECCIONAR_DETALLES;//Regresa al estado previo a publicar
                updateGUIWizard();
            }
        }
    }

}
