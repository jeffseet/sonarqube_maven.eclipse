package com.sddevops.sonarqube_maven.eclipse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SongTest {
	private Song song;
	private Song song2;
	private Song song3;

	@BeforeEach
	void setUp() throws Exception {
		song = new Song("111", "Love Story", "Taylor Swift", 5.55);
	}

	@AfterEach
	void tearDown() throws Exception {
		song = null;
		song2 = null;
		song3 = null;

	}

	@Test
	void testHashCode() {
		song = new Song("111", "Love Story", "Taylor Swift", 5.55);
		song2 = new Song("111", "Love Story", "Taylor Swift", 5.55);
		song3 = new Song("222", "Not Love Story", "Not Taylor Swift", 5.55);

		int firstSongHash = song.hashCode();
		int secondSongHash = song2.hashCode();
		int thirdSongHash = song3.hashCode();

		assertEquals(firstSongHash, secondSongHash);
		assertFalse(firstSongHash == thirdSongHash);

	}

	@Test
	void testSong() {
		song2 = new Song("222", "Addicted", "Simple Plan", 5.55);
		assertEquals(song2.getArtiste(), "Simple Plan");
	}

	@Test
	void testGetId() {
		assertEquals(song.getId(), "111");
	}

	@Test
	void testSetId() {
		song.setId("555");
		assertEquals(song.getId(), "555");
	}

	@Test
	void testGetTitle() {
		assertEquals(song.getTitle(), "Love Story");
	}

	@Test
	void testSetTitle() {
		song.setTitle("Not Love story");
		assertEquals(song.getTitle(), "Not Love story");
	}

	@Test
	void testGetArtiste() {
		assertEquals(song.getArtiste(), "Taylor Swift");
	}

	@Test
	void testSetArtiste() {
		song.setArtiste("Not Taylor");
		assertEquals(song.getArtiste(), "Not Taylor");
	}

	@Test
	void testGetSongLength() {
		assertEquals(song.getSongLength(), 5.55);

	}

	@Test
	void testSetSongLength() {
		song.setSongLength(3.33);

		assertEquals(song.getSongLength(), 3.33);
	}

	@Test
	void testEqualsObject() {
		Song song3 = new Song("111", "Love Story", "Taylor Swift", 5.55);
		Song song4 = new Song("222", "I'm Just A Kid", "Simple Plan", 4.44);

		assertTrue(song.equals(song));
		assertFalse(song.equals("123"));
		assertTrue(song.equals(song3));
		assertFalse(song.equals(song4));
	}

}
