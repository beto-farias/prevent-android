package mx.com.dgom.util.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper  {


	public MySQLiteHelper(Context context, String databaseName, int version) {
		super(context, databaseName, null, version);  
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	public String getTables(){
		return doQuery("SELECT * FROM tablename;");
	}
	
	public String doQuery(String query){
		StringBuffer res = new StringBuffer();
		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst()) {
			do {
				for(int i=0; i < cursor.getColumnCount(); i++){
					res.append( cursor.getString(i) + "\t");
				}
				res.append("\n");
			} while (cursor.moveToNext());
		}


		return res.toString();
	}
}
