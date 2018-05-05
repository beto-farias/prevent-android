package mx.com.dgom.sercco.android.prevent;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import mx.com.dgom.sercco.android.prevent.act.R;

/**
 * Created by beto on 10/12/15.
 */
public class Constantes {


    public static final int REPORTE_1_DIA = 1;
    public static final int REPORTE_2_DIAS = 2;
    public static final int REPORTE_1_SEMANA = 3;
    public static final int REPORTE_1_MES = 4;
    public static final int REPORTE_3_MESES = 5;

    //Tipos multimedia
    public static final int     TIPO_MULTIMEDIA_FOTO = 1;
    public static final int     TIPO_MULTIMEDIA_VIDEO = 2;
    public static final int     TIPO_MULTIMEDIA_AUDIO = 3;

    public static final String VIDEO_URL = "VIDEO_URL";

    //CANTIDAD DE DELITOS MAXIMOS DEL SISTEMA
    public static final int CANT_MAXIMA_DELITOS = 12;

    //PUSH NOTIFICATIONS

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";


    //Por ahora todos los niveles pueden ver todos los delitos
    public static final int CANTIDAD_DELITOS_USUARIO_ANONIMO = CANT_MAXIMA_DELITOS;//3;
    public static final int CANTIDAD_DELITOS_USUARIO_LOGEADO_NO_PRO = CANT_MAXIMA_DELITOS;//5;
    public static final int CANTIDAD_DELITOS_USUARIO_LOGEADO_PRO = CANT_MAXIMA_DELITOS;


    public static final BitmapDescriptor icosBitmapDescriptors[] = new BitmapDescriptor[CANT_MAXIMA_DELITOS];
    public static final Bitmap[] markersBitmap = new Bitmap[CANT_MAXIMA_DELITOS];
    public static final Bitmap[] icosBadgesBitmapSm = new Bitmap[CANT_MAXIMA_DELITOS];
    public static final Bitmap[] icosBadgesBitmapMed = new Bitmap[CANT_MAXIMA_DELITOS];
    public static final Bitmap[] icosBadgesBitmapLarge = new Bitmap[CANT_MAXIMA_DELITOS];

    //Varialbes para las preferencias de usuario
    public static final String PREFS_NAME = "prevent_t_preferences";
    public static final String PREFS_USER_NAME = "PREFS_USER_NAME";
    public static final String PREFS_USER_ID = "PREFS_USER_ID";
    public static final String PREFS_USER_PIC_URL = "PREFS_USER_PIC_URL";
    public static final String PREFS_TIPO_LOGIN = "PREFS_TIPO_LOGIN";
    public static final String PREFS_USER_FB_ID = "PREFS_USER_FB_ID";
    public static final String PREF_USER_PRO = "PREF_USER_PRO";
    public static final String PREF_SHOW_INTRO = "PREF_SHOW_INTRO";



    //Variables de tipos de delitos que están marcados como seleccionados
    public static final String PREFS_TIPO_DELITO_ = "PREFS_TIPO_DELITO_";


    //Tipos de autentificacion del sistema
    public static final int LOGIN_SIN_LOGIN = 0;
    public static final int LOGIN_MAIL = 1;
    public static final int LOGIN_FB = 2;

    public static final int TIPO_DELITO_SECUESTRO = 1;
    public static final int TIPO_DELITO_HOMICIDIO = 2;
    public static final int TIPO_DELITO_DESAPARICIONES = 3;
    public static final int TIPO_DELITO_ROBO = 4;
    public static final int TIPO_DELITO_SEXUAL = 5;
    public static final int TIPO_DELITO_EXTORCION = 6;
    public static final int TIPO_DELITO_MERCADO_NEGRO = 7;
    public static final int TIPO_DELITO_ENFRENTAMIENTOS = 8;
    public static final int TIPO_DELITO_CIBERNETICOS = 9;
    public static final int TIPO_DELITO_MOVIMIENTOS_SOCIALES = 10;
    public static final int TIPO_DELITO_VIOLENCIA = 11;
    public static final int TIPO_DELITO_PREVENCION = 12;


