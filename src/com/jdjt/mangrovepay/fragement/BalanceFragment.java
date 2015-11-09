package com.jdjt.mangrovepay.fragement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.android.pc.ioc.internet.FastHttp;
import com.android.pc.ioc.internet.FastHttpHander;
import com.android.pc.ioc.internet.InternetConfig;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.util.Handler_Inject;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_String;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.activity.BalanceActivity;
import com.jdjt.mangrovepay.activity.ConsumptionListActivity;
import com.jdjt.mangrovepay.adpater.HotelBalanceAdapter;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.common.BaseFragment;
import com.jdjt.mangrovepay.contanst.BalanceTypeEnum;
import com.jdjt.mangrovepay.contanst.ErrorMsgEnum;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.ResultParse;
import com.jdjt.mangrovepay.view.ListFooterView;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-9
 * @Description:TODO (账户余额)
 **************************************************/
public class BalanceFragment extends BaseFragment {
	EventBus eventBus = EventBus.getDefault();
	@InjectView(R.id.activity_balance_list)
	public ListView activity_balance_list;

	ArrayList<HashMap<String, String>> mapList = null;
	HotelBalanceAdapter adapter = null;
	String cardNo;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View rootView = inflater.inflate(R.layout.fragement_balance, container,
				false);
		Handler_Inject.injectFragment(this, rootView);
		return rootView;
	}

	@InjectInit
	public void init() {
		mapList = new ArrayList<HashMap<String, String>>();
		/*
		 * HashMap<String, String> map = new HashMap<String, String>();
		 * map.put("hotel_card_positive_tv", "111111111111"); mapList.add(map);
		 * map = new HashMap<String, String>();
		 * map.put("hotel_card_positive_tv", "22222222"); mapList.add(map);
		 */

		getAccountInfo();
	}

	@InjectMethod(@InjectListener(ids = { R.id.activity_balance_list }, listeners = { com.android.pc.ioc.view.listener.OnItemClick.class }))
	public void OnItemClick(AdapterView<?> parent, final View view,
			final int position, long arg3) {

		// TODO Auto-generated method stub
		// v.findViewById(R.id.)

		String cardNo = mapList.get(position).get("cardNo");
		String subType = mapList.get(position).get("subType");
		Intent intent = new Intent();
		intent.putExtra("cardNo", cardNo);
		intent.putExtra("subType", subType);
		intent.setClass(activity, ConsumptionListActivity.class);
		startActivity(intent);

	}

	public void getAccountInfo() {
		showDialog();
		String url = Url.MEM_INFO_ARRAY;
		// String
		// url="http://192.168.10.132:8000/mymhotel-uum-web/mem/account/member_account_info.json";
		InternetConfig config = new InternetConfig();
		config.setHead(CommonUtils.inHeaders());
		config.setKey(1);
		JsonObject json = new JsonObject();
		json.addProperty("account", MangrovePayApp.app.getData("account") + "");
		json.addProperty("subType", "");
		json.addProperty("includeSubAcc", "1");
		FastHttpHander.ajaxString(url, json.toString(), config, this);
	}

	@InjectHttpOk(1)
	public void result(ResponseEntity r) {
		dismissDialog();
		switch (r.getStatus()) {
		case FastHttp.result_ok:
			Ioc.getIoc().getLogger().i(r.getContentAsString());

			if (!ResultParse.isResultOK(r, activity)) {
				ListFooterView view = new ListFooterView(activity);
				((TextView) view.getView().findViewById(R.id.txt_content))
						.setText("开启账户余额");
				view.getView().setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(activity, BalanceActivity.class);
						startActivityForResult(intent, 0);
					}
				});

				activity_balance_list
						.addFooterView(view.getView(), null, false);
				setAdapter();
			} else {
				ArrayList<HashMap<String, String>> list = Handler_Json
						.jsonToList("subList", r.getContentAsString());
				
				cardNo=(String) Handler_Json.getValue("virtualAccountNo", r.getContentAsString());
				if (null != list && list.size() > 0) {
					// adapter.notifyDataSetChanged();
					setData(list);
				} else {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("hotel_card_positive_tv", "");
					map.put("tx_balance", getString(R.string._amount, "0.00"));
					map.put("subType", "12");
					map.put("cardNo", cardNo);
					mapList.add(map);
					setAdapter();
				}
			}
			break;
		case FastHttp.result_net_err:
			Toast.makeText(activity, ErrorMsgEnum.NET_ERROR.getName(),
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	private void setData(ArrayList<HashMap<String, String>> list) {
		for (HashMap<String, String> m : list) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("hotel_card_positive_tv", m.get("subTypeName"));
			map.put("tx_balance",
					getString(R.string._amount, CommonUtils.fomatFloat2(Float.valueOf(m.get("subMoney")))));
			map.put("hotel_card_image",
					BalanceTypeEnum.getCardTypeEnum(m.get("subType"))
							.getLayout_id() + "");
			map.put("subType", m.get("subType"));
			map.put("cardNo",cardNo);
			
			mapList.add(map);
		}
		setAdapter();
	}

	@InjectHttpErr
	public void resultError(ResponseEntity r) {
		dismissDialog();
		Toast.makeText(activity, ErrorMsgEnum.NET_ERROR.getName(),
				Toast.LENGTH_SHORT).show();
	}

	public void setAdapter() {
		adapter = new HotelBalanceAdapter(activity_balance_list, mapList,
				R.layout.mem_balance_list_item);
		activity_balance_list.setAdapter(adapter);
	}

}
