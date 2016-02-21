package net.msonic.testsyncdata.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.UtilDB;
import net.msonic.testsyncdata.to.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

/**
 * Created by manuelzegarra on 12/02/16.
 */
public class ProductDao {


    @Inject
    UtilDB db;



    CustomApplication application;


    public ProductDao(CustomApplication application){
        this.application = application;
        this.application.getDiComponent().inject(this);
    }



    public int counter(String tableName){

        //UtilDB db = UtilDB.GetUtilDb(context);
        final String SQL = "SELECT counter FROM counter_data WHERE upper(tableName)=upper(?)";

        String[] parametros = new String[] { tableName};


        Cursor cursor = db.getDataBase().rawQuery(SQL, parametros);

        int counter=0;

        if(cursor.moveToNext()){
            counter = cursor.getInt(cursor.getColumnIndex("counter"));
        }

        cursor.close();


        return counter;

    }

    public void counterUpdate(String tableName, int counterServer){
        //UtilDB db = UtilDB.GetUtilDb(context);
        //String SQL = "UPDATE counter_sever SET value=? WHERE upper(tableName)=upper(?)";

        String SQL = "SELECT counter FROM counter_data WHERE upper(tableName)=upper(?)";



        Cursor cursor = db.getDataBase().rawQuery(SQL, new String[] { tableName});

        boolean existe=false;
        if(cursor.moveToNext()){
            existe = true;
        }

        if(existe){
            SQL = "UPDATE counter_data SET counter=? WHERE upper(tableName)=upper(?)";
        }else{
            SQL = "insert into counter_data (counter,tableName) values (?,upper(?))";
        }

        String[] parametros = new String[] { String.valueOf(counterServer),tableName};

        db.getDataBase().execSQL(SQL,parametros);

    }


    public int serverCounterLastSync(String tableName){

        //UtilDB db = UtilDB.GetUtilDb(context);
        final String SQL = "SELECT ifnull(serverCounterLastSync,0) as serverValue  FROM counter_data WHERE upper(tableName)=upper(?)";

        String[] parametros = new String[] { tableName};


        Cursor cursor = db.getDataBase().rawQuery(SQL, parametros);

        int counter=0;

        if(cursor.moveToNext()){
            counter = cursor.getInt(cursor.getColumnIndex("serverCounterLastSync"));
        }

        cursor.close();


        return counter;

    }



    public void serverCounterLastSyncUpdate(String tableName, int counterServer){
        //UtilDB db = UtilDB.GetUtilDb(context);
        //String SQL = "UPDATE counter_sever SET value=? WHERE upper(tableName)=upper(?)";

        String SQL = "SELECT serverCounterLastSync FROM counter_data WHERE upper(tableName)=upper(?)";



        Cursor cursor = db.getDataBase().rawQuery(SQL, new String[] { tableName});

        boolean existe=false;
        if(cursor.moveToNext()){
            existe = true;
        }

        if(existe){
            SQL = "UPDATE counter_data SET serverCounterLastSync=? WHERE upper(tableName)=upper(?)";
        }else{
            SQL = "insert into counter_data (serverCounterLastSync,tableName) values (?,upper(?))";
        }

        String[] parametros = new String[] { String.valueOf(counterServer),tableName};

        db.getDataBase().execSQL(SQL,parametros);

    }

    public int counterLastSync(String tableName){

        //UtilDB db = UtilDB.GetUtilDb(context);
        final String SQL = "SELECT counterLastSync FROM counter_data WHERE upper(tableName)=upper(?)";

        String[] parametros = new String[] { tableName};


        Cursor cursor = db.getDataBase().rawQuery(SQL, parametros);

        int counter=0;

        if(cursor.moveToNext()){
            counter = cursor.getInt(cursor.getColumnIndex("counterLastSync"));
        }

        cursor.close();


        return counter;

    }

    public void counterLastSyncUpdate(String tableName, int counterLocal){
        //UtilDB db = UtilDB.GetUtilDb(context);
        //String SQL = "UPDATE counter_sever SET value=? WHERE upper(tableName)=upper(?)";

        String SQL = "SELECT counterLastSync FROM counter_data WHERE upper(tableName)=upper(?)";



        Cursor cursor = db.getDataBase().rawQuery(SQL, new String[] { tableName});

        boolean existe=false;
        if(cursor.moveToNext()){
            existe = true;
        }

        if(existe){
            SQL = "UPDATE counter_data SET counterLastSync=? WHERE upper(tableName)=upper(?)";
        }else{
            SQL = "insert counter_data into counter_data (counterLastSync,tableName) values (?,upper(?))";
        }

        String[] parametros = new String[] { tableName,String.valueOf(counterLocal)};
        db.getDataBase().execSQL(SQL,parametros);

    }

