package com.github.soajeff.clancompare.rs;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.soajeff.clancompare.ClanRank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RsClanMemberFetcherTest {

	private String sampleData;

	@BeforeEach
	public void setup()
	{
		sampleData = "Clanmate, Clan Rank, Total XP, Kills\n" + "Princess Rae,Owner,1925969448,10\n"
				+ "Applejuiceaj,Deputy Owner,1304210203,3\n" + "Firesteal918,Deputy Owner,1320278394,0\n"
				+ "Boss Shanks,Deputy Owner,1656394258,0\n" + "Plak,Deputy Owner,3540118195,3\n"
				+ "Scotty,Deputy Owner,177080061,0\n" + "Meister Yuki,Overseer,400745082,0\n"
				+ "xp thirsty,Overseer,895476392,0\n" + "DNA Rex,Overseer,862044484,0";
	}


	@Test
	public void testCanParseRsOutput() throws Exception {
		RsClanMemberFetcher fetcher = Mockito.mock(RsClanMemberFetcher.class);
		List<String> lines = Arrays.stream(sampleData.split("\\n")).collect(Collectors.toList());
		Mockito.when(fetcher.fetchRsClanListing()).thenReturn(lines);
		Mockito.when(fetcher.getMembers()).thenCallRealMethod();
		Mockito.when(fetcher.correlateMemberToRanks(Mockito.anyString())).thenReturn(ClanRank.ELDAR);

		Map<String, ClanRank> map = fetcher.getMembers();
		Assertions.assertEquals(9, map.size());
		Assertions.assertEquals(ClanRank.ELDAR, map.get("Applejuiceaj"));
	}

	@Test
	public void testCorrelateRank()
	{
		RsClanMemberFetcher fetcher = new RsClanMemberFetcher(null);
		Assertions.assertEquals(fetcher.correlateMemberToRanks("Deputy_Owner"), ClanRank.ELDAR);
	}
}
