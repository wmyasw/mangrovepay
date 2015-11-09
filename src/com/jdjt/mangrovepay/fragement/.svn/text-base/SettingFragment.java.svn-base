package com.jdjt.mangrovepay.fragement;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectHttpOk;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectListener;
import com.android.pc.ioc.inject.InjectMethod;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.internet.FastHttp;
import com.android.pc.ioc.internet.FastHttpHander;
import com.android.pc.ioc.internet.InternetConfig;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.ioc.util.MapVo;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_File;
import com.android.pc.util.Handler_Inject;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_String;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.activity.ChangeLoginPasswordActivity;
import com.jdjt.mangrovepay.activity.ChangePayPasswordActivity;
import com.jdjt.mangrovepay.activity.ChangePhoneActivity;
import com.jdjt.mangrovepay.activity.FindPayPasswordActivity;
import com.jdjt.mangrovepay.activity.LoginActivity;
import com.jdjt.mangrovepay.adpater.HotelBalanceAdapter;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.common.BaseFragment;
import com.jdjt.mangrovepay.contanst.ErrorMsgEnum;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.utils.CommonAlert;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.DataCleanManager;
import com.jdjt.mangrovepay.utils.ResultParse;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-9
 * @Description:TODO (设置)
 **************************************************/
public class SettingFragment extends BaseFragment {
	EventBus eventBus = EventBus.getDefault();

