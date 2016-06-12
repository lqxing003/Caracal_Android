package com.patrick.caracal.entity;

import io.realm.RealmModel;

/**
 * Created by Patrick on 16/6/11.
 *
 * 快递跟踪信息
 */
public class Trace implements RealmModel {
    public String acceptTime;

    public String acceptStation;

    public String remark;
}
