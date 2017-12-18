package mx.com.tecnologiadenegocios.curso2017.model;

/**
 * Created by Fabian De Ita on 28/03/2015.
 */
public class Danos {
    public static final String TABLE_NAME = "Danos";
    public static final String FIELD_ICVE = "icve";
    public static final String FIELD_NOMBRE = "nombre";

    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "( " +
            FIELD_ICVE + " integer primary key autoincrement," +
            FIELD_NOMBRE + " text" +
            " );";

    public Danos() {
    }

    public Danos(int icve, String nombre) {
        this.icve = icve;
        this.nombre = nombre;
    }

    private int icve;
    private String nombre;

    public int getIcve() {
        return icve;
    }

    public void setIcve(int icve) {
        this.icve = icve;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
