package com.patrick.caracal.view.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.patrick.caracal.Center;
import com.patrick.caracal.R;
import com.patrick.caracal.view.iview.IHotExpressAdapter;

/**
 * Created by junz on 2016/6/18.
 */

public class HotExpressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private int hotExpressWidth;
    private IHotExpressAdapter mIHotExpressAdapter;

    public boolean moreExpressSelect = false;

    public HotExpressAdapter(Context context, IHotExpressAdapter iHotExpressAdapter) {
        mContext = context;
        mIHotExpressAdapter = iHotExpressAdapter;
    }

    public void setHotExpressWidth(int hotExpressWidth) {
        this.hotExpressWidth = hotExpressWidth;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < Center.hotExpresslist.size()){
            return 1;
        }else {
            return 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1){
            return new HotExpressViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_hot_express, parent, false));
        }else if (viewType == 2){
            return new MoreExpressViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_hot_express, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int getItemType = getItemViewType(position);
        if (getItemType == 1) {
            onBindHotExpressViewHolder((HotExpressViewHolder) holder, position);
        }
        if (getItemType == 2) {
            onBindMoreExpressViewHolder((MoreExpressViewHolder) holder, position);
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void onBindHotExpressViewHolder(HotExpressViewHolder holder, final int position) {
        holder.linearItemHotExpress.getLayoutParams().height = hotExpressWidth / 3;
        if (Center.hotExpresslist.get(position).get(0).selected){
            holder.linearItemHotExpress.setBackground(mContext.getResources().getDrawable(R.drawable.share_hot_express_card_selected));
        }else {
            holder.linearItemHotExpress.setBackground(mContext.getResources().getDrawable(R.drawable.share_hot_express_card_unselect));
        }

        holder.tvHotExpress.setText(Center.hotExpresslist.get(position).get(0).name);

        holder.linearItemHotExpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIHotExpressAdapter.selectedHotExpress(position);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void onBindMoreExpressViewHolder(MoreExpressViewHolder holder, int position) {


        holder.linearItemHotExpress.getLayoutParams().height = hotExpressWidth / 3;
        if (!moreExpressSelect){
            holder.linearItemHotExpress.setBackground(mContext.getResources().getDrawable(R.drawable.share_hot_express_card_unselect));
            holder.tvHotExpress.setText(mContext.getString(R.string.query_express_btn_more_select));
        }else {

        }

        holder.linearItemHotExpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIHotExpressAdapter.setectedMoreExpress();
            }
        });
    }


    @Override
    public int getItemCount() {
        return Center.hotExpresslist.size() + 1;
    }

    public class HotExpressViewHolder extends RecyclerView.ViewHolder {

        private TextView tvHotExpress;
        private LinearLayout linearItemHotExpress;

        public HotExpressViewHolder(View itemView) {
            super(itemView);
            tvHotExpress = (TextView) itemView.findViewById(R.id.tvHotExpress);
            linearItemHotExpress = (LinearLayout) itemView.findViewById(R.id.linearItemHotExpress);
        }
    }

    public class MoreExpressViewHolder extends RecyclerView.ViewHolder {

        private TextView tvHotExpress;
        private LinearLayout linearItemHotExpress;

        public MoreExpressViewHolder(View itemView) {
            super(itemView);
            tvHotExpress = (TextView) itemView.findViewById(R.id.tvHotExpress);
            linearItemHotExpress = (LinearLayout) itemView.findViewById(R.id.linearItemHotExpress);
        }
    }
}
