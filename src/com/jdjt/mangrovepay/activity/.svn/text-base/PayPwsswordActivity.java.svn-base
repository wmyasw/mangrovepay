package com.jdjt.mangrovepay.activity;

import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.pc.ioc.verification.Rule;
import com.android.pc.ioc.verification.Validator;
import com.android.pc.ioc.verification.Validator.ValidationListener;
import com.android.pc.ioc.verification.annotation.ConfirmPassword;
import com.android.pc.ioc.verification.annotation.Password;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnTextChanged;
import com.android.pc.util.Handler_String;
import com.android.pc.util.Handler_TextStyle;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.common.BaseFragmentActivity;
import com.jdjt.mangrovepay.contanst.Constants;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.ResultParse;

/*************************************************
@copyright:bupt
@author: 王明雨
@date:2015-1-30
@Description:TODO (设置支付密码)
**************************************************/
@InjectLayer(value = R.layout.activity_setpay_password, parent = R.id.center_common, isTitle = true)
public class PayPwsswordActivity extends BaseFragmentActivity implements
		ValidationListener {
	@Password(message = "请设置6位数字作为支付密码", order = 1,maxLength=6,minLength=6)
	@InjectView(R.id.pay_password)
	EditText pay_password;
	@ConfirmPassword(message = "两次输入的密码不一致", order = 2)
	@InjectView(R.id.pay_password_re)
	EditText pay_password_re;
	@InjectView(R.id.btn_set_password)
	Button btn_set_password;
	// 验证框架
	Validator validator;
	Intent intent;
	// InternetConfig config;
	@InjectInit
	public void init() {
		textview_title.setText("设置支付密码");
		intent=getIntent();
		/*
		 * config=new InternetConfig();
		 * config.setHead(BaseFragmentActivity.inHeaders());
		 */
	}

	@InjectMethod(@InjectListener(ids = { R.id.btn_set_password,R.id.btn_account_agreement }, listeners = { OnClick.class }))
	public void click(View view) {
		switch (view.getId()) {
		case R.id.btn_set_password:
			// 验证
			validator = new Validator(this);
			validator.setValidationListener(this);
			validator.validate();
			break;
		case R.id.btn_account_agreement:
			startActivity(new Intent().setClass(this, WebViewAactiviy.class).putExtra("url", Constants.FW_URL));
			break;
		default:
			break;
		}
	
	}

	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule) {
		// TODO Auto-generated method stub
		String message = failedRule.getFailureMessage();
		CommonUtils.onAlertToast(failedView, message, this);
	}

	@Override
	public void onValidationSucceeded() {
		// TODO Auto-generated method stub
		ajaxPost();
	}

	public void ajaxPost() {
		showDialog();
		String url = Url.METHOD_SETTINTBALANCE;
		Ioc.getIoc().getLogger()
				.i("调用后台红树林卡接口：[" + Url.METHOD_SETTINTBALANCE + "]");
		// InternetConfig internetConfig = new InternetConfig();
		config=new InternetConfig();
		config.setKey(1);
		config.setHead(CommonUtils.inHeaders());
		JsonObject json=new JsonObject();
		json.addProperty("realName",	intent.getStringExtra("relName"));
		json.addProperty("idType", "1");
		json.addProperty("idNo", intent.getStringExtra("idNo"));
		json.addProperty("payPassword", pay_password.getText().toString().trim());
		// 调用后台卡绑定信息接口
		FastHttpHander.ajaxString(url,
				json.toString(), config, this);
	}
	@InjectHttpOk
	public void result(ResponseEntity r){
		dismissDialog();
		if(ResultParse.isResultOK(r,this)){
			Intent intent = new Intent();
			intent.setClass(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
			intent.putExtra("balance", "ok");
			startActivity(intent);
		}
	}
	@InjectHttpErr
	public void netError(ResponseEntity r){
		dismissDialog();
		Toast.makeText(getApplicationContext(), "网络无法连接",
				Toast.LENGTH_SHORT).show();
	}
	/*************************************************
	 * @Title: change
	 * @Description: TODO(根据文本变更 他更改按钮状态)
	 * @param s
	 * @param start
	 * @param before
	 * @param count
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-8
	 *************************************************/
	@InjectMethod(@InjectListener(ids = { R.id.pay_password_re, R.id.pay_password }, listeners = { OnTextChanged.class }))
	public void change(CharSequence s, int start, int before, int count) {

		Editable name = pay_password.getText();
		Editable pwd = pay_password_re.getText();
		if (!Handler_String.isEmpty(name + "")
				&& !Handler_String.isEmpty(pwd + "")) {
			btn_set_password.setEnabled(true);
		} else {
			btn_set_password.setEnabled(false);
		}
	}

}