    public static final int ICO_DELITO_SECUESTRO_INDEX = 0;
    public static final int ICO_DELITO_HOMICIDIO_INDEX = 1;
    public static final int ICO_DELITO_DESAPARICIONES_INDEX = 2;
    public static final int ICO_DELITO_ROBO_INDEX = 3;
    public static final int ICO_DELITO_SEXUAL_INDEX = 4;
    public static final int ICO_DELITO_EXTORCION_INDEX = 5;
    public static final int ICO_DELITO_MERCADO_NEGRO_INDEX = 6;
    public static final int ICO_DELITO_ENFRENTAMIENTOS_INDEX = 7;
    public static final int ICO_DELITO_CIBERNETICOS_INDEX = 8;
    public static final int ICO_DELITO_MOVIMIENTOS_SOCIALES_INDEX = 9;
    public static final int ICO_DELITO_VIOLENCIA_INDEX = 10;
    public static final int ICO_DELITO_PREVENCION_INDEX = 11;
    public static final String EXTRA_ID_DELITO = "EXTRA_ID_DELITO";


    public static String getDelitoNombre(int idTipoDelito) {
        String tipoDelito = "";
        switch (idTipoDelito) {
            case Constantes.TIPO_DELITO_HOMICIDIO:
                tipoDelito = "Homicidio";
                break;
            case Constantes.TIPO_DELITO_SECUESTRO:
                tipoDelito = "Secuestro";
                break;
            case Constantes.TIPO_DELITO_MOVIMIENTOS_SOCIALES:
                tipoDelito = "Movimientos Sociales";
                break;
            case Constantes.TIPO_DELITO_CIBERNETICOS:
                tipoDelito = "Cibernetico";
                break;
            case Constantes.TIPO_DELITO_DESAPARICIONES:
                tipoDelito = "Desaparición";
                break;
            case Constantes.TIPO_DELITO_ENFRENTAMIENTOS:
                tipoDelito = "Enfrentamiento";
                break;
            case Constantes.TIPO_DELITO_EXTORCION:
                tipoDelito = "Extorción";
                break;
            case Constantes.TIPO_DELITO_MERCADO_NEGRO:
                //tipoDelito = "Mercado negro";
                tipoDelito = "Mercado ilegal";
                break;
            case Constantes.TIPO_DELITO_ROBO:
                tipoDelito = "Robo";
                break;
            case Constantes.TIPO_DELITO_SEXUAL:
                tipoDelito = "Delitos Sexuales";
                break;
            case Constantes.TIPO_DELITO_PREVENCION:
                tipoDelito = "Prevención";
                break;
            case Constantes.TIPO_DELITO_VIOLENCIA:
                tipoDelito = "Violencia";
                break;
        }

        return tipoDelito;
    }



