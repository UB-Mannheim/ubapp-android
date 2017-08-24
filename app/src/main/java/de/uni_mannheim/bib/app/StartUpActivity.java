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
 * Switches start module to specified action,
 * as configured in Preferences (ConfigActivity)
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class StartUpActivity extends Activity {

	private boolean log_enabled = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		ActivityRegistry.register(this);
		
		if(log_enabled) {
			Log.e( this.getClass().getName().toUpperCase().toString(), " ... LOADED");
		}
		// Toast.makeText(getApplicationContext(), this.getClass().getName(), 
				// Toast.LENGTH_SHORT).show();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_up);

		// initConfig();
		// switchToStartUpActivity();

		SharedPreferences settings = getSharedPreferences("preferences", 0);
		SharedPreferences.Editor preferencesEditor = settings.edit();

		String mode = settings.getString("Config_AppState", null); // startup,
																   // running,
																   // shutdown

		if (mode.equals("startup")) {

			preferencesEditor.putString("Config_AppState", "running");
			preferencesEditor.commit();

			int startup_id = Integer.valueOf(settings.getString(
					"Config_StartUpActivity", "0"));
			
			// Intent myIntent;
			// DEBUG
			
			switch (startup_id) {

				case 0:
					Intent myIntent0 = new Intent(StartUpActivity.this, 
							MainActivity.class);
					StartUpActivity.this.finish();
					StartUpActivity.this.startActivity(myIntent0);
					// StartUpActivity.this.finish();
					break;
				case 1:
					// StartUpActivity.this.finish();
					openWebView("Webseite");
					// StartUpActivity.this.finish();
					break;
				case 2:
					// StartUpActivity.this.finish();
					openWebView("Katalog");
					// StartUpActivity.this.finish();
					break;
				case 3:
					Intent myIntent3 = new Intent(StartUpActivity.this,
							BlogActivity.class);
					StartUpActivity.this.finish();
					StartUpActivity.this.startActivity(myIntent3);
					// StartUpActivity.this.finish();
					break;
				case 4:
					Intent myIntent4 = new Intent(StartUpActivity.this,
							LoadActivity.class);
					StartUpActivity.this.finish();
					StartUpActivity.this.startActivity(myIntent4);
					// StartUpActivity.this.finish();
					break;
				default:
					Intent myIntent = new Intent(StartUpActivity.this, 
							MainActivity.class);
					StartUpActivity.this.finish();
					StartUpActivity.this.startActivity(myIntent);
					// StartUpActivity.this.finish();
					break;
			}

		} else {
			Intent myIntent = new Intent();
			myIntent = new Intent(StartUpActivity.this, MainActivity.class);
			StartUpActivity.this.finish();
			StartUpActivity.this.startActivity(myIntent);
			// StartUpActivity.this.finish();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_up, menu);
		return true;
	}

	protected void initConfig() {

		// Initialize Shared Preferences
		SharedPreferences settings = getSharedPreferences("preferences", 0);
		SharedPreferences.Editor preferencesEditor = settings.edit();

		// Set Default Preferences
		String cfgChanged = settings.getString("Config_ParamsChanged", null);

		if (cfgChanged == null) {

			// Initial Creation
			preferencesEditor.putString("Config_ParamsChanged", "false");

			preferencesEditor.putString("NetworkConnectionAvailable", "true");
			preferencesEditor.putString("Config_DatabaseModeOn", "false");
			preferencesEditor.putString("Config_NewsRange", "5");
			preferencesEditor.putString("Config_LoadInterval", "1");
			preferencesEditor.putString("Config_StartUpActivity", "0");
			preferencesEditor.commit();

			Log.e("StartUpActivity", "Configuration created");

		} else {
			Log.e("StartUpActivity", "Configuration already exists");
		}

	}

	protected void switchToStartUpActivity() {

		// Values:
		// 0 = Main
		// 1 = Website
		// 2 = Primo
		// 3 = News
		// 4 = Auslastung

		// Initialize Shared Preferences
		SharedPreferences settings = getSharedPreferences("preferences", 0);

		// Set Default Preferences
		int startup_id = Integer.valueOf(settings.getString(
				"Config_StartUpActivity", "0"));
		Intent myIntent;

		switch (startup_id) {

		case 0:
			myIntent = new Intent(StartUpActivity.this, MainActivity.class);
			StartUpActivity.this.startActivity(myIntent);
			break;
		case 1:
			openWebView("Webseite");
			break;
		case 2:
			openWebView("Katalog");
			break;
		case 3:
			myIntent = new Intent(StartUpActivity.this, BlogActivity.class);
			StartUpActivity.this.startActivity(myIntent);
			break;
		case 4:
			myIntent = new Intent(StartUpActivity.this, LoadActivity.class);
			StartUpActivity.this.startActivity(myIntent);
			break;
		default:
			myIntent = new Intent(StartUpActivity.this, MainActivity.class);
			StartUpActivity.this.startActivity(myIntent);
			break;

		}

	}

	protected void openWebView(String site) {

		NetworkChecker nc = new NetworkChecker();

		// Set Config Network=true
		SharedPreferences settings = getSharedPreferences("preferences", 0);
		SharedPreferences.Editor preferencesEditor = settings.edit();

		if (nc.isConnected(this.getApplicationContext())) {

			preferencesEditor.putString("NetworkConnectionAvailable", "true");
			preferencesEditor.commit();
		} else {
			// Set Config Network=false
			preferencesEditor.putString("NetworkConnectionAvailable", "false");
			preferencesEditor.commit();
		}

		String netstate = settings.getString("NetworkConnectionAvailable",
				"false");

		// Log Message
		if (log_enabled) {
			Log.e("Main", "Netstate " + netstate);
		}

		if (netstate.equals("true")) {

			if (site.equals("Webseite")) {

				openWebViewWithUrl(this.getApplicationContext(),
						WebviewActivity.class,
						"http://www.bib.uni-mannheim.de/mobile/de/1.html",
						"www");
			}

			if (site.equals("Katalog")) {
				openWebViewWithUrl(
						this.getApplicationContext(),
						WebviewActivity.class,
						// "http://primo.bib.uni-mannheim.de/primo_library/libweb/action/search.do?vid=MAN_MOBILE",
						"https://primo-49man.hosted.exlibrisgroup.com/primo-explore/search?sortby=rank&vid=MAN_UB&lang=de_DE",
						"catalogue");
			}

		} else {

			openWebViewWithUrl(
					this.getApplicationContext(),
					OfflineActivity.class, "", "");
		}

	}

	// vorerst uebernommen aus MainAdapter
	// FixMe
	// DEBUG
	private void openWebViewWithUrl(Context self, Class next, String url,
			String action) {

		Intent i = new Intent(StartUpActivity.this, next);
		// Intent i = new Intent(self, next);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra("url", url);
			i.putExtra("action", action);

		StartUpActivity.this.finish();
		StartUpActivity.this.startActivity(i);
		// self.startActivity(i);
	}

}
