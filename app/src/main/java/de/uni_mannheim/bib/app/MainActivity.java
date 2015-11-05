/*
 * Copyright (C) 2014 Universitätsbibliothek Mannheim
 *
 * Author:
 *    Alexander Wagner <alexander.wagner@bib.uni-mannheim.de>
 * 
 * 
 * This is free software licensed under the terms of the GNU GPL, 
 * version 3, or (at your option) any later version.
 * See <http://www.gnu.org/licenses/> for more details.
 *
 *
 * Starts app, initializes configuration and shows menu items.
 * or forwards to StartUpActivity (module switcher).
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import java.util.ArrayList;
import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

// http://www.caveofprogramming.com/uncategorized/custom-gridview-with-imageview-and-textview-in-android/

public class MainActivity extends ActionBarActivity {

	private boolean log_enabled = false;

	GridView gv;
	Context context;

	ArrayList<?> prgmName;

	// public static String[] prgmNameList = { "Website", "Katalog", "Aktuelles",
	public static String[] prgmNameList = { "Website", "Primo", "News",
			"Freie Pl�tze" };

	/*
	public static int[] prgmImages = { R.drawable.lila_dunkel_www_rund,
			R.drawable.blau_dunkel_cat_rund, R.drawable.rot_news_rund,
			R.drawable.orange_dunkel_stat_rund };
	*/
	public static int[] prgmImages = { R.drawable.lila_dunkel_www,
		R.drawable.blau_dunkel_cat, R.drawable.rot_news,
		R.drawable.orange_dunkel_stat };
	
	private static long back_pressed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		ActivityRegistry.register(this);
		
		if(log_enabled) {
			Log.e( this.getClass().getName().toUpperCase().toString(), " ... LOADED");
		}
		
		// Toast.makeText(getApplicationContext(), this.getClass().getName(), 
				// Toast.LENGTH_SHORT).show();
		
		// screen layout for main activity
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// customized actionbar (color, title)
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#990000")));
		actionBar.setTitle("UB Mannheim");

		// ONLY DEBUG
		SharedPreferences settings = getSharedPreferences("preferences", 0);
		// settings.edit().clear().commit();

		// init configuration
		initConfig();

		// if custom_startup configured
		boolean custom_startup = issetStartUp();
		if (custom_startup) {

			SharedPreferences.Editor preferencesEditor = settings.edit();

			String mode = settings.getString("Config_AppState", ""); // startup,
																	 // running,
																	 // shutdown

			if(log_enabled) {
				Log.e(this.getClass().toString(), "AppState : " + mode);
			}
			
			if ((mode.equals("")) || (mode.equals("shutdown"))) {
				preferencesEditor.putString("Config_AppState", "startup");
				preferencesEditor.commit();

				Intent myIntent = new Intent();
				myIntent = new Intent(MainActivity.this, StartUpActivity.class);
				MainActivity.this.startActivity(myIntent);

// Log.e(this.getClass().toString(), "goto Startup");

			} else {
				// for example running
				gv = (GridView) findViewById(R.id.gridView1);
				gv.setAdapter(new MainAdapter(this, prgmNameList, prgmImages));
			}

		} else {

			gv = (GridView) findViewById(R.id.gridView1);
			gv.setAdapter(new MainAdapter(this, prgmNameList, prgmImages));

		}

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Log.v("onOptionsItemSelected","called:"+item.getGroupId());

		// switch (item.getGroupId()) {
		switch (item.getItemId()) {
		// case 0:
		case R.id.action_config:
			Intent myIntent0 = new Intent(MainActivity.this,
					ConfigActivity.class);
			MainActivity.this.startActivity(myIntent0);
			return true;
			// case 1:
		case R.id.action_help:
			Intent myIntent1 = new Intent(MainActivity.this, HelpActivity.class);
			MainActivity.this.startActivity(myIntent1);
			return true;
			// case 0: Intent myIntent =
			// new Intent(MainActivity.this, TestDBActivity2.class);
			// MainActivity.this.startActivity(myIntent);
			// return true;

			// GOBACK
			// http://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
		}

		return super.onOptionsItemSelected(item);
	}

	protected void initConfig() {

		// initialize shared preferences
		SharedPreferences settings = getSharedPreferences("preferences", 0);
		SharedPreferences.Editor preferencesEditor = settings.edit();

		// set default preferences
		String cfgChanged = settings.getString("Config_ParamsChanged", null);

		if (cfgChanged == null) {

			// initial creation
			preferencesEditor.putString("Config_ParamsChanged", "false");

			preferencesEditor.putString("NetworkConnectionAvailable", "true");
			preferencesEditor.putString("Config_DatabaseModeOn", "false");
			preferencesEditor.putString("Config_NewsRange", "5");
			preferencesEditor.putString("Config_LoadInterval", "1");
			preferencesEditor.commit();

// Log.e("MainActivity", "Configuration created");

		} else {
// Log.e("MainActivity", "Configuration already exists");
		}

	}

	protected boolean issetStartUp() {

		boolean startup = false;

		SharedPreferences settings = getSharedPreferences("preferences", 0);

		String cfg = settings.getString("Config_StartUpActivity", null);

		if (cfg != null) {
			startup = true;
		}

		return startup;

	}

	/*
	 * protected void switchToActivity() {
	 * 
	 * // Values: // 0 = Main // 1 = Website // 2 = Primo // 3 = News // 4 =
	 * Auslastung
	 * 
	 * 
	 * // initialize shared preferences SharedPreferences settings =
	 * getSharedPreferences("preferences", 0);
	 * 
	 * // set default preferences int startup_id =
	 * Integer.valueOf(settings.getString("Config_StartUpActivity", "0"));
	 * Intent myIntent;
	 * 
	 * switch (startup_id) {
	 * 
	 * case 0: myIntent = new Intent(MainActivity.this, MainActivity.class);
	 * MainActivity.this.startActivity(myIntent); break; case 1:
	 * openWebView("Webseite"); break; case 2: openWebView("Katalog"); break;
	 * case 3: myIntent = new Intent(MainActivity.this, BlogActivity.class);
	 * MainActivity.this.startActivity(myIntent); break; case 4: myIntent = new
	 * Intent(MainActivity.this, LoadActivity.class);
	 * MainActivity.this.startActivity(myIntent); break; default:myIntent = new
	 * Intent(MainActivity.this, MainActivity.class);
	 * MainActivity.this.startActivity(myIntent); break;
	 * 
	 * }
	 * 
	 * }
	 * 
	 * protected void openWebView(String site) {
	 * 
	 * NetworkChecker nc = new NetworkChecker();
	 * 
	 * // setConfig Network=true SharedPreferences settings =
	 * getSharedPreferences("preferences", 0); SharedPreferences.Editor
	 * preferencesEditor = settings.edit();
	 * 
	 * if (nc.isConnected(this.getApplicationContext())) {
	 * 
	 * preferencesEditor.putString("NetworkConnectionAvailable", "true");
	 * preferencesEditor.commit(); } else { // setConfig Network=false
	 * preferencesEditor.putString("NetworkConnectionAvailable", "false");
	 * preferencesEditor.commit(); }
	 * 
	 * 
	 * String netstate = settings.getString("NetworkConnectionAvailable",
	 * "false");
	 * 
	 * // Log Message if(log_enabled) { Log.e("Main", "Netstate " + netstate); }
	 * 
	 * if(netstate.equals("true")) {
	 * 
	 * 
	 * if(site.equals("Webseite")) {
	 * 
	 * openWebViewWithUrl(this.getApplicationContext(), WebviewActivity.class,
	 * "http://www.bib.uni-mannheim.de/mobile/de/1.html", "www"); }
	 * 
	 * if(site.equals("Katalog")) {
	 * openWebViewWithUrl(this.getApplicationContext(), WebviewActivity.class,
	 * "http://primo.bib.uni-mannheim.de/primo_library/libweb/action/search.do?vid=MAN_MOBILE"
	 * , "catalogue"); }
	 * 
	 * } else {
	 * 
	 * openWebViewWithUrl(this.getApplicationContext(), OfflineActivity.class,
	 * "", ""); }
	 * 
	 * 
	 * }
	 * 
	 * // vorerst uebernommen aus MainAdapter // CleanMe! private void
	 * openWebViewWithUrl(Context self, Class next, String url, String action) {
	 * 
	 * Intent i = new Intent(self, next); i.putExtra("url", url);
	 * i.putExtra("action", action);
	 * 
	 * self.startActivity(i); }
	 */

	@Override
	public void onBackPressed() {
		// onBackPressed, onStop, onDestroy

			
			// backstack
			// http://tips.androidhive.info/2013/10/how-to-clear-all-activity-stack-in-android/
			// simply add finish after starting new activity
			
		SharedPreferences settings = getSharedPreferences("preferences", 0);
		SharedPreferences.Editor preferencesEditor = settings.edit();

		preferencesEditor.putString("Config_AppState", "shutdown");
		preferencesEditor.commit();

		// finish();
		ActivityRegistry.getAll();
		
		ActivityRegistry.finishAll();
		
		/*
		Intent i = new Intent(this, EmptyActivity.class);
	    	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	//i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	    	//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	    	//i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    
	    startActivity(i);
	    finish();
	    */
	}
	
	/* EXPERIMENTAL
	@Override
	public void onBackPressed() {
		// onBackPressed, onStop, onDestroy

		SharedPreferences settings = getSharedPreferences("preferences", 0);
		SharedPreferences.Editor preferencesEditor = settings.edit();

		preferencesEditor.putString("Config_AppState", "shutdown");
		preferencesEditor.commit();
		
		//moveTaskToBack(true);
		
		finish();
		
		// System.exit(0);
		
		// android.os.Process.killProcess(android.os.Process.myPid());
		
		// alternatively double click /////////////////////
		 * 
		if (back_pressed + 1000 > System.currentTimeMillis()) {
			// super.onBackPressed();
			
			SharedPreferences settings = getSharedPreferences("preferences", 0);
			SharedPreferences.Editor preferencesEditor = settings.edit();

			preferencesEditor.putString("Config_AppState", "shutdown");
			preferencesEditor.commit();
			
			finish();
			
		} else {
			Toast.makeText(getBaseContext(), "Zum Beenden Doppelklicken", Toast.LENGTH_SHORT).show();
		}
        
		back_pressed = System.currentTimeMillis();
		
		//////////////////////////////////////////////////
		
	}*/
	
}
