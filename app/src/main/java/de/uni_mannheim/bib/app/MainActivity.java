/*
 * Copyright (C) 2014 Universitätsbibliothek Mannheim
 *
 * Author:
 *    Alexander Wagner <alexander.wagner@bib.uni-mannheim.de>
 *    Last modified on 2016-03-15
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

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	private boolean log_enabled = false;

	GridView gv;
	Context context;

	ArrayList<?> prgmName;

	String str_www = "";
	String str_primo = "";
	String str_news = "";
	String str_seats = "";


	public String[] prgmNameList = new String[]{str_www, str_primo, str_news,
			str_seats};

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
		
		// Screen Layout for Main Activity
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Customized Actionbar (color, title)
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.library_bg)));
        actionBar.setTitle(R.string.app_name);

        SharedPreferences settings = getSharedPreferences("preferences", 0);
		// Debug
		// settings.edit().clear().commit();

		// Init Configuration
		initConfig();

		this.str_www = this.getString(R.string.__www__);
		this.str_primo = this.getString(R.string.__primo__);
		this.str_news = this.getString(R.string.__news__);
		this.str_seats = this.getString(R.string.__seats__);

		this.prgmNameList[0] = this.str_www;
		this.prgmNameList[1] = this.str_primo;
		this.prgmNameList[2] = this.str_news;
		this.prgmNameList[3] = this.str_seats;

		// If custom_startup configured
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


		// Howto go-back(), Link
		}

		return super.onOptionsItemSelected(item);
	}

	protected void initConfig() {

		// Initialize shared Preferences
		SharedPreferences settings = getSharedPreferences("preferences", 0);
		SharedPreferences.Editor preferencesEditor = settings.edit();

		// Set default Preferences
		String cfgChanged = settings.getString("Config_ParamsChanged", null);

		if (cfgChanged == null) {

			// Initial Creation
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

	@Override
	public void onBackPressed() {
		// Modes: onBackPressed, onStop, onDestroy

		// backstack, Link
		// Simply add finish after starting new activity
			
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
	
}
