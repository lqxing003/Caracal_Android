package com.patrick.caracal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.patrick.caracal.MainActivity;
import com.patrick.caracal.view.ExpressSelectActivity;
import com.patrick.caracal.R;
import com.patrick.caracal.view.QueryExpressActivity;
import android.widget.TextView;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.patrick.caracal.R;
import com.patrick.caracal.entity.Express;
import com.patrick.caracal.entity.ExpressCompany;
import com.patrick.caracal.entity.Trace;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Patrick on 16/6/13.
 */

public class HomeFragment extends Fragment {

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

    @BindView(R.id.express_list)
    ExpandingList express_listview;

    private Realm realm;

    //加载本地的快递单
    private RealmResults<Express> localExpressResults;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
        //加载快递单的UI列表
        loadExpressLayout();
        return view;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//    }

    private void loadExpressLayout() {
        for (int i = 0; i < localExpressResults.size(); i++) {
            Express express = localExpressResults.get(i);
            ExpandingItem item = express_listview.createNewItem(R.layout.express_expanding_layout);

            item.setIndicatorColorRes(R.color.colorPrimary);
            item.setIndicatorIconRes(R.drawable.ic_express_24dp);

            String companyName = getExpressCompanyName(express.ShipperCode);

            ((TextView)item.findViewById(R.id.logisticCode))
                    .setText("单号："+express.LogisticCode); //快递单

            ((TextView)item.findViewById(R.id.companyName))
                    .setText(companyName);  //快递公司

            RealmList<Trace> traces = express.Traces;
            item.createSubItems(traces.size());
            for (int j = traces.size()-1,k=0; j >= 0; j--,k++) {
                Trace trace = traces.get(j);
                View sub_item = item.getSubItemView(k);
                ((TextView)sub_item.findViewById(R.id.acceptStation)).setText(trace.AcceptStation);

                if (k == 0) ((TextView)sub_item.findViewById(R.id.acceptStation)).setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }

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

}
