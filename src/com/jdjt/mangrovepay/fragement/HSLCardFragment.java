package com.jdjt.mangrovepay.fragement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.android.pc.util.Handler_Inject;
import com.android.pc.util.Handler_Json;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.activity.BindCardActivity;
import com.jdjt.mangrovepay.activity.ConsumptionListActivity;
import com.jdjt.mangrovepay.adpater.HotelCardAdapter;
import com.jdjt.mangrovepay.common.BaseFragment;
import com.jdjt.mangrovepay.contanst.CZKCardTypeEnum;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.event.CardListEntity;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.ResultParse;
import com.jdjt.mangrovepay.view.ListFooterView;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-9
 * @Description:TODO (红树林卡)
 **************************************************/
public class HSLCardFragment extends BaseFragment {
	EventBus eventBus = EventBus.getDefault();
	@InjectView(R.id.activity_list)
	public ListView activity_list;
	public ArrayList<HashMap<String, String>> mapList = null; // 红树林卡列表数据
	private HotelCardAdapter adapter = null; // 红树林卡列表 适配器
	private InternetConfig internetconfig;// 公共传输协议key
	private AlertDialog dlg;// 提示信息
	private Button manage;// 卡列表管理按钮
	// 数据列表
	ArrayList<CardListEntity> cardArrayList = new ArrayList<CardListEntity>();
	boolean isUNbind = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View rootView = inflater.inflate(R.layout.fragement_hslcard, container,
				false);
		Handler_Inject.injectFragment(this, rootView);
		return rootView;
	}

	/*************************************************
	 * @Title: init
	 * @Description: TODO(页面初始化后执行) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-12
	 *************************************************/
	@InjectInit
	public void init() {

		mapList = new ArrayList<HashMap<String, String>>();
		internetconfig = new InternetConfig();
		internetconfig.setHead(CommonUtils.inHeaders());
		ListFooterView view = new ListFooterView(activity);
		/*
		 * activity_main_list.addHeaderView(view.getView());
		 * activity_main_list.addFooterView(view.getView());
		 */
		view.getView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(activity, BindCardActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		activity_list.addFooterView(view.getView(), null, false);

		manage = (Button) activity.findViewById(R.id.btn_manage);
		manage.setText("管理");
		manage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!isUNbind) {
					isUNbind = true;
					manage.setText("完成");
				} else {
					isUNbind = false;
					manage.setText("管理");
				}
				mapList.addAll(getMapList(mapList, isUNbind));
				adapter.notifyDataSetChanged();
			}
		});
		ajaxCardPost();
		setAdapter();
	}
	
	//点击解绑确定按钮
	public OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final String cardNo = v.getTag() + "";
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			dlg = new AlertDialog.Builder(activity).create();
			Window window = dlg.getWindow();
			dlg.show();
			// 设置窗口的内容页面,activity_alert.xml文件中定义view内容
			window.setContentView(R.layout.activity_alert);
			Button cancle_btn = (Button) window
					.findViewById(R.id.hotelcard_cancle_banding_btn);
			cancle_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dlg.cancel();
				}
			});

			Button ok = (Button) window
					.findViewById(R.id.hotelcard_submit_banding_btn);
			ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					unbindAjax(cardNo);
					dlg.cancel();
				}
			});
		}
	};

	/*************************************************
	 * @Title: OnItemClick
	 * @Description: TODO(item 点击事件)
	 * @param parent
	 * @param view
	 * @param position
	 * @param arg3
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-14
	 *************************************************/
	@InjectMethod(@InjectListener(ids = { R.id.activity_list }, listeners = { com.android.pc.ioc.view.listener.OnItemClick.class }))
	public void OnItemClick(AdapterView<?> parent, final View view,
			final int position, long arg3) {

		 ImageView im = (ImageView) view.findViewById(R.id.img_unbind);
		// TODO Auto-generated method stub
		// v.findViewById(R.id.)

		if (im.getVisibility() == 0) {
			if (!isUNbind) {
				isUNbind = true;
				manage.setText("完成");
			} else {
				isUNbind = false;
				manage.setText("管理");
			}
			mapList.addAll(getMapList(mapList, isUNbind));
			adapter.notifyDataSetChanged();
		} else {
			String cardNo = mapList.get(position).get("cardNo");
			String subType = mapList.get(position).get("subType");
			Intent intent = new Intent();
			intent.putExtra("cardNo", cardNo);
			intent.putExtra("subType", subType);
			intent.setClass(activity, ConsumptionListActivity.class);
			startActivity(intent);
		}

	}

	/*************************************************
	 * @Title: unbindAjax
	 * @Description: TODO(解除绑定)
	 * @param cardNo
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-14
	 *************************************************/
	public void unbindAjax(String cardNo) {
		showDialog();
		Map<String, String> map = new HashMap<String, String>();
		map.put("cardNo", cardNo);
		internetconfig.setKey(3);
		// 调用后台个人信息接口
		FastHttpHander.ajaxString(Url.METHOD_CARDUNBINDING,
				Handler_Json.beanToJson(map), internetconfig, this);
	}

	/**
	 * @Function ajaxCardPost
	 * @Description 请求会员卡列表接口
	 * @Input 无
	 * @Return void 无返回值
	 */
	private void ajaxCardPost() {
		Ioc.getIoc().getLogger().i("调用后台红树林卡接口：[" + Url.METHOD_CARDARRAY + "]");
		showDialog();
		internetconfig.setKey(4);
		
		Map<String, String> cardmap = new HashMap<String, String>();
		cardmap.put("cardKind", "2");
		// 会员红树林列表信息
		FastHttpHander.ajaxString(Url.METHOD_CARDARRAY,
				Handler_Json.beanToJson(cardmap), internetconfig, this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(null!=data){
			ajaxCardPost();
		}
	}

	/**
	 * @Function resultOk
	 * @Description 红树林卡解绑响应结果
	 * @Input r：返回结果封装实体类
	 * @Return void 无返回值
	 */
	@InjectHttpOk(3)
	public void resultOk(ResponseEntity r) {
		Ioc.getIoc().getLogger().i("红树林卡解绑接口状态返回结果【" + r + "】");
		// 返回结果格式化处理后OK进行数据处理
		if (ResultParse.isResultOK(r, activity)) {
			ajaxCardPost();
		} else {
			dismissDialog();
		}
	}

	/**
	 * @Function refreshListOk
	 * @Description 红树林卡列表刷新结果
	 * @Input r：返回结果封装实体类
	 * @Return void 无返回值
	 */
	@InjectHttpOk(4)
	public void refreshListOk(ResponseEntity r) {
		Ioc.getIoc().getLogger().i("红树林卡列表接口状态返回结果【" + r + "】");
		// 返回结果格式化处理后OK进行数据处理
		if (ResultParse.isResultOK(r, activity)) {
			cardArrayList.clear();
			mapList.clear();
			try {
				ArrayList<HashMap<String, String>> list = Handler_Json
						.jsonToList("list", r.getContentAsString());
				if (null != list) {
					mapList.addAll(this.getMapList(list, false));
					manage.setVisibility(View.VISIBLE);
					Ioc.getIoc().getLogger().i("装载map数据");
				}else{
					manage.setText("管理");
					manage.setVisibility(View.GONE);
					isUNbind = false;
				
				}
				adapter.notifyDataSetChanged();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				dismissDialog();
			}

			// 装载适配器数据
			// setAdapter();
		}
		dismissDialog();
	}

	/**
	 * @Function resultErr
	 * @Description 请求结果失败处理
	 * @Input r：返回结果封装实体类
	 * @Return void 无返回值
	 */
	@InjectHttpErr(value = { 3, 4 })
	public void resultErr(ResponseEntity r) {
		dismissDialog();
		switch (r.getKey()) {
		// 红树林卡解绑
		case 3:
			Ioc.getIoc().getLogger().i("红树林卡解绑失败,请检查网络连接或接口");
			Toast.makeText(activity, "红树林卡解绑失败,请检查网络或接口", Toast.LENGTH_SHORT)
					.show();
			break;
		case 4:
			Ioc.getIoc().getLogger().i("红树林卡列表获取失败,请检查接口");
			Toast.makeText(activity, "红树林卡列表获取失败,请检查接口", Toast.LENGTH_SHORT)
					.show();
			break;
		}

	}

	/**
	 * @Function setAdapter
	 * @Description 设置适配器
	 * @Input 无
	 * @Return 无返回值
	 */
	public void setAdapter() {
		adapter = new HotelCardAdapter(activity_list, mapList,
				R.layout.mem_hotelcard_list_item, click, manage);
		activity_list.setAdapter(adapter);
	}

	/**
	 * @Function getCardArray
	 * @Description 格式化红树林卡列表信息数据
	 * @Input jsonString：卡列表信息json形式
	 * @Return ArrayList<CardListEntity> 返回卡列表信息数据
	 */
	public ArrayList<CardListEntity> getCardArray(String jsonString) {

		return Handler_Json
				.jsonToBean(CardListEntity.class, "list", jsonString);
	}

	/**
	 * 格式化菜单列表,封装到适配器中
	 * 
	 * @param caterOrderList
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getMapList(
			ArrayList<HashMap<String, String>> cardArrayList, boolean isunbind) {
		ArrayList<HashMap<String, String>> datalist = new ArrayList<HashMap<String, String>>();
		for (HashMap<String, String> cardEntity : cardArrayList) {
			HashMap<String, String> map = new HashMap<String, String>();
			// map.put("", cardEntity.get("cardKind"));
			// map.put("", cardEntity.get("cardType"));
			// map.put("", cardEntity.get("cardTypeName"));
			// map.put("", cardEntity.get("idx"));
			// map.put("", cardEntity.get("outerIdx"));
			// map.put("", cardEntity.get("effectiveDate"));
			// map.put("", cardEntity.get("itemName"));
			// map.put("", cardEntity.get("hotelCode"));
			cardEntity.put("card_name_v", cardEntity.get("subTypeName"));
			String cardno = cardEntity.get("cardNo").substring(
					cardEntity.get("cardNo").length() - 6);
			cardEntity.put("txt_card_no", cardno);
			cardEntity.put("tx_balance_v", cardEntity.get("amount"));
			if (isunbind) {
				cardEntity.put("img_unbind", "1");
			} else {
				cardEntity.put("img_unbind", "2");
			}
			if (null != CZKCardTypeEnum.getCardTypeEnum(cardEntity
					.get("cardNo").substring(0, 4))) {
				cardEntity.put(
						"hotel_card_image",
						CZKCardTypeEnum.getCardTypeEnum(
								cardEntity.get("cardNo").substring(0, 4))
								.getName()
								+ "");
			}
			datalist.add(cardEntity);
		}
		mapList.clear();
		return datalist;
	}

}
