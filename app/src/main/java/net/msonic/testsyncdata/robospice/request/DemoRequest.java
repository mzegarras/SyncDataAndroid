package net.msonic.testsyncdata.robospice.request;

import android.content.Context;

import com.octo.android.robospice.request.SpiceRequest;

import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.bus.ProductService;
import net.msonic.testsyncdata.service.SyncFromClienteProxy;

import javax.inject.Inject;

/**
 * Created by User01 on 24/02/2016.
 */
public class DemoRequest extends SpiceRequest<String> {


    @Inject
    ProductService productService;

      public DemoRequest(Context context) {
        super(String.class);

          ((CustomApplication) context).getDiComponent().inject(this);
    }

    @Override
    public String loadDataFromNetwork() throws Exception {

        Thread.sleep(1000*4);
        productService.syncToServer();

        return "";
    }

}
