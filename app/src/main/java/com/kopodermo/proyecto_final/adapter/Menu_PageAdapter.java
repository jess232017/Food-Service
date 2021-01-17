package com.kopodermo.proyecto_final.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kopodermo.proyecto_final.view.fragment.BebidaFragment;
import com.kopodermo.proyecto_final.view.fragment.ComidaFragment;
import com.kopodermo.proyecto_final.view.fragment.ExtraFragment;

public class Menu_PageAdapter extends FragmentPagerAdapter {
    private final ComidaFragment comidaFragment;
    private final BebidaFragment bebidaFragment ;
    private final ExtraFragment extraFragment ;

    private int numOfTabs;
    public Menu_PageAdapter(FragmentManager fm, int numOfTabs, ComidaFragment comidaFragment,
                            BebidaFragment bebidaFragment,ExtraFragment extraFragment) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.comidaFragment = comidaFragment;
        this.bebidaFragment = bebidaFragment;
        this.extraFragment = extraFragment;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return comidaFragment;
            case 1:
                return bebidaFragment;
            case 2:
                return extraFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}