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
 * Creates and returns valid datasets.
 * 		News (online/offline)
 * 		Load (online/offline)
 * 		...
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

public class DataCollector {

	private boolean log_enabled = false;

	private String bnews[][];

	/*
	 * Klasse zum Sammeln der anzuzeigenden Daten News Online [OK] / News Lokal
	 * [OK] Load Online [OK] / Load Lokal [OK]
	 * 
	 * Die Rückgabe soll als News Liste und [String[] --> OK, wird erwartet]
	 * Load Liste [OK] möglich sein
	 * 
	 * Funktionen :: Auslesen von Online Sourcen / Datenbanken / Textdateien >>
	 * using BlogParser and URLParser [OK]
	 * 
	 * Entgegennehmen von String[][] [OK] / List<T> (<News> und <Load>) [OK]
	 * 
	 * Abgleichen der Inhalte >> Zur Verfuegung stellen der "richtigen" Daten
	 * [OK]
	 */
	public DataCollector() {

	}

	public List<String[]> getNews(DatabaseHelper dbh, int news_range) {

		// General Collector - if Network alive and Database available

		BlogParser b = new BlogParser();
		bnews = b.getBlogEntries(b.url, news_range);

		List<News> dsNews = createDatasetNews(bnews);

		// Log Message
		if (log_enabled) {
			Log.e("BlogActivity", "DEBUG dsNews ---------------------------");
			for (News n : dsNews) {
				Log.e("BlogActivity", n.getId() + " - " + n.getPostId() + " - "
						+ n.getTitle().substring(0, 20));
			}
		}

		List<News> dbNews = dbh.getAllNews();

		// Log Message
		if (log_enabled) {
			Log.e("BlogActivity", "DEBUG dbNews ---------------------------");
			for (News n : dbNews) {
				Log.e("BlogActivity", n.getId() + " - " + n.getPostId() + " - "
						+ n.getTitle().substring(0, 20));
			}
		}

		News LatestNews = dsNews.get(0);
		int dsMaxId = LatestNews.getPostId();

		News LatestDBNews = dbNews.get(0);
		int dbMaxId = LatestDBNews.getPostId();

		// IF CONFIG == CACHE_ACTIVE

		if ((dsMaxId == dbMaxId) && (dsNews.size() == dbNews.size())) {

			// do nothing, all news up to date

			// show entries from db

			// Log Message
			if (log_enabled) {
				Log.e("BlogActivity", "NEWS Complete");
			}

		} else {
/*
			Log.e("BlogActivity", "NEWS Update " + "\n >> dbMax:" + dbMaxId
					+ "\n >> dsMax:" + dsMaxId);
*/
			// check for new entries

			// update database

			dbh.deleteNews();

			for (News n : dsNews) {
				try {
					dbh.createNews(n);
				} catch (Exception e) {

					// Log Message
					if (log_enabled) {
						Log.e("SQL Error on Update: ", e.toString());
					}
				}
			}

			// show entries

			List<News> dbNewsNeu = dbh.getAllNews();

			// Log Message
			if (log_enabled) {
				Log.e("BlogActivity",
						"DATABASE Updated ---------------------------");
				for (News n : dbNewsNeu) {
					Log.e("BlogActivity", n.getPostId() + " - "
							+ n.getTitle().substring(0, 20));
				}
			}

		}

		// String[] captionList = new String[5];
		// String[] contentList = new String[5];

		// captionList[0] = bnews[0][0];
		// captionList[1] = bnews[1][0];
		// captionList[2] = bnews[2][0];
		// captionList[3] = bnews[3][0];
		// captionList[4] = bnews[4][0];

		// contentList[0] = bnews[0][3];
		// contentList[1] = bnews[1][3];
		// contentList[2] = bnews[2][3];
		// contentList[3] = bnews[3][3];
		// contentList[4] = bnews[4][3];

		String[] captionList = new String[news_range];
		String[] contentList = new String[news_range];

		for (int n = 0; n < news_range; n++) {
			captionList[n] = bnews[n][0];
			contentList[n] = bnews[n][3];
		}

		List<String[]> newsList = new ArrayList<String[]>();

		newsList.add(captionList);
		newsList.add(contentList);

		return newsList;

	}

	public List<News> getNewsFromWWW(int news_range) {

		BlogParser b = new BlogParser();
		bnews = b.getBlogEntries(b.url, news_range);

		List<News> dsNews = createDatasetNews(bnews);

		// Log Message
		if (log_enabled) {
			Log.e("BlogActivity - getNewsFromWWW",
					"DEBUG dsNews ---------------------------");
			for (News n : dsNews) {
				Log.e("BlogActivity", n.getId() + " - " + n.getPostId() + " - "
						+ n.getTitle().substring(0, 20));
			}
		}

		return dsNews;

	}

