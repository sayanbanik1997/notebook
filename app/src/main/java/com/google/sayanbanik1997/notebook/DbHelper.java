package com.google.sayanbanik1997.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {

    Context context;

    String[] tblNameArr = new String[]{
        "cataTbl", "contTbl"
    };

    String[][] tblColNameArr = new String[][]{
            {"id", "name"},
            {"id", "cont", "cataId"}
    };
    public DbHelper(Context context){
        super(context, "NoteBookDb",null, 1);
        this .context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table cataTbl " +
                "(id integer primary key autoincrement not null, " +
                "name Text" +
                ")");
        db.execSQL("create table contTbl " +
                "(id integer primary key autoincrement not null, " +
                "cont Text not null," +
                "cataId int" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS workToDoTbl");
        onCreate(db);
    }

    long insrt(String tblName, String[] values){
        SQLiteDatabase sld = getWritableDatabase();

        int tblInd = getTblInd(tblName);
        ContentValues contentValues = new ContentValues();
        for(int i=0; i<values.length; i++){
            if(values[i]!= null){
                contentValues.put(tblColNameArr[tblInd][i+1], values[i]);
            }
        }
        long insertedRowId = sld.insert(tblName, null, contentValues);
        sld.close();
        return insertedRowId;
    }

    protected ArrayList rawQry(String qry, String tblName){
        SQLiteDatabase sld = getReadableDatabase();
        Cursor cursor = sld.rawQuery(qry, null);
        ArrayList<HashMap<String , String>> rsltAl = new ArrayList<>();

        int tblInd = getTblInd(tblName), count=0;
        if(cursor.moveToFirst()){
            do{
                rsltAl.add(new HashMap());
                for(int i=0; i<tblColNameArr[tblInd].length; i++) {
                    rsltAl.get(count).put(tblColNameArr[tblInd][i], cursor.getString(i));
                }
                count++;
            }while (cursor.moveToNext());
        }
        sld.close();
        return rsltAl;
    }

    protected long del(String tblName, String whereClause, String[] parameter){
        SQLiteDatabase sld = getWritableDatabase();
        long delRowNo = sld.delete(tblName, whereClause, parameter);
        sld.close();
        return delRowNo;
    }

    long upd(String tblName, String[] valueArr, String whereClause, String[] parameter){
        SQLiteDatabase sld = getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        int tblId = getTblInd(tblName);
        for(int i=0; i<valueArr.length; i++){
            if(valueArr[i]!=null) {
                contentValues.put(tblColNameArr[tblId][i + 1], valueArr[i]);
            }
        }
        long delRowNo = sld.update(tblName, contentValues, whereClause, parameter);
        sld.close();
        return delRowNo;
    }
    int getTblInd(String tblName){
        int tblInd = 0;
        for(int i=0; i<tblNameArr.length; i++){
            if(tblNameArr[i].equals(tblName)){
                tblInd=i;
                break;
            }
        }
        return tblInd;
    }
}
