package com.android.aifoodapp.adapter;

import static com.android.aifoodapp.interfaceh.baseURL.url;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.android.aifoodapp.domain.dailymeal;

import java.util.ArrayList;
import java.util.List;


public class ReportPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> items = new ArrayList<Fragment>();

    public ReportPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addItem(Fragment item) {
        items.add(item);
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "< " + (position+1) +" >";
    }
}
