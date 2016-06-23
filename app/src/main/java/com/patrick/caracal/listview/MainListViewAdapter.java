package com.patrick.caracal.listview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.patrick.caracal.R;
import com.patrick.caracal.entity.ExpressCompany;

import java.util.List;

import io.realm.RealmResults;


/**
 * Created by junz on 2016/6/16.
 */

public class MainListViewAdapter extends BaseAdapter {

    private List<RealmResults<ExpressCompany>> mList;

    private LayoutInflater inflator;

    private TextView tvExpressName;
    private TextView tvExpressEnName;

    private Activity mActivity;

    public MainListViewAdapter(Activity activity, List<RealmResults<ExpressCompany>> list) {
        mActivity = activity;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){

//            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_express, null);
//            tvExpressName = (TextView)convertView.findViewById(R.id.tvExpressName);
//            tvExpressEnName =(TextView) convertView.findViewById(R.id.tvExpressEnName);
        }
        tvExpressName.setText(mList.get(position).get(0).name);
        tvExpressEnName.setText(mList.get(position).get(0).enName);

        return convertView;
    }


}
