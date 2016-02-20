package net.msonic.testsyncdata.module;

/**
 * Created by manuelzegarra on 19/02/16.
 */

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.okhttp.OkHttpClient;

import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.UtilDB;
import net.msonic.testsyncdata.bus.ProductService;
import net.msonic.testsyncdata.dao.ProductDao;
import net.msonic.testsyncdata.service.SyncToClientProy;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.JacksonConverter;

@Module
public class ToolsApiModule {


    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(CustomApplication application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }




    @Provides
    @Singleton
    JacksonConverter provideJsonConverter() {
        JacksonConverter jacksonConverter = new JacksonConverter();
        return jacksonConverter;
    }

    @Provides
    @Singleton
    RequestInterceptor requestInterceptor() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
                request.addHeader("Content-type", "application/json");
            }
        };

        return requestInterceptor;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        return okHttpClient;
    }

    @Provides
    @Singleton
    @Named("server1")
    RestAdapter provideRetrofit1(RequestInterceptor requestInterceptor,JacksonConverter provideJsonConverter, OkHttpClient okHttpClient) {


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(requestInterceptor)
                .setConverter(provideJsonConverter)
                .setEndpoint("http://192.168.0.24:9002/api/v1/")
                .setClient(new OkClient(okHttpClient))
                .build();

        return restAdapter;
    }

    @Provides
    @Singleton
    @Named("server2")
    RestAdapter provideRetrofit2(RequestInterceptor requestInterceptor,JacksonConverter provideJsonConverter, OkHttpClient okHttpClient) {


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(requestInterceptor)
                .setConverter(provideJsonConverter)
                .setEndpoint("http://192.168.0.24:9002/api/v2/")
                .setClient(new OkClient(okHttpClient))
                .build();

        return restAdapter;
    }







    @Provides
    @Singleton
    UtilDB providesDatabase(CustomApplication application) {
        UtilDB databaseHelper = UtilDB.GetUtilDb(application);
        return databaseHelper;
    }

    @Provides
    @Singleton
    ProductDao productDao(CustomApplication application) {
        ProductDao productDao = new ProductDao(application);
        return productDao;
    }


    @Provides
    @Singleton
    ProductService productService(CustomApplication application) {
        ProductService productService = new ProductService(application);
        return productService;
    }



    @Provides
    @Singleton
    SyncToClientProy syncToClientProy(CustomApplication application) {
        SyncToClientProy syncToClientProy = new SyncToClientProy(application);
        return syncToClientProy;
    }

}