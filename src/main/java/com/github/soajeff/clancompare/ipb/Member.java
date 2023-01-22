package com.github.soajeff.clancompare.ipb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Member {
	private int id;
	private String name;
	private Group primaryGroup;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Group getPrimaryGroup() {
		return primaryGroup;
	}
}