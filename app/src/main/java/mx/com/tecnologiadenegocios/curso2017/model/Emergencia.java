package mx.com.tecnologiadenegocios.curso2017.model;

import java.io.Serializable;

/**
 * Created by Fabian De Ita on 24/03/2015.
 */
public class Emergencia implements Serializable {
    public static final String TABLE_NAME = "Emergencia";

    public static final String FIEL_ACCIONESREALIZADAS = "accionesRealizadas";
    public static final String FIELD_ALTITUD = "altitud";
    public static final String FIELD_CCVE = "ccve";
    public static final String FIELD_DESCRIPCION = "descripcion";
    public static final String FIELD_FECHACREACION = "fechaCreacion";
    public static final String FIELD_FECHADEFINITIVA = "fechaDefinitiva";
    public static final String FIELD_FECHAPROVISIONAL = "fechaProvisional";
    public static final String FIELD_ICVE = "icve";
    public static final String FIELD_ICVECARRETERA = "icveCarretera";
    public static final String FIELD_ICVECATEGORIAEMERGENCIA = "icveCategoriaEmergencia";
    public static final String FIELD_ICVECAUSAEMERGENCIA= "icveCausaEmergencia";
    public static final String FIELD_ICVEDANOS = "FIELD_icveDanos";
    public static final String FIELD_ICVEESTADO = "icveEstado";
    public static final String FIELD_ICVESUBTRAMO = "icveSubtramo";
    public static final String FIELD_ICVETIPOEMERGENCIA = "icveTipoEmergencia";
    public static final String FIELD_ICVETRAMO = "icveTramo";
    public static final String FIELD_ICVETRANSITO = "icveTransito";
    public static final String FIELD_KMFINAL = "kmFinal";
    public static final String FIELD_KMINICIAL = "kmInicial";
    public static final String FIELD_LATITUD = "latitud";
    public static final String FIELD_LONGITUD = "longitud";
    public static final String FIELD_OTRAS = "otras";
    public static final String FIELD_RUTAALTERNATIVA = "rutaAlternativa";
    public static final String FIELD_TIPOVEHICULO = "tipoVehiculo";
    public static final String FIELD_VEHICULOSTRANSITANAUTOS = "vehiculosTransitanAutos";
    public static final String FIELD_VEHICULOSTRANSITANCAMIONES = "vehiculosTransitanCamiones";
    public static final String FIELD_VEHICULOSTRANSITANTODOTIPO = "vehiculosTransitanTodoTipo";


    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "( " +
            FIELD_ICVE + " integer primary key autoincrement," +
            FIEL_ACCIONESREALIZADAS + " text," +
            FIELD_ALTITUD + " real," +
            FIELD_CCVE + " integer," +
            FIELD_DESCRIPCION + " text," +
            FIELD_FECHACREACION + " text," +
            FIELD_FECHADEFINITIVA + " text," +
            FIELD_FECHAPROVISIONAL + " text," +
            FIELD_ICVECARRETERA + " integer," +
            FIELD_ICVECATEGORIAEMERGENCIA + " integer," +
            FIELD_ICVECAUSAEMERGENCIA + " integer," +
            FIELD_ICVEDANOS + " integer," +
            FIELD_ICVEESTADO + " integer," +
            FIELD_ICVESUBTRAMO + " integer," +
            FIELD_ICVETIPOEMERGENCIA + " integer," +
            FIELD_ICVETRAMO + " integer," +
            FIELD_ICVETRANSITO + " integer," +
            FIELD_KMFINAL + " text," +
            FIELD_KMINICIAL + " text," +
            FIELD_LATITUD + " real," +
            FIELD_LONGITUD + " real," +
            FIELD_OTRAS + " text," +
            FIELD_RUTAALTERNATIVA + " integer," +
            FIELD_TIPOVEHICULO + " integer," +
            FIELD_VEHICULOSTRANSITANAUTOS + " integer," +
            FIELD_VEHICULOSTRANSITANCAMIONES + " integer," +
            FIELD_VEHICULOSTRANSITANTODOTIPO + " integer" +
            " );";

