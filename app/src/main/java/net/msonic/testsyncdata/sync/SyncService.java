package net.msonic.testsyncdata.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.sync.adapters.SyncAdapter;

import javax.inject.Inject;

/**
 * Created by User01 on 25/02/2016.
 */
public class SyncService extends Service {

   @Inject
   SyncAdapter syncAdapter;

    // Object to use as a thread-safe lock
    private static final Object syncAdapterLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        ((CustomApplication) getApplication()).getDiComponent().inject(this);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
