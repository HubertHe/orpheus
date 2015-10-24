package com.hippo.orpheus;

import com.hippo.orpheus.toolbox.Request;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Hubert on 15/10/25.
 */
class RequestOperationHandler extends OperationHandler<Request> {

    private Orpheus orpheus;

    public RequestOperationHandler(BlockingQueue<Request> queue, Orpheus orpheus) {
        super(queue);
        this.orpheus = orpheus;
    }

    @Override
    protected void dispatch(Request data) {

    }
}
