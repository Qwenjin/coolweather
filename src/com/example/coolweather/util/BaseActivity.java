package com.example.coolweather.util;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {
	public  static  final  String  TAG = "BaseActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, this.toString()+" OnCreate ");
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
	}
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, this.toString()+" onDestroy ");
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

}
