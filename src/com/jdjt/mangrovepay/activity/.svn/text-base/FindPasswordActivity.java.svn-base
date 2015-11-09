package com.jdjt.mangrovepay.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.ioc.util.MapVo;
import com.android.pc.ioc.verification.Rule;
import com.android.pc.ioc.verification.Rules;
import com.android.pc.ioc.verification.Validator;
import com.android.pc.ioc.verification.Validator.ValidationListener;
import com.android.pc.ioc.verification.annotation.Telphone;
import com.android.pc.ioc.verification.annotation.TextRule;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnTextChanged;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_Network;
import com.android.pc.util.Handler_String;
import com.android.pc.util.Handler_TextStyle;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.common.BaseFragmentActivity;
import com.jdjt.mangrovepay.contanst.ErrorMsgEnum;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.utils.CommonAlert;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.CountTimer;
import com.jdjt.mangrovepay.utils.ResultParse;
import com.jdjt.mangrovepay.utils.Uuid;

/**
 * 找回密码
 * 
 * @copyright bupt
 * @description 找回密码
 * @author lijie
 * @date 2014-11-27
 */
@InjectLayer(value = R.layout.find_password_check, parent = R.id.center_common)
public class FindPasswordActivity extends BaseFragmentActivity implements
		ValidationListener {
	@Telphone(empty = false, message = "请输入正确的手机号", order = 1)
	@InjectView(value = R.id.find_account)
	EditText find_account;// 账户
	@TextRule(maxLength = 6, minLength = 6, message = "请输入正确的验证码", order = 2)
	@InjectView(value = R.id.find_security_code)
	EditText find_security_code;// 验证码
	@InjectView(value = R.id.find_valitation)
	Button find_valitation;// 验证按钮
	CountTimer mc;// 倒计时
	Map<String, String> findMap;// 传参数据
	@InjectView(value = R.id.find_next_button)
	Button find_next_button;
	String uuid;

	/**
	 * 初始化控件
	 * 
	 * @function init
	 * @description 初始化控件
	 * @param
	 * @return void
	 */
	@InjectInit
	public void init() {
		// 设置模态页面关闭
		/*
		 * Drawable drawable=
		 * getResources().getDrawable(R.drawable.icon_cross_white); ///
		 * 这一步必须要做,否则不会显示. drawable.setBounds(0, 0, drawable.getMinimumWidth(),
		 * drawable.getMinimumHeight());
		 * button_back.setCompoundDrawables(drawable,null,null,null); //隐藏底部
		 * hiddenBottomBar();
		 */
		config.setHead(CommonUtils.inHeaders());
		img_back.setBackgroundResource(R.drawable.close_white);
		textview_title.setText("找回密码");
	}

	/**
	 * 提交，找回密码
	 * 
	 * @function click
	 * @description 点击事件,提交找回密码
	 * @param view
	 * @return void
	 */
	@InjectMethod(@InjectListener(ids = { R.id.find_next_button,
			R.id.find_valitation }, listeners = { OnClick.class }))
	public void click(View view) {
		// 验证手机是否联网
		if (!Handler_Network.isNetworkAvailable(this)) {
			Toast.makeText(this, ErrorMsgEnum.NET_ERROR.getName(),
					Toast.LENGTH_SHORT).show();
			return;
		}

		switch (view.getId()) {
		case R.id.find_next_button:
			// 验证
			Validator validator = new Validator(this);
			validator.setValidationListener(this);
			validator.validate();

			break;

		case R.id.find_valitation:
			String account = find_account.getText().toString().trim();
			if(Handler_String.isBlank(account)&&!account.matches(Rules.REGEX_TELPHONE)){
				CommonUtils.onAlertToast(find_account, "请输入正确手机号", this);
				return;
			}
			if (MapVo.get(account) != null) {
				uuid = MapVo.get(account).toString();
			} else {
				uuid = Uuid.getUuid();// 给初始值
				 MapVo.set(account,uuid);
			}

			Ioc.getIoc().getLogger()
					.i("调用后台验证码接口：[" + Url.METHOD_GETCODE + "]");

			config.setKey(3);
			
			// 验证账号是否已注册
			FastHttpHander.ajaxString(Url.MEM_CHECK_ACCOUNT, "{'account':'"
					+ account + "'}", config, this);

			break;

		default:
			break;
		}

	}

	/**
	 * 验证码成功返回结果
	 * 
	 * @function codeResultOk
	 * @description 验证码成功返回结果
	 * @param r
	 *            返回结果实体类
	 * @return void
	 */
	@InjectHttpOk(1)
	public void codeResultOk(ResponseEntity r) {

		Ioc.getIoc().getLogger().i("验证码接口状态返回结果【" + r + "】");
		// 返回OK
		if (ResultParse.isResultOK(r, this)) {
			// 根据code 点击验证成功与否 TODO
		}
		// 获取验证码失败
		else {
			Ioc.getIoc().getLogger().i("获取验证码失败");
		}
	}

	/**
	 * 验证码验成功返回结果
	 * 
	 * @function checkCodeResultOk
	 * @description 验证码验证成功返回结果
	 * @param r
	 * @return void
	 */
	@InjectHttpOk(2)
	public void checkCodeResultOk(ResponseEntity r) {

		Ioc.getIoc().getLogger().i("验证码验证接口状态返回结果【" + r + "】");

		// 返回OK
		if (ResultParse.isResultOK(r, this)) {

			Map<String, String> resultMap = Handler_Json.JsonToCollection(r
					.getContentAsString());
			String result = resultMap.get("result");
			if (result.equals("0")) {
				// 正常
				Intent intent = new Intent(this, FindPwdResetActivity.class);
				intent.putExtra("account", find_account.getText().toString());
				intent.putExtra("code", find_security_code.getText().toString());
				intent.putExtra("uuid", uuid);
				startActivity(intent);
				finish();
			} else if (result.equals("1")) {
				// 验证码不存在
				Toast.makeText(this, "验证码不存在", Toast.LENGTH_SHORT).show();
			} else if (result.equals("2")) {
				// 无效的验证码（超时原因）
				Toast.makeText(this, "验证码无效,已超时", Toast.LENGTH_SHORT).show();
			} else if (result.equals("9")) {
				// 无效的验证码（其他原因）
				Toast.makeText(this, "验证码无效", Toast.LENGTH_SHORT).show();
			}

		}
		// 获取验证码失败
		else {
			Ioc.getIoc().getLogger().i("验证码验证失败");
		}

		// cancelDialog();
	}

	/**
	 * 验证账号重复返回结果
	 * 
	 * @function checkAccountResultOk
	 * @description 验证账号重复返回结果
	 * @param r
	 *            返回结果封装实体类
	 * @return void
	 */
	@InjectHttpOk(3)
	public void checkAccountResultOk(ResponseEntity r) {
		Ioc.getIoc().getLogger().i("验证账号返回结果【" + r + "】");

		// 注册成功 返回OK
		if (ResultParse.isResultOK(r, this)) {
			String result = Handler_Json.getValue("result",
					r.getContentAsString()).toString();
			// 账号重复
			if (result.equals("0")) {
				CommonUtils.onAlertToast(find_account, "该手机未注册,不能用于找回密码",
						this);
				return;
			}
			// 不重复
			// 调用后台接口，往手机上发送验证码
			mc = new CountTimer(60000, 1000, find_valitation, "find_valitation");
			mc.start();

			config.setKey(1);
			// 设置参数
			this.setCodeParams();
			// 调用后台验证码
			FastHttpHander.ajaxString(Url.METHOD_GETCODE,
					Handler_Json.beanToJson(findMap), config, this);
		}
	}

	/**
	 * 请求接口失败返回结果处理
	 * 
	 * @function resultErr
	 * @description 请求接口失败返回结果处理
	 * @param r
	 * @return void
	 */
	@InjectHttpErr(value = { 1, 2, 3 })
	public void resultErr(ResponseEntity r) {
		switch (r.getKey()) {
		// 验证码
		case 1:
			Ioc.getIoc().getLogger().i("获取验证码失败,请检查网络");
			Toast.makeText(this, "获取验证码失败，请检查网络", Toast.LENGTH_SHORT).show();
			break;
		// 绑定
		case 2:
			Ioc.getIoc().getLogger().i("验证码验证失败,请检查网络");
			Toast.makeText(this, "验证码验证失败，请检查网络", Toast.LENGTH_SHORT).show();
			// cancelDialog();
			break;

		default:
			break;
		}
	}

	/**
	 * 封装验证码参数数据
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, String> setCodeParams() {

		findMap = new HashMap<String, String>();
		findMap.put("account", find_account.getText().toString().trim());
		findMap.put("logicFlag", "2");
		findMap.put("uuid", uuid);

		return findMap;
	}

	/**
	 * 验证码验证参数数据
	 * 
	 * @param map
	 *            请求验证码接口参数map
	 * @return
	 */
	public Map<String, String> checkCodeParams() {

		findMap = new HashMap<String, String>();
		findMap.put("code", find_security_code.getText().toString().trim());
		findMap.put("uuid", uuid);

		return findMap;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@InjectMethod(@InjectListener(ids = { R.id.find_account, R.id.find_security_code }, listeners = { OnTextChanged.class }))
	public void change(CharSequence s, int start, int before, int count) {

		Editable name = find_account.getText();
		Editable pwd = find_security_code.getText();
		if (!Handler_String.isEmpty(name + "")
				&& !Handler_String.isEmpty(pwd + "")) {
			find_next_button.setEnabled(true);
		} else {
			find_next_button.setEnabled(false);
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
	public void onValidationFailed(final View failedView, Rule<?> failedRule) {
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

		Ioc.getIoc().getLogger()
				.i("调用后台验证码接口：[" + Url.METHOD_CHECKCAPTCHA + "]");
		config.setKey(2);
		// 设置参数
		this.checkCodeParams();
		// showDialog();
		// 调用后台验证码
		FastHttpHander.ajaxString(Url.METHOD_CHECKCAPTCHA,
				Handler_Json.beanToJson(findMap), config, this);

	}

}
