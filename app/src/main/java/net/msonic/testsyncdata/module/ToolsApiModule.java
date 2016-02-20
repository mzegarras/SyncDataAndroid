package net.msonic.testsyncdata.module;

/**
 * Created by manuelzegarra on 19/02/16.
 */

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.UtilDB;
import net.msonic.testsyncdata.bus.ProductService;
import net.msonic.testsyncdata.dao.ProductDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ToolsApiModule {



    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(CustomApplication application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
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

}