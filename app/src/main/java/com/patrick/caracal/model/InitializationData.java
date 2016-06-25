package com.patrick.caracal.model;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.RawRes;

import com.patrick.caracal.Center;
import com.patrick.caracal.R;
import com.patrick.caracal.entity.ExpressCompany;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Patrick on 16/6/25.
 *
 * 初始化数据的model
 *
 */

public class InitializationData {

    private Context context;

    public InitializationData(Context context) {
        this.context = context;
    }

    //检测本地数据是否已初始化完成
    public void initRequiredData(){
        //检测快递公司
        checkExpressCompanyReady();
    }

    private void checkExpressCompanyReady() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ExpressCompany> results = realm.where(ExpressCompany.class).findAll();
        if (results.isEmpty()) {
            importExpressCompany();
        }
        Center.init();
        realm.close();
    }

    /**
     * 导入快递公司
     */
    private void importExpressCompany(){
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(loadDomesticExp); //国内数据

        realm.executeTransaction(loadForeignExp); //国外数据

        realm.executeTransaction(loadTransportExp); //转运数据

        realm.close();
    }


    private Realm.Transaction loadDomesticExp = new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
            try {
                InputStream inputStream = getRawJsonFile(R.raw.domestic_express);
                realm.createAllFromJson(ExpressCompany.class, inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Realm.Transaction loadForeignExp = new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
            try {
                InputStream inputStream = getRawJsonFile(R.raw.foreign_express);
                realm.createAllFromJson(ExpressCompany.class, inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private Realm.Transaction loadTransportExp = new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
            try {
                InputStream inputStream = getRawJsonFile(R.raw.transport_express);
                realm.createAllFromJson(ExpressCompany.class, inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    private InputStream getRawJsonFile(@RawRes int res) throws Resources.NotFoundException {
        return context.getResources().openRawResource(res);
    }
}
