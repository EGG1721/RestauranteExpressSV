package com.example.restauranteexpresssv.dao;

import static android.icu.text.MessagePattern.ArgType.SELECT;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.restauranteexpresssv.entities.Usuario;

@Dao
public interface UsuarioDao {
    //Inserta un usuario nuevo en la tabla
    @Insert
    void insertar(Usuario usuario);

    //Para buscar usuario por correo
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    Usuario buscarPorCorreo(String correo);

    //Valida el correo y el login
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND password = :password LIMIT 1")
    Usuario login (String correo, String password);
}
