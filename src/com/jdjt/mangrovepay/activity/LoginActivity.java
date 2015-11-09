package com.jdjt.mangrovepay.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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
import com.android.pc.ioc.verification.annotation.Password;
import com.android.pc.ioc.verification.annotation.Telphone;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnTextChanged;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_Network;
import com.android.pc.util.Handler_String;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.common.BaseFragmentActivity;
import com.jdjt.mangrovepay.contanst.ErrorMsgEnum;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.ResultParse;
import com.jdjt.mangrovepay.view.ClearEditText;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-6
 * @Description:TODO (进入登录首页)
 **************************************************/
@InjectLayer(R.layout.activity_login)
public class LoginActivity extends BaseFragmentActivity implements
		ValidationListener {

	@InjectView(R.id.tx_register_link)
	public TextView tx_register_link;
	@Telphone(empty = false, message = "请输入正确的手机号", order = 1)
	@InjectView(R.id.login_name)
	public ClearEditText login_name;
	@Password(maxLength = 18, minLength = 6, message = "请输入长度6-18位由字母数字_和-组成的密码", order = 2)
	@InjectView(R.id.login_pwd)
	public ClearEditText login_pwd;
	@InjectView(R.id.login_button)
	Button login_button;

	// 验证
	Validator validator;
	Map<String, String> loginMap;
	/*************************************************
	 @Title: init 
	 @Description: TODO(初始化完成后加载信息)     设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-1-8
	*************************************************/
	@InjectInit
	public void init() {
		Toast.makeText(getApplicationContext(), "登录页面", 
                Toast.LENGTH_SHORT).show(); 
		String account = MangrovePayApp.app.getData("account");
		String password = MangrovePayApp.app.getData("password");
		login_name.setText(account);
		login_pwd.setText(password);
	}

	@InjectMethod(@InjectListener(ids = { R.id.tx_register_link,
			R.id.tx_pwd_link, R.id.login_button }, listeners = { OnClick.class }))
	public void click(View view) {
		switch (view.getId()) {
		case R.id.tx_register_link:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		case R.id.tx_pwd_link:
			startActivity(new Intent(LoginActivity.this, FindPasswordActivity.class));
			break;
		// 登录
		case R.id.login_button:
			// 验证
			validator = new Validator(this);
			validator.setValidationListener(this);
			validator.validate();

			break;
		default:
			break;
		}
	}
	private long clickTime = 0; //记录第一次点击的时间 
   
    @Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK) { 
            exit(); 
            return true; 
        } 
        return super.onKeyDown(keyCode, event); 
    } 
   
    private void exit() { 
        if ((System.currentTimeMillis() - clickTime) > 2000) { 
            Toast.makeText(getApplicationContext(), "再按一次后退键退出程序", 
                    Toast.LENGTH_SHORT).show(); 
            clickTime = System.currentTimeMillis(); 
        } else { 
            this.finish(); 
//          System.exit(0); 
        } 
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
	@InjectMethod(@InjectListener(ids = { R.id.login_name, R.id.login_pwd }, listeners = { OnTextChanged.class }))
	public void change(CharSequence s, int start, int before, int count) {

		Editable name = login_name.getText();
		Editable pwd = login_pwd.getText();
		if (!Handler_String.isEmpty(name + "")
				&& !Handler_String.isEmpty(pwd + "")) {
			login_button.setEnabled(true);
		} else {
			login_button.setEnabled(false);
		}
	}

	/**
	 * 自动验证成功的方法
	 */
	@Override
	public void onValidationSucceeded() {
		showDialog("登录中...");
		Ioc.getIoc().getLogger().i("登录前端验证成功");
		// Toast.makeText(this, "前端验证通过", Toast.LENGTH_SHORT).show();

		loginMap = new HashMap<String, String>();
		loginMap.put("account", login_name.getText().toString().trim());
		loginMap.put("password", login_pwd.getText().toString().trim());

		// 验证手机是否联网
		if (!Handler_Network.isNetworkAvailable(this)) {
			Toast.makeText(this, ErrorMsgEnum.NET_ERROR.getName(),
					Toast.LENGTH_SHORT).show();
			return;
		}
		config=new InternetConfig();
		config.setKey(R.raw.client);
		config.setHead(CommonUtils.inHeaders());
		Ioc.getIoc().getLogger().i("调用后台登录接口：[" + Url.METHOD_LOGIN + "]");
		// 调用后台登录接口
		FastHttpHander.ajaxString(Url.METHOD_LOGIN,
				Handler_Json.beanToJson(loginMap), config, this);

	}

	/*************************************************
	 @Title: success 
	 @Description: TODO(登录返回) 
	 @param entity    设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-1-8
	*************************************************/
	@InjectHttpOk
	public void success(ResponseEntity entity) {
		Ioc.getIoc().getLogger().i("登录接口状态返回结果【" + entity.toString() + "】");

		// 获取头部信息
		Map<String, String> map = entity.getHeaders();

		// 登录成功 返回OK
		if (ResultParse.isResultOK(entity, this)) {
			// 获取返回内容 ticket
			String jsonString = entity.getContentAsString();

			Ioc.getIoc().getLogger().i("ticket数据【" + jsonString + "】");

			// 存放ticket
			MapVo.map = Handler_Json.JsonToCollection(jsonString);// 设置ticket
																	// TODO
			MangrovePayApp.ticket=(String) MapVo.get("ticket");
			// 存储用户名密码到本地
			MangrovePayApp.app.setData("account",
					loginMap.get("account"));
			MangrovePayApp.app.setData("password",
					loginMap.get("password"));
			Ioc.getIoc().getLogger().i("登录成功");

			Intent intent=new Intent();
			intent.setClass(this, MainActivity.class);
			startActivity(intent);
			//startActivity(new Intent().setClass(this, CaptureActivity.class));
			this.finish();
		}
		// 登录失败
		else {
			Ioc.getIoc().getLogger().i("登录失败");
			Toast.makeText(this, "登录失败，请检查用户名密码是否正确",
					Toast.LENGTH_SHORT).show();
			dismissDialog();
		}

	}

	@InjectHttpErr
	public void fail(ResponseEntity entity) {
		dismissDialog();
		Ioc.getIoc().getLogger().i("登录失败,请检查网络");
		Toast.makeText(this, ErrorMsgEnum.NET_ERROR.getName(),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dismissDialog();
	}

	/**
	 * 自动验证失败的方法
	 */
	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule) {
		String message = failedRule.getFailureMessage();
		CommonUtils.onAlertToast(failedView, message, this);
	};
}
