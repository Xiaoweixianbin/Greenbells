package dreammerwei.com.greenbellweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/7/26.
 */
public class GreenWeatherOpenHelper extends SQLiteOpenHelper {
    /*
    * Province表建表语句
    * */
    public static final String CREATE_PROVINCE = "create table Province("
            +"id integer primary key autoincrement, "
            +"province_name text, "
            +"province_code text)";

    /*
    * City表建表语句
    * */
    public static final String CREATE_CITY = "create table City ("
            +"id integer primary key autoincrement, "
            +"city_name text, "
            +"city_code text, "
            +"province_id integer)";
    /*
    * Country表建表语句
    * */
    public static final String CREATE_COUNTRY = "create table Country ("
            +"id integer primary key autoincrement, "
            +"country_name text, "
            +"country_code text, "
            +"city_id integer)";

    public GreenWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_PROVINCE);        //创建Province表
        db.execSQL(CREATE_CITY);            //创建City表
        db.execSQL(CREATE_COUNTRY);         //创建Country表
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
