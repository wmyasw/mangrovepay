package com.jdjt.mangrovepay.fragement;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectHttpErr;
import com.android.pc.ioc.inject.InjectHttpOk;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectListener;
import com.android.pc.ioc.inject.InjectMethod;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.internet.FastHttpHander;
import com.android.pc.ioc.internet.InternetConfig;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.ioc.verification.Rule;
import com.android.pc.ioc.verification.Rules;
import com.android.pc.ioc.verification.Validator;
import com.android.pc.ioc.verification.Validator.ValidationListener;
import com.android.pc.ioc.verification.annotation.NumberRule;
import com.android.pc.ioc.verification.annotation.Required;
import com.android.pc.ioc.verification.annotation.Telphone;
import com.android.pc.ioc.verification.annotation.NumberRule.NumberType;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.ioc.view.listener.OnTextChanged;
import com.android.pc.util.Handler_Inject;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_String;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.activity.PaySuccessActivity;
import com.jdjt.mangrovepay.activity.TransferChannelAcitivity;
import com.jdjt.mangrovepay.adpater.HotelBalanceAdapter;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.common.BaseFragment;
import com.jdjt.mangrovepay.contanst.ErrorMsgEnum;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.ResultParse;
import com.tencent.mm.a.a;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-9
 * @Description:TODO (转账)
 **************************************************/
public class TransferFragment extends BaseFragment implements ValidationListener {
	EventBus eventBus = EventBus.getDefault();

	ArrayList<HashMap<String, String>> mapList = null;
	HotelBalanceAdapter adapter = null;
	@Telphone(empty = false, message = "请输入正确的手机号", order = 1)
	@InjectView(R.id.tx_in_transfer_account)
	public EditText tx_in_transfer_account;//账户信息
	@NumberRule(message="转账金额最小为0.01", order = 2, type = NumberType.FLOAT,gt=0.01)
	@InjectView(R.id.tx_transfer_amount)
	public EditText tx_transfer_amount; //转账金额
	@InjectView(R.id.btn_transfer_next)
	public Button btn_transfer_next;
	@InjectView(R.id.tx_account_v)
	public TextView tx_account_v; //显示的账户信息 增加实名信息
	@InjectView(R.id.tx_out_account_name)
	public TextView tx_out_account_name;
	
