package com.patrick.caracal;

import android.app.Application;

import com.jiongbull.jlog.JLog;
import com.patrick.caracal.model.InitializationData;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by patrick on 16-6-14.
 */

public class CaracalApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initJLog();

        initRealmDB();

        InitializationData initializationData = new InitializationData(this,Realm.getDefaultInstance());
        initializationData.initRequiredData();
    }

    /**
     * 初始化JLog
     */
    private void initJLog() {
        JLog.init(this)
                .setDebug(true)
                .writeToFile(false);
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
