package com.example.project.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.project.Models.Tab;

import java.util.ArrayList;

public class TabAdapter extends FragmentStatePagerAdapter  {
ArrayList<Tab> tabs = new ArrayList<>();

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }
    public void addTab(Tab tab){
        tabs.add(tab);
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position).getFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return tabs.size();
    }
}
