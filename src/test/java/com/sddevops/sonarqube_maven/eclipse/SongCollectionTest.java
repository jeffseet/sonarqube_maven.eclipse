package com.sddevops.sonarqube_maven.eclipse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

class SongCollectionTest {

	private SongCollection sc;
	private Song s1, s2, s3, s4;
	private static final int SONG_COLLECTION_SIZE = 4;

	@BeforeEach
	void setUp() {
		sc = new SongCollection();
		s1 = new Song("001", "good 4 u", "Olivia Rodrigo", 3.59);
		s2 = new Song("002", "Peaches", "Justin Bieber", 3.18);
		s3 = new Song("003", "MONTERO", "Lil Nas", 2.3);
		s4 = new Song("004", "bad guy", "billie eilish", 3.14);
		sc.addSong(s1);
		sc.addSong(s2);
		sc.addSong(s3);
		sc.addSong(s4);
	}

	@AfterEach
	void tearDown() {
		sc = null;
	}

	@Test
	void testGetSongs() {
		assertEquals(SONG_COLLECTION_SIZE, sc.getSongs().size());
	}

	@Test
	void testAddSong() {
		Song newSong = new Song("005", "Levitating", "Dua Lipa", 3.5);
		sc.addSong(newSong);
		assertEquals(SONG_COLLECTION_SIZE + 1, sc.getSongs().size());
	}

	@Test
	void testAddSongExceedingCapacity() {
		SongCollection smallCollection = new SongCollection(2);
		smallCollection.addSong(s1);
		smallCollection.addSong(s2);
		smallCollection.addSong(s3); // should NOT be added
		assertEquals(2, smallCollection.getSongs().size());
	}

	@Test
	void testSortSongsByTitle() {
		List<Song> sorted = sc.sortSongsByTitle();
		assertEquals("MONTERO", sorted.get(0).getTitle());
		assertEquals("Peaches", sorted.get(1).getTitle());
		assertEquals("bad guy", sorted.get(2).getTitle());
		assertEquals("good 4 u", sorted.get(3).getTitle());
	}

	@Test
	void testSortSongsBySongLength() {
		List<Song> sorted = sc.sortSongsBySongLength();
		assertEquals(3.59, sorted.get(0).getSongLength());
		assertEquals(3.18, sorted.get(1).getSongLength());
		assertEquals(3.14, sorted.get(2).getSongLength());
		assertEquals(2.3, sorted.get(3).getSongLength());
	}

	@Test
	void testFindSongsById() {
		assertEquals("billie eilish", sc.findSongsById("004").getArtiste());
		assertNull(sc.findSongsById("not_found"));
	}

	@Test
	void testFindSongByTitle() {
		assertEquals("Lil Nas", sc.findSongByTitle("MONTERO").getArtiste());
		assertNull(sc.findSongByTitle("non_existent_title"));
	}

	// ---------- Parameterized fetchSongOfTheDay Tests ----------
	static class FetchTestCase {
		String json;
		String expectedArtiste;
		int expectedSize;

		FetchTestCase(String json, String expectedArtiste, int expectedSize) {
			this.json = json;
			this.expectedArtiste = expectedArtiste;
			this.expectedSize = expectedSize;
		}
	}

	static Stream<FetchTestCase> fetchSongTestCases() {
		return Stream.of(new FetchTestCase("""
				{"id": "007", "title": "Perfect", "artiste": "Ed Sheeran", "songLength": 4.20}
				""", "Ed Sheeran", 0), new FetchTestCase("""
				{"id": "008", "title": "Enchanted", "artiste": "Taylor Swift", "songLength": 3.75}
				""", "TS", 1), new FetchTestCase("""
				{"id": "009", "title": "Grenade", "artiste": "Bruno Mars", "songLength": 3.95}
				""", "BM", 1));
	}

