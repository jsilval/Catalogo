package co.droidmesa.jsilval.catalogo.offlinemode.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import co.droidmesa.jsilval.catalogo.models.Attributes_;
import co.droidmesa.jsilval.catalogo.models.Attributes______;
import co.droidmesa.jsilval.catalogo.models.Category;
import co.droidmesa.jsilval.catalogo.models.Entry;
import co.droidmesa.jsilval.catalogo.models.ImContentType;
import co.droidmesa.jsilval.catalogo.models.ImImage;
import co.droidmesa.jsilval.catalogo.models.ImName;
import co.droidmesa.jsilval.catalogo.models.ImPrice;
import co.droidmesa.jsilval.catalogo.models.Summary;
import co.droidmesa.jsilval.catalogo.models.Title;

/**
 * Created by jsilval on 29/01/17.
 * Metodos que realizan operaciones sobre la base de datos que alamcena el cache.
 */

public class DBOperations {
    private DBHelper dbHelper;

    // constructor de la clase, obtiene una nueva instancia de la base de datos.
    public DBOperations(Context context){
        dbHelper = new DBHelper(context);
    }

    // Inseratar datos en una tabla
    public void insertEntry(ContentValues values, String table) {
        //traer referencia a la base de datos actual con permisos de escritura (el dbHelper conoce la estructura de la bd)
        SQLiteDatabase dataBase = dbHelper.getWritableDatabase();
        try{
            // ignora duplicados
            dataBase.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }finally{
            dataBase.close();
        }
    }

    /**
     * Hacer una consulta la tabla que almacena el cache y traerse los resultado, para luego
     * ir creando objetos Entry, que son los que alamcenan la lista de aplicaciones disponibles
     * @return lista de aplicaciones disponibles
     */
    public List<Entry> getEntryFromCache(){
        List<Entry> list_entry = new ArrayList<>();

        // Traer referencia a la bd actual con permisos de lectura
        SQLiteDatabase dataBase = dbHelper.getReadableDatabase();

        // Se obtienen todos los elementos de la tabla
        Cursor cursor = dataBase.query(DBHelper.ENTRY_TABLE, null, null, null, null, null, null);

        // Se ubica al principio del cursor y empieza la iteración sobre el cursor
        int i = 0;
        if(cursor != null && cursor.moveToFirst()) {
            // ir asignando propiedades a cada objeto
            while (!cursor.isAfterLast()) {
                Entry entry = new Entry();
                List<ImImage> imImages = new ArrayList<>();
                ImImage imImage = new ImImage();
                imImage.setLabel(cursor.getString(DBHelper.C_IMAGE50_INDEX));
                imImages.add(imImage);
                imImage.setLabel(cursor.getString(DBHelper.C_IMAGE75_INDEX));
                imImages.add(imImage);
                imImage.setLabel(cursor.getString(DBHelper.C_IMAGE100_INDEX));
                imImages.add(imImage);

                ImPrice imPrice = new ImPrice();
                Attributes_ attributes = new Attributes_();
                attributes.setAmount(cursor.getString(DBHelper.C_PRICE_INDEX));
                attributes.setCurrency("USD");
                imPrice.setAttributes(attributes);

                ImName imName = new ImName();
                imName.setLabel(cursor.getString(DBHelper.C_NAME_INDEX));

                Summary summary = new Summary();
                summary.setLabel(cursor.getString(DBHelper.C_SUMMARY_INDEX));

                Title title = new Title();
                title.setLabel(cursor.getString(DBHelper.C_TITLE_INDEX));

                Category category = new Category();
                Attributes______ attributes______ = new Attributes______();
                attributes______.setLabel(cursor.getString(DBHelper.C_CATEGORY_INDEX));
                category.setAttributes(attributes______);

                entry.setImName(imName);
                entry.setCategory(category);
                entry.setImImage(imImages);
                entry.setSummary(summary);
                entry.setImPrice(imPrice);
                entry.setTitle(title);

                list_entry.add(entry);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return list_entry;
    }
}
