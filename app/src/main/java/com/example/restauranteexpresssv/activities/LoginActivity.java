package com.example.restauranteexpresssv.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

        AppCompatDelegate.setDefaultNightMode(
                session.isDarkMode()
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );

        // Si hay sesión activa, entrar directo a la pantalla principal correcta
        if (session.estaLogueado()) {
            irAlMain();
            return;
        }

        setContentView(R.layout.activity_login);

        db = AppDatabase.getInstance(this);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);

        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> intentarLogin());

        TextView tvRegistrarse = findViewById(R.id.tvRegistrarse);
        tvRegistrarse.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class))
        );
    }

    private void intentarLogin() {
        String correo = etCorreo.getText() == null ? "" : etCorreo.getText().toString().trim();
        String password = etPassword.getText() == null ? "" : etPassword.getText().toString().trim();

        if (correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = db.usuarioDao().login(correo, password);

        if (usuario != null) {
            session.guardarSesion(usuario.getId(), usuario.getNombre());
            irAlMain();
        } else {
            Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }

    private void irAlMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
