package com.github.soajeff.clancompare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ClanRank {
	REGISTERED_USER("Registered User", 3, "Recruit"),
	APPLICANT("Applicant", 36, "Recruit"),
	TYLAR("Tylar", 10, "Corporal"),
	BAEL("Bael", 15, "Sergeant"),
	MYRTH("Myrth",9, "Lieutenant"),
	ATHEM("Athem", 8, "Captain"),
	SADRON("Sadron", 24, "General"),
	ELENDUR("Elendur", 37, "Admin"),
	VORONWE("Voronwë", 44, "Organiser"),
	ADELE("Adele", 31, "Coordinator"),
	ARQUENDI("Arquendi", 6, "Coordinator"),
	ADMINISTRATOR("Administrator",34, "Coordinator" ),
	LIAN("Lian", 7, "Overseer"),
	ELDAR("Eldar", 4, "Deputy_Owner"),
	ONTARI("Ontari", 22, "Deputy_Owner"),
	SERE("Serë", 48, ""),
	RANKED_ALT("", -1, "Sergeant"),
	//ADD POTENTIAL BAEL SWEEP ID HERE
	//BAEL_SWEPT("Bael Swept", 49, ""),
	UNKNOWN("Unknown Group", -1, "Unknown");

	private final String forumGroupName;
	private final int forumId;
	private final String rsClanRank;
	private static final List<ClanRank> rankList;

	static {
		rankList = new ArrayList<>();
		rankList.addAll(Arrays.stream(ClanRank.values()).collect(Collectors.toList()));
	}

	ClanRank(String forumGroupName, int forumId, String rsClanRank)
	{
		this.forumGroupName = forumGroupName;
		this.forumId = forumId;
		this.rsClanRank = rsClanRank;
	}

	public String getForumGroupName() {
		return forumGroupName;
	}

	public int getForumId() {
		return forumId;
	}

	public String getRsClanRank() {
		return rsClanRank;
	}

	public ClanRank getClanRankByForumGroup(String groupName) {
		for (ClanRank r : rankList) {
			if (r.getForumGroupName().equalsIgnoreCase(groupName)) {
				return r;
			}
		}
		return ClanRank.UNKNOWN;
	}

	public ClanRank getClanRankByRsClanRank(String clanRank)
	{
		for(ClanRank r : rankList)
		{
			if(r.rsClanRank.equalsIgnoreCase(clanRank))
				return r;
		}
		return ClanRank.UNKNOWN;
	}
}
