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
 * Defines and creates a News object.
 * 
 * 
 */

package de.uni_mannheim.bib.app;

public class News {

	int id;
	String title;
	String description;
	String url;
	int post_id;

	// Constructors
	public News() {
	}

	public News(int id, String title, int post_id) {
		this.id = id;
		this.title = title;
		this.post_id = post_id;
	}

	public News(int id, String title, String description, String url,
			int post_id) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.url = url;
		this.post_id = post_id;
	}

	// Setters
	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setPostId(int post_id) {
		this.post_id = post_id;
	}

	// Getters
	public long getId() {
		return this.id;
	}

	public String getTitle() {
		return this.title;
	}

	public String getDescription() {
		return this.description;
	}

	public String getUrl() {
		return this.url;
	}

	public int getPostId() {
		return this.post_id;
	}

}