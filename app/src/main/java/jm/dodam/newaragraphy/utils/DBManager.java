package jm.dodam.newaragraphy.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Bong on 2016-08-19.
 */
public class DBManager extends SQLiteOpenHelper {
    private ArrayList<String> imageUris = new ArrayList<>();
    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IMAGES( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insert(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void update(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void delete(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public String PrintData() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery("select * from IMAGES", null);
        while(cursor.moveToNext()) {
            str += cursor.getInt(0)
                    + " : image "
                    + cursor.getString(1)
                    + "\n";
            imageUris.add(cursor.getString(1));
        }

        return str;
    }
    public ArrayList<String> getImageList(){

        SQLiteDatabase db = getReadableDatabase();
        int cnt = 0;

        Cursor cursor = db.rawQuery("select * from IMAGES", null);
        while(cursor.moveToNext()){
            imageUris.add(cursor.getString(1));
            cnt++;
        }
        return imageUris;
    }

}
