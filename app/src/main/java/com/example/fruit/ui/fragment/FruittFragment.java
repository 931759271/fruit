package com.example.fruit.ui.fragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.app.Activity;
//import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
//import android.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.fruit.R;
import com.example.fruit.adapter.FruitAdapter;
import com.example.fruit.adapter.ViewPagerFruitAdapter;
import com.example.fruit.bean.Fruit;
import com.example.fruit.ui.activity.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FruittFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FruittFragment extends Fragment {

    private ViewPager viewPager;
    private ViewPagerFruitAdapter viewPagerFruitAdapter;





    private Activity myActivity;//上下文
    private TabLayout tabTitle;
    private RecyclerView rvfruitList;
    private FruitAdapter mfruitAdapter;
    private LinearLayout llEmpty;
    private Boolean mIsAdmin;
    private EditText etQuery;//搜索内容
    private ImageView ivSearch;//搜索图标
    private FloatingActionButton btnAdd;
    private String[] state = {"0","1","2","3","4","5"};
    private String[] title = {"果类", "柑类","桃类" ,"瓜类","蕉类","葡萄类"};
    private String typeId = "0";
    private List<Fruit> mfruit;
    PagerTabStrip pts_tab;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity= (Activity) context;
    }



    public FruittFragment() {
        // Required empty public constructor
    }


    public static FruittFragment newInstance(String param1, String param2) {
        FruittFragment fragment = new FruittFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        /*    mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fruitt, container, false);
        viewPager=view.findViewById(R.id.vp_content);
        tabTitle=view.findViewById(R.id.tab_outside);
        tabTitle.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabTitle.setupWithViewPager(viewPager);
        FragmentManager fragmentManager = getFragmentManager();
        viewPagerFruitAdapter=new ViewPagerFruitAdapter(fragmentManager,myActivity,viewPager);

        viewPager.setAdapter(viewPagerFruitAdapter);
        viewPager.setOffscreenPageLimit(5);
       // initPagerStrip(view);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //新的页面被选中时调用
                //onPageSelected会在onCretaeView调用前就执行
               /* viewPager.post(() -> {
                    String tag = "android:switcher:" + R.id.vp_content + ":" + position;
                    //使用findFragmentByTag才是fragmentManager实际管理的已加载的fragment，而getItem是一个新的fragment
                    FruitRecycleFragment fragment = (FruitRecycleFragment) getFragmentManager().findFragmentByTag(tag);
                    //FruitRecycleFragment fragment = (FruitRecycleFragment) viewPagerFruitAdapter.getItem(position);
                    if (fragment != null && fragment.isAdded()&&fragment.getView()!=null) {
                        Log.d("page", "onPageSelected: "+fragment);
                       // fragment.setOnTabLayoutReadyListener(tabLayout -> {
                             TabLayout tabLayout = fragment.getTabTitle();
                            if (tabLayout != null) {
                                TabLayout.Tab tab = tabLayout.getTabAt(position);
                                Log.d("page1", "onPageSelected: "+tab);
                                if (tab != null) {
                                    tabLayout.selectTab(tab);
                                }
                            } else {
                                Log.d("page1", "onPageSelected: false");
                            }
                        //});
                    } else {
                        Log.d("page", "onPageSelected: false2"+fragment);
                    }
                });*/


//livedata方法，tabLayoutLiveData 的值是在 onCreateView() 中设置的。如果 observe() 方法调用时，onCreateView() 尚未完成，可能导致 LiveData 的初始值未被观察到。
               /* FruitRecycleFragment fragment = (FruitRecycleFragment) viewPagerFruitAdapter.getItem(position);
                if (fragment != null) {
                    fragment.getTabLayoutLiveData().observe(getViewLifecycleOwner(),tabTitle -> {
                        Log.d("tablay", "onPageSelected: "+tabTitle);
                        if (tabTitle != null) {
                            TabLayout.Tab tab = tabTitle.getTabAt(position);
                            if (tab != null) {
                                tabTitle.selectTab(tab);
                            }
                        }
                    });
                }*/

                //回调的方法，延迟有点久
                /*String fragmentTag = "android:switcher:" + R.id.vp_content + ":" + position;
                FruitRecycleFragment fragment = (FruitRecycleFragment) getFragmentManager().findFragmentByTag(fragmentTag);

                if (fragment != null && fragment.isAdded()) {
                    // 设置监听器
                    fragment.setOnTabLayoutReadyListener(tabLayout -> {
                        if (tabLayout != null) {
                            TabLayout.Tab tab = tabLayout.getTabAt(position);
                            if (tab != null) {
                                tabLayout.selectTab(tab);
                            }
                        }
                    });
                } else {
                    Log.d("onPageSelected", "Fragment is null or not added");
                }*/

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }
    /*private void initPagerStrip(View view){
         pts_tab=view.findViewById(R.id.pts_tab);
        //设置翻页标签栏的文本大小
        pts_tab.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        pts_tab.setTextColor(Color.GREEN);
    }*/

}