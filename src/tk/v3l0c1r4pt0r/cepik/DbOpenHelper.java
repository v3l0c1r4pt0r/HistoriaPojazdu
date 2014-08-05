package tk.v3l0c1r4pt0r.cepik;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

	public static final String KEY_ID = "id";
	public static final String KEY_REJ = "nr_rejestracyjny";
	public static final String KEY_OPIS = "opis";
	public static final String KEY_VIN = "VIN";
	public static final String KEY_DATA = "data_rejestracji";
	
	public static enum COLUMNS
	{
		COL_ID, COL_REJ, COL_OPIS, COL_VIN, COL_DATA
	};

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "HistoriaPojazdu";
    public static final String DICTIONARY_TABLE_NAME = "history";
    private static final String DICTIONARY_TABLE_CREATE =
                "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
                        KEY_ID + " INTEGER PRIMARY KEY, " +
                        KEY_REJ + " TEXT, " +
                        KEY_OPIS + " TEXT, " +
                        KEY_VIN + " TEXT UNIQUE, " +
                        KEY_DATA + " TEXT);";

    DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
