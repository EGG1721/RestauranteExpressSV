package com.example.restauranteexpresssv.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREFS_NAME = "RestaurantePrefs";
    private static final String KEY_LOGGED = "isLoggedIn";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_NOMBRE = "userName";
    private static final String KEY_THEME = "darkMode";

    private final SharedPreferences prefs;

    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    //Para guardar la sesion al hacer Login
    public void guardarSesion(int userId, String nombre) {
        editor.putBoolean(KEY_LOGGED, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_NOMBRE, nombre);
        editor.apply();
    }
    public boolean estaLogueado() {
        return prefs.getBoolean(KEY_LOGGED, false);
    }
    public String getNombreUsuario(){
        return prefs.getString(KEY_NOMBRE, "");
    }
    //Para cerrar sesion y limpiar
    public void cerrarSesion(){
        editor.clear();
        editor.apply();
    }
    //Modo Oscuro
    public void setDarkMode(boolean activado){
        editor.putBoolean(KEY_THEME, activado);
        editor.apply();
    }
    public boolean isDarkMode(){
        return prefs.getBoolean(KEY_THEME, false);
    }
}
