package mx.com.dgom.sercco.android.prevent.to;

/**
 * Created by beto on 12/23/15.
 *
 * Esta clase representa un eemento multimedia de un delito ya publicado en la app
 */
public class DelitoMultimediaTO {



    private long id_tipo_multimedia;
    private String txt_archivo;

    public long getId_tipo_multimedia() {
        return id_tipo_multimedia;
    }

    public void setId_tipo_multimedia(long id_tipo_multimedia) {
        this.id_tipo_multimedia = id_tipo_multimedia;
    }

    public String getTxt_archivo() {
        return txt_archivo;
    }

    public void setTxt_archivo(String txt_archivo) {
        this.txt_archivo = txt_archivo;
    }
}
