package com.example.restauranteexpresssv.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteexpresssv.R;
import com.example.restauranteexpresssv.database.AppDatabase;
import com.example.restauranteexpresssv.entities.Cliente;
import com.example.restauranteexpresssv.entities.Pedido;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PedidoFragment extends Fragment {
    private AppDatabase db;
    private PedidoAdapter adapter;
    private List<Pedido> listaPedidos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);

        db = AppDatabase.getInstance(requireContext());

        RecyclerView rv = view.findViewById(R.id.rvPedidos);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        listaPedidos = db.pedidoDao().obtenerTodos();
        adapter = new PedidoAdapter(listaPedidos,
                this::mostrarDialogoEditarEstado,
                this::confirmarEliminarPedido);
        rv.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAgregar);
        fab.setOnClickListener(v -> mostrarDialogoAgregarPedido());
        return view;
    }

    //Dialogo para agregar un pedido nuevo
    private void mostrarDialogoAgregarPedido() {
        //Regla del Negocio, debe existir al menos un cliente
        List<Cliente> clientes = db.clienteDao().obtenerTodos();
        if (clientes.isEmpty()) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Sin Clientes")
                    .setMessage("Debe Registrar un cliente antes de agregar un pedido")
                    .setPositiveButton("Entendido", null)
                    .show();
            return;
        }

        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_pedido, null);

        //Spinner de clientes
        Spinner spinnerCliente = dialogView.findViewById(R.id.spinnerCliente);
        List<String> nombresClientes = new ArrayList<>();
        for (Cliente c : clientes) nombresClientes.add(c.getNombre());
        spinnerCliente.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, nombresClientes));

        //Spinner de Producto
        Spinner spinnerProducto = dialogView.findViewById(R.id.spinnerProducto);
        String[] productos = {
                "Pupusas Revueltas", "Pupusa de Quedso",
                "Pupusas de Frijol con Queso", "Yuca Frita",
                "Empanadas", "Pastelitos", "Sopa de Gallina",
                "Desayuno Tipico", "Cafe", "Chocolate"
        };
        spinnerProducto.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, productos));

        //Spinner de Modalidad
        Spinner spinnerModalidad = dialogView.findViewById(R.id.spinnerModalidad);
        String[] modalidades = {"Comer en Local", "Para Llevar", "Delivery"};
        spinnerModalidad.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, modalidades));

        //Spinner de Estado
        Spinner spinnerEstado = dialogView.findViewById(R.id.spinnerEstado);
        String[] estados = {"Pendiente", "En Preparacion", "Entregado", "Cancelado"};
        spinnerEstado.setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, estados));

        //Calculo Automatico del Total
        EditText etCantidad = dialogView.findViewById(R.id.etCantidad);
        EditText etPrecio = dialogView.findViewById(R.id.etPrecio);
        TextView tvTotal = dialogView.findViewById(R.id.tvTotal);

        TextWatcher calcularTotal = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int cant = Integer.parseInt(etCantidad.getText().toString());
                    double precio = Double.parseDouble(etPrecio.getText().toString());
                    tvTotal.setText(String.format("Total: $%.2f", cant * precio));
                } catch (NumberFormatException e) {
                    tvTotal.setText("Total : $ 0.00 ");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int i, int c, int a) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int b, int c) {
            }

        };
        etCantidad.addTextChangedListener(calcularTotal);
        etPrecio.addTextChangedListener(calcularTotal);

        new AlertDialog.Builder(getContext())
                .setTitle("Nuevo Pedido")
                .setView(dialogView)
                .setPositiveButton("Guardar", (d, w) ->
                        guardarPedido(dialogView, clientes, spinnerCliente, spinnerProducto, spinnerModalidad, spinnerEstado))
                .setNegativeButton("Cancelar", null)
                .show();

    }

    private void guardarPedido(View dialogView, List<Cliente> clientes,
                               Spinner spinnerCliente, Spinner spinnerProducto,
                               Spinner spinnerModalidad, Spinner spinnerEstado) {

        EditText etCantidad = dialogView.findViewById(R.id.etCantidad);
        EditText etPrecio = dialogView.findViewById(R.id.etPrecio);
        EditText etObservaciones = dialogView.findViewById(R.id.etObservaciones);

        String cantidadStr = etCantidad.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();

        // Validaciones
        if (cantidadStr.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(getContext(), "Cantidad y precio son obligatorios",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        // Obtener cliente seleccionado
        int posCliente = spinnerCliente.getSelectedItemPosition();
        Cliente clienteSel = clientes.get(posCliente);

        int cantidad = Integer.parseInt(cantidadStr);
        double precio = Double.parseDouble(precioStr);
        double total = cantidad * precio;
        String producto = spinnerProducto.getSelectedItem().toString();
        String modalidad = spinnerModalidad.getSelectedItem().toString();
        String estado = spinnerEstado.getSelectedItem().toString();
        String observaciones = etObservaciones.getText().toString().trim();

        // Fecha actual automática
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm",
                Locale.getDefault()).format(new Date());

        Pedido pedido = new Pedido(
                clienteSel.getId(), clienteSel.getNombre(),
                producto, cantidad, precio, total,
                modalidad, estado, fecha, observaciones
        );

        db.pedidoDao().insertar(pedido);
        Toast.makeText(getContext(), "Pedido registrado", Toast.LENGTH_SHORT).show();
        recargarLista();

    }

    // ─── Diálogo para editar solo el estado del pedido ───────────────────────
    private void mostrarDialogoEditarEstado(Pedido pedido) {
        String[] estados = {"Pendiente", "En preparación", "Entregado", "Cancelado"};

        // Encontrar el índice del estado actual
        int indexActual = 0;
        for (int i = 0; i < estados.length; i++) {
            if (estados[i].equals(pedido.getEstado())) {
                indexActual = i;
                break;
            }
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Cambiar estado del pedido")
                .setSingleChoiceItems(estados, indexActual, null)
                .setPositiveButton("Actualizar", (dialog, w) -> {
                    android.app.AlertDialog d = (android.app.AlertDialog) dialog;
                    int seleccionado = d.getListView().getCheckedItemPosition();
                    pedido.setEstado(estados[seleccionado]);
                    db.pedidoDao().actualizar(pedido);
                    Toast.makeText(getContext(), "Estado actualizado", Toast.LENGTH_SHORT).show();
                    recargarLista();
                })
                .setNegativeButton("Cancelar", null)
                .show();

    }
    // ─── REGLA DE NEGOCIO: no eliminar si está En preparación ────────────────
    private void confirmarEliminarPedido(Pedido pedido) {
        if (pedido.getEstado().equals("En preparación")) {
            Toast.makeText(getContext(),
                    "No se puede eliminar un pedido En preparación",
                    Toast.LENGTH_LONG).show();
            return;
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Eliminar pedido")
                .setMessage("¿Eliminar el pedido de " + pedido.getClienteNombre() + "?")
                .setPositiveButton("Eliminar", (d, w) -> {
                    db.pedidoDao().eliminar(pedido);
                    Toast.makeText(getContext(), "Pedido eliminado", Toast.LENGTH_SHORT).show();
                    recargarLista();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // ─── Recargar lista después de cualquier cambio ───────────────────────────
    private void recargarLista() {
        listaPedidos = db.pedidoDao().obtenerTodos();
        adapter.actualizar(listaPedidos);
    }
}