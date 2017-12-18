package mx.com.tecnologiadenegocios.curso2017.model;

/**
 * Created by Fabian De Ita on 28/03/2015.
 */
public class Subtramo {
    public static final String TABLE_NAME = "Subtramo";
    public static final String FIELD_ICVE = "icve";
    public static final String FIELD_ICVETRAMO = "icveTramo";
    public static final String FIELD_KMFINAL = "kmFinal";
    public static final String FIELD_KMINICIAL = "kmInicial";
    public static final String FIELD_NOMBRE = "nombre";
    public static final String FIELD_ICVEESTADO = "icveEstado";

    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "( " +
            FIELD_ICVE + " integer primary key autoincrement," +
            FIELD_ICVETRAMO + " integer," +
            FIELD_ICVEESTADO + " integer," +
            FIELD_KMINICIAL + " text," +
            FIELD_KMFINAL + " text," +
            FIELD_NOMBRE + " text" +

            " );";

    private int icve;
    private int icveTramo;
    private String kmFinal;
    private String kmInicial;
    private String nombre;
    private int icveEstado;

    public Subtramo() {
    }

    public Subtramo(int icve, int icveTramo, int icveEstado, String kmInicial, String kmFinal, String nombre) {
        this.icve = icve;
        this.icveTramo = icveTramo;
        this.kmFinal = kmFinal;
        this.kmInicial = kmInicial;
        this.nombre = nombre;
        this.icveEstado = icveEstado;
    }

    public int getIcve() {
        return icve;
    }

    public void setIcve(int icve) {
        this.icve = icve;
    }

    public int getIcveTramo() {
        return icveTramo;
    }

    public void setIcveTramo(int icveTramo) {
        this.icveTramo = icveTramo;
    }

    public String getKmFinal() {
        return kmFinal;
    }

    public void setKmFinal(String kmFinal) {
        this.kmFinal = kmFinal;
    }

    public String getKmInicial() {
        return kmInicial;
    }

    public void setKmInicial(String kmInicial) {
        this.kmInicial = kmInicial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIcveEstado() {
        return icveEstado;
    }

    public void setIcveEstado(int icveEstado) {
        this.icveEstado = icveEstado;
    }
}
