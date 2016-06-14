package com.patrick.caracal.entity;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * Created by Patrick on 16/6/11.
 *
 * 快递跟踪信息
 */
@RealmClass
public class Trace implements RealmModel {
    public String acceptTime;

    public String acceptStation;

    public String remark;
}
