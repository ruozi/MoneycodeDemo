package com.example.moneycodedemo;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class OcrActivity extends Activity {

	private static final String TAG = "Ocr::Activity";
	private MyApp app;
	private CameraPreview mCameraPreview;
	private TextView tv_order;
	private EditText et_result;
	private int current_order = 1;
	
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
		
		
		// Add Camera Preview to FrameLayout
		mCameraPreview = new CameraPreview(this); 
		FrameLayout preview = (FrameLayout) findViewById(R.id.frame_layout); 
		preview.addView(mCameraPreview);
		
		// Get order&text
		tv_order=(TextView)findViewById(R.id.ocr_order);
		et_result=(EditText)findViewById(R.id.ocr_result);

		// Set clicklistener to button_commit
		Button button_commit = (Button)findViewById(R.id.button_commit);
		button_commit.setOnClickListener(
	        	new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d(TAG,"Click!");	
					
					app.setTotal(current_order-1);
					Intent intent = new Intent();
					intent.setClass(OcrActivity.this, ResultActivity.class);
					startActivity(intent);
				}
	        	}
	        );
		
		// Set clicklistener to button_take_photo
		Button button_take_photo = (Button)findViewById(R.id.button_take_photo);
		button_take_photo.setOnClickListener(
			new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG,"Click!");	
				
				if(mCamera!=null){
	                mCamera.takePicture(null, null, mPicture); 
				}
			}
			}
		);
		
		// Set clicklistener to button_confirm
				Button button_confirm = (Button)findViewById(R.id.button_confirm);
				button_confirm.setOnClickListener(
					new View.OnClickListener() {			
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.d(TAG,"Click!");	
							
						current_order++;
						app.addResult(et_result.getText().toString());
						et_result.setText(null);
						tv_order.setText("第"+current_order+"张");
						}
					}
					
				);
	}
	
	@Override
	protected void onResume(){
		Log.i(TAG,"onResume");
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this, mLoaderCallback);
		
		// 创建Camera实例 
		mCamera = getCameraInstance();
		if(mCamera!=null){
			Log.d(TAG,"mCamera get!");
			mCameraPreview.setCamera(mCamera);
		}
		else {
			Log.d(TAG,"mCamera NULL!！！！！！！");
		}

	}
		
	@Override
	protected void onPause(){
		Log.i(TAG,"onPause --> Realease camera");
		super.onPause();
		if(null!=mCamera){
			mCameraPreview.setCamera(null);
			mCamera.setPreviewCallback(null) ;
			mCamera.stopPreview();
			mCamera.release();
			mCamera=null;
		}
	}
	
	@Override
	protected void onStop(){
		Log.i(TAG,"onStop");
		super.onStop();
		}

	private PictureCallback mPicture = new PictureCallback() { 
		 
	    @Override 
	    public void onPictureTaken(byte[] data, Camera camera) { 
	    	Log.d(TAG,"onPicture");
	    	
	    	Bitmap bm0 = BitmapFactory.decodeByteArray(data, 0, data.length);
	    	
	    	
	    	Matrix matrix = new Matrix();
	    	matrix.setRotate(90,(float)bm0.getWidth()/2, (float)bm0.getHeight()/2);
	    	Bitmap bm1 = Bitmap.createBitmap(bm0, 0, 0, bm0.getWidth(), bm0.getHeight(), matrix, true);
	    	Bitmap mBitmap = Bitmap.createBitmap(bm1, 50, 270, 380, 100);
	    	
	    	MoneyCodeProcesser processer = new MoneyCodeProcesser(mBitmap);
	    	processer.process();
	    	Bitmap mBinBitmap = processer.getBinResultBitmap();

	    	
	    	app.setBitmap(mBinBitmap);
	    	
			String text = OCRUtils.DoOCR(mBinBitmap);
			app.setResult(text);
			
			et_result.setText(text);

//			Intent intent = new Intent();
//			intent.setClass(OcrActivity.this, AlgodebugActivity.class);
//			startActivity(intent);
	    } 
	};

}
