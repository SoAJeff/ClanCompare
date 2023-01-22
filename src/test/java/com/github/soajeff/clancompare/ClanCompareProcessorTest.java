package com.github.soajeff.clancompare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClanCompareProcessorTest {

	public Map<String, ClanRank> ipbMap;
	public Map<String, ClanRank> rsMap;
	public Map<String, ClanRank> altMap;

	@BeforeEach
	public void setup()
	{
		ipbMap = new HashMap<>();
		ipbMap.put("Applejuiceaj", ClanRank.ELDAR);
		ipbMap.put("Arquendi", ClanRank.ARQUENDI);
		ipbMap.put("Tylar", ClanRank.TYLAR);
		ipbMap.put("Unk", ClanRank.UNKNOWN);

		rsMap = new HashMap<>();
		rsMap.put("Applejuiceaj", ClanRank.ELDAR);
		rsMap.put("Arquendi", ClanRank.ARQUENDI);
		rsMap.put("Tylar", ClanRank.TYLAR);

		altMap = new HashMap<>();
	}

	@Test
	public void testMatches()
	{
		ClanCompareProcessor processor = new ClanCompareProcessor(null, null, null, null, -1);
		List<String> results = processor.getResults(ipbMap, rsMap, altMap);
		Assertions.assertEquals(0, results.size());
	}

	@Test
	public void testSkipSere()
	{
		ipbMap.put("Sere", ClanRank.SERE);
		ClanCompareProcessor processor = new ClanCompareProcessor(null, null, null, null, -1);
		List<String> results = processor.getResults(ipbMap, rsMap, altMap);
		Assertions.assertEquals(0, results.size());
	}

	@Test
	public void testMemberOnForumsNotClan()
	{
		ipbMap.put("ExtraUser", ClanRank.TYLAR);
		ClanCompareProcessor processor = new ClanCompareProcessor(null, null, null, null, -1);
		List<String> results = processor.getResults(ipbMap, rsMap, altMap);
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals("ExtraUser is listed on the forums as a ranked user (Tylar), but is not ranked in clan chat.", results.get(0));
	}

	@Test
	public void testMemberRanksDontMatch()
	{
		ipbMap.put("Tylar", ClanRank.MYRTH);
		ClanCompareProcessor processor = new ClanCompareProcessor(null, null, null, null, -1);
		List<String> results = processor.getResults(ipbMap, rsMap, altMap);
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals("Tylar is ranked as Myrth on the forums but as Tylar in clan chat.", results.get(0));
	}

	@Test
	public void testMemberRankedAsRegisteredButRankedInClan()
	{
		ipbMap.put("Tylar", ClanRank.UNKNOWN);
		ClanCompareProcessor processor = new ClanCompareProcessor(null, null, null, null, -1);
		List<String> results = processor.getResults(ipbMap, rsMap, altMap);
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals("Tylar is ranked as Unknown Group on the forums but is ranked in the clan chat.", results.get(0));
	}

	@Test
	public void testMemberInClanNotForums()
	{
		rsMap.put("ExtraUser", ClanRank.TYLAR);
		ClanCompareProcessor processor = new ClanCompareProcessor(null, null, null, null, -1);
		List<String> results = processor.getResults(ipbMap, rsMap, altMap);
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals("ExtraUser is ranked as Tylar in the clan chat, but is not a forum user.", results.get(0));
	}

	@Test
	public void testMemberInClanNotForumsIsAlt()
	{
		rsMap.put("ExtraUser", ClanRank.BAEL);
		altMap.put("ExtraUser", ClanRank.RANKED_ALT);
		ClanCompareProcessor processor = new ClanCompareProcessor(null, null, null, null, -1);
		List<String> results = processor.getResults(ipbMap, rsMap, altMap);
		Assertions.assertEquals(0, results.size());
	}

	@Test
	public void testMemberInClanNotForumsAsAltButWrongRank()
	{
		rsMap.put("ExtraUser", ClanRank.TYLAR);
		altMap.put("ExtraUser", ClanRank.RANKED_ALT);
		ClanCompareProcessor processor = new ClanCompareProcessor(null, null, null, null, -1);
		List<String> results = processor.getResults(ipbMap, rsMap, altMap);
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals("ExtraUser is ranked as Corporal in the clan chat but should be ranked as an alt (Sergeant).", results.get(0));
	}

	@Test
	public void testMemberNotInClanIsAlt()
	{
		altMap.put("ExtraUser", ClanRank.RANKED_ALT);
		ClanCompareProcessor processor = new ClanCompareProcessor(null, null, null, null, -1);
		List<String> results = processor.getResults(ipbMap, rsMap, altMap);
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals("ExtraUser is listed as an alt but is not ranked in the clan chat.", results.get(0));
	}
}
