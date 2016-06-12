package com.patrick.caracal.entity;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Patrick on 16/6/12.
 *
 * 快递实例
 */
public class Express implements RealmModel {

    public static final int STATE_IN_PROGRESS = 2;  //在途中
    public static final int STATE_SUCCESS = 3;  //已签收
    public static final int STATE_PROBLEM = 4;  //问题件

    //快递单号
    @PrimaryKey
    public String logisticCode;

    public RealmList<Trace> traces;

    //2-在途中,3-签收,4-问题件
    public int state;

    public Express(String logisticCode) {
        this.logisticCode = logisticCode;
    }
}