    /**
     * Inicializa los icosBitmapDescriptors de la aplicacion
     */
    public static void initIcos(Resources res) {
        //Bitmap descriptors para los pines
        icosBitmapDescriptors[Constantes.ICO_DELITO_HOMICIDIO_INDEX] = BitmapDescriptorFactory.fromResource(R.drawable.p_icon_homicidio_pin_med);
        icosBitmapDescriptors[Constantes.ICO_DELITO_SECUESTRO_INDEX] = BitmapDescriptorFactory.fromResource(R.drawable.p_icon_secuestro_pin_med);
        icosBitmapDescriptors[Constantes.ICO_DELITO_MOVIMIENTOS_SOCIALES_INDEX] = BitmapDescriptorFactory.fromResource(R.drawable.p_icon_movimientos_sociales_pin_med);
        icosBitmapDescriptors[Constantes.ICO_DELITO_CIBERNETICOS_INDEX] = BitmapDescriptorFactory.fromResource(R.drawable.p_icon_cibernetico_pin_med);
        icosBitmapDescriptors[Constantes.ICO_DELITO_DESAPARICIONES_INDEX] = BitmapDescriptorFactory.fromResource(R.drawable.p_icon_desapariciones_pin_med);
        icosBitmapDescriptors[Constantes.ICO_DELITO_ENFRENTAMIENTOS_INDEX] = BitmapDescriptorFactory.fromResource(R.drawable.p_icon_enfrentamientos_pin_med);
        icosBitmapDescriptors[Constantes.ICO_DELITO_EXTORCION_INDEX] = BitmapDescriptorFactory.fromResource(R.drawable.p_icon_extorcion_pin_med);
        icosBitmapDescriptors[Constantes.ICO_DELITO_MERCADO_NEGRO_INDEX] = BitmapDescriptorFactory.fromResource(R.drawable.p_icon_mercado_negro_pin_med);
        icosBitmapDescriptors[Constantes.ICO_DELITO_ROBO_INDEX] = BitmapDescriptorFactory.fromResource(R.drawable.p_icon_robo_pin_med);
        icosBitmapDescriptors[Constantes.ICO_DELITO_SEXUAL_INDEX] = BitmapDescriptorFactory.fromResource(R.drawable.p_icon_sexual_pin_med);
        icosBitmapDescriptors[Constantes.ICO_DELITO_VIOLENCIA_INDEX] = BitmapDescriptorFactory.fromResource(R.drawable.p_icon_violencia_pin_med);
        icosBitmapDescriptors[Constantes.ICO_DELITO_PREVENCION_INDEX] = BitmapDescriptorFactory.fromResource(R.drawable.p_icon_prevencion_pin_med);

        //BITMAP ICO PINES
        markersBitmap[Constantes.ICO_DELITO_HOMICIDIO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_homicidio_pin_med);
        markersBitmap[Constantes.ICO_DELITO_SECUESTRO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_secuestro_pin_med);
        markersBitmap[Constantes.ICO_DELITO_MOVIMIENTOS_SOCIALES_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_movimientos_sociales_pin_med);
        markersBitmap[Constantes.ICO_DELITO_CIBERNETICOS_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_cibernetico_pin_med);
        markersBitmap[Constantes.ICO_DELITO_DESAPARICIONES_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_desapariciones_pin_med);
        markersBitmap[Constantes.ICO_DELITO_ENFRENTAMIENTOS_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_enfrentamientos_pin_med);
        markersBitmap[Constantes.ICO_DELITO_EXTORCION_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_extorcion_pin_med);
        markersBitmap[Constantes.ICO_DELITO_MERCADO_NEGRO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_mercado_negro_pin_med);
        markersBitmap[Constantes.ICO_DELITO_ROBO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_robo_pin_med);
        markersBitmap[Constantes.ICO_DELITO_SEXUAL_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_sexual_pin_med);
        markersBitmap[Constantes.ICO_DELITO_VIOLENCIA_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_violencia_pin_med);
        markersBitmap[Constantes.ICO_DELITO_PREVENCION_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_prevencion_pin_med);

        //-------- ICOS BADGES---------------


        //SMALL
        icosBadgesBitmapSm[Constantes.ICO_DELITO_HOMICIDIO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_homicidio_badge_sm);
        icosBadgesBitmapSm[Constantes.ICO_DELITO_SECUESTRO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_secuestro_badge_sm);
        icosBadgesBitmapSm[Constantes.ICO_DELITO_MOVIMIENTOS_SOCIALES_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_movimientos_sociales_badge_sm);
        icosBadgesBitmapSm[Constantes.ICO_DELITO_CIBERNETICOS_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_cibernetico_badge_sm);
        icosBadgesBitmapSm[Constantes.ICO_DELITO_DESAPARICIONES_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_desapariciones_badge_sm);
        icosBadgesBitmapSm[Constantes.ICO_DELITO_ENFRENTAMIENTOS_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_enfrentamientos_badge_sm);
        icosBadgesBitmapSm[Constantes.ICO_DELITO_EXTORCION_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_extorcion_badge_sm);
        icosBadgesBitmapSm[Constantes.ICO_DELITO_MERCADO_NEGRO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_mercado_negro_badge_sm);
        icosBadgesBitmapSm[Constantes.ICO_DELITO_ROBO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_robo_badge_sm);
        icosBadgesBitmapSm[Constantes.ICO_DELITO_SEXUAL_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_sexual_badge_sm);
        icosBadgesBitmapSm[Constantes.ICO_DELITO_VIOLENCIA_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_violencia_badge_sm);
        icosBadgesBitmapSm[Constantes.ICO_DELITO_PREVENCION_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_prevencion_badge_sm);


        //MED
        icosBadgesBitmapMed[Constantes.ICO_DELITO_HOMICIDIO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_homicidio_badge_med);
        icosBadgesBitmapMed[Constantes.ICO_DELITO_SECUESTRO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_secuestro_badge_med);
        icosBadgesBitmapMed[Constantes.ICO_DELITO_MOVIMIENTOS_SOCIALES_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_movimientos_sociales_badge_med);
        icosBadgesBitmapMed[Constantes.ICO_DELITO_CIBERNETICOS_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_cibernetico_badge_med);
        icosBadgesBitmapMed[Constantes.ICO_DELITO_DESAPARICIONES_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_desapariciones_badge_med);
        icosBadgesBitmapMed[Constantes.ICO_DELITO_ENFRENTAMIENTOS_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_enfrentamientos_badge_med);
        icosBadgesBitmapMed[Constantes.ICO_DELITO_EXTORCION_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_extorcion_badge_med);
        icosBadgesBitmapMed[Constantes.ICO_DELITO_MERCADO_NEGRO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_mercado_negro_badge_med);
        icosBadgesBitmapMed[Constantes.ICO_DELITO_ROBO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_robo_badge_med);
        icosBadgesBitmapMed[Constantes.ICO_DELITO_SEXUAL_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_sexual_badge_med);
        icosBadgesBitmapMed[Constantes.ICO_DELITO_VIOLENCIA_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_violencia_badge_med);
        icosBadgesBitmapMed[Constantes.ICO_DELITO_PREVENCION_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_prevencion_badge_med);

        //Large
        icosBadgesBitmapLarge[Constantes.ICO_DELITO_HOMICIDIO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_homicidio_badge_large);
        icosBadgesBitmapLarge[Constantes.ICO_DELITO_SECUESTRO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_secuestro_badge_large);
        icosBadgesBitmapLarge[Constantes.ICO_DELITO_MOVIMIENTOS_SOCIALES_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_movimientos_sociales_badge_large);
        icosBadgesBitmapLarge[Constantes.ICO_DELITO_CIBERNETICOS_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_cibernetico_badge_large);
        icosBadgesBitmapLarge[Constantes.ICO_DELITO_DESAPARICIONES_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_desapariciones_badge_large);
        icosBadgesBitmapLarge[Constantes.ICO_DELITO_ENFRENTAMIENTOS_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_enfrentamientos_badge_large);
        icosBadgesBitmapLarge[Constantes.ICO_DELITO_EXTORCION_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_extorcion_badge_large);
        icosBadgesBitmapLarge[Constantes.ICO_DELITO_MERCADO_NEGRO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_mercado_negro_badge_large);
        icosBadgesBitmapLarge[Constantes.ICO_DELITO_ROBO_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_robo_badge_large);
        icosBadgesBitmapLarge[Constantes.ICO_DELITO_SEXUAL_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_sexual_badge_large);
        icosBadgesBitmapLarge[Constantes.ICO_DELITO_VIOLENCIA_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_violencia_badge_large);
        icosBadgesBitmapLarge[Constantes.ICO_DELITO_PREVENCION_INDEX] = BitmapFactory.decodeResource(res, R.drawable.p_icon_prevencion_badge_large);


    }

