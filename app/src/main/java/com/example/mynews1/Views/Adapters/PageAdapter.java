package com.example.mynews1.Views.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mynews1.Controllers.Fragments.ArtsFragment;
import com.example.mynews1.Controllers.Fragments.MostPopularFragment;
import com.example.mynews1.Controllers.Fragments.TopStoriesFragment;

public class PageAdapter extends FragmentPagerAdapter{

    public int tabPosition;

    //Default Constructor
    public PageAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public int getCount() {
        return(3);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: //Page number 1
                tabPosition = 1;
                return TopStoriesFragment.newInstance();
            case 1: //Page number 2
                tabPosition = 2;
                return MostPopularFragment.newInstance();
            case 2: //Page number 3
                tabPosition = 3;
                return ArtsFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: //Page number 1
                return "TOP STORIES";
            case 1: //Page number 2
                return "MOST POPULAR";
            case 2: //Page number 3
                return "ARTS";
            default:
                return null;
        }
    }
}
