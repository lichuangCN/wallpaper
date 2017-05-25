package com.example.cnlcnn.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *  项目名：  WallPaper
 *  创建者:   LiChuang
 *  创建时间:  2017/4/23.
 *  描述： 主界面tablayout适配器
 */

public class HomePagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public void addTab(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
    @Override
    public int getCount() {
        return fragments.size();
    }

}
