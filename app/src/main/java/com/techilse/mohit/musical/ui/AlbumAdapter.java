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
import com.techilse.mohit.musical.util.AlbumArtCache;

import java.util.List;

/**
 * Created by Mohit on 27-10-2014.
 */
public class AlbumAdapter extends ArrayAdapter<Album> {

    private int resource;
    private LayoutInflater inflater;
    private Context context;
    private AlbumArtCache albumCache;

    public AlbumAdapter(Context context, List<Album> values, AlbumArtCache albumCache) {
        super(context, R.layout.album_row_layout, values);
        resource = R.layout.album_row_layout;
        this.context = context;
        this.albumCache = albumCache;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View theView;
        theView = layoutInflater.inflate(R.layout.album_row_layout, parent, false);
        Album album = getItem(position);
        TextView textView = (TextView) theView.findViewById(R.id.albumName);
        textView.setText(album.getAlbumName());
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        Bitmap bm = albumCache.getAlbumArtBitmap(album.getAlbumArt());
        ImageView imageView = (ImageView) theView.findViewById(R.id.albumArt);
        //imageView.setImageURI(Uri.parse(album.getAlbumArt() != null ? album.getAlbumArt() : ""));
        imageView.setImageBitmap(bm);
        return theView;
    }


}
