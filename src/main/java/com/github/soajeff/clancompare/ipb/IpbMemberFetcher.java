package com.github.soajeff.clancompare.ipb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.soajeff.clancompare.ClanRank;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

public class IpbMemberFetcher {

	private final String fetchUrl;
	private final String apiKey;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final List<ClanRank> rankList = new ArrayList<>(
			Arrays.stream(ClanRank.values()).collect(Collectors.toList()));

	public IpbMemberFetcher(String fetchUrl, String apiKey) {
		this.fetchUrl = fetchUrl;
		this.apiKey = apiKey;
	}

	public Map<String, ClanRank> getMembers() {
		MemberResults results = fetchMembers(1);
		Map<String, ClanRank> members = new HashMap<>();
		correlateMembersToRanks(results, members);
		for (int i = 2; i <= results.getTotalPages(); i++) {
			correlateMembersToRanks(fetchMembers(i), members);
		}

		return members;
	}

	void correlateMembersToRanks(MemberResults results, Map<String, ClanRank> members) {
		for (Member m : results.getResults()) {
			//Handle Exception Cases
			if (m.getName().equalsIgnoreCase("Joey")) {
				logger.trace("Skipping Joey, known exception");
			}
			else {
				//Determine clan rank
				ClanRank rank = null;
				for (ClanRank r : rankList) {
					if (m.getPrimaryGroup().getName().equalsIgnoreCase(r.getForumGroupName()))
						rank = r;
				}
				members.put(m.getName().replace(" ", "_"), (rank != null ? rank : ClanRank.UNKNOWN));
			}
		}

	}

	private MemberResults fetchMembers(int page) {
		Client client = ClientBuilder.newClient();
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(this.apiKey, "");
		client.register(feature);
		client.register(JacksonJsonProvider.class);
		WebTarget target = client.target(this.fetchUrl).queryParam("page", page).queryParam("perPage", 100);
		logger.trace("Fetching forum results page " + page);
		return target.request(MediaType.APPLICATION_JSON_TYPE).get(MemberResults.class);
	}
}
