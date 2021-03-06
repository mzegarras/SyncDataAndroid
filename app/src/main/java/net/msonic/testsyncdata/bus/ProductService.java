package net.msonic.testsyncdata.bus;

import android.util.Log;

import net.msonic.testsyncdata.Common;
import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.UtilDB;
import net.msonic.testsyncdata.contract.ResponseList;
import net.msonic.testsyncdata.contract.ResponseRest;
import net.msonic.testsyncdata.dao.ProductDao;
import net.msonic.testsyncdata.service.SyncFromClienteProxy;
import net.msonic.testsyncdata.to.Product;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by manuelzegarra on 20/02/16.
 */
public class ProductService {

    private static String TAG = ProductService.class.getCanonicalName();
    private int conflictHandling;

    CustomApplication application;

    @Inject ProductDao productDao;
    @Inject SyncFromClienteProxy syncFromClienteProxy;

    @Inject
    UtilDB dbHelper;

    public ProductService(CustomApplication application){
        this.application = application;
        this.application.getDiComponent().inject(this);
    }

    public int lastServerCounter(String tableName){


        int counter = -1;
        try{
            dbHelper.openDataBase();
            counter = productDao.serverCounterLastSync(tableName);
        } catch (Exception ex){
            Log.e(TAG, "serverCounterLastSync", ex);
        } finally {
            dbHelper.close();
        }

        return counter;
    }



    public void doSync() {
        // first sync from server to client, then from client to server,
        // because only client can handle conflicts (server cannot handle
        // conflicts because server does not know state of client)
        //this.syncFromServer();
        this.syncToServer();
    }

    public void doFullSync() {
        //this.counter_lastsync = 0; // force full sync to server
        //this.servercounter_lastsync = 0; // force full sync from server
        this.doSync();
    }

    public void syncToServer() {

        boolean raiseError=false;

        int counterLastSync = -1;
        int counter = -1;
        List<Product> products = null;

        try{

            dbHelper.openDataBase();

            counterLastSync = productDao.counterLastSync("product");
            counter =productDao.counter("product");
            products = productDao.list(counterLastSync);

        }catch(Exception ex){
            dbHelper.close();
            Log.e(TAG,"Error",ex);
            raiseError = true;
        }


        Log.d(TAG,"Invocar al rest service");

        if(raiseError) return;

        try{
            ResponseRest<ResponseList<Integer>> response = syncFromClienteProxy.sync(products);

            dbHelper.beginTransaction();

            if(response.status==0){
                if(response.response.result==Common.Status.OK.getValue()){
                    productDao.counterLastSyncUpdate("product",counter);
                    productDao.serverCounterLastSyncUpdate("product",response.response.counterServer);
                }
            }

            dbHelper.setTransactionSuccessful();
        }catch(Exception ex){
            Log.e(TAG,"Error",ex);
        } finally {
            dbHelper.endTransaction();
            dbHelper.close();
        }


    }


    public void syncFromServer(ResponseRest<ResponseList<List<Product>>> response){

        dbHelper.openDataBase();
        int counter_lastsync = productDao.counterLastSync("product");
        int counter = productDao.counter("product");

        for (Product objectToSync:response.response.result){


            Product productLocal = productDao.byId(objectToSync.id);

            if(productLocal!=null){

                if (productLocal.id.compareTo(objectToSync.id) == 0
                        || productLocal.id.compareTo(objectToSync.code) == 0) {

                    // handle PK conflict
                    if (productLocal.code.compareToIgnoreCase(objectToSync.code) == 0) {
                        productLocal.id=objectToSync.id; // merge objects
                    }

                    // check for conflict (object updated locally since last sync to server)
                    if (productLocal.counterUpdate > counter_lastsync) {

                        if (conflictHandling == Common.ConflictHandling.SERVERPRIORITY.getValue()) {
                            productLocal.name = objectToSync.name;
                            productLocal.deleted = objectToSync.deleted;
                        }else if (conflictHandling == Common.ConflictHandling.CLIENTPRIORITY.getValue()) {
                            // no change to local object
                        } else if (conflictHandling == Common.ConflictHandling.TIMESTAMPPRIORITY.getValue()) {

                            if (productLocal.timeStampUpdated > objectToSync.timeStampUpdated) {
                                productLocal.name = objectToSync.name;
                                productLocal.deleted = objectToSync.deleted;
                                productLocal.timeStampUpdated = System.currentTimeMillis() / 1000L;
							}

                        }
                    } else { // no conflict: updateFromServer object locally
                        productLocal.name = objectToSync.name;
                        productLocal.deleted = objectToSync.deleted;
                    }

                    productDao.updateFromServer(productLocal);
                }

            }else{
                objectToSync.counterUpdate = counter; //do not increase $this->counter because no change that must be synced back to server
                productDao.insertFromServer(objectToSync);
            }
        }


        /*
        if (result.getStatuscode() == 1) {
            this.servercounter_lastsync = result.getServercounter();
        }*/
        productDao.serverCounterLastSyncUpdate("product",response.response.counterServer);

        dbHelper.close();
    }

    public void insertFromClient(Product product){
        dbHelper.openDataBase();
        int updateLocalCounter = productDao.counter("product");
        updateLocalCounter +=1;
        product.counterUpdate = updateLocalCounter;
        productDao.insertFromClient(product);

        productDao.counterUpdate("product",updateLocalCounter);
        dbHelper.close();
    }


    public void updateFromClient(Product product){
        dbHelper.openDataBase();
        int updateLocalCounter = productDao.counter("product");
        updateLocalCounter +=1;
        product.counterUpdate = updateLocalCounter;

        productDao.updateFromClient(product);
        productDao.counterUpdate("product",updateLocalCounter);
        dbHelper.close();
    }

    public void deleteFromClient(Product product){
        dbHelper.openDataBase();
        int updateLocalCounter = productDao.counter("product");
        updateLocalCounter +=1;
        product.counterUpdate = updateLocalCounter;
        productDao.deleteFromClient(product);
        productDao.counterUpdate("product",updateLocalCounter);
        dbHelper.close();
    }


}
