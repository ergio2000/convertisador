package pe.pucp.convertisador;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "monedas.db";
    static final String TABLE_Tasas = "equivalencias";
    static final String KEY_ID = "id";
    static final String KEY_NOMBRE = "moneda";
    static final String KEY_TASA = "equivalencia";
    static final String KEY_VER = "ver";

    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Tasas + "(" +
                KEY_ID + " INTEGER PRIMARY KEY , " +
                KEY_NOMBRE + " TEXT," +
                KEY_TASA + " REAL, " +
                KEY_VER + " INTEGER )";

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
