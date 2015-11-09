package com.jdjt.mangrovepay.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.download.FileLoaderManager;
import com.android.pc.ioc.download.NotfiEntity;
import com.android.pc.ioc.util.HeaderConst;
import com.android.pc.ioc.util.MapVo;
import com.android.pc.util.Handler_String;
import com.android.pc.util.Handler_TextStyle;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.view.BadgeView;

public class CommonUtils {

	private static CommonUtils cu;
	private BadgeView  badge;
	
	public static CommonUtils newInstence(){
		if(cu==null){
			cu=new CommonUtils();
		}
		return cu;
	}
	
	private static long lastClickTime;
	public static HashMap<String, Object> inHeaders() {

		// 取出头部信息
		HashMap<String, Object> map = new HashMap<String, Object>();
		String ticket = "";
		if (!Handler_String.isBlank(MangrovePayApp.ticket)) {
			ticket = MangrovePayApp.ticket;
		}

		map.put(HeaderConst.MYMHOTEL_TICKET, ticket);
		map.put(HeaderConst.MYMHOTEL_TYPE, "2001");
		map.put(HeaderConst.MYMHOTEL_VERSION, "1");
		map.put(HeaderConst.MYMHOTEL_DATATYPE, "JSON");
		map.put(HeaderConst.MYMHOTEL_SOURCECODE, "2001");
		map.put(HeaderConst.MYMHOTEL_DATETIME, new Date().getTime() + "");
		map.put(HeaderConst.MYMHOTEL_ACKDATATYPE, "JSON");

		Ioc.getIoc().getLogger().i("输入头部参数信息:{" + map.toString() + "}");

		return map;
	}

	/**
	 * 防止重复点击
	 * 
	 * @return 2014-6-26下午2:35:26
	 * @author wangmingyu
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 800) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 判断是否有长按动作发生
	 * 
	 * @param lastX
	 *            按下时X坐标
	 * @param lastY
	 *            按下时Y坐标
	 * @param thisX
	 *            移动时X坐标
	 * @param donwY
	 *            移动时Y坐标
	 * @param lastDownTime
	 *            按下时间 *
	 * @param thisEventTime
	 *            移动时间 *
	 * @param longPressTime
	 *            判断长按时间的阀值
	 * @return 2014-6-26下午2:54:17
	 * @author wangmingyu
	 */
	public static boolean isLongPressed(float lastX, float lastY, float thisX,
			float donwY, long lastDownTime, long thisEventTime,
			long longPressTime) {
		float offsetX = thisX - lastX;
		float offsetY = donwY - lastY;
		long intervalTime = thisEventTime - lastDownTime;
		if (offsetX <= 10 && offsetY <= 10 && intervalTime >= longPressTime) {
			return true;
		}
		return false;
	}

	/**
	 * 浮点数精确到2位小数
	 * 
	 * @param scale
	 * @return
	 */
	public static String fomatFloat2(float scale) {

		DecimalFormat fnum = new DecimalFormat("##0.00");
		return fnum.format(scale);
	}

	public static BigDecimal zeroTransformNull(BigDecimal bigDecimal) {
		if (null == bigDecimal) {
			return new BigDecimal("0.00");
		} else {
			return bigDecimal.setScale(2);
		}
	}

