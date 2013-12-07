package com.hard.targets.technoblast13;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity implements Runnable {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		getActionBar().hide();
		
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(2500);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
	}

}
