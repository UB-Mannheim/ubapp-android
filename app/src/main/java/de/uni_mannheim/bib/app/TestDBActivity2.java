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
 * Database testing class
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;

public class TestDBActivity2 extends Activity {

	// Database Helper
	DatabaseHelper db;
	String dbname = "bibservice";
	Button btn1, btn2, btn3;
	TextView txt1;
	String packagename;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		ActivityRegistry.register(this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_dbactivity2);

		TextView tv2 = (TextView) findViewById(R.id.textView2);

		SharedPreferences settings = getSharedPreferences("preferences", 0);
		String preferencesString = settings.getString("Config_DatabaseModeOn",
				null);
		tv2.setText("BD Mode: " + preferencesString);

		txt1 = (TextView) findViewById(R.id.textView1);

		packagename = this.getPackageName();

		File dbfile = new File("data/data/" + packagename + "/databases/"
				+ this.dbname);

		if (dbfile.exists() == true) {
			txt1.setText("Database Online");
		} else {
			txt1.setText("Database offline");
		}

		btn1 = (Button) findViewById(R.id.button1);

		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				createDatabase();

				finish();
				startActivity(getIntent());
			}
		});

		btn2 = (Button) findViewById(R.id.button2);

		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				deleteDatabase();

				finish();
				startActivity(getIntent());

			}
		});

		btn3 = (Button) findViewById(R.id.button3);

		btn3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * String PREF_FILE_NAME =
				 * "data/data/"+packagename+"/shared_prefs/preferences.xml";
				 * SharedPreferences mPrefs =
				 * getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
				 * 
				 * mPrefs.edit().putString("ConnectionState", "false"); String
				 * CONF__cache_enabled = mPrefs.getString("ConnectionState",
				 * "0");
				 */

				// http://chrisrisner.com/31-Days-of-Android--Day-22%E2%80%93Shared-Preferences

				initConfig();
				/*
				 * SharedPreferences settings =
				 * getSharedPreferences("preferences", 0);
				 * SharedPreferences.Editor preferencesEditor = settings.edit();
				 * preferencesEditor.putString("ConnectionState", "false");
				 * preferencesEditor.commit();
				 */

				finish();
				startActivity(getIntent());

			}
		});

	}

	protected void initConfig() {

		SharedPreferences settings = getSharedPreferences("preferences", 0);
		SharedPreferences.Editor preferencesEditor = settings.edit();

		// set default preferences

		String cfgChanged = settings.getString("Config_ParamsChanged", null);

		if (cfgChanged == null) {

			// initial creation
			preferencesEditor.putString("Config_ParamsChanged", "false");

			preferencesEditor.putString("NetworkConnectionAvailable", "false");
			preferencesEditor.putString("Config_DatabaseModeOn", "false");
			preferencesEditor.putString("Config_NewsRange", "5");
			preferencesEditor.putString("Config_LoadInterval", "1");
			preferencesEditor.commit();
		}

	}

	protected void createDatabase() {

		db = new DatabaseHelper(getApplicationContext());

		// Creating News
		/*
		 * News news1 = new News(1, "Auswertung der rumfrage! 2014 ist online",
		 * "Die Auswertung der Umfrage des Rechenzentrums der Universität Mannheim ist online."
		 * , "http://blog.bib.uni-mannheim.de/Aktuelles/?p=10588", 10588); News
		 * news2 = new News(2, "Diese Woche im Learning Center:",
		 * "Zugangsdaten & Passwörter, Schreibberatung, Workshop Texte gliedern, VPN & WLAN"
		 * , "http://blog.bib.uni-mannheim.de/Aktuelles/?p=10579", 10579); News
		 * news3 = new News(3,
		 * "Bibliothek der DHBW sucht studentische Hilfskraft",
		 * "Die Bibliothek der Dualen Hochschule Baden-Württemberg Mannheim sucht ..."
		 * , "http://blog.bib.uni-mannheim.de/Aktuelles/?p=10569", 10569); News
		 * news4 = new News(4,
		 * "Workshop: Texte logisch gliedern und schriftlich argumentieren",
		 * "Workshop-Reihe Wissenschaftliches Arbeiten für ...",
		 * "http://blog.bib.uni-mannheim.de/Aktuelles/?p=10565", 10565); News
		 * news5 = new News(5,
		 * "Testen Sie “Springer Reference Works” bis 15.11.2014",
		 * "Bis zum 15. November 2014 können im Campusnetz die ...",
		 * "http://blog.bib.uni-mannheim.de/Aktuelles/?p=10546", 10546); // News
		 * news6 = new News(6,
		 * "Learning Center am Samstag, 18. Oktober 2014 geschlossen",
		 * "Am Samstag, 18. Oktober ist das Learning Center ...",
		 * "http://blog.bib.uni-mannheim.de/Aktuelles/?p=10541", 10541);
		 */

		News news1 = new News(
				1,
				"Diese Woche im Learning Center:",
				"Zugangsdaten & Passwörter, Schreibberatung, Workshop Texte gliedern, VPN & WLAN",
				"http://blog.bib.uni-mannheim.de/Aktuelles/?p=10579", 10579);
		News news2 = new News(
				2,
				"Bibliothek der DHBW sucht studentische Hilfskraft",
				"Die Bibliothek der Dualen Hochschule Baden-Württemberg Mannheim sucht ...",
				"http://blog.bib.uni-mannheim.de/Aktuelles/?p=10569", 10569);
		News news3 = new News(
				3,
				"Workshop: Texte logisch gliedern und schriftlich argumentieren",
				"Workshop-Reihe Wissenschaftliches Arbeiten für ...",
				"http://blog.bib.uni-mannheim.de/Aktuelles/?p=10565", 10565);
		News news4 = new News(4,
				"Testen Sie “Springer Reference Works” bis 15.11.2014",
				"Bis zum 15. November 2014 können im Campusnetz die ...",
				"http://blog.bib.uni-mannheim.de/Aktuelles/?p=10546", 10546);
		News news5 = new News(5,
				"Learning Center am Samstag, 18. Oktober 2014 geschlossen",
				"Am Samstag, 18. Oktober ist das Learning Center ...",
				"http://blog.bib.uni-mannheim.de/Aktuelles/?p=10541", 10541);

		// Initializing Entries
		/*
		 * News news1 = new News(1, "", "", "", 0); News news2 = new News(2, "",
		 * "", "", 0); News news3 = new News(3, "", "", "", 0); News news4 = new
		 * News(4, "", "", "", 0); News news5 = new News(5, "", "", "", 0);
		 * 
		 * // Problem String Operations in URL (BlogActivity)
		 */

		// Inserting tags in db, returns row/news_id
		long news1_id = db.createNews(news1);
		long news2_id = db.createNews(news2);
		long news3_id = db.createNews(news3);
		long news4_id = db.createNews(news4);
		long news5_id = db.createNews(news5);

		Log.d("News Count", "News Count: " + db.getAllNews().size());

		// Getting all News: Id/Title
		Log.d("Get News", "Getting All News");

		List<News> allNews = db.getAllNews();
		for (News news : allNews) {
			Log.d("News Id/Title", news.getId() + "/" + news.getTitle());
		}

		// Deleting a News
		// Log.d("Delete News", "Deleting a News");
		// Log.d("News Count", "News Count Before Deleting: " +
		// db.getNewsCount());

		// db.deleteNews(news5_id);

		// Log.d("News Count", "News Count After Deleting: " +
		// db.getNewsCount());

		Log.d("Get News", "Getting All News");

		// Getting all News: Id/Title
		// allNews = db.getAllNews();
		// for (News news : allNews) {
		// Log.d("News Id/Title", news.getId() + "/" + news.getTitle());
		// }

		// Initializing Entries
		Load load1 = new Load(1, "Info Center", 0);
		Load load2 = new Load(2, "Learning Center", 0);
		Load load3 = new Load(3, "Schneckenhof (BWL)", 0);
		Load load4 = new Load(4, "Ehrenhof (Hasso-Plattner-Bibliothek", 0);
		Load load5 = new Load(5, "Bereich A3", 0);
		Load load6 = new Load(6, "Bereich A5", 0);

		// Inserting tags in db, returns row/news_id
		long load1_id = db.createLoad(load1);
		long load2_id = db.createLoad(load2);
		long load3_id = db.createLoad(load3);
		long load4_id = db.createLoad(load4);
		long load5_id = db.createLoad(load5);
		long load6_id = db.createLoad(load6);

		Log.d("Get DateTime", db.getDateTime());

		History history_news = new History(1, 1, db.getDateTime());
		History history_load = new History(2, 2, db.getDateTime());

		long history1_id = db.createHistory(history_news);
		long history2_id = db.createHistory(history_load);

		// Get Object Information
		// db.getObjectInformation(news1);

		// Don't forget to close database connection
		db.closeDB();
	}

	protected void deleteDatabase() {

		String packagename = this.getPackageName();

		File dbfile = new File("data/data/" + packagename + "/databases/"
				+ this.dbname);
		File dbfile2 = new File("data/data/" + packagename + "/databases/"
				+ this.dbname + "-journal");

		if (dbfile.exists() == true) {

			try {
				// boolean del = dbfile.delete();
				dbfile.delete();
				dbfile2.delete();

			} catch (Exception e) {
				Log.e("Error", "Error", e);
			}
		}
	}

}
