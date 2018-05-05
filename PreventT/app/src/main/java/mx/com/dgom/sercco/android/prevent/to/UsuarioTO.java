package mx.com.dgom.sercco.android.prevent.to;

/**
 * Created by beto on 10/21/15.
 */
public class UsuarioTO {
    private static final String TAG = "UsuarioTO";

    private long idUsuario;
    private String txtUsuario;
    private String txtEmail;
    private String uriPicImageStr;
    private Long fbUser;
    private int usuarioPro;

    public UsuarioTO(){}

    /**
     * Constructor del usuario
     * @param txtUsuario
     * @param txtEmail
     * @param picUri
     */
    public UsuarioTO(String txtUsuario, String txtEmail, String picUri){
        this.txtUsuario = txtUsuario;
        this.txtEmail = txtEmail;
        this.uriPicImageStr = picUri;
    }



    public UsuarioTO(String txtUsuario, String txtEmail, String uriPicImage, long fbUser){
        this(txtUsuario,txtEmail,uriPicImage);
        this.fbUser = fbUser;
    }

    public int getUsuarioPro() {
        return usuarioPro;
    }

    public void setUsuarioPro(int usuarioPro) {
        this.usuarioPro = usuarioPro;
    }

    public Long getFbUser() {
        return fbUser;
    }

    public void setFbUser(Long fbUser) {
        this.fbUser = fbUser;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTxtUsuario() {
        return txtUsuario;
    }

    public void setTxtUsuario(String txtUsuario) {
        this.txtUsuario = txtUsuario;
    }

    public String getTxtEmail() {
        return txtEmail;
    }

    public void setTxtEmail(String txtEmail) {
        this.txtEmail = txtEmail;
    }

    public void setUriPicImageStr(String uriPicImage) {
        this.uriPicImageStr = uriPicImage;
    }

    public String getUriPicImageStr(){
        return this.uriPicImageStr;
    }
}
