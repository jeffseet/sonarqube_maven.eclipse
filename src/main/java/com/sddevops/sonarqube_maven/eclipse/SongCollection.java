package com.sddevops.sonarqube_maven.eclipse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class SongCollection {

	private ArrayList<Song> songs = new ArrayList<>();
	private int capacity;
	private LocalDateTime timeCreated;

	public SongCollection() {
		/*
		 * songs.add(new Song("0001","good 4 u","Olivia Rodrigo",3.59)); songs.add(new
		 * Song("0002","Peaches","Justin Bieber",3.18)); songs.add(new
		 * Song("0003","MONTERO","Lil Nas X",2.3)); songs.add(new
		 * Song("0004","bad guy","Billie Eilish",3.14));
		 */

		this.capacity = 20;
		this.timeCreated = LocalDateTime.now();
	}

	public SongCollection(int capacity) {
		this.capacity = capacity;
		this.timeCreated = LocalDateTime.now();
	}

	public String getFullDateCreated() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return this.timeCreated.format(formatter);
	}

	public String getYearCreated() {
		return String.valueOf(this.timeCreated.getYear());
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void addSong(Song song) {
		if (songs.size() != capacity) {
			songs.add(song);
		}
	}

	public ArrayList<Song> sortSongsByTitle() {
		Collections.sort(songs, Song.titleComparator);
		return songs;
	}

	public ArrayList<Song> sortSongsBySongLength() {
		Collections.sort(songs, Song.songLengthComparator);
		return songs;
	}

	public Song findSongsById(String id) {
		for (Song s : songs) {
			if (s.getId().equals(id))
				return s;
		}
		return null;
	}

	public Song findSongByTitle(String title) {
		for (Song s : songs) {
			if (s.getTitle().equals(title))
				return s;
		}
		return null;
	}

	protected String fetchSongJson() {
		String urlString = "https://mocki.io/v1/36c94419-b141-4cfd-96fa-327f4872aca6";
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder response = new StringBuilder();
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				return response.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Song fetchSongOfTheDay() {
		try {
			String jsonStr = fetchSongJson();
			if (jsonStr == null)
				return null;
			JSONObject json = new JSONObject(jsonStr);
			Song song = new Song(json.getString("id"), json.getString("title"), json.getString("artiste"),
					json.getDouble("songLength"));

			if (song.getArtiste().equals("Taylor Swift")) {
				song.setArtiste("TS");
				this.addSong(song);
			} else if (song.getArtiste().equals("Bruno Mars")) {
				song.setArtiste("BM");
				this.addSong(song);
			}

			return song;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String compareCollection(SongCollection other) {
		if (this.timeCreated.isBefore(other.timeCreated)) {
			return "My collection is older!";
		} else if (this.timeCreated.isEqual(other.timeCreated)) {
			return "My collection was created at the same time!";
		} else {
			return "My collection is newer!";
		}
	}
}
