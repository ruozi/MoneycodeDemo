package com.example.moneycodedemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class AlgodebugActivity extends Activity {

	private MyApp app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_algodebug);
		
		// Get Myapp
		app = ((MyApp)getApplicationContext());
		Bitmap bm=app.getBitmap();
		
		ImageView iv = (ImageView)findViewById(R.id.codeimage);
		iv.setImageBitmap(bm);
		
		TextView tv=(TextView)findViewById(R.id.textView);
		tv.setText(app.getResult());
	}

}
