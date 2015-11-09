package com.jdjt.mangrovepay.adpater;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.pc.ioc.adapter.CommonLazyAdapter;
import com.android.pc.ioc.inject.InjectView;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.adpater.HotelCardAdapter.ViewHolder;
import com.jdjt.mangrovepay.common.BaseViewHolder;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-14
 * @Description:TODO (红树林卡列表适配器)
 **************************************************/
public class HotelCardAdapter extends
		CommonLazyAdapter<HashMap<String, String>, ViewHolder> {
	private ListView listView;
	private ArrayList<HashMap<String, String>> dataList;
	private Button btn;
	private OnClickListener click;
	public HotelCardAdapter(AbsListView listView,
			ArrayList<HashMap<String, String>> dataList, int layout_id,
			OnClickListener click,Button btn) {
		super(listView, dataList, layout_id, 0);
		this.listView = (ListView) listView;
		this.dataList = dataList;
		this.click = click;
		this.btn=btn;
		// TODO Auto-generated constructor stub
	}

	public void deal(HashMap<String, String> data, ViewHolder viewHold,
			int position) {
		// TODO Auto-generated method stub
		// super.deal(data, viewHold, position);
		injectAdapter(viewHold, position);
	}

	/**
	 * adapter里面使用 自动去注入组件
	 * 
	 * @author mars 2013-10-22 下午12:59:07
	 * @param object
	 * @param view
	 * @return void
	 */
	private void injectAdapter(Object view, int position) {
		String data;
		Field[] fields = view.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				InjectView v = field.getAnnotation(InjectView.class);
				if (v == null) {
					continue;
				}
				data = getString(position, field.getName());
				if (data == null) {
					continue;
				}
				try {
					field.setAccessible(true);
					Object object = field.get(view);
					if (object == null
							|| !View.class.isAssignableFrom(object.getClass())) {
						continue;
					}
					((View) object).setTag(position);
					if (TextView.class.isAssignableFrom(object.getClass())) {
						((TextView) object).setText(data);
					}
					if (ImageView.class.isAssignableFrom(object.getClass())) {
						if ("img_unbind".equals(field.getName())) {
							if (!(dataList.get(position).get("img_unbind"))
									.equals("1")) {
								((ImageView) object).setVisibility(View.GONE);
							} else {
								((ImageView) object)
										.setVisibility(View.VISIBLE);
								((ImageView) object).setTag(dataList.get(
										position).get("cardNo"));// 如果解绑的话
																	// 给tag赋值
																	// 便于后续操作
								
								((ImageView) object).setOnClickListener(click);
							}
						} else {
							// if((dataList.get(position).get("hotel_card_image")).equals("1"))
							((ImageView) object).setImageResource(Integer
									.parseInt(data.toString()));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public class ViewHolder extends BaseViewHolder{
		/*************************************************
		Copyright:bupt
		
		@author: 王明雨
		@date:2015-3-23:2010-08-25
		@Description:描述主要实现的功能
		**************************************************/
		
		private static final long serialVersionUID = 5168263359069127997L;
		@InjectView(R.id.hotel_card_image)
		public ImageView hotel_card_image;// card背景图
		@InjectView(R.id.img_unbind)
		public ImageView img_unbind; // 解绑红色按钮

		@InjectView(R.id.txt_card_no)
		public TextView txt_card_no; // 卡号
		@InjectView(R.id.tx_balance_v)
		public TextView tx_balance_v; // 余额
		@InjectView(R.id.card_name_v)
		public TextView card_name_v;// 卡名称

	}
}
