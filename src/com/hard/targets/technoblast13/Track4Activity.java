package com.hard.targets.technoblast13;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class Track4Activity extends Activity {
	
	String url = "file:///android_asset/t4.html";
	String urlUpdate = "http://pulkit636.0fees.net/tb13/updates.html";
	String urlConnect = "file:///android_asset/connect.html";
	
	WebView wvT4, wvU4;
	
	ConnectionDetector cd;
	Boolean isInternetPresent = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_track4);

		setupActionBar();
		
		cd = new ConnectionDetector(getApplicationContext());
		isInternetPresent = cd.isConnected();
		
		wvT4 = (WebView) findViewById(R.id.wvT4);
		wvU4 = (WebView) findViewById(R.id.wvU4);
		
		wvT4.loadUrl(url);
		
		if(isInternetPresent) {
			wvU4.loadUrl(urlUpdate);
		} else {
			wvU4.loadUrl(urlConnect);
		}
		
		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) findViewById(R.id.adViewt4);
		adView.loadAd(new AdRequest());
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setSubtitle("TRACK 4: Game Changers");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.track4, menu);
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
