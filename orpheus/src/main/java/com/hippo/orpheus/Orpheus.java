package com.hippo.orpheus;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.hippo.orpheus.toolbox.Request;
import com.hippo.orpheus.toolbox.RequestCreator;
import com.hippo.orpheus.toolbox.Utils;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Hubert on 15/10/24.
 */
public class Orpheus {

    public static final String TAG = "Orpheus";

    public static final int CAPACITY = 3;

    private static Orpheus singleton;

    private final Context context;

    private LruCache<String, MediaPlayer> cache;

    private RequestOperationHandler[] handlers;

    private LinkedBlockingQueue<Request> requestQueue;

    public static Orpheus with(Context context) {
        if (singleton == null) {
            synchronized (Orpheus.class) {
                if (singleton == null) {
                    singleton = new Builder(context).build();
                }
            }
        }
        return singleton;
    }

    public RequestCreator load(String url) {
        return new RequestCreator(this, Uri.parse(url));
    }

    public RequestCreator load(File file) {
        return new RequestCreator(this, Uri.fromFile(file));
    }

    public RequestCreator load(int resId) {
        return new RequestCreator(this, resId);
    }

    private Orpheus(Context context, LruCache<String, MediaPlayer> cache) {
        this.context = context;
        this.cache = cache;
        handlers = new RequestOperationHandler[CAPACITY];
        for (int i = 0; i < CAPACITY; i++) {
            handlers[i] = new RequestOperationHandler(this);
            handlers[i].start();
        }


    }

    public void submitRequest(Request request) {

    }

    private static class Builder {

        private final Context context;

        private LruCache<String, MediaPlayer> cache;

        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }

        public Builder cache(LruCache<String, MediaPlayer> cache) {
            this.cache = cache;
            return this;
        }

        public Orpheus build() {
            if (cache == null) cache = new LruCache<>(Utils.calculateMemoryCacheSize(context));
            return new Orpheus(context, cache);
        }
    }

    public MediaPlayer quickMemoryCacheCheck(String key) {
        MediaPlayer cached = cache.get(key);
        if (cached != null) {
            Log.d(TAG, "Orpheus hit memory: " + key);
        } else {
            Log.d(TAG, "Orpheus miss memory: " + key);
        }
        return cached;
    }

}
