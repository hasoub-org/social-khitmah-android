package org.hasoub.socialkhitmah;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by personal on 3/14/15.
 */
public class KhitmahService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return Service.START_STICKY;
    }
}
