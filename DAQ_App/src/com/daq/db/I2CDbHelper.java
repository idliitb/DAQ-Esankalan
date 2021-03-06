package com.daq.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.idl.daq.L;

public class I2CDbHelper extends SQLiteOpenHelper {

	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "SensorDatabase.db";

	public static final String I2C_TABLE_NAME = "I2C";
	public static final String I2C_SENSOR_CODE = "sensor_code";
	public static final String I2C_QUANTITY = "quantity";
	public static final String I2C_UNIT = "unit";
	public static final String I2C_ADDRESS = "i2c_id";
	public static final String I2C_PIN_SCL = "pin_scl";
	public static final String I2C_PIN_SDA = "pin_sda";
	public static final String I2C_KEY = "_id";

	public static final String I2C_FORMULA_TABLE_NAME = "I2C_FORMULA";
	public static final String I2C_FORMULA_NAME = "name";
	public static final String I2C_FORMULA_EXPRESSION = "expression";
	public static final String I2C_FORMULA_VARIABLES = "variables";
	public static final String I2C_FORMULA_KEY = "_id";
	public static final String I2C_FORMULA_SENSOR = "sensor";
	public static final String I2C_FORMULA_DISPLAY_NAME = "displayName";
	public static final String I2C_FORMULA_DISPLAY_EXPRESSION = "displayExpression";

	public static final String I2C_CONFIG_TABLE = "I2C_CONFIG";
	public static final String I2C_CONFIG_KEY = "_id";
	public static final String I2C_CONFIG_CMD = "config_cmd";
	public static final String I2C_CONFIG_FOREIGN = "config_foreign";

	public static final String I2C_EXEC_TABLE = "I2C_EXEC";
	public static final String I2C_EXEC_KEY = "_id";
	public static final String I2C_EXEC_CMD = "exec_cmd";
	public static final String I2C_EXEC_FOREIGN = "exec_foreign";

	public SQLiteDatabase sqlDB = null;
	ArrayList<String> I2CCodes = new ArrayList<String>();
	ArrayList<String> I2CQuantities = new ArrayList<String>();
	ArrayList<String> I2CUnits = new ArrayList<String>();

	public static final String[] projection = { I2C_SENSOR_CODE, I2C_QUANTITY,
			I2C_UNIT };

	public static final String I2C_CREATE_SCRIPT = "CREATE TABLE IF NOT EXISTS "
			+ I2C_TABLE_NAME
			+ " ("
			+ I2C_KEY
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ I2C_SENSOR_CODE
			+ " TEXT, "
			+ I2C_QUANTITY
			+ " TEXT, "
			+ I2C_UNIT
			+ " TEXT, "
			+ I2C_ADDRESS
			+ " TEXT, "
			+ I2C_PIN_SCL
			+ " TEXT, "
			+ I2C_PIN_SDA
			+ " TEXT, "
			+ " UNIQUE ("
			+ I2C_SENSOR_CODE
			+ ", "
			+ I2C_QUANTITY + ", " + I2C_UNIT + ") ON CONFLICT REPLACE);";

	public static final String I2C_FORMULA_CREATE_SCRIPT = " CREATE TABLE IF NOT EXISTS "
			+ I2C_FORMULA_TABLE_NAME
			+ " ("
			+ I2C_FORMULA_KEY
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ I2C_FORMULA_NAME
			+ " TEXT, "
			+ I2C_FORMULA_EXPRESSION
			+ " TEXT, "
			+ I2C_FORMULA_DISPLAY_NAME
			+ " TEXT,"
			+ I2C_FORMULA_DISPLAY_EXPRESSION
			+ " TEXT, "
			+ I2C_FORMULA_VARIABLES
			+ " TEXT, "
			+ I2C_FORMULA_SENSOR
			+ " INTEGER, FOREIGN KEY("
			+ I2C_FORMULA_SENSOR
			+ ") REFERENCES "
			+ I2C_TABLE_NAME + "(" + I2C_KEY + "));";

