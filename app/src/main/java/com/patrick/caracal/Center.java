package com.patrick.caracal;

import com.patrick.caracal.entity.ExpressCompany;


import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by junz on 2016/6/15.
 */

public class Center {

    public static List<RealmResults<ExpressCompany>> domesticEexpressList = new ArrayList<>();

    public static List<RealmResults<ExpressCompany>> foreignEexpressList = new ArrayList<>();

    public static List<RealmResults<ExpressCompany>> transportEexpressList = new ArrayList<>();

    public static RealmResults<ExpressCompany> allExpressCompanyList;

    public static List<RealmResults<ExpressCompany>> hotExpresslist = new ArrayList<>();

    public static RealmResults<ExpressCompany> selectExpress;

    public static String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                                         "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                                         "U", "V", "W", "X", "Y", "Z"};

    private static String[] HOT_EXPRESS_COMPANY = {"顺丰快递", "中通速递",
                                                "圆通速递", "韵达快递",
                                                "申通快递", "天天快递",
                                                "百世汇通", "EMS"};

    public static void init(){

        allExpressCompanyList = Realm.getDefaultInstance().where(ExpressCompany.class).findAll();
        selectExpress = Realm.getDefaultInstance().where(ExpressCompany.class).equalTo("selected", true).findAll();

        for (int i = 0; i < alphabet.length; i++){
            RealmResults<ExpressCompany> expressCompanies = Realm.getDefaultInstance()
                    .where(ExpressCompany.class).equalTo("enName", alphabet[i]).equalTo("type", 1).findAll();
            if (expressCompanies.size() > 0){
                domesticEexpressList.add(expressCompanies);
            }
        }

        for (int i = 0; i < alphabet.length; i++){
            RealmResults<ExpressCompany> expressCompanies = Realm.getDefaultInstance()
                    .where(ExpressCompany.class).equalTo("enName", alphabet[i]).equalTo("type", 2).findAll();
            if (expressCompanies.size() > 0){
                foreignEexpressList.add(expressCompanies);
            }
        }

        for (int i = 0; i < alphabet.length; i++){
            RealmResults<ExpressCompany> expressCompanies = Realm.getDefaultInstance()
                    .where(ExpressCompany.class).equalTo("enName", alphabet[i]).equalTo("type", 3).findAll();
            if (expressCompanies.size() > 0){
                transportEexpressList.add(expressCompanies);
            }
        }


        for (int z = 0; z < HOT_EXPRESS_COMPANY.length; z++){
            RealmResults<ExpressCompany> expressCompanies = Realm.getDefaultInstance()
                    .where(ExpressCompany.class)
                    .equalTo("name", HOT_EXPRESS_COMPANY[z])
                    .findAll();
//            HotExpressCompany hotExpressCompany = new HotExpressCompany(false, expressCompanies);
            hotExpresslist.add(expressCompanies);
        }


    }

}
