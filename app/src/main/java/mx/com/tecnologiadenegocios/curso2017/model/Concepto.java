package mx.com.tecnologiadenegocios.curso2017.model;

/**
 * Created by Fabian De Ita on 28/03/2015.
 */
public class Concepto {

    public static final String TABLE_NAME = "Concepto";
    public static final String FIELD_CANTIDAD = "cantidad";
    public static final String FIELD_CCVE = "ccve";
    public static final String FIELD_CCVEEMERGENCIA = "ccveEmergencia";
    public static final String FIELD_DESCRIPCION = "descripcion";
    public static final String FIELD_ICVE = "icve";
    public static final String FIELD_ICVEEMERGENCIA = "icveEmergencia";
    public static final String FIELD_ICVEUNIDAD = "icveUnidad";
    public static final String FIELD_IPORAVANCE = "iporAvance";
    public static final String FIELD_FECHACREACION= "fechaCreacion";


    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "( " +
            FIELD_ICVE + " integer primary key autoincrement," +
            FIELD_CANTIDAD + " integer," +
            FIELD_CCVE + " integer," +
            FIELD_CCVEEMERGENCIA + " integer," +
            FIELD_DESCRIPCION + " text," +
            FIELD_ICVEEMERGENCIA + " integer," +
            FIELD_ICVEUNIDAD + " integer," +
            FIELD_IPORAVANCE + " integer," +
            FIELD_FECHACREACION + " text" +
            " );";

    private int icve;
    private int cantidad;
    private int ccve;
    private int ccveEmergencia;
    private String descripcion;
    private int icveEmergencia;
    private int icveUnidad;
    private int iporAvance;
    private String fechaCreacion;

    public int getIcve() {
        return icve;
    }

    public void setIcve(int icve) {
        this.icve = icve;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
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

    public int getIcveEmergencia() {
        return icveEmergencia;
    }

    public void setIcveEmergencia(int icveEmergencia) {
        this.icveEmergencia = icveEmergencia;
    }

    public int getIcveUnidad() {
        return icveUnidad;
    }

    public void setIcveUnidad(int icveUnidad) {
        this.icveUnidad = icveUnidad;
    }

    public int getIporAvance() {
        return iporAvance;
    }

    public void setIporAvance(int iporAvance) {
        this.iporAvance = iporAvance;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
