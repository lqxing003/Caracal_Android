package com.patrick.caracal.entity;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by Patrick on 16/6/12.
 *
 * 快递实例
 */
//@RealmClass
//public class Express implements RealmModel {
public class Express extends RealmObject {

    public static final int STATE_IN_PROGRESS = 2;  //在途中
    public static final int STATE_SUCCESS = 3;  //已签收
    public static final int STATE_PROBLEM = 4;  //问题件

    public static final int LOCAL_STATE_NORMAL = 1; //正常
    public static final int LOCAL_STATE_ARCHIVE = 2;  //归档
    public static final int LOCAL_STATE_TRASH = 3; //垃圾

    //快递单号
    @PrimaryKey
    public String LogisticCode;

    //快递公司编码
    public String ShipperCode;

    public RealmList<Trace> Traces;

    //2-在途中,3-签收,4-问题件
    public int State;

    /**
     * 本地状态,分为正常、归档、垃圾桶
     */
    public int localState;
}
