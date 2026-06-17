package com.example.restauranteexpresssv.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.restauranteexpresssv.R;
import com.example.restauranteexpresssv.fragments.ConfiguracionFragment;
import com.example.restauranteexpresssv.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
        setContentView(R.layout.activity_main);

        //Mostrar el Dashboard al inicio
        cargarFragment(new Fragment());

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();
            if(id == R.id.navInicio)     fragment = new HomeFragment();
            else if (id == R.id.navClientes) fragment = new ConfiguracionFragment();
            cargarFragment(fragment);
            return true;
        });
    }
    private void cargarFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

}
