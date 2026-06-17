package com.example.restauranteexpresssv.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pedidos")
public class Pedido {
    @PrimaryKey(autoGenerate = true)
    private int id;
    //Clave foranea vincula el pedido con el cliente
    @ColumnInfo(name = "clienteId")
    private int clienteId;

    //Guarda el nombre para mostrarlo sin JOIN adicional
    @ColumnInfo(name = "clienteNombre")
    private String clienteNombre;

    @ColumnInfo(name = "producto")
    private String producto;

    @ColumnInfo(name = "cantidad")
    private int cantidad;

    @ColumnInfo(name = "precioUnitario")
    private double precioUnitario;

    @ColumnInfo(name = "total")
    private double total;

    @ColumnInfo(name = "modalidad")
    private String modalidad;

    //Estados de por lo que pasa
    @ColumnInfo(name = "estado")
    private String estado;
    @ColumnInfo(name = "fecha")
    private String fecha;
    @ColumnInfo(name = "observaciones")
    private String observaciones;

    //Constructor
    public Pedido(int clienteId, String clienteNombre, String producto,
    int cantidad, double precioUnitario, double total, String modalidad, String estado,
                  String fecha, String observaciones) {
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.total = total;
        this.modalidad = modalidad;
        this.estado = estado;
        this.fecha = fecha;
        this.observaciones = observaciones;
    }
    //Getters y Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getTotal() {
        return total;
    }

    public String getModalidad() {
        return modalidad;
    }

    public String getEstado() {
        return estado;
    }

    public String getFecha() {
        return fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }


}
