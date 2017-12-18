package mx.com.tecnologiadenegocios.curso2017.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import mx.com.tecnologiadenegocios.curso2017.model.Actividad;
import mx.com.tecnologiadenegocios.curso2017.model.Concepto;
import mx.com.tecnologiadenegocios.curso2017.model.Emergencia;
import mx.com.tecnologiadenegocios.curso2017.model.Estado;
import mx.com.tecnologiadenegocios.curso2017.model.Imagen;
import mx.com.tecnologiadenegocios.curso2017.model.TipoEmergencia;
import mx.com.tecnologiadenegocios.curso2017.model.Tramo;
import mx.com.tecnologiadenegocios.curso2017.model.Transito;
import mx.com.tecnologiadenegocios.curso2017.model.Carretera;
import mx.com.tecnologiadenegocios.curso2017.model.CategoriaEmergencia;
import mx.com.tecnologiadenegocios.curso2017.model.CausaEmergencia;
import mx.com.tecnologiadenegocios.curso2017.model.Croquis;
import mx.com.tecnologiadenegocios.curso2017.model.Danos;
import mx.com.tecnologiadenegocios.curso2017.model.Subtramo;

/**
 * Created by Ing.Sergio Martínez on 22/06/2015.
 */
public class BDHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Emergencias Pruebas";
    private static final int SCHEME_VERSION = 1;
    private SQLiteDatabase db;


    public BDHelper(Context context) {
        super(context, DB_NAME, null, SCHEME_VERSION);
        db = this.getWritableDatabase();
    }
    public void closeConnection(){
        db.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    public void createTables(){
        db.execSQL(Actividad.CREATE_DB_TABLE);
        db.execSQL(Carretera.CREATE_DB_TABLE);
        db.execSQL(CategoriaEmergencia.CREATE_DB_TABLE);
        db.execSQL(CausaEmergencia.CREATE_DB_TABLE);
        db.execSQL(Concepto.CREATE_DB_TABLE);
        db.execSQL(Danos.CREATE_DB_TABLE);
        db.execSQL(Estado.CREATE_DB_TABLE);
        db.execSQL(Subtramo.CREATE_DB_TABLE);
        db.execSQL(TipoEmergencia.CREATE_DB_TABLE);
        db.execSQL(Emergencia.CREATE_DB_TABLE);
        db.execSQL(Tramo.CREATE_DB_TABLE);
        db.execSQL(Transito.CREATE_DB_TABLE);
        db.execSQL(Croquis.CREATE_DB_TABLE);
        db.execSQL(Imagen.CREATE_DB_TABLE);
    }


    public void dropDatabase(){
        db.execSQL("Drop table " + Actividad.TABLE_NAME);
        db.execSQL("Drop table " + Carretera.TABLE_NAME);
        db.execSQL("Drop table " + CategoriaEmergencia.TABLE_NAME);
        db.execSQL("Drop table " + CausaEmergencia.TABLE_NAME);
        db.execSQL("Drop table " + Concepto.TABLE_NAME);
        db.execSQL("Drop table " + Danos.TABLE_NAME);
        db.execSQL("Drop table " + Estado.TABLE_NAME);
        db.execSQL("Drop table " + Subtramo.TABLE_NAME);
        db.execSQL("Drop table " + TipoEmergencia.TABLE_NAME);
        db.execSQL("Drop table " + Emergencia.TABLE_NAME);
        db.execSQL("Drop table " + Tramo.TABLE_NAME);
        db.execSQL("Drop table " + Transito.TABLE_NAME);
        db.execSQL("Drop table " + Croquis.TABLE_NAME);
        db.execSQL("Drop table " + Imagen.TABLE_NAME);
    }

    public void resetDatabase(){
        dropDatabase();
        createTables();
    }
    //Actividad
    public Long insertarActividad(Actividad actividad){
        return db.insert(Actividad.TABLE_NAME, null, generarActividad(actividad));
    }

    public void updateActividadCCVE(int icve, int ccve ){
        ContentValues cv = new ContentValues();
        cv.put(Actividad.FIELD_CCVE, ccve);
        db.update(Actividad.TABLE_NAME, cv , "icve = " + icve, null);
    }
    private ContentValues generarActividad(Actividad actividad){
        ContentValues valores =  new ContentValues();
        valores.put(Actividad.FIELD_DESCRIPCION, actividad.getDescripcion());
        valores.put(Actividad.FIELD_ICVEEMERGENCIA, actividad.getIcveEmergencia());
        valores.put(Actividad.FIELD_TRANSITO, actividad.getTransito());
        valores.put(Actividad.FIELD_FECHACREACION, actividad.getFechaCreacion().toString());
        return valores;
    }

    public ArrayList<Actividad> findActividadesByIncidencia(int icveIncidencia){
        ArrayList<Actividad> actividades = new ArrayList<>();
        String query = "select * from Actividad as actividad where actividad.icveEmergencia = "+icveIncidencia+"";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Actividad actividad = new Actividad();
                    actividad.setIcve(c.getInt(0));
                    actividad.setCcve(c.getInt(2));
                    actividad.setDescripcion(c.getString(4));
                    actividad.setTransito(c.getInt(6));
                    actividad.setFechaCreacion(c.getString(7));
                    actividades.add(actividad);

                }while(c.moveToNext());
            }
            return actividades;
        }finally {
            c.close();
        }
    }


    //Concepto
    public Long insertarConcepto(Concepto concepto){
        return db.insert(Concepto.TABLE_NAME, null, generarConcepto(concepto));
    }

    public void updateConceptoCCVE(int icve, int ccve ){
        String query = "update Concepto  set ccve = " + ccve + " where icve = " + icve;
        ContentValues cv = new ContentValues();
        cv.put(Concepto.FIELD_CCVE, ccve);
        db.update(Concepto.TABLE_NAME, cv , "icve = " + icve, null);
    }
    private ContentValues generarConcepto(Concepto concepto){
        ContentValues valores =  new ContentValues();
        valores.put(Concepto.FIELD_CANTIDAD, concepto.getCantidad());
        valores.put(Concepto.FIELD_DESCRIPCION, concepto.getDescripcion());
        valores.put(Concepto.FIELD_ICVEEMERGENCIA, concepto.getIcveEmergencia());
        valores.put(Concepto.FIELD_ICVEUNIDAD, concepto.getIcveUnidad());
        valores.put(Concepto.FIELD_IPORAVANCE, concepto.getIporAvance());
        valores.put(Concepto.FIELD_FECHACREACION, concepto.getFechaCreacion());
        return valores;
    }

    public ArrayList<Concepto> findConceptoByIncidencia(int icveIncidencia){
        ArrayList<Concepto> conceptos = new ArrayList<>();
        String query = "select * from Concepto as concepto where concepto.icveEmergencia = "+icveIncidencia+"";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Concepto concepto = new Concepto();
                    concepto.setIcve(c.getInt(0));
                    concepto.setCantidad(c.getInt(1));
                    concepto.setCcve(c.getInt(2));

                    concepto.setDescripcion(c.getString(4));
                    concepto.setIcveEmergencia(c.getInt(5));
                    concepto.setIcveUnidad(c.getInt(6));
                    concepto.setIporAvance(c.getInt(7));
                    concepto.setFechaCreacion(c.getString(8));
                    conceptos.add(concepto);

                }while(c.moveToNext());
            }
            return conceptos;
        }finally {
            c.close();
        }
    }

    public ArrayList<Concepto> findConceptoByIncidenciaOrder(int icveIncidencia){
        ArrayList<Concepto> conceptos = new ArrayList<>();
        String query = "select  DISTINCT descripcion, icve, cantidad, ccve,  " +
                "icveemergencia, icveunidad, iporavance, fechacreacion " +
                "from Concepto where icveEmergencia = "+icveIncidencia+" ";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Concepto concepto = new Concepto();
                    concepto.setIcve(c.getInt(1));
                    concepto.setCantidad(c.getInt(2));
                    concepto.setCcve(c.getInt(3));

                    concepto.setDescripcion(c.getString(0));
                    concepto.setIcveEmergencia(c.getInt(4));
                    concepto.setIcveUnidad(c.getInt(5));
                    concepto.setIporAvance(c.getInt(6));
                    concepto.setFechaCreacion(c.getString(7));
                    conceptos.add(concepto);

                }while(c.moveToNext());
            }
            return conceptos;
        }finally {
            c.close();
        }
    }

    //Imagen
    public Long insertarImagen(Imagen imagen){
        return db.insert(Imagen.TABLE_NAME, null, generarImagen(imagen));
    }

    public void updateImagenCCVE(int icve, int ccve ){
        ContentValues cv = new ContentValues();
        cv.put(Imagen.FIELD_CCVE, ccve);
        db.update(Imagen.TABLE_NAME, cv , "icve = " + icve, null);
    }

    private ContentValues generarImagen(Imagen imagen){
        ContentValues valores =  new ContentValues();
        valores.put(Imagen.FIELD_ICVEEMERGENCIA, imagen.getIcveEmergencia());
        valores.put(Imagen.FIELD_URL, imagen.getUrl());
        valores.put(Imagen.FIELD_ICVECONCEPTO, imagen.getIcveConcepto());
        return valores;
    }

    public ArrayList<Imagen> findImagenConcepto(int icveConcepto){
        ArrayList<Imagen> imagenes = new ArrayList<>();
        String query = "select * from FotoEmergencia where icveConcepto = "+icveConcepto+"";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Imagen imagen = new Imagen();
                    imagen.setIcve(c.getInt(0));
                    imagen.setIcveEmergencia(c.getInt(1));
                    imagen.setUrl(c.getString(2));
                    imagen.setCcve(c.getInt(3));
                    imagen.setIcveConcepto(c.getInt(4));
                    imagenes.add(imagen);

                }while(c.moveToNext());
            }
            return imagenes;
        }finally {
            c.close();
        }
    }

    public ArrayList<Imagen> findImagenIncidencia(int icveIncidencia){
        ArrayList<Imagen> imagenes = new ArrayList<>();
        String query = "select * from FotoEmergencia where icveEmergencia = "+icveIncidencia+"";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Imagen imagen = new Imagen();
                    imagen.setIcve(c.getInt(0));
                    imagen.setIcveEmergencia(c.getInt(1));
                    imagen.setUrl(c.getString(2));
                    imagen.setCcve(c.getInt(3));
                    imagen.setIcveConcepto(c.getInt(4));
                    imagenes.add(imagen);

                }while(c.moveToNext());
            }
            return imagenes;
        }finally {
            c.close();
        }
    }



    //Croquis
    public Long insertarCroquis(Croquis croquis){
        return db.insert(Croquis.TABLE_NAME, null, generarCroquis(croquis));
    }

    public void updateCroquisCCVE(int icve, int ccve ){
        ContentValues cv = new ContentValues();
        cv.put(Croquis.FIELD_CCVE, ccve);
        db.update(Croquis.TABLE_NAME, cv , "icve = " + icve, null);
    }

    public List<Croquis> findCroquisByEmergencia(int icve){
        List<Croquis> croquises = new ArrayList<>();
        String query = "select * from Croquis where icveEmergencia  = "+icve;
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Croquis croquis = new Croquis();
                    croquis.setIcve(c.getInt(0));
                    croquis.setIcveEmergencia(c.getInt(1));
                    croquis.setUrl(c.getString(2));
                    croquis.setCcve(c.getInt(3));
                    croquises.add(croquis);
                }while(c.moveToNext());
            }
            return croquises;
        }catch(Exception d){
            d.printStackTrace();
            return null;
        }
        finally {
            c.close();
        }
    }

    private ContentValues generarCroquis(Croquis croquis){
        ContentValues valores =  new ContentValues();
        valores.put(Croquis.FIELD_ICVEEMERGENCIA, croquis.getIcveEmergencia());
        valores.put(Croquis.FIELD_URL, croquis.getUrl());
        return valores;
    }

    //Emergencia
    public Long insertarEmergencia(Emergencia emergencia){
        return db.insert(Emergencia.TABLE_NAME, null, generarEmergencia(emergencia));
    }

    public void updateIncidenciaCCVE(int icve, int ccve ){
        ContentValues cv = new ContentValues();
        cv.put(Emergencia.FIELD_CCVE, ccve);
        db.update(Emergencia.TABLE_NAME, cv , "icve = " + icve, null);
    }

    public void updateIncidenciaTransito(int icve, int transito, int autos, int camiones, int todoTipo ){
        ContentValues cv = new ContentValues();
        cv.put(Emergencia.FIELD_ICVETRANSITO, transito);
        cv.put(Emergencia.FIELD_VEHICULOSTRANSITANAUTOS, autos);
        cv.put(Emergencia.FIELD_VEHICULOSTRANSITANCAMIONES, camiones);
        cv.put(Emergencia.FIELD_VEHICULOSTRANSITANTODOTIPO, todoTipo);
        db.update(Emergencia.TABLE_NAME, cv , "icve = " + icve, null);
    }

    public void updateIncidenciaEstado(int icve, int icveEstado ){
        ContentValues cv = new ContentValues();
        cv.put(Emergencia.FIELD_ICVEESTADO, icveEstado);
        db.update(Emergencia.TABLE_NAME, cv , "icve = " + icve, null);
    }


    public ArrayList<Emergencia> findEmergencias(){
        ArrayList<Emergencia> emergencias = new ArrayList<>();
        String columnas[] = {Emergencia.FIELD_ICVE, Emergencia.FIELD_FECHACREACION, Emergencia.FIELD_DESCRIPCION, Emergencia.FIELD_LATITUD, Emergencia.FIELD_LONGITUD, Emergencia.FIELD_ALTITUD};
        Cursor c = db.query(Emergencia.TABLE_NAME, columnas, null, null, null, null, null);
        try{
            if(c. moveToFirst()){
                do{
                    Emergencia emergencia = new Emergencia();
                    emergencia.setIcve(c.getInt(0));
                    emergencia.setFechaCreacion(c.getString(1));
                    emergencia.setDescripcion(c.getString(2));
                    emergencia.setLatitud(c.getDouble(3));
                    emergencia.setLongitud(c.getDouble(4));
                    emergencia.setAltitud(c.getDouble(5));
                    emergencias.add(emergencia);

                }while(c.moveToNext());
            }
            return emergencias;
        }finally {
            c.close();
        }
    }

    public ArrayList<Emergencia> findIncidenciaById(int idIncidencia){
        ArrayList<Emergencia> emergencias = new ArrayList<>();
        String query = "select * from Emergencia  where  icve = "+idIncidencia+" ";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Emergencia emergencia = new Emergencia();

                    emergencia.setIcve(c.getInt(0));
                    emergencia.setAccionesRealizadas(c.getString(1));
                    if(c.getString(2) != null)
                        emergencia.setAltitud(Double.parseDouble(c.getString(2)));
                    if(c.getString(19) != null)
                        emergencia.setLatitud(Double.parseDouble(c.getString(19)));
                    if(c.getString(20) != null)
                        emergencia.setLongitud(Double.parseDouble(c.getString(20)));

                    emergencia.setCcve(c.getInt(3));
                    emergencia.setDescripcion(c.getString(4));
                    emergencia.setFechaCreacion(c.getString(5));
                    emergencia.setFechaDefinitiva(c.getString(6));
                    emergencia.setFechaProvisional(c.getString(7));
                    emergencia.setIcveCarretera(c.getInt(8));
                    emergencia.setIcveCategoriaEmergencia(c.getInt(9));
                    emergencia.setIcveCausaEmergencia(c.getInt(10));
                    emergencia.setIcveDanos(c.getInt(11));
                    emergencia.setIcveEstado(c.getInt(12));
                    emergencia.setIcveSubtramo(c.getInt(13));
                    emergencia.setIcveTipoEmergencia(c.getInt(14));
                    //emergencia.setIcveTramo(c.getInt(15));
                    emergencia.setIcveTransito(c.getInt(16));
                    emergencia.setKmInicial(c.getString(18));
                    emergencia.setKmFinal(c.getString(17));

                    emergencia.setOtras(c.getString(21));
                    emergencia.setRutaAlternativa(c.getInt(22));
                    emergencia.setTipoVehiculo(c.getInt(23));
                    emergencia.setVehiculosTransitanAutos(c.getInt(24));
                    emergencia.setVehiculosTransitanCamiones(c.getInt(25));
                    emergencia.setVehiculosTransitanTodoTipo(c.getInt(26));



                    emergencias.add(emergencia);

                }while(c.moveToNext());
            }
            return emergencias;
        }catch(Exception d){
            d.printStackTrace();
            return null;
        }
        finally {
            c.close();
        }

    }

    public ArrayList<Emergencia> findEmergenciasByEntidad(int idEntidad){
        ArrayList<Emergencia> emergencias = new ArrayList<>();
        String query = "select * from Emergencia  e where  e.icveSubtramo in (select s.icve from Subtramo s where s.icveEstado = "+idEntidad+" )";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Emergencia emergencia = new Emergencia();

                    emergencia.setIcve(c.getInt(0));
                    emergencia.setAccionesRealizadas(c.getString(1));
                    if(c.getString(2) != null)
                        emergencia.setAltitud(Double.parseDouble(c.getString(2)));
                    if(c.getString(19) != null)
                        emergencia.setLatitud(Double.parseDouble(c.getString(19)));
                    if(c.getString(20) != null)
                        emergencia.setLongitud(Double.parseDouble(c.getString(20)));

                    emergencia.setCcve(c.getInt(3));
                    emergencia.setDescripcion(c.getString(4));
                    emergencia.setFechaCreacion(c.getString(5));
                    emergencia.setFechaDefinitiva(c.getString(6));
                    emergencia.setFechaProvisional(c.getString(7));
                    emergencia.setIcveCarretera(c.getInt(8));
                    emergencia.setIcveCategoriaEmergencia(c.getInt(9));
                    emergencia.setIcveCausaEmergencia(c.getInt(10));
                    emergencia.setIcveDanos(c.getInt(11));
                    emergencia.setIcveEstado(c.getInt(12));
                    emergencia.setIcveSubtramo(c.getInt(13));
                    emergencia.setIcveTipoEmergencia(c.getInt(14));
                    //emergencia.setIcveTramo(c.getInt(15));
                    emergencia.setIcveTransito(c.getInt(16));
                    emergencia.setKmInicial(c.getString(18));
                    emergencia.setKmFinal(c.getString(17));

                    emergencia.setOtras(c.getString(21));
                    emergencia.setRutaAlternativa(c.getInt(22));
                    emergencia.setTipoVehiculo(c.getInt(23));
                    emergencia.setVehiculosTransitanAutos(c.getInt(24));
                    emergencia.setVehiculosTransitanCamiones(c.getInt(25));
                    emergencia.setVehiculosTransitanTodoTipo(c.getInt(26));



                    emergencias.add(emergencia);

                }while(c.moveToNext());
            }
            return emergencias;
        }catch(Exception d){
            d.printStackTrace();
            return null;
        }
        finally {
            c.close();
        }

    }

    public ArrayList<Emergencia> findAllEmergencias(){
        ArrayList<Emergencia> emergencias = new ArrayList<>();
        String query = "select * from Emergencia  ";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Emergencia emergencia = new Emergencia();

                    emergencia.setIcve(c.getInt(0));
                    emergencia.setAccionesRealizadas(c.getString(1));
                    if(c.getString(2) != null)
                        emergencia.setAltitud(Double.parseDouble(c.getString(2)));
                    if(c.getString(19) != null)
                        emergencia.setLatitud(Double.parseDouble(c.getString(19)));
                    if(c.getString(20) != null)
                        emergencia.setLongitud(Double.parseDouble(c.getString(20)));

                    emergencia.setCcve(c.getInt(3));
                    emergencia.setDescripcion(c.getString(4));
                    emergencia.setFechaCreacion(c.getString(5));
                    emergencia.setFechaDefinitiva(c.getString(6));
                    emergencia.setFechaProvisional(c.getString(7));
                    emergencia.setIcveCarretera(c.getInt(8));
                    emergencia.setIcveCategoriaEmergencia(c.getInt(9));
                    emergencia.setIcveCausaEmergencia(c.getInt(10));
                    emergencia.setIcveDanos(c.getInt(11));
                    emergencia.setIcveEstado(c.getInt(12));
                    emergencia.setIcveSubtramo(c.getInt(13));
                    emergencia.setIcveTipoEmergencia(c.getInt(14));
                    //emergencia.setIcveTramo(c.getInt(15));
                    emergencia.setIcveTransito(c.getInt(16));
                    emergencia.setKmInicial(c.getString(18));
                    emergencia.setKmFinal(c.getString(17));

                    emergencia.setOtras(c.getString(21));
                    emergencia.setRutaAlternativa(c.getInt(22));
                    emergencia.setTipoVehiculo(c.getInt(23));
                    emergencia.setVehiculosTransitanAutos(c.getInt(24));
                    emergencia.setVehiculosTransitanCamiones(c.getInt(25));
                    emergencia.setVehiculosTransitanTodoTipo(c.getInt(26));



                    emergencias.add(emergencia);

                }while(c.moveToNext());
            }
            return emergencias;
        }catch(Exception d){
            d.printStackTrace();
            return null;
        }
        finally {
            c.close();
        }

    }
    public ArrayList<Emergencia> findEmergenciasByCarretera(int icveCarretera){
        ArrayList<Emergencia> emergencias = new ArrayList<>();
        String query = "select * from Emergencia as emergencia where emergencia.icveCarretera = "+icveCarretera+"";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Emergencia emergencia = new Emergencia();
                    emergencia.setIcve(c.getInt(0));
                    emergencia.setAccionesRealizadas(c.getString(1));
                    if(c.getString(2) != null)
                        emergencia.setAltitud(Double.parseDouble(c.getString(2)));
                    if(c.getString(19) != null)
                        emergencia.setLatitud(Double.parseDouble(c.getString(19)));
                    if(c.getString(20) != null)
                        emergencia.setLongitud(Double.parseDouble(c.getString(20)));
                    emergencia.setCcve(c.getInt(3));
                    emergencia.setDescripcion(c.getString(4));
                    emergencia.setFechaCreacion(c.getString(5));
                    emergencia.setFechaDefinitiva(c.getString(6));
                    emergencia.setFechaProvisional(c.getString(7));
                    emergencia.setIcveCarretera(c.getInt(8));
                    emergencia.setIcveCategoriaEmergencia(c.getInt(9));
                    emergencia.setIcveCausaEmergencia(c.getInt(10));
                    emergencia.setIcveDanos(c.getInt(11));
                    emergencia.setIcveEstado(c.getInt(12));
                    emergencia.setIcveSubtramo(c.getInt(13));
                    emergencia.setIcveTipoEmergencia(c.getInt(14));
                    //emergencia.setIcveTramo(c.getInt(15));
                    emergencia.setIcveTransito(c.getInt(16));
                    emergencia.setKmInicial(c.getString(18));
                    emergencia.setKmFinal(c.getString(17));

                    emergencia.setOtras(c.getString(21));
                    emergencia.setRutaAlternativa(c.getInt(22));
                    emergencia.setTipoVehiculo(c.getInt(23));
                    emergencia.setVehiculosTransitanAutos(c.getInt(24));
                    emergencia.setVehiculosTransitanCamiones(c.getInt(25));
                    emergencia.setVehiculosTransitanTodoTipo(c.getInt(26));
                    emergencias.add(emergencia);
                }while(c.moveToNext());
            }
            return emergencias;
        }finally {
            c.close();
        }
    }

    public ArrayList<Emergencia> findEmergenciasByTramo(int icveTramo){
        ArrayList<Emergencia> emergencias = new ArrayList<>();
        String query = "select * from Emergencia as emergencia where emergencia.icveTramo = "+icveTramo+"";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Emergencia emergencia = new Emergencia();

                    emergencia.setIcve(c.getInt(0));
                    emergencia.setAccionesRealizadas(c.getString(1));
                    if(c.getString(2) != null)
                        emergencia.setAltitud(Double.parseDouble(c.getString(2)));
                    if(c.getString(19) != null)
                        emergencia.setLatitud(Double.parseDouble(c.getString(19)));
                    if(c.getString(20) != null)
                        emergencia.setLongitud(Double.parseDouble(c.getString(20)));

                    emergencia.setCcve(c.getInt(3));
                    emergencia.setDescripcion(c.getString(4));
                    emergencia.setFechaCreacion(c.getString(5));
                    emergencia.setFechaDefinitiva(c.getString(6));
                    emergencia.setFechaProvisional(c.getString(7));
                    emergencia.setIcveCarretera(c.getInt(8));
                    emergencia.setIcveCategoriaEmergencia(c.getInt(9));
                    emergencia.setIcveCausaEmergencia(c.getInt(10));
                    emergencia.setIcveDanos(c.getInt(11));
                    emergencia.setIcveEstado(c.getInt(12));
                    emergencia.setIcveSubtramo(c.getInt(13));
                    emergencia.setIcveTipoEmergencia(c.getInt(14));
                    //emergencia.setIcveTramo(c.getInt(15));
                    emergencia.setIcveTransito(c.getInt(16));
                    emergencia.setKmInicial(c.getString(18));
                    emergencia.setKmFinal(c.getString(17));

                    emergencia.setOtras(c.getString(21));
                    emergencia.setRutaAlternativa(c.getInt(22));
                    emergencia.setTipoVehiculo(c.getInt(23));
                    emergencia.setVehiculosTransitanAutos(c.getInt(24));
                    emergencia.setVehiculosTransitanCamiones(c.getInt(25));
                    emergencia.setVehiculosTransitanTodoTipo(c.getInt(26));
                    emergencias.add(emergencia);

                }while(c.moveToNext());
            }
            return emergencias;
        }finally {
            c.close();
        }
    }

    public ArrayList<Emergencia> findEmergenciasBySubtramo(int idSubtramo){
        ArrayList<Emergencia> emergencias = new ArrayList<>();
        String query = "select * from Emergencia as emergencia where emergencia.icveSubtramo = "+idSubtramo+"";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Emergencia emergencia = new Emergencia();

                    emergencia.setIcve(c.getInt(0));
                    emergencia.setAccionesRealizadas(c.getString(1));
                    if(c.getString(2) != null)
                        emergencia.setAltitud(Double.parseDouble(c.getString(2)));
                    if(c.getString(19) != null)
                        emergencia.setLatitud(Double.parseDouble(c.getString(19)));
                    if(c.getString(20) != null)
                        emergencia.setLongitud(Double.parseDouble(c.getString(20)));

                    emergencia.setCcve(c.getInt(3));
                    emergencia.setDescripcion(c.getString(4));
                    emergencia.setFechaCreacion(c.getString(5));
                    emergencia.setFechaDefinitiva(c.getString(6));
                    emergencia.setFechaProvisional(c.getString(7));
                    emergencia.setIcveCarretera(c.getInt(8));
                    emergencia.setIcveCategoriaEmergencia(c.getInt(9));
                    emergencia.setIcveCausaEmergencia(c.getInt(10));
                    emergencia.setIcveDanos(c.getInt(11));
                    emergencia.setIcveEstado(c.getInt(12));
                    emergencia.setIcveSubtramo(c.getInt(13));
                    emergencia.setIcveTipoEmergencia(c.getInt(14));
                    //emergencia.setIcveTramo(c.getInt(15));
                    emergencia.setIcveTransito(c.getInt(16));
                    emergencia.setKmInicial(c.getString(18));
                    emergencia.setKmFinal(c.getString(17));

                    emergencia.setOtras(c.getString(21));
                    emergencia.setRutaAlternativa(c.getInt(22));
                    emergencia.setTipoVehiculo(c.getInt(23));
                    emergencia.setVehiculosTransitanAutos(c.getInt(24));
                    emergencia.setVehiculosTransitanCamiones(c.getInt(25));
                    emergencia.setVehiculosTransitanTodoTipo(c.getInt(26));
                    emergencias.add(emergencia);

                }while(c.moveToNext());
            }
            return emergencias;
        }finally {
            c.close();
        }
    }

    private ContentValues generarEmergencia(Emergencia emergencia){
        ContentValues valores =  new ContentValues();
        valores.put(Emergencia.FIEL_ACCIONESREALIZADAS, emergencia.getAccionesRealizadas());
        valores.put(Emergencia.FIELD_ALTITUD, emergencia.getAltitud());
        valores.put(Emergencia.FIELD_CCVE, 0);
        valores.put(Emergencia.FIELD_DESCRIPCION, emergencia.getDescripcion());
        valores.put(Emergencia.FIELD_FECHACREACION, emergencia.getFechaCreacion());
        valores.put(Emergencia.FIELD_FECHADEFINITIVA, emergencia.getFechaDefinitiva());
        valores.put(Emergencia.FIELD_FECHAPROVISIONAL, emergencia.getFechaProvisional());
        valores.put(Emergencia.FIELD_ICVECARRETERA, emergencia.getIcveCarretera());
        valores.put(Emergencia.FIELD_ICVECATEGORIAEMERGENCIA, emergencia.getIcveCategoriaEmergencia());
        valores.put(Emergencia.FIELD_ICVECAUSAEMERGENCIA, emergencia.getIcveCausaEmergencia());
        valores.put(Emergencia.FIELD_ICVEDANOS, emergencia.getIcveDanos());
        valores.put(Emergencia.FIELD_ICVEESTADO, emergencia.getIcveEstado());
        valores.put(Emergencia.FIELD_ICVESUBTRAMO, emergencia.getIcveSubtramo());
        valores.put(Emergencia.FIELD_ICVETIPOEMERGENCIA, emergencia.getIcveTipoEmergencia());
        valores.put(Emergencia.FIELD_ICVETRAMO, emergencia.getIcveTramo());
        valores.put(Emergencia.FIELD_ICVETRANSITO, emergencia.getIcveTransito());
        valores.put(Emergencia.FIELD_KMFINAL, emergencia.getKmFinal());
        valores.put(Emergencia.FIELD_KMINICIAL, emergencia.getKmInicial());
        valores.put(Emergencia.FIELD_LATITUD, emergencia.getLatitud());
        valores.put(Emergencia.FIELD_LONGITUD, emergencia.getLongitud());
        // valores.put(Emergencia.FIELD_OTRAS, emergencia.);
        valores.put(Emergencia.FIELD_RUTAALTERNATIVA, emergencia.getRutaAlternativa());
        valores.put(Emergencia.FIELD_TIPOVEHICULO, emergencia.getTipoVehiculo());
        valores.put(Emergencia.FIELD_VEHICULOSTRANSITANAUTOS, emergencia.getVehiculosTransitanAutos());
        valores.put(Emergencia.FIELD_VEHICULOSTRANSITANCAMIONES, emergencia.getVehiculosTransitanCamiones());
        valores.put(Emergencia.FIELD_VEHICULOSTRANSITANTODOTIPO, emergencia.getVehiculosTransitanTodoTipo());

        return valores;
    }


    //Estado
    private ContentValues generarEstados(Estado estado){
        ContentValues valores =  new ContentValues();
        valores.put(Estado.FIELD_NOMBRE, estado.getNombre());
        return valores;
    }

    public ArrayList<Estado> findEstados(){
        ArrayList<Estado> estados = new ArrayList<>();
        String columnas[] = {Estado.FIELD_ICVE, Estado.FIELD_NOMBRE};
        Cursor c = db.query(Estado.TABLE_NAME, columnas, null, null, null, null, null);
        try{
            if(c. moveToFirst()){
                do{
                    Estado estado = new Estado();
                    estado.setIcve(c.getInt(0));
                    estado.setNombre(c.getString(1));
                    estados.add(estado);

                }while(c.moveToNext());
            }
            return estados;
        }finally {
            c.close();
        }
    }

    public void insertarEstado(Estado estado){
        db.insert(Estado.TABLE_NAME, null, generarEstados(estado));
    }

    //Carretera
    private ContentValues generarCarretera(Carretera carretera){
        ContentValues valores =  new ContentValues();
        valores.put(Carretera.FIELD_ICVE, carretera.getIcve());
        valores.put(Carretera.FIELD_NOMBRE, carretera.getNombre());
        return valores;
    }

    public void insertarCarretera(Carretera carretera){

        db.insert(Carretera.TABLE_NAME, null, generarCarretera(carretera));
    }

    public ArrayList<Carretera> findCarreteras(){
        ArrayList<Carretera> carreteras = new ArrayList<>();
        String columnas[] = {Carretera.FIELD_ICVE, Carretera.FIELD_NOMBRE};
        Cursor c = db.query(Carretera.TABLE_NAME, columnas, null, null, null, null, null);
        try{
            if(c. moveToFirst()){
                do{
                    Carretera carretera = new Carretera();
                    carretera.setIcve(c.getInt(0));
                    carretera.setNombre(c.getString(1));
                    carreteras.add(carretera);
                }while(c.moveToNext());
            }
            return carreteras;
        }finally {
            c.close();
        }
    }

    public ArrayList<Carretera> findCarreterasByNombre(String nombre){
        ArrayList<Carretera> carreteras = new ArrayList<>();
        String query = "select * from Carretera as carretera where carretera.nombre like'"+nombre+"'";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Carretera carretera = new Carretera();
                    carretera.setIcve(c.getInt(0));
                    carretera.setIcveEstado(c.getInt(1));
                    carretera.setNombre(c.getString(2));
                    carreteras.add(carretera);
                }while(c.moveToNext());
            }
            return carreteras;
        }finally {
            c.close();
        }
    }

    public ArrayList<Carretera> findCarreterasByEstado(String estado){
        ArrayList<Carretera> carreteras = new ArrayList<>();
        String columnas[] = {Carretera.FIELD_ICVE, Carretera.FIELD_NOMBRE};
        String query = "select DISTINCT carretera.nombre, carretera.icve from Estado as estado, Carretera as carretera, Tramo as tramo, Subtramo as subtramo " +
                "where (select icve from Estado where estado.nombre like'"+estado+"') = subtramo.icveEstado and " +
                "subtramo.icveTramo = tramo.icve and tramo.icveCarretera = carretera.icve ORDER BY carretera.nombre";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Carretera carretera = new Carretera();
                    carretera.setIcve(c.getInt(1));
                    carretera.setNombre(c.getString(0));
                    carreteras.add(carretera);
                }while(c.moveToNext());
            }
            return carreteras;
        }finally {
            c.close();
        }
    }


    //Tramos

    private ContentValues generarTramos(Tramo tramo){
        ContentValues valores =  new ContentValues();
        valores.put(Tramo.FIELD_ICVE, tramo.getIcve());
        valores.put(Tramo.FIELD_ICVECARRETERA, tramo.getIcveCarretera());
        valores.put(Tramo.FIELD_NOMBRE, tramo.getNombre());

        return valores;
    }

    public void insertarTramo(Tramo tramo){

        db.insert(Tramo.TABLE_NAME, null, generarTramos(tramo));
    }

    public ArrayList<Tramo> findTramos() {
        ArrayList<Tramo> tramos = new ArrayList<>();
        String columnas[] = {Tramo.FIELD_ICVE, Tramo.FIELD_ICVECARRETERA, Tramo.FIELD_NOMBRE};
        Cursor c = db.query(Tramo.TABLE_NAME, columnas, null, null, null, null, null);
        try{
            if (c.moveToFirst()) {
                do {
                    Tramo tramo = new Tramo();
                    tramo.setIcve(c.getInt(0));
                    tramo.setIcveCarretera(c.getInt(1));
                    tramo.setNombre(c.getString(2));
                    tramos.add(tramo);
                } while (c.moveToNext());
            }
            return tramos;
        }finally {
            c.close();
        }
    }
    public ArrayList<Tramo> findTramosByCarreteras(String estado, String carretera){
        ArrayList<Tramo> tramos = new ArrayList<>();
        String query = "select DISTINCT tramo.nombre, tramo.icve from Estado as estado, Carretera as carretera, Tramo as tramo, Subtramo as subtramo " +
                "where (select icve from Estado where estado.nombre like'"+estado+"') = subtramo.icveEstado and " +
                "subtramo.icveTramo = tramo.icve and tramo.icveCarretera = carretera.icve and carretera.nombre like'"+carretera+"' ORDER BY tramo.nombre";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Tramo tramo = new Tramo();
                    tramo.setNombre(c.getString(0));
                    tramo.setIcve(c.getInt(1));
                    tramos.add(tramo);
                }while(c.moveToNext());
            }
            return tramos;
        }finally {
            c.close();
        }
    }
    public ArrayList<Tramo> findTramoByCarreteraTramo(String nombreCarretera, String nombreTramo){
        ArrayList<Tramo> tramos = new ArrayList<>();
        String query = "select DISTINCT tramo.icve, tramo.icveCarretera, tramo.nombre from Carretera as carretera, Tramo as tramo " +
                "where  tramo.icveCarretera = (select carretera.icve from Carretera as carretera where carretera.nombre like'"+nombreCarretera+"') and tramo.nombre like'"+nombreTramo+"' ";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Tramo tramo = new Tramo();
                    tramo.setIcve(c.getInt(0));
                    tramo.setIcveCarretera(c.getInt(1));
                    tramo.setNombre(c.getString(2));
                    tramos.add(tramo);
                }while(c.moveToNext());
            }
            return tramos;
        }finally {
            c.close();
        }
    }
    //-Subtramos

    private ContentValues generarSubtramos(Subtramo subtramo){
        ContentValues valores =  new ContentValues();
        valores.put(Subtramo.FIELD_ICVE, subtramo.getIcve());
        valores.put(Subtramo.FIELD_ICVETRAMO, subtramo.getIcveTramo());
        valores.put(Subtramo.FIELD_ICVEESTADO, subtramo.getIcveEstado());
        valores.put(Subtramo.FIELD_KMINICIAL, subtramo.getKmInicial());
        valores.put(Subtramo.FIELD_KMFINAL, subtramo.getKmFinal());
        valores.put(Subtramo.FIELD_NOMBRE, subtramo.getNombre());


        return valores;
    }

    public void insertarSubtramo(Subtramo subtramo){

        db.insert(Subtramo.TABLE_NAME, null, generarSubtramos(subtramo));
    }

    public ArrayList<Subtramo> findSubtramos() {
        ArrayList<Subtramo> subtramos = new ArrayList<>();
        String columnas[] = {Subtramo.FIELD_ICVE, Subtramo.FIELD_ICVETRAMO, Subtramo.FIELD_ICVEESTADO, Subtramo.FIELD_KMINICIAL, Subtramo.FIELD_KMFINAL, Subtramo.FIELD_NOMBRE};
        Cursor c = db.query(Subtramo.TABLE_NAME, columnas, null, null, null, null, null);
        try {
            if (c.moveToFirst()) {
                do {
                    Subtramo subtramo = new Subtramo();
                    subtramo.setIcve(c.getInt(0));
                    subtramo.setIcveTramo(c.getInt(1));
                    subtramo.setIcveEstado(c.getInt(2));
                    subtramo.setKmInicial(c.getString(3));
                    subtramo.setKmFinal(c.getString(4));
                    subtramo.setNombre(c.getString(5));
                    subtramos.add(subtramo);
                } while (c.moveToNext());
            }
            return subtramos;
        }finally {
            c.close();
        }

    }

    public ArrayList<Subtramo> findSubtramosByTramo(String estado, String carretera, String tramo){
        ArrayList<Subtramo> subtramos = new ArrayList<>();
        String query = "select DISTINCT subtramo.icve, subtramo.icvetramo, subtramo.icveestado, subtramo.kminicial, subtramo.kmfinal, subtramo.nombre from Estado as estado, Carretera as carretera, Tramo as tramo, Subtramo as subtramo " +
                "where (select icve from Estado where estado.nombre like'"+estado+"') = subtramo.icveEstado and " +
                "subtramo.icveTramo = tramo.icve and tramo.icveCarretera = carretera.icve and carretera.nombre like'"+carretera+"' and tramo.nombre like'"+tramo+"' ORDER BY subtramo.nombre";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Subtramo subtramo = new Subtramo();
                    subtramo.setIcve(c.getInt(0));
                    subtramo.setIcveTramo(c.getInt(1));
                    subtramo.setIcveEstado(c.getInt(2));
                    subtramo.setKmInicial(c.getString(3));
                    subtramo.setKmFinal(c.getString(4));
                    subtramo.setNombre(c.getString(5));
                    subtramos.add(subtramo);
                }while(c.moveToNext());
            }
            return subtramos;
        }finally {
            c.close();
        }
    }





    public ArrayList<Subtramo> findSubtramosByNombre(String nombreSubtramo){
        ArrayList<Subtramo> subtramos = new ArrayList<>();
        String query = "select * from Subtramo where nombre like'"+nombreSubtramo+"'";
        Cursor c = db.rawQuery(query, null);
        try{
            if(c. moveToFirst()){
                do{
                    Subtramo subtramo = new Subtramo();
                    subtramo.setIcve(c.getInt(0));
                    subtramo.setIcveTramo(c.getInt(1));
                    subtramo.setIcveEstado(c.getInt(2));
                    subtramo.setKmInicial(c.getString(3));
                    subtramo.setKmFinal(c.getString(4));
                    subtramo.setNombre(c.getString(5));
                    subtramos.add(subtramo);
                }while(c.moveToNext());
            }
            return subtramos;
        }finally {
            c.close();
        }
    }

    //-Danos causados

    private ContentValues generarDanosCausados(Danos danos){
        ContentValues valores =  new ContentValues();
        valores.put(Danos.FIELD_ICVE, danos.getIcve());
        valores.put(Danos.FIELD_NOMBRE, danos.getNombre());
        return valores;
    }

    public void insertarDanoCausado(Danos danoCausado){

        db.insert(Danos.TABLE_NAME, null, generarDanosCausados(danoCausado));
    }

    public ArrayList<Danos> findDanosCausados() {
        ArrayList<Danos> danosCausados = new ArrayList<>();
        String columnas[] = {Danos.FIELD_ICVE, Danos.FIELD_NOMBRE};
        Cursor c = db.query(Danos.TABLE_NAME, columnas, null, null, null, null, null);
        try {
            if (c.moveToFirst()) {
                do {
                    Danos dano = new Danos();
                    dano.setIcve(c.getInt(0));
                    dano.setNombre(c.getString(1));
                    danosCausados.add(dano);
                } while (c.moveToNext());
            }
            return danosCausados;
        }finally {
            c.close();
        }

    }

    //Causa

    private ContentValues generarCausaEmergencia(CausaEmergencia causaEmergencia){
        ContentValues valores =  new ContentValues();
        valores.put(CausaEmergencia.FIELD_ICVE, causaEmergencia.getIcve());
        valores.put(CausaEmergencia.FIELD_NOMBRE, causaEmergencia.getNombre());
        return valores;
    }

    public void insertarCausaEmergencia(CausaEmergencia causaEmergencia){

        db.insert(CausaEmergencia.TABLE_NAME, null, generarCausaEmergencia(causaEmergencia));
    }

    public ArrayList<CausaEmergencia> findCausasEmergencia() {
        ArrayList<CausaEmergencia> causasEmergencia = new ArrayList<>();
        String columnas[] = {CausaEmergencia.FIELD_ICVE, CausaEmergencia.FIELD_NOMBRE};
        Cursor c = db.query(CausaEmergencia.TABLE_NAME, columnas, null, null, null, null, null);
        try {
            if (c.moveToFirst()) {
                do {
                    CausaEmergencia causa = new CausaEmergencia();
                    causa.setIcve(c.getInt(0));
                    causa.setNombre(c.getString(1));
                    causasEmergencia.add(causa);
                } while (c.moveToNext());
            }
            return causasEmergencia;
        }finally {
            c.close();
        }

    }

    //Tipo Emergencia

    private ContentValues generarTipoEmergencia(TipoEmergencia tipoEmergencia){
        ContentValues valores =  new ContentValues();
        valores.put(TipoEmergencia.FIELD_ICVE, tipoEmergencia.getIcve());
        valores.put(TipoEmergencia.FIELD_ICVECAUSAEMERGENCIA, tipoEmergencia.getIcveCausaEmergencia());
        valores.put(TipoEmergencia.FIELD_NOMBRE, tipoEmergencia.getNombre());
        return valores;
    }

    public void insertarTipoEmergencia(TipoEmergencia tipoEmergencia){

        db.insert(TipoEmergencia.TABLE_NAME, null, generarTipoEmergencia(tipoEmergencia));
    }

    public ArrayList<TipoEmergencia> findTiposEmergencia() {
        ArrayList<TipoEmergencia> tiposEmergencia = new ArrayList<>();
        String columnas[] = {TipoEmergencia.FIELD_ICVE, TipoEmergencia.FIELD_ICVECAUSAEMERGENCIA, TipoEmergencia.FIELD_NOMBRE};
        Cursor c = db.query(TipoEmergencia.TABLE_NAME, columnas, null, null, null, null, null);
        try {
            if (c.moveToFirst()) {
                do {
                    TipoEmergencia tipoEmergencia = new TipoEmergencia();
                    tipoEmergencia.setIcve(c.getInt(0));
                    tipoEmergencia.setIcveCausaEmergencia(1);
                    tipoEmergencia.setNombre(c.getString(2));
                    tiposEmergencia.add(tipoEmergencia);
                } while (c.moveToNext());
            }
            return tiposEmergencia;
        }finally {
            c.close();
        }

    }

    public ArrayList<TipoEmergencia> findTiposEmergenciaByCausa(String nombreCausa) {
        ArrayList<TipoEmergencia> tiposEmergencia = new ArrayList<>();
        String query = "select DISTINCT tipo.nombre from TipoEmergencia as tipo, CausaEmergencia as causa where (select icve from CausaEmergencia where nombre like'"+nombreCausa+"' ) = tipo.icveCausaEmergencia ORDER BY tipo.nombre";
        Cursor c = db.rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {
                    TipoEmergencia tipoEmergencia = new TipoEmergencia();
                    tipoEmergencia.setNombre(c.getString(0));
                    tiposEmergencia.add(tipoEmergencia);
                } while (c.moveToNext());
            }
            return tiposEmergencia;
        }finally {
            c.close();
        }

    }

    public ArrayList<TipoEmergencia> findTipoEmergenciaByNombre(String nombreTipo) {
        ArrayList<TipoEmergencia> tiposEmergencia = new ArrayList<>();
        String query = "select * from TipoEmergencia as tipo where nombre like'"+nombreTipo+"' ";
        Cursor c = db.rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {
                    TipoEmergencia tipoEmergencia = new TipoEmergencia();
                    tipoEmergencia.setIcve(c.getInt(0));
                    tipoEmergencia.setIcveCausaEmergencia(c.getInt(1));
                    tipoEmergencia.setNombre(c.getString(2));
                    tiposEmergencia.add(tipoEmergencia);
                } while (c.moveToNext());
            }
            return tiposEmergencia;
        }finally {
            c.close();
        }

    }

    //Categoria

    private ContentValues generarCategoria(CategoriaEmergencia categoriaEmergencia){
        ContentValues valores =  new ContentValues();
        valores.put(CategoriaEmergencia.FIELD_ICVE, categoriaEmergencia.getIcve());
        valores.put(CategoriaEmergencia.FIELD_ICVETIPOEMERGENCIA, categoriaEmergencia.getIcveTipoEmergencia());
        valores.put(CategoriaEmergencia.FIELD_NOMBRE, categoriaEmergencia.getNombre());
        return valores;
    }

    public void insertarCategoriaEmergencia(CategoriaEmergencia categoriaEmergencia){

        db.insert(categoriaEmergencia.TABLE_NAME, null, generarCategoria(categoriaEmergencia));
    }

    public ArrayList<CategoriaEmergencia> findCategoriaEmergencia() {
        ArrayList<CategoriaEmergencia> categoriasEmergencia = new ArrayList<>();
        String columnas[] = {CategoriaEmergencia.FIELD_ICVE, CategoriaEmergencia.FIELD_ICVETIPOEMERGENCIA, CategoriaEmergencia.FIELD_NOMBRE};
        Cursor c = db.query(CategoriaEmergencia.TABLE_NAME, columnas, null, null, null, null, null);
        try {
            if (c.moveToFirst()) {
                do {
                    CategoriaEmergencia categoriaEmergencia = new CategoriaEmergencia();
                    categoriaEmergencia.setIcve(c.getInt(0));
                    categoriaEmergencia.setIcveTipoEmergencia(c.getInt(1));
                    categoriaEmergencia.setNombre(c.getString(2));
                    categoriasEmergencia.add(categoriaEmergencia);
                } while (c.moveToNext());
            }
            return categoriasEmergencia;
        }finally {
            c.close();
        }

    }

    public ArrayList<CategoriaEmergencia> findCategoriaEmergenciaByTipo(String nombreTipo) {
        ArrayList<CategoriaEmergencia> categoriasEmergencia = new ArrayList<>();
        String query = "select DISTINCT categoria.nombre from CategoriaEmergencia as categoria, TipoEmergencia as tipo where (select icve from TipoEmergencia where nombre like'"+nombreTipo+"' ) = categoria.icveTipoEmergencia";
        Cursor c = db.rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {
                    CategoriaEmergencia categoriaEmergencia = new CategoriaEmergencia();
                    categoriaEmergencia.setNombre(c.getString(0));
                    categoriasEmergencia.add(categoriaEmergencia);
                } while (c.moveToNext());
            }
            return categoriasEmergencia;
        }finally {
            c.close();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
