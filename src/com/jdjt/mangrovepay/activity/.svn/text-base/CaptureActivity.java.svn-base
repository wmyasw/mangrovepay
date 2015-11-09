package com.jdjt.mangrovepay.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.ioc.inject.InjectHttpErr;
import com.android.pc.ioc.inject.InjectHttpOk;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.inject.InjectListener;
import com.android.pc.ioc.inject.InjectMethod;
import com.android.pc.ioc.internet.FastHttpHander;
import com.android.pc.ioc.internet.InternetConfig;
import com.android.pc.ioc.internet.ResponseEntity;
import com.android.pc.ioc.verification.Rules;
import com.android.pc.ioc.verification.annotation.Regex;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.pc.util.Handler_Json;
import com.android.pc.util.Handler_String;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.application.MangrovePayApp;
import com.jdjt.mangrovepay.common.BaseFragmentActivity;
import com.jdjt.mangrovepay.contanst.ErrorMsgEnum;
import com.jdjt.mangrovepay.contanst.HeaderConst;
import com.jdjt.mangrovepay.contanst.Url;
import com.jdjt.mangrovepay.fragement.SettingFragment;
import com.jdjt.mangrovepay.utils.CommonAlert;
import com.jdjt.mangrovepay.utils.CommonUtils;
import com.jdjt.mangrovepay.utils.ResultParse;
import com.jdjt.mangrovepay.zxing.camera.CameraManager;
import com.jdjt.mangrovepay.zxing.decoding.CaptureActivityHandler;
import com.jdjt.mangrovepay.zxing.decoding.InactivityTimer;
import com.jdjt.mangrovepay.zxing.decoding.RGBLuminanceSource;
import com.jdjt.mangrovepay.zxing.decoding.Utils;
import com.jdjt.mangrovepay.zxing.view.ViewfinderView;

/*************************************************
@copyright:bupt
@author: 王明雨
@date:2015-3-24
@Description:TODO (扫码支付)
**************************************************/
@InjectLayer(value = R.layout.zxing_main, parent = R.id.center_common,  isTitle = true)
public class CaptureActivity extends BaseFragmentActivity implements Callback {
	private static final int REQUEST_CODE = 234;
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	String tokenId;
	Bitmap scanBitmap;
	private String photo_path;
	 CommonAlert alert;
	@InjectMethod(@InjectListener(ids = { R.id.img_back }, listeners = { OnClick.class }))
	public void onBackClick(View v) {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
		startActivity(intent);
		this.finish();
	}

