package net.msonic.testsyncdata.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import net.msonic.testsyncdata.CustomApplication;
import net.msonic.testsyncdata.UtilDB;
import net.msonic.testsyncdata.to.Product;

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




    public int lastServerCounter(String tableName){

        //UtilDB db = UtilDB.GetUtilDb(context);
        final String SQL = "SELECT serverValue FROM counter_sever WHERE upper(tableName)=upper(?)";

        String[] parametros = new String[] { tableName};


        Cursor cursor = db.getDataBase().rawQuery(SQL, parametros);

        int counter=0;

        if(cursor.moveToNext()){
            counter = cursor.getInt(cursor.getColumnIndex("serverValue"));
        }

        cursor.close();


        return counter;

    }

    public void updateCounterServer(String tableName, int counterServer){
        //UtilDB db = UtilDB.GetUtilDb(context);
        //String SQL = "UPDATE counter_sever SET value=? WHERE upper(tableName)=upper(?)";

        String SQL = "SELECT serverValue FROM counter_data WHERE upper(tableName)=upper(?)";
        String[] parametros = new String[] { tableName,String.valueOf(counterServer)};


        Cursor cursor = db.getDataBase().rawQuery(SQL, new String[] { tableName});

        boolean existe=false;
        if(cursor.moveToNext()){
            existe = true;
        }

        if(existe){
            SQL = "UPDATE counter_data SET serverValue=? WHERE upper(tableName)=upper(?)";
        }else{
            SQL = "insert into counter_data (tableName,serverValue) values (upper(?),?)";
        }

        db.getDataBase().execSQL(SQL,parametros);

    }

    public int lastLocalCounter(String tableName){

        //UtilDB db = UtilDB.GetUtilDb(context);
        final String SQL = "SELECT localValue FROM counter_sever WHERE upper(tableName)=upper(?)";

        String[] parametros = new String[] { tableName};


        Cursor cursor = db.getDataBase().rawQuery(SQL, parametros);

        int counter=0;

        if(cursor.moveToNext()){
            counter = cursor.getInt(cursor.getColumnIndex("localValue"));
        }

        cursor.close();


        return counter;

    }

    public void updateCounterLocal(String tableName, int counterLocal){
        //UtilDB db = UtilDB.GetUtilDb(context);
        //String SQL = "UPDATE counter_sever SET value=? WHERE upper(tableName)=upper(?)";

        String SQL = "SELECT localValue FROM counter_data WHERE upper(tableName)=upper(?)";
        String[] parametros = new String[] { tableName,String.valueOf(counterLocal)};


        Cursor cursor = db.getDataBase().rawQuery(SQL, new String[] { tableName});

        boolean existe=false;
        if(cursor.moveToNext()){
            existe = true;
        }

        if(existe){
            SQL = "UPDATE counter_data SET localValue=? WHERE upper(tableName)=upper(?)";
        }else{
            SQL = "insert into counter_data (tableName,localValue) values (upper(?),?)";
        }

        db.getDataBase().execSQL(SQL,parametros);

    }

    public void insert(Product product){

        final String SQL = "INSERT INTO product(id,code,name,counterFromServer,counterToServer,isDeleted,timeStampUpdated) VALUES (?,?,?,?,?,?,?)";


        final SQLiteStatement statementInsert = db.getDataBase().compileStatement(SQL);
        statementInsert.clearBindings();
        int index=0;

        statementInsert.bindString(++index, product.id);
        statementInsert.bindString(++index, product.code);
        statementInsert.bindString(++index, product.name);
        statementInsert.bindLong(++index, product.counterFromServer);
        statementInsert.bindLong(++index, product.counterToServer);
        statementInsert.bindLong(++index, product.deleted);
        statementInsert.bindLong(++index, product.timeStampUpdated);

        statementInsert.executeUpdateDelete();


    }

    public void update(Product product){

        final String SQL = "UPDATE product SET name=?,counterFromServer=?,counterToServer=?,isDeleted=?,timeStampUpdated=? WHERE ID=?";

        final SQLiteStatement statementInsert = db.getDataBase().compileStatement(SQL);
        statementInsert.clearBindings();
        int index=0;

        statementInsert.bindString(++index, product.name);
        statementInsert.bindLong(++index, product.counterFromServer);
        statementInsert.bindLong(++index, product.counterToServer);
        statementInsert.bindLong(++index, product.deleted);
        statementInsert.bindLong(++index, product.timeStampUpdated);
        statementInsert.bindString(++index, product.id);
        statementInsert.executeUpdateDelete();

    }

    public Product byId(String id){

        Product product = null;

        final String SQL = "SELECT id,code,name,counterFromServer,counterToServer,isDeleted,timeStampUpdated FROM product WHERE ID=?";
        Cursor cursor = db.getDataBase().rawQuery(SQL,new String[]{id});
        if(cursor.moveToNext()) {
            product = new Product();

            product.id = cursor.getString(cursor.getColumnIndex("id"));
            product.code = cursor.getString(cursor.getColumnIndex("code"));
            product.name = cursor.getString(cursor.getColumnIndex("name"));
            product.counterFromServer = cursor.getInt(cursor.getColumnIndex("counterFromServer"));
            product.counterToServer = cursor.getInt(cursor.getColumnIndex("counterToServer"));
            product.deleted = cursor.getInt(cursor.getColumnIndex("isDeleted"));
            product.timeStampUpdated = cursor.getLong(cursor.getColumnIndex("timeStampUpdated"));

        }
        cursor.close();
        return product;

    }

}
