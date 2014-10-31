package com.techilse.mohit.musical.entity;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Mohit on 10-10-2014.
 */

public class Song implements Serializable {
    private long songId;
    private String pathToFile;
    private String mimeType;
    private long size;
    private String fileName;
    private String title;
    private long duration;
    private long albumId;
    private String albumName;
    private String year;
    private String artist;
    private String composer;
    private String albumArt;


    private String songDetail;
    private boolean played;

    public Song() {

    }

    public Song(long songId, String pathToFile, String mimeType, long size, String fileName, String title, long duration, long albumId, String albumName, String year, String artist, String composer) {
        this.songId = songId;
        this.pathToFile = pathToFile;
        this.mimeType = mimeType;
        this.size = size;
        this.fileName = fileName;
        this.title = title;
        this.duration = duration;
        this.albumId = albumId;
        this.albumName = albumName;
        this.year = year;
        this.artist = artist;
        this.composer = composer;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getSongDetail() {
        return (getTitle() == null ? getFileName().substring(0, getFileName().lastIndexOf(".")) + " | " : getTitle() + " | ") +
                (getArtist() == null ? "" : getArtist() + " | ") +
                (getAlbumName() == null ? "" : getAlbumName() + " | ") +
                (getComposer() == null ? "" : getComposer() + " | ") +
                (getYear() == null ? "" : getYear() + " | ");

    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song song = (Song) o;

        if (songId != song.songId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (songId ^ (songId >>> 32));
    }

    //Sorters
    public static Comparator<Song> YEAR_SORTER = new Comparator<Song>() {
        @Override
        public int compare(Song song, Song song2) {
            return song.getYear().compareToIgnoreCase(song2.getYear());
        }
    };

    public static Comparator<Song> ALBUM_SORTER = new Comparator<Song>() {
        @Override
        public int compare(Song song, Song song2) {
            return song.getAlbumName().compareToIgnoreCase(song2.getAlbumName());
        }
    };

    public static Comparator<Song> TITLE_SORTER = new Comparator<Song>() {
        @Override
        public int compare(Song song, Song song2) {
            return song.getTitle().compareToIgnoreCase(song2.getTitle());
        }
    };

}
