package co.droidmesa.jsilval.catalogo.offlinemode.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by jsilval on 29/01/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    //Datos de la base de datos
    private static final String DB_NAME = "topfreeapp.db";

    //Número de versión del esquema
    private static final int DB_VERSION = 1;

    //ID para hacer referencia fácilmente a cada uno de los registros que van guardando en cada tabla
    public static final String C_ID = BaseColumns._ID;

    // Nombre de la tabla que se va a crear en la base de datos
    public static final String ENTRY_TABLE = "entry";

    public static final String C_NAME = "name";
    public static final String C_SUMMARY = "summary";
    public static final String C_PRICE = "price";
    public static final String C_TITLE = "title";
    public static final String C_IMAGE50 = "image50";
    public static final String C_IMAGE75 = "image75";
    public static final String C_IMAGE100 = "image100";
    // private static final String C_FKNAME = "category";

    //indices para acceder a cada columna dela tabla "entry"
    public static final int C_ID_INDEX = 0;
    public static final int C_NAME_INDEX = 1;
    public static final int C_SUMMARY_INDEX = 2;
    public static final int C_PRICE_INDEX = 3;
    public static final int C_TITLE_INDEX = 4;
    public static final int C_IMAGE50_INDEX = 5;
    public static final int C_IMAGE75_INDEX = 6;
    public static final int C_IMAGE100_INDEX = 7;
    public static final int C_CATEGORY_INDEX = 8;

    // Nombre de la tabla que se va a crear en la base de datos
    public static final String CATEGORY_TABLE = "category";

    public static final String C_CATEGORY = "category";

    //indices para acceder a cada columna dela tabla "category"

    //constructor de la clase
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creación de una tabla en la base de datos
        /*String  sql = "CREATE TABLE "+ CATEGORY_TABLE + " ("
                +C_ID + " integer, "
                +C_CATEGORY + " text primary key);";*/

       // db.execSQL(sql);

        String sql = "CREATE TABLE "+ ENTRY_TABLE + " ("
                +C_ID + " integer,"
                +C_NAME + " text primary key, "
                +C_SUMMARY + " text not null, "
                +C_PRICE + " text not null, "
                +C_TITLE + " text not null, "
                +C_IMAGE50 + " text not null, "
                +C_IMAGE75 + " text not null, "
                +C_IMAGE100 + " text not null, "
                +C_CATEGORY + " text not null); ";
    //            +" FOREIGN KEY ("+C_CATEGORY+") REFERENCES "+CATEGORY_TABLE+"("+C_CATEGORY+"));";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + CATEGORY_TABLE);
        db.execSQL("drop table if exists " + ENTRY_TABLE);
        onCreate(db); // ejecuta el método onCreate para crear la nueva base de datos.
    }
}
