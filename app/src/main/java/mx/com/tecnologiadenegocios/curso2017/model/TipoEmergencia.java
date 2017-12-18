package mx.com.tecnologiadenegocios.curso2017.model;

/**
 * Created by Fabian De Ita on 28/03/2015.
 */
public class TipoEmergencia {
    public static final String TABLE_NAME = "TipoEmergencia";
    public static final String FIELD_ICVE = "icve";
    public static final String FIELD_ICVECAUSAEMERGENCIA = "icveCausaEmergencia";
    public static final String FIELD_NOMBRE = "nombre";

    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "( " +
            FIELD_ICVE + " integer primary key autoincrement," +
            FIELD_ICVECAUSAEMERGENCIA + " integer," +
            FIELD_NOMBRE + " text" +
            " );";

    public TipoEmergencia(int icve, int icveCausaEmergencia, String nombre) {
        this.icve = icve;
        this.icveCausaEmergencia = icveCausaEmergencia;
        this.nombre = nombre;
    }

    public TipoEmergencia() {
    }

    private int icve;
    private int icveCausaEmergencia;
    private String nombre;

    public int getIcve() {
        return icve;
    }

    public void setIcve(int icve) {
        this.icve = icve;
    }

    public int getIcveCausaEmergencia() {
        return icveCausaEmergencia;
    }

    public void setIcveCausaEmergencia(int icveCausaEmergencia) {
        this.icveCausaEmergencia = icveCausaEmergencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
