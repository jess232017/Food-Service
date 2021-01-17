package com.kopodermo.proyecto_final.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.view.activity.Reporte.Detalles_Activity;
import com.kopodermo.proyecto_final.model.Reporte;

import java.util.ArrayList;

public class Reportes_Adapter extends RecyclerView.Adapter<Reportes_Adapter.ProductoViewHolder> {
     //-- Declaracion de Variables
    private ArrayList<Reporte> reportes;
    private int resource;
    private Activity activity;

    //-----------------
    public Reportes_Adapter(Activity activity, ArrayList<Reporte> reportes, int resource) {
        this.activity = activity;
        this.reportes = reportes;
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
        Reporte reporte = reportes.get(position);
        holder.orderID.setText("Orden ID: "+ reporte.getIdCabecera());
        holder.order_name.setText(reporte.getNombre());
        holder.dev_date.setText(reporte.getFecha_Entr());
        holder.pay_date.setText(reporte.getFecha_Pago());
        holder.pay_status.setText(reporte.getEstadoPago());
        holder.order_price.setText("C$ "+ reporte.getTotal());

        holder.order_tracking.setOnClickListener(v -> {
            Intent intent = new Intent(activity, Detalles_Activity.class);

            intent.putExtra("Id_Cabecera", reporte.getIdCabecera());
            intent.putExtra("Forma_Pago", reporte.getEstadoPago());
            intent.putExtra("FechaPedido", reporte.getEstadoPago());
            intent.putExtra("FechaEntrega", reporte.getEstadoPago());
            intent.putExtra("FechaPago", reporte.getEstadoPago());
            intent.putExtra("IdCliente", reporte.getEstadoPago());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                Explode explode = new Explode();
                explode.setDuration(1000);
                activity.getWindow().setExitTransition(explode);
                activity.startActivity(intent,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(activity, v, activity.getString(R.string.transitionname_picture)).toBundle());
            }else {
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount () {
        return reportes.size();
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder{

        private TextView orderID;
        private TextView dev_date;
        private TextView pay_date;
        private TextView pay_status;
        private TextView order_name;
        private TextView order_price;
        private TextView order_tracking;

        ProductoViewHolder(View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.orderID);
            dev_date = itemView.findViewById(R.id.dev_date);
            pay_date = itemView.findViewById(R.id.pay_date);
            pay_status = itemView.findViewById(R.id.pay_status);
            order_name = itemView.findViewById(R.id.order_name);
            order_price = itemView.findViewById(R.id.order_price);
            order_tracking = itemView.findViewById(R.id.order_tracking);
        }
    }
}
