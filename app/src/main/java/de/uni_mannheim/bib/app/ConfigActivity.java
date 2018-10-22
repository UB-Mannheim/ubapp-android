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
 * App configuration, possible choices
 *		Caching (on/off)
 * 		Count of News entries (5/10/15)
 * 		Start module (Menu, Web, Primo, News, Load)
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import java.io.File;
import java.util.List;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;

public class ConfigActivity<E> extends AppCompatActivity {

	private boolean log_enabled = false;
	
	String db = "bibservice";

	RadioButton rb, rb0, rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8, rb9, rb10,
			rb11, rb12, rb13;
	RadioGroup rg1, rg2, rg3;
	CheckBox cb1;
	Button btn1, btn2, btn3, btn4;
	TextView tv1, tv2, tv3, tv4;

	TextView tv;

	boolean db_active;
	boolean restart_app;
	int startup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		ActivityRegistry.register(this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);

		// Customized Actionbar (Color, Title)
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		// Deactivated, not returning to main
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.library_bg)));
		actionBar.setTitle(R.string.app_name);

		// Defining Buttons and ~groups
		btn3 = (Button) findViewById(R.id.button3);
	
		rb6 = (RadioButton) findViewById(R.id.radio6);
		rb7 = (RadioButton) findViewById(R.id.radio7);
		rb8 = (RadioButton) findViewById(R.id.radio8);
		rb9 = (RadioButton) findViewById(R.id.radio9);
		rb10 = (RadioButton) findViewById(R.id.radio10);
		rb11 = (RadioButton) findViewById(R.id.radio11);
		rb12 = (RadioButton) findViewById(R.id.radio12);
		rb13 = (RadioButton) findViewById(R.id.radio13);

		cb1 = (CheckBox) findViewById(R.id.checkBox1);
		btn1 = (Button) findViewById(R.id.button1);
		
		// Get Preferences
		SharedPreferences settings = getSharedPreferences("preferences", 0);
		SharedPreferences.Editor preferencesEditor = settings.edit();

		restart_app = false;
		
		// Any existing databases?
		if (!existsDB(db)) {

			// Log Message
			if (log_enabled) {
				Log.e("ConfigActivity", "No Database File exists.");
			}

			// Caching inactive
			preferencesEditor.putString("Config_DatabaseModeOn", "false");
			preferencesEditor.commit();

			cb1.setChecked(false);

		} else {

			// Log Message
			if (log_enabled) {
				Log.e("ConfigActivity", "Database File exists.");
			}

			// caching active
			preferencesEditor.putString("Config_DatabaseModeOn", "true");
			preferencesEditor.commit();

			// check checkbox
			cb1.setChecked(true);

		}

		String news_range = settings.getString("Config_NewsRange", "0");
		// String load_interval = settings.getString("Config_LoadInterval",
		// null);

		// catch error "null" as integer in live debug
		if (news_range == null) {
			news_range = "0";
		}

		// check radio buttons
		if (Integer.valueOf(news_range) > 10) {
			rb8.setChecked(true);
		} else if (Integer.valueOf(news_range) > 5) {
			rb7.setChecked(true);
		} else {
			rb6.setChecked(true);
		}

		
		// If is set StartActivity

		// Log Message
		if (log_enabled) {
			Log.e("ConfigActivity", "StartActvity Set");
		}

		startup = Integer.valueOf(settings.getString(
				"Config_StartUpActivity", "0"));

		switch (startup) {
		case 0:
			rb9.setChecked(true);
			break;
		case 1:
			rb10.setChecked(true);
			break;
		case 2:
			rb11.setChecked(true);
			break;
		case 3:
			rb12.setChecked(true);
			break;
		case 4:
			rb13.setChecked(true);
			break;
		default:
			rb9.setChecked(true);
			break;
		}

		// onClick Listener Button1 :: db active || inactive
		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Checking Form Elements: CheckBox
				final CheckBox cb1 = (CheckBox) findViewById(R.id.checkBox1);

				SharedPreferences settings = getSharedPreferences(
						"preferences", 0);
				SharedPreferences.Editor preferencesEditor = settings.edit();

				// 2do: creating a default database should be
				// avoided and transferred to LoadActivity/BlogActivity or
				// DataCollector
				
				if (!cb1.isChecked()) {
					deleteDatabase();
					preferencesEditor.putString("Config_DatabaseModeOn", "false");
					preferencesEditor.commit();
				}
				
				if (cb1.isChecked()) {
					createDatabase();
					preferencesEditor.putString("Config_DatabaseModeOn", "true");
					preferencesEditor.commit();
				}

				// Checking Form Elements: RadioButtons
				final RadioButton r8 = (RadioButton) findViewById(R.id.radio8);
				final RadioButton r7 = (RadioButton) findViewById(R.id.radio7);
				final RadioButton r6 = (RadioButton) findViewById(R.id.radio6);

				try {

					if (r6.isChecked()) {
						preferencesEditor.putString("Config_NewsRange", "5");
					}
					if (r7.isChecked()) {
						preferencesEditor.putString("Config_NewsRange", "10");
					}
					if (r8.isChecked()) {
						preferencesEditor.putString("Config_NewsRange", "15");
					}

					preferencesEditor.commit();

				} catch (Exception e) {

				}

				
				// Checking Form Elements: RadioButtons
				final RadioButton r9 = (RadioButton) findViewById(R.id.radio9);
				final RadioButton r10 = (RadioButton) findViewById(R.id.radio10);
				final RadioButton r11 = (RadioButton) findViewById(R.id.radio11);
				final RadioButton r12 = (RadioButton) findViewById(R.id.radio12);
				final RadioButton r13 = (RadioButton) findViewById(R.id.radio13);

				try {

					if (r9.isChecked()) {
						preferencesEditor.remove("Config_StartUpActivity");
						
						if(startup!=0) {
							restart_app = true;
						}
					}
					if (r10.isChecked()) {
						preferencesEditor.putString("Config_StartUpActivity",
								"1");
						
						if(startup!=1) {
							restart_app = true;
						}
					}
					if (r11.isChecked()) {
						preferencesEditor.putString("Config_StartUpActivity",
								"2");
						
						if(startup!=2) {
							restart_app = true;
						}
					}
					if (r12.isChecked()) {
						preferencesEditor.putString("Config_StartUpActivity",
								"3");
						
						if(startup!=3) {
							restart_app = true;
						}
					}
					if (r13.isChecked()) {
						preferencesEditor.putString("Config_StartUpActivity",
								"4");
						
						if(startup!=4) {
							restart_app = true;
						}
					}

					preferencesEditor.commit();

				} catch (Exception e) {
					
					// Log Message
					if (log_enabled) {
						Log.e(this.getClass().toString(), e.toString());
					}
				}


				// If App is restarted

				if(restart_app) {
					new AlertDialog.Builder(ConfigActivity.this)
					.setTitle(getString(R.string.dialog_configActivity_startup_changed__title))
					.setMessage(getString(R.string.dialog_configActivity_startup_changed__message))
					.setPositiveButton(getString(R.string.dialog_configActivity_startup_changed__now),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									
									SharedPreferences settings = getSharedPreferences("preferences", 0);
									SharedPreferences.Editor preferencesEditor = settings.edit();

									preferencesEditor.putString("Config_AppState", "shutdown");
									preferencesEditor.commit();

									// Close all Activities properly
									ActivityRegistry.getAll();
									ActivityRegistry.finishAll();
									
									// Set Message
									Toast.makeText(ConfigActivity.this,
											getString(R.string.dialog_configActivity_restart_app__message), Toast.LENGTH_SHORT).show();
									
									// and restart app
									Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
								    startActivity(i);
								}
							})
					.setNegativeButton(getString(R.string.dialog_configActivity_startup_changed__later),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									
									// set message and
									Toast.makeText(ConfigActivity.this,
											getString(R.string.dialog_configActivity_saved_settings__message), Toast.LENGTH_SHORT).show();
									// goto upper activity
									finish();
								}
							}).show();
				}
				
				if(restart_app==false) {
					// Set Toast and get back to original Activity
					Toast.makeText(ConfigActivity.this,
							getString(R.string.dialog_configActivity_saved_settings__message), Toast.LENGTH_SHORT).show();
					finish();
				}
				
			}
		});

	}

	// generalized "Button onClick Listener" (not used yet. just prepared)

	/*
    public void addListenerOnButton(final Button button, final RadioGroup rgroup) {

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// get selected radio button from radioGroup
				int selectedId = rgroup.getCheckedRadioButtonId();

				// find the radiobutton by returned id
				rb = (RadioButton) findViewById(selectedId);

				int id = button.getId();
				String name = String.valueOf(button.findViewById(id));
				String nname = String
						.valueOf(button.getContext().getString(id));
				Toast.makeText(ConfigActivity.this,
				// rb.getText()
						nname, Toast.LENGTH_SHORT).show();

				// set database entry and

				// reload Activity
				finish();
				startActivity(getIntent());
			}

		});

	}
    */

    public boolean existsDB(String dbname) {

		boolean db_exists = false;

		String name = this.getPackageName();
		File dbfile = new File("data/data/" + name + "/databases/" + dbname);

		if (dbfile.exists()) {
			db_exists = true;
		}

		return db_exists;
	}

	protected void createDatabase() {

		String packagename = this.getPackageName();

		File dbfile = new File("data/data/" + packagename + "/databases/" + db);

		if (!(dbfile.exists())) {

			DatabaseHelper db = new DatabaseHelper(getApplicationContext());
			db.initTables(db.getWritableDatabase());

			// Creating Standard Set of News
			News news1 = new News(1, "", "", "", 0);
			News news2 = new News(2, "", "", "", 0);
			News news3 = new News(3, "", "", "", 0);
			News news4 = new News(4, "", "", "", 0);
			News news5 = new News(5, "", "", "", 0);
			News news6 = new News(6, "", "", "", 0);
			News news7 = new News(7, "", "", "", 0);
			News news8 = new News(8, "", "", "", 0);
			News news9 = new News(9, "", "", "", 0);
			News news10 = new News(10, "", "", "", 0);
			News news11 = new News(11, "", "", "", 0);
			News news12 = new News(12, "", "", "", 0);
			News news13 = new News(13, "", "", "", 0);
			News news14 = new News(14, "", "", "", 0);
			News news15 = new News(15, "", "", "", 0);

			long news1_id = db.createNews(news1);
			long news2_id = db.createNews(news2);
			long news3_id = db.createNews(news3);
			long news4_id = db.createNews(news4);
			long news5_id = db.createNews(news5);
			long news6_id = db.createNews(news6);
			long news7_id = db.createNews(news7);
			long news8_id = db.createNews(news8);
			long news9_id = db.createNews(news9);
			long news10_id = db.createNews(news10);
			long news11_id = db.createNews(news11);
			long news12_id = db.createNews(news12);
			long news13_id = db.createNews(news13);
			long news14_id = db.createNews(news14);
			long news15_id = db.createNews(news15);
			
			// Creating Standard Set of Load Entries
			Load load1 = new Load(1, "Info Center", -1);
			Load load2 = new Load(2, "Learning Center", -1);
			Load load3 = new Load(3, "Schneckenhof (BWL)", -1);
			Load load4 = new Load(4, "Ehrenhof (Hasso-Plattner-Bibliothek)", -1);
			Load load5 = new Load(5, "Bereich A3", -1);
			Load load6 = new Load(6, "Bereich A5", -1);

			long load1_id = db.createLoad(load1);
			long load2_id = db.createLoad(load2);
			long load3_id = db.createLoad(load3);
			long load4_id = db.createLoad(load4);
			long load5_id = db.createLoad(load5);
			long load6_id = db.createLoad(load6);

			
			// Log Message
			if (log_enabled) {
				Log.d("Get DateTime", db.getDateTime());
			}

			// Creating Standard Set of Load Entries
			History history_news = new History(1, 1, db.getDateTime());
			History history_load = new History(2, 2, db.getDateTime());

			long history1_id = db.createHistory(history_news);
			long history2_id = db.createHistory(history_load);

			// Get Object Information
			// db.getObjectInformation(news1);

			// Close database connection
			db.closeDB();

		}
	}

	protected void createInitialDatabase(List<News> initNews,
			List<Load> initLoad) {

		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		db.initTables(db.getWritableDatabase());

		// Creating Standard Set of News
		News news1 = initNews.get(0);
		News news2 = initNews.get(1);
		News news3 = initNews.get(2);
		News news4 = initNews.get(3);
		News news5 = initNews.get(4);

		long news1_id = db.createNews(news1);
		long news2_id = db.createNews(news2);
		long news3_id = db.createNews(news3);
		long news4_id = db.createNews(news4);
		long news5_id = db.createNews(news5);

		// Creating Standard Set of Load Entries
		Load load1 = initLoad.get(0);
		Load load2 = initLoad.get(1);
		Load load3 = initLoad.get(2);
		Load load4 = initLoad.get(3);
		Load load5 = initLoad.get(4);
		Load load6 = initLoad.get(5);

		long load1_id = db.createLoad(load1);
		long load2_id = db.createLoad(load2);
		long load3_id = db.createLoad(load3);
		long load4_id = db.createLoad(load4);
		long load5_id = db.createLoad(load5);
		long load6_id = db.createLoad(load6);

		// Creating Standard Set of Load Entries
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

		File dbfile = new File("data/data/" + packagename + "/databases/" + db);
		File dbfile2 = new File("data/data/" + packagename + "/databases/" + db
				+ "-journal");

		if (dbfile.exists()) {

			try {
				// boolean del = dbfile.delete();
				dbfile.delete();
				dbfile2.delete();

			} catch (Exception e) {
				Log.e("Error", "Error", e);
			}
		}
	}

	protected void getConfig() {
		// latest from Screen

	}

	protected void setConfig() {
		// write to file
	}

}
