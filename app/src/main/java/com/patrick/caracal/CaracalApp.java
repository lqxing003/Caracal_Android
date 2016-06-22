package com.patrick.caracal;

import android.app.Application;
import android.support.annotation.RawRes;

import com.patrick.caracal.entity.ExpressCompany;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by patrick on 16-6-14.
 */

public class CaracalApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initRealmDB();

        checkRequiredData();
    }

    /**
     * 初始化RealmDB
     */
    private void initRealmDB() {
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    /**
     * 检查需要的数据是否导入
     * 根据快递公司的数量去判断是否有导入快递公司编码
     */
    private void checkRequiredData() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ExpressCompany> results = realm.where(ExpressCompany.class).findAll();
        if (results.isEmpty()) {
            loadExpressCompany();
        }
        Center.init();
    }

    private void loadExpressCompany() {
        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(loadDomesticExp); //国内数据

        realm.executeTransaction(loadForeignExp); //国外数据

        realm.executeTransaction(loadTransportExp); //转运数据
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

    private InputStream getRawJsonFile(@RawRes int res) {
        return this.getResources().openRawResource(res);
    }
}
