/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdjt.mangrovepay.zxing.view;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.android.pc.ioc.util.ContextUtils;
import com.google.zxing.ResultPoint;
import com.jdjt.mangrovepay.R;
import com.jdjt.mangrovepay.zxing.camera.CameraManager;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {
	private static final int CURRENT_POINT_OPACITY = 0xA0;
	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
			128, 64 };
	private static final long ANIMATION_DELAY = 100L;
	private static final int OPAQUE = 0xFF;

	private final Paint paint;
	private Bitmap resultBitmap;
	private Bitmap qrcode_kuang;
	private Drawable qrcode_line;
	private final int maskColor;
	private final int resultColor;
	private final int frameColor;
	private final int laserColor;
	private final int resultPointColor;
	private int scannerAlpha;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;
	private Context context;
	private int scan_range;
	private boolean direct;
	private int kuangWidth;
	private float textFontSize;
	private float offset;
	Rect mRect;

	// This constructor is used when the class is built from an XML resource.
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// Initialize these once for performance rather than calling them every
		// time in onDraw().
		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		frameColor = resources.getColor(R.color.viewfinder_frame);
		laserColor = resources.getColor(R.color.viewfinder_laser);
		resultPointColor = resources.getColor(R.color.possible_result_points);
		scannerAlpha = 0;
		
		
		possibleResultPoints = new HashSet<ResultPoint>(5);
		resultBitmap = BitmapFactory.decodeResource(resources, R.drawable.qrbg);

		qrcode_kuang = BitmapFactory.decodeResource(getResources(),
				R.drawable.qrbg);
		qrcode_line = getResources().getDrawable(R.drawable.line);
		mRect = new Rect();
		kuangWidth = ContextUtils.dip2px(context, 238);
		qrcode_kuang = zoomImg(qrcode_kuang, kuangWidth, kuangWidth);
		textFontSize = 17 * ContextUtils.getScale(context);
		offset = ContextUtils.dip2px(context, 10);
	}

	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片 www.2cto.com
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	@Override
	public void onDraw(Canvas canvas) {
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		// // 画出四个角
		// paint.setColor(getResources().getColor(R.color.btn_bg_green));
		// // 左上角
		// canvas.drawRect(frame.left, frame.top, frame.left + 15,frame.top + 5,
		// paint);
		// canvas.drawRect(frame.left, frame.top, frame.left + 5,frame.top + 15,
		// paint);
		// // 右上角
		// canvas.drawRect(frame.right - 15, frame.top, frame.right,frame.top +
		// 5, paint);
		// canvas.drawRect(frame.right - 5, frame.top, frame.right,frame.top +
		// 15, paint);
		// // 左下角
		// canvas.drawRect(frame.left, frame.bottom - 5, frame.left +
		// 15,frame.bottom, paint);
		// canvas.drawRect(frame.left, frame.bottom - 15, frame.left +
		// 5,frame.bottom, paint);
		// // 右下角
		// canvas.drawRect(frame.right - 15, frame.bottom - 5,
		// frame.right,frame.bottom, paint);
		// canvas.drawRect(frame.right - 5, frame.bottom - 15,
		// frame.right,frame.bottom, paint);
		// Draw the exterior (i.e. outside the framing rect) darkened
		/*
		 * paint.setColor(resultBitmap != null ? resultColor : maskColor);
		 * canvas.drawRect(0, 0, width, frame.top, paint); canvas.drawRect(0,
		 * frame.top, frame.left, frame.bottom + 1, paint);
		 * canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
		 * paint); canvas.drawRect(0, frame.bottom + 1, width, height, paint);
		 */
		// System.out.println("kuangWidth:" + kuangWidth);
		// Draw the exterior (i.e. outside the framing rect) darkened
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		canvas.drawRect(0, 0, width, (height - kuangWidth) / 5 + offset, paint);
		canvas.drawRect(0, (height - kuangWidth) / 5 + offset,
				(width - kuangWidth) / 2 + offset, (height - kuangWidth) / 5
						+ kuangWidth - offset, paint);
		canvas.drawRect((width - kuangWidth) / 2 + kuangWidth - offset,
				(height - kuangWidth) / 5 + offset, (width - kuangWidth) / 2
						+ offset + kuangWidth + (width - kuangWidth) / 2,
				(height - kuangWidth) / 5 + kuangWidth - offset, paint);
		canvas.drawRect(0, (height - kuangWidth) / 5 + kuangWidth - offset,
				width, height, paint);

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(CURRENT_POINT_OPACITY);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {
			// 在扫描框中画出模拟扫描的线条
			// 设置扫描线条颜色为绿色
			paint.setColor(getResources().getColor(R.color.white));
			// 设置绿色线条的透明值
			paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
			// 透明度变化
			scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;

			if ((scan_range += offset) < (kuangWidth - 2 * offset)) {

				// paint.setColor(Color.rgb(122, 177, 147));
				/* 以下为图片作为扫描线 */
				mRect.set((width - kuangWidth) / 2 + 15, (height - kuangWidth)
						/ 5 + scan_range, (width - kuangWidth) / 2 + kuangWidth
						- 5, (height - kuangWidth) / 5 + scan_range - 5);

				qrcode_line.setBounds(mRect);
				qrcode_line.draw(canvas);
				// invalidate();
			} else {
				scan_range = 0;
			}
//			canvas.drawRect(
//					(width - kuangWidth) / 2 + offset + offset / 2,
//					(height - kuangWidth) / 5 + scan_range,
//					(width - kuangWidth) / 2 + kuangWidth - offset - offset / 2,
//					(height - kuangWidth) / 5 + scan_range - 5, paint);
			paint.setColor(frameColor);
			canvas.drawBitmap(qrcode_kuang, (width - kuangWidth) / 2,
					(height - kuangWidth) / 5, paint);
			paint.setColor(Color.WHITE);
			paint.setTextSize(textFontSize);
			canvas.drawText("请扫描二维码",
					(width - paint.measureText("请扫描二维码")) / 2,
					(height - kuangWidth) / 5 + kuangWidth + 40, paint);
//			if (!direct) {
//				scan_range += offset;
//				if (scan_range > (kuangWidth - 2 * offset))
//					direct = true;
//			} else {
//				scan_range -= offset;
//				if (scan_range < 0)
//					direct = false;
//			}

			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) {
		        lastPossibleResultPoints = null;
		      } else {
		        possibleResultPoints = new HashSet<ResultPoint>(5);
		        lastPossibleResultPoints = currentPossible;
		        paint.setAlpha(OPAQUE);
		        paint.setColor(resultPointColor);
		        for (ResultPoint point : currentPossible) {
		          canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
		        }
		      }
		      if (currentLast != null) {
		        paint.setAlpha(OPAQUE / 2);
		        paint.setColor(resultPointColor);
		        for (ResultPoint point : currentLast) {
		          canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
		        }
		      }

			// Request another update at the animation interval, but only
			// repaint the laser line,
			// not the entire viewfinder mask.
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
					frame.right, frame.bottom);
		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

	public void recycleLineDrawable() {
		if (qrcode_line != null) {
			qrcode_line.setCallback(null);
		}
	}
}
