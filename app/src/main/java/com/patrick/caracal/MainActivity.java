package com.patrick.caracal;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.ncapdevi.fragnav.FragNavController;
import com.patrick.caracal.fragment.HomeFragment;
import com.patrick.caracal.fragment.MeFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomBar bottomBar;

    private List<Fragment> fragments = new ArrayList<>(4);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.noTopOffset();
        bottomBar.noNavBarGoodness();
        bottomBar.setItems(R.menu.menu_bottom_bar);
        bottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch (menuItemId){
                    case R.id.bottomBarItem_Home:

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

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        bottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorPrimary));
        bottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.colorPrimary));
        bottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.colorPrimary));
        bottomBar.mapColorForTab(3, ContextCompat.getColor(this, R.color.colorPrimary));


        fragments.add(HomeFragment.newInstance());
//        fragments.add(Fragment.instantiate(this,"feature2"));
//        fragments.add(Fragment.instantiate(this,"feature3"));
        fragments.add(MeFragment.newInstance());
        fragments.add(MeFragment.newInstance());
        fragments.add(MeFragment.newInstance());

        FragNavController fragNavController = new FragNavController(getSupportFragmentManager(),R.id.container,fragments);
        fragNavController.switchTab(FragNavController.TAB1);
//
//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//                startActivityForResult(intent, REQUEST_CODE);
//            }
//        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        bottomBar.onSaveInstanceState(outState);
    }

    //
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK){ //RESULT_OK = -1
//            Bundle bundle = data.getExtras();
//            String scanResult = bundle.getString("result");
//            Toast.makeText(MainActivity.this, scanResult, Toast.LENGTH_LONG).show();
//        }
//    }
}
