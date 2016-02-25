package net.msonic.testsyncdata.robospice.request;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.octo.android.robospice.request.SpiceRequest;

import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.bus.ProductService;
import net.msonic.testsyncdata.notification.BusProvider;
import net.msonic.testsyncdata.notification.IntentServiceResult;
import net.msonic.testsyncdata.service.SyncFromClienteProxy;
import net.msonic.testsyncdata.to.Process;

import javax.inject.Inject;

/**
 * Created by User01 on 24/02/2016.
 */
public class DemoRequest extends SpiceRequest<String> {


    Process process;

    @Inject
    BusProvider busProvider;

    @Inject
    ProductService productService;

      public DemoRequest(Context context) {
        super(String.class);

          ((CustomApplication) context).getDiComponent().inject(this);
    }

    @Override
    public String loadDataFromNetwork() throws Exception {

        Log.d(DemoRequest.class.getCanonicalName(),"sss");
      //  process.descripcion="Inicio";

        busProvider.postOnMain(new IntentServiceResult(Activity.RESULT_OK, "Inicio"));

        Thread.sleep(1000*4);

        productService.syncToServer();

        //process.descripcion="Inicio";

        busProvider.postOnMain(new IntentServiceResult(Activity.RESULT_OK, "Fin"));


        Log.d(DemoRequest.class.getCanonicalName(),"ssssss");
        return "";
    }

}
