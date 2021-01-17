package com.kopodermo.proyecto_final.model;

import android.widget.Filter;


import com.kopodermo.proyecto_final.adapter.GProducto_Adapter;
import com.kopodermo.proyecto_final.adapter.Productos_Adapter;

import java.util.ArrayList;

public class ProductosFilter extends Filter {
    private Productos_Adapter adapter;
    private GProducto_Adapter adapter2;
    private ArrayList<Producto> filterList;

    public ProductosFilter(ArrayList<Producto> filterList, Productos_Adapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;
    }

    public ProductosFilter(ArrayList<Producto> filterList, GProducto_Adapter adapter)
    {
        this.adapter2=adapter;
        this.filterList=filterList;
    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Producto> filteredPlayers=new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getNombre().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }
            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }
        return results;
    }
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if(adapter2==null){
            adapter.data = (ArrayList<Producto>) results.values;
            adapter.notifyDataSetChanged();
        }else{
            adapter2.data = (ArrayList<Producto>) results.values;
            adapter2.notifyDataSetChanged();
        }
    }
}