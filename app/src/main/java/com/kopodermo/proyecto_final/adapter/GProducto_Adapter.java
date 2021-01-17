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
import com.kopodermo.proyecto_final.model.Producto;
import com.kopodermo.proyecto_final.model.ProductosFilter;

import java.util.ArrayList;

public class GProducto_Adapter extends RecyclerView.Adapter<GProducto_Adapter.FoodViewHolder> implements Filterable {
    //Declaracion de Variables
    private ColorGenerator generator = ColorGenerator.MATERIAL;
    private static final String TAG = "NotesRecyclerAdapter";
    private OnFoodListener mOnFoodListener;
    private ArrayList<Producto> filterList;
    public ArrayList<Producto> data;
    private ProductosFilter filter;
    private Activity activity;
    //---------------------------

    public GProducto_Adapter(ArrayList<Producto> data, OnFoodListener onFoodListener, Activity activity) {
        this.data = data;
        this.filterList = data;
        this.mOnFoodListener  = onFoodListener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_producto, parent, false);
        return new FoodViewHolder(view, mOnFoodListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final FoodViewHolder holder, int position) {
        final Producto producto = data.get(position);

        holder.txtNamePlato.setText(producto.getNombre());
        holder.txtDescription.setText("# " + producto.getExistencias());
        holder.txtCostoPlato.setText("C$ " + producto.getPrecio());

        String letter = String.valueOf(producto.getNombre().charAt(0));
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor());


        //Picasso.get().load(food.getImagen()).into(holder.imgPlato);
        Glide.with(activity)
                .load(producto.getImagen())
                .apply(new RequestOptions().placeholder(drawable))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imgPlato);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new ProductosFilter(filterList,this);
        }
        return filter;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnFoodListener mOnFoodListener;
        private TextView txtNamePlato;
        private TextView txtDescription;
        private TextView txtCostoPlato;
        private ImageView imgPlato;

        FoodViewHolder(View itemView, OnFoodListener onFoodListener) {
            super(itemView);
            txtNamePlato = itemView.findViewById(R.id.item_chlid_name);
            txtDescription = itemView.findViewById(R.id.item_chlid_content);
            txtCostoPlato = itemView.findViewById(R.id.item_chlid_money);
            imgPlato = itemView.findViewById(R.id.item_chlid_image);

            this.mOnFoodListener = onFoodListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            Producto producto = data.get(getAdapterPosition());
            mOnFoodListener.onNoteClick(producto);
        }
    }

    public interface OnFoodListener{
        void onNoteClick(Producto partydata);
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
