package com.techilse.mohit.musical.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import com.techilse.mohit.musical.R;

/**
 * Created by Mohit on 27-10-2014.
 */
public class AlbumArtCache {

    private LruCache<String, Bitmap> memoryCache;
    private Context context;

    private final String DEFAULT_ALBUM_ART = "com.techilse.mohit.musical.util.DEFAULT_ALBUM_ART";

    public AlbumArtCache(Context context) {
        init(context);
    }

    private void init(Context context) {
        final int memClass = ((ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();
        final int cacheSize = 1024 * 1024 * memClass / 8;
        memoryCache = new LruCache(cacheSize);
        this.context = context;
    }

    public Bitmap getAlbumArtBitmap(String url) {
        Bitmap bitmap = null;
        if (!(url != null && url.length() > 0)) {
            url = DEFAULT_ALBUM_ART;
        }
        bitmap = memoryCache.get(url);

        if (bitmap == null) {
            if (url.equals(DEFAULT_ALBUM_ART)) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.raw.missing_album_art);
            } else {
                bitmap = BitmapFactory.decodeFile(url);
            }
            memoryCache.put(url, bitmap);
        }
        return bitmap;
    }


}
