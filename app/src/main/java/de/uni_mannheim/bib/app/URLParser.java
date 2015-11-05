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
 * Parses the latest load statistics of the UB Mannheim Website and 
 * returns a string. 
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class URLParser implements Runnable {

	protected String url;
	protected String load;

	@Override
	public void run() {

		// android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

		try {
			this.load = getWLANLoad(this.url);
			// Log.i("Aktuelle Last", this.load);
		} catch (Exception e) {
			e.toString();
		}

	}

	protected String getWLANLoad(String url) {

		Document doc;
		// String title;
		String load = "";

		try {
			doc = Jsoup.connect(url).get();
			// title = doc.title();
			Elements td = doc.select("table tr td");
			load = td.text();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return load;
	}
}