    public void insertFromServer(Product product){

        final String SQL = "INSERT INTO product(id,code,name,counterLastUpdate,isDeleted,timeStampUpdated) VALUES (?,?,?,?,?,?)";


        final SQLiteStatement statementInsert = db.getDataBase().compileStatement(SQL);
        statementInsert.clearBindings();
        int index=0;

        statementInsert.bindString(++index, product.id);
        statementInsert.bindString(++index, product.code);
        statementInsert.bindString(++index, product.name);
        statementInsert.bindLong(++index, product.counterUpdate);
        statementInsert.bindLong(++index, product.deleted);
        statementInsert.bindLong(++index, product.timeStampUpdated);

        statementInsert.executeUpdateDelete();


    }


    public void updateFromServer(Product product){

        final String SQL = "UPDATE product SET name=?,counterLastUpdate=?,isDeleted=?,timeStampUpdated=? WHERE ID=?";

        final SQLiteStatement statementInsert = db.getDataBase().compileStatement(SQL);
        statementInsert.clearBindings();
        int index=0;

        statementInsert.bindString(++index, product.name);
        statementInsert.bindLong(++index, product.counterUpdate);
        statementInsert.bindLong(++index, product.deleted);
        statementInsert.bindLong(++index, product.timeStampUpdated);
        statementInsert.bindString(++index, product.id);
        statementInsert.executeUpdateDelete();

    }

    public Product byId(String id){

        Product product = null;

        final String SQL = "SELECT id,code,name,counterLastUpdate,isDeleted,timeStampUpdated FROM product WHERE ID=?";
        Cursor cursor = db.getDataBase().rawQuery(SQL,new String[]{id});
        if(cursor.moveToNext()) {
            product = new Product();

            product.id = cursor.getString(cursor.getColumnIndex("id"));
            product.code = cursor.getString(cursor.getColumnIndex("code"));
            product.name = cursor.getString(cursor.getColumnIndex("name"));
            product.counterUpdate = cursor.getInt(cursor.getColumnIndex("counterLastUpdate"));
            product.deleted = cursor.getInt(cursor.getColumnIndex("isDeleted"));
            product.timeStampUpdated = cursor.getLong(cursor.getColumnIndex("timeStampUpdated"));

        }
        cursor.close();
        return product;

    }


    public List<Product> list(int counterLastSync){

        List<Product> productos = new ArrayList<Product>();

        Product product = null;

        // object changed on client since last sync to server ?
        final String SQL = "SELECT id,code,name,counterLastUpdate,isDeleted,timeStampUpdated FROM product WHERE counterLastUpdate>?";
        Cursor cursor = db.getDataBase().rawQuery(SQL,new String[]{String.valueOf(counterLastSync)});


        while (cursor.moveToNext()) {
            product = new Product();

            product.id = cursor.getString(cursor.getColumnIndex("id"));
            product.code = cursor.getString(cursor.getColumnIndex("code"));
            product.name = cursor.getString(cursor.getColumnIndex("name"));
            product.counterUpdate = cursor.getInt(cursor.getColumnIndex("counterLastUpdate"));
            product.deleted = cursor.getInt(cursor.getColumnIndex("isDeleted"));
            product.timeStampUpdated = cursor.getLong(cursor.getColumnIndex("timeStampUpdated"));

            productos.add(product);
        }

        cursor.close();

        return productos;

    }




    public void insertFromClient(Product product){



        if(product.id==null){
            UUID uuid = UUID.randomUUID();
            product.id = uuid.toString().replace("-","").toUpperCase();
        }

        product.timeStampUpdated = System.currentTimeMillis()/1000L;


        final String SQL = "INSERT INTO product(id,code,name,counterLastUpdate,isDeleted,timeStampUpdated) VALUES (?,?,?,?,?,?)";

        final SQLiteStatement statementInsert = db.getDataBase().compileStatement(SQL);
        statementInsert.clearBindings();
        int index=0;

        statementInsert.bindString(++index, product.id);
        statementInsert.bindString(++index, product.code);
        statementInsert.bindString(++index, product.name);
        statementInsert.bindLong(++index, product.counterUpdate);
        statementInsert.bindLong(++index, product.deleted);
        statementInsert.bindLong(++index, product.timeStampUpdated);

        statementInsert.executeUpdateDelete();


    }


    public void updateFromClient(Product product){

        product.timeStampUpdated = System.currentTimeMillis()/1000L;

        final String SQL = "UPDATE product SET name=?,counterLastUpdate=?,isDeleted=?,timeStampUpdated=? WHERE ID=?";

        final SQLiteStatement statementInsert = db.getDataBase().compileStatement(SQL);
        statementInsert.clearBindings();
        int index=0;

        statementInsert.bindString(++index, product.name);
        statementInsert.bindLong(++index, product.counterUpdate);
        statementInsert.bindLong(++index, product.deleted);
        statementInsert.bindLong(++index, product.timeStampUpdated);
        statementInsert.bindString(++index, product.id);
        statementInsert.executeUpdateDelete();

    }


    public void deleteFromClient(Product product){
        final String SQL = "UPDATE product SET isDeleted=1 WHERE ID=?";

        final SQLiteStatement statementInsert = db.getDataBase().compileStatement(SQL);
        statementInsert.clearBindings();
        int index=0;

        statementInsert.bindString(++index, product.id);
        statementInsert.executeUpdateDelete();

    }
}
