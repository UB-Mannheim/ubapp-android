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
 * Defines and creates a Load object.
 * 
 * 
 */

package de.uni_mannheim.bib.app;

public class Load {

	int id;
	String sector;
	int load;

	// Constructors
	public Load() {
	}

	public Load(int id, int load) {
		this.id = id;
		this.load = load;
	}

	public Load(int id, String sector, int load) {
		this.id = id;
		this.sector = sector;
		this.load = load;
	}

	// Setters
	public void setId(int id) {
		this.id = id;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public void setLoad(int load) {
		this.load = load;
	}

	// Getters
	public long getId() {
		return this.id;
	}

	public String getSector() {
		return this.sector;
	}

	public int getLoad() {
		return this.load;
	}

}