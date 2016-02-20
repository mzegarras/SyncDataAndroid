package net.msonic.testsyncdata.component;

/**
 * Created by manuelzegarra on 19/02/16.
 */


import net.msonic.testsyncdata.module.ApplicationModule;
import net.msonic.testsyncdata.module.ToolsApiModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class,ToolsApiModule.class})
public interface ToolsComponent {
    // to update the fields in your activities
    //void inject(MainActivity activity);
    //void inject(MainActivityFragment activity);
}