	@InjectInit
	public void init() {
		// 初始化 CameraManager
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		textview_title.setText("扫码");
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@InjectMethod(@InjectListener(ids = { R.id.qrcode_tab2, R.id.qrcode_tab3 }, listeners = { OnClick.class }))
	public void imgClick(View v) {
		switch (v.getId()) {
		case R.id.qrcode_tab2:
			openAlbum();
			
			break;
		case R.id.qrcode_tab3:
			boolean isOpen = CameraManager.get().openHlight();
			// ImageView qrcode_tab3 =(ImageView)
//			 v.findViewById(R.id.qrcode_tab3);
//			 if (isOpen) {
//				 cameraManager.setTorch(false);
//			 } else {
//				 cameraManager.setTorch(false);
//			 }

			break;
		default:
			break;
		}
	}

	protected Result scanningImage(String path) {
		if (Handler_String.isEmpty(path)) {

			return null;

		}
		// DecodeHintType 和EncodeHintType
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小

		int sampleSize = (int) (options.outHeight / (float) 200);

		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);

		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);

		} catch (NotFoundException e) {

			e.printStackTrace();

		} catch (ChecksumException e) {

			e.printStackTrace();

		} catch (com.google.zxing.FormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK){
			switch (requestCode) {
			case REQUEST_CODE:

					String[] proj = { MediaStore.Images.Media.DATA };
					// 获取选中图片的路径
					Cursor cursor = getContentResolver().query(data.getData(),
							proj, null, null, null);

					if (cursor.moveToFirst()) {

						int column_index = cursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						photo_path = cursor.getString(column_index);
						if (photo_path == null) {
							photo_path = Utils.getPath(getApplicationContext(),
									data.getData());
							Log.i("123path  Utils", photo_path);
						}
						Log.i("123path", photo_path);

					}

					cursor.close();
				final	Result result = scanningImage(photo_path);
					// String result = decode(photo_path);
					//Looper.prepare();
					if (result == null) {
						Toast.makeText(getApplicationContext(), "图片格式有误", 0)
								.show();
					} else {
						String recode = recode(result.toString());
						Ioc.getIoc().getLogger().i(recode);
						if (Handler_String.isBlank(recode)) {
							alertMsg("不能识别的二维码",null);
						}else{
							tokenId = recode;
							scanCode();
						}
					}
					
					break;

				default:
					break;
			}
		}
		
	}

	/*************************************************
	 @Title: recode 
	 @Description: TODO(设置解析编码 支持中文) 
	 @param str
	 @return    设定文件 
	 @return String    返回类型 
	 @throws 
	 @date  2015-1-27
	*************************************************/
	private String recode(String str) {
		String formart = "";

		try {
			boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
					.canEncode(str);
			if (ISO) {
				formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
				Log.i("1234      ISO8859-1", formart);
			} else {
				formart = str;
				Log.i("1234      stringExtra", str);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formart;
	}

	/*************************************************
	 * @Title: openAlbum
	 * @Description: TODO(打开相册) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-19
	 *************************************************/
	private void openAlbum() {
		Intent innerIntent = new Intent(); // "android.intent.action.GET_CONTENT"
		innerIntent.addCategory(Intent.CATEGORY_OPENABLE);  
		if (Build.VERSION.SDK_INT < 19) {
			innerIntent.setAction(Intent.ACTION_GET_CONTENT);
		} else {
			innerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
		}
		innerIntent.setType("image/*");
		Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");

		this.startActivityForResult(wrapperIntent, REQUEST_CODE);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
			// do something here
			Intent intent = new Intent();
			intent.setClass(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 它可以关掉所要到的界面中间的activity
			startActivity(intent);
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (handler != null) // 实现连续扫描
			handler.restartPreviewAndDecode();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(final Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
	
		playBeepSoundAndVibrate();
		String resultString = obj.getText();
		System.out.println("resultString:" + resultString);
		// FIXME
		if (Handler_String.isBlank(resultString)) {
			alertMsg("不能识别的二维码",null);
		}else{
			tokenId = resultString;
			scanCode();
		}
	}

	/*************************************************
	 * @Title: scanCode
	 * @Description: TODO(这里用一句话描述这个方法的作用) 设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-19
	 *************************************************/
	private void scanCode() {
		if(tokenId.matches("\\b(([\\w-]+://?|www[.])[^\\s()<>]+(?:\\([\\w\\d]+\\)|([^[:punct:]\\s]|/)))")){
			alertMsg("",tokenId);
		}else{
			showDialog("请稍候...");
			String appid = MangrovePayApp.app.getData("appid");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("appid", appid);
			params.put("token", tokenId);
			params.put("ticket", "");
			InternetConfig config = new InternetConfig();
			// config.setHead(CommonUtils.inHeaders());
			config.setKey(11);
			config.setHead(CommonUtils.inHeaders());
			String url = Url.QRCODE_SCAN;
			FastHttpHander.ajaxGet(url, params, config, this);
		}
	}

	/*************************************************
	 * @Title: sacnResult
	 * @Description: TODO(扫码验证返回)
	 * @param r
	 *            设定文件
	 * @return void 返回类型
	 * @throws
	 * @date 2015-1-19
	 *************************************************/
	@InjectHttpOk(11)
	public void sacnResult(ResponseEntity r) {
		dismissDialog();
		Ioc.getIoc().getLogger().i("扫码成功 ，返回数据:" + r.toString());
		if (ResultParse.isResultOK(r, this)) {
			JSONObject switcher = (JSONObject) Handler_Json.getValue(r
					.getContentAsString());
			try {
				String sidCode = switcher.getString("sidCode");
				String msgType = switcher.getString("msgType");
				String token = switcher.getString("token");
				String createTime = switcher.getString("createTime");
				String content = switcher.getString("content");

				if (sidCode.equals("11")) {
					String[] codes = content.split("[|]");
					alertMsg("暂不支持在度假宝进行绑定房间操作",null);
				} else {
					/**/
					ajaxPost(content);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				alertMsg("二维码无效",null);
			}
		} else {
			Map<String, String> map = r.getHeaders();
			String[] msgArray = map.get(HeaderConst.MYMHOTEL_MESSAGE).split(
					"\\|");
			alertMsg(msgArray[1],null);
		}

	}

	public void ajaxPost(String orderno) {
		showDialog("请稍候...");
		String url = Url.PAYMENT_ORDER_INFO_URL;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("payOrderNo", orderno);
		InternetConfig config = new InternetConfig();
		config.setKey(1);
		config.setHead(CommonUtils.inHeaders());
		FastHttpHander.ajaxGet(url, map, config, this);
	}

	@InjectHttpOk(1)
	public void resultOk(ResponseEntity r) {
		dismissDialog();
		if (ResultParse.isResultOK(r, this)) {
			HashMap<String, String> map = Handler_Json.JsonToCollection(r
					.getContentAsString());
			if ("0000".equals(map.get("orderStatus"))
					|| "1001".equals(map.get("orderStatus"))) {
				Intent intent = new Intent();
				intent.setClass(this, QROnlinePayActivity.class);
				intent.putExtra("order_no", map.get("payOrderNo"));
				startActivity(intent);
			} else {
				alertMsg("该订单已支付成功",null);
			}
		} else {
			Map<String, String> map = r.getHeaders();
			String[] msgArray = map.get(HeaderConst.MYMHOTEL_MESSAGE).split(
					"\\|");
			alertMsg(msgArray[1],null);
		}
	}

	private void alertMsg(String msg,final String resultCode) {
		alert=new CommonAlert(this);
		if(!Handler_String.isBlank(resultCode)){
			alert.setMessage(resultCode);
		}else{
			alert.setMessage(msg);
		}
		alert.setNegativeButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!Handler_String.isBlank(resultCode)){
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse(resultCode);
					intent.setData(content_url);
					startActivity(intent);
				}else{
					Ioc.getIoc().getLogger().i("确定后实现重新扫描");
					if (handler != null) // 实现连续扫描
						handler.restartPreviewAndDecode();
				}
				alert.cancel();
			}
		});
		alert.setPositiveButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Ioc.getIoc().getLogger().i("取消后 实现重新扫描");
				if (handler != null) // 实现连续扫描
					handler.restartPreviewAndDecode();
				alert.cancel();
			}
		});
		
	}

	@InjectHttpErr(value = { 1, 11 })
	public void resultErr(ResponseEntity r) {
		dismissDialog();
		Ioc.getIoc().getLogger().i("扫码成功 ，但请求失败 ，返回数据:" + r.toString());
		alertMsg(ErrorMsgEnum.NET_ERROR.getName(),null);
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}