package com.example.emiliano.appturnos;

/**
 * Created by emiliano on 13/07/17.
 */

public class Especialidad {

    private String id_especialidad;

    private String nom_especialidad;


    private String desc_especialidad;

    public Especialidad(String id_especialidad, String nom_especialidad, String desc_especialidad) {
        this.id_especialidad = id_especialidad;
        this.nom_especialidad = nom_especialidad;
        this.desc_especialidad = desc_especialidad;
    }

    public String getId_especialidad() {
        return id_especialidad;
    }

    public void setId_especialidad(String id_especialidad) {
        this.id_especialidad = id_especialidad;
    }

    public String getNom_especialidad() {
        return nom_especialidad;
    }

    public void setNom_especialidad(String nom_especialidad) {
        this.nom_especialidad = nom_especialidad;
    }

    public String getDesc_especialidad() {
        return desc_especialidad;
    }

    public void setDesc_especialidad(String desc_especialidad) {
        this.desc_especialidad = desc_especialidad;
    }
}
