package com.example.emiliano.appturnos.backend;

import java.io.Serializable;

/**
 * Created by emiliano on 05/07/17.
 */

public class Usuario implements Serializable {

    private Integer id_usuario;
    private Integer id_paciente;
    private String username;
    private String password;
    private String apellidos;
    private String nombres;
    private Integer tipo_doc;
    private Integer nro_doc;
    private String email;
    private String direccion;
    private String fecha_nac;

    private transient boolean logueado;

    public Usuario(Integer id_usuario, Integer id_paciente, String username, String password, String apellidos, String nombres, Integer tipo_doc, Integer nro_doc, String email, String direccion, String fecha_nac) {
        this.id_usuario = id_usuario;
        this.id_paciente = id_paciente;
        this.username = username;
        this.password = password;
        this.apellidos = apellidos;
        this.nombres = nombres;
        this.tipo_doc = tipo_doc;
        this.nro_doc = nro_doc;
        this.email = email;
        this.direccion = direccion;
        this.fecha_nac = fecha_nac;
    }

    public Usuario() {

    }


    public int getIdUsuario() {
        return id_usuario;
    }

    public void setIdUsuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getIdPaciente() {
        return id_paciente;
    }

    public void setIdPaciente(int id_paciente) {
        this.id_paciente = id_paciente;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isLogueado() {
        return logueado;
    }

    public void setLogueado(boolean logueado) {
        this.logueado = logueado;
    }

    public int getTipoDoc() {
        return tipo_doc;
    }

    public void setTipoDoc(int tipo_doc) {
        this.tipo_doc = tipo_doc;
    }

    public int getNroDoc() {
        return nro_doc;
    }

    public void setNroDoc(int nro_doc) {
        this.nro_doc = nro_doc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFechaNac() {
        return fecha_nac;
    }

    public void setFechaNac(String fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public boolean esPaciente(){
        return this.id_paciente != null;
    }
}
