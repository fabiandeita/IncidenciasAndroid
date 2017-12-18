package mx.com.tecnologiadenegocios.curso2017.model;

/**
 * Created by Fabian De Ita on 28/03/2015.
 */
public class Actividad {

    public static final String TABLE_NAME = "Actividad";
    public static final String FIELD_ICVE = "icve";
    public static final String FIEL_ACTIVO = "activo";
    public static final String FIELD_CCVE = "ccve";
    public static final String FIELD_CCVEEMERGENCIA = "ccveEmergencia";
    public static final String FIELD_DESCRIPCION = "descripcion";
    public static final String FIELD_ICVEEMERGENCIA = "icveEmergencia";
    public static final String FIELD_TRANSITO = "transito";
    public static final String FIELD_FECHACREACION= "fechaCreacion";


    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "( " +
            FIELD_ICVE + " integer primary key autoincrement," +
            FIEL_ACTIVO + " integer," +
            FIELD_CCVE + " integer," +
            FIELD_CCVEEMERGENCIA + " integer," +
            FIELD_DESCRIPCION + " text," +
            FIELD_ICVEEMERGENCIA + " integer," +
            FIELD_TRANSITO + " integer," +
            FIELD_FECHACREACION + " text" +
            " );";

    private int activo;
    private int ccve;
    private int ccveEmergencia;
    private String descripcion;
    private int icve;
    private int icveEmergencia;
    private int transito;
    private String fechaCreacion;

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public int getCcve() {
        return ccve;
    }

    public void setCcve(int ccve) {
        this.ccve = ccve;
    }

    public int getCcveEmergencia() {
        return ccveEmergencia;
    }

    public void setCcveEmergencia(int ccveEmergencia) {
        this.ccveEmergencia = ccveEmergencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIcve() {
        return icve;
    }

    public void setIcve(int icve) {
        this.icve = icve;
    }

    public int getIcveEmergencia() {
        return icveEmergencia;
    }

    public void setIcveEmergencia(int icveEmergencia) {
        this.icveEmergencia = icveEmergencia;
    }

    public int getTransito() {
        return transito;
    }

    public void setTransito(int transito) {
        this.transito = transito;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
