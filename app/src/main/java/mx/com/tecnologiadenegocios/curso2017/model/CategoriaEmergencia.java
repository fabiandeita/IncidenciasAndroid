package mx.com.tecnologiadenegocios.curso2017.model;

/**
 * Created by Fabian De Ita on 28/03/2015.
 */
public class CategoriaEmergencia {

    public static final String TABLE_NAME = "CategoriaEmergencia";
    public static final String FIELD_ICVE = "icve";
    public static final String FIELD_ICVETIPOEMERGENCIA = "icveTipoEmergencia";
    public static final String FIELD_NOMBRE = "nombre";

    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "( " +
            FIELD_ICVE + " integer primary key autoincrement," +
            FIELD_ICVETIPOEMERGENCIA + " integer," +
            FIELD_NOMBRE + " text" +
            " );";

    private int icve;
    private int icveTipoEmergencia;
    private String nombre;

    public CategoriaEmergencia() {
    }

    public CategoriaEmergencia(int icve, int icveTipoEmergencia, String nombre) {
        this.icve = icve;
        this.icveTipoEmergencia = icveTipoEmergencia;
        this.nombre = nombre;
    }

    public int getIcve() {
        return icve;
    }

    public void setIcve(int icve) {
        this.icve = icve;
    }

    public int getIcveTipoEmergencia() {
        return icveTipoEmergencia;
    }

    public void setIcveTipoEmergencia(int icveTipoEmergencia) {
        this.icveTipoEmergencia = icveTipoEmergencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
