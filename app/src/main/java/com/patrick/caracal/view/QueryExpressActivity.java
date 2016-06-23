package com.patrick.caracal.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.patrick.caracal.Center;
import com.patrick.caracal.R;
import com.patrick.caracal.entity.ExpressCompany;
import com.patrick.caracal.view.adapter.HotExpressAdapter;
import com.patrick.caracal.view.iview.IHotExpressAdapter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by junz on 2016/6/18.
 */

public class QueryExpressActivity extends AppCompatActivity implements IHotExpressAdapter {

    private static int SELECT_EXPRESS = 100;

    private EditText etExpressNum;
    private LinearLayout linearHotExpress;
    private RecyclerView rvHotExpress;
    private GridLayoutManager mGridLayoutManager;
    private HotExpressAdapter mHotExpressAdapter;
    private int hotExpressWidth;

    private RealmChangeListener mRealmChangeListener = new RealmChangeListener() {

        @Override
        public void onChange(Object element) {
            mHotExpressAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_query);
        getSupportActionBar().hide();
        init();
    }

    private void init(){
        etExpressNum = (EditText) findViewById(R.id.etExpressNum);
        rvHotExpress = (RecyclerView) findViewById(R.id.rvHotExpress);
        linearHotExpress = (LinearLayout) findViewById(R.id.linearHotExpress);
        mGridLayoutManager = new GridLayoutManager(this,3);
        rvHotExpress.setLayoutManager(mGridLayoutManager);
        mHotExpressAdapter = new HotExpressAdapter(this, this);
        Center.allExpressCompanyList.addChangeListener(mRealmChangeListener);
        surveyControl();
    }
    private void surveyControl() {
        ViewTreeObserver vto = linearHotExpress.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                linearHotExpress.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //设置裁剪的尺寸
                hotExpressWidth= linearHotExpress.getWidth();
                mHotExpressAdapter.setHotExpressWidth(hotExpressWidth);
                rvHotExpress.setAdapter(mHotExpressAdapter);
            }
        });
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);


    }

    @Override
    public void selectedHotExpress(int p) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        RealmResults<ExpressCompany> expressCompanies = realm.where(ExpressCompany.class)
                .equalTo("selected", true).findAll();
        if (expressCompanies.size() > 0){
            expressCompanies.get(0).selected = false;
            Center.hotExpresslist.get(p).get(0).selected = true;
        }else {
            Center.hotExpresslist.get(p).get(0).selected = true;
        }
        realm.commitTransaction();
        realm.close();
        mHotExpressAdapter.notifyDataSetChanged();
    }

    @Override
    public void setectedMoreExpress() {
//        Center.hotExpresslist.get(position).select = false;
//        position = -1;
        Intent intent = new Intent(QueryExpressActivity.this, ExpressSelectActivity.class);
        startActivityForResult(intent, SELECT_EXPRESS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Center.allExpressCompanyList.removeChangeListener(mRealmChangeListener);
    }
}
