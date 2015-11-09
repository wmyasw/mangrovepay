package com.jdjt.mangrovepay.application;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.inject.InjectHttpErr;
import com.android.pc.ioc.inject.InjectHttpOk;
import com.android.pc.ioc.internet.FastHttpHander;
import com.android.pc.ioc.internet.InternetConfig;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.util.ExceptionHandler;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_SharedPreferences;
import com.android.pc.util.Handler_String;
import com.android.pc.util.Handler_System;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.activity.LoginActivity;
import com.jdjt.mangrovepay.contanst.Constants;
import com.jdjt.mangrovepay.contanst.HeaderConst;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.Uuid;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-6
 * @Description:TODO ( 全局application)
 **************************************************/
public class MangrovePayApp extends Application {

	public static MangrovePayApp app;
	private IWXAPI api;
	public static float version;
	ExceptionHandler eh;
	public static String ticket = "";
	private ArrayList<Activity> mList = new ArrayList<Activity>();
	public static String msgTime;//消息基准时间
	public static String appid;
	public static int msgCount=0;//新消息条数
	@Override
	public void onCreate() {
		app = this;
		Ioc.getIoc().init(this);
		// 通过WXAPIFactory 工厂，获取IWXAPI 事例
		//通过WXAPIFactory 工厂，获取IWXAPI 事例
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
//		将应用appid注册到微信
		api.registerApp(Constants.APP_ID);
		try {
			version = Float.valueOf(Handler_System.getAppVersionNumber());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msgTime=getData("msgTime");
		if(Handler_String.isBlank(msgTime)){
			msgTime="1970-01-01 00:00:00";
			setData("msgTime", msgTime);
		}
		appid = MangrovePayApp.app.getData("appid");
		if (Handler_String.isBlank(appid)) { // 检查缓存是否存在appid 如果没有生成一个 并保存到缓存数据库
			appid = Uuid.getUuid();
			MangrovePayApp.app.setData("appid", appid);
		}
	
		 Thread.setDefaultUncaughtExceptionHandler(restartHandler); //
		// 程序崩溃时触发线程
		// 以下用来捕获程序崩溃异常
		super.onCreate();
	}

	// 创建服务用于捕获崩溃异常
	private UncaughtExceptionHandler restartHandler = new UncaughtExceptionHandler() {
		public void uncaughtException(Thread thread, Throwable ex) {
			ex.printStackTrace();
			restartApp();// 发生崩溃异常时,重启应用

		}
	};

	public void restartApp() {
		Intent intent = new Intent(app, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		app.startActivity(intent);
		System.out.println("系统崩溃~~~~~");
		android.os.Process.killProcess(android.os.Process.myPid()); // 结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
	}

	public synchronized static MangrovePayApp getInstance() {
		if (null == app) {
			app = new MangrovePayApp();
		}
		return app;
	}

	/*************************************************
	 * @Title: setData
	 * @Description: TODO(数据存储到本地数据库)
	 * @param key
	 * @param value
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-6
	 *************************************************/
	public void setData(String key, String value) {
		Handler_SharedPreferences.WriteSharedPreferences(
				getApplicationContext(), "Cache", key, value);

	}

	/*************************************************
	 * @Title: getData
	 * @Description: TODO(取出本地数据)
	 * @param key
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 * @date 2015-1-6
	 *************************************************/
	public String getData(String key) {
		return Handler_SharedPreferences.getValueByName(
				getApplicationContext(), "Cache", key,
				Handler_SharedPreferences.STRING).toString();
	}

	/*************************************************
	 * @Title: remove
	 * @Description: TODO(清空数据库) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-13
	 *************************************************/
	public void remove() {
		Handler_SharedPreferences.ClearSharedPreferences(
				getApplicationContext(), "Cache");
	}

	/*************************************************
	 * @Title: setData
	 * @Description: TODO(设置指定数据库名称)
	 * @param database
	 * @param key
	 * @param value
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-13
	 *************************************************/
	public void setData(String database, String key, String value) {
		Handler_SharedPreferences.WriteSharedPreferences(
				getApplicationContext(), database, key, value);
	}

	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}
}
