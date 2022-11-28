package com.MBP.honey_recipe.adapter;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.MBP.honey_recipe.Categories.category_desert;
import com.MBP.honey_recipe.Categories.category_etc;
import com.MBP.honey_recipe.Categories.category_juice;
import com.MBP.honey_recipe.Categories.category_noodle;
import com.MBP.honey_recipe.Categories.category_rice;

import java.util.ArrayList;

public class bestViewPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private ArrayList<String> name = new ArrayList<>();

    public bestViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;

        //카테고리 이름
        name.add("면류");
        name.add("밥류");
        name.add("디저트류");
        name.add("음료류");
        name.add("기타");

    }

    //각 프래그먼트 연결
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                category_noodle noodle= new category_noodle();
                return noodle;
            case 1:
                category_rice rice= new category_rice();
                return rice;
            case 2:
                category_desert desert= new category_desert();
                return desert;
            case 3:
                category_juice juice= new category_juice();
                return juice;
            case 4:
                category_etc etc= new category_etc();
                return etc;

            default:
                return null;
        }
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return name.get(position);
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}