package com.example.restauranteexpresssv.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restauranteexpresssv.R;
import com.example.restauranteexpresssv.entities.Pedido;
import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private List<Pedido> lista;

    public interface OnEditarListener  { void onEditar(Pedido p); }
    public interface OnEliminarListener { void onEliminar(Pedido p); }

    private final OnEditarListener   onEditar;
    private final OnEliminarListener onEliminar;

    public PedidoAdapter(List<Pedido> lista,
                         OnEditarListener onEditar,
                         OnEliminarListener onEliminar) {
        this.lista     = lista;
        this.onEditar  = onEditar;
        this.onEliminar = onEliminar;
    }

    public void actualizar(List<Pedido> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = lista.get(position);

        holder.tvClienteNombre.setText("👤 " + pedido.getClienteNombre());
        holder.tvProducto.setText("🍽️\n" + pedido.getProducto());
        holder.tvTotal.setText(String.format("$%.2f", pedido.getTotal()));
        holder.tvEstado.setText("Estado: " + pedido.getEstado());

        holder.btnEditar.setOnClickListener(v -> onEditar.onEditar(pedido));
        holder.btnEliminar.setOnClickListener(v -> onEliminar.onEliminar(pedido));
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvClienteNombre, tvProducto, tvTotal, tvEstado;
        ImageButton btnEditar, btnEliminar;

        PedidoViewHolder(View itemView) {
            super(itemView);
            tvClienteNombre = itemView.findViewById(R.id.tvClienteNombre);
            tvProducto      = itemView.findViewById(R.id.tvProducto);
            tvTotal         = itemView.findViewById(R.id.tvTotal);
            tvEstado        = itemView.findViewById(R.id.tvEstado);
            btnEditar       = itemView.findViewById(R.id.btnEditar);
            btnEliminar     = itemView.findViewById(R.id.btnEliminar);
        }
    }
}