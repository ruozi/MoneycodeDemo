package com.example.moneycodedemo;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class OcrActivity extends Activity {

	private static final String TAG = "Ocr::Activity";
	private MyApp app;
	private CameraPreview mCameraPreview;
	
	private Camera mCamera = null; 
	
	/** 安全获取Camera对象实例的方法*/ 
	public static Camera getCameraInstance(){ 
	    Camera c = null; 
	    try { 
	        c = Camera.open(0); // 试图获取Camera实例
	    } 
	    catch (Exception e){ 
	        // 摄像头不可用（正被占用或不存在）
	    } 
	    return c; // 不可用则返回null
	}
	
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
    	
    	@Override
    	public void onManagerConnected(int status){
    		switch (status) {
			case LoaderCallbackInterface.SUCCESS:
			{
				Log.i(TAG,"OpenCV loaded successfully");
			}
				break;
			default:
			{
				super.onManagerConnected(status);
			}
				break;
			}
    	}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ocr);
		
		// Get Myapp
		app = ((MyApp)getApplicationContext());
						
		// 创建Camera实例 
		mCamera = getCameraInstance();
		Log.d(TAG,"mCamera get!");
		
		// Add Camera Preview to FrameLayout
		mCameraPreview = new CameraPreview(this, mCamera); 
		FrameLayout preview = (FrameLayout) findViewById(R.id.frame_layout); 
		preview.addView(mCameraPreview);
				
		Button button_commit = (Button)findViewById(R.id.button_commit);
		button_commit.setOnClickListener(
	        	new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d(TAG,"Click!");	
					
					Intent intent = new Intent();
					intent.setClass(OcrActivity.this, ResultActivity.class);
					startActivity(intent);
				}
	        	}
	        );
		
	}
	
	@Override
	protected void onResume(){
		Log.i(TAG,"onResume");
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this, mLoaderCallback);

	}
	
	@Override
	protected void onStart(){
		Log.i(TAG,"onStart");
		super.onStart();	
	}
	
	@Override
	protected void onPause(){
		Log.i(TAG,"onPause");
		super.onStart();	
	}


}
