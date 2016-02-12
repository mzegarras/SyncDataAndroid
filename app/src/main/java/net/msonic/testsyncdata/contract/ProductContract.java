package net.msonic.testsyncdata.contract;

import net.msonic.testsyncdata.to.Product;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by manuelzegarra on 12/02/16.
 */
public interface ProductContract {


    @POST("/products/syncToClient/{counterServer}")
    ResponseRest<ResponseList<List<Product>>> syncToClient(@Path("counterServer") int counterServer,@Body String data);


}
