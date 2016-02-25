package net.msonic.testsyncdata.sync.adapters;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.bus.ProductService;
import net.msonic.testsyncdata.notification.BusProvider;

import javax.inject.Inject;

/**
 * Created by User01 on 25/02/2016.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    @Inject
    BusProvider busProvider;

    @Inject
    ProductService productService;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

            ((CustomApplication) context).getDiComponent().inject(this);

    }


    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {

        productService.syncToServer();

    }

}
