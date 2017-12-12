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
 * Turns menu content into a structured (xml formatted) list.
 * Is used by MainActivity.
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainAdapter extends BaseAdapter {

	private boolean log_enabled = false;

	String[] result;
	Context context;
	int load;

	int[] imageId;
	private LayoutInflater inflater = null;

	@SuppressWarnings("rawtypes")
	private void openWebViewWithUrl(Context self, Class next, String url,
			String action) {

		Intent i = new Intent(self, next);
		i.putExtra("url", url);
		i.putExtra("action", action);

		self.startActivity(i);
	}

	public MainAdapter(MainActivity mainActivity, String[] prgmNameList,
			int[] prgmImages) {

		result = prgmNameList;
		context = mainActivity;
		imageId = prgmImages;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return result.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class Holder {
		TextView tv;
		ImageView img;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {

		Holder holder = new Holder();
		View rowView;

		rowView = inflater.inflate(R.layout.program_list, null);
		holder.tv = (TextView) rowView.findViewById(R.id.textView1);
		holder.img = (ImageView) rowView.findViewById(R.id.imageView1);

		holder.tv.setText(result[position]);
		holder.img.setImageResource(imageId[position]);

		rowView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String lang = context.getResources().getConfiguration().locale.getDefault().getLanguage();

				// Toast.makeText(context, "You Clicked "+result[position],
				// Toast.LENGTH_LONG).show();

				NetworkChecker nc = new NetworkChecker();

				// setConfig Network=true
				SharedPreferences settings = context.getSharedPreferences(
						"preferences", 0);
				SharedPreferences.Editor preferencesEditor = settings.edit();

				if (nc.isConnected(context)) {

					preferencesEditor.putString("NetworkConnectionAvailable",
							"true");
					preferencesEditor.commit();
				} else {
					// setConfig Network=false
					preferencesEditor.putString("NetworkConnectionAvailable",
							"false");
					preferencesEditor.commit();
				}

				String netstate = settings.getString(
						"NetworkConnectionAvailable", "false");

				// Log Message ////////////////////////////////////////
				if (log_enabled) {
					Log.e(this.getClass().toString(), "Netstate " + netstate);
				}

				// onClick >> WEBSITE
				if (result[position].equals("Website")) {

					if (netstate.equals("true")) {
						openWebViewWithUrl(
								context,
								WebviewActivity.class,
								"http://www.bib.uni-mannheim.de/mobile/" + lang + "/1.html",
								// "http://anyplace.cs.ucy.ac.cy/viewer/",
								"www");
					} else {

						openWebViewWithUrl(context, OfflineActivity.class, "",
								"");
					}

				}


				String primo_lang = "";

				if (lang.equals("en")) {
					primo_lang = "&prefLang=en_US";
				} else {
					primo_lang = "&prefLang=de_DE";
				}

				// onClick >> KATALOG
				// if (result[position].equals("Katalog")) {
				if (result[position].equals("Primo")) {
					if (netstate.equals("true")) {
						openWebViewWithUrl(
								context,
								WebviewActivity.class,
	//"http://primo.bib.uni-mannheim.de/primo_library/libweb/action/search.do?vid=MAN_MOBILE"+primo_lang,
	"http://primo-49man.hosted.exlibrisgroup.com/primo-explore/search?sortby=rank&vid=MAN_UB&lang="+primo_lang,
//"http://primo-test.bib.uni-mannheim.de/primo_library/libweb/action/search.do?vid=MAN_MOBILE",
								// "http://primo.bib.uni-mannheim.de/primo_library/libweb/action/search.do?vid=MAN_KB",
								"catalogue");
					} else {

						openWebViewWithUrl(context, OfflineActivity.class, "",
								"");
					}

				}

				// onClick >> AKTUELLES
				// if (result[position].equals("Aktuelles")) {
				if (result[position].equals("News")) {
					openWebViewWithUrl(context, BlogActivity.class, "", "");
				}

				// onClick >> FREIE PLäTZE
				if (result[position].equals("Freie Plätze") || result[position].equals("Free Seats")) {
					openWebViewWithUrl(context, LoadActivity.class, "", "");
				}

				// } else {

				// setConfig Network=false
				// preferencesEditor.putString("NetworkConnectionAvailable",
				// "false");
				// preferencesEditor.commit();

				// openWebViewWithUrl(context, OfflineActivity.class, "", "");
				// }

			}

		});

		return rowView;
	}

}