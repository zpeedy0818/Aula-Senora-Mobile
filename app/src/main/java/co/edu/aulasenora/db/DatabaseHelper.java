package co.edu.aulasenora.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "aulasenora.db";
    // Si la versión sube, Android invocará onUpgrade automáticamente
    private static final int DATABASE_VERSION = 6;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_TIME_SPENT = "time_spent";
    public static final String COLUMN_SPECIALTY = "specialty";

    public static final String TABLE_AULAS = "aulas";
    public static final String COLUMN_AULA_ID = "id";
    public static final String COLUMN_AULA_NAME = "name";
    public static final String COLUMN_AULA_DESCRIPTION = "description";
    public static final String COLUMN_AULA_SUBJECT = "subject";
    public static final String COLUMN_AULA_VOLUNTEER_EMAIL = "volunteer_email";
    public static final String COLUMN_AULA_CREATED_AT = "created_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE + " TEXT, " +
                COLUMN_SPECIALTY + " TEXT, " +
                COLUMN_TIME_SPENT + " INTEGER DEFAULT 0)";
        db.execSQL(createTableUsers);

        String createTableAulas = "CREATE TABLE " + TABLE_AULAS + " (" +
                COLUMN_AULA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_AULA_NAME + " TEXT, " +
                COLUMN_AULA_DESCRIPTION + " TEXT, " +
                COLUMN_AULA_SUBJECT + " TEXT, " +
                COLUMN_AULA_VOLUNTEER_EMAIL + " TEXT, " +
                COLUMN_AULA_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableAulas);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        seedUsers(db);
    }

    private void seedUsers(SQLiteDatabase db) {
        // Estudiante Prueba
        Cursor c1 = db.rawQuery("SELECT 1 FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{"est@correo.com"});
        if (c1.getCount() == 0) {
            ContentValues v1 = new ContentValues();
            v1.put(COLUMN_NAME, "Estudiante Prueba");
            v1.put(COLUMN_EMAIL, "est@correo.com");
            v1.put(COLUMN_PASSWORD, "password123");
            v1.put(COLUMN_ROLE, "Estudiante");
            v1.put(COLUMN_TIME_SPENT, 8200);
            db.insert(TABLE_USERS, null, v1);
        }
        c1.close();

        // Voluntario Prueba
        Cursor c2 = db.rawQuery("SELECT 1 FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{"vol@correo.com"});
        if (c2.getCount() == 0) {
            ContentValues v2 = new ContentValues();
            v2.put(COLUMN_NAME, "Voluntario Prueba");
            v2.put(COLUMN_EMAIL, "vol@correo.com");
            v2.put(COLUMN_PASSWORD, "password123");
            v2.put(COLUMN_ROLE, "Voluntario");
            v2.put(COLUMN_SPECIALTY, "Matemáticas");
            v2.put(COLUMN_TIME_SPENT, 12000);
            db.insert(TABLE_USERS, null, v2);
        }
        c2.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AULAS);
        onCreate(db);
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean registerUser(String name, String email, String password, String role, String specialty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_SPECIALTY, specialty);
        values.put(COLUMN_TIME_SPENT, 0);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean isEmailAvailable(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        boolean available = (cursor.getCount() == 0);
        cursor.close();
        return available;
    }

    public String getUserRole(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ROLE + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password});
        String role = null;
        if (cursor.moveToFirst()) {
            role = cursor.getString(0);
        }
        cursor.close();
        return role;
    }

    public long getTimeSpent(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_TIME_SPENT + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        long timeSpent = 0;
        if (cursor.moveToFirst()) {
            timeSpent = cursor.getLong(0);
        }
        cursor.close();
        return timeSpent;
    }

    public void updateTimeSpent(String email, long newTimeSpent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME_SPENT, newTimeSpent);
        db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{email});
    }

    public int getRoleCount(String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ROLE + "=?", new String[]{role});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public String getUserSpecialty(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_SPECIALTY + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        String specialty = null;
        if (cursor.moveToFirst()) {
            specialty = cursor.getString(0);
        }
        cursor.close();
        return specialty;
    }

    public boolean createAula(String name, String description, String subject, String volunteerEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AULA_NAME, name);
        values.put(COLUMN_AULA_DESCRIPTION, description);
        values.put(COLUMN_AULA_SUBJECT, subject);
        values.put(COLUMN_AULA_VOLUNTEER_EMAIL, volunteerEmail);
        long result = db.insert(TABLE_AULAS, null, values);
        return result != -1;
    }

    public java.util.List<co.edu.aulasenora.models.Aula> getAulasByVolunteer(String volunteerEmail) {
        java.util.List<co.edu.aulasenora.models.Aula> aulasList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_AULAS + " WHERE " + COLUMN_AULA_VOLUNTEER_EMAIL + "=?", new String[]{volunteerEmail});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.Aula aula = new co.edu.aulasenora.models.Aula(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                aulasList.add(aula);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return aulasList;
    }

    public java.util.List<co.edu.aulasenora.models.Aula> getRecentAulasByVolunteer(String volunteerEmail, int limit) {
        java.util.List<co.edu.aulasenora.models.Aula> aulasList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_AULAS + " WHERE " + COLUMN_AULA_VOLUNTEER_EMAIL + "=? ORDER BY " + COLUMN_AULA_CREATED_AT + " DESC LIMIT ?", new String[]{volunteerEmail, String.valueOf(limit)});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.Aula aula = new co.edu.aulasenora.models.Aula(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                aulasList.add(aula);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return aulasList;
    }
}
