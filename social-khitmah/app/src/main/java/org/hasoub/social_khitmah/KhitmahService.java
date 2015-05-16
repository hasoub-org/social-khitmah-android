package org.hasoub.social_khitmah;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by personal on 2/21/15.
 */
class KhitmahService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public KhitmahService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        String dataString = workIntent.getDataString();

    }
}