package com.hippo.orpheus.toolbox;

/**
 * Created by Hubert on 15/10/24.
 */
public enum MemoryPolicy {

    SKIP_MEMORY(1), SKIP_NETWORK(2), SKIP_STORAGE(4);

    int index;

    MemoryPolicy(int index) {
        this.index = index;
    }
}
