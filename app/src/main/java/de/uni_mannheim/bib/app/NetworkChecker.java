/*
 * Copyright (C) 2014 Universitätsbibliothek Mannheim
 *
 * Author:
 *    Universitätsbibliothek Mannheim <admin@bib.uni-mannheim.de>
 *    Last modified on 2016-03-15
 * 
 * 
 * This is free software licensed under the terms of the GNU GPL, 
 * version 3, or (at your option) any later version.
 * See <http://www.gnu.org/licenses/> for more details.
 *
 *
 * Checks if network connection is available
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChecker {

	public final boolean isConnected(Context context) {
		final ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		final NetworkInfo networkInfo = connectivityManager
				.getActiveNetworkInfo();

		return networkInfo != null && networkInfo.isConnected();
		// return false;
	}

	public final int getHTTPResponseStatusCode(String u) throws IOException {

		URL url = new URL(u);
		HttpURLConnection http = (HttpURLConnection) url.openConnection();

		return http.getResponseCode();

	}

}
