package com.patrick.caracal.entity;

import io.realm.RealmModel;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

/**
 * Created by patrick on 16-6-14.
 * 快递公司数据
 */
@RealmClass
public class ExpressCompany implements RealmModel{

    //国内
    public static final int EXPRESS_TYPE_DOMESTIC = 1;
    //国外
    public static final int EXPRESS_TYPE_FOREIGN = 2;
    //转运
    public static final int EXPRESS_TYPE_TRANSPORT = 3;

    //快递公司编码
    @PrimaryKey
    public String code;

    //快递公司名
    @Required
    public String name;

    //快递公司类型
    public int type;

    //
    public String enName;

    public boolean selected = false;
}
