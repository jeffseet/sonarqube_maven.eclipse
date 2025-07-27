package com.sddevops.sonarqube_maven.eclipse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SongTest {

	private Song song;
	private Song song2;

	@BeforeEach
	void setUp() {
		song = new Song("111", "Love Story", "Taylor Swift", 5.55);
	}

	@AfterEach
	void tearDown() {
		song = null;
		song2 = null;
	}

	@Test
	void testSongConstructorAndGetters() {
		song2 = new Song("222", "Addicted", "Simple Plan", 4.44);
		assertEquals("222", song2.getId());
		assertEquals("Addicted", song2.getTitle());
		assertEquals("Simple Plan", song2.getArtiste());
		assertEquals(4.44, song2.getSongLength());
	}

	@Test
	void testSetId() {
		song.setId("555");
		assertEquals("555", song.getId());
	}

	@Test
	void testSetTitle() {
		song.setTitle("Not Love Story");
		assertEquals("Not Love Story", song.getTitle());
	}

	@Test
	void testSetArtiste() {
		song.setArtiste("Not Taylor");
		assertEquals("Not Taylor", song.getArtiste());
	}

	@Test
	void testSetSongLength() {
		song.setSongLength(3.33);
		assertEquals(3.33, song.getSongLength());
	}

	@Test
	void testEqualsObject() {
		Song sameSong = new Song("111", "Love Story", "Taylor Swift", 5.55);
		Song differentSong = new Song("222", "I'm Just A Kid", "Simple Plan", 4.44);

		assertEquals(song, song); // self comparison
		assertNotEquals("123", song); // different type
		assertEquals(song, sameSong); // equal values
		assertNotEquals(song, differentSong); // different values
	}

	@Test
	void testHashCode() {
		song2 = new Song("111", "Love Story", "Taylor Swift", 5.55);
		Song differentSong = new Song("222", "Not Love Story", "Not Taylor Swift", 5.55);

		int firstSongHash = song.hashCode();
		int secondSongHash = song2.hashCode();
		int thirdSongHash = differentSong.hashCode();

		assertEquals(firstSongHash, secondSongHash);
		assertNotEquals(firstSongHash, thirdSongHash);
	}

	@Test
	void testToString() {
		assertEquals("Love Story by Taylor Swift", song.toString());
	}

	@Test
	void testTitleComparator() {
		Song a = new Song("1", "A Song", "Artist A", 3.0);
		Song b = new Song("2", "B Song", "Artist B", 3.0);
		Song c = new Song("3", "A Song", "Artist C", 2.0);

		assertEquals(-1, Song.titleComparator.compare(a, b)); // A < B
		assertEquals(0, Song.titleComparator.compare(a, c)); // A == A
		assertEquals(1, Song.titleComparator.compare(b, a)); // B > A
	}

	@Test
	void testSongLengthComparator() {
		Song a = new Song("1", "Song A", "Artist A", 4.0);
		Song b = new Song("2", "Song B", "Artist B", 5.0);
		Song c = new Song("3", "Song C", "Artist C", 4.0);

		assertEquals(-1, Song.songLengthComparator.compare(b, a)); // b(5.0) before a(4.0)
		assertEquals(0, Song.songLengthComparator.compare(a, c)); // equal lengths
		assertEquals(1, Song.songLengthComparator.compare(a, b)); // a(4.0) after b(5.0)
	}
}