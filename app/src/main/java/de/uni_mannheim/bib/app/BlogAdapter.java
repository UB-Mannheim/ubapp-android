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
 * Turns list content into a structured (xml formatted) list.
 * Is used by BlogActivity.
 * 
 * 
 */

package de.uni_mannheim.bib.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BlogAdapter extends BaseAdapter {

	private boolean log_enabled = false;

	String[] captions;
	String[] contents;

	Context context;

	private static LayoutInflater inflater = null;

	public BlogAdapter(BlogActivity blogActivity, String[] captionList,
			String[] contentList) {

		// Log Message
		if (log_enabled) {
			Log.e("BlogAdapter", "constructed");
		}

		context = blogActivity;
		captions = captionList;
		contents = contentList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return captions.length;
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
		TextView tv_caption;
		TextView tv_content;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Holder holder = new Holder();
		View rowView;

		rowView = inflater.inflate(R.layout.news_list, null);
		holder.tv_caption = (TextView) rowView.findViewById(R.id.textView1);
		holder.tv_content = (TextView) rowView.findViewById(R.id.textView2);

		holder.tv_caption.setText(captions[position]);
		holder.tv_content.setText(contents[position]);

		return rowView;
	}

}