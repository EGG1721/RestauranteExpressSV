package com.example.restauranteexpresssv.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restauranteexpresssv.R;
import com.example.restauranteexpresssv.entities.Cliente;
import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private List<Cliente> lista;

    // ⚠️ Las interfaces deben empezar con mayúscula y estar bien escritas
    public interface OnEditarListener {
        void onEditar(Cliente c);
    }

    public interface OnEliminarListener {
        void onEliminar(Cliente c);
    }

    private final OnEditarListener   onEditar;
    private final OnEliminarListener onEliminar;

    public ClienteAdapter(List<Cliente> lista,
                          OnEditarListener onEditar,
                          OnEliminarListener onEliminar) {
        this.lista      = lista;
        this.onEditar   = onEditar;
        this.onEliminar = onEliminar;
    }

    public void actualizar(List<Cliente> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = lista.get(position);
        holder.tvNombre.setText(cliente.getNombre());
        holder.tvTelefono.setText("📞 " + cliente.getTelefono());
        holder.tvMunicipio.setText("📍 " + cliente.getMunicipio());

        holder.btnEditar.setOnClickListener(v -> onEditar.onEditar(cliente));
        holder.btnEliminar.setOnClickListener(v -> onEliminar.onEliminar(cliente));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    // ViewHolder
    static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView    tvNombre, tvTelefono, tvMunicipio;
        ImageButton btnEditar, btnEliminar;

        ClienteViewHolder(View itemView) {
            super(itemView);
            tvNombre    = itemView.findViewById(R.id.tvNombre);
            tvTelefono  = itemView.findViewById(R.id.tvTelefono);
            tvMunicipio = itemView.findViewById(R.id.tvMunicipio);
            btnEditar   = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}