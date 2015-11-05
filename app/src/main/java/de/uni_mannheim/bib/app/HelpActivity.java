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
 * Shows help topics categorized by module.
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import android.os.Bundle;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

public class HelpActivity extends ActionBarActivity {
	
	private boolean log_enabled = false;

	protected String getVersionCode() throws NameNotFoundException {
		
		String vcode = "";
		
		String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		int versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

		vcode = "  UB Mannheim, Version: " + versionName + " (" + String.valueOf(versionCode) + ")";
		
		return vcode;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		ActivityRegistry.register(this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		// customized actionbar (Color, Title)
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#990000")));
		actionBar.setTitle("UB Mannheim");

		// http://tablet-market.de/android-listview-tutorial-mit-listadapter-einsteiger/

		// final Button button1 = (Button) findViewById(R.id.button1);
		final TableRow tRow0 = (TableRow) findViewById(R.id.tableRow0);
		final TableRow tRow1 = (TableRow) findViewById(R.id.tableRow1);

		final TableRow tRow2 = (TableRow) findViewById(R.id.tableRow2);
		final TableRow tRow3 = (TableRow) findViewById(R.id.tableRow3);

		final TableRow tRow4 = (TableRow) findViewById(R.id.tableRow4);
		final TableRow tRow5 = (TableRow) findViewById(R.id.tableRow5);

		final TableRow tRow6 = (TableRow) findViewById(R.id.tableRow6);
		final TableRow tRow7 = (TableRow) findViewById(R.id.tableRow7);

		final TableRow tRow8 = (TableRow) findViewById(R.id.tableRow8);
		final TableRow tRow9 = (TableRow) findViewById(R.id.tableRow9);
		final TableRow tRow10 = (TableRow) findViewById(R.id.tableRow10);
		final TableRow tRow11 = (TableRow) findViewById(R.id.tableRow11);
		final TableRow tRow12 = (TableRow) findViewById(R.id.tableRow12);

		final TextView textView2 = (TextView) findViewById(R.id.textView2);
		final TextView textView4 = (TextView) findViewById(R.id.textView4);
		final TextView textView6 = (TextView) findViewById(R.id.textView6);
		final TextView textView8 = (TextView) findViewById(R.id.textView8);
		final TextView textView10 = (TextView) findViewById(R.id.textView10);

		// final TextView textView11 = (TextView) findViewById(R.id.textView11);
		// final TextView textView12 = (TextView) findViewById(R.id.textView12);
		// final TextView textView13 = (TextView) findViewById(R.id.textView13);
		
		final TextView textView15 = (TextView) findViewById(R.id.textView15);
		try {
			textView15.setText(getVersionCode());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			textView15.setVisibility(View.GONE);
		}
		
		tRow0.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tRow1.getVisibility() == View.GONE) {
					tRow1.setVisibility(View.VISIBLE);
					textView2.setText(((String) textView2.getText()).replace(
							'+', '-'));

					tRow3.setVisibility(View.GONE);
					tRow5.setVisibility(View.GONE);
					tRow7.setVisibility(View.GONE);
					tRow9.setVisibility(View.GONE);
					tRow10.setVisibility(View.GONE);
					tRow11.setVisibility(View.GONE);
					tRow12.setVisibility(View.GONE);

					textView4.setText(((String) textView4.getText()).replace(
							'-', '+'));
					textView6.setText(((String) textView6.getText()).replace(
							'-', '+'));
					textView8.setText(((String) textView8.getText()).replace(
							'-', '+'));
					textView10.setText(((String) textView10.getText()).replace(
							'-', '+'));
				} else {
					tRow1.setVisibility(View.GONE);
					textView2.setText(((String) textView2.getText()).replace(
							'-', '+'));
				}
			}
		});

		tRow2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tRow3.getVisibility() == View.GONE) {
					tRow3.setVisibility(View.VISIBLE);
					textView4.setText(((String) textView4.getText()).replace(
							'+', '-'));

					tRow1.setVisibility(View.GONE);
					tRow5.setVisibility(View.GONE);
					tRow7.setVisibility(View.GONE);
					tRow9.setVisibility(View.GONE);
					tRow10.setVisibility(View.GONE);
					tRow11.setVisibility(View.GONE);
					tRow12.setVisibility(View.GONE);

					textView2.setText(((String) textView2.getText()).replace(
							'-', '+'));
					textView6.setText(((String) textView6.getText()).replace(
							'-', '+'));
					textView8.setText(((String) textView8.getText()).replace(
							'-', '+'));
					textView10.setText(((String) textView10.getText()).replace(
							'-', '+'));
				} else {
					tRow3.setVisibility(View.GONE);
					textView4.setText(((String) textView4.getText()).replace(
							'-', '+'));
				}
			}
		});

		tRow4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tRow5.getVisibility() == View.GONE) {
					tRow5.setVisibility(View.VISIBLE);
					textView6.setText(((String) textView6.getText()).replace(
							'+', '-'));

					tRow1.setVisibility(View.GONE);
					tRow3.setVisibility(View.GONE);
					tRow7.setVisibility(View.GONE);
					tRow9.setVisibility(View.GONE);
					tRow10.setVisibility(View.GONE);
					tRow11.setVisibility(View.GONE);
					tRow12.setVisibility(View.GONE);

					textView2.setText(((String) textView2.getText()).replace(
							'-', '+'));
					textView4.setText(((String) textView4.getText()).replace(
							'-', '+'));
					textView8.setText(((String) textView8.getText()).replace(
							'-', '+'));
					textView10.setText(((String) textView10.getText()).replace(
							'-', '+'));
				} else {
					tRow5.setVisibility(View.GONE);
					textView6.setText(((String) textView6.getText()).replace(
							'-', '+'));
				}
			}
		});

		tRow6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tRow7.getVisibility() == View.GONE) {
					tRow7.setVisibility(View.VISIBLE);
					textView8.setText(((String) textView8.getText()).replace(
							'+', '-'));

					tRow1.setVisibility(View.GONE);
					tRow3.setVisibility(View.GONE);
					tRow5.setVisibility(View.GONE);
					tRow9.setVisibility(View.GONE);
					tRow10.setVisibility(View.GONE);
					tRow11.setVisibility(View.GONE);
					tRow12.setVisibility(View.GONE);
					
					textView2.setText(((String) textView2.getText()).replace(
							'-', '+'));
					textView4.setText(((String) textView4.getText()).replace(
							'-', '+'));
					textView6.setText(((String) textView6.getText()).replace(
							'-', '+'));
					textView10.setText(((String) textView10.getText()).replace(
							'-', '+'));
				} else {
					tRow7.setVisibility(View.GONE);
					textView8.setText(((String) textView8.getText()).replace(
							'-', '+'));
				}
			}
		});

		tRow8.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tRow9.getVisibility() == View.GONE) {
					tRow9.setVisibility(View.VISIBLE);
					tRow10.setVisibility(View.VISIBLE);
					tRow11.setVisibility(View.VISIBLE);
					tRow12.setVisibility(View.VISIBLE);
					textView10.setText(((String) textView10.getText()).replace(
							'+', '-'));

					tRow1.setVisibility(View.GONE);
					tRow3.setVisibility(View.GONE);
					tRow5.setVisibility(View.GONE);
					tRow7.setVisibility(View.GONE);

					textView2.setText(((String) textView2.getText()).replace(
							'-', '+'));
					textView4.setText(((String) textView4.getText()).replace(
							'-', '+'));
					textView6.setText(((String) textView6.getText()).replace(
							'-', '+'));
					textView8.setText(((String) textView8.getText()).replace(
							'-', '+'));
				} else {
					tRow9.setVisibility(View.GONE);
					tRow10.setVisibility(View.GONE);
					tRow11.setVisibility(View.GONE);
					tRow12.setVisibility(View.GONE);
					textView10.setText(((String) textView10.getText()).replace(
							'-', '+'));
				}
			}
		});

	}

}
