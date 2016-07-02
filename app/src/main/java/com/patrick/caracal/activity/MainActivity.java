package com.patrick.caracal.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ncapdevi.fragnav.FragNavController;
import com.patrick.caracal.R;
import com.patrick.caracal.view.fragment.HistoryFragment;
import com.patrick.caracal.view.fragment.HomeFragment;
import com.patrick.caracal.view.fragment.MeFragment;
import com.patrick.caracal.view.fragment.NewExpressFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FragNavController fragNavController;

    private BottomBar bottomBar;

    private List<Fragment> fragments = new ArrayList<>(4);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //设置toolbar
        setSupportActionBar(toolbar);

        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.noTopOffset();
        bottomBar.noNavBarGoodness();
        bottomBar.setItems(R.menu.menu_bottom_bar);

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        bottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.bottombar_color));
        bottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.bottombar_color));
        bottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.bottombar_color));
        bottomBar.mapColorForTab(3, ContextCompat.getColor(this, R.color.bottombar_color));


        fragments.add(HomeFragment.newInstance());
        fragments.add(HistoryFragment.newInstance("history","history"));
        fragments.add(NewExpressFragment.newInstance("new express","new express"));
        fragments.add(MeFragment.newInstance());

        fragNavController = new FragNavController(getSupportFragmentManager(),R.id.container,fragments);

        bottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId){
                    case R.id.bottomBarItem_Home:
                        fragNavController.switchTab(FragNavController.TAB1);
                        break;
                    case R.id.bottomBarItem_History:
                        fragNavController.switchTab(FragNavController.TAB2);
                        break;
                    case R.id.bottomBarItem_NewOrder:
                        fragNavController.switchTab(FragNavController.TAB3);
                        break;
                    case R.id.bottomBarItem_Me:
                        fragNavController.switchTab(FragNavController.TAB4);
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItem_Home) {
                    // The user reselected item number one, scroll your content to top.
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        bottomBar.onSaveInstanceState(outState);
    }

}
