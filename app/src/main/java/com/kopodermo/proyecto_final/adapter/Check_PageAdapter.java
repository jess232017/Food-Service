package com.kopodermo.proyecto_final.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kopodermo.proyecto_final.view.fragment.PedidoFragment;
import com.kopodermo.proyecto_final.view.fragment.RegistroFragment;

import org.jetbrains.annotations.NotNull;

public class Check_PageAdapter extends FragmentPagerAdapter {
    private final RegistroFragment registroFragment;
    private final PedidoFragment pedidoFragment;

    private int numOfTabs;
    public Check_PageAdapter(FragmentManager fm, int numOfTabs, RegistroFragment registroFragment,
                             PedidoFragment pedidoFragment) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.registroFragment = registroFragment;
        this.pedidoFragment = pedidoFragment;

    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return registroFragment;
            case 1:
                return pedidoFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}