package com.patrick.caracal.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * Created by junz on 2016/6/16.
 */

public class MainListView extends FrameLayout implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private ListView mListView;

    private MainListViewAdapter mAdapter;

    public MainListView(Context context) {
        super(context);
        init(context, null);
    }

    public MainListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
//
//
//    public ListViewMain(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }


    private void init(Context context, AttributeSet attrs){

//        if (attrs != null) {
//            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndexableStickyListView);
//            mBarTextColor = a.getColor(R.styleable.IndexableStickyListView_indexBar_textColor, getResources().getColor(R.color.default_indexBar_textcolor));
//            mBarTextSize = a.getDimension(R.styleable.IndexableStickyListView_indexBar_textSize, getResources().getDimension(R.dimen.default_indexBar_textSize));
//            mBarSelectedTextColor = a.getColor(R.styleable.IndexableStickyListView_indexBar_selected_textColor, getResources().getColor(R.color.dafault_indexBar_selected_textColor));
//            mRightOverlayColor = a.getColor(R.styleable.IndexableStickyListView_indexListView_rightOverlayColor, getResources().getColor(R.color.default_indexListView_rightOverlayColor));
//            mTypeOverlay = a.getInt(R.styleable.IndexableStickyListView_indexListView_type_overlay, 0);
//            a.recycle();
//        }


        mListView = new ListView(context);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mListView.setDivider(null);
        addView(mListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));


    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void setAdapter(MainListViewAdapter adapter) {
        mAdapter = adapter;
//        mAdapter.setParent(this);
        mListView.setAdapter(adapter);

    }
}
