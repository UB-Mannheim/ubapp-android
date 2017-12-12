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
import android.support.v7.app.ActionBarActivity;
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

public class WebviewActivity extends ActionBarActivity {

    /*
	// Speech Input
	private TextView txtSpeechInput;
	private String searchTerm;
	private ImageButton btnSpeak;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	*/
    
   // Logs
	private boolean log_enabled = false;
	
	// Maybe integrate onStart action / threads ?
	
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
	// Always extend ActionBarActivity

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


		// Speech Input
		/*
inserted new Primo URL, therefore disabled button for speech input

		// txtSpeechInput = (TextView) findViewById(R.id.textView1);
        btnSpeak = (ImageButton) findViewById(R.id.imageButton1);
 
        // webView = (WebView) findViewById(R.id.webView1);
        // webView.loadUrl("http://www.google.de");
        
        btnSpeak.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        
        // Generally hide Speech Input
     	// inserted new Primo URL, therefore disabled button for speech input
		// btnSpeak.setVisibility(View.INVISIBLE);
		*/

		// Get common Parameters
		url = getIntent().getExtras().getString("url");
		action = getIntent().getExtras().getString("action");
		
		// Get and set Webview
		webView = (WebView) findViewById(R.id.webView1);
		WebSettings wset = webView.getSettings();
		wset.setJavaScriptEnabled(true);

		// Set ProgressBar
		progressBar = new ProgressDialog(webView.getContext());
		progressBar.setMessage(getString(R.string.alert_loading));
		progressBar.show();

		// Create Webclient
		webView.setWebViewClient(new myWebClient());
		
		// Reload the old Webview content
		if (savedInstanceState != null) {
			webView.restoreState(savedInstanceState);
		} else {
			webView.loadUrl(url);
		}

