package com.github.soajeff.clancompare;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.soajeff.clancompare.alts.AltFetcher;
import com.github.soajeff.clancompare.ipb.IpbMemberFetcher;
import com.github.soajeff.clancompare.rs.RsClanMemberFetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClanCompareProcessor {

	private final String clanFetchUrl;
	private final String clanFetchApiKey;
	private final String rsClanFetchUrl;
	private final String altFetchUrl;
	private final int compId;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	public ClanCompareProcessor(String clanFetchUrl, String clanFetchApiKey, String rsClanFetchUrl, String altFetchUrl,
			int compId) {
		this.clanFetchUrl = clanFetchUrl;
		this.clanFetchApiKey = clanFetchApiKey;
		this.rsClanFetchUrl = rsClanFetchUrl;
		this.altFetchUrl = altFetchUrl;
		this.compId = compId;
	}

	public List<String> process() {
		Map<String, ClanRank> ipbResults = runIpbFetchProcess();
		Map<String, ClanRank> rsResults = runRsFetchProcess();
		Map<String, ClanRank> altResults = runAltFetchProcess();

		return getResults(ipbResults, rsResults, altResults);
	}

	private Map<String, ClanRank> runIpbFetchProcess() {
		IpbMemberFetcher fetcher = new IpbMemberFetcher(clanFetchUrl, clanFetchApiKey);
		return fetcher.getMembers();
	}

	private Map<String, ClanRank> runRsFetchProcess() {
		RsClanMemberFetcher fetcher = new RsClanMemberFetcher(rsClanFetchUrl);
		return fetcher.getMembers();
	}

	private Map<String, ClanRank> runAltFetchProcess() {
		AltFetcher fetcher = new AltFetcher(altFetchUrl, compId);
		return fetcher.getAlts();
	}

	List<String> getResults(Map<String, ClanRank> ipbResults, Map<String, ClanRank> rsResults, Map<String, ClanRank> altResults) {
		List<String> results = new ArrayList<>();
		List<String> comparedUsers = new ArrayList<>();
		for (String member : ipbResults.keySet()) {
			logger.trace("Analyzing member: " + member);
			comparedUsers.add(member);

			//We are not unknown, so we should actually compare
			if (!ipbResults.get(member).equals(ClanRank.UNKNOWN)) {
				ClanRank rank = ipbResults.get(member);
				if (!rsResults.containsKey(member)) {
					//Sere shouldn't be in clan chat as they have passed, so leave them out of this check.
					//Also include Bael Sweep here to be excluded!
					if (!rank.equals(ClanRank.SERE) && !rank.equals(ClanRank.REGISTERED_USER)) {
						logger.debug(member + " is listed on the forums as a ranked user (" + rank.getForumGroupName()
								+ "), but is not ranked in clan chat.");
						results.add(member + " is listed on the forums as a ranked user (" + rank.getForumGroupName()
								+ "), but is not ranked in clan chat.");
					}
				} else {
					ClanRank rsClanRank = rsResults.get(member);
					if (!rank.equals(rsClanRank)) {
						//NEED EXCEPTION CASES FOR:
						//Arquendi/Adele/Administrator (same rank, I guess we moved Martin down 5 years ago?  Who knew)
						//Registered User/Applicant (same rank)
						//Eldar/Ontari (same rank)
						if(!rank.getRsClanRank().equals(rsClanRank.getRsClanRank())) {
							logger.debug(member + " is ranked as " + rank.getForumGroupName() + " on the forums but as "
									+ rsClanRank.getForumGroupName() + " in clan chat.");
							results.add(member + " is ranked as " + rank.getForumGroupName() + " on the forums but as "
									+ rsClanRank.getForumGroupName() + " in clan chat.");
						}
					}
				}
			} else {
				if (rsResults.containsKey(member)) {
					logger.debug(member + " is ranked as Unknown Group on the forums but is ranked in the clan chat.");
					results.add(member + " is ranked as Unknown Group on the forums but is ranked in the clan chat.");
				}
			}
		}

		//We've gone through every forum user, now check if any clan users aren't in that list
		List<String> alts = new ArrayList<>();
		for (String member : rsResults.keySet()) {
			if (!comparedUsers.contains(member)) {
				if(altResults.containsKey(member)) {
					alts.add(member);
					//Check rank
					if (!altResults.get(member).getRsClanRank().equals(rsResults.get(member).getRsClanRank())) {
						logger.debug(member + " is ranked as " + rsResults.get(member).getRsClanRank()
								+ " in the clan chat but should be ranked as an alt (" + altResults.get(member).getRsClanRank() + ").");
						results.add(member + " is ranked as " + rsResults.get(member).getRsClanRank()
								+ " in the clan chat but should be ranked as an alt (" + altResults.get(member).getRsClanRank() + ").");
					}
				}
				else {
					logger.debug(member + " is ranked as " + rsResults.get(member).getForumGroupName() + " in the clan chat, but is not a forum user.");
					results.add(member + " is ranked as " + rsResults.get(member).getForumGroupName() + " in the clan chat, but is not a forum user.");
				}
			}
		}
		for (String member : altResults.keySet()) {
			if (!alts.contains(member)) {
				logger.debug(member + " is listed as an alt but is not ranked in the clan chat.");
				results.add(member + " is listed as an alt but is not ranked in the clan chat.");
			}
		}
		return results;
	}
}
