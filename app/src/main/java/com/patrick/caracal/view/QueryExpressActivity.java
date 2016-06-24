package com.patrick.caracal.view;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.patrick.caracal.Center;
import com.patrick.caracal.R;
import com.patrick.caracal.entity.Express;
import com.patrick.caracal.entity.ExpressCompany;
import com.patrick.caracal.net.KDNiaoAPI;
import com.patrick.caracal.view.adapter.HotExpressAdapter;
import com.patrick.caracal.view.iview.IHotExpressAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by junz on 2016/6/18.
 * 1、本界面主要是用户填写和选择需查询快递公司的信息
 */

public class QueryExpressActivity extends AppCompatActivity implements IHotExpressAdapter {

    private static int SELECT_EXPRESS = 100;

    private GridLayoutManager mGridLayoutManager;
    private HotExpressAdapter mHotExpressAdapter;
    private int hotExpressWidth;
    private ProgressDialog mProgressDialog;

    @BindView(R.id.tvSelectExpress)
    TextView tvSelectExpress;
    @BindView(R.id.etExpressNum)
    EditText etExpressNum;
    @BindView(R.id.linearHotExpress)
    LinearLayout linearHotExpress;
    @BindView(R.id.rvHotExpress)
    RecyclerView rvHotExpress;


    @OnClick(R.id.tvComeback) void onClickComeback(){
        finish();
    }

    //用户完成数据填写和选择后进行查询请求
    @OnClick(R.id.tvComplete) void onClickComplete(){

        if (!etExpressNum.getText().toString().equals("") && Center.selectExpress.size() > 0){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage(getString(R.string.progress_msg_query));
            mProgressDialog.show();
            try {
                KDNiaoAPI.queryExp(
                        Center.selectExpress.get(0).code,
                        etExpressNum.getText().toString(),
                        mCallback);
            } catch (Exception e) {
                mProgressDialog.dismiss();
                e.printStackTrace();
            }
        }else if (etExpressNum.getText().toString().equals("")){
            Toast.makeText(QueryExpressActivity.this, "物流单不能为空！", Toast.LENGTH_SHORT).show();
        }else if (Center.selectExpress.size() == 0){
            Toast.makeText(QueryExpressActivity.this, "请选择快递公司！", Toast.LENGTH_SHORT).show();
        }

    }

    //realm监听事件
    private RealmChangeListener mRealmChangeListener = new RealmChangeListener() {

        @Override
        public void onChange(Object element) {
            mHotExpressAdapter.notifyDataSetChanged();
            changeSelectExpress();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_query);
        ButterKnife.bind(this);
        //隐藏ActionBar
        getSupportActionBar().hide();

        init();
    }

    private void init(){

        changeSelectExpress();

        mGridLayoutManager = new GridLayoutManager(this,3);
        rvHotExpress.setLayoutManager(mGridLayoutManager);
        mHotExpressAdapter = new HotExpressAdapter(this, this);
        //对Center.selectExpress进行数据变化监听
        Center.selectExpress.addChangeListener(mRealmChangeListener);
        surveyControl();
    }

    /**
     * 测量指定控件尺寸
     */
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

    /**
     * 改变tvSelectExpress内容
     */
    private void changeSelectExpress(){
        if (Center.selectExpress.size() > 0){
            tvSelectExpress.setText("已选择：" + Center.selectExpress.get(0).name);
        }else {
            tvSelectExpress.setText("请选择快递公司");
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);

    }

    /**
     * 点击rvHotExpress的item回调事件
     * @param p 点击item位置
     */
    @Override
    public void selectedHotExpress(int p) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (Center.selectExpress.size() > 0){
            Center.selectExpress.get(0).selected = false;
            Center.hotExpresslist.get(p).get(0).selected = true;
        }else {
            Center.hotExpresslist.get(p).get(0).selected = true;
        }
        realm.commitTransaction();
        realm.close();
        mHotExpressAdapter.notifyDataSetChanged();
    }

    /**
     * 跳转至更多快递公司选择
     */
    @Override
    public void setectedMoreExpress() {
//        Center.hotExpresslist.get(position).select = false;
//        position = -1;
        Intent intent = new Intent(QueryExpressActivity.this, SelectExpressActivity.class);
        startActivityForResult(intent, SELECT_EXPRESS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeSelectExpress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Center.selectExpress.removeChangeListener(mRealmChangeListener);
    }

    //网络请求回调
    private Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            mProgressDialog.dismiss();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            if (response.code() == 200){

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
//                Express express = realm.createObject(Express.class);
//                express.LogisticCode =  etExpressNum.getText().toString();
//                express.ShipperCode = Center.selectExpress.get(0).code;

                realm.createOrUpdateAllFromJson(Express.class, "[" + response.body().string() + "]");

                realm.commitTransaction();
                realm.close();
                mProgressDialog.dismiss();
                finish();
            }else {
                mProgressDialog.dismiss();
            }

        }
    };
}
