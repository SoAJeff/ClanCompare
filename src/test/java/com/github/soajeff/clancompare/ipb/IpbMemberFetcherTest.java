package com.github.soajeff.clancompare.ipb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.remote.rmi._RMIConnection_Stub;

import com.github.soajeff.clancompare.ClanRank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class IpbMemberFetcherTest {

	@Test
	public void testDetermineStandardMember()
	{
		MemberResults res = new MemberResults();
		Member member = Mockito.mock(Member.class);
		Group group = Mockito.mock(Group.class);
		Mockito.when(member.getName()).thenReturn("Applejuiceaj");
		Mockito.when(group.getName()).thenReturn("Eldar");
		Mockito.when(member.getPrimaryGroup()).thenReturn(group);
		List<Member> results = new ArrayList<>();
		results.add(member);
		res.setResults(results);

		IpbMemberFetcher fetcher = new IpbMemberFetcher(null, null);
		Map<String, ClanRank> map = new HashMap<>();
		fetcher.correlateMembersToRanks(res, map);

		Assertions.assertTrue(map.size() == 1);
		Assertions.assertTrue(map.get("Applejuiceaj") == ClanRank.ELDAR);
	}

	@Test
	public void testIgnoresJoey()
	{
		MemberResults res = new MemberResults();
		Member member = Mockito.mock(Member.class);
		Group group = Mockito.mock(Group.class);
		Mockito.when(member.getName()).thenReturn("Joey");
		Mockito.when(group.getName()).thenReturn("Administrator");
		Mockito.when(member.getPrimaryGroup()).thenReturn(group);
		List<Member> results = new ArrayList<>();
		results.add(member);
		res.setResults(results);

		IpbMemberFetcher fetcher = new IpbMemberFetcher(null, null);
		Map<String, ClanRank> map = new HashMap<>();
		fetcher.correlateMembersToRanks(res, map);

		Assertions.assertTrue(map.size() == 0);
	}

	@Test
	public void testWeirdRank()
	{
		MemberResults res = new MemberResults();
		Member member = Mockito.mock(Member.class);
		Group group = Mockito.mock(Group.class);
		Mockito.when(member.getName()).thenReturn("Applejuiceaj");
		Mockito.when(group.getName()).thenReturn("Random");
		Mockito.when(member.getPrimaryGroup()).thenReturn(group);
		List<Member> results = new ArrayList<>();
		results.add(member);
		res.setResults(results);

		IpbMemberFetcher fetcher = new IpbMemberFetcher(null, null);
		Map<String, ClanRank> map = new HashMap<>();
		fetcher.correlateMembersToRanks(res, map);

		Assertions.assertTrue(map.size() == 1);
		Assertions.assertTrue(map.get("Applejuiceaj") == ClanRank.UNKNOWN);
	}
}
