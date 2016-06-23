package com.patrick.caracal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.patrick.caracal.R;
import com.patrick.caracal.entity.ExpressCompany;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by junz on 2016/6/19.
 */

public class SelectExpressSubAdapter extends RecyclerView.Adapter<SelectExpressSubAdapter.ViewHolder> {

    private Context mContext;
    private RealmResults<ExpressCompany> mExpressCompanyRealmResults;



    public SelectExpressSubAdapter(Context context,
                                   RealmResults<ExpressCompany> expressCompanyRealmResults) {
        mContext = context;
        mExpressCompanyRealmResults = expressCompanyRealmResults;
    }



    @Override
    public SelectExpressSubAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_select_express_sub, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectExpressSubAdapter.ViewHolder holder, final int position) {

        holder.tvExpressName.setText(mExpressCompanyRealmResults.get(position).name);
        if (!mExpressCompanyRealmResults.get(position).selected){
            holder.linearExpressSub.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else {
            holder.linearExpressSub.setBackgroundColor(mContext.getResources().getColor(R.color.selected));
        }

        holder.tvExpressName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                RealmResults<ExpressCompany> expressCompanies = realm.where(ExpressCompany.class)
                        .equalTo("selected", true).findAll();
                if (expressCompanies.size() > 0){
                    expressCompanies.get(0).selected = false;
                    mExpressCompanyRealmResults.get(position).selected = true;
                }else {
                    mExpressCompanyRealmResults.get(position).selected = true;
                }
                realm.commitTransaction();
                realm.close();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExpressCompanyRealmResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvExpressName;
        private LinearLayout linearExpressSub;

        public ViewHolder(View itemView) {
            super(itemView);
            tvExpressName = (TextView) itemView.findViewById(R.id.tvExpressName);
            linearExpressSub = (LinearLayout) itemView.findViewById(R.id.linearExpressSub);
        }
    }


}
