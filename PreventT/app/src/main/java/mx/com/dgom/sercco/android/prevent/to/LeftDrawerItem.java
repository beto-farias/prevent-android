package mx.com.dgom.sercco.android.prevent.to;

/**
 * Created by beto on 10/23/15.
 */
public class LeftDrawerItem {

    public static final int MENU_LOGIN                  = 1; //Solo texto
    public static final int MENU_LOGOUT                 = 2; //Solo texto
    public static final int MENU_REPORTAR_DELITO        = 3; //Solo texto

    public static final int MENU_CHECK_SECUESTRO            = 1001; // texto con check box
    public static final int MENU_CHECK_HOMICIDIO            = 1002; // texto con check box
    public static final int MENU_CHECK_DESAPARICONES        = 1003; // texto con check box
    public static final int MENU_CHECK_CIBERNETICO          = 1004; // texto con check box
    public static final int MENU_CHECK_ENFRENTAMIENTOS      = 1005; // texto con check box
    public static final int MENU_CHECK_EXTORCION            = 1006; // texto con check box
    public static final int MENU_CHECK_MOVIMIENTOS_SOCIALES = 1007; // texto con check box
    public static final int MENU_CHECK_MERCADO_NEGRO        = 1008; // texto con check box
    public static final int MENU_CHECK_ROBO                 = 1009; // texto con check box
    public static final int MENU_CHECK_SEXUAL               = 1010; // texto con check box
    public static final int MENU_CHECK_VIOLENCIA            = 1011; // ANTES SOCIAL
    public static final int MENU_CHECK_PREVENCION           = 1012; // texto con check box


    private String title;
    private boolean selected;
    private int type;
    private boolean hasSelectableItem;
    private int idDelito;

    public LeftDrawerItem(int type, int idDelito, boolean hasSelectableItem){
        this.type = type;
        this.hasSelectableItem = hasSelectableItem;
        this.idDelito = idDelito;
    }

    public LeftDrawerItem(int type, int idDelito,boolean hasSelectableItem,String title){
        this(type,idDelito,hasSelectableItem);
        this.title = title;
    }
    public LeftDrawerItem(int type, int idDelito,boolean hasSelectableItem,String title, boolean isSelected){
        this(type,idDelito,hasSelectableItem,title);
        this.selected = isSelected;
    }

    public int getIdDelito() {
        return idDelito;
    }

    public void setIdDelito(int idDelito) {
        this.idDelito = idDelito;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isHasSelectableItem() {
        return hasSelectableItem;
    }

    public void setHasSelectableItem(boolean hasSelectableItem) {
        this.hasSelectableItem = hasSelectableItem;
    }
}
