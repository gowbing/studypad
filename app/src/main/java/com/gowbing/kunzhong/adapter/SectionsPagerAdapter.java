package com.gowbing.kunzhong.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gowbing.kunzhong.ui.fragment.ZuoyeListFragment;

import java.util.List;

/**
 * Created by hss on 2016/11/27.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private List<ZuoyeListFragment> mFragmentList;
    public SectionsPagerAdapter(FragmentManager fm, List<ZuoyeListFragment> fragmentList) {
        super(fm);
        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

}