    /**
     * Recupera el icono del delito BADGE, de acuerdo al id del delito
     * @param idTipoDelito
     * @return
     */
    public static Bitmap getIcoBadgeByDelitoSm(int idTipoDelito) {
        switch (idTipoDelito) {
            case TIPO_DELITO_CIBERNETICOS:
                return icosBadgesBitmapSm[ICO_DELITO_CIBERNETICOS_INDEX];
            case TIPO_DELITO_DESAPARICIONES:
                return icosBadgesBitmapSm[ICO_DELITO_DESAPARICIONES_INDEX];
            case TIPO_DELITO_ENFRENTAMIENTOS:
                return icosBadgesBitmapSm[ICO_DELITO_ENFRENTAMIENTOS_INDEX];
            case TIPO_DELITO_EXTORCION:
                return icosBadgesBitmapSm[ICO_DELITO_EXTORCION_INDEX];
            case TIPO_DELITO_HOMICIDIO:
                return icosBadgesBitmapSm[ICO_DELITO_HOMICIDIO_INDEX];
            case TIPO_DELITO_MERCADO_NEGRO:
                return icosBadgesBitmapSm[ICO_DELITO_MERCADO_NEGRO_INDEX];
            case TIPO_DELITO_ROBO:
                return icosBadgesBitmapSm[ICO_DELITO_ROBO_INDEX];
            case TIPO_DELITO_SECUESTRO:
                return icosBadgesBitmapSm[ICO_DELITO_SECUESTRO_INDEX];
            case TIPO_DELITO_SEXUAL:
                return icosBadgesBitmapSm[ICO_DELITO_SEXUAL_INDEX];
            case TIPO_DELITO_MOVIMIENTOS_SOCIALES:
                return icosBadgesBitmapSm[ICO_DELITO_MOVIMIENTOS_SOCIALES_INDEX];
            case TIPO_DELITO_VIOLENCIA:
                return icosBadgesBitmapSm[ICO_DELITO_VIOLENCIA_INDEX];
            case TIPO_DELITO_PREVENCION:
                return icosBadgesBitmapSm[ICO_DELITO_PREVENCION_INDEX];
        }
        return null;
    }

