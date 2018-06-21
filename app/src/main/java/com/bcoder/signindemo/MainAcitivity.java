package com.bcoder.signindemo;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentController;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.maps2d.AMapUtils;


import com.amap.api.maps2d.model.LatLng;
import com.bcoder.signindemo.util.AMapUtil;
import com.bcoder.signindemo.view.HomeFragment;
import com.bcoder.signindemo.view.SettingFragment;
import com.bcoder.signindemo.view.TimeLineFragment;
import com.zaaach.tabradiobutton.TabRadioButton;

/**
 * Created by Administrator on 2018/6/19.
 */

public class MainAcitivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "MainActivity";


    //Fragment
    private Fragment[] mFragments = null;
    private int mIndex = 0;
    //    private RadioButton homeRBtn;
//    private RadioButton timeLineRBtn;
//    private RadioButton settingRBtn;
    private TabRadioButton homeRBtn;
    private TabRadioButton timeLineRBtn;
    private TabRadioButton settingRBtn;
    private SettingFragment settingFragment;

    private void initFragment() {
        //首页
        HomeFragment homeFragment = new HomeFragment();
        //时间轴
        TimeLineFragment timeLineFragment = new TimeLineFragment();
        //设置
        settingFragment = new SettingFragment();
        //添加到数组
        mFragments = new Fragment[]{homeFragment, timeLineFragment};
        //开启事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //添加首页
        ft.add(R.id.content_frag_layout, homeFragment).commit();
        //默认设置为第0个
        setIndexSelected(0);

    }

    private void setIndexSelected(int index) {

        if (mIndex == index) {
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (index < 99) {
            //隐藏
            if (mIndex < 99){
                ft.hide(mFragments[mIndex]);
            }else{
                this.getFragmentManager().beginTransaction()
                        .hide(settingFragment)
                        .commit();
            }

            //判断是否添加
            if (!mFragments[index].isAdded()) {
                ft.add(R.id.content_frag_layout, mFragments[index]).show(mFragments[index]);
            } else {
                ft.show(mFragments[index]);
            }
        } else {
            if (!settingFragment.isAdded()) {
                this.getFragmentManager().beginTransaction()
                        .add(R.id.content_frag_layout, settingFragment)
                        .commit();
            } else {
                this.getFragmentManager().beginTransaction()
                        .show(settingFragment)
                        .commit();
            }
            ft.hide(mFragments[mIndex]);
        }


        ft.commit();
        //再次赋值
        mIndex = index;

    }


    private void bindViews() {


        homeRBtn = findViewById(R.id.home_frag);
        timeLineRBtn = findViewById(R.id.timeline_frag);
        settingRBtn = findViewById(R.id.setting_frag);

        homeRBtn.setOnClickListener(this);
        timeLineRBtn.setOnClickListener(this);
        settingRBtn.setOnClickListener(this);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        initFragment();

    }


    private void hideSettingFrag() {
        this.getFragmentManager().beginTransaction()
                .hide(settingFragment)
                .commit();
    }

    private void changeSettingFrag() {

        this.getFragmentManager().beginTransaction()
                .replace(R.id.content_frag_layout, settingFragment)
                .commit();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.home_frag:
                setIndexSelected(0);
                hideSettingFrag();
                break;
            case R.id.timeline_frag:
                setIndexSelected(1);

                break;
            case R.id.setting_frag:
                setIndexSelected(100);
                break;
            default:
                break;
        }
    }
}
