package com.patrick.caracal.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.patrick.caracal.R;
import com.patrick.caracal.entity.Express;
import com.patrick.caracal.entity.ExpressCompany;
import com.patrick.caracal.net.KDNiaoAPI;
import com.rey.material.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by junz on 2016/6/18.
 * 1、本界面主要是用户填写和选择需查询快递公司的信息
 */

public class QueryExpressActivity extends BaseActivity {

    private Dialog mDialog;
    private String streamInfo;
    private ProgressDialog mProgressDialog;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /**
     * 快递单号文本框
     */
    @BindView(R.id.input_exp_code)
    EditText inputExpCode;

    @OnClick(R.id.scan_exp_code)
    void scanExpBarcode() {
        /*
        * TODO 跳入扫码界面，然后当扫码完成后，跳回本界面，把扫码的String填充到 inputExpCode 里面
        */
        // 调用扫码界面
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, CAPTURE_REQ_CODE);
    }

    // 返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { //RESULT_OK = -1*

            if (requestCode == CAPTURE_REQ_CODE) {
                //扫码后的结果
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("result");
                inputExpCode.setText(scanResult);
                Toast.makeText(QueryExpressActivity.this, scanResult, Toast.LENGTH_LONG).show();
            } else if (requestCode == SELECT_REQ_CODE) {
                //选择快递公司结果
                String selectExpressResult = data.getStringExtra("SelectExpress");
                companyName.setText(selectExpressResult);
                Toast.makeText(QueryExpressActivity.this, selectExpressResult, Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * 快递公司名称
     */
    @BindView(R.id.choose_exp_company)
    TextView companyName;

    @OnClick(R.id.choose_exp_company)
    void selectCompany() {
        //TODO 选择快递公司，进入ExpCompanySelectActivity选择，选择后回到此界面把内容填充到 companyName里面
        //跳转到快递选择界面
        Intent intent = new Intent(QueryExpressActivity.this, ExpCompanySelectActivity.class);
        startActivityForResult(intent, SELECT_REQ_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        //返回按钮
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueryExpressActivity.this.finish();
            }
        });

        mProgressDialog = new ProgressDialog(QueryExpressActivity.this);
        mProgressDialog.setMessage("正在查询请稍等……");

        //弹框
        mDialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.dialog_stream_none_error))
                .setPositiveButton(getString(R.string.confirmation),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveOrUpdateData();
                                mDialog.dismiss();
                            }
                        })
                .setNegativeButton(getString(R.string.cencel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        streamInfo = null;
                        mDialog.dismiss();
                    }
                }).create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_express_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done:

                addExpress();
                break;
        }
        return true;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SELECT_SAVE_INFO:
                    mDialog.show();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 将查询结果进行保存
     */
    private void saveOrUpdateData() {
        realm.beginTransaction();
        realm.createOrUpdateAllFromJson(Express.class, "[" + streamInfo + "]");
        realm.commitTransaction();
        QueryExpressActivity.this.setResult(RESULT_OK);
        QueryExpressActivity.this.finish();
    }

    /**
     * 添加快递
     */
    private void addExpress() {
        // TODO 检查快递单和公司是否有选
        // 调用KDNiaoAPI.queryExp()进行查询快递
        // 在callback中，success就把结果保存到Realm
        // 如果callback到onFailure,弹出一个toast，然后用 showToast 把错误内容显示出来

        mProgressDialog.show();

        //根据快递公司名查询快递公司Code
        RealmResults<ExpressCompany> expressCompanys = realm
                .where(ExpressCompany.class)
                .equalTo("name", companyName.getText().toString()).findAll();
        if (expressCompanys.size() == 1) {
            try {
                KDNiaoAPI.queryExp(expressCompanys.get(0).code, inputExpCode.getText().toString(), mCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //快递查询Callback
    private Callback mCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            mProgressDialog.dismiss();
            showToast(e.toString());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.code() == 200) {

                streamInfo = response.body().string();

                try {
                    final JSONObject jsonObject = new JSONObject(streamInfo);
                    if (!TextUtils.isEmpty(jsonObject.optString("Reason"))) {
                        //如果Reason不为空
                        showToast(jsonObject.optString("Reason"));
                        mHandler.sendEmptyMessage(SELECT_SAVE_INFO);
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        Express express = realm.createOrUpdateObjectFromJson(Express.class, jsonObject);
                                        express.localState = Express.LOCAL_STATE_NORMAL;
                                    }
                                });
                                mProgressDialog.dismiss();
                                QueryExpressActivity.this.setResult(RESULT_OK);
                                QueryExpressActivity.this.finish();
                            }
                        });


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    };

    @Override
    int layout() {
        return R.layout.activity_express_add;
    }

}
