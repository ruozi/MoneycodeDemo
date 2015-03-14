package com.example.moneycodedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class OcrActivity extends Activity {

	private static final String TAG = "Ocr::Activity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ocr);
		
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


}
