package mx.com.dgom.util.version;


public class AppVersionTO {

	private Long id_version;
	private Double id_version_aplicacion;
	private Long id_aplicacion;
	private String txt_tipo_dispositivo;
	private String txt_nombre_app;
	private String txt_notas;
	private String txt_url;
	private int b_obligatorio;
	private int b_habilitado;
	private String fch_limite_instalacion;
	private String txt_nombre;
	private String txt_md5_app;
	private String version;

	public Long getId_version() {
		return id_version;
	}

	public void setId_version(Long id_version) {
		this.id_version = id_version;
	}

	public Double getId_version_aplicacion() {
		return id_version_aplicacion;
	}

	public void setId_version_aplicacion(Double id_version_aplicacion) {
		this.id_version_aplicacion = id_version_aplicacion;
	}

	public Long getId_aplicacion() {
		return id_aplicacion;
	}

	public void setId_aplicacion(Long id_aplicacion) {
		this.id_aplicacion = id_aplicacion;
	}

	public String getTxt_tipo_dispositivo() {
		return txt_tipo_dispositivo;
	}

	public void setTxt_tipo_dispositivo(String txt_tipo_dispositivo) {
		this.txt_tipo_dispositivo = txt_tipo_dispositivo;
	}

	public String getTxt_nombre_app() {
		return txt_nombre_app;
	}

	public void setTxt_nombre_app(String txt_nombre_app) {
		this.txt_nombre_app = txt_nombre_app;
	}

	public String getTxt_notas() {
		return txt_notas;
	}

	public void setTxt_notas(String txt_notas) {
		this.txt_notas = txt_notas;
	}

	public String getTxt_url() {
		return txt_url;
	}

	public void setTxt_url(String txt_url) {
		this.txt_url = txt_url;
	}

	public int getB_obligatorio() {
		return b_obligatorio;
	}

	public void setB_obligatorio(int b_obligatorio) {
		this.b_obligatorio = b_obligatorio;
	}

	public int getB_habilitado() {
		return b_habilitado;
	}

	public void setB_habilitado(int b_habilitado) {
		this.b_habilitado = b_habilitado;
	}

	public String getFch_limite_instalacion() {
		return fch_limite_instalacion;
	}

	public void setFch_limite_instalacion(String fch_limite_instalacion) {
		this.fch_limite_instalacion = fch_limite_instalacion;
	}

	public String getTxt_nombre() {
		return txt_nombre;
	}

	public void setTxt_nombre(String txt_nombre) {
		this.txt_nombre = txt_nombre;
	}

	public String getTxt_md5_app() {
		return txt_md5_app;
	}

	public void setTxt_md5_app(String txt_md5_app) {
		this.txt_md5_app = txt_md5_app;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
