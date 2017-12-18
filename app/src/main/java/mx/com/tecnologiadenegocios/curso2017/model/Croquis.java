package mx.com.tecnologiadenegocios.curso2017.model;

import java.io.Serializable;

/**
 * Created by Fabian De Ita on 06/04/2015.
 */
public class Croquis implements Serializable {
    public static final String TABLE_NAME = "Croquis";

    public static final String FIEL_ICVE = "icve";
    public static final String FIELD_ICVEEMERGENCIA = "icveEmergencia";
    public static final String FIELD_URL = "url";
    public static final String FIELD_CCVE = "ccve";

    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "( " +
            FIEL_ICVE + " integer primary key autoincrement," +
            FIELD_ICVEEMERGENCIA + " integer," +
            FIELD_URL + " text," +
            FIELD_CCVE + " integer" +
            " );";



    private int icve;
    private int icveEmergencia;
    private String url;
    private int ccve;


    public Croquis() {
    }

    public Croquis(int icve, int icveEmergencia, String url, int ccve) {
        this.icve = icve;
        this.icveEmergencia = icveEmergencia;
        this.url = url;
        this.ccve = ccve;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCcve() {
        return ccve;
    }

    public void setCcve(int ccve) {
        this.ccve = ccve;
    }
}