	public List<News> getNewsFromDB(DatabaseHelper dbh) {

		List<News> dbNews = dbh.getAllNews();

		// Log Message
		if (log_enabled) {
			Log.e("BlogActivity - getNewsFromDB",
					"DEBUG dbNews ---------------------------");
			for (News n : dbNews) {
				Log.e("BlogActivity", n.getId() + " - " + n.getPostId() + " - "
						+ n.getTitle().substring(0, 20));
			}
		}

		return dbNews;
	}

	public List<String[]> getNewsAccordingToSyncState(Context context,
			String network_state, String db_mode_on, DatabaseHelper dbh,
			int news_range, List<News> listWWW, List<News> listDB) {

		// http://www.tutorialspoint.com/android/android_network_connection.htm
		// http://codereview.stackexchange.com/questions/45819/httpurlconnection-response-code-handling

		List<String[]> newsList = new ArrayList<String[]>();

		News LatestNews;
		News LatestDBNews;

		int dsMaxId = 0;
		int dbMaxId = 0;

		// IF ONLINE (NETWORK ALIVE)
		if (network_state.equals("online")) {
			// Log Message
			if (log_enabled) {
				Log.e("DataCollector", "Network Status: Online");
			}

			if (listWWW.size() > 0) {
				LatestNews = listWWW.get(0);
				dsMaxId = LatestNews.getPostId();
			}

			// IF DB ACTIVE
			if (db_mode_on.equals("true")) {

				// Log Message
				if (log_enabled) {
					Log.e("DataCollector", "Database Status: existing");
				}

				if (listDB.size() > 0) {
					LatestDBNews = listDB.get(0);
					dbMaxId = LatestDBNews.getPostId();
				}

				if ((dsMaxId == dbMaxId) && (listWWW.size() == listDB.size())) {
					// Log Message
					if (log_enabled) {
						Log.e("DataCollector", "NEWS Complete");
					}

				} else {

					// if news history timestamp > x minutes update

					// Log Message
					if (log_enabled) {
						Log.e("DataCollector", "NEWS Update " + "\n >> dbMax:"
								+ dbMaxId + "\n >> dsMax:" + dsMaxId);
					}

					dbh.deleteNews();

					for (News n : listWWW) {
						try {
							dbh.createNews(n);
						} catch (Exception e) {
							// Log Message
							if (log_enabled) {
								Log.e("SQL Error on Update: ", e.toString());
							}
						}
					}

				}

				List<News> listDB_new = dbh.getAllNews();

				String[] captionList = new String[news_range];
				String[] contentList = new String[news_range];

				for (int n = 0; n < news_range; n++) {
					captionList[n] = listDB_new.get(n).title;
					contentList[n] = listDB_new.get(n).description;
				}

				newsList.add(captionList);
				newsList.add(contentList);

			} else {

				// DATABASE INACTIVE
				// Log Message
				if (log_enabled) {
					Log.e("DataCollector", "Database Status: no Database");
				}

				String[] captionList = new String[news_range];
				String[] contentList = new String[news_range];

				for (int n = 0; n < news_range; n++) {
					captionList[n] = listWWW.get(n).title;
					contentList[n] = listWWW.get(n).description;
				}

				newsList.add(captionList);
				newsList.add(contentList);

			}

		} else {

			// NETWORK OFFLINE

			// Log Message
			if (log_enabled) {
				Log.e("DataCollector", "Network Status: Offline");
			}

			// IF DB ACTIVE
			if (db_mode_on.equals("true")) {
				// Log Message
				if (log_enabled) {
					Log.e("DataCollector", "Database Status: existing");
				}

				if (listDB.size() > 0) {

					List<News> listDB_new = dbh.getAllNews();

					// Wenn Daten aus DB kleiner als Mindestanzahl
					// funktioniert der Ablgeich im Offline Modus nicht

					String[] captionList = new String[news_range];
					String[] contentList = new String[news_range];

					for (int n = 0; n < news_range; n++) {
						captionList[n] = listDB_new.get(n).title;
						contentList[n] = listDB_new.get(n).description;
					}

					newsList.add(captionList);
					newsList.add(contentList);
				}

			} else {

				// context.startActivity(new Intent(context,
				// OfflineActivity.class));
				newsList = null;
			}

		}

		return newsList;

	}

