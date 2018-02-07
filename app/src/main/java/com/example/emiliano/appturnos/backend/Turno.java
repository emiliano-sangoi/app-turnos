package com.example.emiliano.appturnos.backend;

import android.support.annotation.NonNull;

import com.example.emiliano.appturnos.backend.Especialidad;
import com.example.emiliano.appturnos.backend.Medico;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by emiliano on 14/07/17.
 */

public class Turno implements Serializable{

    private Integer id_turno;
    private Usuario usuario;
    private Afiliacion afiliacion;
    private Medico medico;
    private Especialidad especialidad;
    private HorarioAtencion horarioAtencion;
    private String fecha_cancelacion;

    private Integer sanatorio_id;
    private String sanatorio_nombre;
    private String sanatorio_direccion;
    private String sanatorio_telefono;
    private Integer fecha_hora_ini;
    private Integer fecha_hora_fin;

    public Turno() {
        this.especialidad = null;

    }


    public Map<String, String> getHttpPostParams(){
        Map<String, String> params = new HashMap<String, String>();

        params.put("paciente_id", Integer.toString(usuario.getIdPaciente()));
        params.put("obra_social_id", afiliacion != null ? Integer.toString(afiliacion.getIdOs()) : null);
        params.put("horario_atencion_id", Integer.toString(horarioAtencion.getIdHorarioAtencion()));

        return params;
    }

    /**
     * Debuelve true si el turno ya se encuentra finalizado.
     *
     * @return
     */
    public boolean isFinalizado(){
        Long dif = this.getHorarioAtencion().getTiempoRestanteEnMilis();
        return dif <=0 ;
    }

    /**
     * True si el turno fue cancelado por el usuario.
     *
     * @return
     */
    public boolean isCancelado(){
        return this.fecha_cancelacion != null;
    }

    public Integer getId() {
        return id_turno;
    }

    public void setId(Integer id_turno) {
        this.id_turno = id_turno;
    }

    public Afiliacion getAfiliacion() {
        return afiliacion;
    }

    public void setAfiliacion(Afiliacion afiliacion) {
        this.afiliacion = afiliacion;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Integer getSanatorioId() {
        return sanatorio_id;
    }

    public void setSanatorioId(Integer sanatorio_id) {
        this.sanatorio_id = sanatorio_id;
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

    public String getSanatorioTelefono() {
        return sanatorio_telefono;
    }

    public void setSanatorioTelefono(String sanatorio_telefono) {
        this.sanatorio_telefono = sanatorio_telefono;
    }

    public Integer getFechaHoraIni() {
        return fecha_hora_ini;
    }

    public void setFechaHoraIni(Integer fecha_hora_ini) {
        this.fecha_hora_ini = fecha_hora_ini;
    }

    public Integer getFechaHoraFin() {
        return fecha_hora_fin;
    }

    public void setFechaHoraFin(Integer fecha_hora_fin) {
        this.fecha_hora_fin = fecha_hora_fin;
    }

    public HorarioAtencion getHorarioAtencion() {
        return horarioAtencion;
    }

    public void setHorarioAtencion(HorarioAtencion horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }

    public String getFechaCancelacion() {
        return fecha_cancelacion;
    }

    public void setFechaCancelacion(String fechaCancelacion) {
        this.fecha_cancelacion = fechaCancelacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public static Comparator<Turno> estadoCanceladoComparator = new Comparator<Turno>() {
        @Override
        public int compare(Turno o1, Turno o2) {
            boolean o1IsC = o1.isCancelado();
            boolean o2IsC = o2.isCancelado();

            if( ! (o1IsC ^ o2IsC) ){
                return 0;
            }else if(o2IsC){
                return 1;
            }else{
                return -1;
            }
        }
    };

    public static Comparator<Turno> estadoFinalizadoComparator = new Comparator<Turno>() {
        @Override
        public int compare(Turno o1, Turno o2) {

            boolean o1IsF = o1.isFinalizado();
            boolean o2IsF = o2.isFinalizado();

            if( ! (o1IsF ^ o2IsF) ){
                return 0;
            }else if(o2IsF){
                return 1;
            }else{
                return -1;
            }

        }
    };

    public static Comparator<Turno> tiempoRestanteComparator = new Comparator<Turno>() {
        @Override
        public int compare(Turno o1, Turno o2) {

            long t1 = o1.getHorarioAtencion().getTiempoRestanteEnMilis();
            long t2 = o2.getHorarioAtencion().getTiempoRestanteEnMilis();

            return t1 > t2 ? 1 : (t1 == t2 ? 0 : -1);

        }
    };


}
