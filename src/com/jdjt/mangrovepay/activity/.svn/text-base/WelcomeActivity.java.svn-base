package com.jdjt.mangrovepay.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.download.FileLoaderManager;
import com.android.pc.ioc.download.NotfiEntity;
import com.android.pc.ioc.inject.InjectHttpErr;
import com.android.pc.ioc.inject.InjectHttpOk;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.internet.FastHttpHander;
import com.android.pc.ioc.internet.InternetConfig;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.ioc.util.MapVo;
import com.android.pc.ioc.verification.Validator;
import com.android.pc.ioc.verification.annotation.Password;
import com.android.pc.ioc.verification.annotation.Telphone;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_Network;
import com.android.pc.util.Handler_String;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.contanst.ErrorMsgEnum;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.utils.CommonAlert;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.ResultParse;
import com.jdjt.mangrovepay.view.ClearEditText;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-6
 * @Description:TODO (进入欢迎页)
 **************************************************/
@InjectLayer(value = R.layout.welcome)
public class WelcomeActivity extends Activity {

	@InjectView(R.id.tx_register_link)
	public TextView tx_register_link;
	@Telphone(empty = false, message = "请输入正确的手机号", order = 1)
	@InjectView(R.id.login_name)
	public ClearEditText login_name;
	@Password(maxLength = 18, minLength = 6, message = "请输入长度6-18位由字母数字_和-组成的密码", order = 2)
	@InjectView(R.id.login_pwd)
	public ClearEditText login_pwd;
	@InjectView(R.id.login_button)
	public Button login_button;

	// 验证
	public Validator validator;
	public Map<String, String> loginMap;
	public String updateURL;
	public CommonAlert alert;

	/*************************************************
	 * @Title: init
	 * @Description: TODO(初始化完成后加载信息) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-8
	 *************************************************/
	@InjectInit
	public void init() {
		ajaxIsVerson();

	}
	
	/*************************************************
	 * @Title: ajaxIsVerson
	 * @Description: TODO(获取网站 版本查看是否需要更新) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-30
	 *************************************************/
	public void ajaxIsVerson() {
		String url = Url.MEM_VERSIONS;
		InternetConfig config = new InternetConfig();
		config.setKey(1);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxString(url, "", config, this);
	}

	/*************************************************
	 * @Title: ajaxPostLogin
	 * @Description: TODO(登录方法）
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-30
	 *************************************************/
	public void ajaxPostLogin() {
		// 验证手机是否联网
		if (!Handler_Network.isNetworkAvailable(this)) {
			Toast.makeText(this, ErrorMsgEnum.NET_ERROR.getName(),
					Toast.LENGTH_SHORT).show();
			return;
		}
		InternetConfig config = new InternetConfig();
		config.setKey(R.raw.client);
		config.setHead(CommonUtils.inHeaders());
		Ioc.getIoc().getLogger().i("调用后台登录接口：[" + Url.METHOD_LOGIN + "]");
		// 调用后台登录接口
		FastHttpHander.ajaxString(Url.METHOD_LOGIN,
				Handler_Json.beanToJson(loginMap), config, this);
	}

	/*************************************************
	 * @Title: success
	 * @Description: TODO(登录返回)
	 * @param entity
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-8
	 *************************************************/
	@InjectHttpOk(value = { R.raw.client, 1 })
	public void success(ResponseEntity entity) {
		HashMap<String, String> map = null;
		switch (entity.getKey()) {
		case R.raw.client:
			Ioc.getIoc().getLogger().i("登录接口状态返回结果【" + entity + "】");

			// 获取头部信息
			// map = entity.getHeaders();

			// 登录成功 返回OK
			if (ResultParse.isResultOK(entity, this)) {
				// 获取返回内容 ticket
				String jsonString = entity.getContentAsString();

				Ioc.getIoc().getLogger().i("ticket数据【" + jsonString + "】");

				// 存放ticket
				MapVo.map= Handler_Json.JsonToCollection(jsonString);// 设置ticket
																		// TODO
				MangrovePayApp.ticket=(String) MapVo.get("ticket");
				
				// 存储用户名密码到本地
				MangrovePayApp.app.setData("account", loginMap.get("account"));
				MangrovePayApp.app
						.setData("password", loginMap.get("password"));
				Ioc.getIoc().getLogger().i("登录成功tickt"+MangrovePayApp.ticket);
				Ioc.getIoc().getLogger().i("登录成功");

				Intent intent = new Intent();
				intent.setClass(this, MainActivity.class);
				startActivity(intent);
				finish();
			}
			// 登录失败
			else {
				Ioc.getIoc().getLogger().i("登录失败");
				// Toast.makeText(this, ErrorMsgEnum.NET_ERROR.getName(),
				// Toast.LENGTH_SHORT).show();
				try {
					Thread.sleep(3000);
					startActivity(new Intent().setClass(this,
							LoginActivity.class));
					finish();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			break;
		case 1:
			if (ResultParse.isResultOK(entity, this)) {
				map = Handler_Json
						.JsonToCollection(entity.getContentAsString());
				if (null != map) {
					float newVersion = Float.valueOf(map.get("newVersion"));
					float lowVersion = Float.valueOf(map.get("lowVersion"));
					updateURL = map.get("updateURL");
					if (newVersion > MangrovePayApp.version && MangrovePayApp.version >= lowVersion) {
						// 提示更新
						alertVersion("度假宝有最新版本，请更新", "1");
					}else if (newVersion > MangrovePayApp.version && MangrovePayApp.version < lowVersion) {
						// 强制更新
						alertVersion("您需要更新才能继续使用", "2");
					}else{//否则去加载登录 
						isLogin();
					}
				}
			}
			break;
		default:
			break;
		}

	}
private void isLogin(){
	String account = MangrovePayApp.app.getData("account");
	String password = MangrovePayApp.app.getData("password");
	if (null != MapVo.get("ticket")) {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		finish();
	} else {
		if (!Handler_String.isEmpty(account)
				&& !Handler_String.isEmpty(password)) {
			loginMap = new HashMap<String, String>();
			loginMap.put("account", account.trim());
			loginMap.put("password", password.trim());
			ajaxPostLogin();
		} else {
			startActivity(new Intent().setClass(this, LoginActivity.class));
			finish();
		}
	}
}
	public void alertVersion(String msg, final String type) {
		alert = new CommonAlert(this);
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
				update();
			}
		});
		alert.setPositiveButton(clear, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				// TODO Auto-generated method stub
				if ("1".equals(type)){
					isLogin();
					alert.dismiss();
				}
				else
					finish();
			}
		});
	}

	private void update() {
		// 构建一个下载通知栏对象 并填充资源
	
			Toast.makeText(this, "正在下载。。",
					Toast.LENGTH_SHORT).show();
		NotfiEntity notfi = new NotfiEntity();
		notfi.setLayout_id(R.layout.update_notification_progress);
		notfi.setIcon_id(R.id.updatehelper_notification_progress_icon);
		notfi.setProgress_id(R.id.updatehelper_notification_progress_pb);
		notfi.setProgress_txt_id(R.id.updatehelper_notification_progress_tv);
		FileLoaderManager.downloadUpdate(updateURL, 3, notfi);
	}

	@InjectHttpErr
	public void fail(ResponseEntity entity) {
		Toast.makeText(this, ErrorMsgEnum.NET_ERROR.getName(),
				Toast.LENGTH_SHORT).show();
		startActivity(new Intent().setClass(this, LoginActivity.class));
		finish();

	}

}