	public List<Load> getLoad(DatabaseHelper dbh) {

		String BB_SN_INF = "https://www.bib.uni-mannheim.de/bereichsauslastung/index.php?bereich=infocenter";
		String BB_SN_LC = "https://www.bib.uni-mannheim.de/bereichsauslastung/index.php?bereich=learncenter";
		String BB_BWL = "https://www.bib.uni-mannheim.de/bereichsauslastung/index.php?bereich=bwl";
		String BB_EH = "https://www.bib.uni-mannheim.de/bereichsauslastung/index.php?bereich=ehrenhof";
		String BB_A3 = "https://www.bib.uni-mannheim.de/bereichsauslastung/index.php?bereich=a3";
		String BB_A5 = "https://www.bib.uni-mannheim.de/bereichsauslastung/index.php?bereich=a5";

		String percent = "";

		URLParser urlp = new URLParser();

		String[] urls = { BB_SN_INF, BB_SN_LC, BB_BWL, BB_EH, BB_A3, BB_A5 };
		String[] sector = { "Info Center", "Learning Center",
				"Schneckenhof (BWL)", "Ehrenhof (Hasso-Plattner-Bibliothek)",
				"Bereich A3", "Bereich A5" };

		List<Load> dsLoad = new ArrayList<Load>();

		int BB_id = 1;
		for (String s : urls) {

			Load load = new Load();

			urlp.url = s;

			Thread t = new Thread(urlp);
			t.start();
			while (!(t.getState().toString().equals("TERMINATED"))) {
				// wait for completion
			}

			percent = urlp.load;

			load.setId(BB_id);
			load.setLoad(Integer.valueOf(percent.replace("%", "").trim()));
			load.setSector(sector[BB_id - 1]);

			dsLoad.add(load);

			BB_id++;

		}

		// return dsLoad;

		/*
		 * DEBUG
		 */

		// Read DS

		// Log Message
		if (log_enabled) {
			Log.e("LoadActivity", "DEBUG dsLoad ---------------------------");
			for (Load l : dsLoad) {
				Log.e("LoadActivity", l.getId() + " - " + l.getLoad() + " - "
						+ l.getSector());
			}
		}

		// Read DB

		List<Load> dbLoad = dbh.getAllLoads();

		// Log Message
		if (log_enabled) {
			Log.e("LoadActivity", "DEBUG dbLoad ---------------------------");
			for (Load l : dbLoad) {
				Log.e("LoadActivity", l.getId() + " - " + l.getLoad() + " - "
						+ l.getSector());
			}
		}

		// Update DB

		// if (last_update > 60s)
		// <example>

		for (int i = 0; i < dbLoad.size(); i++) {
			Load temp = new Load();
			temp = dbLoad.get(i);
			temp.setLoad(dsLoad.get(i).load);
			dbh.updateLoad(temp);
		}

		dbLoad = dbh.getAllLoads();

		// Log Message
		if (log_enabled) {
			Log.e("LoadActivity", "DEBUG dbLoadNeu ---------------------------");
			for (Load l : dbLoad) {
				Log.e("LoadActivity", l.getId() + " - " + l.getLoad() + " - "
						+ l.getSector());
			}
		}

		return dbLoad;
	}

	public List<Load> getLoadFromWWW() {

		String percent = "";

		URLParser urlp = new URLParser();

		// Libraries
		String BB_SN_INF = "https://www.bib.uni-mannheim.de/bereichsauslastung/index.php?bereich=infocenter";
		String BB_SN_LC = "https://www.bib.uni-mannheim.de/bereichsauslastung/index.php?bereich=learncenter";
		String BB_BWL = "https://www.bib.uni-mannheim.de/bereichsauslastung/index.php?bereich=bwl";
		String BB_EH = "https://www.bib.uni-mannheim.de/bereichsauslastung/index.php?bereich=ehrenhof";
		String BB_A3 = "https://www.bib.uni-mannheim.de/bereichsauslastung/index.php?bereich=a3";
		String BB_A5 = "https://www.bib.uni-mannheim.de/bereichsauslastung/index.php?bereich=a5";

		String[] BB_urls = { BB_SN_INF, BB_SN_LC, BB_BWL, BB_EH, BB_A3, BB_A5 };
		String[] BB_names = { "Info Center", "Learning Center",
				"Schneckenhof (BWL)", "Ehrenhof (Hasso-Plattner-Bibliothek)",
				"Bereich A3", "Bereich A5" };

		List<Load> dsLoad = new ArrayList<Load>();

		int BB_id = 1;
		for (String s : BB_urls) {

			Load load = new Load();

			urlp.url = s;

			Thread t = new Thread(urlp);
			t.start();
			while (!(t.getState().toString().equals("TERMINATED"))) {
				// wait for completion
			}

			percent = urlp.load;

			load.setId(BB_id);
			load.setLoad(Integer.valueOf(percent.replace("%", "").trim()));
			load.setSector(BB_names[BB_id - 1]);

			dsLoad.add(load);

			BB_id++;

		}

		// Log Message
		if (log_enabled) {
			Log.e("DataCollector",
					"DEBUG getLoadFromWWW ---------------------------");
			for (Load l : dsLoad) {
				Log.e("DataCollector", l.getId() + " - " + l.getLoad() + " - "
						+ l.getSector());
			}
		}

		return dsLoad;

	}

