package com.jdjt.mangrovepay.utils;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.inject.InjectHttpOk;
import com.android.pc.ioc.internet.FastHttpHander;
import com.android.pc.ioc.internet.InternetConfig;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.ioc.util.MapVo;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_Network;
import com.android.pc.util.Handler_String;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.activity.LoginActivity;
import com.jdjt.mangrovepay.activity.MainActivity;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.contanst.ErrorMsgEnum;
import com.jdjt.mangrovepay.contanst.HeaderConst;
import com.jdjt.mangrovepay.contanst.Url;

/**
 * 对后台接口返回数据进行处理
 * 
 * @author Administrator
 * 
 */
public class ResultParse {
	static Activity activity;
	@InjectHttpOk
	private void success(ResponseEntity entity) {
		Ioc.getIoc().getLogger().i("登录接口状态返回结果【" + entity.toString() + "】");

		// 获取头部信息
		Map<String, String> map = entity.getHeaders();
		// 获取头部信息
		String[] msgArray = map.get(HeaderConst.MYMHOTEL_MESSAGE).split("\\|");

		// 成功 返回OK
		if ("OK".equals(map.get(HeaderConst.MYMHOTEL_STATUS))) {
			// Toast.makeText(context, msgArray[1], Toast.LENGTH_SHORT).show();
		
			// 获取返回内容 ticket
			String jsonString = entity.getContentAsString();

			Ioc.getIoc().getLogger().i("ticket数据【" + jsonString + "】");

			// 存放ticket
			MapVo.map = Handler_Json.JsonToCollection(jsonString);// 设置ticket
																	// TODO
			MangrovePayApp.ticket=(String) MapVo.get("ticket");
		}
		// 登录失败
		else {
			Intent intent = new Intent();
			intent.setClass(activity, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 它可以关掉所要到的界面中间的activity
			activity.startActivity(intent);
			activity.finish();
		}

	}
	public static void login(Activity activity){
		//showDialog("登录中...");
		Ioc.getIoc().getLogger().i("登录前端验证成功");
		// Toast.makeText(this, "前端验证通过", Toast.LENGTH_SHORT).show();

		 HashMap<String, String> loginMap = new HashMap<String, String>();
		loginMap.put("account", MangrovePayApp.app.getData("account"));
		loginMap.put("password", MangrovePayApp.app.getData("password"));

		// 验证手机是否联网
		if (!Handler_Network.isNetworkAvailable(activity)) {
			Toast.makeText(activity, ErrorMsgEnum.NET_ERROR.getName(),
					Toast.LENGTH_SHORT).show();
			return;
		}
		InternetConfig	config=new InternetConfig();
		config.setKey(R.raw.client);
		config.setHead(CommonUtils.inHeaders());
		Ioc.getIoc().getLogger().i("调用后台登录接口：[" + Url.METHOD_LOGIN + "]");
		// 调用后台登录接口
		FastHttpHander.ajaxString(Url.METHOD_LOGIN,
				Handler_Json.beanToJson(loginMap), config, ResultParse.class);
	}
	public static boolean isResultOK(ResponseEntity r, Activity context) {
		activity=context;
		Map<String, String> map = r.getHeaders();
		// 获取头部信息
		String[] msgArray = map.get(HeaderConst.MYMHOTEL_MESSAGE).split("\\|");

		// 成功 返回OK
		if ("OK".equals(map.get(HeaderConst.MYMHOTEL_STATUS))) {
			// Toast.makeText(context, msgArray[1], Toast.LENGTH_SHORT).show();
			return true;
		}// 失败
		else {
			if (msgArray[0].equals("UNLOGIN")
					|| "TICKET_ISNULL".equals(msgArray[0])) {
				Toast.makeText(context,
						ErrorMsgEnum.getErrorMsgEnum(msgArray[0]).getName(),
						Toast.LENGTH_SHORT).show();
				MapVo.map = null;
				Intent intent = new Intent();
				intent.setClass(context, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 它可以关掉所要到的界面中间的activity
				context.startActivity(intent);
				context.finish();
				//login(context);
			}
			// Ioc.getIoc().getLogger().i(msgArray[1]);
			// 如果提示信息编号为登录失效,跳到登录界面重新登录,并且将内存信息制空 TODO
			// if(system_e)
			Ioc.getIoc().getLogger().i("返回消息内容g:" + r.getContentAsString());
			Ioc.getIoc().getLogger()
					.i("返回消息内容g:" + r.getContentAsString().trim().length());

			if (!"".equals(r.getContentAsString().trim())
					&& r.getContentAsString().trim().length() > 2) {
				HashMap<String, String> m = Handler_Json.JsonToCollection(r
						.getContentAsString());
				if (!Handler_String.isBlank(m.get("errCode"))) {
					String errMessage = m.get("errMessage");
					Toast.makeText(context, errMessage, Toast.LENGTH_SHORT)
							.show();
					return false;
				}
			} else if (msgArray != null && msgArray.length > 1) {
					if (msgArray[0]
							.equals(ErrorMsgEnum.TICKET_ISNULL.getCode())
							|| msgArray[0].equals("UNLOGIN")) {// 系统异常传出 tickt
																// 问题 统一提示 用户未登录
						// 根据错误代码 显示错误信息
						// ErrorMessageEnum.getErrorMessageEnumName(msgArray[0])
						// 补齐错误代码和提示信息后。不需要 equals code
						Toast.makeText(
								context,
								ErrorMsgEnum.getErrorMsgEnum(msgArray[0])
										.getName(), Toast.LENGTH_SHORT).show();
						// 直接跳到登陆界面
						// ticket失效
						MapVo.map = null;
						Intent intent = new Intent();
						intent.setClass(context, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
						context.startActivity(intent);
						//login(context);
						return false;
					} else {
						// Toast.makeText(context, msgArray[1],
						// Toast.LENGTH_SHORT).show();
						if (null != ErrorMsgEnum.getErrorMsgEnum(msgArray[0])) {
							Toast.makeText(
									context,
									ErrorMsgEnum.getErrorMsgEnum(msgArray[0])
											.getName(), Toast.LENGTH_SHORT)
									.show();
						} else {
							// Toast.makeText(context, "请求失败，请稍候在试",
							// Toast.LENGTH_SHORT).show();
							Toast.makeText(context, msgArray[1],
									Toast.LENGTH_SHORT).show();
						}
						return false;
					}
				}
			}
			return false;
		}

}
