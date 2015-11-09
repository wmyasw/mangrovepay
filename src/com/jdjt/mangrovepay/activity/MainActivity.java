package com.jdjt.mangrovepay.activity;

import java.util.Map;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.event.EventBus;
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
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_String;
import com.google.gson.JsonObject;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.common.BaseFragmentActivity;
import com.jdjt.mangrovepay.contanst.HeaderConst;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.event.FragmentEntity;
import com.jdjt.mangrovepay.event.FragmentEventEntity;
import com.jdjt.mangrovepay.event.MSGCountEntity;
import com.jdjt.mangrovepay.event.MSGEntity;
import com.jdjt.mangrovepay.event.MSGInfoEntity;
import com.jdjt.mangrovepay.event.MainEntity;
import com.jdjt.mangrovepay.event.SlidingToggleEntity;
import com.jdjt.mangrovepay.fragement.BalanceFragment;
import com.jdjt.mangrovepay.fragement.HSLCardFragment;
import com.jdjt.mangrovepay.fragement.HomeFragment;
import com.jdjt.mangrovepay.fragement.LeftFragment;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.view.BadgeView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;

@InjectLayer(value = R.layout.acitivity_main, parent = R.id.center_common, isTitle = true)
public class MainActivity extends BaseFragmentActivity {
	public SlidingMenu menu;
	// ---------------------------------------------------------------------------------------------------
	private static boolean isExit = false; 
	FragmentEventEntity fe = null;
	EventBus eventBus = EventBus.getDefault();
	@InjectView(R.id.main_tab)
	public LinearLayout main_tab;
	String appid;
	CommonUtils cu;
	BadgeView badge;
	@InjectInit
	public void init() {
		
		img_back.setVisibility(View.GONE);
		btn_leftmenu.setVisibility(View.VISIBLE);
		//
		initMenu();
		getMsgCount();
		// ----------------------------------------------------------------------
		// 通知事件
		/*
		 * EventBus eventBus = EventBus.getDefault(); eventBus.register(this);
		 */
		main_tab.setVisibility(View.VISIBLE);
		textview_title.setText("度假宝钱包");
		fe = new FragmentEventEntity();
		fe.setLayoutid(R.id.fragement_main);
		eventBus.register(this, "onEventMainThread");
		eventBus.register(this, "onEventPostFragment");
		eventBus.register(this, "onEventMainFragement");
		eventBus.register(this, "onEventMainView");
		eventBus.register(this, "onEventHideMsg");
		eventBus.register(this, "onEventShowAllMSG");
		
	}
	private void getMsgCount(){
		String url=Url.SERVICE_MSG_PAY_COUNT_URL;
		JsonObject json=new JsonObject();
		json.addProperty("msgTime", MangrovePayApp.app.getData("msgTime"));
		InternetConfig config=new InternetConfig();
		config.setKey(1);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxString(url, json.toString(), config, this);
	}
	@InjectHttpOk
	public void getMsgResult(ResponseEntity r){
		try {
			Map<String, String> map = r.getHeaders();
			// 获取头部信息
		//	String[] msgArray = map.get(HeaderConst.MYMHOTEL_MESSAGE).split("\\|");
			// 成功 返回OK
			if ("OK".equals(map.get(HeaderConst.MYMHOTEL_STATUS))) {
				// Toast.makeText(context, msgArray[1], Toast.LENGTH_SHORT).show();
				String count= (String) Handler_Json.getValue("msgCount", r.getContentAsString());
				MangrovePayApp.msgCount=Integer.valueOf(count);
				Ioc.getIoc().getLogger().i("-------------新消息条数------------");
				Ioc.getIoc().getLogger().i("-------------新消息条数"+	MangrovePayApp.msgCount);
				if(MangrovePayApp.msgCount>0){
					//跨进程通信 告诉菜单有新消息
					MSGEntity mce=new MSGEntity();
					mce.setRead(true);
					eventBus.post(mce);
				}
			}// 失败
		} catch (Exception e) {
			Ioc.getIoc().getLogger().i("-------------获取小心中心条数失败------------");
			e.printStackTrace();
		}
	}
	
