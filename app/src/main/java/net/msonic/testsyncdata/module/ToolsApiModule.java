package net.msonic.testsyncdata.module;

/**
 * Created by manuelzegarra on 19/02/16.
 */

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.UtilDB;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ToolsApiModule {



    @Provides
    @Singleton
        // Application reference must come from AppModule.class
    SharedPreferences providesSharedPreferences(CustomApplication application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
        // Application reference must come from AppModule.class
    UtilDB providesDatabase(CustomApplication application) {
        UtilDB databaseHelper = UtilDB.GetUtilDb(application);
        return databaseHelper;
    }

}