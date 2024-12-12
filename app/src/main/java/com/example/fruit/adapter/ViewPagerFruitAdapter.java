package com.example.fruit.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.fruit.ui.fragment.FruitRecycleFragment;


public class ViewPagerFruitAdapter extends FragmentPagerAdapter {
    private Context context;
    private ViewPager viewPager;


    private String[] title = {"果类", "柑类","桃类" ,"瓜类","蕉类","葡萄类"};

    public ViewPagerFruitAdapter(@NonNull FragmentManager fm, Context context, ViewPager viewPager) {
        super(fm);
        this.context=context;
        this.viewPager=viewPager;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return FruitRecycleFragment.newInstance(context,position,viewPager);
    }


    @Override
    public int getCount() {
        return title.length;
    }
    /*// 可选：为每个页面提供一个标题
    @Override
    public CharSequence getPageTitle(int position) {
        return (position + 1)+title[position];
    }*/

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        // 设置 Tab 标题
        switch (position) {
            case 0:
                return "果类";
            case 1:
                return "柑类";
            case 2:
                return "桃类";
            case 3:
                return "瓜类";
            case 4:
                return "蕉类";
            case 5:
                return "葡萄类";
            default:
                return null;
        }
    }
}
