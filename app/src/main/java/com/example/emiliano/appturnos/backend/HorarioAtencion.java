package com.example.emiliano.appturnos.backend;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by emi88 on 12/12/17.
 */

public class HorarioAtencion implements Serializable {

    private int id_horario_atencion;
    private String nro_matricula;
    private int id_usuario;
    private String apellidos;
    private String nombres;
    private String fecha_hora_ini;
    private String fecha_hora_fin;
    private String sanatorio_nombre;
    private String sanatorio_direccion;
    private String sanatorio_web;
    private String sanatorio_telefono;

    private SimpleDateFormat formatter;


    public HorarioAtencion() {

        this.formatter = new SimpleDateFormat("y-M-d H:m:s");

    }

    public int getIdHorarioAtencion() {
        return id_horario_atencion;
    }

    public void setIdHorarioAtencion(int id_horario_atencion) {
        this.id_horario_atencion = id_horario_atencion;
    }

    public String getNroMatricula() {
        return nro_matricula;
    }

    public void setNroMatricula(String nro_matricula) {
        this.nro_matricula = nro_matricula;
    }

    public int getIdUsuario() {
        return id_usuario;
    }

    public void setIdUsuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getFechaHoraIni() {
        return fecha_hora_ini;
    }

    public Date getFechaHoraIniAsDate() {

        try {
            Date date = formatter.parse(this.fecha_hora_ini);
            return  date;
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return null;
    }

    public Calendar getFechaHoraIniAsCalendar(){
        return getFechaAsCalendar( getFechaHoraIniAsDate() );
    }

    /**
     * Devuelve el tiempo restante que falta para el turno.
     *
     * @return
     */
    public Long getTiempoRestanteEnMilis(){
        Calendar calendarNow = Calendar.getInstance();
        Calendar horaIniCalendar = getFechaHoraIniAsCalendar();

        long dif = horaIniCalendar.getTimeInMillis() - calendarNow.getTimeInMillis();

        return new Long(dif);
    }

    public Long getTiempoRestanteEnDias(){
        Calendar calendarNow = Calendar.getInstance();
        Calendar horaIniCalendar = getFechaHoraIniAsCalendar();

        long dif = horaIniCalendar.getTimeInMillis() - calendarNow.getTimeInMillis();
        long difDias = TimeUnit.MILLISECONDS.toDays( dif );

        return difDias;
    }

    public void setFechaHoraIni(String fecha_hora_ini) {
        this.fecha_hora_ini = fecha_hora_ini;
    }

    public String getFechaHoraFin() {
        return fecha_hora_fin;
    }

    public Date getFechaHoraFinAsDate() {
        try {
            Date date = formatter.parse(this.fecha_hora_fin);
            return  date;
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return null;
    }

    public Calendar getFechaHoraFinAsCalendar(){
        return getFechaAsCalendar( getFechaHoraFinAsDate() );
    }

    private Calendar getFechaAsCalendar(Date fechaAsDate){
        Calendar fecha = Calendar.getInstance();
        fecha.setTime( fechaAsDate );
        return fecha;
    }


    public void setFechaHoraFin(String fecha_hora_fin) {
        this.fecha_hora_fin = fecha_hora_fin;
    }

    public String getSanatorioNombre() {
        return sanatorio_nombre;
    }

    public void setSanatorioNombre(String sanatorio_nombre) {
        this.sanatorio_nombre = sanatorio_nombre;
    }

    public String getSanatorioDireccion() {
        return sanatorio_direccion;
    }

    public void setSanatorioDireccion(String sanatorio_direccion) {
        this.sanatorio_direccion = sanatorio_direccion;
    }

    public String getSanatorioWeb() {
        return sanatorio_web;
    }

    public void setSanatorioWeb(String sanatorio_web) {
        this.sanatorio_web = sanatorio_web;
    }

    public String getSanatorioTelefono() {
        return sanatorio_telefono;
    }

    public void setSanatorioTelefono(String sanatorio_telefono) {
        this.sanatorio_telefono = sanatorio_telefono;
    }

    @Override
    public String toString() {

        Date horaIni = getFechaHoraIniAsDate();
        if(horaIni != null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy H:mm");
            String strFechaTurno = simpleDateFormat.format(horaIni);
            return strFechaTurno + " Hs.";
        }

        return super.toString();
    }
}
