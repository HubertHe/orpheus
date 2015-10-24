package com.hippo.orpheus;

/**
 * Created by Hubert on 15/10/24.
 */
public interface Callback {

    void onLoadSuccess();

    void onError(Exception e);
}
