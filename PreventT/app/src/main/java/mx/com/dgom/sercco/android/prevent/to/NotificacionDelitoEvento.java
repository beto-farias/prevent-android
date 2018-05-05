package mx.com.dgom.sercco.android.prevent.to;

/**
 * Created by beto on 12/3/15.
 */
public class NotificacionDelitoEvento {
    private long id_evento;
    private long id_num_delito;
    private int id_tipo_delito;
    private long id_usuario;

    public long getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(long id_usuario) {
        this.id_usuario = id_usuario;
    }

    public long getId_evento() {
        return id_evento;
    }

    public void setId_evento(long id_evento) {
        this.id_evento = id_evento;
    }

    public long getId_num_delito() {
        return id_num_delito;
    }

    public void setId_num_delito(long id_num_delito) {
        this.id_num_delito = id_num_delito;
    }

    public int getId_tipo_delito() {
        return id_tipo_delito;
    }

    public void setId_tipo_delito(int id_tipo_delito) {
        this.id_tipo_delito = id_tipo_delito;
    }
}
