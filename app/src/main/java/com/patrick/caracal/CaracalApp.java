package com.patrick.caracal;

import android.app.Application;
import android.support.annotation.RawRes;

import com.patrick.caracal.entity.ExpressCompany;
import com.patrick.caracal.model.InitializationData;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by patrick on 16-6-14.
 */

public class CaracalApp extends Application {

    private InitializationData initializationData;

    @Override
    public void onCreate() {
        super.onCreate();

        initRealmDB();

        initializationData = new InitializationData(this);
        initializationData.initRequiredData();
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




}
