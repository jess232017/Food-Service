package com.kopodermo.proyecto_final.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.model.Menú;
import com.kopodermo.proyecto_final.model.Producto;
import com.kopodermo.proyecto_final.model.ProductosFilter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class Productos_Adapter extends RecyclerView.Adapter<Productos_Adapter.ProductoViewHolder> implements Filterable {
    private EventBus bus = EventBus.getDefault();

    //-- Declaracion de Variables
    private ColorGenerator generator = ColorGenerator.MATERIAL;
    private ArrayList<Producto> filterList;
    public ArrayList<Producto> data;
    private ProductosFilter filter;
    private Activity activity;
    private int resource;
    private int Tipo;

    //-----------------
    public Productos_Adapter(ArrayList<Producto> data, int resource, Activity activity, int Tipo) {
        this.filterList = data;
        this.data = data;
        this.resource = resource;
        this.activity = activity;
        this.Tipo = Tipo;
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

        //Si el producto es igual al que se requiere visualizar
        if(Tipo == producto.getTipo()){

            holder.txtNamePlato.setText(producto.getNombre());
            holder.txtDescription.setText("# " + producto.getExistencias());
            holder.txtCostoPlato.setText("C$ " + producto.getPrecio());
            holder.txtTotal.setText(producto.getCantidad() + "");


            String letter = String.valueOf(producto.getNombre().charAt(0));

            TextDrawable drawable = TextDrawable.builder()
                    .buildRect(letter, generator.getRandomColor());

            //Picasso.get().load(food.getImagen()).into(holder.imgPlato);
            Glide.with(activity)
                    .load(producto.getImagen())
                    .apply(new RequestOptions().placeholder(drawable))
                    .into(holder.imgPlato);

            holder.img_increase.setOnClickListener(view -> {

                holder.txtTotal.setText(producto.getCantidad() + "");

                if (producto.getCantidad() < producto.getExistencias()){

                    producto.setCantidad(producto.getCantidad()+1);
                    holder.txtTotal.setText(producto.getCantidad() + "");

                    bus.post(new Menú(producto.getPrecio(),data,1, "RV1"));
                }

            });

            holder.img_decrease.setOnClickListener(view -> {
                if (producto.getCantidad()>0){

                    producto.setCantidad(producto.getCantidad()-1);
                    holder.txtTotal.setText(producto.getCantidad() + "");

                    bus.post(new Menú(producto.getPrecio(),data,2, "RV2"));
                }
            });
        }else{
            holder.laytOrder.setVisibility(View.GONE);
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
            filter=new ProductosFilter(filterList,this);
        }
        return filter;
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNamePlato;
        private TextView txtDescription;
        private TextView txtCostoPlato;
        private TextView txtTotal;
        private ImageView imgPlato;
        private ImageButton img_increase;
        private ImageButton img_decrease;
        private LinearLayout laytOrder;

        ProductoViewHolder(View itemView) {
            super(itemView);
            txtNamePlato = itemView.findViewById(R.id.item_chlid_name);
            txtDescription = itemView.findViewById(R.id.item_chlid_content);
            txtCostoPlato = itemView.findViewById(R.id.item_chlid_money);
            txtTotal = itemView.findViewById(R.id.total);
            imgPlato = itemView.findViewById(R.id.item_chlid_image);
            img_increase = itemView.findViewById(R.id.img_increase);
            img_decrease = itemView.findViewById(R.id.img_decrease);
            laytOrder = itemView.findViewById(R.id.laytOrder);
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