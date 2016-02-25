package net.msonic.testsyncdata.robospice.request;

import android.content.Context;

import com.octo.android.robospice.request.SpiceRequest;

import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.bus.ProductService;
import net.msonic.testsyncdata.service.SyncFromClienteProxy;
import net.msonic.testsyncdata.to.Process;

import javax.inject.Inject;

/**
 * Created by User01 on 24/02/2016.
 */
public class DemoRequest extends SpiceRequest<String> {


    Process process;

    @Inject
    ProductService productService;

      public DemoRequest(Context context) {
        super(String.class);

          ((CustomApplication) context).getDiComponent().inject(this);
    }

    @Override
    public String loadDataFromNetwork() throws Exception {


        process.descripcion="Inicio";

        Thread.sleep(1000*4);
        productService.syncToServer();

        return "";
    }

}
