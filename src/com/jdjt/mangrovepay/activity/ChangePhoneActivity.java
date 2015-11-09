package com.jdjt.mangrovepay.activity;

import java.util.Map;

import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.inject.InjectHttpErr;
import com.android.pc.ioc.inject.InjectHttpOk;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.inject.InjectListener;
import com.android.pc.ioc.inject.InjectMethod;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.internet.FastHttpHander;
import com.android.pc.ioc.internet.InternetConfig;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.ioc.util.MapVo;
import com.android.pc.ioc.verification.Rule;
import com.android.pc.ioc.verification.Validator;
import com.android.pc.ioc.verification.Validator.ValidationListener;
import com.android.pc.ioc.verification.annotation.TextRule;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnTextChanged;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_Network;
import com.android.pc.util.Handler_String;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.common.BaseFragmentActivity;
import com.jdjt.mangrovepay.contanst.ErrorMsgEnum;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.CountTimer;
import com.jdjt.mangrovepay.utils.ResultParse;
import com.jdjt.mangrovepay.utils.Uuid;

/*************************************************
@copyright:bupt
@author: 王明雨
@date:2015-2-3
@Description:TODO (修改绑定手机)
**************************************************/
@InjectLayer(value = R.layout.activity_change_phone, parent = R.id.center_common,  isTitle = true)
public class ChangePhoneActivity extends BaseFragmentActivity implements
		ValidationListener {
	@TextRule(maxLength = 6, minLength = 4, message = "请输入正确的验证码", order = 1)
	@InjectView(R.id.change_phone_valid_code)
	EditText change_phone_valid_code;
	@InjectView(R.id.tx_change_phone)
	TextView tx_change_phone;
	@InjectView(R.id.change_phone_valitation)
	Button change_phone_valitation;
	// 验证
	Validator validator;
	@InjectView(R.id.btn_change_phone)
	Button btn_change_phone;
	Map<String, String> regMap;

	CountTimer cm;
	String uuid;
	String account;
	@InjectInit
	public void init() {
		account=tx_change_phone.getText().toString().trim();
		//Ioc.getIoc().getLogger().i("进入注册界面，初始化ui");
		textview_title.setText("更换绑定手机");
		tx_change_phone.setText(MangrovePayApp.app.getData("account"));
		// 获取验证码60秒钟内的uuid,如果有则取,如果没有则重新生成
	}
	@InjectMethod(@InjectListener(ids = { R.id.change_phone_valid_code }, listeners = { OnTextChanged.class }))
	public void change(CharSequence s, int start, int before, int count) {

		Editable name = change_phone_valid_code.getText();
		if (!Handler_String.isEmpty(name + "")
			) {
			btn_change_phone.setEnabled(true);
		} else {
			btn_change_phone.setEnabled(false);
		}
	}
	@InjectMethod(@InjectListener(ids = { R.id.btn_change_phone,
			R.id.change_phone_valitation }, listeners = { OnClick.class }))
	public void checkClick(View view) {
		switch (view.getId()) {
		// 提交注册
		case R.id.btn_change_phone:

			// 验证
			validator = new Validator(this);
			validator.setValidationListener(this);
			validator.validate();

			break;

		// 发送验证码
		case R.id.change_phone_valitation:
			String account = tx_change_phone.getText().toString().trim();
			// 验证手机是否联网
			if (!Handler_Network.isNetworkAvailable(getApplicationContext())) {
				Toast.makeText(this, ErrorMsgEnum.NET_NOT_CONNECT.getName(),
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (MapVo.get(account) != null) {
				uuid = MapVo.get(account).toString();
			} else {
				uuid = Uuid.getUuid();// 给初始值
			}
			// 一分钟内不能重复点击
			if (MapVo.get("change_phone_valitation") != null) {
				Toast.makeText(getApplicationContext(),
						ErrorMsgEnum.CHECK_CONTINUE.getName(),
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				uuid = Uuid.getUuid();// 用于参数的uuid
				MapVo.set(account, uuid);
			}
			

			showDialog();
			config=new InternetConfig();
			config.setKey(1);
			config.setHead(CommonUtils.inHeaders());
			JsonObject json=new JsonObject();
			json.addProperty("account", account);
			json.addProperty("logicFlag", "3");
			json.addProperty("uuid", uuid);
			
			//不重复
			//调用后台接口，往手机上发送验证码
			 cm = new CountTimer(60000, 1000,change_phone_valitation,"change_phone_valitation");  
		     cm.start();  
			// 验证账号是否已注册
			FastHttpHander.ajaxString(Url.METHOD_GETCODE, json.toString(), config, this);

			break;
		default:
			break;
		}
	}

	
	public void checkCode(){
		showDialog();
		String url=Url.METHOD_CHECKCAPTCHA;
		JsonObject json=new JsonObject();
		json.addProperty("uuid", uuid);
		json.addProperty("code", change_phone_valid_code.getText()+"");
		config=new InternetConfig();
		config.setKey(2);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxString(url, json.toString(), config, this);
	}
	/**
	 * 前端验证失败
	 * 
	 * @function onValidationFailed
	 * @description 前端验证失败
	 * @param failedView
	 * @param failedRule
	 */
	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule) {
		String message = failedRule.getFailureMessage();
		CommonUtils.onAlertToast(failedView, message, this);
	}

	/*************************************************
	 * @Title: onValidationSucceeded
	 * @Description: TODO(验证成功) 设定文件
	 * @throws
	 * @date 2015-1-7
	 * @see com.android.pc.ioc.verification.Validator.ValidationListener#onValidationSucceeded()
	 *************************************************/
	@Override
	public void onValidationSucceeded() {
		checkCode();
	}

	/**
	 * 验证码成功返回结果
	 * 
	 * @function codeResultOk
	 * @description 验证码成功返回结果
	 * @param r
	 */
	@InjectHttpOk(1)
	private void codeResultOk(ResponseEntity r) {
		dismissDialog();
		Ioc.getIoc().getLogger().i("验证码接口状态返回结果【" + r + "】");
		if(!ResultParse.isResultOK(r, this)){
			Toast.makeText(getApplicationContext(), "获取验证码失败，请稍后再次尝试",
					Toast.LENGTH_SHORT).show();
		}
	}
	/*************************************************
	 @Title: resultCheckCodeOk 
	 @Description: TODO(check验证码) 
	 @param r    设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-1-29
	*************************************************/
	@InjectHttpOk(2)
	private void resultCheckCodeOk(ResponseEntity r) {
		dismissDialog();
		Ioc.getIoc().getLogger().i("check验证码返回结果【" + r.toString() + "】");
		if(ResultParse.isResultOK(r, this)){
		String result=	(String) Handler_Json.getValue("result",r.getContentAsString());
			if("0".equals(result)){
				String account = tx_change_phone.getText().toString().trim();
				Intent intent=new Intent().setClass(this, BindPhoneActivity.class);
				intent.putExtra("uuid", uuid);
				intent.putExtra("account", account);
				intent.putExtra("code", change_phone_valid_code.getText()+"");
				startActivity(intent);
				finish();
			}else{
                /** 以对话框的形式*/				
//				CommonUtils.onAlertToast(change_phone_valid_code, "无效的验证码", this);
				Toast.makeText(getApplicationContext(), "无效的验证码",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	

	/**
	 * 请求数据失败处理
	 * 
	 * @function resultErr
	 * @description 请求数据失败处理
	 * @param r
	 */
	@InjectHttpErr(value = { 1})
	private void resultErr(ResponseEntity r) {
		dismissDialog();
		switch (r.getKey()) {
		// 验证码
		case 1:
			Ioc.getIoc().getLogger().i("获取验证码失败");
			Toast.makeText(getApplicationContext(), "获取验证码失败，请检查网络",
					Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		// ((LoginAndRegisterFragmentActivity)activity).dialog.cancel();
	}
}
