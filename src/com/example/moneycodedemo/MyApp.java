package com.example.moneycodedemo;


import java.util.ArrayList;

import android.app.Application;
import android.graphics.Bitmap;

public class MyApp extends Application{
	
	private Bitmap bm;
	private String result = null;
	private ArrayList<String> ss = new ArrayList<String>();
	private int total;
	
	public Bitmap getBitmap()
	{
		return bm;
	}
	
	public void setBitmap(Bitmap bitmap)
	{
		bm = bitmap;
	}

	public void setResult(String s)
	{
		result = s;
	}
	
	public String getResult()
	{
		return result;
	}
	
	public void addResult(String s)
	{
		ss.add(s);
	}
	
	public ArrayList<String> getAllResult()
	{
		return ss;
	}
	
	public void setTotal(int i)
	{
		total=i;
	}
	
	public int getTotal()
	{
		return total;
	}
	
	
}
