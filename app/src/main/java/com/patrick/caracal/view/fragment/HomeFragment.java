package com.patrick.caracal.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrick.caracal.R;
import com.patrick.caracal.activity.QueryExpressActivity;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.patrick.caracal.entity.Express;
import com.patrick.caracal.entity.ExpressCompany;
import com.patrick.caracal.entity.Trace;
import com.patrick.caracal.view.adapter.ParcelListAdapter;
import com.patrick.caracal.view.widget.SwipeToAction;

import java.io.IOException;
import java.io.InputStream;

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

public class HomeFragment extends Fragment{

    @BindView(R.id.btn_add_test_data)
    FloatingActionButton btn_add_test_data;

    @OnClick(R.id.btn_add_test_data) void importTestData(){
        //导入测试数据
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        InputStream inputStream = this.getResources().openRawResource(R.raw.test_data);
        try {
            realm.createOrUpdateAllFromJson(Express.class,inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            realm.commitTransaction();
            realm.close();
        }
    }

    @OnClick(R.id.manual_add_express) void manualAddExpressNumber(){
        Intent intent = new Intent(getActivity(), QueryExpressActivity.class);
        startActivityForResult(intent, 100);
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
    private void loadLocalExpress(){
        localExpressResults = realm.where(Express.class).findAll();
        localExpressResults.addChangeListener(listener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);

        //RecyclerView 初始化
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new ParcelListAdapter(parcelProvider);
        recyclerView.setAdapter(adapter);

        //滑动快递列表的事件
        swipeParcelList = new SwipeToAction(recyclerView, swipeListener);

        return view;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//    }

//    private void loadExpressLayout() {
//        for (int i = 0; i < localExpressResults.size(); i++) {
//            Express express = localExpressResults.get(i);
//            ExpandingItem item = express_listview.createNewItem(R.layout.express_expanding_layout);
//
//            item.setIndicatorColorRes(R.color.colorPrimary);
//            item.setIndicatorIconRes(R.drawable.ic_express_24dp);
//
//            String companyName = getExpressCompanyName(express.ShipperCode);
//
//            ((TextView)item.findViewById(R.id.logisticCode))
//                    .setText("单号："+express.LogisticCode); //快递单
//
//            ((TextView)item.findViewById(R.id.companyName))
//                    .setText(companyName);  //快递公司
//
//            RealmList<Trace> traces = express.Traces;
//            item.createSubItems(traces.size());
//            for (int j = traces.size()-1,k=0; j >= 0; j--,k++) {
//                Trace trace = traces.get(j);
//                View sub_item = item.getSubItemView(k);
//                ((TextView)sub_item.findViewById(R.id.acceptStation)).setText(trace.AcceptStation);
//
//                if (k == 0) ((TextView)sub_item.findViewById(R.id.acceptStation)).setTextColor(getResources().getColor(R.color.colorPrimary));
//            }
//        }
//    }


    /**
     * 获取快递公司名
     * @param shipperCode 快递公司编码
     * @return
     */
    private String getExpressCompanyName(String shipperCode){
        ExpressCompany company = realm.where(ExpressCompany.class).equalTo("code",shipperCode).findFirst();
        if (company !=null){
            return company.name;
        }else {
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
            if (traces.isEmpty()){
                //如果快递行程是空,返回 "暂无数据"
                return "暂无数据";
            }else {
                return traces.first().AcceptStation;
            }
        }

        @Override
        public String getAcceptTime(int position) {
            //快递行程
            RealmList<Trace> traces = localExpressResults.get(position).Traces;
            if (traces.isEmpty()){
                return "暂无时间";
            }else {
                return traces.first().AcceptTime;
            }
        }
    };

    /**
     * 滑动事件
     */
    private SwipeToAction.SwipeListener<Express> swipeListener =new SwipeToAction.SwipeListener<Express>() {
        @Override
        public boolean swipeLeft(final Express itemData) {
            //删除

            return true;
        }

        @Override
        public boolean swipeRight(final Express itemData) {
            //归档
            return true;
        }

        @Override
        public void onClick(Express itemData) {

        }

        @Override
        public void onLongClick(Express itemData) {

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
}
