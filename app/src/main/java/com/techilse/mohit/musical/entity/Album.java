package com.techilse.mohit.musical.entity;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Mohit on 13-10-2014.
 */
public class Album {
    private List<Song> songs;
    private String albumName;
    private Long albumId;
    private String albumArt;
    private String yearOfFirstSong;

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getYearOfFirstSong() {
        return yearOfFirstSong;
    }

    public void setYearOfFirstSong(String yearOfFirstSong) {
        this.yearOfFirstSong = yearOfFirstSong;
    }

    @Override
    public String toString() {
        return getAlbumName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;

        if (!albumId.equals(album.albumId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return albumId.hashCode();
    }

    public static Comparator<Album> ALBUM_NAME_SORTER = new Comparator<Album>() {
        @Override
        public int compare(Album album1, Album album2) {
            return album1.getAlbumName().compareToIgnoreCase(album2.getAlbumName());
        }
    };
}