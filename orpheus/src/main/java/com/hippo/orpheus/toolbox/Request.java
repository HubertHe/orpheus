package com.hippo.orpheus.toolbox;

import android.net.Uri;

/**
 * Created by Hubert on 15/10/25.
 */
public class Request {

    private Uri uri;

    private int resId;

    long started;

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
        resId = 0;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
        uri = null;
    }
}
