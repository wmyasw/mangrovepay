package com.jdjt.mangrovepay.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.inject.InjectHttp;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.inject.InjectListener;
import com.android.pc.ioc.inject.InjectMethod;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.internet.FastHttp;
import com.android.pc.ioc.internet.FastHttpHander;
import com.android.pc.ioc.internet.InternetConfig;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.ioc.verification.Rule;
import com.android.pc.ioc.verification.Validator;
import com.android.pc.ioc.verification.Validator.ValidationListener;
import com.android.pc.ioc.verification.annotation.ConfirmPassword;
import com.android.pc.ioc.verification.annotation.Password;
import com.android.pc.ioc.verification.annotation.Regex;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnTextChanged;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_Network;
import com.android.pc.util.Handler_String;
import com.android.pc.util.Handler_TextStyle;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.common.BaseFragmentActivity;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.ResultParse;

/**
 * 重置密码
 * 
 * @copyright bupt
 * @description 重置支付密码
 * @author lijie
 * @date 2014-11-27
 */
@InjectLayer(value = R.layout.re_pay_password, parent = R.id.center_common, isTitle = true)
public class FindPayPwdResetActivity extends BaseFragmentActivity implements
		ValidationListener {
	@Password(maxLength = 6, minLength = 6, message = "密码不符合规则，请重新输入", order = 1)
	@InjectView(value = R.id.new_pwd)
	EditText new_pwd;
	@ConfirmPassword(message = "两次输入的密码不一致，请重新输入", order = 2)
	@InjectView(value = R.id.continue_pwd)
	EditText continue_pwd;
	@InjectView(value = R.id.find_reset_button)
	Button find_reset_button;
	Intent intent;

	Map<String, String> resetMap;

	Validator validator;

	/**
	 * 提交，找回密码
	 * 
	 * @function click
	 * @description 提交找回密码点击时间
	 * @param view
	 * @return void
	 */
	@InjectMethod(@InjectListener(ids = { R.id.find_reset_button }, listeners = { OnClick.class }))
	public void click(View view) {
		// 验证手机是否联网
		if (!Handler_Network.isNetworkAvailable(this)) {
			Toast.makeText(this, "手机未联网", Toast.LENGTH_SHORT).show();
			return;
		}
		// 验证
		validator = new Validator(this);
		validator.setValidationListener(this);
		validator.validate();

	}

	/**
	 * 前端验证通过
	 * 
	 * @function onValidationSucceeded
	 * @description TODO
	 */
	@Override
	public void onValidationSucceeded() {
		showDialog();
		String url;
		url = Url.METHOD_RESET_PAYMENT_PASSWORD;

		// 设置参数
		this.setPasswordParams();
		// 调用后台验证码
		FastHttpHander.ajaxString(url, Handler_Json.beanToJson(resetMap),
				config, this);
	}

	/**
	 * 重置密码调用接口返回结果
	 * 
	 * @function result
	 * @description 重置密码调用接口返回结果
	 * @param r
	 *            返回结果封装实体类
	 * @return void
	 */
	@InjectHttp
	public void result(ResponseEntity r) {
		Ioc.getIoc().getLogger().i("重置密码返回结果【" + r + "】");
		dismissDialog();
		switch (r.getStatus()) {
		case FastHttp.result_ok:

			// 返回OK
			if (ResultParse.isResultOK(r, this)) {
				Toast.makeText(this, "支付密码设置成功", Toast.LENGTH_SHORT).show();
				finish();
			}
			// 失败
			else {
				Ioc.getIoc().getLogger().i("重置密码失败");
			}

			break;

		// 失败
		case FastHttp.result_net_err:
			Ioc.getIoc().getLogger().i("重置密码失败");
			Toast.makeText(this, "重置密码失败,请检查网络", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	/**
	 * 初始化控件
	 * 
	 * @function init
	 * @description 初始化
	 * @return void
	 */
	@InjectInit
	public void init() {
		intent = this.getIntent();

		/*
		 * find_reset_account.setText(intent.getStringExtra("account")); //隐藏底部
		 * hiddenBottomBar();
		 */

		super.textview_title.setText("重置支付密码");
	}

	@InjectMethod(@InjectListener(ids = { R.id.new_pwd, R.id.continue_pwd }, listeners = { OnTextChanged.class }))
	public void change(CharSequence s, int start, int before, int count) {

		Editable newpwd = new_pwd.getText();
		Editable repwd = continue_pwd.getText();
		if (!Handler_String.isEmpty(newpwd + "")
				&& !Handler_String.isEmpty(repwd + "")) {
			find_reset_button.setEnabled(true);
		} else {
			find_reset_button.setEnabled(false);
		}
	}

	/**
	 * 封装验证码参数数据
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, String> setPasswordParams() {

		resetMap = new HashMap<String, String>();
		resetMap.put("account", intent.getStringExtra("account"));
		resetMap.put("code", intent.getStringExtra("code"));
		resetMap.put("uuid", intent.getStringExtra("uuid"));
		resetMap.put("password", new_pwd.getText().toString().trim());

		return resetMap;
	}

	/**
	 * 验证不通过
	 * 
	 * @function onValidationFailed
	 * @description 前端验证不通过
	 * @param failedView
	 *            验证控件
	 * @param failedRule
	 *            验证规则
	 */
	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule) {
		String message = failedRule.getFailureMessage();
		CommonUtils.onAlertToast(failedView, message, this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
