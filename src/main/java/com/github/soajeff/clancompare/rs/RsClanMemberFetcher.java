package com.github.soajeff.clancompare.rs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.net.ssl.HttpsURLConnection;

import com.github.soajeff.clancompare.ClanRank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RsClanMemberFetcher {

	private final String url;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final List<ClanRank> rankList = new ArrayList<>(
			Arrays.stream(ClanRank.values()).collect(Collectors.toList()));

	public RsClanMemberFetcher(String url) {
		this.url = url;
	}

	public Map<String, ClanRank> getMembers() {
		Map<String, ClanRank> members = new HashMap<>();
		List<String> rsCLanListing = null;
		try {
			rsCLanListing = fetchRsClanListing();
			//Ditch the first line, since its a header row.
			rsCLanListing.remove(0);
		} catch (Exception e) {
			logger.error("Error fetching RS Clan listing", e);
		}
		if(rsCLanListing != null) {
			for (String line : rsCLanListing) {
				String[] member = line.split(",");
				//Exception case
				if(member[0].equalsIgnoreCase("Princess_Rae"))
					members.put("Princess_Rae", ClanRank.ELDAR);
				else {
					ClanRank rank = correlateMemberToRanks(member[1]);
					members.put(member[0], rank);
				}
			}
		}

		return members;
	}

	ClanRank correlateMemberToRanks(String rank) {
		for (ClanRank r : rankList) {
			if (r.getRsClanRank().equalsIgnoreCase(rank))
				return r;
		}
		return ClanRank.UNKNOWN;

	}

	public List<String> fetchRsClanListing() throws Exception {
		URL urlToDownload = new URL(url);
		List<String> linesRead = new ArrayList<>();

		HttpsURLConnection connection = (HttpsURLConnection) urlToDownload.openConnection();
		if (connection.getResponseCode() == 200) {
			try (InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
					BufferedReader br = new BufferedReader(streamReader);
					Stream<String> lines = br.lines()) {
				linesRead.addAll(
						lines.map(s -> s.replace("�", "_"))
								.map(s->s.replace(" ", "_"))
								.map(s -> s.replace(" ", "_")).collect(Collectors.toList()));
			}
		}
		else {
			logger.error("Error fetching RS Clan listing");
		}
		return linesRead;
	}
}
