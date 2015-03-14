package com.example.moneycodedemo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.util.Log;

public class MoneyCodeProcesser {
	private static final String TAG = "Processer";
	private static final double minarea = 100;

	private Bitmap mBitmap;
	private Mat mSrc;
	private Mat mBin;
	private Bitmap mBinBitmap;

	public MoneyCodeProcesser(Bitmap bm) {
		mBitmap = bm;
		mSrc = new Mat();
		Utils.bitmapToMat(mBitmap, mSrc,false);
		mBin = new Mat(mSrc.height(), mSrc.width(), CvType.CV_8UC1, new Scalar(
				255, 255, 255));
	}

	public void process() {
		// Cache
		List<Mat> mv = new ArrayList<Mat>();
		Mat mv_green = new Mat();
		Mat green_closing = new Mat();
		Mat hierarchy = new Mat();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

		// Threshold red channel
		Core.split(mSrc, mv);
		Imgproc.threshold(mv.get(1), mv_green, 80, 255,
				Imgproc.THRESH_BINARY_INV);

		// Closing
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,
				new Size(2, 2));
		Imgproc.morphologyEx(mv_green, green_closing, Imgproc.MORPH_CLOSE,
				kernel);
		Log.d(TAG, "closing operation!");

		// contours
		Imgproc.findContours(green_closing, contours, hierarchy, Imgproc.RETR_CCOMP,
				Imgproc.CHAIN_APPROX_SIMPLE);
		Log.d(TAG, "find " + Integer.toString(contours.size()) + " contours");

		// delete area smaller than minimum area
		Iterator<MatOfPoint> iter = contours.iterator();
		while (iter.hasNext()) {
			MatOfPoint contour_single = iter.next();
			double area = Math.abs(Imgproc.contourArea(contour_single));
			if (area < minarea) {
				iter.remove();
			}
		}
		Log.d(TAG, "contours deleted!");

		// draw contours
		Imgproc.drawContours(mBin, contours, -1, new Scalar(0, 0, 0),
				Core.FILLED);
		Log.d(TAG, "contours draw");
		
		
		//Debug
//		mBin = mv_green;
		
		//Mat2Bitmap
		binMat2Bitmap();
		Log.d(TAG,"Mat2Bitmap");
		

	}
	
	private void binMat2Bitmap()
	{
		 Mat mBgra = new Mat(mBin.height(), mBin.width(), CvType.CV_8UC4);
		 Imgproc.cvtColor(mBin, mBgra, Imgproc.COLOR_GRAY2RGBA);
		 mBinBitmap = Bitmap.createBitmap(mBgra.cols(), mBgra.rows(), Bitmap.Config.ARGB_8888);
		 Utils.matToBitmap(mBgra, mBinBitmap);
	}
	
	public Mat getBinResultMat()
	{
		return mBin;
	}
	
	public Bitmap getBinResultBitmap()
	{
		return mBinBitmap;
	}

}
