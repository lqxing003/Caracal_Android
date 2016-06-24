package com.patrick.caracal.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.patrick.caracal.Center;
import com.patrick.caracal.R;
import com.patrick.caracal.entity.ExpressCompany;
import com.patrick.caracal.view.adapter.EnActionBarAdapter;
import com.patrick.caracal.view.adapter.SelectExpressAdapter;
import com.patrick.caracal.view.iview.IEnActionBarAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by junz on 2016/6/19.
 */

public class SelectFragment extends Fragment implements IEnActionBarAdapter {


    private View mView;
    private LinearLayoutManager mLinearLayoutManager;
    private LinearLayoutManager nLinearLayoutManager;
    private SelectExpressAdapter mSelectExpressAdapter;
    private EnActionBarAdapter mEnActionBarAdapter;
    public List<RealmResults<ExpressCompany>> mRealmResultses;

    private String TAG = "SelectFragment";
    private RealmChangeListener mRealmChangeListener = new RealmChangeListener() {

        @Override
        public void onChange(Object element) {
            mSelectExpressAdapter.notifyDataSetChanged();
        }
    };

    private int frameActionbarHeight;

    @BindView(R.id.frameEnActionbar) FrameLayout frameEnActionbar;
    @BindView(R.id.rvDomestic) RecyclerView rvDomestic;
    @BindView(R.id.rvEnNameActionbar) RecyclerView rvEnNameActionbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_select_express, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Center.selectExpress.addChangeListener(mRealmChangeListener);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mSelectExpressAdapter = new SelectExpressAdapter(getContext(), mRealmResultses);
        rvDomestic.setLayoutManager(mLinearLayoutManager);
        rvDomestic.setAdapter(mSelectExpressAdapter);

        nLinearLayoutManager = new LinearLayoutManager(getContext());

        rvEnNameActionbar.setLayoutManager(nLinearLayoutManager);


        surveyControl();
    }

    private void surveyControl() {
        ViewTreeObserver vto = frameEnActionbar.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                frameEnActionbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //设置裁剪的尺寸
                frameActionbarHeight= frameEnActionbar.getHeight();
                mEnActionBarAdapter = new EnActionBarAdapter(getContext(),
                                                            mRealmResultses,
                                                            SelectFragment.this,
                                                            frameActionbarHeight);
                rvEnNameActionbar.setAdapter(mEnActionBarAdapter);
            }
        });
    }
    @Override
    public void onClickEnName(int p) {

        rvDomestic.scrollToPosition(p);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Center.selectExpress.removeChangeListener(mRealmChangeListener);
    }
}
