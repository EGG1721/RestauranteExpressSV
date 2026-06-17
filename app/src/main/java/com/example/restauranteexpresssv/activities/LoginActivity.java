package com.example.restauranteexpresssv.activities;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.restauranteexpresssv.MainActivity;
import com.example.restauranteexpresssv.R;
import com.example.restauranteexpresssv.database.AppDatabase;
import com.example.restauranteexpresssv.entities.Usuario;
import com.example.restauranteexpresssv.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText etCorreo, etPassword;
    private AppDatabase db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);
        if(session.isDarkMode()) {
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
            );
        }
        //Si hay sesion activa ir al main
        if(session.estaLogueado()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        db = AppDatabase.getInstance(this);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);

        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> intentarLogin());

        TextView tvRegistrarse = findViewById(R.id.tvRegistrarse);
        tvRegistrarse.setOnClickListener(v -> startActivity(new Intent(this,RegistroActivity.class)));
    }
    private void intentarLogin(){
        String correo = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        //Validaciones Basicas
        if(correo.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Complete todos los campos",Toast.LENGTH_SHORT).show();
            return;
        }
        //Consultar en la Base de Datos
        Usuario usuario = db.usuarioDao().login(correo, password);

        if(usuario != null) {
            //Login exitoso, guarda la sesion y continua navegando
            session.guardarSesion(usuario.getId(), usuario.getNombre());
            startActivity(new Intent(this, MainActivity.class));
            finish(); //Para evitar ir al login con el boton Atras
        } else {
            Toast.makeText(this,"Correo o Contraseña Incorrecto", Toast.LENGTH_SHORT).show();

        }
    }

}
