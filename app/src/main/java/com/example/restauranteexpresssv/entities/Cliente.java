package com.example.restauranteexpresssv.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "clientes")
public class Cliente {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nombre")
    private String nombre;
    @ColumnInfo(name = "telefono")
    private String telefono;
    @ColumnInfo(name = "direccion")
    private String direccion;
    @ColumnInfo(name = "referencia")
    private String referencia;
    @ColumnInfo(name = "municipio")
    private String municipio;

    //Constructor
    public Cliente(String nombre, String telefono, String direccion, String referencia, String municipio) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.referencia = referencia;
        this.municipio = municipio;
    }

    //Getters y Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
}

