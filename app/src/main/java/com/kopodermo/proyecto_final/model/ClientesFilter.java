package com.kopodermo.proyecto_final.model;

import android.widget.Filter;

import com.kopodermo.proyecto_final.adapter.Clientes_Adapter;

import java.util.ArrayList;

public class ClientesFilter extends Filter {
    private Clientes_Adapter adapter;
    private ArrayList<Cliente> filterList;

    public ClientesFilter(ArrayList<Cliente> filterList, Clientes_Adapter adapter)
    {
        this.adapter=adapter;
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
            ArrayList<Cliente> filteredPlayers=new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getNombres().toUpperCase().contains(constraint))
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
        adapter.data = (ArrayList<Cliente>) results.values;
        adapter.notifyDataSetChanged();
    }
}