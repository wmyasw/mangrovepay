package com.jdjt.mangrovepay.activity;

import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.inject.InjectListener;
import com.android.pc.ioc.inject.InjectMethod;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.verification.Rule;
import com.android.pc.ioc.verification.Validator;
import com.android.pc.ioc.verification.Validator.ValidationListener;
import com.android.pc.ioc.verification.annotation.Regex;
import com.android.pc.ioc.verification.annotation.Required;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnTextChanged;
import com.android.pc.util.Handler_String;
import com.android.pc.util.Handler_TextStyle;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.common.BaseFragmentActivity;
import com.jdjt.mangrovepay.utils.CommonUtils;

/*************************************************
@copyright:bupt
@author: 王明雨
@date:2015-1-13
@Description:TODO (添加账户余额功能 1、首先实名验证 2、设置支付密码)
**************************************************/
@InjectLayer(value = R.layout.activity_balance_add, parent = R.id.center_common, isTitle = true)
public class BalanceActivity extends BaseFragmentActivity implements
		ValidationListener {
	@Required(message = "请输入您的真实姓名", trim = true, order = 1)
	@InjectView(R.id.member_name)
	EditText member_name;
	@Regex(message = "请输入您的证件号码", trim = true,pattern="^\\d{15}|\\d{18}$" ,order = 2)
	@InjectView(R.id.member_certificate)
	EditText member_certificate;
	@InjectView(R.id.btn_balance)
	Button btn_balance;

	// 验证框架
	Validator validator;
	 //InternetConfig config;
	@InjectInit
	public void init() {
		textview_title.setText("实名信息");
		img_back.setBackgroundResource(R.drawable.close_white);
	/*	config=new InternetConfig();
		config.setHead(BaseFragmentActivity.inHeaders());*/
	}

	@InjectMethod(@InjectListener(ids = { R.id.btn_balance }, listeners = { OnClick.class }))
	public void click(View view) {
		// 验证
		validator = new Validator(this);
		validator.setValidationListener(this);
		validator.validate();
	}

	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule) {
		String message = failedRule.getFailureMessage();
		CommonUtils.onAlertToast(failedView, message, this);
	}

	@Override
	public void onValidationSucceeded() {
		Intent intent=	new Intent().setClass(this, PayPwsswordActivity.class);
		intent.putExtra("idNo", member_certificate.getText().toString().trim());
		intent.putExtra("relName", member_name.getText().toString().trim());
		// TODO Auto-generated method stub
		startActivity(intent);
	}
	/*************************************************
	 @Title: change 
	 @Description: TODO(根据文本变更 他更改按钮状态) 
	 @param s
	 @param start
	 @param before
	 @param count    设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-1-8
	*************************************************/
	@InjectMethod(@InjectListener(ids = { R.id.member_name, R.id.member_certificate }, listeners = { OnTextChanged.class }))
	public void change(CharSequence s, int start, int before, int count) {

		Editable name = member_name.getText();
		Editable pwd = member_certificate.getText();
		if (!Handler_String.isEmpty(name + "")
				&& !Handler_String.isEmpty(pwd + "")) {
			btn_balance.setEnabled(true);
		} else {
			btn_balance.setEnabled(false);
		}
	}

}
