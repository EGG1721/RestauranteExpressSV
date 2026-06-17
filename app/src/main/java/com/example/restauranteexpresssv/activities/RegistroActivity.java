package com.example.restauranteexpresssv.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.restauranteexpresssv.R;
import com.example.restauranteexpresssv.database.AppDatabase;
import com.example.restauranteexpresssv.entities.Usuario;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegistroActivity extends AppCompatActivity {

    private TextInputEditText etNombre, etCorreo, etPassword, etConfirmar;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        db = AppDatabase.getInstance(this);

        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        etConfirmar = findViewById(R.id.etConfirmar);

        MaterialButton btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener( v -> registrar());

    }
    private void registrar(){
        String nombre = etNombre.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmar = etConfirmar.getText().toString().trim();

        //Validaciones
        if(nombre.isEmpty() || correo.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(confirmar)) {
            Toast.makeText(this, "La contraseña no coincide", Toast.LENGTH_SHORT).show();
            return;
        }
        //Verificar correo duplicado
        if(db.usuarioDao().buscarPorCorreo(correo) != null){
            Toast.makeText(this, "Este correo ya esta Registrado", Toast.LENGTH_SHORT).show();
            return;
        }
        //Guardar Usuario
        db.usuarioDao().insertar(new Usuario(nombre, correo, password));
        Toast.makeText(this, "Cuenta Creada, ¡Inicie Sesion!", Toast.LENGTH_SHORT).show();
        finish();

    }

}
