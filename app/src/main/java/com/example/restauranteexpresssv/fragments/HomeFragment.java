package com.example.restauranteexpresssv.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.restauranteexpresssv.R;
import com.example.restauranteexpresssv.database.AppDatabase;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        AppDatabase db = AppDatabase.getInstance(requireContext());

        //Obtener Metricas
        int totalClientes = db.clienteDao().contarClientes();
        int totalPedidos = db.pedidoDao().contarPedidos();
        int pendientes = db.pedidoDao().contarPendientes();
        double vendido = db.pedidoDao().totalVendido();

        //Mostrar en las tarjetas
        ((TextView) view.findViewById(R.id.tvTotalClientes)).setText(String.valueOf(totalClientes));
        ((TextView) view.findViewById(R.id.tvTotalPedidos)).setText(String.valueOf(totalPedidos));
        ((TextView) view.findViewById(R.id.tvPendientes)).setText(String.valueOf(pendientes));
        ((TextView) view.findViewById(R.id.tvTotalVendido)).setText(String.format("$%.2f", vendido));

        return view;

    }
}
