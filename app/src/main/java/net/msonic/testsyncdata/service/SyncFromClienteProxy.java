package net.msonic.testsyncdata.service;

import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.contract.ProductContract;
import net.msonic.testsyncdata.contract.ResponseList;
import net.msonic.testsyncdata.to.Product;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit.RestAdapter;

/**
 * Created by manuelzegarra on 21/02/16.
 */
public class SyncFromClienteProxy {




    public SyncFromClienteProxy(CustomApplication application) {
        application.getDiComponent().inject(this);
    }

    @Inject
    @Named("server1")
    RestAdapter restAdapter;

    public ResponseList<Integer> list(List<Product> products){
        final ProductContract service = restAdapter.create(ProductContract.class);

        return service.syncFromClient(products);

    }

}
