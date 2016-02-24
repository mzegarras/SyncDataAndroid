package net.msonic.testsyncdata;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import net.msonic.testsyncdata.bus.ProductService;
import net.msonic.testsyncdata.contract.ResponseList;
import net.msonic.testsyncdata.contract.ResponseRest;
import net.msonic.testsyncdata.dao.ProductDao;
import net.msonic.testsyncdata.robospice.BaseSpiceActivity;
import net.msonic.testsyncdata.robospice.request.DemoRequest;
import net.msonic.testsyncdata.service.SyncFromClienteProxy;
import net.msonic.testsyncdata.service.SyncToClientProy;
import net.msonic.testsyncdata.to.Product;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseSpiceActivity {

    @Inject
    ProductDao productDao;

    @Inject
    ProductService productService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((CustomApplication) getApplication()).getDiComponent().inject(this);

    }



    public void btnAceptar(View view){
        SyncToClientTask task = new SyncToClientTask();
        task.execute("product");
    }

    @Inject
    DemoRequest demoRequest;

    public void btnToServer(View view){


        getSpiceManager().execute(demoRequest, new RequestListener<String>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(String s) {
                Log.d(MainActivity.class.getCanonicalName(),s);
            }
        });

       /* SyncClientToServer task = new SyncClientToServer();
        task.execute();*/




    }




    public void btnAdd(View view){
        Product p = new Product();
        p.name="Product9";
        p.code="00009";

        productService.insertFromClient(p);

    }

    @Inject SyncToClientProy syncToClientProy;
    @Inject
    SyncFromClienteProxy syncFromClienteProxy;
    private class SyncToClientTask extends AsyncTask<String, Void, ResponseRest<ResponseList<List<Product>>>> {




        @Override
        protected ResponseRest<ResponseList<List<Product>>> doInBackground(String... params) {


            int lastServerCounter = productService.lastServerCounter("product");


            return syncToClientProy.list(lastServerCounter);
        }

        @Override
        protected void onPostExecute(ResponseRest<ResponseList<List<Product>>> response) {
            //TextView txt = (TextView) findViewById(R.id.output);
            //txt.setText("Executed"); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you


            if(response.status==0){

                SyncToClientDbTask syncToClientDbTask = new SyncToClientDbTask();
                syncToClientDbTask.execute(response);

            }else{

            }



        }

    }


    private class SyncToClientDbTask extends AsyncTask<ResponseRest<ResponseList<List<Product>>>, Void, String> {

        @Override
        protected String doInBackground(ResponseRest<ResponseList<List<Product>>>... params) {

            ResponseRest<ResponseList<List<Product>>> response = params[0];


            //DESCARGAR AL Cliente
            productService.syncFromServer(response);

            //productService.syncToServer();

            //productDao.serverCounterLastSyncUpdate("product",response.response.counterServer);

            return null;
        }
    }


    private class SyncClientToServer extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {

            productService.syncToServer();




            return null;
        }







    }




}
