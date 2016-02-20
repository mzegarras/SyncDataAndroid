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
        int localCounter = productDao.lastLocalCounter("product");

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
                    if (productLocal.counterFromServer > lastServerCounter) {

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
                    } else { // no conflict: update object locally
                        productLocal.name = objectToSync.name;
                        productLocal.deleted = objectToSync.deleted;
                    }

                    productDao.update(productLocal);
                }

            }else{
                objectToSync.counterFromServer = localCounter; //do not increase $this->counter because no change that must be synced back to server
                productDao.insert(objectToSync);
            }
        }


        /*
        if (result.getStatuscode() == 1) {
            this.servercounter_lastsync = result.getServercounter();
        }*/
        productDao.updateCounterServer("product",response.response.counterServer);

        dbHelper.close();
    }
}