		// Further information, alternative solution:
		// Retain Link

	}
	
	// Speech Input
	
	/**
     * Showing google speech input dialog
     * */


	/*
	inserted new Primo URL, therefore disabled button for speech input

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
  	*/

    /**
     * Receiving speech input
     * */


    /*
    inserted new Primo URL, therefore disabled button for speech input

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case REQ_CODE_SPEECH_INPUT: {
            if (resultCode == RESULT_OK && null != data) {
 
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                // txtSpeechInput.setText(result.get(0));
                
                if(searchTerm != "") {
                	searchTerm = result.get(0);
                
                	// Open URL Link
                	
                	webView = (WebView) findViewById(R.id.webView1);
                	webView.getSettings().setJavaScriptEnabled(true);
                
                	// webView.loadUrl("http://primo.bib.uni-mannheim.de/
                	// primo_library/libweb/action/search.do?vid=MAN_MOBILE
                	// &vl%28freeText0%29="+searchTerm);
                	
                	webView.loadUrl("http://primo.bib.uni-mannheim.de/" +
                					"primo_library/libweb/action/search.do?" +
                					"&vl%28freeText0%29="+searchTerm +
                					"&fn=search&vid=MAN_MOBILE&voiceInput=TRUE");

                	// watch out for primo server logs in 
                	// Primo Path
                // /exlibris/primo/p4_1/ng/primo/home/profile/search/log/localhost_access_log.2015-06-08.txt
                }
                
            }
            break;
        }
 
        }
    }
	*/

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Log Message
		if (log_enabled) {
			Log.e(this.getClass().toString(),"called");
		}



		// actionbar alternative menu only in webview->website
		/*
		as the new website was inserted, the old menu is not longer needed
		if (action.equals("www")) {

			// Inflate the menu (adds items to the action bar if it is present)
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.www_activity_actions, menu);
			
			// Log Message
			if (log_enabled) {
				Log.e(this.getClass().toString(), "ACTION_BAR CREATED");
			}
			
		}

		if (action.equals("catalogue")) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main_activity_actions, menu);
			
			// Use Custom Layout Link
		
            // http://www.techrepublic.com/article/pro-tip-use-a-custom-layout-to-badge-androids-action-bar-menu-items/
		}
		*/

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Log Message
		if (log_enabled) {
			Log.e(this.getClass().toString(), "onOptionsItemSelected called");
		}
		
		if (action.equals("www")) {

			switch (item.getItemId()) {

			case android.R.id.home:
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. For more details, see the
				// Navigation
				// pattern on Android Design:
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back

				NavUtils.navigateUpFromSameTask(this);
				return true;

			case R.id.action_wwwCatalogue:
				openURLsFromCustomMenuEntry(1);
				return true;
			case R.id.action_wwwSearch:
				openURLsFromCustomMenuEntry(2);
				return true;
			case R.id.action_wwwElectronicMedia:
				openURLsFromCustomMenuEntry(3);
				return true;
			case R.id.action_wwwService:
				openURLsFromCustomMenuEntry(4);
				return true;
			case R.id.action_wwwInformation:
				openURLsFromCustomMenuEntry(5);
				return true;
			case R.id.action_wwwNews:
				openURLsFromCustomMenuEntry(6);
				return true;
			case R.id.action_wwwUB:
				openURLsFromCustomMenuEntry(7);
				return true;
			case R.id.action_wwwLibrarySections:
				openURLsFromCustomMenuEntry(8);
				return true;
			case R.id.action_wwwContact:
				openURLsFromCustomMenuEntry(9);
				return true;
			}

		}

		if (action.equals("catalogue")) {

			switch (item.getItemId()) {
			case R.id.action_config:
				Intent myIntent0 = new Intent(WebviewActivity.this,
						ConfigActivity.class);
				WebviewActivity.this.startActivity(myIntent0);
				return true;
			case R.id.action_help:
				Intent myIntent1 = new Intent(WebviewActivity.this,
						HelpActivity.class);
				WebviewActivity.this.startActivity(myIntent1);
				return true;
			}

		}

		return super.onOptionsItemSelected(item);
	}

	// Rewrite HTML Menus

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		// Log Message
		if (log_enabled) {
			Log.e(this.getClass().toString(), "onPrepare called");
		}

		lang = getLang();

		if (lang.equals("en")) {
			menu.findItem(R.id.action_wwwCatalogue).setTitle("Online Catalog");
				// menu.findItem(R.id.action_wwwCatalogue).setEnabled(false);
			menu.findItem(R.id.action_wwwSearch).setTitle("Search and Order");
			menu.findItem(R.id.action_wwwElectronicMedia).setTitle(
					"Electronic Resources");
			menu.findItem(R.id.action_wwwService).setTitle("Services");
			menu.findItem(R.id.action_wwwInformation).setTitle("Subjects");
			menu.findItem(R.id.action_wwwNews).setTitle("News");
			menu.findItem(R.id.action_wwwUB).setTitle("The University Library");
			menu.findItem(R.id.action_wwwLibrarySections).setTitle(
					"Library Sections");
			menu.findItem(R.id.action_wwwContact).setTitle("Contact");
		}

		// Depending on your conditions, either enable/disable
		// item.setEnabled(false);
		super.onPrepareOptionsMenu(menu);

		return true;
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

	// Rewrite Menu URLs (lang)
	public void openURLsFromCustomMenuEntry(int id) {

		// Log Message
		if(log_enabled) {
			Log.e(this.getClass().toString(), "openURL called");
		}
		
		lang = getLang();

		String[] entries = new String[10];

		entries[0] = "";
		// entries[1] = "http://www.bib.uni-mannheim.de/mobile/"+ lang
		// +"/133.html";
		// entries[1] = "http://primo.bib.uni-mannheim.de/primo_library/libweb/action/search.do?vid=MAN_MOBILE";
		entries[1] = "https://primo-49man.hosted.exlibrisgroup.com/primo-explore/search?sortby=rank&vid=MAN_UB&lang=de_DE";
		// entries[1] = "http://primo.bib.uni-mannheim.de/primo_library/libweb/action/search.do?vid=MAN_KB";
		entries[2] = "http://www.bib.uni-mannheim.de/mobile/" + lang
				+ "/5.html";
		entries[3] = "http://www.bib.uni-mannheim.de/mobile/" + lang
				+ "/361.html";
		entries[4] = "http://www.bib.uni-mannheim.de/mobile/" + lang
				+ "/6.html";
		entries[5] = "http://www.bib.uni-mannheim.de/mobile/" + lang
				+ "/37.html";
		entries[6] = "http://www.bib.uni-mannheim.de/mobile/" + lang
				+ "/3.html";
		entries[7] = "http://www.bib.uni-mannheim.de/mobile/" + lang
				+ "/4.html";
		entries[8] = "http://www.bib.uni-mannheim.de/mobile/" + lang
				+ "/63.html";
		entries[9] = "http://www.bib.uni-mannheim.de/mobile/" + lang
				+ "/241.html";

		webView.loadUrl(entries[id]);
	}

	public class myWebClient extends WebViewClient {

		// Possible scaling of contents (as above)
		    // webView.setInitialScale(1);
		// webView.getSettings().setLoadWithOverviewMode(true);
            // webView.getSettings().setUseWideViewPort(true);
            // webView.getSettings().setJavaScriptEnabled(true);

		// android-webview-webpage Link

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
			// super.onPageFinished(view, action);

			progressBar.dismiss();

			webView.setVisibility(View.VISIBLE);

						
			// DOM Manipulation after loading Webview contents
			
			// Layout changes in Website context
			
				// Hiding Mobile Trailer
	
				// if(url.contains("mobile")) {
				if (action.equals("www")) {
					/*
					webView.loadUrl("javascript:(function() { "
							+ "document.getElementById('mobile_trailer').style.display = 'none'; "
							+ "})()");
*/
					webView.loadUrl("javascript:(function() { "
							+ "var x = document.getElementsByClassName('navbar-header'); "
							+ "x[0].style.backgroundColor = 'white'; "
							+ "var y = document.getElementsByClassName('navbar-brand'); "
							+ "y[0].style.display = 'none'; "
							/*
							+ "document.getElementById('navbar-button').style.display = 'none'; "
							*/
							+ "})()");

					// additional menu items in actionbar
	
				}
	
				
			// Layout Changes in Catalogue Context
	
				// if(url.contains("primo_library")) {
				if (action.equals("catalogue")
						|| (url.contains("primo_library") && url
								.contains("MAN_UB"))) {
					
					/*
					inserted new Primo URL, therefore disabled button for speech input
					
					// check if Speech Input Microphone should be displayed
					
					if(webView.getUrl().contains("pds")) {
						btnSpeak.setVisibility(View.INVISIBLE);
					} else {
						btnSpeak.setVisibility(View.VISIBLE);
					}
					*/
					// document.getElementById('katalog').style.display = 'none';
					// document.getElementById('katalogUBSchriftzug').style.display
					// = 'none';
					// document.getElementById('ub_mini_logo').style.display =
					// 'none';
						/*
					inserted new Primo URL, therefore disabled javascript actions for old UI

					webView.loadUrl("javascript:(function() { "
							+ "document.getElementById('exlidUserAreaTile').style.padding = '10px'; "
							+ "document.getElementById('logos').style.visibility = 'hidden'; "
							+ "document.getElementById('exlidSearchTile').style.position = 'relative'; "
							+ "document.getElementById('exlidSearchTile').style.top = '-80px'; "
							+ "document.getElementById('exlidMainMenuRibbon').style.display = 'none'; "
							// + "document.getElementById('exlidResultsContainer').style.paddingTop = '0px'; "
							// + "document.getElementById('resultsHeaderNoId').style.marginBottom = '0px'; "
							+ "document.getElementById('exlidHeaderContainer').style.height = '100px'; "
							// + "document.getElementById('exlidUserName').style.top = '10px'; "
							+ "})()");
					 */

					webView.loadUrl("javascript:(function() { "
							+ "var x = document.getElementsByClassName('ub-logo'); "
					        + "x[0].style.visibility = 'hidden'; "
							// + "x[0].style.display = 'none'; "
							+ "})()");

}

				
			// ATTENTION
			// code behind large comment !!!

            // FixMe
			/**
			 * 
			 * old functions, should better be native activities and no longer
			 * webviews
			 * 
			 * //////////////////////////////////////
			 * // CHANGE CSS IN NAVIGATION CONTEXT //
			 * 
			 * // if(url.contains("dreidplan")) {
			 * if(action.equals("navigation")) {
			 * 
			 * // int width = webView.getRight()-webView.getLeft()-300; //
			 * Log.v("www-width", String.valueOf(width)); // String w =
			 * String.valueOf(width);
			 * 
			 * webView.loadUrl("javascript:(function() { " +
			 * 
			 * "document.body.style.width = '"+width+"px';"+
			 * "document.getElementById('header').style.display = 'none';"+
			 * "document.getElementById('beschreibung').style.paddingTop = '10px';"
			 * +
			 * "document.getElementById('listbox_bbs').style.display = 'block';"
			 * +
			 * "document.getElementById('listbox_bbs').style.paddingBottom = '10px';"
			 * +
			 * "document.getElementById('sprachlink').style.cssFloat = 'none';"+
			 * "document.getElementById('sprachlink').style.marginLeft= '10px';"
			 * + // Funktioniert nicht, die "bar" hat ein Eigenleben und
			 * skaliert eigenstaendig (...) // auch im normalen Browser kommt es
			 * bei kleinen Aufloesungen nach einem // Resize des Fensters zum
			 * gleihen Problem //
			 * "document.getElementById('bar').style.top = '100px';" +
			 * "document.getElementById('bar').style.width = '"+width+"px';"+
			 * "})()");
			 * 
			 * }
			 * 
			 * ////////////////////////////////
			 * // CHANGE CSS IN CHAT CONTEXT //
			 * 
			 * if(action.equals("chat")) {
			 * webView.loadUrl("javascript:(function() { " +
			 * "document.getElementById('greybg').style.width = '"+width+"px'; "
			 * + "})()"); }
			 *
             *
             * //////////////////////////////////////
			 * // CHANGE CSS IN WLAN LOAD CONTEXT //
			 * 
			 * if(action.equals("statistic")) {
			 * 
			 * String fontSize= String.valueOf(width/85);
			 * 
			 * webView.loadUrl("javascript:(function() { " +
			 * "document.body.style.fontSize = '"+fontSize+"px';"+
			 * "document.getElementsByTagName('table')[0].style.width = '"
			 * +width+"px';"+
			 * "document.getElementsByTagName('table')[0].style.height = '"
			 * +height+"px';"+ "})()"); Log.v("width", String.valueOf(width));
			 * // internal: Plus Klappmenu in Menubar?
			 * 
			 * }
			 * 
			 * /////////////////////////////////
			 * // CHANGE CSS IN BLOG CONTEXT //
			 * 
			 * if(action.equals("news")) {
			 * 
			 * // Log.e("BIB_LOG", "Called Webview, action=news");
			 * 
			 * webView.loadUrl("javascript:(function() { " +
			 * "document.getElementById('news').style.paddingLeft = '10px'; " +
			 * "document.getElementById('news').style.paddingRight = '20px'; " +
			 * //
			 * "document.getElementById('content').style.paddingLeft = '20px'; "
			 * + //
			 * "document.getElementById('content').style.paddingRight = '20px'; "
			 * +
			 * "document.getElementsByTagName('a')[0].setAttribute('href','#');"
			 * +
			 * "document.getElementsByTagName('a')[1].setAttribute('href','#');"
			 * +
			 * "document.getElementsByTagName('a')[2].setAttribute('href','#');"
			 * +
			 * "document.getElementsByTagName('a')[3].setAttribute('href','#');"
			 * +
			 * "document.getElementsByTagName('a')[4].setAttribute('href','#');"
			 * +
			 * "document.getElementsByTagName('a')[0].style.color = '#000000';"+
			 * "document.getElementsByTagName('a')[1].style.color = '#000000';"+
			 * "document.getElementsByTagName('a')[2].style.color = '#000000';"+
			 * "document.getElementsByTagName('a')[3].style.color = '#000000';"+
			 * "document.getElementsByTagName('a')[4].style.color = '#000000';"+
			 * // check if display of local time depends on device
			 * "for(var i=0;i<5;i++) {"+
			 * "var element = document.getElementsByTagName('span')[i].innerHTML;"
			 * + "var date_i=Date.parse(element);"+ "d=new Date(date_i);"+
			 * "document.getElementsByTagName('span')[i].innerHTML=d.toLocaleString();"
			 * + "}"+ "})()"); }
			 */

			// focus webview, to activate input in website
			webView.requestFocus(View.FOCUS_DOWN);

			// Webview Links

		}

	}

}
