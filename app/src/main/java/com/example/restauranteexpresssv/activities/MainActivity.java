package com.example.restauranteexpresssv.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.restauranteexpresssv.R;
import com.example.restauranteexpresssv.fragments.ClientesFragment;
import com.example.restauranteexpresssv.fragments.ConfiguracionFragment;
import com.example.restauranteexpresssv.fragments.HomeFragment;
import com.example.restauranteexpresssv.fragments.PedidosFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();

            if (id == R.id.navInicio) {
                fragment = new HomeFragment();
            } else if (id == R.id.navClientes) {
                fragment = new ClientesFragment();
            } else if (id == R.id.navPedidos) {
                fragment = new PedidosFragment();
            } else if (id == R.id.navConfiguracion) {
                fragment = new ConfiguracionFragment();
            } else {
                return false;
            }

            cargarFragment(fragment);
            return true;
        });

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.navInicio);
        }
    }

    // Metodo cargarFragment
    private void cargarFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

}