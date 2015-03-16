package com.example.moneycodedemo;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback { 
    private static final String TAG = "CameraPreview";
	private SurfaceHolder mHolder; 
    private Camera mCamera; 
 
    public CameraPreview(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        Log.d(TAG,"CameraPreviewInitial!!!!!!");
        init();
    }

    public CameraPreview(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        Log.d(TAG,"CameraPreviewInitial!!!!!!");
        init();
    }

    public CameraPreview(Context context)
    {
        super(context);
        Log.d(TAG,"CameraPreviewInitial!!!!!!");
        init();
    }
    
    private void init()
    {
        Log.d(TAG, "CameraPreview initialize");

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.setKeepScreenOn(true);
        mHolder.addCallback(this);
        this.setFocusable(true);
    }
    
    public void setCamera(Camera camera)
    {
        mCamera = camera;
    }
 
    public void surfaceCreated(SurfaceHolder holder) { 
        // surface�ѱ����������ڰ�Ԥ�������λ��֪ͨ����ͷ
    	Log.d(TAG,"SurfaceCreated!!!!!!");
        try { 
        	if(null!=mCamera){
		        Parameters para=mCamera.getParameters();
		        para.setFlashMode(Parameters.FLASH_MODE_OFF);     
		        para.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		        int height=para.getPreviewSize().height;
		        int width=para.getPreviewSize().width;
		        para.setPreviewSize(height, width);
		        para.setPictureSize(height, width); 
	            mCamera.setParameters(para);
	            mCamera.setPreviewDisplay(holder);
	            mCamera.setDisplayOrientation(90); 
	            mCamera.startPreview();
	            mCamera.cancelAutoFocus();
        	}
        } catch (Exception e) { 
            Log.d(TAG, "Error setting camera preview: " + e.getMessage()); 
        } 
    	Log.d(TAG,"Camera parameters set!!!!!!");
    } 
 
    public void surfaceDestroyed(SurfaceHolder holder) { 
        // �մ��롣ע����activity���ͷ�����ͷԤ������
    	Log.d(TAG,"SurfaceDestroyed!!!!!!");
//    	mCamera.setPreviewCallback(null);
    	} 
 
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) { 
        // ���Ԥ���޷����Ļ���ת��ע��˴����¼�
        // ȷ�������Ż�����ʱֹͣԤ��
    	Log.d(TAG,"SurfaceChanged!!!!!!");
        if (mHolder.getSurface() == null){ 
          // Ԥ��surface������
          return; 
        } 
 
        // ����ʱֹͣԤ�� 
        try { 
            mCamera.stopPreview(); 
        } catch (Exception e){ 
          // ���ԣ���ͼֹͣ�����ڵ�Ԥ��
        } 
 
        // �ڴ˽������š���ת��������֯��ʽ
 
        // ���µ���������Ԥ��
        try { 
            mCamera.setPreviewDisplay(mHolder); 
            mCamera.startPreview(); 
 
        } catch (Exception e){ 
            Log.d(TAG, "Error starting camera preview: " + e.getMessage()); 
        } 
    } 


}