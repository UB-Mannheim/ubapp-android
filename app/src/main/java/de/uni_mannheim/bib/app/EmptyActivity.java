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
 * Dummy activity.
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class EmptyActivity extends Activity {
	
	private boolean log_enabled = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		ActivityRegistry.register(this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empty);
		
		finish();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.empty, menu);

		return true;
	}

	/*
	 * SNIPPET POOL::LOAD
	 * 
	 * DELETE BEFORE RELEASE DELETE BEFORE RELEASE DELETE BEFORE RELEASE
	 * 
	 * 
	 * webViewBB_SN_IC.setBackgroundColor(0x00000000);
	 * webViewBB_SN_IC.loadUrl("javascript:(function() { " +
	 * "document.getElementById('mobile_trailer').style.backgroundColor = 'transparent'; "
	 * +
	 * "document.getElementsByTagName('body')[0].style.backgroundColor = 'transparent';"
	 * + "})()"); if (Build.VERSION.SDK_INT >= 11)
	 * webViewBB_SN_IC.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
	 * 
	 * webViewBB_SN_IC.setWebViewClient(new WebViewClient() { public void
	 * onPageFinished(WebView view, String url) {
	 * webViewBB_SN_IC.setBackgroundColor(0x00000000); if (Build.VERSION.SDK_INT
	 * >= 11) webViewBB_SN_IC.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null); }
	 * });
	 */

	/*
	 * SNIPPET POOL::MAIN
	 * 
	 * DELETE BEFORE RELEASE DELETE BEFORE RELEASE DELETE BEFORE RELEASE
	 * 
	 * 
	 * // get width and height of display
	 * 
	 * DisplayMetrics displaymetrics = new DisplayMetrics();
	 * getWindowManager().getDefaultDisplay().getMetrics(displaymetrics); int
	 * height = displaymetrics.heightPixels; int width =
	 * displaymetrics.widthPixels;
	 * 
	 * wwwButton.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * String url = "http://www.bib.uni-mannheim.de/mobile/de/1.html";
	 * 
	 * Intent i = new Intent(MainActivity.this, WebviewActivity.class);
	 * i.putExtra("url", url); i.putExtra("action", "www");
	 * 
	 * startActivity(i);
	 * 
	 * // Intent websiteActivity = new Intent(getBaseContext(),
	 * WebsiteActivity.class); // startActivity(websiteActivity); } });
	 * 
	 * catalogueButton.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * String url =
	 * "http://primo.bib.uni-mannheim.de/primo_library/libweb/action/search.do?vid=MAN_MOBILE"
	 * ;
	 * 
	 * Intent i = new Intent(MainActivity.this, WebviewActivity.class);
	 * i.putExtra("url", url); i.putExtra("action", "catalogue");
	 * 
	 * startActivity(i);
	 * 
	 * // Intent catalogueActivity = new Intent(getBaseContext(),
	 * CatalogueActivity.class); // startActivity(catalogueActivity); } });
	 * 
	 * newsButton.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * // etvl Link aendern ... siehe Blog-Anzeige in Webseite String url =
	 * "http://www.bib.uni-mannheim.de/mobile/de/3_app.html";
	 * 
	 * Intent i = new Intent(MainActivity.this, WebviewActivity.class);
	 * i.putExtra("url", url); i.putExtra("action", "news");
	 * 
	 * startActivity(i);
	 * 
	 * // Intent newsActivity = new Intent(getBaseContext(),
	 * NewsActivity.class); // startActivity(newsActivity);
	 * 
	 * // String debug = "news not available yet"; // debugText.setText(debug);
	 * }
	 * 
	 * 
	 * // @Override // public void onClick(View v) { // // Intent i = new
	 * Intent(getBaseContext(), RSSActivity.class); // startActivity(i);
	 * 
	 * //}
	 * 
	 * 
	 * });
	 * 
	 * chatButton.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * String url = "http://www.bib.uni-mannheim.de/webim/client.php";
	 * 
	 * Intent i = new Intent(MainActivity.this, WebviewActivity.class);
	 * i.putExtra("url", url); i.putExtra("action", "chat");
	 * 
	 * startActivity(i);
	 * 
	 * // String debug = "chat not available yet"; // debugText.setText(debug);
	 * }
	 * 
	 * });
	 * 
	 * statisticButton.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * // String url =
	 * "http://www.bib.uni-mannheim.de/bereichsauslastung/index.php?bereich=all"
	 * ; // // String url =
	 * "http://www.bib.uni-mannheim.de/mobile/de/63_app.html";
	 * 
	 * // Intent i = new Intent(MainActivity.this, WebviewActivity.class); //
	 * i.putExtra("url", url); // i.putExtra("action", "statistic");
	 * 
	 * 
	 * 
	 * Intent i = new Intent(getBaseContext(), LoadActivity.class);
	 * startActivity(i);
	 * 
	 * 
	 * // Intent newsActivity = new Intent(getBaseContext(),
	 * StatisticActivity.class); // startActivity(newsActivity); }
	 * 
	 * });
	 * 
	 * reserveButton.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * // String url = ""; // // Intent i = new Intent(StartActivity.this,
	 * WebviewActivity.class); // i.putExtra("url", url); //
	 * i.putExtra("action", "reservation"); // // startActivity(i);
	 * 
	 * 
	 * // String debug = "smartboards reservation not available yet"; //
	 * debugText.setVisibility(1); // debugText.setText(debug);
	 * 
	 * 
	 * //Intent i = new Intent(getBaseContext(), EmptyActivity.class); // Intent
	 * i = new Intent(getBaseContext(), GridActivity.class);
	 * 
	 * // Intent i = new Intent(getBaseContext(), HttpActivity.class);
	 * 
	 * // Intent i = new Intent(getBaseContext(), RSSActivity.class);
	 * //startActivity(i);
	 * 
	 * }
	 * 
	 * });
	 */

	/*
	 * SNIPPET POOL::WEBVIEW
	 * 
	 * DELETE BEFORE RELEASE DELETE BEFORE RELEASE DELETE BEFORE RELEASE
	 * 
	 * 
	 * 
	 * import android.util.DisplayMetrics;
	 * 
	 * @Override protected void onCreate(Bundle savedInstanceState) {
	 * 
	 * 
	 * // screen layout for main activity
	 * 
	 * super.onCreate(savedInstanceState);
	 * setContentView(R.layout.activity_webview);
	 * 
	 * 
	 * DisplayMetrics displaymetrics = new DisplayMetrics();
	 * getWindowManager().getDefaultDisplay().getMetrics(displaymetrics); height
	 * = displaymetrics.heightPixels; width = displaymetrics.widthPixels; //
	 * Log.v("system-XY", String.valueOf(height)+" - "+String.valueOf(width));
	 * 
	 * // ACTION BAR TRY N ERROR
	 * 
	 * // Show the Up button in the action bar. // setupActionBar();
	 * 
	 * // Show back button, if parent defined android.support.v7.app.ActionBar
	 * actionBar = getSupportActionBar();
	 * actionBar.setDisplayHomeAsUpEnabled(true);
	 * 
	 * //change Color actionBar.setBackgroundDrawable(new
	 * ColorDrawable(Color.parseColor("#990000")));
	 * 
	 * //title actionBar.setTitle("UB Mannheim");
	 * 
	 * // hide action bar // android.support.v7.app.ActionBar actionBar =
	 * getSupportActionBar(); // actionBar.hide();
	 * 
	 * url = getIntent().getExtras().getString("url"); action =
	 * getIntent().getExtras().getString("action");
	 * 
	 * // Cat Log Writer // Log.e("UB_Log", "getIntent->String");
	 * 
	 * // if(url.contains("mobile")) { if(action.contains("www")) {
	 * 
	 * // TEST, kann man spaeter loswerden :)
	 * 
	 * // Log.d("UB_Log", "INNER IF"); // Log.v("UB_Log", "Log");
	 * 
	 * //? Remove Title Bar
	 * //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 * 
	 * //? Remove Notification Bar
	 * //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
	 * 
	 * // Actionbar verstecken // actionBar.hide();
	 * 
	 * // Actionbar TRANSPARENT // actionBar.setBackgroundDrawable(new
	 * ColorDrawable(Color.TRANSPARENT)); }
	 * 
	 * webView = (WebView) findViewById(R.id.webView1); progressBar =
	 * (ProgressBar) findViewById(R.id.progressBar1);
	 * 
	 * WebSettings wset = webView.getSettings();
	 * wset.setJavaScriptEnabled(true);
	 * 
	 * // bei Hochkant Ansicht wird der Bildschirm erweitert, das wollen wir //
	 * fuer folgende Webviews nicht: // - www/chat/ //
	 * wset.setUseWideViewPort(true);
	 * 
	 * //wset.setRenderPriority(RenderPriority.HIGH);
	 * //wset.setCacheMode(WebSettings.LOAD_NO_CACHE);
	 * webView.setWebViewClient(new myWebClient()); webView.loadUrl(url); }
	 * 
	 * 
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * 
	 * // Inflate the menu; this adds items to the action bar if it is present.
	 * // getMenuInflater().inflate(R.menu.webview, menu); // return true;
	 * 
	 * // Inflate the custom menu ; Erweiterung des Standard Menus rechts oben
	 * // nach Vorlage menu/webview_main.xml //
	 * https://developer.android.com/training
	 * /basics/actionbar/adding-buttons.html
	 * 
	 * getMenuInflater().inflate(R.menu.webview_activity_actions, menu); return
	 * super.onCreateOptionsMenu(menu);
	 * 
	 * 
	 * }
	 */
}
