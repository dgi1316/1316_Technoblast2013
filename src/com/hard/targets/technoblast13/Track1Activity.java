package com.hard.targets.technoblast13;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class Track1Activity extends Activity {
	
	String urlL = "file:///android_asset/t1.html";
	String urlUpdate = "http://pulkit636.0fees.net/tb13/updates.html";
	String urlConnect = "file:///android_asset/connect.html";
	
	WebView wvT1, wvU1;
	
	ConnectionDetector cd;
	Boolean isInternetPresent = false;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_track1);

		setupActionBar();
		
		cd = new ConnectionDetector(getApplicationContext());
		isInternetPresent = cd.isConnected();
		
		wvT1 = (WebView) findViewById(R.id.wvT1);
		wvU1 = (WebView) findViewById(R.id.wvU1);
		
		wvT1.getSettings().setJavaScriptEnabled(true);
		wvT1.loadUrl(urlL);
		
		if(isInternetPresent) {
			wvU1.loadUrl(urlUpdate);
		} else {
			wvU1.loadUrl(urlConnect);
		}
		
		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) findViewById(R.id.adViewt1);
		adView.loadAd(new AdRequest());
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setSubtitle("TRACK 1: Professor Xaviers (CSE & IT)");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.track1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//NavUtils.navigateUpFromSameTask(this);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
