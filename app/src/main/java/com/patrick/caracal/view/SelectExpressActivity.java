package com.patrick.caracal.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.patrick.caracal.Center;
import com.patrick.caracal.R;
import com.patrick.caracal.fragment.SelectFragment;

import com.patrick.caracal.view.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by junz on 2016/6/15.
 */

public class SelectExpressActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private String TAG = "SelectExpressActivity";

    private ViewPager mViewPager;
    private FragmentAdapter mFragmentAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private SelectFragment domesticFragment;
    private SelectFragment foreignFragment;
    private SelectFragment transportFragment;
    private int currentIndex;
    private int screenWidth;

    @BindView(R.id.tabLineIv) ImageView mTabLineIv;
    @BindView(R.id.linearExpressType) LinearLayout linearExpressType;
    @OnClick(R.id.tvComeback) void onClickComeback(){
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_select);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        mViewPager = (ViewPager) findViewById(R.id.vpExpressSelect);
        initFragment();
        mViewPager.addOnPageChangeListener(this);
        surveyControl();
    }

    private void initFragment(){

        domesticFragment = new SelectFragment();
        domesticFragment.mRealmResultses = Center.domesticEexpressList;
        foreignFragment = new SelectFragment();
        foreignFragment.mRealmResultses = Center.foreignEexpressList;
        transportFragment = new SelectFragment();
        transportFragment.mRealmResultses = Center.transportEexpressList;

        mFragmentList.add(domesticFragment);
        mFragmentList.add(foreignFragment);
        mFragmentList.add(transportFragment);

        mFragmentAdapter = new FragmentAdapter(
                this.getSupportFragmentManager(),
                mFragmentList
        );

        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setCurrentItem(0);
    }

    private void surveyControl() {
        ViewTreeObserver vto = linearExpressType.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                linearExpressType.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //设置裁剪的尺寸
                screenWidth = linearExpressType.getWidth();
                mTabLineIv.getLayoutParams().width = screenWidth/3;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //以下三个方法是viewPager的滑动监听事件
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();

        /**
         * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
         * 设置mTabLineIv的左边距 滑动场景：
         * 记3个页面,
         * 从左到右分别为0,1,2
         * 0->1; 1->2; 2->1; 1->0
         */

        if (currentIndex == 0 && position == 0)// 0->1
        {
            lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 3) + currentIndex
                    * (screenWidth / 3));

        } else if (currentIndex == 1 && position == 0) // 1->0
        {
            lp.leftMargin = (int) (-(1 - positionOffset)
                    * (screenWidth * 1.0 / 3) + currentIndex
                    * (screenWidth / 3));

        } else if (currentIndex == 1 && position == 1) // 1->2
        {
            lp.leftMargin = (int) (positionOffset * (screenWidth * 1.0 / 3) + currentIndex
                    * (screenWidth / 3));
        } else if (currentIndex == 2 && position == 1) // 2->1
        {
            lp.leftMargin = (int) (-(1 - positionOffset)
                    * (screenWidth * 1.0 / 3) + currentIndex
                    * (screenWidth / 3));
        }
        mTabLineIv.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
        currentIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
