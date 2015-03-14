package com.example.moneycodedemo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class OCRUtils {

	private static final String TAG = "OCRUtils";

	public static Boolean SaveBitmap(String filepath,Bitmap bm)
	{	
		File savefile = new File(filepath);
    	try {
			savefile.createNewFile();
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(savefile));
			bm.compress(Bitmap.CompressFormat.PNG, 100, os);
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return true;
	}
	
	public static String SaveBitmap(Bitmap bm)
	{	
		SimpleDateFormat sDateFormat = new SimpleDateFormat("hh-mm-ss",Locale.getDefault());
		String time = sDateFormat.format(new java.util.Date());
		String filepath = Environment.getExternalStorageDirectory().getPath()+"/savepic/"+time+".png";
		Log.d(TAG, "write file to "+filepath);
		
		File savefile = new File(filepath);
    	try {
			savefile.createNewFile();
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(savefile));
			bm.compress(Bitmap.CompressFormat.PNG, 100, os);
			os.flush();
			os.close();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return filepath;
	}
	
	public static String SaveBitmap(Mat m)
	{	
		SimpleDateFormat sDateFormat = new SimpleDateFormat("hh-mm-ss",Locale.getDefault());
		String time = sDateFormat.format(new java.util.Date());
		String filepath = Environment.getExternalStorageDirectory().getPath()+"/savepic/"+time+".bmp";
		Log.d(TAG, "write file to "+filepath);
		
		File savefile = new File(filepath);
    	try {
			savefile.createNewFile();
			Highgui.imwrite(filepath, m);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return filepath;
	}
	
	public static String ParseMoneyCode(String s)
	{
		Log.d(TAG,"string s:"+s);
		s=s.replace(" ", "");
		String result = null;
		Log.d(TAG,"s.length():"+Integer.toString(s.length()));
		
		if(s.length()<10)
			s = s.replace("H", "11");
		
		//Process
		if(s.length()==10)
		{
			try {
				String first=s.substring(0,1).replace("0", "O");
				String head=s.substring(1,4);
				String digits=s.substring(4,10).replace("I", "1").replace("B", "8");
				
				Log.d(TAG,"first:"+first);
				Log.d(TAG,"head:"+head);
				Log.d(TAG,"digits:"+digits);
				//statistic
				for(int i=0;i<3;i++){
					char c = head.charAt(i);
					if((c>='A'&&c<='Z')&&c!='I'){
						result = first+head.replace("I", "1")+digits;
						Log.d(TAG,"Break1!!");
						return result;
					}
				}
				
				for(int i=0;i<3;i++){
					char c = head.charAt(i);
					if (c=='I'||c=='0'){
						head = head.substring(0,i)+head.substring(i,i+1).replace("0", "O")+head.substring(i+1,3);
						Log.d(TAG,"Break2!!");
						break;	
					}
				}
				
				result=first+head+digits;
				Log.d(TAG,"result:"+result);
			} catch (IndexOutOfBoundsException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		else {
			result = s;
		}
		return result;
	}

	public static String BytesToHexString(byte[] src,int size)
	{
		StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || size <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < size; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString(); 
	}
	
	public static String BytesToHexString(byte[] src)
	{
		StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString(); 
	}
	
	@SuppressLint("DefaultLocale")
	public static byte[] hexStringToBytes(String hexString) {  
	    if (hexString == null || hexString.equals("")) {  
	        return null;  
	    }  
	    hexString = hexString.toUpperCase();  
	    int length = hexString.length() / 2;  
	    char[] hexChars = hexString.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) ("0123456789ABCDEF".indexOf(hexChars[pos]) << 4 | "0123456789ABCDEF".indexOf(hexChars[pos + 1]));  
	    }  
	    return d;  
	}
	
	public static String DoOCR(Bitmap bm)
	{
		TessBaseAPI baseApi;
		String text;
		final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath()+"/";
		
		baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		
		baseApi.init(SDCARD_PATH+"tesseract", "money");
		baseApi.setVariable("tessedit_char_whitelist", "0123456789ABCDEFGHIJKLMNPQRSTUVWXYZ");
		baseApi.setImage(bm);
		
		text= baseApi.getUTF8Text();
		text= OCRUtils.ParseMoneyCode(text);
		
		baseApi.clear();
		baseApi.end();
		
		return text;
	}
	
	public static byte DoLRC(byte[] b)
	{
		byte lrc = 0x00;
		
		for(int i=1;i<b.length;i++)
		{
			lrc = (byte) (lrc ^ b[i]);
		}
		
		return lrc;
	}
	
	public static byte DoLRCFull(byte[] b)
	{
		byte lrc = 0x00;
		
		for(int i=0;i<b.length;i++)
		{
			lrc = (byte) (lrc ^ b[i]);
		}
		
		return lrc;
	}
}
