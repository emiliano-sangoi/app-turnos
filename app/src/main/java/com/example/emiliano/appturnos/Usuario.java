package com.example.emiliano.appturnos;

import java.io.Serializable;

/**
 * Created by emiliano on 05/07/17.
 */

public class Usuario implements Serializable {

    private int id_usuario;
    private String username;
    private String password;
    private String apellidos;
    private String nombres;
    private boolean logueado;

    public Usuario(int id_usuario, String username, String password, String apellidos, String nombres) {
        this.id_usuario = id_usuario;
        this.username = username;
        this.password = password;
        this.apellidos = apellidos;
        this.nombres = nombres;
        this.logueado = false;
    }

    public Usuario() {

    }


    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
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
}
