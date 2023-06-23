package com.isoft.customalmonds.barcodegenerate;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public abstract class BarcodeCreater {

	// private static int marginW = 20;

	public static BarcodeFormat barcodeFormat;
	private static int textSize = 14;

	public static Bitmap creatBarcodearabic(Context context, String contents,
			String destext,int bcodepos, int descpos, String pname, int desiredWidth, int desiredHeight,
			 boolean displayCode, int barType, int stickerHeight) {
		Bitmap ruseltBitmap = null;
		if (barType == 1) {
			barcodeFormat = BarcodeFormat.QR_CODE;
		} else if (barType == 2) {
			barcodeFormat = BarcodeFormat.CODE_128;
		} else if (barType == 3) {
			barcodeFormat = BarcodeFormat.CODE_39;
		} else if (barType == 4) {
			barcodeFormat = BarcodeFormat.EAN_13;
		}
		if (displayCode) {
			Bitmap barcodeBitmap = null;
			try {
				barcodeBitmap = encodeAsBitmap(contents, barcodeFormat,
						desiredWidth, desiredHeight);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Bitmap codeBitmap = creatCodeBitmap(pname, desiredWidth,
					desiredHeight, context);
			Bitmap descBitmap = creatCodeBitmap(destext, desiredWidth,
					desiredHeight, context);
			ruseltBitmap = mixtureBitmap(barcodeBitmap, codeBitmap,
					bcodepos, descpos, stickerHeight);
			if (codeBitmap != null) {
				codeBitmap.recycle();
			}
			codeBitmap = null;

			if (barcodeBitmap != null) {
				barcodeBitmap.recycle();
			}
			barcodeBitmap = null;
			if (descBitmap != null) {
				descBitmap.recycle();
			}
			descBitmap = null;
		} else {
			try {
				ruseltBitmap = encodeAsBitmap(contents, barcodeFormat,
						desiredWidth, desiredHeight);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ruseltBitmap;
	}

	public static Bitmap encode2dAsBitmap(String contents, int desiredWidth,
			int desiredHeight, int barType) {
		if (barType == 1) {
			barcodeFormat = BarcodeFormat.QR_CODE;
		} else if (barType == 2) {
			barcodeFormat = BarcodeFormat.CODE_128;
		} else if (barType == 3) {
			barcodeFormat = BarcodeFormat.CODE_39;
		} else if (barType == 4) {
			barcodeFormat = BarcodeFormat.EAN_13;
		}
		Bitmap barcodeBitmap = null;
		try {
			barcodeBitmap = encodeAsBitmap(contents, barcodeFormat,
					desiredWidth, desiredHeight);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return barcodeBitmap;
	}

	public static Bitmap encodeAsBitmap(String contents, BarcodeFormat format,
			int desiredWidth, int desiredHeight) throws WriterException {
		final int WHITE = 0xFFFFFFFF; // ����ָ��������ɫ���ö�ά���ɲ�ɫЧ��
		final int BLACK = 0xFF000000;

		HashMap<EncodeHintType, String> hints = null;
		String encoding = guessAppropriateEncoding(contents);
		if (encoding != null) {
			hints = new HashMap<EncodeHintType, String>(2);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result = writer.encode(contents, format, desiredWidth,
				desiredHeight, hints);
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		// All are 0, or black, by default
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	public static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}

	public static boolean saveBitmap2file(Bitmap bmp, String filename) {
		CompressFormat format = CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream("/sdcard/" + filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bmp.compress(format, quality, stream);
	}
	public static Bitmap creatBarcode(Context context, String contents,
			 int bcodepos,
			int descpos, int desiredWidth, int desiredHeight, boolean displayCode, int barType, int stickerHeight) {
		Bitmap ruseltBitmap = null;
		Log.e("barType1z", "" + "-"+barType);
		if (barType == 1) {
			barcodeFormat = BarcodeFormat.QR_CODE;
		} else if (barType == 2) {
			barcodeFormat = BarcodeFormat.CODE_128;
		} else if (barType == 3) {
			barcodeFormat = BarcodeFormat.CODE_39;
		} else if (barType == 4) {
			barcodeFormat = BarcodeFormat.EAN_13;
		}
		if (displayCode) {
			Bitmap barcodeBitmap = null;
			try {
					barcodeBitmap = encodeAsBitmap(contents, barcodeFormat,
							desiredWidth, desiredHeight);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Bitmap codeBitmap = creatCodeBitmap(contents, desiredWidth,
					desiredHeight, context);


			ruseltBitmap = mixtureBitmap(barcodeBitmap, codeBitmap,
					bcodepos, descpos, stickerHeight);
			if (codeBitmap != null) {
				codeBitmap.recycle();
			}
			codeBitmap = null;

			if (barcodeBitmap != null) {
				barcodeBitmap.recycle();
			}
			barcodeBitmap = null;

		} else {
			try {
				ruseltBitmap = encodeAsBitmap(contents, barcodeFormat,
						desiredWidth, desiredHeight);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ruseltBitmap;
	}
	public static Bitmap creatCodeBitmap(String contents, int width,
			int height, Context context) {
		Typeface font = Typeface.createFromAsset(context.getAssets(), "Helvetica.ttf");
		Log.e("barType1z", "" + "RND Senthil");
		TextView tv = new TextView(context);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				width, height);
		tv.setLayoutParams(layoutParams);
		tv.setText(contents);
		tv.setTypeface(font);
		// tv.setHeight(10);
		tv.setTextSize(textSize);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setWidth(width);
		tv.setDrawingCacheEnabled(true);
		tv.setTextColor(Color.BLACK);
		tv.setBackgroundColor(Color.WHITE);
		tv.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

		tv.buildDrawingCache();
		Bitmap bitmapCode = tv.getDrawingCache();
		return bitmapCode;
	}
	public static Bitmap mixtureBitmap(Bitmap barCodeBitmap, Bitmap barCodeTextBitmap,
			 int bcodepos, int descpos, int stickerHight) {
		int topPoint=0;
		int height = 0; 
		Log.e("barType1z1", "" + "RND");
		Log.e("bcodepos", "" + "bcodepos");
		Log.e("descpos", "" + "descpos");
		
		if (barCodeBitmap == null || barCodeTextBitmap == null) {
			return null;
		}
		height+= barCodeBitmap.getHeight() + barCodeTextBitmap.getHeight();
//		if(descBitmap!=null){
//			height+=descBitmap.getHeight();
//		}
		Log.e("barType1z", "" + "RND");
		Log.e("bcodepos0", "" + "-"+bcodepos);
		Log.e("sds", "" + "-"+descpos);
		Bitmap newBitmap = Bitmap
				.createBitmap(
						barCodeBitmap.getWidth(),
						stickerHight, Config.ARGB_4444);
		Canvas cv = new Canvas(newBitmap);
		if (bcodepos == 1 && descpos == 1) {
			Log.e("zerz", "above");
			cv.drawBitmap(barCodeTextBitmap, 0, topPoint, null);
			topPoint += barCodeTextBitmap.getHeight();
//			if(descBitmap!=null){
//			cv.drawBitmap(descBitmap, 0, topPoint, null);
//			topPoint += descBitmap.getHeight();
//			}
			cv.drawBitmap(barCodeBitmap, 0, topPoint, null);
		} else if (bcodepos == 0 && descpos == 0) {
			Log.e("zerz", "below");
			cv.drawBitmap(barCodeBitmap, 0, topPoint, null);
			topPoint += barCodeBitmap.getHeight();
			cv.drawBitmap(barCodeTextBitmap, 0, topPoint, null);
			topPoint += barCodeTextBitmap.getHeight();
//			if(descBitmap!=null){
//			cv.drawBitmap(descBitmap, 0, topPoint, null);
//			}
		} else if (bcodepos == 1 && descpos == 0) {			
			Log.e("zerz", "3");
			cv.drawBitmap(barCodeTextBitmap, 0, topPoint, null);
			topPoint += barCodeTextBitmap.getHeight();
			cv.drawBitmap(barCodeBitmap, 0, topPoint, null);
			topPoint += barCodeBitmap.getHeight();
//			if(descBitmap!=null){
//			cv.drawBitmap(descBitmap, 0, topPoint, null);
//			}
		} else if (bcodepos == 0 && descpos == 1) {
			Log.e("zerz", "4");
//			if(descBitmap!=null){
//			cv.drawBitmap(descBitmap, 0, topPoint, null);
//			topPoint += descBitmap.getHeight();
//			}
			cv.drawBitmap(barCodeBitmap, 0, topPoint, null);
			topPoint += barCodeBitmap.getHeight();
			cv.drawBitmap(barCodeTextBitmap, 0, topPoint, null);
		} else if (bcodepos == 1 && descpos == 2) {
			Log.e("zerz", "5");
			cv.drawBitmap(barCodeTextBitmap, 0, topPoint, null);
			cv.drawBitmap(barCodeBitmap, 0, barCodeTextBitmap.getHeight(), null);
		} else if (bcodepos == 0 && descpos == 2) {
			Log.e("zerz", "6");
			cv.drawBitmap(barCodeBitmap, 0, topPoint, null);
			cv.drawBitmap(barCodeTextBitmap, 0, barCodeBitmap.getHeight(), null);
		} else if (bcodepos == 2 && descpos == 0) {
			Log.e("zerz", "7");
			cv.drawBitmap(barCodeBitmap, 0, topPoint, null);
			topPoint += barCodeBitmap.getHeight();
//			if(descBitmap!=null){
//			cv.drawBitmap(descBitmap, 0, topPoint, null);
//			}
		} else if (bcodepos == 2 && descpos == 1) {
			Log.e("zerz", "8");
//			if(descBitmap!=null){
//			cv.drawBitmap(descBitmap, 0, topPoint, null);
//			topPoint += descBitmap.getHeight();
//			}
			cv.drawBitmap(barCodeBitmap, 0, topPoint, null);
		} else {
			Log.e("zerz", "9");
			cv.drawBitmap(barCodeBitmap, 0, topPoint, null);
		}
		//cv.save(Canvas.ALL_SAVE_FLAG);
		cv.save();
		cv.restore();

		return newBitmap;
	}
}