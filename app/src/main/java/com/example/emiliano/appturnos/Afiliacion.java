package com.example.emiliano.appturnos;

import java.lang.ref.SoftReference;
import java.util.Date;

/**
 * Created by emiliano on 11/07/17.
 */

public class Afiliacion{

    private int idOs;
    private String nombre;
    private String codOs;
    private String direccion;
    private String telefono;
    private String fechaIni;
    private int idPaciente;

    public Afiliacion(int idOs, String nombre, String codeOs, String direccion, String telefono, String fechaIni, int idPaciente) {
        this.idOs = idOs;
        this.nombre = nombre;
        this.codOs = codeOs;
        this.direccion = direccion;
        this.telefono = telefono;
        this.fechaIni = fechaIni;
        this.idPaciente = idPaciente;
    }

    public int getIdOs() {
        return idOs;
    }

    public void setIdOs(int idOs) {
        this.idOs = idOs;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodOs() {
        return codOs;
    }

    public void setCodOs(String codOs) {
        this.codOs = codOs;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(String fechaIni) {
        this.fechaIni = fechaIni;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    @Override
    public String toString() {
        return nombre + " - " + codOs;
    }
}