	ArrayList<HashMap<String, String>> mapList = null;
	HotelBalanceAdapter adapter = null;
	CommonAlert alert;
	String updateURL;
	float version;
	@InjectView(R.id.tx_cache_size)
	public TextView tx_cache_size;
	String cardNo;
	String	path ="";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View rootView = inflater.inflate(R.layout.fragement_setting, container,
				false);
		Handler_Inject.injectFragment(this, rootView);
		return rootView;
	}

	@InjectInit
	public void init() {
		version = MangrovePayApp.version;
		cardNo=MangrovePayApp.app.getData("virtualAccountNo");
		getCacheSize();
		
	}

	/*************************************************
	 * @Title: itemClick
	 * @Description: TODO(设置item 点击事件)
	 * @param v
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-30
	 *************************************************/
	@InjectMethod(@InjectListener(ids = { /*R.id.rl_about,*/ R.id.rl_change_phone,
			R.id.rl_find_pay_password, R.id.rl_change_login_passwrod,
			R.id.rl_change_pay_password, R.id.btn_pay_next,
			R.id.rl_update_check,R.id.rl_cache }, listeners = { OnClick.class }))
	public void itemClick(View v) {
		switch (v.getId()) {
		case R.id.rl_update_check:
			ajaxIsVerson();
			break;
	/*	case R.id.rl_about:
			Toast.makeText(activity, "关于我们", Toast.LENGTH_SHORT).show();
			break;*/
		case R.id.rl_change_phone:
			startActivity(new Intent().setClass(activity,ChangePhoneActivity.class));
			break;

		case R.id.rl_find_pay_password:
			
			if (!Handler_String.isBlank(cardNo)) {
				startActivity(new Intent().setClass(activity,
						FindPayPasswordActivity.class));
			} 
			//getAccountInfo(1);
			break;

		case R.id.rl_change_pay_password:
			if (!Handler_String.isBlank(cardNo)){
				startActivity(new Intent().setClass(activity,
						ChangePayPasswordActivity.class));
			}
			//getAccountInfo(2);
			break;

		case R.id.rl_change_login_passwrod:
			startActivity(new Intent().setClass(activity,
					ChangeLoginPasswordActivity.class));
			break;
		case R.id.btn_pay_next:// 安全退出
			alertOut();
			break;
		case R.id.rl_cache:// 清楚缓存
			try {
				long folderSize = DataCleanManager.getFolderSize(new File(path));
				if(folderSize>0){
					DataCleanManager.cleanCustomCache(path);
					getCacheSize();
				}else{
					Toast.makeText(activity, "清除成功", Toast.LENGTH_SHORT)
					.show();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	private void getCacheSize(){
		String folderSize;
		try {
			path = Handler_File.getExternalCacheDir(Ioc.getIoc().getApplication(), "files").getPath() ;
			folderSize = DataCleanManager.getCacheSize(new File(path));
			tx_cache_size.setText(folderSize+"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/*************************************************
	 * @Title: alertVersion
	 * @Description: TODO(alert提示)
	 * @param msg
	 * @param type
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-30
	 *************************************************/
	public void alertVersion(String msg, final String type) {
		alert = new CommonAlert(activity);
		alert.setMessage(msg);
		alert.setCancelable(false);
		String clear = "";
		if ("1".equals(type))
			clear = "忽略";
		else
			clear = "退出";
		alert.setNegativeButton("更新", new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtils.update(updateURL);
			}
		});
		alert.setPositiveButton(clear, new OnClickListener() {

			@Override
			public void onClick(View v) {

				if ("1".equals(type))
					alert.dismiss();
				else
					activity.finish();
			}
		});
	}

	/*************************************************
	 * @Title: ajaxIsVerson
	 * @Description: TODO(获取网站 版本查看是否需要更新) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-30
	 *************************************************/
	public void ajaxIsVerson() {
		showDialog("检查最新版本...");
		String url = Url.MEM_VERSIONS;
		InternetConfig config = new InternetConfig();
		config.setKey(11);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxString(url, "", config, this);
	}

	/*************************************************
	 * @Title: getAccountInfo
	 * @Description: TODO(获取账户信息)
	 * @param key
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-30
	 *************************************************/
	public void getAccountInfo(int key) {
		showDialog();
		String url = Url.MEM_INFO_ARRAY;
		// String
		// url="http://192.168.10.132:8000/mymhotel-uum-web/mem/account/member_account_info.json";
		InternetConfig config = new InternetConfig();
		config.setHead(CommonUtils.inHeaders());
		config.setKey(key);
		JsonObject json = new JsonObject();
		json.addProperty("account", MangrovePayApp.app.getData("account") + "");
		json.addProperty("subType", "");
		json.addProperty("includeSubAcc", "1");
		FastHttpHander.ajaxString(url, json.toString(), config, this);
	}

	/*************************************************
	 * @Title: result
	 * @Description: TODO(check 当前用户是否开启账户余额)
	 * @param r
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-30
	 *************************************************/
	@InjectHttpOk(value = { 1, 2 })
	public void result(ResponseEntity r) {
		dismissDialog();
		switch (r.getStatus()) {
		case FastHttp.result_ok:
			if (!ResultParse.isResultOK(r, activity)) {
				Toast.makeText(activity, "未进行实名验证不能进行此操作", Toast.LENGTH_SHORT)
						.show();
			} else {
				if (r.getKey() == 1) {
					startActivity(new Intent().setClass(activity,
							FindPayPasswordActivity.class));
				} else {
					startActivity(new Intent().setClass(activity,
							ChangePayPasswordActivity.class));
				}
			}
			break;
		case FastHttp.result_net_err:
			Toast.makeText(activity, ErrorMsgEnum.NET_ERROR.getName(),
					Toast.LENGTH_SHORT).show();
			break;
		}
	}

	// 注销返回结果
	@InjectHttpOk(3)
	public void resultLogoutOk(ResponseEntity r) {
		Ioc.getIoc().getLogger().i("用户接口退出状态返回结果【" + r + "】");
		dismissDialog();
		// 注销成功 返回OK
		if (ResultParse.isResultOK(r, activity)) {
			// 退出成功
			MapVo.map = null;
			startActivityForResult(new Intent(activity, LoginActivity.class), 1);
			activity.finish();
		}
	}

	/*************************************************
	 * @Title: reusltOk
	 * @Description: TODO(check 版本更新)
	 * @param entity
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-30
	 *************************************************/
	@InjectHttpOk(11)
	public void reusltOk(ResponseEntity entity) {
		dismissDialog();
		HashMap<String, String> map = null;
		if (ResultParse.isResultOK(entity, activity)) {
			map = Handler_Json.JsonToCollection(entity.getContentAsString());
			if (null != map) {
				float newVersion = Float.valueOf(map.get("newVersion"));
				float lowVersion = Float.valueOf(map.get("lowVersion"));
				updateURL = map.get("updateURL");
				if (newVersion > version && version >= lowVersion) {
					// 提示更新
					alertVersion("度假宝有最新版本，请更新", "1");
				} else if (newVersion > version && version < lowVersion) {
					// 强制更新
					alertVersion("您需要更新才能继续使用", "2");
				} else {
					Toast.makeText(activity, "当前已经是最新版本", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}

	/*************************************************
	 * @Title: alert
	 * @Description: TODO(创建提示信息) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-30
	 *************************************************/
	private void alertOut() {
		alert = new CommonAlert(activity);
		alert.setMessage("是否确定退出？");
		alert.setNegativeButton("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Ioc.getIoc().getLogger()
						.i("调用后台获退出接口：[" + Url.METHOD_LOGOUT + "]");
				showDialog();
				InternetConfig config = new InternetConfig();
				config.setKey(3);
				config.setHead(CommonUtils.inHeaders());
				// 调用后台退出接口
				FastHttpHander.ajaxString(Url.METHOD_LOGOUT, null, config,
						SettingFragment.this);
				alert.dismiss();
			}
		});
		alert.setPositiveButton("取消", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alert.dismiss();
			}
		});
	}

}
