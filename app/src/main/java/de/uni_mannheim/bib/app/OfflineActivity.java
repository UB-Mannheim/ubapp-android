/*
 * Copyright (C) 2014 Universitätsbibliothek Mannheim
 *
 * Author:
 *    Universitätsbibliothek Mannheim <sysadmin@bib.uni-mannheim.de>
 *    Last modified on 2016-03-15
 * 
 * 
 * This is free software licensed under the terms of the GNU GPL, 
 * version 3, or (at your option) any later version.
 * See <http://www.gnu.org/licenses/> for more details.
 *
 *
 * Shows HTML file with offline dialog. Is used if WebviewActivity
 * has no online content due to connection problems.
 * Refers to
 * 		Catalogue &
 * 		Website
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

public class OfflineActivity extends ActionBarActivity {

	// For customized and consistent UI (Android 2.3 - 4.x)
	// Always extend ActionBarActivity

	private boolean log_enabled = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		ActivityRegistry.register(this);
		
		if(log_enabled) {
			Log.e( this.getClass().getName().toUpperCase().toString(), " ... LOADED");
		}
		
		// Screen Layout for Main Activity
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offline);

		// Show the Up button in the action bar. (Autmatic)
		setupActionBar();

		// Customized Actionbar (Color, Title)
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.library_bg)));
		actionBar.setTitle(R.string.app_name);

		final WebView webView1 = (WebView) findViewById(R.id.webView1);

		webView1.loadUrl("file:///android_asset/offline.html");
		
		// Get Preferences
		SharedPreferences settings = getSharedPreferences(
				"preferences", 0);
		// Network State
		String network_state = settings.getString(
				"NetworkConnectionAvailable", "false");
		
		if(network_state.equals("false")) {
			new AlertDialog.Builder(
					OfflineActivity.this)
					.setTitle(getString(R.string.dialog_offlineActivity_connection_error__title))
					.setMessage(getString(R.string.dialog_offlineActivity_connection_error__message))
					.setPositiveButton(
							getString(R.string.dialog_offlineActivity_connection_error__positive),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											OfflineActivity.this,
											MainActivity.class);
									startActivity(intent);
								}
							}).show();
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.offline, menu); return true; }
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		// Exits app, no more activites in stack?
		// moveTaskToBack(true);
		
		NavUtils.navigateUpFromSameTask(this);
        
	}

}
