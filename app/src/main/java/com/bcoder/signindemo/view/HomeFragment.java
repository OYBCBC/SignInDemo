package com.bcoder.signindemo.view;

import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.bcoder.signindemo.R;
import com.bcoder.signindemo.view.button.AnimationButton;
import com.bcoder.signindemo.view.button.LoadingCircleView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/6/21.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    private double latitude;
    private double longitude;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    private Button stopLocationBtn;
//    private Button startLocationBtn;
    private EditText tvLocation;
    private Button turnToMapBtn;
    private Button destroyLocationBtn;

    private AnimationButton startLocationBtn;

    //定位
    private LatLng OFFICE_1 = new LatLng(23.057423, 113.395828);

    private TextView timeTv;
    //    private ImageView signInButton;
    private ImageView signInButton;

    private LoadingCircleView loadingCircleView;
    private AnimationButton animationButton;

    //获取默认办公点
    private void initDefaultLocation(){

    }

    private void timeDisplay(){
        new TimeThread().start();//启动线程
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
//        initTickButton(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        timeDisplay();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocationClient.stopLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    private void initTickButton(View view) {
//        signInButton = view.findViewById(R.id.sign_in);
        AnimatedVectorDrawableCompat animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(
                getContext(), R.drawable.animated_vector_tick);
        signInButton.setImageDrawable(animatedVectorDrawableCompat);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Animatable) signInButton.getDrawable()).start();
            }
        });
    }

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

//                    Toast.makeText(getContext(), "距离为:" + d +"米", Toast.LENGTH_LONG).show();
//                    tvLocation.setText("longitude: " + longitude + '\n' +
//                            "latitude: " + latitude + '\n' +
//                            "address" + address + '\n' +
//                            "distance" + d
//                    );
                    if (d < 50 || d == 50){
                        Toast.makeText(getContext(),"签到成功 " ,Toast.LENGTH_SHORT).show();
                        //插入签到数据
                        animationButton.start();
                    }else{
                        Toast.makeText(getContext(), "不在签到范围内（50米）" ,Toast.LENGTH_LONG).show();
                    }

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    private double measureDistance(LatLng l1, LatLng l2) {
        return AMapUtils.calculateLineDistance(l1, l2);
    }


    private void locate() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getContext());
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

    private void initViews() {
        timeTv = getActivity().findViewById(R.id.current_time);

        startLocationBtn = getActivity().findViewById(R.id.btn_start_locate);
        startLocationBtn.setOnClickListener(this);

        animationButton = getActivity().findViewById(R.id.btn_start_locate);
        animationButton.setOnClickListener(this);

//        stopLocationBtn = view.findViewById(R.id.btn_stop_locate);
//        tvLocation = view.findViewById(R.id.et_location);
//        turnToMapBtn = view.findViewById(R.id.btn_cal_distance);

//        destroyLocationBtn = view.findViewById(R.id.btn_destroy_locate);
//        stopLocationBtn.setOnClickListener(this);
//        destroyLocationBtn.setOnClickListener(this);

//        loadingCircleView = view.findViewById(R.id.loading_circle_view);
//        loadingCircleView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loadingCircleView.loadCircle();
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    loadingCircleView.stop();
//                }
//
//            }
//        });




    }

    public class TimeThread extends Thread{
        @Override
        public void run() {
            super.run();
            do{
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);

        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    timeTv.setText(new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())));
                    break;
            }
            return false;
        }
    });



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_locate:
                Log.i(TAG, "animationButton clicked");
//                animationButton.start();
                locate();
                break;

//            case R.id.btn_stop_locate:
//                mLocationClient.stopLocation();
//                break;
//            case R.id.btn_destroy_locate:
//                mLocationClient.onDestroy();
//                break;

            default:
                break;
        }
    }
}
