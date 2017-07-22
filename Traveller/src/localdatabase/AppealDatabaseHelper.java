package localdatabase;

import dataType.Tour;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AppealDatabaseHelper extends SQLiteOpenHelper{

	private static final String DB_NAME = "Appeal.db";
	private static final String TABLE_NAME = "tour";
	private static final int DB_VERSION = 1;
	private Context myContext;
	private String userName = "hdy";
	
	public AppealDatabaseHelper(Context context)
	{
		super(context,DB_NAME , null, 1);
		myContext = context;
		
	}
	
	public AppealDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "create table " + TABLE_NAME +
				"(id integer primary key autoincrement," +
				"user_name text, acname text, destination text, type text, budget text, date text," +
				" description text);";
		db.execSQL(CREATE_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exits " + TABLE_NAME);
		onCreate(db);
	}
	
	// 插入数据
	public long insert(Tour tour)
	{
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("user_name", "路飞");
		values.put("acname", tour.getAcname());
		values.put("destination",tour.getDestination());
		values.put("type", tour.getType());
		values.put("budget", tour.getBudget());
		values.put("description", tour.getDescription());
		
		long id  = db.insert(TABLE_NAME, null, values);
		
		db.close();
		return id;
		
	}
	
	
	public int update(Tour tour)
	{
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = "acname = ?";
		String [] whereArgs = { tour.getAcname()};
		
		ContentValues values = new ContentValues();
		values.put("user_name", "路飞");
		values.put("acname", tour.getAcname());
		values.put("destination",tour.getDestination());
		values.put("type", tour.getType());
		values.put("budget", tour.getBudget());
		values.put("description", tour.getDescription());
		
		
		int rows = db.update(TABLE_NAME, values, whereClause, whereArgs);
		db.close();
		
		return rows;
	}
	
	
	public int delete(Tour tour)
	{
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = "acname = ?";
		String [] whereArgs = {tour.getAcname()};
		
		int rows = db.delete(TABLE_NAME, whereClause, whereArgs);
		db.close();
		return rows;
	}
	
	public Cursor query()
	{
		SQLiteDatabase  db = getReadableDatabase();
		return db.query(TABLE_NAME, null, null, null, null, null, null);
	}
	
	

}
