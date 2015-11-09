package com.jdjt.mangrovepay.service;

import java.util.Map;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectHttpErr;
import com.android.pc.ioc.inject.InjectHttpOk;
import com.android.pc.ioc.internet.FastHttpHander;
import com.android.pc.ioc.internet.InternetConfig;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.util.Handler_Json;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.contanst.HeaderConst;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.event.MSGCountEntity;
import com.jdjt.mangrovepay.utils.CommonUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LocalService extends Service {
	EventBus eventBus = EventBus.getDefault();
    private static final String TAG = "LocalService"; 
    private IBinder binder=new LocalService.LocalBinder();
    private Notification notification;  
    private NotificationManager nManager;  
    private Intent intent;  
    private PendingIntent pIntent;  
    @Override
    public IBinder onBind(Intent intent) {
         
        return binder;
    }
    MediaPlayer mediaPlayer=null;
    @Override 
    public void onCreate() { 
           Ioc.getIoc().getLogger().i("111111111111111111111");
            //这里可以启动媒体播放器
           // if(mediaPlayer==null)
           //     mediaPlayer=MediaPlayer.create(this, uri);
            super.onCreate(); 
           // getMsgCount();
    } 
    
	

    public void notification(){

        String service =NOTIFICATION_SERVICE;  
        nManager = (NotificationManager)getSystemService(service);  
  
        notification = new Notification();  
        String tickerText = "测试Notifaction"; // 通知提示  
        // 显示时间  
        long when = System.currentTimeMillis();  
  
        notification.icon = R.drawable.ic_launcher;// 设置通知的图标  
        notification.tickerText = tickerText; // 显示在状态栏中的文字  
        notification.when = when; // 设置来通知时的时间  
        notification.sound = Uri.parse("android.resource://com.sun.alex/raw/dida"); // 自定义声音  
        notification.flags = Notification.FLAG_NO_CLEAR; // 点击清除按钮时就会清除消息通知,但是点击通知栏的通知时不会消失  
        notification.flags = Notification.FLAG_ONGOING_EVENT; // 点击清除按钮不会清除消息通知,可以用来表示在正在运行  
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失  
        notification.flags |= Notification.FLAG_INSISTENT; // 一直进行，比如音乐一直播放，知道用户响应  
        notification.defaults = Notification.DEFAULT_SOUND; // 调用系统自带声音  
        notification.defaults = Notification.DEFAULT_SOUND;// 设置默认铃声  
        notification.defaults = Notification.DEFAULT_VIBRATE;// 设置默认震动  
        notification.defaults = Notification.DEFAULT_ALL; // 设置铃声震动  
        notification.defaults = Notification.DEFAULT_ALL; // 把所有的属性设置成默认
    }
    @Override 
    public void onStart(Intent intent, int startId) { 
            Log.i(TAG, "onStart"); 
            super.onStart(intent, startId); 
    } 

    @Override 
    public int onStartCommand(Intent intent, int flags, int startId) { 
          Log.i(TAG, "onStartCommand"); 
        return START_STICKY;
    }

    
    
    @Override 
    public void onDestroy() { 
            Log.i(TAG, "onDestroy"); 
            super.onDestroy(); 
    } 

    
    //定义内容类继承Binder
    public class LocalBinder extends Binder{
        //返回本地服务
        LocalService getService(){
            return LocalService.this;
        }
    }
    
    
}