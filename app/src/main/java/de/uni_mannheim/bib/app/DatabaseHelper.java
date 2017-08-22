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
 * Creates or modifies the database.
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private boolean log_enabled = false;

	private static Context context;
	// LogCat tag
	private static final String LOG = "DatabaseHelper";

	// Initializing Version getVersionCode(int fallback)
	// static int versionNumber = getVersionCode(1);
	// String versionName = getVersionName("1.3_bookworm");
	
	// Database Version
	// private static final int DATABASE_VERSION = versionNumber;
	private static final int DATABASE_VERSION = 1;
	
	// Database Name
	private static final String DATABASE_NAME = "bibservice";

	// Table Names
	private static final String TABLE_MODULES = "Modules";
	private static final String TABLE_HISTORY = "History";
	private static final String TABLE_LOAD = "Load";
	private static final String TABLE_NEWS = "News";

	// Common column names
	private static final String KEY_ID = "id";

	// Modules Table - column names
	private static final String KEY_MODULES_NAME = "name";
	private static final String KEY_MODULES_SAVE_MODE = "save_mode";

	// History Table - column names
	private static final String KEY_HISTORY_MODULE_ID = "module_id";
	private static final String KEY_HISTORY_LAST_UPDATE = "last_update";

	// Load Table - column names
	private static final String KEY_LOAD_SECTOR = "sector";
	private static final String KEY_LOAD_LOAD = "load";

	// News Table - column names
	private static final String KEY_NEWS_TITLE = "news_id";
	private static final String KEY_NEWS_DESCRIPTION = "description";
	private static final String KEY_NEWS_URL = "url";
	private static final String KEY_NEWS_POST_ID = "post_id";

	// Test Keys as Array
	private static final String[] ALL_KEYS_MODULES = {};
	private static final String[] ALL_KEYS_HISTORY = {};
	private static final String[] ALL_KEYS_LOAD = {};
	private static final String[] ALL_KEYS_NEWS = { KEY_ID, KEY_NEWS_TITLE,
			KEY_NEWS_DESCRIPTION, KEY_NEWS_URL, KEY_NEWS_POST_ID };

	// Table Create Statements

	// Module table create statement
	private static final String CREATE_TABLE_MODULES = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_MODULES
			+ " ("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY NOT NULL UNIQUE, "
			+ KEY_MODULES_NAME
			+ " VARCHAR(50), "
			+ KEY_MODULES_SAVE_MODE
			+ " INTEGER NOT NULL DEFAULT(1)" + ");";

	// History table create statement
	private static final String CREATE_TABLE_HISTORY = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_HISTORY
			+ " ("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY NOT NULL UNIQUE, "
			+ KEY_HISTORY_MODULE_ID
			+ " INTEGER REFERENCES Modules(id), "
			+ KEY_HISTORY_LAST_UPDATE
			+ " DATETIME" + ");";

	// Load table create statement
	private static final String CREATE_TABLE_LOAD = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_LOAD
			+ " ("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
			+ KEY_LOAD_SECTOR
			+ " VARCHAR(100), "
			+ KEY_LOAD_LOAD
			+ " INTEGER"
			+ ");";

	// News table create statement
	private static final String CREATE_TABLE_NEWS = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NEWS
			+ " ("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
			+ KEY_NEWS_TITLE
			+ " VARCHAR(100), "
			+ KEY_NEWS_DESCRIPTION
			+ " VARCHAR(255), "
			+ KEY_NEWS_URL
			+ " VARCHAR(100), "
			+ KEY_NEWS_POST_ID + " INTEGER" + ");";

	// Initialize Modules
	private static String INIT_DATABASE_MODULES_NEWS = "INSERT INTO "
			+ TABLE_MODULES + " (" + KEY_ID + ", " + KEY_MODULES_NAME + ")"
			+ " VALUES (1, 'News');";

	private static String INIT_DATABASE_MODULES_LOAD = "INSERT INTO "
			+ TABLE_MODULES + " (" + KEY_ID + ", " + KEY_MODULES_NAME + ")"
			+ " VALUES (2, 'Load');";

	// Construct
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		DatabaseHelper.context = context;
	}

	// initializeData
	public void initTables(SQLiteDatabase db) {

		db.execSQL(CREATE_TABLE_MODULES);
		db.execSQL(CREATE_TABLE_HISTORY);
		db.execSQL(CREATE_TABLE_LOAD);
		db.execSQL(CREATE_TABLE_NEWS);

		db.execSQL(INIT_DATABASE_MODULES_NEWS);
		db.execSQL(INIT_DATABASE_MODULES_LOAD);

	}

	// onCreate
	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * db.execSQL(CREATE_TABLE_MODULES); db.execSQL(CREATE_TABLE_HISTORY);
		 * db.execSQL(CREATE_TABLE_LOAD); db.execSQL(CREATE_TABLE_NEWS);
		 * 
		 * db.execSQL(INIT_DATABASE_MODULES_NEWS);
		 * db.execSQL(INIT_DATABASE_MODULES_LOAD);
		 */
	}

	// onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// Log Message
		if (log_enabled) {
			Log.w(DatabaseHelper.class.getName(),
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
		}

		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODULES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOAD);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);

		// create new tables
		onCreate(db);
	}

	// CRUD (Create, Read, Update and Delete) Operations

	// ------------------------ "News" Table Methods ----------------//

	// Create News Entry

	public long createNews(News news) {
		// public long createNews(News news, long[] tag_ids) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, news.getId());
		values.put(KEY_NEWS_TITLE, news.getTitle());
		values.put(KEY_NEWS_DESCRIPTION, news.getDescription());
		values.put(KEY_NEWS_URL, news.getUrl());
		values.put(KEY_NEWS_POST_ID, news.getPostId());

		// insert row
		long news_id = db.insert(TABLE_NEWS, null, values);

		/*
		 * // assigning tags to todo for (long tag_id : tag_ids) {
		 * createNewsTag(news_id, tag_id); }
		 */

		db.close();

		return news_id;
	}

	// Get News Entry

	public News getNews(long news_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_NEWS + " WHERE " + KEY_ID
				+ " = " + news_id;

		// Log Message
		if (log_enabled) {
			Log.e(LOG, selectQuery);
		}

		Cursor c = db.rawQuery(selectQuery, null);
		News n = new News();
		
		if (c != null) {

			c.moveToFirst();

			n.setId(c.getInt(c.getColumnIndex(KEY_ID)));
			n.setTitle((c.getString(c.getColumnIndex(KEY_NEWS_TITLE))));
			n.setDescription((c.getString(c.getColumnIndex(KEY_NEWS_DESCRIPTION))));
			n.setUrl((c.getString(c.getColumnIndex(KEY_NEWS_URL))));
			n.setPostId(c.getInt(c.getColumnIndex(KEY_NEWS_POST_ID)));

		}
		
		db.close();

		return n;
	}

	// Get all News Entries

	public List<News> getAllNews() {
		List<News> news = new ArrayList<News>();
		String selectQuery = "SELECT  * FROM " + TABLE_NEWS;

		// Log Message
		if (log_enabled) {
			Log.e(LOG + "...", selectQuery);
		}

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				News n = new News();
				n.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				n.setTitle((c.getString(c.getColumnIndex(KEY_NEWS_TITLE))));
				n.setDescription((c.getString(c
						.getColumnIndex(KEY_NEWS_DESCRIPTION))));
				n.setUrl((c.getString(c.getColumnIndex(KEY_NEWS_URL))));
				n.setPostId(c.getInt(c.getColumnIndex(KEY_NEWS_POST_ID)));

				// adding to news list
				news.add(n);
			} while (c.moveToNext());
		}

		db.close();

		return news;
	}

	// Count News Entries

	public int getNewsCount() {
		String countQuery = "SELECT * FROM " + TABLE_NEWS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		db.close();

		// return count
		return count;
	}

	// Update News Entry

	public int updateNews(News news) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NEWS_TITLE, news.getTitle());
		values.put(KEY_NEWS_DESCRIPTION, news.getDescription());
		values.put(KEY_NEWS_URL, news.getUrl());
		values.put(KEY_NEWS_POST_ID, news.getPostId());

		int dbstate = db.update(TABLE_NEWS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(news.getId()) });
		
		db.close();
		
		return dbstate;
	}

	// Delete News Entry

	public void deleteNews(long news_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NEWS, KEY_ID + " = ?",
				new String[] { String.valueOf(news_id) });
	}

	// Delete All News
	public void deleteNews() {
		List<News> allNews = getAllNews();
		for (News news : allNews) {
			deleteNews(news.getId());
		}
	}

	// ------------------------ "Load" Table Methods ----------------//

	// Create Load Entry

	public long createLoad(Load load) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, load.getId());
		values.put(KEY_LOAD_SECTOR, load.getSector());
		values.put(KEY_LOAD_LOAD, load.getLoad());

		long news_id = db.insert(TABLE_LOAD, null, values);

		db.close();

		return news_id;
	}

	// Get Load Entry

	public Load getLoad(long load_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_LOAD + " WHERE " + KEY_ID
				+ " = " + load_id;

		// Log Message
		if (log_enabled) {
			Log.e(LOG, selectQuery);
		}

		Cursor c = db.rawQuery(selectQuery, null);
		Load l = new Load();
		
		if (c != null) {
			
			c.moveToFirst();

			l.setId(c.getInt(c.getColumnIndex(KEY_ID)));
			l.setSector((c.getString(c.getColumnIndex(KEY_LOAD_SECTOR))));
			l.setLoad((c.getInt(c.getColumnIndex(KEY_LOAD_LOAD))));
		}
		
		db.close();

		return l;
	}

	// Get all Load Entries

	public List<Load> getAllLoads() {
		List<Load> loads = new ArrayList<Load>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOAD;

		// Log Message
		if (log_enabled) {
			Log.e(LOG, selectQuery);
		}

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Load l = new Load();
				l.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				l.setSector((c.getString(c.getColumnIndex(KEY_LOAD_SECTOR))));
				l.setLoad((c.getInt(c.getColumnIndex(KEY_LOAD_LOAD))));

				// adding to news list
				loads.add(l);
			} while (c.moveToNext());
		}

		db.close();

		return loads;
	}

	// Count Load Entries

	public int getLoadCount() {
		String countQuery = "SELECT * FROM " + TABLE_LOAD;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();
		// return count

		db.close();

		return count;
	}

	// Update Load Entry

	public int updateLoad(Load load) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LOAD_SECTOR, load.getSector());
		values.put(KEY_LOAD_LOAD, load.getLoad());

		int dbstate =  db.update(TABLE_LOAD, values, KEY_ID + " = ?",
				new String[] { String.valueOf(load.getId()) });
		
		db.close();
		
		return dbstate;
	}

	// Delete Load Entry

	public void deleteLoad(long load_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_LOAD, KEY_ID + " = ?",
				new String[] { String.valueOf(load_id) });
	}

	// ------------------------ "Modules" Table Methods ----------------//

	// Create Module Entry

	public long createModule(Module module) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, module.getId());
		values.put(KEY_LOAD_SECTOR, module.getName());
		values.put(KEY_LOAD_LOAD, module.getSaveMode());

		long module_id = db.insert(TABLE_MODULES, null, values);

		db.close();

		return module_id;
	}

	// Get Module Entry

	public Module getModule(long module_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_MODULES + " WHERE "
				+ KEY_ID + " = " + module_id;

		// Log Message
		if (log_enabled) {
			Log.e(LOG, selectQuery);
		}

		Cursor c = db.rawQuery(selectQuery, null);
		Module m = new Module();
		
		if (c != null) {
			
			c.moveToFirst();
		
			m.setId(c.getInt(c.getColumnIndex(KEY_ID)));
			m.setName((c.getString(c.getColumnIndex(KEY_MODULES_NAME))));
			m.setSaveMode((c.getInt(c.getColumnIndex(KEY_MODULES_SAVE_MODE))));
			
		}
		
		db.close();

		return m;
	}

	// Get all Module Entries

	public List<Module> getAllModules() {
		List<Module> modules = new ArrayList<Module>();
		String selectQuery = "SELECT  * FROM " + TABLE_MODULES;

		// Log Message
		if (log_enabled) {
			Log.e(LOG, selectQuery);
		}

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Module m = new Module();
				m.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				m.setName((c.getString(c.getColumnIndex(KEY_MODULES_NAME))));
				m.setSaveMode((c.getInt(c.getColumnIndex(KEY_MODULES_SAVE_MODE))));

				// adding to news list
				modules.add(m);
			} while (c.moveToNext());
		}

		db.close();

		return modules;
	}

	// Count Module Entries

	public int getModuleCount() {
		String countQuery = "SELECT * FROM " + TABLE_MODULES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();
		// return count

		db.close();

		return count;
	}

	// Update Module Entry

	public int updateModules(Module module) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_MODULES_NAME, module.getName());
		values.put(KEY_MODULES_SAVE_MODE, module.getSaveMode());

		int dbstate = db.update(TABLE_MODULES, values, KEY_ID + " = ?",
				new String[] { String.valueOf(module.getId()) });
		
		db.close();
		
		return dbstate;
	}

	// Delete Module Entry

	public void deleteModule(long module_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MODULES, KEY_ID + " = ?",
				new String[] { String.valueOf(module_id) });
	}

	// ------------------------ "History" Table Methods ----------------//

	// Create History Entry

	public long createHistory(History history) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, history.getId());
		values.put(KEY_HISTORY_MODULE_ID, history.getModuleId());
		values.put(KEY_HISTORY_LAST_UPDATE, history.getLastUpdate());

		long history_id = db.insert(TABLE_HISTORY, null, values);

		db.close();

		return history_id;
	}

	// Get History Entry

	public History getHistory(long history_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_HISTORY + " WHERE "
				+ KEY_ID + " = " + history_id;

		// Log Message
		if (log_enabled) {
			Log.e(LOG, selectQuery);
		}

		Cursor c = db.rawQuery(selectQuery, null);
		History h = new History();
		
		if (c != null) {
			
			c.moveToFirst();
			
			h.setId(c.getInt(c.getColumnIndex(KEY_ID)));
			h.setModuleId((c.getInt(c.getColumnIndex(KEY_HISTORY_MODULE_ID))));
			h.setLastUpdate((c.getString(c.getColumnIndex(KEY_HISTORY_LAST_UPDATE))));
			
		}
		
		db.close();
		
		return h;
		
	}

	// Get all History Entries

	public List<History> getAllHistory() {
		List<History> histories = new ArrayList<History>();
		String selectQuery = "SELECT  * FROM " + TABLE_HISTORY;

		// Log Message
		if (log_enabled) {
			Log.e(LOG, selectQuery);
		}

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				History h = new History();
				h.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				h.setModuleId((c.getInt(c.getColumnIndex(KEY_HISTORY_MODULE_ID))));
				h.setLastUpdate((c.getString(c
						.getColumnIndex(KEY_HISTORY_LAST_UPDATE))));

				// adding to news list
				histories.add(h);
			} while (c.moveToNext());
		}

		db.close();

		return histories;
	}

	// Count History Entries

	public int getHistoryCount() {
		String countQuery = "SELECT * FROM " + TABLE_HISTORY;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();
		// return count

		db.close();

		return count;
	}

	// Update History Entry

	public int updateHistory(History history) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_HISTORY_MODULE_ID, history.getModuleId());
		values.put(KEY_HISTORY_LAST_UPDATE, history.getLastUpdate());

		// updating row
		int dbstate = db.update(TABLE_HISTORY, values, KEY_ID + " = ?",
				new String[] { String.valueOf(history.getId()) });
		
		db.close();
		
		return dbstate;
	}

	// Delete History Entry

	public void deleteHistory(long history_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_HISTORY, KEY_ID + " = ?",
				new String[] { String.valueOf(history_id) });
	}

	// ------------------------ "Additional" Methods ----------------//

	// Closing Database

	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	// Get DateTime
	public String getDateTime() {
		// private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date(System.currentTimeMillis());
		dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
		return dateFormat.format(date);
	}

	// Object Information
	public void getObjectInformation(Object object) {

		// Log Message
		if (log_enabled) {
			Log.e("DatabaseHelper", "Object found: "
					+ object.getClass().getSimpleName()); // News
		}

		Field[] fields = object.getClass().getDeclaredFields();
		Method[] methods = object.getClass().getDeclaredMethods();

		// Log Message
		if (log_enabled) {
			Log.e("DatabaseHelper", "Object Fields: ");
			for (Field f : fields) {
				Log.d("Field", f.getName() + " (" + f.getType() + ") "); // title
																			// (class
																			// java.lang.String)
			}

			Log.e("DatabaseHelper", "Object Methods: ");
			for (Method m : methods) {
				Log.d("Method", m.getName() + " (" + m.getReturnType() + ") "); // getTitle
																				// (class
																				// java.lang.String)
			}
		}

	}
	
	public static int getVersionCode(int v) {

		int version = v;
	    try {
	        version = context.getPackageManager().getPackageInfo(
	        		context.getPackageName(), 0).versionCode;
	    } catch (NameNotFoundException e) {
	        // Huh? Really?
	    }
	    return version;
	}
	
	public String getVersionName(String n) {

		String name = n;
	    try {
	        name = context.getPackageManager().getPackageInfo(
	        		context.getPackageName(), 0).versionName;
	    } catch (NameNotFoundException e) {
	        // Huh? Really?
	    }
	    return name;
	}
}