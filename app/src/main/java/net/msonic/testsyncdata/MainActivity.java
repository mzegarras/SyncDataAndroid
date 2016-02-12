package net.msonic.testsyncdata;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.msonic.testsyncdata.contract.ResponseList;
import net.msonic.testsyncdata.contract.ResponseRest;
import net.msonic.testsyncdata.service.SyncToClientProy;
import net.msonic.testsyncdata.to.Product;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void btnAceptar(View view){

        SyncToClientTask task = new SyncToClientTask();
        task.execute("product");
    }

    private class SyncToClientTask extends AsyncTask<String, Void, ResponseRest<ResponseList<List<Product>>>> {


        @Override
        protected ResponseRest<ResponseList<List<Product>>> doInBackground(String... params) {
            SyncToClientProy loginProxy = new SyncToClientProy(MainActivity.this);


            ProductDao productDao = new ProductDao();
            productDao.context=MainActivity.this;
            int value = productDao.lastServerCounter("product");


            return loginProxy.list(value);
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

            ProductDao productDao = new ProductDao();
            productDao.context=MainActivity.this;

            productDao.updateCounter("product",response.response.counterServer);

            return null;
        }
    }



    }
