package net.msonic.testsyncdata;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.msonic.testsyncdata.bus.ProductService;
import net.msonic.testsyncdata.contract.ResponseList;
import net.msonic.testsyncdata.contract.ResponseRest;
import net.msonic.testsyncdata.dao.ProductDao;
import net.msonic.testsyncdata.service.SyncToClientProy;
import net.msonic.testsyncdata.to.Product;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

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

    public void btnToServer(View view){

        SyncClientToServer task = new SyncClientToServer();
        task.execute();
    }




    public void btnAdd(View view){
        Product p = new Product();
        p.name="Product2";
        p.code="00002";

        productService.insertFromClient(p);

    }

    @Inject SyncToClientProy loginProxy;

    private class SyncToClientTask extends AsyncTask<String, Void, ResponseRest<ResponseList<List<Product>>>> {




        @Override
        protected ResponseRest<ResponseList<List<Product>>> doInBackground(String... params) {


            int lastServerCounter = productService.lastServerCounter("product");


            return loginProxy.list(lastServerCounter);
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
            productService.syncFromServer(response.response.counterServer,response);

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
