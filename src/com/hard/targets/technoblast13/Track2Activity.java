package com.hard.targets.technoblast13;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class Track2Activity extends Activity {
	
	String url = "file:///android_asset/t2.html";
	String urlUpdate = "http://pulkit636.0fees.net/tb13/updates.html";
	String urlConnect = "file:///android_asset/connect.html";
	
	WebView wvT2, wvU2;
	
	ConnectionDetector cd;
	Boolean isInternetPresent = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_track2);

		setupActionBar();
		
		cd = new ConnectionDetector(getApplicationContext());
		isInternetPresent = cd.isConnected();
		
		wvT2 = (WebView) findViewById(R.id.wvT2);
		wvU2 = (WebView) findViewById(R.id.wvU2);
		
		wvT2.loadUrl(url);
		
		if(isInternetPresent) {
			wvU2.loadUrl(urlUpdate);
		} else {
			wvU2.loadUrl(urlConnect);
		}
		
		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) findViewById(R.id.adViewt2);
		adView.loadAd(new AdRequest());
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setSubtitle("TRACK 2: Transformers");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.track2, menu);
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
