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
 * Shows a defined number of blog entries as a list. 
 * This may be online or database content.
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

public class BlogActivity extends ActionBarActivity {

	private boolean log_enabled = false;

	private BlogActivity ba = this;
	private GridView gv;

	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();

	private String db_mode_on;
	public static List<String[]> newsList;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		ActivityRegistry.register(this);
		
		if(log_enabled) {
			Log.e( this.getClass().getName().toUpperCase().toString(), " ... LOADED");
		}
		
		// screen layout for main activity
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blog);

		// customized actionbar (Color, Title)
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.library_bg)));
		actionBar.setTitle(R.string.app_name);

		gv = (GridView) this.findViewById(R.id.gridView1);

		// Log Message
		if (log_enabled) {
			Log.e(this.getClass().toString(), "constructed");
		}
	}

	@Override
	public void onStart() {
		// public void onStart(View v) {

		// Log Message
		if (log_enabled) {
			Log.e(this.getClass().toString(), "onStart");
		}

		// prepare for a progress bar dialog
		progressBar = new ProgressDialog(gv.getContext());
		// progressBar.setCancelable(true);
		progressBar.setMessage(getString(R.string.dialog_loading));
		// progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// progressBar.setProgress(0);
		// progressBar.setMax(100);
		progressBar.show();

		// reset progress bar status
		progressBarStatus = 0;

		// GridView invisible until content is loaded
		gv.setVisibility(View.GONE);

		// progressBar = ProgressDialog.show(this, loading, "", true, false);

		// initialize newsList
		newsList = null;

		new Thread(new Runnable() {
			@Override
			public void run() {

				// Log Message
				if (log_enabled) {
					Log.e(this.getClass().toString(), "Start inner Thread ...");
				}

				// get Preferences
				SharedPreferences settings = getSharedPreferences(
						"preferences", 0);

				// Network State
				String network_state = settings.getString(
						"NetworkConnectionAvailable", "false");
				Helper h = new Helper();
				String network_helper_state = h
						.getNetworkConnectionState(network_state);

				// News Range
				int news_range = Integer.valueOf(settings.getString(
						"Config_NewsRange", "5"));

				// Log Message
				if (log_enabled) {
					Log.e(this.getClass().toString(), "Anzahl der News: "
							+ String.valueOf(news_range));
				}

				// DB Mode
				db_mode_on = settings.getString("Config_DatabaseModeOn",
						"false");

				// DB Helper
				DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());

				// Data Collector
				DataCollector dc = new DataCollector();

				// Actions in loop
				while (progressBarStatus < 100) {

					// Log Message
					if (log_enabled) {
						Log.e(this.getClass().toString(),
								String.valueOf(progressBarStatus));
					}

					// if computer is too fast, sleep 1 second to see dialog
					try {
						// Thread.sleep(1000);
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// Get News from Online Resource
					List<News> listWWW = null;
					if (network_helper_state.equals("online")) {
						listWWW = dc.getNewsFromWWW(news_range);
					}

					// Get News from DB
					List<News> listDB = null;
					if (db_mode_on.equals("true")) {
						listDB = dc.getNewsFromDB(dbh);
					}

					/*
					 * // Log Message
					 * if(log_enabled) {
					 * if(network_helper_state.equals("online")) { for(News n :
					 * listWWW) { Log.e(this.getClass().toString(), n.getId() +
					 * " - " + n.getTitle().substring(0, 20) ); } } }
					 */

					// if News not available
					// until newsList is complete
					if (newsList == null) {

						// Log Message
						if (log_enabled) {
							Log.e(this.getClass().toString(), "News List empty");
						}

						// Create Global News List
						newsList = dc.getNewsAccordingToSyncState(
								getApplicationContext(), network_helper_state,
								db_mode_on, dbh, news_range, listWWW, listDB);

						// if DB is Off
						if (db_mode_on.equals("false")) {
							try {
								// Thread.currentThread().interrupt();
								Thread.currentThread().setPriority(
										Thread.MIN_PRIORITY);
								progressBarStatus += 1;
							} catch (Exception e) {
								
								// Log Message
								if(log_enabled) {
									Log.e("", "Error on interrupt " + e);
								}
							}
						}

					} else {
						// count up progressBar
						// progressBarStatus += 1;

						// set progressBar=100
						progressBarStatus = 100;

						// Log Message
						if (log_enabled) {
							Log.e(this.getClass().toString(),
									String.valueOf(progressBarStatus));
						}

					}

					// Update the progress bar
					progressBarHandler.post(new Runnable() {
						@Override
						public void run() {

							// Log Message
							if (log_enabled) {
								Log.e(this.getClass().toString(),
										"set progressBarStatus");
							}
							progressBar.setProgress(progressBarStatus);
						}
					});

				} // while end

				// ok, action is fullfilled
				if (progressBarStatus >= 100) {

					// Log Message
					if (log_enabled) {
						Log.e(this.getClass().toString(), "ProgressBar Full");
					}

					// sleep some seconds, so that you can see the 100%
					try {
						// Thread.sleep(2000);
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// If Global News available
					if (newsList != null) {

						// Log Message
						if (log_enabled) {
							for (String[] n : newsList) {
								Log.e(this.getClass().toString(),
										"newsList Content: " +
										// n.toString());
												":" + n[0].toString() + ":");
							}
						}

						// If DB Mode on update History
						if (db_mode_on.equals("true")) {
							History hnews = new History(1, 1, dbh.getDateTime());
							dbh.updateHistory(hnews);
						}

						// Log Message
						if (log_enabled) {
							Log.e(this.getClass().toString(), "pre gv.post ...");
						}

						// Fill Adapter
						gv.post(new Runnable() {
							@Override
							public void run() {

								if (((newsList.get(0)[0].length() == 0) && (db_mode_on
										.equals("true")))
										|| newsList.get(0)[0].equals("")) {

									// Log Message
									if (log_enabled) {
										Log.e(this.getClass().toString(),
												"GET 0" + newsList.get(0)[0]);
									}

									progressBar.dismiss();

									new AlertDialog.Builder(
											BlogActivity.this)
											.setTitle(getString(R.string.dialog_newsActivity_cache_state__title))
											.setMessage(getString(R.string.dialog_newsActivity_cache_state__message))
											.setPositiveButton(
													getString(R.string.dialog_newsActivity_cache_state__positive),
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															Intent intent = new Intent(
																	BlogActivity.this,
																	MainActivity.class);
															startActivity(intent);
														}
													}).show();

								} else {

									gv.setAdapter(new BlogAdapter(ba, newsList
											.get(0), newsList.get(1)));

									// Close Loading Dialog
									// progressBar.dismiss(); // called at end
									// of loop
									// GridView visibility on
									gv.setVisibility(View.VISIBLE);

								}
							}

						});

						// If exist Global News NOT available
					} else {

						gv.post(new Runnable() {
							@Override
							public void run() {

								// Close Loading Dialog
								progressBar.dismiss();

								new AlertDialog.Builder(BlogActivity.this)
										.setTitle(getString(R.string.dialog_newsActivity_connection_error__title))
										.setMessage(getString(R.string.dialog_newsActivity_connection_error__message))
										.setPositiveButton(
												getString(R.string.dialog_newsActivity_connection_error__positive),
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														Intent intent = new Intent(
																BlogActivity.this,
																MainActivity.class);
														startActivity(intent);
													}
												}).show();

							}
						});

					}

					// close the progress bar dialog
					progressBar.dismiss();

				} // if progressBarStatus >= 100

			} // end void run

		}).start();

		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_config:
			Intent myIntent0 = new Intent(BlogActivity.this,
					ConfigActivity.class);
			BlogActivity.this.startActivity(myIntent0);
			return true;
		case R.id.action_help:
			Intent myIntent1 = new Intent(BlogActivity.this,
					HelpActivity.class);
			BlogActivity.this.startActivity(myIntent1);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		
		NavUtils.navigateUpFromSameTask(this);
        
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
		
		// Log.e("--------XXXXXXXXXXXXXXXXx-----------", "onRestart");
		SharedPreferences settings = BlogActivity.this.getSharedPreferences(
				"preferences", 0);
		
		NetworkChecker nc = new NetworkChecker();
		boolean netstat = nc.isConnected(BlogActivity.this);
		
		// Log.e("NETSTAT", String.valueOf(netstat));
		
		if(netstat) {
			SharedPreferences.Editor preferencesEditor = settings.edit();
			preferencesEditor.putString("NetworkConnectionAvailable",
					"true");
			preferencesEditor.commit();
		}
		else {
			SharedPreferences.Editor preferencesEditor = settings.edit();
			preferencesEditor.putString("NetworkConnectionAvailable",
					"false");
			preferencesEditor.commit();
			/*
			new AlertDialog.Builder(LoadActivity.this)
			.setTitle(
					"Verbindungsfehler")
			.setMessage(
					"F�r diese Anwendung ist eine Datenverbindung erforderlich. "
						+ "Atkivieren Sie Mobilfunk oder Wi-Fi, um Daten "
						+ "herunterzuladen.")
			.setPositiveButton(
					"Einstellungen",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							
							startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
							finish();
						}
					})
			.setNegativeButton("Abbrechen",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
			
						}
					}).show();
			*/
		}
	}

}
