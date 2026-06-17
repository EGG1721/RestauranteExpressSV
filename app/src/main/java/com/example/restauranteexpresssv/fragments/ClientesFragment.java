package com.example.restauranteexpresssv.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteexpresssv.R;
import com.example.restauranteexpresssv.adapters.ClienteAdapter;
import com.example.restauranteexpresssv.database.AppDatabase;
import com.example.restauranteexpresssv.entities.Cliente;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ClientesFragment extends Fragment {

    private AppDatabase db;
    private ClienteAdapter adapter;
    private List<Cliente> listaClientes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);

        db=AppDatabase.getInstance(requireContext());

        RecyclerView rv = view.findViewById(R.id.rvClientes);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        listaClientes = db.clienteDao().obtenerTodos();
        adapter = new ClienteAdapter(listaClientes, this::mostrarDialogoEditar, this::confirmarEliminar);
        rv.setAdapter(adapter);


        //Busqueda en tiempo real
        EditText etBuscar = view.findViewById(R.id.etBuscar);
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                filtrar(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int i, int c, int a) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int b, int c) {

            }
        });
        FloatingActionButton fab = view.findViewById(R.id.fabAgregar);
        fab.setOnClickListener(v -> mostrarDialogoAgregar());
        return view;
    }
    private void filtrar(String texto) {
        listaClientes = db.clienteDao().buscarPorNombre(texto);
        adapter.actualizar(listaClientes);
    }
    //Dialogo para agregar un cliente nuevo
    private void mostrarDialogoAgregar() {
        View dialogoView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_cliente, null);

        new AlertDialog.Builder(getContext())
                .setTitle("Nuevo Cliente")
                .setView(dialogoView)
                .setPositiveButton("Guardar",(d, w)-> {
                    guardarCliente(dialogoView, null);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    //Para editar clientes existentes
    private void mostrarDialogoEditar(Cliente cliente){
        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_cliente, null);

        //Pre-llenado campos actuales
        ((EditText)dialogView.findViewById(R.id.etNombre)).setText(cliente.getNombre());
        ((EditText)dialogView.findViewById(R.id.etTelefono)).setText(cliente.getTelefono());
        ((EditText)dialogView.findViewById(R.id.etDireccion)).setText(cliente.getDireccion());
        ((EditText)dialogView.findViewById(R.id.etReferencia)).setText(cliente.getReferencia());
        ((EditText)dialogView.findViewById(R.id.etMunicipio)).setText(cliente.getMunicipio());

        new AlertDialog.Builder(getContext())
                .setTitle("Editar Cliente")
                .setView(dialogView)
                .setPositiveButton("Actualizar",(d, w) ->{
                    guardarCliente(dialogView, cliente);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void guardarCliente(View dialogView, Cliente existente){
        String nombre = ((EditText)
                dialogView.findViewById(R.id.etNombre)).getText().toString().trim();
        String telefono = ((EditText)
                dialogView.findViewById(R.id.etTelefono)).getText().toString().trim();
        String direccion = ((EditText)
                dialogView.findViewById(R.id.etDireccion)).getText().toString().trim();
        String referencia = ((EditText)
                dialogView.findViewById(R.id.etReferencia)).getText().toString().trim();
        String municipio = ((EditText)
                dialogView.findViewById(R.id.etMunicipio)).getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(getContext(),"El nombre es obligatorio",Toast.LENGTH_SHORT).show();
            return;
        }

        if(existente == null) {
            //Crear uno nuevo
            db.clienteDao().insertar(new Cliente(nombre, telefono, direccion, referencia, municipio));
            Toast.makeText(getContext(),"Cliente Agregado",Toast.LENGTH_SHORT).show();
        }else {
            //Actualizar
            existente.setNombre(nombre);
            existente.setTelefono(telefono);
            existente.setDireccion(direccion);
            existente.setReferencia(referencia);
            existente.setMunicipio(municipio);
            db.clienteDao().actualizar(existente);
            Toast.makeText(getContext(),"Cliente Actualizado",Toast.LENGTH_SHORT).show();

        }
        //Recargar lista
        listaClientes = db.clienteDao().obtenerTodos();
        adapter.actualizar(listaClientes);
    }

    private void confirmarEliminar(Cliente cliente) {
        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar Cliente")
                .setMessage("¿Seguro que desea eliminar a " + cliente.getNombre() + "?")
                .setPositiveButton("Eliminar", (d, w)-> {
                    db.clienteDao().eliminar(cliente);
                    listaClientes = db.clienteDao().obtenerTodos();
                    adapter.actualizar(listaClientes);
                    Toast.makeText(getContext(),"Cliente Eliminado",Toast.LENGTH_SHORT).show();

                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