    private String accionesRealizadas = "";
    private Double altitud;
    private int ccve;
    private String descripcion;
    private String fechaDefinitiva;
    private String fechaProvisional;
    private String fechaCreacion;
    private int icve;
    private int icveCarretera;
    private int icveCategoriaEmergencia;
    private int icveCausaEmergencia;
    private int icveDanos;
    private int icveEstado;
    private int icveSubtramo;
    private int icveTipoEmergencia;
    private int icveTramo;
    private int icveTransito;
    private String kmFinal;
    private String kmInicial;
    private Double latitud;
    private Double longitud;
    private String otras;
    private int rutaAlternativa;
    private int tipoVehiculo;
    private int vehiculosTransitanAutos;
    private int vehiculosTransitanCamiones;
    private int vehiculosTransitanTodoTipo;



    public Emergencia() {
    }
    public String getAccionesRealizadas() {
        return accionesRealizadas;
    }

    public void setAccionesRealizadas(String accionesRealizadas) {
        this.accionesRealizadas = accionesRealizadas;
    }

    public Double getAltitud() {
        return altitud;
    }

    public void setAltitud(Double altitud) {
        this.altitud = altitud;
    }

    public int getCcve() {
        return ccve;
    }

    public void setCcve(int ccve) {
        this.ccve = ccve;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaDefinitiva() {
        return fechaDefinitiva;
    }

    public void setFechaDefinitiva(String fechaDefinitiva) {
        this.fechaDefinitiva = fechaDefinitiva;
    }

    public String getFechaProvisional() {
        return fechaProvisional;
    }

    public void setFechaProvisional(String fechaProvisional) {
        this.fechaProvisional = fechaProvisional;
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

    public int getIcveCategoriaEmergencia() {
        return icveCategoriaEmergencia;
    }

    public void setIcveCategoriaEmergencia(int icveCategoriaEmergencia) {
        this.icveCategoriaEmergencia = icveCategoriaEmergencia;
    }

    public int getIcveCausaEmergencia() {
        return icveCausaEmergencia;
    }

    public void setIcveCausaEmergencia(int icveCausaEmergencia) {
        this.icveCausaEmergencia = icveCausaEmergencia;
    }

    public int getIcveDanos() {
        return icveDanos;
    }

    public void setIcveDanos(int icveDanos) {
        this.icveDanos = icveDanos;
    }

    public int getIcveEstado() {
        return icveEstado;
    }

    public void setIcveEstado(int icveEstado) {
        this.icveEstado = icveEstado;
    }

    public int getIcveSubtramo() {
        return icveSubtramo;
    }

    public void setIcveSubtramo(int icveSubtramo) {
        this.icveSubtramo = icveSubtramo;
    }

    public int getIcveTipoEmergencia() {
        return icveTipoEmergencia;
    }

    public void setIcveTipoEmergencia(int icveTipoEmergencia) {
        this.icveTipoEmergencia = icveTipoEmergencia;
    }

    public int getIcveTramo() {
        return icveTramo;
    }

    public void setIcveTramo(int icveTramo) {
        this.icveTramo = icveTramo;
    }

    public int getIcveTransito() {
        return icveTransito;
    }

    public void setIcveTransito(int icveTransito) {
        this.icveTransito = icveTransito;
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

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getOtras() {
        return otras;
    }

    public void setOtras(String otras) {
        this.otras = otras;
    }

    public int getRutaAlternativa() {
        return rutaAlternativa;
    }

    public void setRutaAlternativa(int rutaAlternativa) {
        this.rutaAlternativa = rutaAlternativa;
    }

    public int getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(int tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public int getVehiculosTransitanAutos() {
        return vehiculosTransitanAutos;
    }

    public void setVehiculosTransitanAutos(int vehiculosTransitanAutos) {
        this.vehiculosTransitanAutos = vehiculosTransitanAutos;
    }

    public int getVehiculosTransitanCamiones() {
        return vehiculosTransitanCamiones;
    }

    public void setVehiculosTransitanCamiones(int vehiculosTransitanCamiones) {
        this.vehiculosTransitanCamiones = vehiculosTransitanCamiones;
    }

    public int getVehiculosTransitanTodoTipo() {
        return vehiculosTransitanTodoTipo;
    }

    public void setVehiculosTransitanTodoTipo(int vehiculosTransitanTodoTipo) {
        this.vehiculosTransitanTodoTipo = vehiculosTransitanTodoTipo;
    }


    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
