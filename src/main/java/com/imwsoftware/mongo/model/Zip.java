package com.imwsoftware.mongo.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;

/**
 * Class: Zip.java
 *
 * @author: Springzen
 * @since: Jul 6, 2017
 * @version: 1.0
 *
 *           Copyright (c) 2017 Springzen
 *
 */
@EqualsAndHashCode
@Document(collection = "zipcodes")
public class Zip {

	@Id
	private String id;
	@TextIndexed
	private String state;
	@TextIndexed
	private String city;
	@Indexed
	private double pop;
	private ArrayList<Integer> loc;

	public Zip() {

	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getPop() {
		return this.pop;
	}

	public void setPop(double pop) {
		this.pop = pop;
	}

	public ArrayList<Integer> getLoc() {
		return this.loc;
	}

	public void setLoc(ArrayList<Integer> loc) {
		this.loc = loc;
	}

	@Override
	public String toString() {
		return "Zip [id=" + id + ", state=" + state + ", city=" + city + ", pop=" + pop + "]";
	}

}