	public List<Load> getLoadFromDB(DatabaseHelper dbh) {

		List<Load> dbLoad = dbh.getAllLoads();

		// Log Message
		if (log_enabled) {
			Log.e("DataCollector",
					"DEBUG getLoadFromDB ---------------------------");
			for (Load l : dbLoad) {
				Log.e("DataCollector", l.getId() + " - " + l.getLoad() + " - "
						+ l.getSector());
			}
		}

		return dbLoad;

	}

	public List<Load> getLoadAccordingToSyncState(Context context,
			String network_state, String db_mode_on, DatabaseHelper dbh,
			List<Load> listWWW, List<Load> listDB) {

		List<Load> loadList = new ArrayList<Load>();

		// if Online
		if (network_state.equals("online")) {
			
			if (log_enabled) {
				Log.e("DataCollector", "Network Status: Online");
			}
			
			if (db_mode_on.equals("true")) {
				// if db active

				// Log Message
				if (log_enabled) {
					Log.e("DataCollector", "Database Status: existing");
				}

				// update db and return loadDB
				for (int i = 0; i < listDB.size(); i++) {
					Load temp = new Load();
					temp = listDB.get(i);
					temp.setLoad(listWWW.get(i).load);
					dbh.updateLoad(temp);
				}

				loadList = dbh.getAllLoads();
				/*
				 * List<Load> upd = dbh.getAllLoads(); for(Load u: upd) {
				 * loadList.add(u); }
				 */

			} else {
				// if db inactive

				// Log Message
				if (log_enabled) {
					Log.e("DataCollector", "Database Status: not available");
				}

				loadList = listWWW;
				/*
				 * // return loadWWW for(Load l: listWWW) { loadList.add(l); }
				 * 
				 * Log.e("DataCollector", "TMP ---------------------------");
				 * for(Load l: listWWW) { Log.e("DataCollector", l.getId() +
				 * " - " + l.getLoad() + " - " + l.getSector()); }
				 */
			}

		} else {
			// if Offline

			// Log Message
			if (log_enabled) {
				Log.e("DataCollector", "Network Status: Offline");
			}

			if (db_mode_on.equals("true")) {
				// if db active

				// Log Message
				if (log_enabled) {
					Log.e("DataCollector", "Database Status: existing");
				}
				// retunr loadDB
				loadList = listDB;

			} else {
				// if db inactive

				// Log Message
				if (log_enabled) {
					Log.e("DataCollector", "Database Status: not available");
				}

				loadList = null;
				// open offline activity
				// context.startActivity(new Intent(context,
				// OfflineActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			}
		}

		return loadList;
	}

	public List<News> createDatasetNews(String[][] news) {

		List<News> dataset = new ArrayList<News>();

		int id = 1;

		for (String[] n : news) {

			/*
			 * news_item[0] = n[0]; // title news_item[1] = n[1]; // link
			 * news_item[2] = n[2]; // date news_item[3] = n[3]; // content
			 * news_item[4] = n[4]; // post_id
			 */

			News news_item = new News();

			news_item.setId(id);
			news_item.setTitle(n[0]);
			news_item.setUrl(n[1]);
			// news_item.?setDate(n[2]); // Datum nicht in DB implementiert
			news_item.setDescription(n[3]);
			news_item.setPostId(Integer.valueOf(n[4]));

			dataset.add(news_item);
			id++;

		}

		return dataset;
	}

	public <T> List<T> getData(String[][] list) {

		// temporarily inner getNews() / getLoad()

		return null;
	}

	public <T> List<T> compareData(List<T> list_online, List<T> list_local) {

		// temporarily inner getNews() / getLoad()

		return null;
	}
}
