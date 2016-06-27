package com.patrick.caracal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.patrick.caracal.R;
import com.rey.material.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by junz on 2016/6/18.
 * 1、本界面主要是用户填写和选择需查询快递公司的信息
 */

public class QueryExpressActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /**
     * 快递单号文本框
     */
    @BindView(R.id.input_exp_code)
    EditText inputExpCode;

    @OnClick(R.id.scan_exp_code) void scanExpBarcode(){
        /*
        * TODO 跳入扫码界面，然后当扫码完成后，跳回本界面，把扫码的String填充到 inputExpCode 里面
        *
        * 调用扫码界面
        * Intent intent = new Intent(this, CaptureActivity.class);
        * startActivityForResult(intent, REQUEST_CODE);
        *
        * 返回结果
        *
        * @Override
        * protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        *     super.onActivityResult(requestCode, resultCode, data);
        *         if(resultCode == RESULT_OK){ //RESULT_OK = -1
        *             Bundle bundle = data.getExtras();
        *             //扫码后的结果
        *             String scanResult = bundle.getString("result");
        *             Toast.makeText(MainActivity.this, scanResult, Toast.LENGTH_LONG).show();
        *    }
        *}
        * */
    }

    /**
     * 快递公司名称
     */
    @BindView(R.id.choose_exp_company)
    TextView companyName;

    @OnClick(R.id.choose_exp_company) void selectCompany(){
        //TODO 选择快递公司，进入ExpCompanySelectActivity选择，选择后回到此界面把内容填充到 companyName里面
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_express_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.done:
                //TODO 点击右上角完成后的事件
                addExpress();
                break;
        }
        return true;
    }

    /**
     * 添加快递
     */
    private void addExpress() {
        // TODO 检查快递单和公司是否有选
        // 调用KDNiaoAPI.queryExp()进行查询快递
        // 在callback中，success就把结果保存到Realm
        // 如果callback到onFailure,弹出一个toast，然后用 showToast 把错误内容显示出来

    }

    @Override
    int layout() {
        return R.layout.activity_express_add;
    }

}
