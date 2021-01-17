package com.kopodermo.proyecto_final.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.model.Producto;

import java.util.ArrayList;

public class Detalles_Adapter extends RecyclerView.Adapter<Detalles_Adapter.ProductoViewHolder> {
     //-- Declaracion de Variables
    private ArrayList<Producto> data;
    private int resource;

    //-----------------
    public Detalles_Adapter(ArrayList<Producto> data, int resource) {
        this.data = data;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ProductoViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder (@NonNull final ProductoViewHolder holder, int position){
        Producto producto = data.get(position);
        holder.checkout_name.setText(producto.getNombre());
        holder.checkout_quantity.setText(producto.getCantidad() + " x");
        holder.checkout_price.setText("C$ "+producto.getPrecio());
    }

    @Override
    public int getItemCount () {
        return data.size();
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder{

        private TextView checkout_name;
        private TextView checkout_quantity;
        private TextView checkout_price;

        ProductoViewHolder(View itemView) {
            super(itemView);
            checkout_name = itemView.findViewById(R.id.checkout_name);
            checkout_quantity = itemView.findViewById(R.id.checkout_quantity);
            checkout_price = itemView.findViewById(R.id.checkout_price);
        }
    }

    //Ajusta la nueva informacion al recyclerview
    public void setData(ArrayList<Producto> newData) {
        if (data != null) {
            DataDiffCallback dataDiffCallback = new DataDiffCallback(data, newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(dataDiffCallback);

            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        } else {
            // first initialization
            data = newData;
        }
    }

    class DataDiffCallback extends DiffUtil.Callback {
        private final ArrayList<Producto> oldData, newData;

        DataDiffCallback(ArrayList<Producto> oldCliente, ArrayList<Producto> newCliente) {
            this.oldData = oldCliente;
            this.newData = newCliente;
        }

        @Override
        public int getOldListSize() {
            return oldData.size();
        }

        @Override
        public int getNewListSize() {
            return newData.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldData.get(oldItemPosition).getIdProducto().equals(newData.get(newItemPosition).getIdProducto());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldData.get(oldItemPosition).equals(newData.get(newItemPosition));
        }
    }
}
