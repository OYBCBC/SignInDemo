package com.bcoder.signindemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by Administrator on 2018/6/19.
 */

public class MainAcitivity extends AppCompatActivity implements View.OnClickListener {

    private double latitude;
    private double longitude;

    //定位
    private LatLng OFFICE_1 = new LatLng(23.057423, 113.395828);

    private static final String TAG = "MainActivity";

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;


    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    String country = aMapLocation.getCountry();//国家信息
                    String province = aMapLocation.getProvince();//省信息
                    String city = aMapLocation.getCity();//城市信息
                    String district = aMapLocation.getDistrict();//城区信息
                    String street = aMapLocation.getStreet();//街道信息
                    String streetnum = aMapLocation.getStreetNum();//街道门牌号信息

                    longitude = aMapLocation.getLongitude();
                    latitude = aMapLocation.getLatitude();

                    LatLng latLng = new LatLng(latitude, longitude);
                    double d = measureDistance(latLng, OFFICE_1);

                    String address = aMapLocation.getAddress();
                    String msg = country + province + city + district + street + streetnum + "longitude/n" + longitude + "latitude/n" + latitude;

                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    tvLocation.setText("longitude: " + longitude + '\n' +
                            "latitude: " + latitude + '\n' +
                            "address" + address + '\n' +
                            "distance" + d
                    );


                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };


    private Button stopLocationBtn;
    private Button startLocationBtn;
    private EditText tvLocation;
    private Button turnToMapBtn;
    private Button destroyLocationBtn;

    private void bindViews() {
        startLocationBtn = (Button) findViewById(R.id.btn_start_locate);
        stopLocationBtn = (Button) findViewById(R.id.btn_stop_locate);
        tvLocation = (EditText) findViewById(R.id.et_location);
        turnToMapBtn = (Button) findViewById(R.id.btn_cal_distance);
        destroyLocationBtn = (Button) findViewById(R.id.btn_destroy_locate);

        startLocationBtn.setOnClickListener(this);
        stopLocationBtn.setOnClickListener(this);
        destroyLocationBtn.setOnClickListener(this);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
    }

    private double measureDistance(LatLng l1, LatLng l2) {
        return AMapUtils.calculateLineDistance(l1, l2);
    }


    private void locate() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        AMapLocationClientOption option = new AMapLocationClientOption();

        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        option.setInterval(1000);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        option.setMockEnable(true);
        //设置是否返回地址信息（默认返回地址信息）
        option.setNeedAddress(true);

        Log.i(TAG, option.toString());

        if (null != mLocationClient) {
            mLocationClient.setLocationOption(option);

            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();

        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_start_locate:
                locate();
                break;
            case R.id.btn_stop_locate:
                mLocationClient.stopLocation();
                break;
            case R.id.btn_destroy_locate:
                mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                break;


            default:
                break;
        }
    }
}
