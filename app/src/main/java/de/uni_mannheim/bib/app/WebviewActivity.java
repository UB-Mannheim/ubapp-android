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
 * Shows Catalogue and Website in Webview.
 * Small Layout changes during loading process.
 * 		Website: 2nd navigation on "menu" button
 * 
 */

package de.uni_mannheim.bib.app;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.transition.Visibility;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class WebviewActivity extends AppCompatActivity {

    // Logs
	private boolean log_enabled = false;
	
	WebView webView;
	ProgressDialog progressBar;

	// Define variables for transferred content
	protected String url;
	protected String action;
	protected int height;
	protected int width;
	public String lang = "de";

	public String state = "";

	// For customized and consistent UI (Android 2.3 - 4.x)
	// Always extend AppCompatActivity

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		ActivityRegistry.register(this);
		
		if(log_enabled) {
			Log.e( this.getClass().getName().toUpperCase().toString(), " ... LOADED");
		}
		
		// Screen Layout for Main Activity
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		// Customized Actionbar (Color, Title, Back Button)
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.library_bg)));
		actionBar.setTitle(R.string.app_name);

		// Get common Parameters
		url = getIntent().getExtras().getString("url");
		action = getIntent().getExtras().getString("action");
		
		// Get and set Webview
		webView = (WebView) findViewById(R.id.webView1);
		WebSettings wset = webView.getSettings();

		// Enable JS
		wset.setJavaScriptEnabled(true);

		// Enable further HTML5 Compatibility in Webview (Primo, Angular JS)
		wset.setDomStorageEnabled(true);
		// in case another error occurs: try one of these
		// wset.setDatabaseEnabled(true);
		// wset.setAppCacheEnabled(true);
		// wset.setAppCacheMaxSize(1024*1024*8);


		// Set ProgressBar
		progressBar = new ProgressDialog(webView.getContext());
		progressBar.setMessage(getString(R.string.alert_loading));
		progressBar.show();

		webView.setWebViewClient(new myWebClient());
		
		// Reload the old Webview content
		if (savedInstanceState != null) {
		 	webView.restoreState(savedInstanceState);
		 	} else {
			webView.loadUrl(url);
		}

	}

	// Preventing Reload of Webview on rotate
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		webView.saveState(outState);
	}

	// Overriding back-button within webview as "browser-back",
	// NOT last_task in stack
	// http://stackoverflow.com/questions/6077141/android-webview-how-to-code-the-back-button
	/*
	 * devices > android 2.2
	 */
	@Override
	public void onBackPressed() {
		if (webView.canGoBack()) {
			webView.goBack();
		} else {

			NavUtils.navigateUpFromSameTask(this);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	// Check Language

	public String getLang() {

		// Log Message
		if (log_enabled) {
			Log.e(this.getClass().toString(), "getLang called");
		}

		// Log Message
		if (log_enabled) {
			Log.e(this.getClass().toString(), ">>" + webView.getTitle()+"<<");
		}
		
		// If Webview exists (has a title)
		if (webView.getTitle() != null) {

			if (webView.getUrl().contains("/en/")) {
				lang = "en";
			} else {
				lang = "de";
			}
		}

		return lang;
	}


	public class myWebClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		// @Override
		@Override
		public void onPageFinished(WebView view, String url) {
		// public void onPageFinished(WebView view, String action) {

			super.onPageFinished(view, url);
			progressBar.dismiss();

			webView.setVisibility(View.VISIBLE);

			// Hide red navbar
			if (action.equals("www")) {
				webView.loadUrl("javascript:(function() { "
						+ "var x = document.getElementsByClassName('navbar-header'); "
						+ "x[0].style.backgroundColor = 'white'; "
						+ "var y = document.getElementsByClassName('navbar-brand'); "
						+ "y[0].style.display = 'none'; "
						+ "})()");
			}

			// Hide Logo
			if ( action.equals("catalogue") || url.contains("primo-explore") ) {
				webView.loadUrl("javascript:(function() { "
						+ "var x = document.getElementsByClassName('ub-logo'); "
						+ "x[0].style.visibility = 'hidden'; "
						+ "})()");
			}

			webView.requestFocus(View.FOCUS_DOWN);

		}

	}

}
