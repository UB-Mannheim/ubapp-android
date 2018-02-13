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
 * Dummy activity for several use cases.
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

}