	@InjectHttpErr
	public void fialHttp(ResponseEntity r){
		Ioc.getIoc().getLogger().i("错误信息："+r.toString());
		Map<String, String> map = r.getHeaders();
		// 获取头部信息
		String[] msgArray = map.get(HeaderConst.MYMHOTEL_MESSAGE).split("\\|");
		Ioc.getIoc().getLogger().i(msgArray[1]);
	}
	public void onEventShowAllMSG(MSGEntity ce){
			cu=CommonUtils.newInstence();
			badge=cu.careteMsg(this, btn_leftmenu);
			MSGCountEntity mce=new MSGCountEntity();
			mce.setRead(true);
			eventBus.post(mce);
			
	}
	/*************************************************
	 * @Title: initMenu
	 * @Description: TODO(初始化 右侧菜单) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2014-11-19
	 *************************************************/
	private void initMenu() {
		menu = new SlidingMenu(this);
		// TOUCHMODE_FULLSCREEN 全屏模式，在整个content页面中，滑动，可以打开SlidingMenu
		// TOUCHMODE_MARGIN
		// 边缘模式，在content页面中，如果想打开SlidingMenu,你需要在屏幕边缘滑动才可以打开SlidingMenu
		// TOUCHMODE_NONE 不能通过手势打开SlidingMenu
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);// SlidingMenu滑动时的渐变程度
		menu.setMode(SlidingMenu.LEFT);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new LeftFragment()).commit();
		menu.setMenu(R.layout.menu_frame);// 设置menu的布局文件
		menu.setSecondaryShadowDrawable(R.drawable.shadow);

		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setOnOpenedListener(new OnOpenedListener() {

			@Override
			public void onOpened() {
				// TODO Auto-generated method stub
				View view = getWindow().peekDecorView();
				if (view != null) {
//隐藏虚拟键盘
					InputMethodManager inputmanger = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
					inputmanger.hideSoftInputFromWindow(view.getWindowToken(),
							0);
				}
				
			}
		});
		// ------------------------------------------------------------------------
		startFragmentAdd(new HomeFragment());
		// ------------------------------------------------------------------------
	}

	/*************************************************
	 * @Title: onKeyDown
	 * @Description: TODO(监听手机硬件返回按钮。如果当前左侧菜单打开 则关闭)
	 * @param keyCode
	 * @param event
	 * @return 设定文件
	 * @throws
	 * @date 2015-1-15
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int,
	 *      android.view.KeyEvent)
	 *************************************************/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 按下的如果是BACK，同时没有重复
			if (menu.isMenuShowing()) {
				menu.toggle();
				 return false;
			}else{
				 exit();  
				  return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	

	/*************************************************
	 @Title: exit 
	 @Description: TODO(关闭应用)     设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-2-6
	*************************************************/
	public void exit(){  
		System.out.println("调用了几次？"+isExit);
        if (!isExit) {  
            isExit = true;  
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();  
            mHandler.sendEmptyMessageDelayed(0, 2000);  
        } else {  
        	System.out.println("关闭应用？");
        	MangrovePayApp.getInstance().exit();
        }  

    }  
	
	Handler mHandler = new Handler() {  

        @Override  
        public void handleMessage(Message msg) {  

            super.handleMessage(msg);  
            isExit = false;  

        }  
    };  
	/*************************************************
	 * @Title: btnClick
	 * @Description: TODO(单击按钮 打开左侧菜单) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-15
	 *************************************************/
	@InjectMethod(value = @InjectListener(ids = { R.id.btn_leftmenu }, listeners = { OnClick.class }))
	public void btnClick(View v) {
		// ------------------------------------------------------------------------
		// 左右滑动空间
		menu.showMenu();// 显示SlidingMenu
	}

	/**
	 * Fragment 跳转事件
	 * 
	 * @author gdpancheng@gmail.com 2013-10-28 上午10:54:48
	 * @param fragment
	 * @return void
	 */
	private void startFragmentAdd(Fragment fragment) {
		// ------------------------------------------------------------------------
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		Ioc.getIoc().getLogger().i("当前打开的窗口:" + HomeFragment.class.getName());

		Fragment to_fragment = fragmentManager.findFragmentByTag(fragment
				.getClass().getName());
		if (to_fragment != null) {
			if (HomeFragment.class.getName().equals(
					fragment.getClass().getName())) {
				MainEntity me = new MainEntity();
				me.setViewEnable(false);
				me.setTitle_name("度假宝钱包");
				eventBus.post(me);// 隐藏首页的 tab
			}
			for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
				BackStackEntry entry = fragmentManager.getBackStackEntryAt(i);
				if (fragment.getClass().getName().equals(entry.getName())) {
					fragmentManager.popBackStack(entry.getName(), 1);
				}
			}
		}
		fragmentTransaction.replace(R.id.main_content, fragment,
				fragment.getClass().getName()).commitAllowingStateLoss();
		// ------------------------------------------------------------------------
	}

	/**
	 * 监听顶部右导航的点击事件 （）
	 * 
	 * @author gdpancheng@gmail.com 2013-10-28 上午10:53:05
	 * @param event
	 * @return void
	 */
	public void onEventMainThread(SlidingToggleEntity event) {
		if (menu.isMenuShowing()) {
			menu.toggle();
		} else {
			menu.showMenu();
		}
	}

	/*************************************************
	 * @Title: onEventMainView
	 * @Description: TODO(显示隐藏首页 view)
	 * @param event
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-15
	 *************************************************/
	public void onEventMainView(MainEntity event) {
		if (!event.isViewEnable()) {
			main_tab.setVisibility(View.VISIBLE);
			// btn_manage.setVisibility(View.VISIBLE);
		} else {
			main_tab.setVisibility(View.GONE);
			btn_manage.setVisibility(View.GONE);
		}
		if (!Handler_String.isEmpty(event.getTitle_name())) {
			textview_title.setText(event.getTitle_name());
		}
	}

	/**
	 * 监听Fragment跳转
	 * 
	 * @author gdpancheng@gmail.com 2013-10-28 上午10:52:19
	 * @param event
	 * @return void
	 */
	public void onEventMainFragement(FragmentEntity event) {
		startFragmentAdd(event.getFragment());
	}

	/*************************************************
	 * @Title: click
	 * @Description: TODO(tab 切换)
	 * @param view
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-9
	 *************************************************/
	@InjectMethod(@InjectListener(ids = { R.id.tab_hslcard, R.id.tab_balance }, listeners = { OnClick.class }))
	public void click(View view) {
		switch (view.getId()) {
		case R.id.tab_hslcard:
			fe.setFragment(new HSLCardFragment());
			btn_manage.setVisibility(View.VISIBLE);
			break;
		case R.id.tab_balance:
			fe.setFragment(new BalanceFragment());
			btn_manage.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		eventBus.post(fe);
	}

	/*************************************************
	 * @Title: onEventPostFragment
	 * @Description: TODO( getLayoutid 填充容器id getFragment 填充内容)
	 * @param crg
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2014-11-19
	 *************************************************/
	public void onEventPostFragment(FragmentEventEntity crg) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(crg.getLayoutid(), crg.getFragment());
		// transaction.addToBackStack(null);
		// commit\ 方法容易导致 Can not perform this action after onSaveInstanceState
		// 错误
		// commit方法是在Activity的onSaveInstanceState()之后调用的onSaveInstanceState方法是在该Activity即将被销毁前调用，
		// 来保存Activity数据的，如果在保存玩状态后
		// 再给它添加Fragment就会出错
		transaction.commitAllowingStateLoss();
	}
	public void onEventHideMsg(MSGInfoEntity msg) {
		if(null!=badge){
			badge.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		menu.destroyDrawingCache();
		main_tab.destroyDrawingCache();
		eventBus.unregister(this);
		if(null!=badge){
			badge.destroyDrawingCache();
		}
	
	}
}
