package com.github.soajeff.clancompare.alts;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.github.soajeff.clancompare.ClanRank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AltFetcher {

	private final String url;
	private final int compId;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public AltFetcher(String url, int compId) {
		this.url = url;
		this.compId = compId;
	}

	public Map<String, ClanRank> getAlts() {
		Map<String, ClanRank> members = new HashMap<>();
		List<String> altsListing = null;
		try
		{
			altsListing = fetchAltsListing();
		}
		catch(Exception e)
		{
			logger.error("Error fetching alts listing", e);
			//throw e;
		}
		//All alts are listed as RANKED_ALT in enum
		if(altsListing != null) {
			altsListing.forEach(alt -> members.put(alt, ClanRank.RANKED_ALT));
		}
		return members;
	}

	public List<String> fetchAltsListing() throws Exception {

		URL urlToDownload = new URL(url + compId);
		List<String> linesRead = null;

		HttpsURLConnection connection = (HttpsURLConnection) urlToDownload.openConnection();
		if (connection.getResponseCode() == 200) {
			try (InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
					BufferedReader br = new BufferedReader(streamReader)) {
				String line = br.readLine();
				linesRead = parseListing(line);
			}
		} else {
			logger.error("Error fetching alts listing");
		}
		return linesRead;
	}

	List<String> parseListing(String line) {
		List<String> linesRead;
		line = line.substring(line.indexOf("<br><br>") + 8);
		line = line.substring(0, line.indexOf("<br><a"));
		linesRead = Arrays.asList(line.split("<br>"));
		return linesRead;
	}
}
