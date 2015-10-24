package com.hippo.orpheus.toolbox;

import android.media.MediaPlayer;
import android.net.Uri;

import com.hippo.orpheus.Orpheus;
import com.hippo.orpheus.RemoteAudioView;

/**
 * Created by Hubert on 15/10/24.
 */
public class RequestCreator {


    private int memoryPolicy;

    Orpheus orpheus;

    Uri uri;

    private int resId;

    public RequestCreator(Orpheus orpheus, Uri uri) {
        this.orpheus = orpheus;
        this.uri = uri;
    }

    public RequestCreator(Orpheus orpheus, int resId) {
        this.orpheus = orpheus;
        this.resId = resId;
    }

    public RequestCreator memoryPolicy(MemoryPolicy policy, MemoryPolicy... additional) {
        if (policy == null) {
            throw new IllegalArgumentException("Memory policy cannot be null.");
        }
        memoryPolicy |= policy.index;
        if (additional != null) {
            for (MemoryPolicy extra : additional) {
                if (extra != null) {
                    memoryPolicy |= extra.index;
                }
            }
        }
        return this;
    }

    public void into(RemoteAudioView target) {
        if (target == null) {
            throw new IllegalArgumentException("Target must not be null.");
        }
        long started = System.nanoTime();
        if (shouldReadFromMemory(memoryPolicy)) {
            MediaPlayer cached = orpheus.quickMemoryCacheCheck(uri.toString());
            if (cached != null) {
                target.onComplete(cached);
                return;
            }
        }
        Request request = new Request();
        if (uri != null) {
            request.setUri(uri);
        }
        if (resId > 0) {
            request.setResId(resId);
        }
        request.started = started;
        orpheus.submitRequest(request);
    }

    private boolean shouldReadFromMemory(int memoryPolicy) {
        return (memoryPolicy & MemoryPolicy.SKIP_MEMORY.index) == 0;
    }

}
