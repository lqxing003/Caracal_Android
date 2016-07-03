package com.patrick.caracal.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.patrick.caracal.R;
import com.patrick.caracal.activity.ParcelInfoActivity;
import com.patrick.caracal.activity.QueryExpressActivity;

import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.patrick.caracal.entity.Express;
import com.patrick.caracal.entity.ExpressCompany;
import com.patrick.caracal.entity.Trace;
import com.patrick.caracal.view.adapter.ParcelListAdapter;
import com.patrick.caracal.view.widget.SwipeToAction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Patrick on 16/6/13.
 */

public class HomeFragment extends Fragment {

    //fragment的root layout
    @BindView(R.id.fragment_home_layout)
    CoordinatorLayout home_layout;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.multiple_actions)
    FloatingActionsMenu multiple_actions;

    @BindView(R.id.btn_add_test_data)
    FloatingActionButton btn_add_test_data;

    @OnClick(R.id.btn_add_test_data)
    void importTestData() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        realm.where(Express.class).findAll().first().localState = Express.LOCAL_STATE_NORMAL;
        realm.commitTransaction();

        multiple_actions.collapse();
    }

    @OnClick(R.id.manual_add_express)
    void manualAddExpressNumber() {
        Intent intent = new Intent(getActivity(), QueryExpressActivity.class);
        startActivityForResult(intent, 100);

        multiple_actions.collapse();
    }

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private Realm realm;

    //加载本地的快递单
    private RealmResults<Express> localExpressResults;

    private ParcelListAdapter adapter;

    private SwipeToAction swipeParcelList;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();

        //加载本地的快递列表
        loadLocalExpress();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    /**
     * 从Realm读取快递单
     */
    private void loadLocalExpress() {
        localExpressResults = realm.where(Express.class)
                .equalTo("localState", Express.LOCAL_STATE_NORMAL)
                .findAll();
        localExpressResults.addChangeListener(listener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        //RecyclerView 初始化
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new ParcelListAdapter(parcelProvider);
        recyclerView.setAdapter(adapter);

        //滑动快递列表的事件
        swipeParcelList = new SwipeToAction(recyclerView, swipeListener);

        //设置下拉刷新的事件
        swipeRefreshLayout.setOnRefreshListener(swipeRefresh);
        return view;
    }


    /**
     * 获取快递公司名
     *
     * @param shipperCode 快递公司编码
     * @return
     */
    private String getExpressCompanyName(String shipperCode) {
        ExpressCompany company = realm.where(ExpressCompany.class).equalTo("code", shipperCode).findFirst();
        if (company != null) {
            return company.name;
        } else {
            return "未知快递";
        }
    }

    /**
     * 提供给快递列表的Adapter使用
     */
    private ParcelListAdapter.ParcelProvider parcelProvider = new ParcelListAdapter.ParcelProvider() {
        @Override
        public int getParcelCount() {
            return localExpressResults.size();
        }

        @Override
        public Express getParcel(int position) {
            return localExpressResults.get(position);
        }

        @Override
        public String getCompanyName(int position) {
            String shipperCode = localExpressResults.get(position).ShipperCode;

            return getExpressCompanyName(shipperCode);
        }

        @Override
        public String getLogistcCode(int position) {
            return localExpressResults.get(position).LogisticCode;
        }

        @Override
        public String getAcceptStation(int position) {
            //快递行程
            RealmList<Trace> traces = localExpressResults.get(position).Traces;
            if (traces.isEmpty()) {
                //如果快递行程是空,返回 "暂无数据"
                return "暂无数据";
            } else {
                return traces.last().AcceptStation;
            }
        }

        @Override
        public String getAcceptTime(int position) {
            //快递行程
            RealmList<Trace> traces = localExpressResults.get(position).Traces;
            if (traces.isEmpty()) {
                return "暂无时间";
            } else {
                return traces.last().AcceptTime;
            }
        }
    };

    /**
     * 滑动事件
     */
    private SwipeToAction.SwipeListener<Express> swipeListener = new SwipeToAction.SwipeListener<Express>() {
        @Override
        public boolean swipeLeft(final Express itemData) {
            //归档
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    itemData.localState = Express.LOCAL_STATE_ARCHIVE;
                }
            });
            return true;
        }

        @Override
        public boolean swipeRight(final Express itemData) {
            //进入垃圾桶
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    itemData.localState = Express.LOCAL_STATE_TRASH;
                }
            });

            displaySnackbar(itemData.LogisticCode + "已删除", "撤销", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            itemData.localState = Express.LOCAL_STATE_NORMAL;
                        }
                    });
                }
            });
            return true;
        }

        @Override
        public void onClick(Express itemData) {
            //进入详情
            Intent intent = new Intent(getContext(), ParcelInfoActivity.class);
            intent.putExtra("parcelId", itemData.LogisticCode);
            startActivity(intent);
        }

        @Override
        public void onLongClick(Express itemData) {
            //暂时没有长按
        }
    };

    private SwipeRefreshLayout.OnRefreshListener swipeRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //下拉刷新,把当前的 localExpressResults 遍历去更新状态

        }
    };

    private RealmChangeListener listener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    };

    private void displaySnackbar(String text, String actionName, View.OnClickListener action) {


//        if (recyclerView != null) {
            Snackbar snack = Snackbar.make(home_layout, text, Snackbar.LENGTH_LONG).setAction(actionName, action);

            View v = snack.getView();
            v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            ((TextView) v.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
            ((TextView) v.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(Color.BLACK);

            snack.show();
//        }


    }
}
