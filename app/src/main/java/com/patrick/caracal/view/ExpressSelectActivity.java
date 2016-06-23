package com.patrick.caracal.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.patrick.caracal.Center;
import com.patrick.caracal.R;
import com.patrick.caracal.fragment.SelectFragment;
import com.patrick.caracal.listview.MainListView;
import com.patrick.caracal.listview.MainListViewAdapter;
import com.patrick.caracal.view.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by junz on 2016/6/15.
 */

public class ExpressSelectActivity extends AppCompatActivity {

    private String TAG = "ExpressSelectActivity";

    private ViewPager mViewPager;
    private FragmentAdapter mFragmentAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private SelectFragment domesticFragment;
    private SelectFragment foreignFragment;
    private SelectFragment transportFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_select);
        getSupportActionBar().hide();

        mViewPager = (ViewPager) findViewById(R.id.vpExpressSelect);
        initFragment();
//        mViewPager.addOnPageChangeListener(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
