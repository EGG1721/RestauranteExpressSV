package com.example.restauranteexpresssv.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.restauranteexpresssv.R;
import com.example.restauranteexpresssv.activities.LoginActivity;
import com.example.restauranteexpresssv.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class ConfiguracionFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuracion, container, false);

        SessionManager session = new SessionManager(requireContext());

        //Switch modo oscuro
        SwitchMaterial switchDark = view.findViewById(R.id.switchDarkMode);
        switchDark.setChecked(session.isDarkMode());
        switchDark.setOnCheckedChangeListener((btn, isCheked) -> {
            session.setDarkMode(isCheked);
            //Aplica el tema de forma instantanea
            AppCompatDelegate.setDefaultNightMode(
                    isCheked ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO);

        });
        //Boton Cerrrar Sesion

        MaterialButton btnSalir = view.findViewById(R.id.btnCerrarSesion);
        btnSalir.setOnClickListener(v -> {
            session.cerrarSesion();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        return view;


    }
}
