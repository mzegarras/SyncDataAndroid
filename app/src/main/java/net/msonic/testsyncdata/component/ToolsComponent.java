package net.msonic.testsyncdata.component;

/**
 * Created by manuelzegarra on 19/02/16.
 */


import net.msonic.testsyncdata.MainActivity;
import net.msonic.testsyncdata.bus.ProductService;
import net.msonic.testsyncdata.dao.ProductDao;
import net.msonic.testsyncdata.module.ApplicationModule;
import net.msonic.testsyncdata.module.ToolsApiModule;
import net.msonic.testsyncdata.robospice.request.DemoRequest;
import net.msonic.testsyncdata.service.SyncFromClienteProxy;
import net.msonic.testsyncdata.service.SyncToClientProy;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class,ToolsApiModule.class})
public interface ToolsComponent {



    void inject(MainActivity activity);

    void inject(ProductService productService);
    void inject(ProductDao productDao);

    void inject(SyncToClientProy syncToClientProy);
    void inject(SyncFromClienteProxy syncFromClienteProxy);



    void inject(DemoRequest demoRequest);


}
