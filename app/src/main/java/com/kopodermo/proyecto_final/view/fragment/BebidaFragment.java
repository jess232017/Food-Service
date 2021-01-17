package com.kopodermo.proyecto_final.view.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.adapter.Productos_Adapter;
import com.kopodermo.proyecto_final.view.activity.Pedido.Pedido_Activity;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class BebidaFragment extends Fragment {
    private Productos_Adapter productos_adapter;

    public BebidaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bebida, container, false);

        RecyclerView productoRecycler = view.findViewById(R.id.RvBebida);

        //------------
        productos_adapter = new Productos_Adapter(((Pedido_Activity)
                Objects.requireNonNull(getActivity())).Main_Producto, R.layout.raw_order, getActivity(),2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        productoRecycler.setLayoutManager(linearLayoutManager);


        productoRecycler.setAdapter(productos_adapter);
        return view;
    }

    public void MostrarQuery(String query){
        //FILTER AS YOU TYPE
        productos_adapter.getFilter().filter(query);
    }
}
