package com.export.vipshop.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <p>Title: BitMapUtil</p>
 * <p>Description: BitMapUtil</p>
 */
public class BitMapUtil {
	public static final int WX_MAX = 120;

	public static Bitmap getUCAvatar(final String avatar) {
		if (TextUtils.isEmpty(avatar)) {
			return null;
		}
		try {
			byte[] imageByte = Base64.decode(avatar, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}

	public static Bitmap getBitMap4WXApiWithUrl(String url, boolean fixed, boolean recycle) {

		Bitmap scaledBitmap = null ;

		try {
			Bitmap bmp = BitmapFactory.decodeStream(new URL(url).openStream());
			if(bmp == null){
				return null;
			}
			if (!fixed) {
				scaledBitmap = getBitMap4WXApi(bmp,recycle);
			} else {
				scaledBitmap = Bitmap.createScaledBitmap(bmp, WX_MAX, WX_MAX, true);
			}
			
		} catch (MalformedURLException e) {
			scaledBitmap = null;
			e.printStackTrace();
		} catch (IOException e) {
			scaledBitmap = null;
			e.printStackTrace();
		}
		return scaledBitmap;
	}

	/**
	 * 通过url获取适合微信分享的位图。注意：微信分享时的icon图标在发送给微信时不能过大， 以120*120以下为宜（当然
	 * 也可以直接设置icon的长宽）
	 *
	 */
	public static Bitmap getBitMap4WXApi(Bitmap bmp ,boolean recycle) {
		Bitmap scaledBitmap;
		if(bmp == null){
			scaledBitmap = null;
		}else{
			int bmpWitdh = bmp.getWidth();
			int bmpHeight = bmp.getHeight();

			
			if(bmpWitdh<WX_MAX && bmpHeight<WX_MAX){
				//当图片长宽均小于WX_MAX大小时，不进行强制设置，否则图片失真，微信分享失败
				scaledBitmap = Bitmap.createScaledBitmap(bmp, bmpHeight,
						bmpHeight, true);
			}else{
				float scaleWitdth = ((float) WX_MAX) / bmpWitdh;
				float scaleHeight = ((float) WX_MAX) / bmpHeight;
				
				float chooseScale = scaleWitdth > scaleHeight ? scaleHeight
						: scaleWitdth;

				int chooseWidth = (int) (chooseScale * bmpWitdh);
				int chooseHeight = (int) (chooseScale * bmpHeight);

				if (chooseWidth > WX_MAX || chooseHeight > WX_MAX) {
					chooseWidth = WX_MAX;
					chooseHeight = WX_MAX;
				}

				scaledBitmap = Bitmap.createScaledBitmap(bmp, chooseWidth,
						chooseHeight, true);
			}

			if(recycle){
				bmp.recycle();
			}
		}
		return scaledBitmap;

	}

	/**
	 * 按屏幕适配Bitmap
	 */
	public static Bitmap scaleBitmap(Context context, byte[] data, float percent) {

		// 这里我不获取了，假设是下面这个分辨率
		int screenWidth = DensityUtil.getWidthPixels(context);
		int screenrHeight = DensityUtil.getHeightPixels(context);
		// 设置 options
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		// 读取
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
				options);

		int imgWidth = options.outWidth;
		int imgHeight = options.outHeight;
		if (imgWidth > screenWidth * percent
				|| imgHeight > screenrHeight * percent) {
			options.inSampleSize = calculateInSampleSize(options, screenWidth,
					screenrHeight, percent);
		}

		options.inJustDecodeBounds = false;

		options.inPreferredConfig = Bitmap.Config.RGB_565;

		options.inPurgeable = true;

		options.inInputShareable = true;

		bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
		return bitmap;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int screenWidth, int screenHeight, float percent) {

		// 原始图片宽高
		final int height = options.outHeight;
		final int width = options.outWidth;
		// 倍数
		int inSampleSize = 1;

		if (height > screenHeight * percent || width > screenWidth * percent) {

			// 计算目标宽高与原始宽高的比值
			final int inSampleSize_h = Math.round((float) height / (float) (screenHeight * percent));

			final int inSampleSize_w = Math.round((float) width / (float) (screenWidth * percent));

			// 选择两个比值中较小的作为inSampleSize的
			inSampleSize = inSampleSize_h < inSampleSize_w ? inSampleSize_h : inSampleSize_w;

			if (inSampleSize < 1) {
				inSampleSize = 1;
			}
		}
		// 简单说这个数字就是 缩小为原来的几倍，根据你的image需要占屏幕多大动态算的（比如你用的权重设置layout）
		return inSampleSize;
	}
	public static Bitmap decodeSampledBitmapFromResource(String res,
	        int reqWidth, int reqHeight) {  
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;  
	    BitmapFactory.decodeFile(res, options);
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);  
	    options.inJustDecodeBounds = false;  
	    return BitmapFactory.decodeFile(res, options);
	} 
	private static int calculateInSampleSize(BitmapFactory.Options options,
	        int reqWidth, int reqHeight) {  
	    // 源图片的高度和宽度  
	    final int height = options.outHeight;  
	    final int width = options.outWidth;  
	    int inSampleSize = 1;  
	    if (height > reqHeight || width > reqWidth) {  
	        // 计算出实际宽高和目标宽高的比率  
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);
	        // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高  
	        // 一定都会大于等于目标的宽和高。  
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;  
	    }
	    return inSampleSize;  
	}

	public static Bitmap decodeSplashBg(Context context, String path) {
		int width = DeviceUtil.getScreenWidth(context);
		int height = DeviceUtil.getScreenHeight(context);
		Bitmap bitmap = decodeSampledBitmapFromResource(path,width,height);

		return bitmap;
	}

	public static Bitmap getOptBitmap(Context context, int resId){
		int width = DeviceUtil.getScreenWidth(context);
		int height = DeviceUtil.getScreenHeight(context);

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), resId, options);
		options.inSampleSize = calculateInSampleSize(options, width, height);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(context.getResources(), resId, options);
	}

	public static void blur(Context context, Bitmap bkg, View view) {
		float scaleFactor = 8; //图片缩放比例；
		float radius = 20; //模糊程度

		if (view.getWidth()==0 || view.getHeight()==0)
			return;
		Bitmap overlay = Bitmap.createBitmap(
				(int) (view.getMeasuredWidth() / scaleFactor),
				(int) (view.getMeasuredHeight() / scaleFactor),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()/ scaleFactor);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(bkg, 0, 0, paint);

		overlay = FastBlur.doBlur(overlay, (int) radius, true);
		view.setBackground(new BitmapDrawable(context.getResources(), overlay));
		/**
		 * 打印高斯模糊处理时间，如果时间大约16ms，用户就能感到到卡顿，时间越长卡顿越明显，如果对模糊完图片要求不高，可是将scaleFactor设置大一些。
		 */
	}

	static class FastBlur {

		public static Bitmap doBlur(Bitmap sentBitmap, int radius,
									boolean canReuseInBitmap) {
			Bitmap bitmap;
			if (canReuseInBitmap) {
				bitmap = sentBitmap;
			} else {
				bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
			}

			if (radius < 1) {
				return (null);
			}

			int w = bitmap.getWidth();
			int h = bitmap.getHeight();

			int[] pix = new int[w * h];
			bitmap.getPixels(pix, 0, w, 0, 0, w, h);

			int wm = w - 1;
			int hm = h - 1;
			int wh = w * h;
			int div = radius + radius + 1;

			int r[] = new int[wh];
			int g[] = new int[wh];
			int b[] = new int[wh];
			int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
			int vmin[] = new int[Math.max(w, h)];

			int divsum = (div + 1) >> 1;
			divsum *= divsum;
			int dv[] = new int[256 * divsum];
			for (i = 0; i < 256 * divsum; i++) {
				dv[i] = (i / divsum);
			}

			yw = yi = 0;

			int[][] stack = new int[div][3];
			int stackpointer;
			int stackstart;
			int[] sir;
			int rbs;
			int r1 = radius + 1;
			int routsum, goutsum, boutsum;
			int rinsum, ginsum, binsum;

			for (y = 0; y < h; y++) {
				rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
				for (i = -radius; i <= radius; i++) {
					p = pix[yi + Math.min(wm, Math.max(i, 0))];
					sir = stack[i + radius];
					sir[0] = (p & 0xff0000) >> 16;
					sir[1] = (p & 0x00ff00) >> 8;
					sir[2] = (p & 0x0000ff);
					rbs = r1 - Math.abs(i);
					rsum += sir[0] * rbs;
					gsum += sir[1] * rbs;
					bsum += sir[2] * rbs;
					if (i > 0) {
						rinsum += sir[0];
						ginsum += sir[1];
						binsum += sir[2];
					} else {
						routsum += sir[0];
						goutsum += sir[1];
						boutsum += sir[2];
					}
				}
				stackpointer = radius;

				for (x = 0; x < w; x++) {

					r[yi] = dv[rsum];
					g[yi] = dv[gsum];
					b[yi] = dv[bsum];

					rsum -= routsum;
					gsum -= goutsum;
					bsum -= boutsum;

					stackstart = stackpointer - radius + div;
					sir = stack[stackstart % div];

					routsum -= sir[0];
					goutsum -= sir[1];
					boutsum -= sir[2];

					if (y == 0) {
						vmin[x] = Math.min(x + radius + 1, wm);
					}
					p = pix[yw + vmin[x]];

					sir[0] = (p & 0xff0000) >> 16;
					sir[1] = (p & 0x00ff00) >> 8;
					sir[2] = (p & 0x0000ff);

					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];

					rsum += rinsum;
					gsum += ginsum;
					bsum += binsum;

					stackpointer = (stackpointer + 1) % div;
					sir = stack[(stackpointer) % div];

					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];

					rinsum -= sir[0];
					ginsum -= sir[1];
					binsum -= sir[2];

					yi++;
				}
				yw += w;
			}
			for (x = 0; x < w; x++) {
				rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
				yp = -radius * w;
				for (i = -radius; i <= radius; i++) {
					yi = Math.max(0, yp) + x;

					sir = stack[i + radius];

					sir[0] = r[yi];
					sir[1] = g[yi];
					sir[2] = b[yi];

					rbs = r1 - Math.abs(i);

					rsum += r[yi] * rbs;
					gsum += g[yi] * rbs;
					bsum += b[yi] * rbs;

					if (i > 0) {
						rinsum += sir[0];
						ginsum += sir[1];
						binsum += sir[2];
					} else {
						routsum += sir[0];
						goutsum += sir[1];
						boutsum += sir[2];
					}

					if (i < hm) {
						yp += w;
					}
				}
				yi = x;
				stackpointer = radius;
				for (y = 0; y < h; y++) {
					// Preserve alpha channel: ( 0xff000000 & pix[yi] )
					pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
							| (dv[gsum] << 8) | dv[bsum];

					rsum -= routsum;
					gsum -= goutsum;
					bsum -= boutsum;

					stackstart = stackpointer - radius + div;
					sir = stack[stackstart % div];

					routsum -= sir[0];
					goutsum -= sir[1];
					boutsum -= sir[2];

					if (x == 0) {
						vmin[y] = Math.min(y + r1, hm) * w;
					}
					p = x + vmin[y];

					sir[0] = r[p];
					sir[1] = g[p];
					sir[2] = b[p];

					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];

					rsum += rinsum;
					gsum += ginsum;
					bsum += binsum;

					stackpointer = (stackpointer + 1) % div;
					sir = stack[stackpointer];

					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];

					rinsum -= sir[0];
					ginsum -= sir[1];
					binsum -= sir[2];

					yi += w;
				}
			}

			bitmap.setPixels(pix, 0, w, 0, 0, w, h);

			return (bitmap);
		}
	}
}
