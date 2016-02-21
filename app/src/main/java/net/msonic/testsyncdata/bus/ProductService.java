package net.msonic.testsyncdata.bus;

import android.util.Log;

import net.msonic.testsyncdata.Common;
import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.UtilDB;
import net.msonic.testsyncdata.contract.ResponseList;
import net.msonic.testsyncdata.contract.ResponseRest;
import net.msonic.testsyncdata.dao.ProductDao;
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
            counter = productDao.lastServerCounter(tableName);
        } catch (Exception ex){
            Log.e(TAG, "lastServerCounter", ex);
        } finally {
            dbHelper.close();
        }

        return counter;
    }


    public void syncToServer() {
        dbHelper.openDataBase();

        int localCounter = productDao.lastLocalCounter("product");

        productDao.list(localCounter);

        dbHelper.close();

    }


    public void syncFromServer(int lastServerCounter,ResponseRest<ResponseList<List<Product>>> response){


        dbHelper.openDataBase();
        int counter_lastsync = productDao.lastLocalCounter("product");
        int updateLocalCounter = productDao.lastUpdateCounter("product");

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
                objectToSync.counterUpdate = updateLocalCounter; //do not increase $this->counter because no change that must be synced back to server
                productDao.insertFromServer(objectToSync);
            }
        }


        /*
        if (result.getStatuscode() == 1) {
            this.servercounter_lastsync = result.getServercounter();
        }*/
        productDao.updateCounterServer("product",response.response.counterServer);

        dbHelper.close();
    }



    public void debugOutput(String text, List<Product> objects) {
        System.out.println(text);
        if(objects==null || objects.size()<=0){
            System.out.println("None");
        }else{
            System.out.println(objects.size());
        }

        for (Product object : objects) {
            object.toString();
        }
    }

    public void display() {
        System.out.println("----------------------------------------");

        int localCounter = productDao.lastLocalCounter("product");
        int serverCounter = productDao.lastServerCounter("product");

        Log.d(TAG, String.format("State of client: name: %s - counter: %s - counter last sync: %s - - Server counter last sync:%s",
                "Android",
                0,
                localCounter,
                serverCounter));


        //debugOutput("Objects on client:", this.records);

    }



}
