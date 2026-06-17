package com.example.restauranteexpresssv.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.restauranteexpresssv.dao.ClienteDao;
import com.example.restauranteexpresssv.dao.PedidoDao;
import com.example.restauranteexpresssv.dao.UsuarioDao;
import com.example.restauranteexpresssv.entities.Cliente;
import com.example.restauranteexpresssv.entities.Pedido;
import com.example.restauranteexpresssv.entities.Usuario;

@Database(entities = {Usuario.class, Cliente.class, Pedido.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract PedidoDao pedidoDao();
    public abstract ClienteDao clienteDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "restaurante_express_db"
                            )
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}