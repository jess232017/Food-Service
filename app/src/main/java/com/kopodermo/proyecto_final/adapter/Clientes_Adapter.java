package com.kopodermo.proyecto_final.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.model.Cliente;
import com.kopodermo.proyecto_final.model.ClientesFilter;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Clientes_Adapter extends RecyclerView.Adapter<Clientes_Adapter.ProductoViewHolder> implements Filterable {

    //-- Declaracion de Variables
    private ColorGenerator generator = ColorGenerator.MATERIAL;
    private OnClienteListener mOnClienteListener;
    private ArrayList<Cliente> filterList;
    public ArrayList<Cliente> data;
    private ClientesFilter filter;
    private Activity activity;
    private int resource;
    //-----------------
    public Clientes_Adapter(ArrayList<Cliente> data, int resource, Activity activity, OnClienteListener mOnClienteListener) {
        this.data = data;
        this.filterList = data;
        this.resource = resource;
        this.activity = activity;
        this.mOnClienteListener = mOnClienteListener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ProductoViewHolder(view,mOnClienteListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder (@NonNull final ProductoViewHolder holder, int position){
        Cliente cliente = data.get(position);
        holder.name.setText(cliente.getNombres() + " " + cliente.getApellidos());
        holder.phone.setText("" + cliente.getTelefono());
        holder.order.setText("" + cliente.getPedidos());

        String letter = String.valueOf(cliente.getNombres().charAt(0));
        TextDrawable drawable;

        if(resource==R.layout.raw_cliente2){
            drawable = TextDrawable.builder()
                    .buildRect(letter, generator.getRandomColor());

            Glide.with(activity)
                    .load(cliente.getImagen())
                    .apply(new RequestOptions().placeholder(drawable))
                    .into(holder.image);
        }else{
            drawable = TextDrawable.builder()
                    .buildRound(letter, generator.getRandomColor());

            Glide.with(activity)
                    .load(cliente.getImagen())
                    .apply(new RequestOptions().placeholder(drawable))
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount () {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new ClientesFilter(filterList,this);
        }
        return filter;
    }

    public interface OnClienteListener {
        void onClienteClick(Cliente cliente);
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private OnClienteListener mOnClienteListener;
        private TextView name;
        private TextView phone;
        private TextView order;
        private ImageView image;

        ProductoViewHolder(View itemView, OnClienteListener mOnClienteListener) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            image = itemView.findViewById(R.id.image);
            order = itemView.findViewById(R.id.order);
            this.mOnClienteListener = mOnClienteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            Cliente cliente = data.get(getAdapterPosition());
            mOnClienteListener.onClienteClick(cliente);
        }
    }

    //Ajusta la nueva informacion al recyclerview
    public void setData(ArrayList<Cliente> newData) {
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
        private final ArrayList<Cliente> oldData, newData;

        DataDiffCallback(ArrayList<Cliente> oldData, ArrayList<Cliente> newData) {
            this.oldData = oldData;
            this.newData = newData;
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
            return oldData.get(oldItemPosition).getIdCliente().equals(newData.get(newItemPosition).getIdCliente());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldData.get(oldItemPosition).equals(newData.get(newItemPosition));
        }
    }
}