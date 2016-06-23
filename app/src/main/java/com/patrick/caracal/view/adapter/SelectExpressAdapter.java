package com.patrick.caracal.view.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.patrick.caracal.R;
import com.patrick.caracal.entity.ExpressCompany;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by junz on 2016/6/19.
 */

public class SelectExpressAdapter extends RecyclerView.Adapter<SelectExpressAdapter.ViewHolder>  {

    private List<RealmResults<ExpressCompany>> mRealmResultses;
    private Context mContext;

    public SelectExpressAdapter(Context context, List<RealmResults<ExpressCompany>> realmResultses) {
        mContext = context;
        mRealmResultses = realmResultses;

    }

    @Override
    public SelectExpressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_express, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvExpressEN.setText(mRealmResultses.get(position).get(0).enName);
        SelectExpressSubAdapter mSelectExpressSubAdapter = new SelectExpressSubAdapter(mContext, mRealmResultses.get(position));
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        holder.rvDomesticSub.setLayoutManager(mLinearLayoutManager);
        holder.rvDomesticSub.setAdapter(mSelectExpressSubAdapter);

    }


    @Override
    public int getItemCount() {
        return mRealmResultses.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvExpressEN;
        private RecyclerView rvDomesticSub;

        public ViewHolder(View itemView) {
            super(itemView);
            tvExpressEN = (TextView) itemView.findViewById(R.id.tvExpressEN);
            rvDomesticSub = (RecyclerView) itemView.findViewById(R.id.rvDomesticSub);
        }
    }
}