	/**
	 * 错误信息提示
	 * @param failedView
	 * @param failureMessage
	 * @param context
	 */
	public static void onErrorToast(View failedView,String failureMessage, Context context) {
		if (failedView instanceof EditText) {
			failedView.requestFocus();
			Handler_TextStyle handler_TextStyle = new Handler_TextStyle();
			handler_TextStyle.setString(failureMessage);
			//handler_TextStyle.setBackgroundColor(Color.RED, 0, failureMessage.length());
			((EditText) failedView).setError(handler_TextStyle.getSpannableString());
		} else {
			Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show();
		}
    }
	public static void onAlertToast(final View failedView,String failureMessage, Context context){
		final CommonAlert alert=new CommonAlert(context, R.layout.activity_alert_ok);
		alert.setMessage(failureMessage);
		alert.setNegativeButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (failedView instanceof EditText) {
					failedView.requestFocus();
					alert.dismiss();
				}
			}
		});
	}
	/**
	 * 获取iPhone地址
	 * @return
	 * @author wangmingyu
	 * @2014-9-20@下午2:32:10
	 * String	
	 */
	public static  String getLocalIpAddress()  
    {  
        try  
        {  
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)  
            {  
               NetworkInterface intf = en.nextElement();  
               for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)  
               {  
                   InetAddress inetAddress = enumIpAddr.nextElement();  
                   if (!inetAddress.isLoopbackAddress())  
                   {  
                       return inetAddress.getHostAddress().toString();  
                   }  
               }  
           }  
        }  
        catch (SocketException ex)  
        {  
            Log.e("WifiPreference IpAddress", ex.toString());  
        }  
        return null;  
    }  
	public static String getPsdnIp() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
	                //if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } catch (Exception e) {
	    }
	    return "";
	}
	
	 public static String intToIp(int i) {       
         
         return (i & 0xFF ) + "." +       
       ((i >> 8 ) & 0xFF) + "." +       
       ((i >> 16 ) & 0xFF) + "." +       
       ( i >> 24 & 0xFF) ;  
    } 
	 
	 /*************************************************
	 @Title: update 
	 @Description: TODO(更新通知) 
	 @param url    设定文件 
	 @return void    返回类型 
	 @throws 
	 @date  2015-1-30
	*************************************************/
	public static void update(String url) {
		try {
			// 构建一个下载通知栏对象 并填充资源
						NotfiEntity notfi = new NotfiEntity();
						notfi.setLayout_id(R.layout.update_notification_progress);
						notfi.setIcon_id(R.id.updatehelper_notification_progress_icon);
						notfi.setProgress_id(R.id.updatehelper_notification_progress_pb);
						notfi.setProgress_txt_id(R.id.updatehelper_notification_progress_tv);
						FileLoaderManager.downloadUpdate(url, 3);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
			
			// 构建一个下载通知栏对象 并填充资源
		}
	/*************************************************
	 * @Title: getVersionName
	 * @Description: TODO(获取当前版本号)
	 * @return
	 * @throws Exception
	 *             设定文件
	 * @return String 返回类型
	 * @throws
	 * @date 2015-1-30
	 *************************************************/
	public static String getVersionName(Context context) throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),
				0);
		String version = packInfo.versionName;
		return version;
	}
	
	public static File getFileFromServer(String path, ProgressDialog pd) throws Exception{
		//如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			URL url = new URL(path);
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			//获取到文件的大小 
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len ;
			int total=0;
			while((len =bis.read(buffer))!=-1){
				fos.write(buffer, 0, len);
				total+= len;
				//获取当前下载量
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		}
		else{
			return null;
		}
	}
	public BadgeView careteMsg(Context context ,View view){
		  badge= new BadgeView(context, view);//创建一个BadgeView对象，view为你需要显示提醒信息的控件
		badge.setWidthAndHight(12);
		badge.setBadgePosition(BadgeView.POSITION_TOP_LEFT);//显示的位置.中间，还有其他位置属性
//		badge.setTextColor(Color.WHITE);  //文本颜色
		badge.setBadgeBackgroundColor(Color.RED); //背景颜色
//		//badge.setTextSize(12); //文本大小
//		//badge.setHeight(pixels)
		badge.setBadgeMargin(0, 5); //水平和竖直方向的间距
//		badge.setBadgeMargin(0);//各边间距
		        /**
		         * 可以设置一个动画
		         */
//		        TranslateAnimation anim = new TranslateAnimation(-100, 0, 0, 0);
//		        anim.setInterpolator(new BounceInterpolator());
//		        anim.setDuration(1000);
//		        badge1.toggle(anim,null);
		badge.toggle(); //设置，还要加这句,已经显示则影藏，否则显示
		      //单纯的显示badge1.show();
		       //badge1.hiden();影藏
		return badge;
	}
	
	public void showMsg(){
		badge.show();
	}
	public void hideMsg(){
		badge.hide();
	}
}
