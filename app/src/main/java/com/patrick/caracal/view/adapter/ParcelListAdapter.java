package com.patrick.caracal.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.patrick.caracal.R;
import com.patrick.caracal.entity.Express;
import com.patrick.caracal.view.widget.SwipeToAction;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patrick on 16/7/2.
 */

public class ParcelListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    private RealmResults<Express> parcelList;

    private ParcelProvider provider;

    public ParcelListAdapter(ParcelProvider provider) {
        this.provider = provider;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adaptet_parcel_item, parent, false);

        return new ParcelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ParcelViewHolder parcelViewHolder = (ParcelViewHolder) holder;

        parcelViewHolder.tv_logisticCode.setText("No: "+provider.getLogistcCode(position));
        parcelViewHolder.tv_companyName.setText(provider.getCompanyName(position));
        parcelViewHolder.tv_acceptStation.setText(provider.getAcceptStation(position));
        parcelViewHolder.tv_acceptTime.setText(provider.getAcceptTime(position));

        parcelViewHolder.data = provider.getParcel(position);
    }

    @Override
    public int getItemCount() {
        return provider.getParcelCount();
    }


    class ParcelViewHolder extends SwipeToAction.ViewHolder<Express> {

        @BindView(R.id.logisticCode)
        public TextView tv_logisticCode;   //单号

        @BindView(R.id.companyName)
        public TextView tv_companyName;    //公司名称

        @BindView(R.id.acceptTime)
        public TextView tv_acceptTime;    //接应时间

        @BindView(R.id.acceptStation)
        public TextView tv_acceptStation;  //接应站点

        ParcelViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    /**
     * 由Activity实现来提供有关快递数据
     */
    public interface ParcelProvider{

        /**
         * @return 快递总数
         */
        int getParcelCount();

        /**
         * 获取整个快递的对象
         */
        Express getParcel(int position);
        /**
         * 获取快递公司名称
         */
        String getCompanyName(int position);

        /**
         * 获取快递Num
         */
        String getLogistcCode(int position);

        /**
         * 获取快递最后接收的站点
         */
        String getAcceptStation(int position);

        /**
         * 获取快递最后接收的时间
         */
        String getAcceptTime(int position);
    }
}