	// 验证
	Validator validator;
	private AlertDialog dlg;
	private String passwrod;
	private String cardNo;
	private String transfer_account;
	private String subType;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View rootView = inflater.inflate(R.layout.fragement_transfer,
				container, false);
		Handler_Inject.injectFragment(this, rootView);
		return rootView;
	}

	@InjectInit
	public void init() {

		// getAccountInfo();
		tx_in_transfer_account
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (!hasFocus) {
							transfer_account=tx_in_transfer_account.getText().toString().trim();
							if(!Handler_String.isBlank(transfer_account)&&transfer_account.matches(Rules.REGEX_TELPHONE)){
								if(!transfer_account.equals(MangrovePayApp.app.getData("account"))){
									checkAccount(transfer_account);
								}else{
									Toast.makeText(activity, "不能给自己进行转账", Toast.LENGTH_SHORT).show();
								}
							}else{
								//CommonUtils.onErrorToast(tx_in_transfer_account, "请输入正确的帐号", activity);
								Toast.makeText(activity, "请输入正确的帐号", Toast.LENGTH_SHORT).show();
							}
						}

					}
				});
		tx_transfer_amount.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				 if (s.toString().contains(".")) {
	                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
	                        s = s.toString().subSequence(0,
	                                s.toString().indexOf(".") + 3);
	                        tx_transfer_amount.setText(s);
	                        tx_transfer_amount.setSelection(s.length());
	                    }
	                }
	                if (s.toString().trim().substring(0).equals(".")) {
	                    s = "0" + s;
	                    tx_transfer_amount.setText(s);
	                    tx_transfer_amount.setSelection(2);
	                }
	 
	                if (s.toString().startsWith("0")
	                        && s.toString().trim().length() > 1) {
	                    if (!s.toString().substring(1, 2).equals(".")) {
	                    	tx_transfer_amount.setText(s.subSequence(0, 1));
	                    	tx_transfer_amount.setSelection(1);
	                        return;
	                    }
	                }
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		tx_transfer_amount.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					String amount=tx_transfer_amount.getText()+"";
					if(!Handler_String.isBlank(amount)){
						DecimalFormat df = new DecimalFormat("##0.00");  
						tx_transfer_amount.setText(df.format(Double.valueOf(amount)));
					}
				}
			}
		});
	}

	@InjectMethod(@InjectListener(ids = { R.id.rl_transfer_channel,
			R.id.btn_transfer_next,R.id.tx_account_v }, listeners = { OnClick.class }))
	public void click(View v) {
		switch (v.getId()) {
		case R.id.rl_transfer_channel:
			Intent intent = new Intent().setClass(activity,
					TransferChannelAcitivity.class);
			// intent.putExtra("payOrderNo", tx_order_no.getText() + "");
			// System.out.println("选择后的渠道code" + pay_channel_name.getTag());
			intent.putExtra("channelCode", tx_out_account_name.getTag() + "");
			startActivityForResult(intent, 1);
			break;
		case R.id.btn_transfer_next:
			tx_in_transfer_account.clearFocus();
			// 验证

			if(null!=tx_out_account_name.getTag()){
				validator = new Validator(this);
				validator.setValidationListener(this);
				validator.validate();
			}else{
				Toast.makeText(activity, "请选择转出账户", Toast.LENGTH_SHORT).show();
			}
		
			break;
		case R.id.tx_account_v:
			v.setVisibility(View.GONE);
			tx_in_transfer_account.setVisibility(View.VISIBLE);
			tx_in_transfer_account.requestFocus();
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		Ioc.getIoc().getLogger().i("选择渠道返回");
		switch (resultCode) {
		case Activity.RESULT_OK:
			if (null != data && null != data.getExtras()) {
				String channelCode = data.getStringExtra("channelCode");
				String channelName = data.getStringExtra("channelName");
				cardNo = data.getStringExtra("cardNo");
				if (!Handler_String.isBlank(channelName)) {
					tx_out_account_name.setText(channelName);
					tx_out_account_name.setTag(channelCode);
				}
			}
			break;

		default:
			break;
		}
	}

	public void checkAccount(String account) {
		showDialog();
		String url = Url.MEM_INFO_ARRAY;
		JsonObject json = new JsonObject();
		json.addProperty("account", account);
		json.addProperty("subType", "");
		json.addProperty("includeSubAcc", "1");
		InternetConfig config = new InternetConfig();
		config.setKey(2);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxString(url, json.toString(), config, this);
	}

	/*************************************************
	 * @Title: sendTransfer
	 * @Description: TODO(发送转账请求) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-23
	 *************************************************/
	public void sendTransfer() {
		showDialog();
		String url = Url.MEM_SUB_TRANSFER;
		JsonObject json = new JsonObject();
		json.addProperty("money", tx_transfer_amount.getText() + "");
		json.addProperty("subType", tx_out_account_name.getTag() + "");
		json.addProperty("password", passwrod);
		json.addProperty("proceedsPhone", tx_in_transfer_account.getText() + "");
		InternetConfig config = new InternetConfig();
		config.setKey(1);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxString(url, json.toString(), config, this);
	}

	/*************************************************
	 @Title: resultOk 
	 @Description: TODO(请求成功返回) 
	 @param r    返回信息
	 @return void    返回类型 
	 @throws 
	 @date  2015-1-29
	*************************************************/
	@InjectHttpOk(value = { 1, 2 })
	public void resultOk(ResponseEntity r) {
		dismissDialog();
		switch (r.getKey()) {
		case 1:
			if (ResultParse.isResultOK(r, activity)) {
				
				Intent intent=new Intent();
				intent.setClass(activity, PaySuccessActivity.class);
				intent.putExtra("merchantName",tx_account_v.getText()+"");
				intent.putExtra("price", tx_transfer_amount.getText()+"");
				intent.putExtra("title", "转账成功");
				//Toast.makeText(activity, "转账成功", Toast.LENGTH_SHORT).show();
				activity.startActivity(intent);
				activity.finish();
			} 
			break;
		case 2:
			if (ResultParse.isResultOK(r, activity)) {
				HashMap<String,String> map=Handler_Json.JsonToCollection(r.getContentAsString());
				String name="  *"+map.get("realName").substring(1);
				String showname=transfer_account+name;
				tx_in_transfer_account.setVisibility(View.GONE);
				tx_account_v.setText(showname);
				tx_account_v.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(activity, "获取账户信息失败", Toast.LENGTH_SHORT).show();
				tx_in_transfer_account.setText("");
				tx_in_transfer_account.requestFocus();
			}
			break;
		default:
			break;
		}

	}

	@InjectHttpErr(value = { 1, 2 })
	public void resultErr(ResponseEntity r) {
		Toast.makeText(activity, ErrorMsgEnum.NET_ERROR.getName(),
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
	@InjectMethod(@InjectListener(ids = { R.id.tx_in_transfer_account,
			R.id.tx_transfer_amount }, listeners = { OnTextChanged.class }))
	public void change(CharSequence s, int start, int before, int count) {

		Editable account = tx_in_transfer_account.getText();
		Editable amount = tx_transfer_amount.getText();
		if (!Handler_String.isEmpty(account + "")
				&& !Handler_String.isEmpty(amount + "")) {
			btn_transfer_next.setEnabled(true);
		} else {
			btn_transfer_next.setEnabled(false);
		}
	}

	/*************************************************
	 * @Title: alertPayPwd
	 * @Description: TODO(自定义密码输入) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-19
	 *************************************************/
	private void alertPayPwd() {
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.activity_pay_alert, null);
		dlg = new AlertDialog.Builder(activity).create(); // 初始化dialog
		dlg.setView(view);// 重写dialog 模版
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.activity_pay_alert);// 再次设置。是为了修正 在dialog
															// 添加edit 不能调用键盘的bug
		/* 获取自定义按钮 及文本 */
		Button cancle_btn = (Button) window.findViewById(R.id.btn_clear);
		final Button ok = (Button) window.findViewById(R.id.btn_ok);
		EditText pay_pwd = (EditText) window.findViewById(R.id.tx_pay_pwd);
		TextView tx_pay_merchant = (TextView) window
				.findViewById(R.id.tx_pay_merchant);
		TextView tx_alert_title = (TextView) window
				.findViewById(R.id.tx_alert_title);
		TextView tx_pay_amount = (TextView) window
				.findViewById(R.id.tx_pay_amount);
		tx_pay_merchant.setText(tx_account_v.getText());
		tx_pay_amount.setText(getString(R.string._amount,tx_transfer_amount.getText()));
		tx_alert_title.setText("请输入支付密码");

		// 设置文本框输入监听事件 如果不为空那么 确定按钮变为可用状态
		pay_pwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (!Handler_String.isBlank(s.toString())
						&& s.toString().length() >= 6) {
					passwrod = s.toString();
					ok.setEnabled(true);
				}
			}
		});
		// 支付密码 取消按钮
		cancle_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dlg.cancel();
			}
		});
		// 支付密码 确定按钮
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// unbindAjax(cardNo);
				sendTransfer();
				dlg.cancel();
			}
		});
	}

	@Override
	public void onValidationFailed(View arg0, Rule<?> arg1) {
		// TODO Auto-generated method stub
		CommonUtils.onAlertToast(arg0, arg1.getFailureMessage(), activity);
	}

	@Override
	public void onValidationSucceeded() {
		// TODO Auto-generated method stub
		tx_transfer_amount.clearFocus();
		alertPayPwd();
	}
}
