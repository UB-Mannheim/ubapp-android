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
 * Defines and creates a History object.
 * 
 * 
 */

package de.uni_mannheim.bib.app;

public class History {

	int id;
	int module_id;
	String last_update;

	// Constructors
	public History() {
	}

	public History(int id, int module_id) {
		this.id = id;
		this.module_id = module_id;
	}

	public History(int id, int module_id, String last_update) {
		this.id = id;
		this.module_id = module_id;
		this.last_update = last_update;
	}

	// Setters
	public void setId(int id) {
		this.id = id;
	}

	public void setModuleId(int module_id) {
		this.module_id = module_id;
	}

	public void setLastUpdate(String last_update) {
		this.last_update = last_update;
	}

	// Getters
	public long getId() {
		return this.id;
	}

	public int getModuleId() {
		return this.module_id;
	}

	public String getLastUpdate() {
		return this.last_update;
	}

}