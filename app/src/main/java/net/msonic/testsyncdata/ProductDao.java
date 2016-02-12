package net.msonic.testsyncdata;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by manuelzegarra on 12/02/16.
 */
public class ProductDao {

    public Context context;

    public int lastServerCounter(String tableName){

        UtilDB db = UtilDB.GetUtilDb(context);
        String SQL = "SELECT value FROM counter_sever WHERE upper(tableName)=upper(?)";

        String[] parametros = new String[] { tableName};


        db.openDataBase();
        Cursor cursor = db.getDataBase().rawQuery(SQL, parametros);

        int counter=0;

        if(cursor.moveToNext()){
            counter = cursor.getInt(cursor.getColumnIndex("value"));
        }

        cursor.close();
        db.close();

        return counter;

    }

    public void updateCounter(String tableName,int counterServer){
        UtilDB db = UtilDB.GetUtilDb(context);
        //String SQL = "UPDATE counter_sever SET value=? WHERE upper(tableName)=upper(?)";

        String SQL = "SELECT value FROM counter_sever WHERE upper(tableName)=upper(?)";
        String[] parametros = new String[] { tableName,String.valueOf(counterServer)};


        db.openDataBase();
        Cursor cursor = db.getDataBase().rawQuery(SQL, new String[] { tableName});

        boolean existe=false;
        if(cursor.moveToNext()){
            existe = true;
        }

        if(existe){
            SQL = "UPDATE counter_sever SET value=? WHERE upper(tableName)=upper(?)";
        }else{
            SQL = "insert into counter_sever (tableName,value) values (upper(?),?)";
        }

        db.getDataBase().execSQL(SQL,parametros);

    }

}
