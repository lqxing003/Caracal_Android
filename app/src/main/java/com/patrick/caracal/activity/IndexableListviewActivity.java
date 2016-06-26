package com.patrick.caracal.activity;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.patrick.caracal.R;
import com.patrick.caracal.entity.ExpCompanyShowEntity;
import com.patrick.caracal.entity.ExpressCompany;
import com.patrick.caracal.view.adapter.ExpCompanyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import me.yokeyword.indexablelistview.IndexEntity;
import me.yokeyword.indexablelistview.IndexHeaderEntity;
import me.yokeyword.indexablelistview.IndexableStickyListView;

public class IndexableListviewActivity extends AppCompatActivity {

    private static final String TAG = "IndexableListviewActivi";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.listView)
    IndexableStickyListView exp_listview;

    private Realm realm;

    private IndexHeaderEntity<ExpCompanyShowEntity> hotCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indexable_listview);
        ButterKnife.bind(this);

        initRealm();

        initView();

        initExpCompany();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_exp_company, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("请输入快递公司");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                exp_listview.searchTextChange(newText);
                return true;
            }
        });
        return true;
    }

    private void initRealm() {
        realm = Realm.getDefaultInstance();
    }

    private void closeRealm(){
        if (realm != null) {
            realm.close();
        }
    }

    private void initView() {
        ExpCompanyAdapter adapter = new ExpCompanyAdapter(this);
        exp_listview.setAdapter(adapter);
        exp_listview.setOnItemContentClickListener(onClickExpCompany);

        setSupportActionBar(toolbar);
    }

    private void initExpCompany() {

        List<ExpCompanyShowEntity> allCompany = getAllExpCompany();

        exp_listview.bindDatas(allCompany,hotCompany);
    }

    /**
     * 获取热门的快递列表
     * 从Realm 读取全部的Express Company,然后转换成ExpCompanyShowEntity为了adapter显示
     * @return
     */
    private List<ExpCompanyShowEntity> getAllExpCompany(){
        //全部的快递列表
        List<ExpCompanyShowEntity> expCompanyList = new ArrayList<>();
        //热门的快递列表
        List<ExpCompanyShowEntity> hotCompanyList = new ArrayList<>();

        RealmResults<ExpressCompany> results = realm.where(ExpressCompany.class).findAll();
        for (ExpressCompany company :
                results) {
            ExpCompanyShowEntity entity = new ExpCompanyShowEntity(company.name,company.code);
            if (company.hot){
                //如果是热门的快递,就添加一份的热门快递列表里面
                hotCompanyList.add(entity);
            }else{
                expCompanyList.add(entity);
            }

        }

        setupHotCompany(hotCompanyList);
        return expCompanyList;
    }

    /**
     * 设置热门城市的Header
     * @param companyList 热门城市列表
     */
    private void setupHotCompany(List<ExpCompanyShowEntity> companyList){
        hotCompany = new IndexHeaderEntity<>("热","热门快递",companyList);
    }

    private IndexableStickyListView.OnItemContentClickListener onClickExpCompany = new IndexableStickyListView.OnItemContentClickListener() {
        @Override
        public void onItemClick(View v, IndexEntity indexEntity) {
            Log.d(TAG, "onItemClick: "+indexEntity.getName());
        }
    };
}