	@ParameterizedTest
	@MethodSource("fetchSongTestCases")
	void testFetchSongOfTheDay_Various(FetchTestCase testCase) {
		SongCollection collection = spy(new SongCollection());
		doReturn(testCase.json).when(collection).fetchSongJson();

		Song result = collection.fetchSongOfTheDay();

		assertNotNull(result);
		assertEquals(testCase.expectedArtiste, result.getArtiste());
		assertEquals(testCase.expectedSize, collection.getSongs().size());
	}

	@Test
	void testFetchSongOfTheDay_NullJson() {
		SongCollection spyCollection = spy(new SongCollection());
		doReturn(null).when(spyCollection).fetchSongJson();
		assertNull(spyCollection.fetchSongOfTheDay());
	}

	@Test
	void testFetchSongOfTheDay_Exception() {
		SongCollection spyCollection = spy(new SongCollection());
		doThrow(new RuntimeException("Fail")).when(spyCollection).fetchSongJson();
		assertNull(spyCollection.fetchSongOfTheDay());
		assertEquals(0, spyCollection.getSongs().size());
	}

	@Test
	void testGetYearCreated() {
		LocalDateTime fixedDate = LocalDateTime.of(2023, Month.AUGUST, 10, 12, 0);
		try (MockedStatic<LocalDateTime> mock = mockStatic(LocalDateTime.class)) {
			mock.when(LocalDateTime::now).thenReturn(fixedDate);
			assertEquals("2023", new SongCollection().getYearCreated());
		}
	}

	@Test
	void testGetFullDateCreated() {
		LocalDateTime fixedDate = LocalDateTime.of(2024, Month.MARCH, 15, 10, 30);
		try (MockedStatic<LocalDateTime> mock = mockStatic(LocalDateTime.class)) {
			mock.when(LocalDateTime::now).thenReturn(fixedDate);
			assertEquals("15/03/2024", new SongCollection().getFullDateCreated());
		}
	}

	@Test
	void testCompareCollection_SameTime() {
		LocalDateTime date = LocalDateTime.of(2025, Month.JULY, 1, 12, 0);
		try (MockedStatic<LocalDateTime> mock = mockStatic(LocalDateTime.class)) {
			mock.when(LocalDateTime::now).thenReturn(date);
			SongCollection one = new SongCollection();
			mock.when(LocalDateTime::now).thenReturn(date);
			SongCollection two = new SongCollection();
			assertEquals("My collection was created at the same time!", one.compareCollection(two));
		}
	}

	@Test
	void testCompareCollection_Older() {
		LocalDateTime earlier = LocalDateTime.of(2023, 1, 1, 10, 0);
		LocalDateTime later = LocalDateTime.of(2024, 1, 1, 10, 0);

		try (MockedStatic<LocalDateTime> mock = mockStatic(LocalDateTime.class)) {
			mock.when(LocalDateTime::now).thenReturn(earlier);
			SongCollection old = new SongCollection();
			mock.when(LocalDateTime::now).thenReturn(later);
			SongCollection young = new SongCollection();

			assertEquals("My collection is older!", old.compareCollection(young));
		}
	}

	@Test
	void testCompareCollection_Newer() {
		LocalDateTime later = LocalDateTime.of(2025, 1, 1, 10, 0);
		LocalDateTime earlier = LocalDateTime.of(2024, 1, 1, 10, 0);

		try (MockedStatic<LocalDateTime> mock = mockStatic(LocalDateTime.class)) {
			mock.when(LocalDateTime::now).thenReturn(later);
			SongCollection newCol = new SongCollection();
			mock.when(LocalDateTime::now).thenReturn(earlier);
			SongCollection oldCol = new SongCollection();

			assertEquals("My collection is newer!", newCol.compareCollection(oldCol));
		}
	}

	@Test
	void testFetchSongJson_HttpError() {
		// Anonymous subclass to trigger fetchSongJson error handling branch
		SongCollection errorCollection = new SongCollection() {
			@Override
			protected String fetchSongJson() {
				try {
					// force exception by malformed URL
					new java.net.URL("ht!tp://bad_url").openStream();
				} catch (Exception e) {
					// expected exception path
					e.printStackTrace();
				}
				return null;
			}
		};

		assertNull(errorCollection.fetchSongOfTheDay());
	}
}