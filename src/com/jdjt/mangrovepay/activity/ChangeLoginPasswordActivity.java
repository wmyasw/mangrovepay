package com.jdjt.mangrovepay.activity;

import java.util.Map;

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
import com.android.pc.ioc.verification.Validator;
import com.android.pc.ioc.verification.Validator.ValidationListener;
import com.android.pc.ioc.verification.annotation.ConfirmPassword;
import com.android.pc.ioc.verification.annotation.Password;
import com.android.pc.ioc.verification.annotation.Regex;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnTextChanged;
import com.android.pc.util.Handler_Network;
import com.android.pc.util.Handler_String;
import com.android.pc.util.Handler_TextStyle;
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

@InjectLayer(value = R.layout.activity_change_login_password, parent = R.id.center_common, isTitle = true)
public class ChangeLoginPasswordActivity extends BaseFragmentActivity implements
		ValidationListener {
	@Regex(message = "请输入长度6-18位由字母数字_和-组成的密码", trim = true, pattern = "[a-zA-Z0-9_-]{6,18}", order = 6)
	@InjectView(R.id.ed_login_password)
	EditText ed_login_password;
	@Password(maxLength = 18, minLength = 6, message = "请输入长度6-18位由字母数字_和-组成的密码",pattern = "[a-zA-Z0-9_-]{6,18}", order = 2)
	@InjectView(R.id.new_password)
	EditText new_password;
	@ConfirmPassword(message = "两次输入的密码不一致，请重新输入", order = 3)
	@InjectView(R.id.new_password_re)
	EditText new_password_re;

	@InjectView(R.id.btn_set_login_password)
	Button btn_set_login_password;
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
		textview_title.setText("修改登录密码");
		// 获取验证码60秒钟内的uuid,如果有则取,如果没有则重新生成
		if (MapVo.get("register_tel_valitation_uuid") != null) {
			uuid = MapVo.get("register_tel_valitation_uuid").toString();
		} else {
			uuid = Uuid.getUuid();// 给初始值
		}

	}

	@InjectMethod(@InjectListener(ids = { R.id.btn_set_login_password }, listeners = { OnClick.class }))
	public void checkClick(View view) {
		switch (view.getId()) {
		case R.id.btn_set_login_password:

			// 验证
			validator = new Validator(this);
			validator.setValidationListener(this);
			validator.validate();

			break;
		default:
			break;
		}
	}

	@InjectMethod(@InjectListener(ids = { R.id.ed_login_password,
			R.id.new_password, R.id.new_password_re }, listeners = { OnTextChanged.class }))
	public void change(CharSequence s, int start, int before, int count) {

		Editable pwd = ed_login_password.getText();
		Editable newpwd = new_password.getText();
		Editable rpwd = new_password_re.getText();
		if (!Handler_String.isEmpty(pwd + "")
				&& !Handler_String.isEmpty(newpwd + "")
				&& !Handler_String.isEmpty(rpwd + "")) {
			btn_set_login_password.setEnabled(true);
		} else {
			btn_set_login_password.setEnabled(false);
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
		// 验证手机是否联网
		if (!Handler_Network.isNetworkAvailable(getApplicationContext())) {
			Toast.makeText(getApplicationContext(),
					ErrorMsgEnum.NET_NOT_CONNECT.getName(), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		InternetConfig config = new InternetConfig();
		config.setKey(1);
		config.setHttps(true);
		config.setHead(CommonUtils.inHeaders());
		JsonObject json = new JsonObject();
		json.addProperty("password", ed_login_password.getText() + "");
		json.addProperty("newPassword", new_password.getText() + "");
		// ((LoginAndRegisterFragmentgetApplicationContext())getApplicationContext()).dialog.show();
		// 调用后台注册接口
		FastHttpHander.ajaxString(Url.METHOD_MODIFYPASSWORD, json.toString(),
				config, this);

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

		// 返回OK
		if (ResultParse.isResultOK(r, this)) {
			Toast.makeText(getApplicationContext(), "修改登录密码成功", Toast.LENGTH_SHORT)
					.show();
			MangrovePayApp.app.setData("password",  new_password.getText() + "");
			finish();
		} else {
			Toast.makeText(getApplicationContext(), "修改登录密码失败，请检查原密码是否正确",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 请求数据失败处理
	 * 
	 * @function resultErr
	 * @description 请求数据失败处理
	 * @param r
	 */
	@InjectHttpErr(value = { 1 })
	private void resultErr(ResponseEntity r) {
		dismissDialog();
		Toast.makeText(getApplicationContext(), "修改登录密码失败，请检查网络",
				Toast.LENGTH_SHORT).show();
		// ((LoginAndRegisterFragmentActivity)activity).dialog.cancel();
	}
}
