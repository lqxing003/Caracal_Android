package com.patrick.caracal.model;

import android.content.Context;

import io.realm.Realm;

/**
 * Created by Patrick on 16/6/26.
 */
public abstract class Model {

    protected Context context;
    /**
     * 这里的realm是由activity提供,而不是从Realm.getDefaultInstance()获取
     */
    protected Realm realm;

    public Model(Context context, Realm realm) {
        this.context = context;
        this.realm = realm;
    }



}
