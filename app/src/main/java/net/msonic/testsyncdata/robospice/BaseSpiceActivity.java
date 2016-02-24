package net.msonic.testsyncdata.robospice;

import android.support.v7.app.AppCompatActivity;

import com.octo.android.robospice.SpiceManager;

/**
 * Created by User01 on 24/02/2016.
 */
public class BaseSpiceActivity extends AppCompatActivity {


    private SpiceManager spiceManager = new SpiceManager(OfflineSpiceService.class);

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }

}
