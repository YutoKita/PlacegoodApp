package jp.north.mt.placegoodapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.android.gms.maps.MapFragment;

public class ExampleFragmentPagerAdapter extends FragmentPagerAdapter {
    public ExampleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
//                return ListFragment.newInstance(android.R.color.holo_blue_bright);
                return ListFragment.newInstance();
            case 1:
                return MapsFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "List";
            case 1:
                return "Map";
        }
        return null;
    }
}