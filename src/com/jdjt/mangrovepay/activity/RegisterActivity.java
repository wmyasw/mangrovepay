package com.jdjt.mangrovepay.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
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
import com.android.pc.ioc.util.MapVo;
import com.android.pc.ioc.verification.Rule;
import com.android.pc.ioc.verification.Rules;
import com.android.pc.ioc.verification.Validator;
import com.android.pc.ioc.verification.Validator.ValidationListener;
import com.android.pc.ioc.verification.annotation.Password;
import com.android.pc.ioc.verification.annotation.Telphone;
import com.android.pc.ioc.verification.annotation.TextRule;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnTextChanged;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_Network;
import com.android.pc.util.Handler_SharedPreferences;
import com.android.pc.util.Handler_String;
import com.android.pc.util.Handler_TextStyle;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.common.BaseFragmentActivity;
import com.jdjt.mangrovepay.contanst.Constants;
import com.jdjt.mangrovepay.contanst.ErrorMsgEnum;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.CountTimer;
import com.jdjt.mangrovepay.utils.ResultParse;
import com.jdjt.mangrovepay.utils.Uuid;

@InjectLayer(value = R.layout.activity_regsiter, parent = R.id.center_common, isTitle = true)
public class RegisterActivity extends BaseFragmentActivity implements
		ValidationListener {
	@Telphone(empty = false, message = "请输入正确的手机号", order = 1)
	@InjectView
	EditText register_account;
	@TextRule(maxLength = 6, minLength = 4, message = "请输入正确的验证码", order = 3)
	@InjectView
	EditText register_security_code;
	@Password(maxLength = 18, minLength = 6, message = "请输入长度6-18位由字母数字_和-组成的密码", order = 2)
	@InjectView
	EditText register_password;

	@InjectView
	Button register_valitation;
	@InjectView(R.id.register_button)
	Button register_button;
	// 验证
	Validator validator;

	Map<String, String> regMap;

	InternetConfig inConfig;
	CountTimer cm;
	String uuid;

	@InjectInit
	public void init() {
		// Ioc.getIoc().getLogger().i("进入注册界面，初始化ui");
		inConfig = new InternetConfig();
		inConfig.setHead(CommonUtils.inHeaders());
		textview_title.setText("");
		img_back.setBackgroundResource(R.drawable.close_white);
	}
	@InjectMethod(@InjectListener(ids = { R.id.register_account, R.id.register_security_code, R.id.register_password }, listeners = { OnTextChanged.class }))
	public void change(CharSequence s, int start, int before, int count) {

		Editable name = register_account.getText();
		Editable pwd = register_password.getText();
		Editable code =register_security_code.getText();
		if (!Handler_String.isEmpty(name + "")
				&& !Handler_String.isEmpty(pwd + "")&&!Handler_String.isEmpty(code + "")) {
			register_button.setEnabled(true);
		} else {
			register_button.setEnabled(false);
		}
	}

	@InjectMethod(@InjectListener(ids = { R.id.register_button,
			R.id.register_valitation ,R.id.register_agree_text}, listeners = { OnClick.class }))
	public void checkClick(View view) {
		switch (view.getId()) {
		// 提交注册
		case R.id.register_button:

			// 验证
			validator = new Validator(this);
			validator.setValidationListener(this);
			validator.validate();

			break;

		// 发送验证码
		case R.id.register_valitation:
			String account = register_account.getText()+"";
			// 验证账号 邮箱,手机
			if (!account.matches(Rules.REGEX_TELPHONE)) {
				CommonUtils.onAlertToast(register_account, "请输入正确的手机号", this);
				return;
			}
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
			if (MapVo.get("register_tel_valitation") != null) {
				Toast.makeText(getApplicationContext(),
						ErrorMsgEnum.CHECK_CONTINUE.getName(),
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				uuid = Uuid.getUuid();// 用于参数的uuid
				MapVo.set(account, uuid);
			}
			inConfig.setKey(4);
			JsonObject json=new JsonObject();
			json.addProperty("account", account);
			// 验证账号是否已注册
			FastHttpHander.ajaxString(Url.MEM_CHECK_ACCOUNT, json.toString(), inConfig, this);

			break;
		case R.id.register_agree_text:
			startActivity(new Intent().setClass(this, WebViewAactiviy.class).putExtra("url", Constants.ZC_URL));
			break;
		default:
			break;
		}
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
		showDialog();
		Ioc.getIoc().getLogger().i("注册前端验证成功");
		// 封装参数数据
		setRegParams();

		// 验证手机是否联网
		if (!Handler_Network.isNetworkAvailable(getApplicationContext())) {
			Toast.makeText(getApplicationContext(),
					ErrorMsgEnum.NET_NOT_CONNECT.getName(), Toast.LENGTH_SHORT)
					.show();
			return;
		}

		Ioc.getIoc().getLogger().i("调用后台注册接口：[" + Url.METHOD_REG + "]");
		InternetConfig config = new InternetConfig();
		config.setKey(2);
		config.setHttps(true);
		config.setHead(CommonUtils.inHeaders());
		this.setRegParams();// 设置注册数据
		// ((LoginAndRegisterFragmentgetApplicationContext())getApplicationContext()).dialog.show();
		// 调用后台注册接口
		FastHttpHander.ajaxString(Url.METHOD_REG,
				Handler_Json.beanToJson(regMap), config, this);

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

		Ioc.getIoc().getLogger().i("验证码接口状态返回结果【" + r + "】");

		// 返回OK
		ResultParse.isResultOK(r,this);
	}

	/**
	 * 注册成功返回结果
	 * 
	 * @function regResultOk
	 * @description 注册成功返回结果
	 * @param r
	 */
	@InjectHttpOk(2)
	private void regResultOk(ResponseEntity r) {
		Ioc.getIoc().getLogger().i("注册状态返回结果【" + r + "】");
		dismissDialog();
		// 注册成功 返回OK
		if (ResultParse.isResultOK(r, this)) {

			Ioc.getIoc().getLogger().i("注册成功");

			// 调用登录接口

			Ioc.getIoc().getLogger().i("调用后台登录接口：[" + Url.METHOD_LOGIN + "]");

			// 设置登录数据
			setLoginParams();

			inConfig.setKey(3);
			// 调用后台登录接口
			FastHttpHander.ajaxString(Url.METHOD_LOGIN,
					Handler_Json.beanToJson(regMap), inConfig, this);

		} else {
			// ((LoginAndRegisterFragmentActivity)activity).dialog.cancel();
		}
	}

	/**
	 * 登录成功返回结果
	 * 
	 * @function loginResultOk
	 * @description 注册成功返回结果
	 * @param r
	 */
	@InjectHttpOk(3)
	private void loginResultOk(ResponseEntity r) {
		Ioc.getIoc().getLogger().i("登录接口状态返回结果【" + r + "】");
		dismissDialog();
		// 登录成功 返回OK
		if (ResultParse.isResultOK(r, this)) {
			// 获取返回内容 ticket
			String jsonString = r.getContentAsString();

			Ioc.getIoc().getLogger().i("ticket数据【" + jsonString + "】");

			// 存放ticket
			MapVo.map = Handler_Json.JsonToCollection(jsonString);// 设置ticket
																	// TODO

			// 存储用户名密码到本地
			MangrovePayApp.app.setData("account", regMap.get("account"));
			MangrovePayApp.app.setData("password", regMap.get("password"));

			Ioc.getIoc().getLogger().i("登录成功");

			this.setResult(Activity.RESULT_OK, new Intent(
					getApplicationContext(), MainActivity.class));

			this.finish();
		}

		// ((LoginAndRegisterFragmentActivity)activity).dialog.cancel();
	}

	/**
	 * 验证账号返回结果
	 * 
	 * @function checkAccountResultOk
	 * @description 注册成功返回结果
	 * @param r
	 */
	@InjectHttpOk(4)
	private void checkAccountResultOk(ResponseEntity r) {
		Ioc.getIoc().getLogger().i("验证账号返回结果【" + r + "】");
		// ((LoginAndRegisterFragmentActivity)activity).dialog.cancel();
		dismissDialog();
		// 注册成功 返回OK
		if (ResultParse.isResultOK(r, this)) {
			String result = Handler_Json.getValue("result",
					r.getContentAsString()).toString();
			// 账号重复
			if (result.equals("1")) {
				CommonUtils.onAlertToast(register_account, "该手机号已注册", this);
				return;
			}

			// 不重复
			// 调用后台接口，往手机上发送验证码
			cm = new CountTimer(60000, 1000, register_valitation,
					"register_tel_valitation");
			cm.start();

			inConfig.setKey(1);
			this.setCodeParams();// 设置验证码数据

			// 调用后台验证码
			FastHttpHander.ajaxString(Url.METHOD_GETCODE,
					Handler_Json.beanToJson(regMap), inConfig, this);
		}
	}

	/**
	 * 封装验证码参数数据
	 * 
	 * @function setCodeParams
	 * @description 封装验证码参数数据
	 * @return
	 */
	public Map<String, String> setCodeParams() {

		regMap = new HashMap<String, String>();
		regMap.put("account", register_account.getText().toString().trim());
		regMap.put("logicFlag", "1");
		regMap.put("uuid", uuid);

		return regMap;
	}

	/*************************************************
	 * @Title: setLoginParams
	 * @Description: TODO(封装登录参数数据)
	 * @return 设定文件
	 * @return Map<String,String> 返回类型
	 * @throws
	 * @date 2015-1-7
	 *************************************************/
	public Map<String, String> setLoginParams() {

		regMap = new HashMap<String, String>();
		regMap.put("account", register_account.getText().toString().trim());
		regMap.put("password", register_password.getText().toString().trim());

		return regMap;
	}

	/**
	 * 封装注册参数数据
	 * 
	 * @function setRegParams
	 * @description 封装注册参数数据
	 * @return
	 */
	public Map<String, String> setRegParams() {

		regMap = new HashMap<String, String>();
		regMap.put("account", register_account.getText().toString().trim());
		regMap.put("password", register_password.getText().toString().trim());
		regMap.put("code", register_security_code.getText().toString().trim());
		regMap.put("uuid", uuid);

		return regMap;
	}

	/**
	 * 请求数据失败处理
	 * 
	 * @function resultErr
	 * @description 请求数据失败处理
	 * @param r
	 */
	@InjectHttpErr(value = { 1, 2, 3, 4 })
	private void resultErr(ResponseEntity r) {
		dismissDialog();
		switch (r.getKey()) {
		// 验证码
		case 1:
			Ioc.getIoc().getLogger().i("获取验证码失败");
			Toast.makeText(getApplicationContext(), "获取验证码失败，请检查网络",
					Toast.LENGTH_SHORT).show();
			break;
		// 注册
		case 2:
			Ioc.getIoc().getLogger().i("注册失败");
			Toast.makeText(getApplicationContext(), "注册失败，请检查网络",
					Toast.LENGTH_SHORT).show();
			break;
		// 登录
		case 3:
			Ioc.getIoc().getLogger().i("登录失败");
			Toast.makeText(getApplicationContext(), "登录失败，请检查网络",
					Toast.LENGTH_SHORT).show();
			break;
		// 账号验证
		case 4:
			Ioc.getIoc().getLogger().i("账号验证失败");
			Toast.makeText(getApplicationContext(), "账号验证失败，请检查网络",
					Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		// ((LoginAndRegisterFragmentActivity)activity).dialog.cancel();
	}
}
