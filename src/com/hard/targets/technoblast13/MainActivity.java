package com.hard.targets.technoblast13;

import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mListTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTitle = mDrawerTitle = getTitle();
        mListTitles = getResources().getStringArray(R.array.list_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mListTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,  
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_about).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		
		switch(item.getItemId()) {
        case R.id.action_about:
            // create intent to perform web search for this planet
            Intent intent = new Intent("com.hard.targets.technoblast13.ABOUTACTIVITY");
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
			.setIcon(R.drawable.ic_action_alert)
			.setMessage(R.string.dialog_message)
			.setTitle(R.string.dialog_title)
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User clicked OK button
					MainActivity.this.finish();
				}
			})
			.setNegativeButton(R.string.no, null)
			.show();
		}
		return super.onKeyDown(keyCode, event);
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
	
	private void selectItem(int position) {
        Fragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ListFragment.ARG_ITEM_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(mListTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
	
	@Override
    public void setTitle(CharSequence title) {
        //mTitle = title;
        getActionBar().setSubtitle(title);
    }
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    public static class ListFragment extends Fragment implements OnClickListener {
        public static final String ARG_ITEM_NUMBER = "item_number";
        
        int image_index = 0;
	    int MAX_IMAGE_COUNT = 10;
	    
	    Integer[] mImageIds = {
	            R.drawable.i01,
	            R.drawable.i02,
	            R.drawable.i03,
	            R.drawable.i04,
	            R.drawable.i05,
	            R.drawable.i06,
	            R.drawable.i07,
	            R.drawable.i08,
	            R.drawable.i09,
	            R.drawable.i10,
	    };
	    
	    ImageView iv;
	    
	    String urlUpdate = "http://pulkit636.0fees.net/tb13/updates.html";
	    String urlConnect = "file:///android_asset/connect.html";
	    
	    ConnectionDetector cd;
	    Boolean isInternetPresent = false;

        public ListFragment() {
        }

        @SuppressLint("SetJavaScriptEnabled")
		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	cd = new ConnectionDetector(getActivity().getApplicationContext());
    	    isInternetPresent = cd.isConnected();
            View rootView = null;
            AdView adView = null;
            int i = getArguments().getInt(ARG_ITEM_NUMBER);
            switch (i) {
            case 0:
            	rootView = inflater.inflate(R.layout.fragment_l0, container, false);
            	
            	WebView wv1, wv2;
            	String urlHome = "file:///android_asset/home.html";
            	
            	wv1 = (WebView) rootView.findViewById(R.id.wvHome1);
            	wv2 = (WebView) rootView.findViewById(R.id.wvHome2);
            	
            	wv1.getSettings().setJavaScriptEnabled(true);
            	wv1.loadUrl(urlHome);
            	
            	if(isInternetPresent) {
            		wv2.getSettings().setJavaScriptEnabled(true);
                	wv2.loadUrl(urlUpdate);
            	} else {
            		wv2.loadUrl(urlConnect);
            	}
            	
            	// Look up the AdView as a resource and load a request.
				adView = (AdView) rootView.findViewById(R.id.adViewl0);
				adView.loadAd(new AdRequest());
            	
            	break;
			case 1:
				rootView = inflater.inflate(R.layout.fragment_l1, container, false);
				
				WebView wvAbt1 = (WebView) rootView.findViewById(R.id.wvAboutUs1);
				WebView wvAbt2 = (WebView) rootView.findViewById(R.id.wvAboutUs2);
				
				String urlAbt = "file:///android_asset/about.html";
				
				wvAbt1.getSettings().setJavaScriptEnabled(true);
				wvAbt1.loadUrl(urlAbt);
				
				if(isInternetPresent) {
					wvAbt2.getSettings().setJavaScriptEnabled(true);
					wvAbt2.loadUrl(urlUpdate);
				} else {
					wvAbt2.loadUrl(urlConnect);
				}
				
				// Look up the AdView as a resource and load a request.
				adView = (AdView) rootView.findViewById(R.id.adViewl1);
				adView.loadAd(new AdRequest());
				
				break;
			case 2:
				rootView = inflater.inflate(R.layout.fragment_l2, container, false);
				
				final ListView listTrack = (ListView) rootView.findViewById(R.id.listTrack);
				String[] items = {"Professor Xaviers (CSE & IT)", "Transformers (ECE & EEE)", "Wolverine (ME & Civil)", "Game Changers (MBA)"};
				LinkedList<String> p = new LinkedList<String>();
				for(String k:items) {
					p.add(k);
				}
				ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, p);
				listTrack.setAdapter(aa);
				listTrack.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent;
						String track = listTrack.getItemAtPosition(arg2).toString();
						if(track.equals("Professor Xaviers (CSE & IT)")) {
							intent = new Intent("com.hard.targets.technoblast13.TRACK1ACTIVITY");
							startActivity(intent);
						} else if(track.equals("Transformers (ECE & EEE)")) {
							intent = new Intent("com.hard.targets.technoblast13.TRACK2ACTIVITY");
							startActivity(intent);
						} else if(track.equals("Wolverine (ME & Civil)")) {
							intent = new Intent("com.hard.targets.technoblast13.TRACK3ACTIVITY");
							startActivity(intent);
						} else if(track.equals("Game Changers (MBA)")) {
							intent = new Intent("com.hard.targets.technoblast13.TRACK4ACTIVITY");
							startActivity(intent);
						}
					}
				});
				
				WebView wvTrack = (WebView) rootView.findViewById(R.id.wvTracks);
				
				if(isInternetPresent) {
					wvTrack.getSettings().setJavaScriptEnabled(true);
					wvTrack.loadUrl(urlUpdate);
				} else {
					wvTrack.loadUrl(urlConnect);
				}
				
				// Look up the AdView as a resource and load a request.
				adView = (AdView) rootView.findViewById(R.id.adViewl2);
				adView.loadAd(new AdRequest());
				break;
			case 3:				
				rootView = inflater.inflate(R.layout.fragment_l3, container, false);
				
				WebView wvReg = (WebView) rootView.findViewById(R.id.wvRegister);
				wvReg.getSettings().setJavaScriptEnabled(true);
				wvReg.loadUrl("file:///android_asset/register.html");
				
				TextView tvReg = (TextView) rootView.findViewById(R.id.tvRegister);
				
				Button register = (Button) rootView.findViewById(R.id.bRegister);
				register.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String url = "http://technoblast.somee.com/registration.aspx";
						Intent i1 = new Intent(Intent.ACTION_VIEW);
						i1.setData(Uri.parse(url));
						startActivity(i1);
					}
				});
				
				String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
				String[] d = currentDateTimeString.split(" ");
				int date = Integer.parseInt(d[0]);
				int month = 0;
				String mn = d[1];
				if(mn.equals("Jan")) {
					month = 1;
				} else if(mn.equals("Feb")) {
					month = 2;
				} else if(mn.equals("Mar")) {
					month = 3;
				} else if(mn.equals("Apr")) {
					month = 4;
				} else if(mn.equals("May")) {
					month = 5;
				} else if(mn.equals("Jun")) {
					month = 6;
				} else if(mn.equals("Jul")) {
					month = 7;
				} else if(mn.equals("Aug")) {
					month = 8;
				} else if(mn.equals("Sep")) {
					month = 9;
				} else if(mn.equals("Oct")) {
					month = 10;
				} else if(mn.equals("Nov")) {
					month = 11;
				} else if(mn.equals("Dec")) {
					month = 12;
				}
				int year = Integer.parseInt(d[2]);
				if(date > 8 && (month > 11 || year > 2013)) {
					register.setVisibility(View.INVISIBLE);
					tvReg.setText("REGISTRATIONS CLOSED");
				}
				
				// Look up the AdView as a resource and load a request.
				adView = (AdView) rootView.findViewById(R.id.adViewl3);
				adView.loadAd(new AdRequest());
				break;
			case 4:
				rootView = inflater.inflate(R.layout.fragment_l4, container, false);
				
				String urlsponsors = "http://pulkit636.0fees.net/tb13/sponsor.html";
				String urlFail = "file:///android_asset/connect1.html";
				
				WebView wvSponsors = (WebView) rootView.findViewById(R.id.wvSponsors1);
				WebView wvSponsors1 = (WebView) rootView.findViewById(R.id.wvSponsors2);
				if(isInternetPresent) {
					wvSponsors.getSettings().setJavaScriptEnabled(true);
					wvSponsors.loadUrl(urlsponsors);
					wvSponsors1.getSettings().setJavaScriptEnabled(true);
					wvSponsors1.loadUrl(urlUpdate);
				} else {
					wvSponsors.loadUrl(urlFail);
					wvSponsors1.loadUrl(urlFail);
				}
				
				// Look up the AdView as a resource and load a request.
				adView = (AdView) rootView.findViewById(R.id.adViewl4);
				adView.loadAd(new AdRequest());
				
				break;
			case 5:
				rootView = inflater.inflate(R.layout.fragment_l5, container, false);
				
				String urlprize = "file:///android_asset/prize.html";
				
				WebView prize = (WebView) rootView.findViewById(R.id.wvPrize);
				prize.getSettings().setJavaScriptEnabled(true);
				prize.loadUrl(urlprize);
				
				WebView prize2 = (WebView) rootView.findViewById(R.id.wvPrizes);
				if(isInternetPresent) {
					prize2.getSettings().setJavaScriptEnabled(true);
					prize2.loadUrl(urlUpdate);
				} else {
					prize2.loadUrl(urlConnect);
				}
				
				// Look up the AdView as a resource and load a request.
				adView = (AdView) rootView.findViewById(R.id.adViewl5);
				adView.loadAd(new AdRequest());
				break;
			case 6:
				rootView = inflater.inflate(R.layout.fragment_l6, container, false);
				
				iv = (ImageView) rootView.findViewById(R.id.imageView1);
				Button prev, next;
				prev = (Button) rootView.findViewById(R.id.bPrev);
				next = (Button) rootView.findViewById(R.id.bNext);
				prev.setOnClickListener(this);
				next.setOnClickListener(this);
				
				// Look up the AdView as a resource and load a request.
				adView = (AdView) rootView.findViewById(R.id.adViewl6);
				adView.loadAd(new AdRequest());
				break;
			case 7:
				rootView = inflater.inflate(R.layout.fragment_l7, container, false);
				
				WebView wvTb2012 = (WebView) rootView.findViewById(R.id.wvTb2012);
				wvTb2012.getSettings().setJavaScriptEnabled(true);
				wvTb2012.loadUrl("file:///android_asset/tb2012.html");
				
				// Look up the AdView as a resource and load a request.
				adView = (AdView) rootView.findViewById(R.id.adViewl7);
				adView.loadAd(new AdRequest());
				break;
			case 8:
				rootView = inflater.inflate(R.layout.fragment_l8, container, false);
				
				String urlmap = "file:///android_asset/map.html";
				
				WebView map = (WebView) rootView.findViewById(R.id.wvMap);
				map.getSettings().setJavaScriptEnabled(true);
				map.loadUrl(urlmap);
				
				final TextView tvCM1, tvCM2, tvCM3, tvCM4, tvCM5;
				
				tvCM1 = (TextView) rootView.findViewById(R.id.tvCM1);
				tvCM2 = (TextView) rootView.findViewById(R.id.tvCM2);
				tvCM3 = (TextView) rootView.findViewById(R.id.tvCM3);
				tvCM4 = (TextView) rootView.findViewById(R.id.tvCM4);
				tvCM5 = (TextView) rootView.findViewById(R.id.tvCM5);
				
				tvCM1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String no = tvCM1.getText().toString();
						Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + no));
						startActivity(dial);
					}
				});
				
				tvCM2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String no = tvCM2.getText().toString();
						Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + no));
						startActivity(dial);
					}
				});

				tvCM3.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String no = tvCM3.getText().toString();
						Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + no));
						startActivity(dial);
					}
				});

				tvCM4.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String no = tvCM4.getText().toString();
						Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + no));
						startActivity(dial);
					}
				});

				tvCM5.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String no = tvCM5.getText().toString();
						Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + no));
						startActivity(dial);
					}
				});
				
				final TextView tvCS1, tvCSM1, tvCS2, tvCSM2, tvCS3, tvCSM3, tvCS4, tvCSM4, tvCS5, tvCSM5;
				
				tvCS1 = (TextView) rootView.findViewById(R.id.tvCS1);
				tvCSM1 = (TextView) rootView.findViewById(R.id.tvCSM1);
				tvCS2 = (TextView) rootView.findViewById(R.id.tvCS2);
				tvCSM2 = (TextView) rootView.findViewById(R.id.tvCSM2);
				tvCS3 = (TextView) rootView.findViewById(R.id.tvCS3);
				tvCSM3 = (TextView) rootView.findViewById(R.id.tvCSM3);
				tvCS4 = (TextView) rootView.findViewById(R.id.tvCS4);
				tvCSM4 = (TextView) rootView.findViewById(R.id.tvCSM4);
				tvCS5 = (TextView) rootView.findViewById(R.id.tvCS5);
				tvCSM5 = (TextView) rootView.findViewById(R.id.tvCSM5);
				
				tvCS1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String u = "https://www.facebook.com/akhil.narayanan.7";
						Intent p = new Intent(Intent.ACTION_VIEW);
						p.setData(Uri.parse(u));
						startActivity(p);
					}
				});
				
				tvCSM1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String m = tvCSM1.getText().toString();
						Intent d = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + m));
						startActivity(d);
					}
				});

				tvCS2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String u = "https://www.facebook.com/gopal.gupta.18041";
						Intent p = new Intent(Intent.ACTION_VIEW);
						p.setData(Uri.parse(u));
						startActivity(p);
					}
				});
				
				tvCSM2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String m = tvCSM2.getText().toString();
						Intent d = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + m));
						startActivity(d);
					}
				});

				tvCS3.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String u = "https://www.facebook.com/jyoti.raghav.31";
						Intent p = new Intent(Intent.ACTION_VIEW);
						p.setData(Uri.parse(u));
						startActivity(p);
					}
				});
				
				tvCSM3.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String m = tvCSM3.getText().toString();
						Intent d = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + m));
						startActivity(d);
					}
				});

				tvCS4.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String u = "https://www.facebook.com/kshitij.salariya";
						Intent p = new Intent(Intent.ACTION_VIEW);
						p.setData(Uri.parse(u));
						startActivity(p);
					}
				});
				
				tvCSM4.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String m = tvCSM4.getText().toString();
						Intent d = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + m));
						startActivity(d);
					}
				});

				tvCS5.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String u = "https://www.facebook.com/pulkit63";
						Intent p = new Intent(Intent.ACTION_VIEW);
						p.setData(Uri.parse(u));
						startActivity(p);
					}
				});
				
				tvCSM5.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String m = tvCSM5.getText().toString();
						Intent d = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + m));
						startActivity(d);
					}
				});
				
				// Look up the AdView as a resource and load a request.
				adView = (AdView) rootView.findViewById(R.id.adViewl8);
				adView.loadAd(new AdRequest());
				break;
			}
            return rootView;
        }

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bPrev:
				image_index--;
				if (image_index == -1) {                    
                    image_index = MAX_IMAGE_COUNT - 1;                  
                }
				iv.setImageResource(mImageIds[image_index]);
				break;
			case R.id.bNext:
				image_index++;
				if (image_index == MAX_IMAGE_COUNT) {               
	                image_index = 0;                
	            }
				iv.setImageResource(mImageIds[image_index]);
				break;
			default:
				break;
			}
		}
    }

}
