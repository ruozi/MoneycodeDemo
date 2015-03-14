package com.example.moneycodedemo;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		TextView tv_result = (TextView)findViewById(R.id.textView_ocr_result);
		TextView tv_number = (TextView)findViewById(R.id.textView_ocr_number);
		
		// Get Myapp
		MyApp app = ((MyApp)getApplicationContext());
		ArrayList<String> result = app.getAllResult();
		int number = app.getTotal()*100;
		
		//set result
		tv_number.setText("交易金额："+number+".00元");
		
		Iterator<String> it = result.iterator();
		while(it.hasNext())
		{
			tv_result.setText(tv_result.getText()+" "+it.next());
		}
	}
}
