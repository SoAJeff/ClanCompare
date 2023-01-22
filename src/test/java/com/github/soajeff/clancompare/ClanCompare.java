package com.github.soajeff.clancompare;

import java.util.List;

public class ClanCompare {

	private static String clanFetchUrl = "https://forums.soa-rs.com/api/core/members";
	private static String clanFetchApiKey;
	private static String rsClanFetchUrl = "https://secure.runescape.com/m=clan-hiscores/members_lite.ws?clanName=Spirits%20of%20Arianwyn";
	private static String altFetchUrl = "https://competitions.spiritsofarianwyn.com/viewnames.php?compid=";
	private static int compId = 297;

	public static void main(String[] args) {
		parseArgs(args);
		ClanCompareProcessor processor = new ClanCompareProcessor(clanFetchUrl, clanFetchApiKey, rsClanFetchUrl,
				altFetchUrl, compId);
		List<String> results = processor.process();

		if (results.size() == 0) {
			System.out.println("Everything lines up.");
		} else {
			for (String rs : results) {
				System.out.println(rs);
			}
		}
	}

	public static void parseArgs(String[] args) {
		int i = 0;
		while (args.length > i) {
			if (args[i].equalsIgnoreCase("-apikey")) {
				i++;
				clanFetchApiKey = args[i];
			} else if (args[i].equalsIgnoreCase("-forumUrl")) {
				i++;
				clanFetchUrl = args[i];
			} else if (args[i].equalsIgnoreCase("-rsUrl")) {
				i++;
				rsClanFetchUrl = args[i];
			}
			i++;
		}
	}

}
