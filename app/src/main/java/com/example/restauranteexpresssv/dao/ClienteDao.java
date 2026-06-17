package com.example.restauranteexpresssv.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restauranteexpresssv.entities.Cliente;

import java.util.List;

@Dao
public interface ClienteDao {
    @Insert
    void insertar (Cliente cliente);

    @Update
    void actualizar (Cliente cliente);

    @Delete
    void eliminar (Cliente cliente);

    //Orden de clientes por nombre A - Z
    @Query("SELECT * FROM clientes WHERE nombre LIKE '%' || :texto || '%' ORDER BY nombre ASC ")
    List<Cliente>buscarPorNombre(String texto);

    //Recuento total de clientes para Dashboard
    @Query("SELECT COUNT(*) FROM clientes")
    int contarClientes();


}
