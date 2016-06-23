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

import com.patrick.caracal.Center;
import com.patrick.caracal.R;
import com.patrick.caracal.entity.ExpressCompany;
import com.patrick.caracal.view.adapter.EnActionBarAdapter;
import com.patrick.caracal.view.adapter.SelectExpressAdapter;
import com.patrick.caracal.view.iview.IEnActionBarAdapter;

import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by junz on 2016/6/19.
 */

public class SelectFragment extends Fragment implements IEnActionBarAdapter {

    private RecyclerView rvDomestic;
    private RecyclerView rvEnNameActionbar;
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_select_express, container, false);
        rvDomestic = (RecyclerView) mView.findViewById(R.id.rvDomestic);
        rvEnNameActionbar = (RecyclerView) mView.findViewById(R.id.rvEnNameActionbar);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Center.allExpressCompanyList.addChangeListener(mRealmChangeListener);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mSelectExpressAdapter = new SelectExpressAdapter(getContext(), mRealmResultses);
        rvDomestic.setLayoutManager(mLinearLayoutManager);
        rvDomestic.setAdapter(mSelectExpressAdapter);

        nLinearLayoutManager = new LinearLayoutManager(getContext());
        mEnActionBarAdapter = new EnActionBarAdapter(getContext(), mRealmResultses, this);
        rvEnNameActionbar.setLayoutManager(nLinearLayoutManager);
        rvEnNameActionbar.setAdapter(mEnActionBarAdapter);

        rvDomestic.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                Log.i(TAG, "onScrollStateChanged: "+ mSelectExpressAdapter.getItemCount());
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
        Center.allExpressCompanyList.removeChangeListener(mRealmChangeListener);
    }
}
