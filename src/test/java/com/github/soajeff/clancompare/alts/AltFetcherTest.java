package com.github.soajeff.clancompare.alts;

import java.util.List;
import java.util.Map;

import com.github.soajeff.clancompare.ClanRank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AltFetcherTest {

	private String sampleData;

	@BeforeEach
	public void setup() {
		sampleData = "<a href=\"#1k\">Attendance (1k minimum/Skill of the Week)</a><br><a href=\"#50k\">Attendance (50k minimum/Monthly Comp)</a><br>"
				+ "<a href=\"#byxp\">All participants by XP gain (descending)</a><br><hr><hr>All participants<br><br>Amyntas_Bion<br>Kasteal<br>"
				+ "Lincoln_Lady<br>Rumberry<br>Satyrinae<br>Sylla9182<br>Xander<br><a name=\"1k\"></a><hr><hr>"
				+ "Participants qualifying for attendance (1k)<br><br>Amyntas_Bion<br>Kasteal<br>Lincoln_Lady<br>Rumberry<br>"
				+ "Satyrinae<br>Sylla9182<br>Xander<br><a name=\"50k\"></a><hr><hr>Participants qualifying for attendance (50k)<br>"
				+ "<br>Amyntas_Bion<br>Kasteal<br>Lincoln_Lady<br>Rumberry<br>Satyrinae<br>Sylla9182<br>Xander<br><a name=\"byxp\"></a>"
				+ "<hr><hr>All participants by XP gain (descending)<br><br>Xander ~ 28,039,416<br>Satyrinae ~ 9,917,556<br>Sylla9182 ~ 6,102,969"
				+ "<br>Rumberry ~ 5,926,364<br>Amyntas_Bion ~ 2,688,594<br>Lincoln_Lady ~ 2,188,282<br>Kasteal ~ 1,068,360<br>";
	}

	@Test
	public void testAltFetcher() throws Exception {
		AltFetcher fetcher = new AltFetcher(null, -1);
		List<String> lines = fetcher.parseListing(sampleData);
		Assertions.assertEquals(7, lines.size());
		Assertions.assertTrue(lines.contains("Amyntas_Bion"));
		Assertions.assertTrue(lines.contains("Kasteal"));
		Assertions.assertTrue(lines.contains("Lincoln_Lady"));
		Assertions.assertTrue(lines.contains("Rumberry"));
		Assertions.assertTrue(lines.contains("Satyrinae"));
		Assertions.assertTrue(lines.contains("Sylla9182"));
		Assertions.assertTrue(lines.contains("Xander"));
	}

	@Test
	public void testGetAlts() throws Exception {
		AltFetcher fetcher = Mockito.mock(AltFetcher.class);
		Mockito.when(fetcher.parseListing(sampleData)).thenCallRealMethod();
		List<String> lines = fetcher.parseListing(sampleData);
		Mockito.when(fetcher.fetchAltsListing()).thenReturn(lines);
		Mockito.when(fetcher.getAlts()).thenCallRealMethod();

		Map<String, ClanRank> map = fetcher.getAlts();
		Assertions.assertEquals(7, map.size());
		Assertions.assertTrue(map.entrySet().stream().allMatch(e->e.getValue().equals(ClanRank.RANKED_ALT)));
	}
}
