package net.msonic.testsyncdata.service;

import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.contract.ProductContract;
import net.msonic.testsyncdata.contract.ResponseList;
import net.msonic.testsyncdata.contract.ResponseRest;
import net.msonic.testsyncdata.to.Product;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit.RestAdapter;

/**
 * Created by manuelzegarra on 12/02/16.
 */
public class SyncToClientProy {


    public SyncToClientProy(CustomApplication application) {
        application.getDiComponent().inject(this);
    }

    @Inject @Named("server1") RestAdapter restAdapter;

    public ResponseRest<ResponseList<List<Product>>> list(int lastServerCounter){
        final ProductContract service = restAdapter.create(ProductContract.class);

        return service.syncToClient(lastServerCounter,"");

    }


}
