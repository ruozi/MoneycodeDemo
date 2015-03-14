package com.example.moneycodedemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	private static final String TAG = "Main::Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button_login = (Button)findViewById(R.id.imageButton_login);
		button_login.setOnClickListener(
	        	new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d(TAG,"Click!");									

				}
	        	}
	        );
	}
}
