package tk.v3l0c1r4pt0r.cepik;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

	private static final String KEY_ID = "id";
	private static final String KEY_REJ = "nr_rejestracyjny";
	private static final String KEY_OPIS = "opis";
	private static final String KEY_VIN = "VIN";
	private static final String KEY_DATA = "data_rejestracji";

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "HistoriaPojazdu";
    private static final String DICTIONARY_TABLE_NAME = "history";
    private static final String DICTIONARY_TABLE_CREATE =
                "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
                        KEY_ID + " INTEGER PRIMARY KEY, " +
                        KEY_REJ + " TEXT, " +
                        KEY_OPIS + " TEXT, " +
                        KEY_VIN + " TEXT, " +
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
