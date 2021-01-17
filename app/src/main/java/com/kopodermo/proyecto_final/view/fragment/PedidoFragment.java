package com.kopodermo.proyecto_final.view.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kopodermo.proyecto_final.R;
import com.kopodermo.proyecto_final.adapter.Detalles_Adapter;
import com.kopodermo.proyecto_final.view.activity.Pedido.Confirmar_Activity;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class PedidoFragment extends Fragment {
    private Detalles_Adapter detalles_adapter;

    public PedidoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pedido, container, false);

        RecyclerView productoRecycler = view.findViewById(R.id.RvDetalles);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        productoRecycler.setLayoutManager(linearLayoutManager);

        //------------
        Detalles_Adapter detalles_adapter = new Detalles_Adapter(((Confirmar_Activity)
                Objects.requireNonNull(getActivity())).GestionarPedido, R.layout.raw_detail);
        productoRecycler.setAdapter(detalles_adapter);

        return view;
    }

}
