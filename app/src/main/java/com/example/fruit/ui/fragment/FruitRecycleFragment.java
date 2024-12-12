package com.example.fruit.ui.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fruit.MyApplication;
import com.example.fruit.R;
import com.example.fruit.adapter.FruitAdapter;
import com.example.fruit.bean.Fruit;
import com.example.fruit.ui.activity.AddFruitActivity;
import com.example.fruit.ui.activity.FruitDetailActivity;
import com.example.fruit.ui.activity.LoginActivity;
import com.example.fruit.util.KeyBoardUtil;
import com.example.fruit.util.SPUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FruitRecycleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FruitRecycleFragment extends Fragment {
    private Activity myActivity;//上下文

    public TabLayout getTabTitle() {
        return tabTitle;
    }

    public void setTabTitle(TabLayout tabTitle) {
        this.tabTitle = tabTitle;
    }

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
    private Context context;
    private int position;
    private ViewPager viewPager;

    public FruitRecycleFragment(Context context, int position, ViewPager viewPager) {
        // Required empty public constructor
        this.context=context;
        this.position=position;
        this.viewPager=viewPager;
    }


    public static FruitRecycleFragment newInstance(Context context,int position,ViewPager viewPager) {
        FruitRecycleFragment fragment = new FruitRecycleFragment(context,position,viewPager);
        Bundle args = new Bundle();
        //防止现存状态丢失，导致内容出错
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_fruit_recycle,container,false);
        //tabTitle = (TabLayout)view.findViewById(R.id.tab_title);

        //livedata方法，没用
        //tabLayoutLiveData.setValue(tabTitle);

        Log.d("tab", "onCreateView: "+tabLayoutListener);
        /*if (tabLayoutListener != null) {
            tabLayoutListener.onTabLayoutReady(tabTitle);
        }*/
        rvfruitList = (RecyclerView)view.findViewById(R.id.rv_fruit_list);
        llEmpty = view.findViewById(R.id.ll_empty);
        etQuery=view.findViewById(R.id.et_query);
        ivSearch=view.findViewById(R.id.iv_search);
        btnAdd = (FloatingActionButton)view.findViewById(R.id.btn_add);
        //获取控件
        initView();
        //软键盘搜索
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();//加载数据
            }
        });
        //点击软键盘中的搜索
        etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtil.hideKeyboard(v);//隐藏软键盘
                    loadData();//加载数据
                    return true;
                }
                return false;
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddFruitActivity.class);
                startActivityForResult(intent,100);
            }
        });
        return view;
    }

    /**
     * 初始化页面
     */
    private void initView() {
        mIsAdmin = (Boolean) SPUtils.get(context, SPUtils.IS_ADMIN, false);
        btnAdd.setVisibility(mIsAdmin? View.VISIBLE: View.GONE);
        /*tabTitle.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置tablayout距离上下左右的距离
        // tabTitle.setPadding(20,20,20,20);


        // 绑定 TabLayout 和 ViewPager,简单粗暴，不要设置监听器
        tabTitle.setupWithViewPager(viewPager);*/


        //为TabLayout添加tab名称
      /*  for (int i=0;i<title.length;i++){
            tabTitle.addTab(tabTitle.newTab().setText(title[i]));
        }*/


        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvfruitList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        mfruitAdapter=new FruitAdapter(llEmpty,rvfruitList);
        //=2.3、设置recyclerView的适配器
        rvfruitList.setAdapter(mfruitAdapter);
        loadData();
        mfruitAdapter.setItemListener(new FruitAdapter.ItemListener() {
            @Override
            public void ItemClick(Fruit fruit) {
                boolean isAdmin = (boolean) SPUtils.get(context,SPUtils.IS_ADMIN,false);
                String account = (String) SPUtils.get(context,SPUtils.ACCOUNT,"");
                if ("".equals(account)) {//未登录,跳转到登录页面
                    MyApplication.Instance.getMainActivity().finish();
                    startActivity(new Intent(context, LoginActivity.class));
                }else {//已经登录
                    Intent intent;
                    if (isAdmin){
                        intent = new Intent(context, AddFruitActivity.class);
                    }else {
                        intent = new Intent(context, FruitDetailActivity.class);
                    }
                    intent.putExtra("fruit",fruit);
                    startActivityForResult(intent,100);
                }
            }
        });
        /*tabTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //position=tab.getPosition();
                //typeId = state[tab.getPosition()];
               // viewPager.setCurrentItem(position);


               // loadData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
    }
    private void loadData(){
        typeId=String.valueOf(position);
        String content=etQuery.getText().toString();//获取搜索内容
        if ("".equals(content) ){
            mfruit = DataSupport.where("typeId = ?",typeId).find(Fruit.class);
        }else {
            mfruit =DataSupport.where("typeId = ? and title like ?" ,typeId,"%"+content+"%").find(Fruit.class);//通过标题模糊查询留言
        }
        if (mfruit !=null && mfruit.size()>0){
            rvfruitList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            mfruitAdapter.addItem(mfruit);
        }else {
            rvfruitList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            loadData();//加载数据
        }
    }
//回调带FruittFragment的方法，没用
    private OnTabLayoutReadyListener tabLayoutListener;

    public interface OnTabLayoutReadyListener {
        void onTabLayoutReady(TabLayout tabLayout);
    }
    public void setOnTabLayoutReadyListener(OnTabLayoutReadyListener listener) {
        this.tabLayoutListener = listener;
        // 如果视图已经创建，立即触发回调
        if (tabTitle != null) {
            Log.d("tab", "setOnTabLayoutReadyListener: Triggering callback immediately");
            tabLayoutListener.onTabLayoutReady(tabTitle);
        }
    }


    //livedata方法，没用
    private MutableLiveData<TabLayout> tabLayoutLiveData = new MutableLiveData<>();
    public LiveData<TabLayout> getTabLayoutLiveData() {
        return tabLayoutLiveData;
    }

}