    /**
     * Recupera el icono del delito BADGE
     * @param idTipoDelito
     * @return
     */
    public static Bitmap getIcoBadgeByDelitoMed(int idTipoDelito) {
        switch (idTipoDelito) {
            case TIPO_DELITO_CIBERNETICOS:
                return icosBadgesBitmapMed[ICO_DELITO_CIBERNETICOS_INDEX];
            case TIPO_DELITO_DESAPARICIONES:
                return icosBadgesBitmapMed[ICO_DELITO_DESAPARICIONES_INDEX];
            case TIPO_DELITO_ENFRENTAMIENTOS:
                return icosBadgesBitmapMed[ICO_DELITO_ENFRENTAMIENTOS_INDEX];
            case TIPO_DELITO_EXTORCION:
                return icosBadgesBitmapMed[ICO_DELITO_EXTORCION_INDEX];
            case TIPO_DELITO_HOMICIDIO:
                return icosBadgesBitmapMed[ICO_DELITO_HOMICIDIO_INDEX];
            case TIPO_DELITO_MERCADO_NEGRO:
                return icosBadgesBitmapMed[ICO_DELITO_MERCADO_NEGRO_INDEX];
            case TIPO_DELITO_ROBO:
                return icosBadgesBitmapMed[ICO_DELITO_ROBO_INDEX];
            case TIPO_DELITO_SECUESTRO:
                return icosBadgesBitmapMed[ICO_DELITO_SECUESTRO_INDEX];
            case TIPO_DELITO_SEXUAL:
                return icosBadgesBitmapMed[ICO_DELITO_SEXUAL_INDEX];
            case TIPO_DELITO_MOVIMIENTOS_SOCIALES:
                return icosBadgesBitmapMed[ICO_DELITO_MOVIMIENTOS_SOCIALES_INDEX];
            case TIPO_DELITO_VIOLENCIA:
                return icosBadgesBitmapMed[ICO_DELITO_VIOLENCIA_INDEX];
            case TIPO_DELITO_PREVENCION:
                return icosBadgesBitmapMed[ICO_DELITO_PREVENCION_INDEX];
        }
        return null;
    }


    /**
     * Recupera el icono del delito BADGE
     * @param idTipoDelito
     * @return
     */
    public static Bitmap getIcoBadgeByDelitoLarge(int idTipoDelito) {
        switch (idTipoDelito) {
            case TIPO_DELITO_CIBERNETICOS:
                return icosBadgesBitmapLarge[ICO_DELITO_CIBERNETICOS_INDEX];
            case TIPO_DELITO_DESAPARICIONES:
                return icosBadgesBitmapLarge[ICO_DELITO_DESAPARICIONES_INDEX];
            case TIPO_DELITO_ENFRENTAMIENTOS:
                return icosBadgesBitmapLarge[ICO_DELITO_ENFRENTAMIENTOS_INDEX];
            case TIPO_DELITO_EXTORCION:
                return icosBadgesBitmapLarge[ICO_DELITO_EXTORCION_INDEX];
            case TIPO_DELITO_HOMICIDIO:
                return icosBadgesBitmapLarge[ICO_DELITO_HOMICIDIO_INDEX];
            case TIPO_DELITO_MERCADO_NEGRO:
                return icosBadgesBitmapLarge[ICO_DELITO_MERCADO_NEGRO_INDEX];
            case TIPO_DELITO_ROBO:
                return icosBadgesBitmapLarge[ICO_DELITO_ROBO_INDEX];
            case TIPO_DELITO_SECUESTRO:
                return icosBadgesBitmapLarge[ICO_DELITO_SECUESTRO_INDEX];
            case TIPO_DELITO_SEXUAL:
                return icosBadgesBitmapLarge[ICO_DELITO_SEXUAL_INDEX];
            case TIPO_DELITO_MOVIMIENTOS_SOCIALES:
                return icosBadgesBitmapLarge[ICO_DELITO_MOVIMIENTOS_SOCIALES_INDEX];
            case TIPO_DELITO_VIOLENCIA:
                return icosBadgesBitmapLarge[ICO_DELITO_VIOLENCIA_INDEX];
            case TIPO_DELITO_PREVENCION:
                return icosBadgesBitmapLarge[ICO_DELITO_PREVENCION_INDEX];
        }
        return null;
    }


