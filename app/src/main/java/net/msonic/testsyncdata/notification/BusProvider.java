package net.msonic.testsyncdata.notification;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by User01 on 24/02/2016.
 */
public class BusProvider extends Bus {


    private final Handler mainThread = new Handler(Looper.getMainLooper());

    public BusProvider() {
    }



    public void postOnMain(final Object event) {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            this.mainThread.post(new Runnable() {
                public void run() {
                    BusProvider.this.post(event);
                }
            });
        }

    }
}
