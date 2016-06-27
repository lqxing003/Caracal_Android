package com.patrick.caracal.model;

import android.content.Context;

import com.patrick.caracal.entity.ExpCompanyShowEntity;
import com.patrick.caracal.entity.ExpressCompany;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Patrick on 16/6/26.
 *
 * 快递公司有关的model
 */
public class ExpCompanyModel extends Model {

    //全部的快递列表(Adapter显示用的)
    private List<ExpCompanyShowEntity> expCompanyList = new ArrayList<>();

    //热门的快递列表(Adapter显示用的)
    private List<ExpCompanyShowEntity> hotCompanyList = new ArrayList<>();

    public ExpCompanyModel(Context context, Realm realm) {
        super(context, realm);

        initExpCompanyData();
    }

    /**
     * 初始化快递公司列表数据
     * expCompanyList 和 hotCompanyList
     */
    private void initExpCompanyData() {
        RealmResults<ExpressCompany> results = realm.where(ExpressCompany.class).findAll();
        for (ExpressCompany company :
                results) {
            ExpCompanyShowEntity entity = new ExpCompanyShowEntity(company.name, company.code);
            if (company.hot) {
                //如果是热门的快递,就添加一份的热门快递列表里面
                hotCompanyList.add(entity);
            } else {
                expCompanyList.add(entity);
            }
        }
    }

    /**
     * @return 全部快递公司列表
     */
    public List<ExpCompanyShowEntity> getAllExpCompany() {
        return expCompanyList;
    }

    /**
     * @return 热门的快递公司列表
     */
    public List<ExpCompanyShowEntity> getHotExpCompany(){
        return hotCompanyList;
    }

    /**
     * 获取全部(Realm)快递公司
     * @param realm
     * @return
     */
    public RealmResults<ExpressCompany> getAllRealmExpressCompany(Realm realm){
        return realm.where(ExpressCompany.class).findAll();
    }

    /**
     * 获取热门的(Realm)快递公司
     * @param realm
     * @return
     */
    public RealmResults<ExpressCompany> getHotRealmExpressCompany(Realm realm){
        return realm.where(ExpressCompany.class).equalTo("hot",true).findAll();
    }

}
