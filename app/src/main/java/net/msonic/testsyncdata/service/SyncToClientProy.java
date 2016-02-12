package net.msonic.testsyncdata.service;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;

import net.msonic.testsyncdata.contract.ProductContract;
import net.msonic.testsyncdata.contract.ResponseList;
import net.msonic.testsyncdata.contract.ResponseRest;
import net.msonic.testsyncdata.to.Product;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.JacksonConverter;

/**
 * Created by manuelzegarra on 12/02/16.
 */
public class SyncToClientProy {

    private static int TIMEOUT=90;
    private static String HOST_SERVICE="http://192.168.0.24:9002/api/v1/";

    private final Context context;


    public SyncToClientProy(Context context) {
        this.context = context;
    }


    public ResponseRest<ResponseList<List<Product>>> list(int counterServer){
        final ProductContract service = getRestAdapter().create(ProductContract.class);

        return service.syncToClient(counterServer,"");

    }

    public RestAdapter getRestAdapter(){

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
                request.addHeader("Content-type", "application/json");
            }
        };




        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS);


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(requestInterceptor)
                .setConverter(new JacksonConverter())
                .setEndpoint(HOST_SERVICE)
                .setClient(new OkClient(okHttpClient))
                .build();

        return restAdapter;
    }
}
