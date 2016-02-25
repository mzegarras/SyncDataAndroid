package net.msonic.testsyncdata;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.CachedSpiceRequest;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.SpiceServiceAdapter;
import com.octo.android.robospice.request.listener.SpiceServiceListener;
import com.squareup.otto.Subscribe;

import net.msonic.testsyncdata.bus.ProductService;
import net.msonic.testsyncdata.contract.ResponseList;
import net.msonic.testsyncdata.contract.ResponseRest;
import net.msonic.testsyncdata.dao.ProductDao;
import net.msonic.testsyncdata.notification.BusProvider;
import net.msonic.testsyncdata.notification.IntentServiceResult;
import net.msonic.testsyncdata.robospice.BaseSpiceActivity;
import net.msonic.testsyncdata.robospice.request.DemoRequest;
import net.msonic.testsyncdata.service.SyncFromClienteProxy;
import net.msonic.testsyncdata.service.SyncToClientProy;
import net.msonic.testsyncdata.to.Process;
import net.msonic.testsyncdata.to.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;

public class MainActivity extends BaseSpiceActivity {

    @Inject
    ProductDao productDao;

    @Inject
    ProductService productService;

    @Inject
    BusProvider busProvider;

    RecyclerView rv;

    private Toolbar toolbar;
    private ProgressBar progress_spinner;
    private List<Process> procesos = new ArrayList<Process>();
    Adapter adapter;

    @Inject
    DemoRequest  demoRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((CustomApplication) getApplication()).getDiComponent().inject(this);

        //Añado la toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progress_spinner = (ProgressBar) findViewById(R.id.progress_spinner);
        rv = (RecyclerView)findViewById(R.id.rv);
        setSupportActionBar(toolbar);
        // 2. set layoutManger
        rv.setLayoutManager(new LinearLayoutManager(this));

        if(latch!=null && latch.getCount()==0)
            progress_spinner.setVisibility(View.INVISIBLE);

        procesos = new ArrayList<Process>();

        Process p1 = new Process();
        demoRequest.process=p1;
        p1.descripcion="Descarga";
        procesos.add(p1);

        adapter = new Adapter(procesos);


        rv.setAdapter(adapter);
    }


    @Override
    protected void onPause() {
        super.onPause();
        busProvider.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        busProvider.register(this);
    }


    public void btnAceptar(View view){
        SyncToClientTask task = new SyncToClientTask();
        task.execute("product");
    }


    @Subscribe
    public void doThis(IntentServiceResult intentServiceResult) {
        adapter.notifyDataSetChanged();
        Toast.makeText(this, intentServiceResult.getResultValue(), Toast.LENGTH_SHORT).show();
    }



    CountDownLatch latch;

    RequestListener<String> listener = new RequestListener<String>() {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.d(MainActivity.class.getCanonicalName(),String.valueOf(getSpiceManager().getPendingRequestCount()));

          //  latch.countDown();

          //  if(latch.getCount()==0)
            //    progress_spinner.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onRequestSuccess(String s) {
            //setProgressBarIndeterminateVisibility(false);
            Log.d(MainActivity.class.getCanonicalName(),String.valueOf(getSpiceManager().getPendingRequestCount()));

           // latch.countDown();



           // if(latch.getCount()==0)
             //   progress_spinner.setVisibility(View.INVISIBLE);
        }
    };

    public void btnToServer(View view){

        //latch = new CountDownLatch(5);
        //Make progress bar appear when you need it
        progress_spinner.setVisibility(View.VISIBLE);

        //getSpiceManager().execute(demoRequest,listener);

        // Create the account type and default account
        Account newAccount = new Account("dummyaccount", "com.sportsteamkarma");
        AccountManager accountManager = (AccountManager) this.getSystemService(ACCOUNT_SERVICE);
        // If the account already exists no harm is done but
        // a warning will be logged.
        accountManager.addAccountExplicitly(newAccount, null, null);

        ContentResolver.setSyncAutomatically(newAccount, "com.sportsteamkarma.provider", true);




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



    public class Adapter extends RecyclerView.Adapter<ResumenViewHolder> {

        private final List<Process> detalle;

        public Adapter(List<Process> detalle) {
            this.detalle = detalle;
        }

        @Override
        public ResumenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_content, parent, false);
            ResumenViewHolder pvh = new ResumenViewHolder(v);

            return pvh;

        }

        @Override
        public void onBindViewHolder(ResumenViewHolder holder, int position) {
            Process item = detalle.get(position);

            holder.txtDescripcion.setText(item.descripcion);

        }

        @Override
        public int getItemCount() {
            if (detalle == null)
                return 0;
            else {
                return detalle.size();
            }
        }


    }

    public class ResumenViewHolder extends RecyclerView.ViewHolder {
        TextView txtDescripcion;
        ResumenViewHolder(View itemView) {
            super(itemView);
            txtDescripcion = (TextView) itemView.findViewById(R.id.txtDescripcion);
        }
    }


}
