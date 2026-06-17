package com.example.restauranteexpresssv.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranteexpresssv.R;
import com.example.restauranteexpresssv.adapters.PedidoAdapter;
import com.example.restauranteexpresssv.database.AppDatabase;
import com.example.restauranteexpresssv.entities.Cliente;
import com.example.restauranteexpresssv.entities.Pedido;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PedidoFragment extends Fragment {
    private AppDatabase db;
    private PedidoAdapter adapter;
    private List<Pedido> listaPedidos;

    private static class ProductoPedido {
        String nombre;
        int cantidad;
        double precioUnitario;
        double subtotal;

        ProductoPedido(String nombre, int cantidad, double precioUnitario) {
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = cantidad * precioUnitario;
        }
    }

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

    // Diálogo para agregar un pedido nuevo con varios productos.
    private void mostrarDialogoAgregarPedido() {
        // Regla del negocio: debe existir al menos un cliente.
        List<Cliente> clientes = db.clienteDao().obtenerTodos();
        if (clientes.isEmpty()) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Sin clientes")
                    .setMessage("Debe registrar un cliente antes de agregar un pedido.")
                    .setPositiveButton("Entendido", null)
                    .show();
            return;
        }

        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_pedido, null);

        Spinner spinnerCliente = dialogView.findViewById(R.id.spinnerCliente);
        List<String> nombresClientes = new ArrayList<>();
        for (Cliente c : clientes) nombresClientes.add(c.getNombre());
        ArrayAdapter<String> clienteAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, nombresClientes);
        clienteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCliente.setAdapter(clienteAdapter);

        Spinner spinnerProducto = dialogView.findViewById(R.id.spinnerProducto);
        String[] productos = {
                "Pupusas Revueltas", "Pupusa de Queso",
                "Pupusas de Frijol con Queso", "Yuca Frita",
                "Empanadas", "Pastelitos", "Sopa de Gallina",
                "Desayuno Típico", "Café", "Chocolate"
        };
        ArrayAdapter<String> productoAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, productos);
        productoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducto.setAdapter(productoAdapter);

        Spinner spinnerModalidad = dialogView.findViewById(R.id.spinnerModalidad);
        String[] modalidades = {"Comer en local", "Para llevar", "Delivery"};
        ArrayAdapter<String> modalidadAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, modalidades);
        modalidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModalidad.setAdapter(modalidadAdapter);

        Spinner spinnerEstado = dialogView.findViewById(R.id.spinnerEstado);
        String[] estados = {"Pendiente", "En proceso", "Entregado", "Cancelado"};
        ArrayAdapter<String> estadoAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, estados);
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(estadoAdapter);

        EditText etCantidad = dialogView.findViewById(R.id.etCantidad);
        EditText etPrecio = dialogView.findViewById(R.id.etPrecio);
        TextView tvTotal = dialogView.findViewById(R.id.tvTotal);
        TextView tvProductosAgregados = dialogView.findViewById(R.id.tvProductosAgregados);
        Button btnAgregarProducto = dialogView.findViewById(R.id.btnAgregarProducto);

        List<ProductoPedido> productosAgregados = new ArrayList<>();

        TextWatcher calcularTotalTemporal = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                actualizarVistaProductos(productosAgregados, tvProductosAgregados, tvTotal,
                        etCantidad, etPrecio);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        };
        etCantidad.addTextChangedListener(calcularTotalTemporal);
        etPrecio.addTextChangedListener(calcularTotalTemporal);

        btnAgregarProducto.setOnClickListener(v -> agregarProductoAlPedido(
                productosAgregados, spinnerProducto, etCantidad, etPrecio,
                tvProductosAgregados, tvTotal));

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Nuevo pedido")
                .setView(dialogView)
                .setPositiveButton("Guardar", null)
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    boolean hayProductoPendiente = !etCantidad.getText().toString().trim().isEmpty()
                            || !etPrecio.getText().toString().trim().isEmpty();

                    if (hayProductoPendiente) {
                        boolean agregado = agregarProductoAlPedido(productosAgregados, spinnerProducto,
                                etCantidad, etPrecio, tvProductosAgregados, tvTotal);
                        if (!agregado) return;
                    }

                    if (productosAgregados.isEmpty()) {
                        Toast.makeText(getContext(), "Agregue al menos un producto al pedido",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    guardarPedido(dialogView, clientes, spinnerCliente, spinnerModalidad,
                            spinnerEstado, productosAgregados);
                    dialog.dismiss();
                }));

        dialog.show();
    }

    private boolean agregarProductoAlPedido(List<ProductoPedido> productosAgregados,
                                            Spinner spinnerProducto,
                                            EditText etCantidad,
                                            EditText etPrecio,
                                            TextView tvProductosAgregados,
                                            TextView tvTotal) {
        String cantidadStr = etCantidad.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();

        if (cantidadStr.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(getContext(), "Ingrese cantidad y precio del producto",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        int cantidad;
        double precio;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Cantidad o precio no válido",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (cantidad <= 0 || precio <= 0) {
            Toast.makeText(getContext(), "La cantidad y el precio deben ser mayores que cero",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        String producto = spinnerProducto.getSelectedItem().toString();
        productosAgregados.add(new ProductoPedido(producto, cantidad, precio));

        etCantidad.setText("");
        etPrecio.setText("");
        actualizarVistaProductos(productosAgregados, tvProductosAgregados, tvTotal, etCantidad, etPrecio);

        Toast.makeText(getContext(), "Producto agregado al pedido", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void actualizarVistaProductos(List<ProductoPedido> productosAgregados,
                                          TextView tvProductosAgregados,
                                          TextView tvTotal,
                                          EditText etCantidad,
                                          EditText etPrecio) {
        double totalPedido = calcularTotalProductos(productosAgregados);

        String cantidadStr = etCantidad.getText().toString().trim();
        String precioStr = etPrecio.getText().toString().trim();

        if (!cantidadStr.isEmpty() && !precioStr.isEmpty()) {
            try {
                int cantidadTemporal = Integer.parseInt(cantidadStr);
                double precioTemporal = Double.parseDouble(precioStr);
                if (cantidadTemporal > 0 && precioTemporal > 0) {
                    totalPedido += cantidadTemporal * precioTemporal;
                }
            } catch (NumberFormatException ignored) { }
        }

        if (productosAgregados.isEmpty()) {
            tvProductosAgregados.setText("Aún no hay productos agregados");
        } else {
            tvProductosAgregados.setText(construirDetalleProductos(productosAgregados));
        }

        tvTotal.setText(String.format(Locale.US, "Total del pedido: $%.2f", totalPedido));
    }

    private double calcularTotalProductos(List<ProductoPedido> productosAgregados) {
        double total = 0;
        for (ProductoPedido item : productosAgregados) {
            total += item.subtotal;
        }
        return total;
    }

    private int calcularCantidadTotal(List<ProductoPedido> productosAgregados) {
        int total = 0;
        for (ProductoPedido item : productosAgregados) {
            total += item.cantidad;
        }
        return total;
    }

    private String construirDetalleProductos(List<ProductoPedido> productosAgregados) {
        StringBuilder detalle = new StringBuilder();
        for (int i = 0; i < productosAgregados.size(); i++) {
            ProductoPedido item = productosAgregados.get(i);
            detalle.append(i + 1)
                    .append(". ")
                    .append(item.nombre)
                    .append(" x")
                    .append(item.cantidad)
                    .append(" - $")
                    .append(String.format(Locale.US, "%.2f", item.subtotal));

            if (i < productosAgregados.size() - 1) {
                detalle.append("\n");
            }
        }
        return detalle.toString();
    }

    private void guardarPedido(View dialogView, List<Cliente> clientes,
                               Spinner spinnerCliente,
                               Spinner spinnerModalidad,
                               Spinner spinnerEstado,
                               List<ProductoPedido> productosAgregados) {

        EditText etObservaciones = dialogView.findViewById(R.id.etObservaciones);

        int posCliente = spinnerCliente.getSelectedItemPosition();
        Cliente clienteSel = clientes.get(posCliente);

        String detalleProductos = construirDetalleProductos(productosAgregados);
        int cantidadTotal = calcularCantidadTotal(productosAgregados);
        double totalPedido = calcularTotalProductos(productosAgregados);
        String modalidad = spinnerModalidad.getSelectedItem().toString();
        String estado = spinnerEstado.getSelectedItem().toString();
        String observaciones = etObservaciones.getText().toString().trim();

        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm",
                Locale.getDefault()).format(new Date());

        Pedido pedido = new Pedido(
                clienteSel.getId(), clienteSel.getNombre(),
                detalleProductos, cantidadTotal, 0, totalPedido,
                modalidad, estado, fecha, observaciones
        );

        db.pedidoDao().insertar(pedido);
        Toast.makeText(getContext(), "Pedido registrado", Toast.LENGTH_SHORT).show();
        recargarLista();
    }

    // Diálogo para editar solo el estado del pedido.
    private void mostrarDialogoEditarEstado(Pedido pedido) {
        String[] estados = {"Pendiente", "En proceso", "Entregado", "Cancelado"};

        int indexActual = 0;
        String estadoActualNormalizado = normalizarEstado(pedido.getEstado());
        for (int i = 0; i < estados.length; i++) {
            if (normalizarEstado(estados[i]).equals(estadoActualNormalizado)) {
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

    // Regla de negocio: no eliminar pedidos que están en proceso/preparación.
    private void confirmarEliminarPedido(Pedido pedido) {
        if (pedidoEstaEnProceso(pedido)) {
            Toast.makeText(getContext(),
                    "No se puede eliminar un pedido que está en proceso",
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

    private boolean pedidoEstaEnProceso(Pedido pedido) {
        String estado = normalizarEstado(pedido.getEstado());
        return estado.equals("en proceso") || estado.equals("en preparacion");
    }

    private String normalizarEstado(String estado) {
        if (estado == null) return "";
        String texto = estado.trim().toLowerCase(Locale.ROOT);
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return texto;
    }

    private void recargarLista() {
        listaPedidos = db.pedidoDao().obtenerTodos();
        adapter.actualizar(listaPedidos);
    }
}