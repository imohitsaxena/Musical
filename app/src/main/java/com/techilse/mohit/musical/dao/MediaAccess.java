package com.techilse.mohit.musical.dao;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.techilse.mohit.musical.entity.Album;
import com.techilse.mohit.musical.entity.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mohit on 13-10-2014.
 */
public class MediaAccess {
    private Context context;

    public MediaAccess(Context context) {
        this.context = context;
    }

    public List<Song> getAllSongs(List<Album> albums){
        List<Song> songs = new ArrayList<Song>();
        Collections.sort(albums,Album.ALBUM_NAME_SORTER);
        for(Album a: albums){
            songs.addAll(a.getSongs());
        }
        return songs;
    }

    /*
    public List<Song> getAllSongs() {

        String[] albuminfo_columns = {"_id", "album_art", "minyear", "album"};
        String[] audio_columns = {"_id", "_data", "mime_type", "_size", "_display_name", "title", "duration", "album_id", "album", "year", "artist", "composer"};

        Uri mediaUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;


        ContentResolver contentResolver = context.getContentResolver();

        Cursor albumCursor = contentResolver.query(albumUri, albuminfo_columns, null, null, null);
        Cursor mediaCursor = contentResolver.query(mediaUri, audio_columns, "mime_type in ('audio/mpeg','audio/aac','audio/mp4')", null, null);

        List<Song> songs = new ArrayList<Song>();
        Map<Long, String> albumArtMap = new HashMap<Long, String>();
        Song song;

        mediaCursor.moveToFirst();
        albumCursor.moveToFirst();

        do {
            albumArtMap.put(albumCursor.getLong(0), albumCursor.getString(1));
        } while (albumCursor.moveToNext());

        albumCursor.close();

        do {
            song = new Song(mediaCursor.getLong(0), mediaCursor.getString(1), mediaCursor.getString(2), mediaCursor.getLong(3),
                    mediaCursor.getString(4), mediaCursor.getString(5), mediaCursor.getLong(6), mediaCursor.getLong(7),
                    mediaCursor.getString(8), mediaCursor.getString(9), mediaCursor.getString(10), mediaCursor.getString(11));
            song.setAlbumArt(albumArtMap.get(song.getAlbumId()));
            songs.add(song);
        } while (mediaCursor.moveToNext());

        mediaCursor.close();

        return songs;
    }
*/
    /*
    * Redo the method more effectively using getAllSongs()
    * */
    public List<Album> getAllAlbums(List<Song> songs) {
        String[] albuminfo_columns = {"_id", "album_art", "minyear", "album"};
        String[] audio_columns = {"_id", "_data", "mime_type", "_size", "_display_name", "title", "duration", "album_id", "album", "year", "artist", "composer"};

        Uri mediaUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;


        ContentResolver contentResolver = context.getContentResolver();

        Cursor albumCursor = contentResolver.query(albumUri, albuminfo_columns, null, null, null);
        Cursor mediaCursor = null;

        songs = null;
        Song song;

        albumCursor.moveToFirst();
        Album album = null;
        List<Album> albums = new ArrayList<Album>();

        do {
            album = new Album();
            album.setAlbumId(albumCursor.getLong(0));
            album.setAlbumArt(albumCursor.getString(1));
            album.setAlbumName(albumCursor.getString(3));
            mediaCursor = contentResolver.query(mediaUri, audio_columns, "mime_type in ('audio/mpeg','audio/aac','audio/mp4') and album_id = '" + album.getAlbumId() + "'", null, null);
            mediaCursor.moveToFirst();
            songs = new ArrayList<Song>();
            do {
                song = new Song(mediaCursor.getLong(0), mediaCursor.getString(1), mediaCursor.getString(2), mediaCursor.getLong(3),
                        mediaCursor.getString(4), mediaCursor.getString(5), mediaCursor.getLong(6), mediaCursor.getLong(7),
                        mediaCursor.getString(8), mediaCursor.getString(9), mediaCursor.getString(10), mediaCursor.getString(11));
                song.setAlbumArt(album.getAlbumArt());
                songs.add(song);
            } while (mediaCursor.moveToNext());
            mediaCursor.close();
            album.setSongs(songs);
            albums.add(album);
            album = null;
            songs = null;
        } while (albumCursor.moveToNext());
        albumCursor.close();
        return albums;
    }

}
