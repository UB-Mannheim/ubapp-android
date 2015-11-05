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
 * Small (unused?) helper class.
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import java.util.List;

import android.content.SharedPreferences;

public class Helper {

	public String getSyncState(SharedPreferences settings) {

		String network_available = settings.getString(
				"NetworkConnectionAvailable", null);
		String database_mode_on = settings.getString("Config_DatabaseModeOn",
				null);
		String state = "";

		if (network_available.equals("true") && database_mode_on.equals("true")) {
			state = "online";
		}
		if (network_available.equals("true")
				&& database_mode_on.equals("false")) {
			state = "db_offline";
		}
		if (network_available.equals("false")
				&& database_mode_on.equals("true")) {
			state = "network_offline";
		}
		if (network_available.equals("false")
				&& database_mode_on.equals("false")) {
			state = "offline";
		}

		return state;

	}

	public String getNetworkConnectionState(String cfg_state) {

		String state = "offline";

		if (cfg_state.equals("true")) {
			state = "online";
		}

		return state;

	}

	// Get First or Last List<> Element
	public <T> T getFirst(List<T> list) {
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}

	public <T> T getLast(List<T> list) {
		return list != null && !list.isEmpty() ? list.get(list.size() - 1)
				: null;
	}
}