    public static String getDelitoNombreById(int idTipoDelito, Context ctx) {
        switch (idTipoDelito) {
            case TIPO_DELITO_CIBERNETICOS:
                return ctx.getString(R.string.delito_cibernetico);
            case TIPO_DELITO_DESAPARICIONES:
                return ctx.getString(R.string.delito_desapariciones);
            case TIPO_DELITO_ENFRENTAMIENTOS:
                return ctx.getString(R.string.delito_enfrentamientos);
            case TIPO_DELITO_EXTORCION:
                return ctx.getString(R.string.delito_extorcion);
            case TIPO_DELITO_HOMICIDIO:
                return ctx.getString(R.string.delito_homicidio);
            case TIPO_DELITO_MERCADO_NEGRO:
                return ctx.getString(R.string.delito_mercado_negro);
            case TIPO_DELITO_ROBO:
                return ctx.getString(R.string.delito_robo);
            case TIPO_DELITO_SECUESTRO:
                return ctx.getString(R.string.delito_secuestro);
            case TIPO_DELITO_SEXUAL:
                return ctx.getString(R.string.delito_sexual);
            case TIPO_DELITO_MOVIMIENTOS_SOCIALES:
                return ctx.getString(R.string.delito_movimientos_sociales);
            case TIPO_DELITO_VIOLENCIA:
                return ctx.getString(R.string.delito_violencia);
        }
        return null;
    }


    /**
     * Recupera los subdelitos
     * @param idTipoDelito
     * @return
     */
    public static String[] getSubDelitosById(int idTipoDelito){
        switch (idTipoDelito) {
            case TIPO_DELITO_CIBERNETICOS:
                return new String[]{"Clonación tarjetas","Robo de identidad","Hacking","Robo de información"};
            case TIPO_DELITO_DESAPARICIONES:
                return new String[] {"Forzada"};
            case TIPO_DELITO_ENFRENTAMIENTOS:
                return new String[] {"Gobierno - Delincuentes", "Delicuentes - Delincuentes", "Sociedad - Delincuentes"};
            case TIPO_DELITO_EXTORCION:
                return new String[]{"Servidor público","Grupo delictivo", "Telefónica","Cibernética","Franelero"};
            case TIPO_DELITO_HOMICIDIO:
                return new String[] {"Arma blanca","Arma fuego","Involuntario"};
            case TIPO_DELITO_MERCADO_NEGRO:
                return new String[] {"Drogas","Armas","Pirateria","Organos","Animales"};
            case TIPO_DELITO_ROBO:
                return new String[] {"Vehículo","Transeunte","Casa","Negocio","Automovilista","Cibernetico"};
            case TIPO_DELITO_SECUESTRO:
                return new String[]{"Simple",  "Grupo delictivo","Virtual","Desapariciones forzadas"};
            case TIPO_DELITO_SEXUAL:
                return new String[]{"Violación","Trata de blancas","Acoso sexual","Pornografía infantil"};
            case TIPO_DELITO_MOVIMIENTOS_SOCIALES:
                return new String[] {"Marcha","Plantones","Bloqueos"};
            case TIPO_DELITO_VIOLENCIA:
                return new String[] {"Violencia intrafamiliar","Alteración del orden público"};
            case TIPO_DELITO_PREVENCION:
                return new String[] {"Persona sospechosos","Vehículo abandonado"};
        }
        return new String[] {};
    }


    /**
     * Recupera el nombre corto del mes
     * @param mes
     * @return
     */
    public static String getNombreCortoMes(int mes){

        switch(mes) {
            case 0:
                return "Ene";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Abr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Ago";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dic";
            default:
                return "";
        }
    }
}