	public static final String I2C_CONFIG_CREATE_SCRIPT = " CREATE TABLE IF NOT EXISTS "
			+ I2C_CONFIG_TABLE
			+ " ("
			+ I2C_CONFIG_KEY
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ I2C_CONFIG_CMD
			+ " TEXT, "
			+ I2C_CONFIG_FOREIGN
			+ " INTEGER, FOREIGN KEY("
			+ I2C_CONFIG_FOREIGN
			+ ") REFERENCES "
			+ I2C_TABLE_NAME
			+ "("
			+ I2C_KEY + "));";

	public static final String I2C_EXEC_CREATE_SCRIPT = "CREATE TABLE IF NOT EXISTS "
			+ I2C_EXEC_TABLE
			+ " ("
			+ I2C_EXEC_KEY
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ I2C_EXEC_CMD
			+ " TEXT, "
			+ I2C_EXEC_FOREIGN
			+ " INTEGER, FOREIGN KEY("
			+ I2C_EXEC_FOREIGN
			+ ") REFERENCES " + I2C_TABLE_NAME + "(" + I2C_KEY + "));";;

	public I2CDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		L.d(I2C_CREATE_SCRIPT + " " + I2C_FORMULA_CREATE_SCRIPT);
		L.d("on create database called");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		L.d("on upgrade database called");
		db.execSQL("DROP TABLE IF EXISTS I2C;DROP TABLE IF EXISTS I2C_FORMULA;DROP TABLE IF EXISTS I2C_CONFIG;DROP TABLE IF EXISTS I2C_EXEC");
		onCreate(db);

	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
		L.d("on open database called");
		L.d(I2C_CREATE_SCRIPT + " " + I2C_FORMULA_CREATE_SCRIPT);
		db.execSQL(I2C_CREATE_SCRIPT);
		db.execSQL(I2C_FORMULA_CREATE_SCRIPT);
		db.execSQL(I2C_CONFIG_CREATE_SCRIPT);
		db.execSQL(I2C_EXEC_CREATE_SCRIPT);
	}

	public void openDB() {
		// TODO Auto-generated method stub
		if (sqlDB == null) {
			sqlDB = getWritableDatabase();
			L.d("get wrtiable database");
		}
	}

	public void loadEntries() {
		// TODO Auto-generated method stub
		Cursor c = sqlDB.query(I2C_TABLE_NAME, projection, null, null, null,
				null, null);

		I2CCodes.clear();
		I2CQuantities.clear();
		I2CUnits.clear();

		c.moveToFirst();
		do {
			I2CCodes.add(c.getString(c.getColumnIndex(I2C_SENSOR_CODE)));
			I2CQuantities.add(c.getString(c.getColumnIndex(I2C_QUANTITY)));
			I2CUnits.add(c.getString(c.getColumnIndex(I2C_UNIT)));
		} while (c.moveToNext());
	}

	public ArrayList<String> getI2CCodes() {
		return I2CCodes;
	}

	public ArrayList<String> getI2CQuantities() {
		return I2CQuantities;
	}

	public ArrayList<String> getI2CUnits() {
		return I2CUnits;
	}

	public SQLiteDatabase getSqlDB() {
		return sqlDB;
	}

	public void test() {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(I2C_SENSOR_CODE, "MPU6050");
		values.put(I2C_QUANTITY, "temperature");
		values.put(I2C_UNIT, "celsius");
		values.put(I2C_PIN_SCL, "P9_19");
		values.put(I2C_PIN_SDA, "P9_20");
		values.put(I2C_ADDRESS, "69");

		long newRowId;
		newRowId = sqlDB.insert(I2C_TABLE_NAME, null, values);
		L.d("in test i2c table "+newRowId);
		
		ContentValues valuesConfig = new ContentValues();
		valuesConfig.put(I2C_CONFIG_CMD,"w:6B:0");
		valuesConfig.put(I2C_CONFIG_FOREIGN, newRowId);
		
		L.d("in test config cmd " + sqlDB.insert(I2C_CONFIG_TABLE,null,valuesConfig));
		
		ContentValues valuesExec = new ContentValues();
		valuesExec.put(I2C_EXEC_CMD, "rs:41");
		valuesExec.put(I2C_EXEC_FOREIGN, newRowId);
		
		
		long temp = sqlDB.insert(I2C_EXEC_TABLE,null,valuesExec);
		L.d("in test exec table row "+temp+ " "+newRowId);
		
		valuesExec = new ContentValues();
		valuesExec.put(I2C_EXEC_CMD, "ru:42");
		valuesExec.put(I2C_EXEC_FOREIGN, newRowId);
		
		temp = sqlDB.insert(I2C_EXEC_TABLE,null,valuesExec);
		L.d("in test exec table row "+temp+" "+newRowId);
		
		valuesExec = new ContentValues();
		valuesExec.put(I2C_EXEC_CMD, "d:1000");
		valuesExec.put(I2C_EXEC_FOREIGN, newRowId);
		
		temp = sqlDB.insert(I2C_EXEC_TABLE,null,valuesExec);
		L.d("in test exec table row "+temp+" "+newRowId);

		ContentValues valuesFormula = new ContentValues();
		valuesFormula.put(I2C_FORMULA_NAME, "read0");
		valuesFormula.put(I2C_FORMULA_EXPRESSION, "read0");
		valuesFormula.put(I2C_FORMULA_VARIABLES, "");
		valuesFormula.put(I2C_FORMULA_SENSOR, newRowId);
		valuesFormula.put(I2C_FORMULA_DISPLAY_NAME, "41");
		valuesFormula.put(I2C_FORMULA_DISPLAY_EXPRESSION, "read0");

		L.d("in test formula1 "+sqlDB.insert(I2C_FORMULA_TABLE_NAME, null, valuesFormula));

		valuesFormula = new ContentValues();
		valuesFormula.put(I2C_FORMULA_NAME, "read1");
		valuesFormula.put(I2C_FORMULA_EXPRESSION, "read1");
		valuesFormula.put(I2C_FORMULA_VARIABLES, "");
		valuesFormula.put(I2C_FORMULA_SENSOR, newRowId);
		valuesFormula.put(I2C_FORMULA_DISPLAY_NAME, "42");
		valuesFormula.put(I2C_FORMULA_DISPLAY_EXPRESSION, "read1");

		L.d("in test formula2 "+sqlDB.insert(I2C_FORMULA_TABLE_NAME, null, valuesFormula));
		
		valuesFormula = new ContentValues();
		valuesFormula.put(I2C_FORMULA_NAME, "raw");
		valuesFormula.put(I2C_FORMULA_EXPRESSION, "read0*256+read1");
		valuesFormula.put(I2C_FORMULA_VARIABLES, "read0:read1:");
		valuesFormula.put(I2C_FORMULA_SENSOR, newRowId);
		valuesFormula.put(I2C_FORMULA_DISPLAY_NAME, "raw");
		valuesFormula.put(I2C_FORMULA_DISPLAY_EXPRESSION, "41*256+42");

		
		L.d("in test formula3 "+sqlDB.insert(I2C_FORMULA_TABLE_NAME, null, valuesFormula));
		
		valuesFormula = new ContentValues();
		valuesFormula.put(I2C_FORMULA_NAME, "f1");
		valuesFormula.put(I2C_FORMULA_EXPRESSION, "raw/340+36.53");
		valuesFormula.put(I2C_FORMULA_VARIABLES, "raw:");
		valuesFormula.put(I2C_FORMULA_SENSOR, newRowId);
		valuesFormula.put(I2C_FORMULA_DISPLAY_NAME, "f1");
		valuesFormula.put(I2C_FORMULA_DISPLAY_EXPRESSION, "raw/340+36.53");

		
		L.d("in test formula3 "+sqlDB.insert(I2C_FORMULA_TABLE_NAME, null, valuesFormula));
		

	}

	public Cursor getSensorsFor(String s) {
		Cursor c;
		String query = "SELECT * FROM " + I2C_TABLE_NAME + " WHERE "
				+ I2C_SENSOR_CODE + " LIKE '" + s + "%' OR " + I2C_QUANTITY
				+ " LIKE '" + s + "%';";
		L.d(query);
		c = sqlDB.rawQuery(query, null);
		L.d(c.toString());
		L.d(c.getCount());
		return c;
	}

}
