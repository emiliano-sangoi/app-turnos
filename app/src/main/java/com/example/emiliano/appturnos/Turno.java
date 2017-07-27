package com.example.emiliano.appturnos;

import java.io.Serializable;

/**
 * Created by emiliano on 14/07/17.
 */

public class Turno implements Serializable{

    private Integer id_turno;
    private Integer paciente_id;
    private Integer horario_atencion_id;
    private Integer obra_social_id;
    private String obra_social_nombre;
    private Medico medico;
    //private Integer medico_especialidad_id;
    //private String medico_especialidad_nombre;
    private Especialidad especialidad;
    private Integer sanatorio_id;
    private String sanatorio_nombre;
    private String sanatorio_direccion;
    private String sanatorio_telefono;
    private Integer fecha_hora_ini;
    private Integer fecha_hora_fin;

    public Turno() {
        this.especialidad = null;

    }

    public Integer getId() {
        return id_turno;
    }

    public void setId(Integer id_turno) {
        this.id_turno = id_turno;
    }

    public Integer getPacienteId() {
        return paciente_id;
    }

    public void setPacienteId(Integer paciente_id) {
        this.paciente_id = paciente_id;
    }

    public Integer getHorarioAtencionId() {
        return horario_atencion_id;
    }

    public void setHorarioAtencionId(Integer horario_atencion_id) {
        this.horario_atencion_id = horario_atencion_id;
    }

    public Integer getObraSocialId() {
        return obra_social_id;
    }

    public void setObraSocialId(Integer obra_social_id) {
        this.obra_social_id = obra_social_id;
    }

    public String getObraSocialNombre() {
        return obra_social_nombre;
    }

    public void setObraSocialNombre(String obra_social_nombre) {
        this.obra_social_nombre = obra_social_nombre;
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

    /*
    public Integer getMedicoEspecialidadId() {
        return medico_especialidad_id;
    }

    public void setMedicoEspecialidadId(Integer medico_especialidad_id) {
        this.medico_especialidad_id = medico_especialidad_id;
    }

    public String getMedicoEspecialidadNombre() {
        return medico_especialidad_nombre;
    }

    public void setMedicoEspecialidadNombre(String medico_especialidad_nombre) {
        this.medico_especialidad_nombre = medico_especialidad_nombre;
    }*/

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
}
