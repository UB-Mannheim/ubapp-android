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
 * Defines and creates a Module object.
 * 
 * 
 */

package de.uni_mannheim.bib.app;

public class Module {

	int id;
	String name;
	int save_mode;

	// constructors
	public Module() {
	}

	public Module(int id, int save_mode) {
		this.id = id;
		this.save_mode = save_mode;
	}

	public Module(int id, String name, int save_mode) {
		this.id = id;
		this.name = name;
		this.save_mode = save_mode;
	}

	// setters
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSaveMode(int save_mode) {
		this.save_mode = save_mode;
	}

	// getters
	public long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public int getSaveMode() {
		return this.save_mode;
	}

}