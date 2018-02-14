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
 * Parses the latest n blog entries of the UB Mannheim blog and 
 * returns an array. 
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class BlogParser {

	private boolean log_enabled = false;

	protected String url = "http://blog.bib.uni-mannheim.de/Aktuelles/";
	// Alternate URL with lookup in Category 4
	// protected String url = "http://blog.bib.uni-mannheim.de/Aktuelles/?cat=4";
	protected String[][] news;
	
	public BlogParser() {

		// Log Message
		if (log_enabled) {
			Log.e("BlogParser", "constructed");
		}

	}

	public String[][] getBlogEntries(String url, int range) {

		if (log_enabled) {
			Log.e("BlogParser", "getBlogEntries");
		}

		Document doc;
		String news[][] = new String[range][5];

		String[] atitles = new String[range];
		String[] alinks = new String[range];
		String[] adates = new String[range];
		String[] acontents = new String[range];
		// String[] apostids = new String[range];

		// If Internet Connection available, try
		try {

			doc = Jsoup.connect(url).get();
			
			int i = 0;
			Elements titles = doc.select("h2.entry-title a[href]");
			for (Element title : titles) {
				if (i < range) {
					atitles[i] = title.text();
					alinks[i] = title.attr("href");
					
					// Log Message
					if (log_enabled) {
						Log.e("text: ", title.text());
						Log.e("text: ", title.attr("href"));
					}
				}
				i++;
			}

			i = 0;
			Elements dates = doc.select("span.entry-date");
			for (Element date : dates) {
				if (i < range) {
					adates[i] = date.text();
					
					// Log Message
					if (log_enabled) {
						Log.e("text: ", date.text());
					}
				}
				i++;
			}

			i = 0;
			Elements contents = doc.select("div.entry-content");	// 1st
			// Elements contents = doc.select("div.entry-summary");
			for (Element content : contents) {
				if (i < range) {
					
						// If TextLength > 200 cut
						if(content.text().length()>=200) {
							acontents[i] = content.text().substring(0, 200) + "... \n"
									+ alinks[i];
						} else {
						// Show all
							acontents[i] = content.text() + "... \n"
									+ alinks[i];
						}
					// Log Message
					if (log_enabled) {
						Log.e("text:", content.text().substring(0, 200));
					}
				}
				i++;
			}

			// If apostids[] defined
			/*
			 * i=0; Elements ids = doc.select("div.content"); for (Element id :
			 * ids) { if(i<5) { apostids[i]=id.text(); } i++; }
			 */

			for (int m = 0; m < range; m++) {
				
				news[m][0] = atitles[m];
				news[m][1] = alinks[m];
				news[m][2] = adates[m];
				news[m][3] = acontents[m];
				news[m][4] = news[m][1].substring(news[m][1].length() - 5,
						news[m][1].length());
								
			}

			// Log Message
			if (log_enabled) {
				for (int m = 0; m < 5; m++) {
					Log.e("elements: ", news[m][0] + " - " + news[m][1] + " - "
							+ news[m][2] + " - " + news[m][3]);
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR in getBlogEntries");
			e.printStackTrace();
		}

		return news;
	}

}