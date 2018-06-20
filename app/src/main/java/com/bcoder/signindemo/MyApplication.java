package com.bcoder.signindemo;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2018/6/19.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        startAlarm();
    }

    public void startAlarm(){
        /**
         首先获得系统服务
         */
        AlarmManager am = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);

        /** 设置闹钟的意图，我这里是去调用一个服务，该服务功能就是获取位置并且上传*/
        Intent intent = new Intent(this, LocationService.class);
        PendingIntent pendSender = PendingIntent.getService(this, 0, intent, 0);
        am.cancel(pendSender);

        /**AlarmManager.RTC_WAKEUP 这个参数表示系统会唤醒进程；我设置的间隔时间是10分钟 */
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10*60*1000, pendSender);
    }
}
