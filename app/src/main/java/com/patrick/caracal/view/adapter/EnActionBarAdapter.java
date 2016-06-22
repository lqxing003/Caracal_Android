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
import com.patrick.caracal.view.iview.IEnActionBarAdapter;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by junz on 2016/6/19.
 */

public class EnActionBarAdapter extends RecyclerView.Adapter<EnActionBarAdapter.ViewHolder> {

    private List<RealmResults<ExpressCompany>> mRealmResultses;
    private Context mContext;
    private IEnActionBarAdapter mIEnActionBarAdapter;


    public EnActionBarAdapter(Context context, List<RealmResults<ExpressCompany>> realmResultses, IEnActionBarAdapter IEnActionBarAdapter) {
        mContext = context;
        mRealmResultses = realmResultses;
        mIEnActionBarAdapter = IEnActionBarAdapter;
    }

    @Override
    public EnActionBarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_en_name, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tvEnName.setText(mRealmResultses.get(position).get(0).enName);
        holder.tvEnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIEnActionBarAdapter.onClickEnName(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mRealmResultses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvEnName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEnName = (TextView) itemView.findViewById(R.id.tvEnName);

        }
    }
}
