package com.example.restauranteexpresssv.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restauranteexpresssv.entities.Pedido;

import java.util.List;

@Dao
public interface PedidoDao {
    @Insert
    void insertar(Pedido pedido);

    @Update
    void actualizar(Pedido pedido);

    @Update
    void eliminar(Pedido pedido);

    //Ordena  todos los pedidos, el mas reciente primero
    @Query("SELECT * FROM pedidos ORDER BY id DESC")
    List<Pedido> obtenerTodos();

    //Contar Pedidos totales
    @Query("SELECT COUNT(*) FROM pedidos ")
    int contarPedidos();

    //Contar los Pedidos Pendientes
    @Query("SELECT COUNT(*) FROM pedidos WHERE estado = 'Pendiente'")
    int contarPendientes();

    //Suma total de lo vendido, solo los pedidos entregados
    @Query("SELECT COALESCE(SUM(total), 0) FROM pedidos WHERE estado = 'Entregado' ")
            double totalVendido();

}
