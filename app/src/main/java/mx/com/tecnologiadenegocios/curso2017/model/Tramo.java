package mx.com.tecnologiadenegocios.curso2017.model;

/**
 * Created by Fabian De Ita on 28/03/2015.
 */
public class Tramo {
    public static final String TABLE_NAME = "Tramo";
    public static final String FIELD_ICVE = "icve";
    public static final String FIELD_ICVECARRETERA = "icveCarretera";
    public static final String FIELD_NOMBRE = "nombre";

    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "( " +
            FIELD_ICVE + " integer primary key autoincrement," +
            FIELD_ICVECARRETERA + " integer," +
            FIELD_NOMBRE + " text" +
            " );";

    private int icve;
    private int icveCarretera;
    private String nombre;

    public Tramo(int icve, int icveCarretera, String nombre) {
        this.icve = icve;
        this.icveCarretera = icveCarretera;
        this.nombre = nombre;
    }

    public Tramo() {
    }

    public int getIcve() {
        return icve;
    }

    public void setIcve(int icve) {
        this.icve = icve;
    }

    public int getIcveCarretera() {
        return icveCarretera;
    }

    public void setIcveCarretera(int icveCarretera) {
        this.icveCarretera = icveCarretera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
