package com.jdjt.mangrovepay.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
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
import com.android.pc.ioc.verification.annotation.Required;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnTextChanged;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_String;
import com.android.pc.util.Handler_TextStyle;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.common.BaseFragmentActivity;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.ResultParse;

/*************************************************
@copyright:bupt
@author: 王明雨
@date:2015-1-13
@Description:TODO (绑定红树林卡功能)
**************************************************/
@InjectLayer(value = R.layout.activity_bindcard_add, parent = R.id.center_common, isTitle = true)
public class BindCardActivity extends BaseFragmentActivity implements
		ValidationListener {
	@Required(message = "请输入卡号", trim = true, order = 1)
	@InjectView(R.id.bind_cardNo)
	EditText bind_cardNo;
	@Required(message = "请输入卡号", trim = true, order = 2)
	@InjectView(R.id.bind_cardpwd)
	EditText bind_cardpwd;
	@InjectView(R.id.btn_bindcard)
	Button btn_bindcard;
	// 验证框架
	Validator validator;
	 //InternetConfig config;
	@InjectInit
	public void init() {
		textview_title.setText("");
		img_back.setBackgroundResource(R.drawable.close_white);
	}

	/*************************************************
	 @Title: click 
	 @Description: TODO(点击按钮验证输入信息) 
	 @param view    设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-1-13
	*************************************************/
	@InjectMethod(@InjectListener(ids = { R.id.btn_bindcard }, listeners = { OnClick.class }))
	public void click(View view) {
		// 验证
		validator = new Validator(this);
		validator.setValidationListener(this);
		validator.validate();
	}

	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule) {
		// TODO Auto-generated method stub
		String message = failedRule.getFailureMessage();
		CommonUtils.onAlertToast(failedView, message, this);
	}

	/*************************************************
	 @Title: onValidationSucceeded 
	 @Description: TODO(验证成功 调用后台绑定接口)     设定文件 
	 @throws 
	 @date  2015-1-13
	 @see com.android.pc.ioc.verification.Validator.ValidationListener#onValidationSucceeded()
	*************************************************/
	@Override
	public void onValidationSucceeded() {
		showDialog();
		// TODO Auto-generated method stub
		Ioc.getIoc().getLogger()
				.i("调用后台红树林卡接口：[" + Url.METHOD_CARDBINDING + "]");
		//InternetConfig internetConfig = new InternetConfig();
		config=new InternetConfig();
		config.setKey(1);
		config.setHead(CommonUtils.inHeaders());
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("cardNo", bind_cardNo.getText().toString().trim());
		map.put("password", bind_cardpwd.getText().toString().trim());
		// 调用后台卡绑定信息接口
		FastHttpHander.ajaxString(Url.METHOD_CARDBINDING,
				Handler_Json.beanToJson(map), config, this);

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
	@InjectMethod(@InjectListener(ids = { R.id.bind_cardNo, R.id.bind_cardpwd }, listeners = { OnTextChanged.class }))
	public void change(CharSequence s, int start, int before, int count) {

		Editable name = bind_cardNo.getText();
		Editable pwd = bind_cardpwd.getText();
		Ioc.getIoc().getLogger().i("获取登录 信息:" + name);
		Ioc.getIoc().getLogger().i("获取密码 信息:" + pwd);
		if (!Handler_String.isEmpty(name + "")
				&& !Handler_String.isEmpty(pwd + "")) {
			btn_bindcard.setEnabled(true);
		} else {
			btn_bindcard.setEnabled(false);
		}
	}


	/*************************************************
	@Function       resultOk
	@Description    红树林卡绑定结果
	@Input          r：返回结果实体类
	@Return         无返回值
	*************************************************/
	@InjectHttpOk(1)
	public void resultOk(ResponseEntity r) {
		Ioc.getIoc().getLogger().i("红树林卡绑定接口状态返回结果【"+r+"】");
		//返回结果格式化处理后OK进行数据处理
		if(ResultParse.isResultOK(r,this)){
			Intent it = new Intent();  
            it.putExtra("STEPRESULT", 0);  
            setResult(Activity.RESULT_OK, it);  
			finish();
//			config.setKey(2);
//			Map<String, String> cardmap = new HashMap<String, String>();
//			cardmap.put("cardKind", "2");
//			//会员红树林列表信息
//			FastHttpHander.ajaxString(Url.METHOD_CARDARRAY,Handler_Json.beanToJson(cardmap),config,this);
			//finish();//关闭当前页返回上一页

		}else{
			Toast.makeText(this, "卡号或密码不正确", Toast.LENGTH_SHORT).show();
			dismissDialog();
		}
	}
//	
//	/*************************************************
//	@Function       cardOk
//	@Description    红树林卡列表（会员）响应结果
//	@Input          r：返回结果实体类
//	@Return         无返回值
//	*************************************************/
//	@InjectHttpOk(2)
//	public void cardOk(ResponseEntity r) {
//		Ioc.getIoc().getLogger().i("红树林卡列表(会员)成功返回结果【"+r+"】");
//		dismissDialog();
//		//获取成功 返回OK
//		if(ResultParse.isResultOK(r, this)){
//			//装载控件
//			//startActivity(new Intent(this, HotelCardListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("cardArrayList", r.getContentAsString()));
//			 Intent it = new Intent();  
//             it.putExtra("STEPRESULT", 0);  
//             setResult(Activity.RESULT_OK, it);  
//			finish();
//			
//		}else{
//			Toast.makeText(this, "卡号或密码不正确", Toast.LENGTH_SHORT).show();
//		}
//	}
	/*************************************************
	@Function       resultErr
	@Description    请求失败处理结果
	@Input          r：返回结果实体类
	@Return         无返回值
	*************************************************/
	@InjectHttpErr(value = { 1,2})
	public void resultErr(ResponseEntity r) {
		dismissDialog();
		switch (r.getKey()) {
		//获取红树林卡绑定
		case 1:
			Ioc.getIoc().getLogger().i("红树林卡绑定失败,请检查网络连接或接口");
			Toast.makeText(this, "红树林卡绑定失败,请检查网络或接口", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Ioc.getIoc().getLogger().i("会员红树林卡获取失败,请检查接口");
			Toast.makeText(this, "会员红树林卡获取失败,请检查接口", Toast.LENGTH_SHORT).show();
			break;	
		}
		
	}
}
