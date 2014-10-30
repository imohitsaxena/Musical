package com.techilse.mohit.musical.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.techilse.mohit.musical.R;
import com.techilse.mohit.musical.entity.Album;
import com.techilse.mohit.musical.entity.Song;
import com.techilse.mohit.musical.util.AlbumArtCache;

/**
 * Created by Mohit on 27-10-2014.
 */
public class SongAdapter extends ArrayAdapter<Song> {

    private Album album;
    private AlbumArtCache albumCache;

    public SongAdapter(Context context, Album album, AlbumArtCache albumCache) {
        super(context, R.layout.song_row_layout, album.getSongs());
        this.album = album;
        this.albumCache = albumCache;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View theView = layoutInflater.inflate(R.layout.song_row_layout, parent, false);
        Song song = getItem(position);

        TextView songName = (TextView) theView.findViewById(R.id.songName);
        songName.setText(song.getTitle());
        songName.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        TextView songDetails = (TextView) theView.findViewById(R.id.songDetails);
        songDetails.setText(song.getArtist() + " | " + song.getComposer() + " | " + song.getYear());
        songDetails.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        Bitmap bm = albumCache.getAlbumArtBitmap(album.getAlbumArt());
        ImageView imageView = (ImageView) theView.findViewById(R.id.albumArt);
        //imageView.setImageURI(Uri.parse(album.getAlbumArt() != null ? album.getAlbumArt() : ""));
        imageView.setImageBitmap(bm);
        return theView;
    }

}
