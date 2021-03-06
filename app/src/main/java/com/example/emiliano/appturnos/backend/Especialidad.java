package com.example.emiliano.appturnos.backend;

import java.io.Serializable;

/**
 * Created by emiliano on 13/07/17.
 */

public class Especialidad implements Serializable {

    private Integer id_especialidad;

    private String nom_especialidad;

    private transient String desc_especialidad;

    public Especialidad(Integer id_especialidad, String nom_especialidad, String desc_especialidad) {
        this.id_especialidad = id_especialidad;
        this.nom_especialidad = nom_especialidad;
        this.desc_especialidad = desc_especialidad;
    }

    public Especialidad() {
        this.id_especialidad = null;
        this.nom_especialidad = null;
        this.desc_especialidad = null;
    }


    public Integer getIdEspecialidad() {
        return id_especialidad;
    }

    public void setIdEspecialidad(Integer id_especialidad) {
        this.id_especialidad = id_especialidad;
    }


    public String getNomEspecialidad() {
        /*
            La api devuelve los nombres codificados en UTF-8 Y Java internamente usa UTF-16 (ISO-8859-1),
            por esta razon se los debe convertir.
         */
        String out = null;
        try {
            out = new String(nom_especialidad.getBytes("ISO-8859-1"), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return "";
        }
        return out;
    }

    public void setNomEspecialidad(String nom_especialidad) {
        this.nom_especialidad = nom_especialidad;
    }

    public String getDescEspecialidad() {
        return desc_especialidad;
    }

    public void setDescEspecialidad(String desc_especialidad) {
        this.desc_especialidad = desc_especialidad;
    }

    @Override
    public String toString() {
        return this.getNomEspecialidad().toUpperCase();
    }


}
