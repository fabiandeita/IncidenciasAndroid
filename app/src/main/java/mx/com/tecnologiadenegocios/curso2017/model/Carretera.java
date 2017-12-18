package mx.com.tecnologiadenegocios.curso2017.model;

/**
 * Created by Fabian De Ita on 28/03/2015.
 */
public class Carretera {

    public static final String TABLE_NAME = "Carretera";
    public static final String FIELD_ICVE = "icve";
    public static final String FIELD_ICVEESTADO = "icveEstado";
    public static final String FIELD_NOMBRE = "nombre";

    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "( " +
            FIELD_ICVE + " integer primary key autoincrement," +
            FIELD_ICVEESTADO + " integer," +
            FIELD_NOMBRE + " text" +
            " );";

    private int icve;
    private int icveEstado;
    private String nombre;

    public Carretera() {
    }

    public Carretera(int icve, String nombre) {
        this.icve = icve;
        this.nombre = nombre;
    }

    public int getIcve() {
        return icve;
    }

    public void setIcve(int icve) {
        this.icve = icve;
    }

    public int getIcveEstado() {
        return icveEstado;
    }

    public void setIcveEstado(int icveEstado) {
        this.icveEstado = icveEstado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
