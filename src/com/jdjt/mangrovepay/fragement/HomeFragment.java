package com.jdjt.mangrovepay.fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.event.EventBus;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.util.Handler_Inject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.common.BaseFragment;
import com.jdjt.mangrovepay.event.FragmentEventEntity;

/*************************************************
 * @copyright:bupt
 * @author: 王明雨
 * @date:2015-1-9
 * @Description:TODO (首页功能 fragement)
 **************************************************/
public class HomeFragment extends BaseFragment {
	EventBus eventBus = EventBus.getDefault();
	FragmentEventEntity fe=null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		View rootView = inflater.inflate(R.layout.fragement_main, container,
				false);
		Handler_Inject.injectFragment(this, rootView);
		return rootView;
	}

	@InjectInit
	public void init() {
		fe=new FragmentEventEntity();
		fe.setFragment(new HSLCardFragment());
		fe.setLayoutid(R.id.fragement_main);
		String isbalance =activity.getIntent().getStringExtra("balance");
		Ioc.getIoc().getLogger().i("开启余额账户："+isbalance);
		if("ok".equals(isbalance)){
			fe.setFragment(new BalanceFragment());
			RadioGroup rg= (RadioGroup) activity.findViewById(R.id.tabFragmentGroup);
			rg.check(R.id.tab_balance);
		}
		eventBus.post(fe);
	}

	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		eventBus.unregister(this);
	}
	
}
