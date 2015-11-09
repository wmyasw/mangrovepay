package com.jdjt.mangrovepay.adpater;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pc.ioc.adapter.CommonLazyAdapter;
import com.android.pc.ioc.inject.InjectView;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.adpater.HotelBalanceAdapter.ViewHolder;
import com.jdjt.mangrovepay.common.BaseViewHolder;

/*************************************************
 * @copyright:bupt
 * @author: 李洁
 * @date:2014-11-19
 * @Description:会员中心--红树林卡列表适配器
 **************************************************/
public class HotelBalanceAdapter extends
		CommonLazyAdapter<HashMap<String, String>, ViewHolder> {
	
	public HotelBalanceAdapter(AbsListView listView,
			ArrayList<HashMap<String, String>> dataList, int layout_id) {
		super(listView, dataList, layout_id,0);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void deal(HashMap<String, String> data, ViewHolder viewHold,
			int position) {
		// TODO Auto-generated method stub
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
		
		//获取view的所有申明的字段，即包括public、private和proteced，但是不包括父类的申明字段。 
		Field[] fields = view.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				//getAnnotation()方法是为了获取以@开头的注解的那个对象的注释
				InjectView v = field.getAnnotation(InjectView.class);
				if (v == null) {
					continue;
				}
				data = getString(position, field.getName());
				if (data == null) {
					continue;
				}
				try {
					//4.0 如果要访问私有的方法，所以我们在这里将可访问设置为true，则JVM不会执行访问控制检查
					field.setAccessible(true);
					Object object = field.get(view);
					/*Class.isAssignableFrom()是用来判断一个类Class1和另一个类Class2是否相同或是另一个类的子类或接口。   
						格式为：
				        Class1.isAssignableFrom(Class2)   
				                      调用者和参数都是java.lang.Class类型。   
				                      而instanceof是用来判断一个对象实例是否是一个类或接口的或其子类子接口的实例。  
				                      格式是：o instanceof TypeName     
				                      第一个参数是对象实例名，第二个参数是具体的类名或接口名，例如   String,InputStream。
			        */
					if (object == null
							|| !View.class.isAssignableFrom(object.getClass())) {
						continue;
					}
					((View) object).setTag(position);
					if (TextView.class.isAssignableFrom(object.getClass())) {
						((TextView) object).setText(data);
					}
					if (ImageView.class.isAssignableFrom(object.getClass())) {
						((ImageView) object).setImageResource(Integer
								.parseInt(data.toString()));
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
		
		private static final long serialVersionUID = -5004372093408255279L;
		@InjectView(R.id.hotel_card_positive_tv)
		public TextView hotel_card_positive_tv;
		@InjectView(R.id.tx_balance)
		public TextView tx_balance;
		@InjectView(R.id.hotel_card_image)
		public ImageView hotel_card_image;
		
	}


}
