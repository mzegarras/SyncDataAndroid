package net.msonic.testsyncdata.bus;

import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.dao.ProductDao;

import javax.inject.Inject;

/**
 * Created by manuelzegarra on 20/02/16.
 */
public class ProductService {

    CustomApplication application;

    @Inject ProductDao productDao;

    public ProductService(CustomApplication application){
        this.application = application;
        this.application.getDiComponent().inject(this);
    }

    public int lastServerCounter(String tableName){

        return productDao.lastServerCounter(tableName);
    }

}
