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
 * Registers all started activities in a list
 * - can enumerate over activity list and return items
 * - can finish all activities
 * 
 * Used to close application properly onBack from MainActivity
 * 
 */

package de.uni_mannheim.bib.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityRegistry {

	private static List<Activity> _activities;

	  public static void register(Activity activity) {
	    if(_activities == null) {
	      _activities = new ArrayList<Activity>();
	    }
	    _activities.add(activity);
	  }

	  public static void getAll() {
		    for (Activity activity : _activities) {
		      // Log.e("", activity.toString());
		    }
		  }
	  
	  public static void finishAll() {
	    for (Activity activity : _activities) {
	      activity.finish();
	    }
	  }
}