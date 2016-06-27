package com.patrick.caracal.model;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.RawRes;

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
public class InitializationData extends Model{

    public InitializationData(Context context,Realm realm) {
        super(context,realm);
    }

    /**
     * 检测本地数据是否已初始化完成
     */
    public void initRequiredData(){
        //检测快递公司
        checkExpressCompanyReady();
    }

    /**
     * 检查快递公司是否已导入
     */
    private void checkExpressCompanyReady() {
        RealmResults<ExpressCompany> results = realm.where(ExpressCompany.class).findAll();
        if (results.isEmpty()) {
            importExpressCompany();
        }


    }

    /**
     * 导入快递公司
     */
    private void importExpressCompany(){

        realm.executeTransaction(loadDomesticExp); //国内数据

        realm.executeTransaction(loadForeignExp); //国外数据

        realm.executeTransaction(loadTransportExp); //转运数据

        //导入数据完毕后,关闭realm
        realm.close();
    }

    /**
     * 导入国内快递
     */
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

    /**
     * 导入国外快递
     */
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

    /**
     * 导入中转物流
     */
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

    /**
     * 获取raw目录下的文件流
     * @param res raw文件夹的文件
     * @return
     * @throws Resources.NotFoundException
     */
    private InputStream getRawJsonFile(@RawRes int res) throws Resources.NotFoundException {
        return context.getResources().openRawResource(res);
    }
}
