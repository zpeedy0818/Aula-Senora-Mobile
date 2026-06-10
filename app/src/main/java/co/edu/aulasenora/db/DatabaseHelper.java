package co.edu.aulasenora.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "aulasenora.db";
    // Si la versión sube, Android invocará onUpgrade automáticamente
    private static final int DATABASE_VERSION = 10;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_TIME_SPENT = "time_spent";
    public static final String COLUMN_SPECIALTY = "specialty";
    public static final String COLUMN_TIME_IN_APP = "time_in_app";

    public static final String TABLE_AULAS = "aulas";
    public static final String COLUMN_AULA_ID = "id";
    public static final String COLUMN_AULA_NAME = "name";
    public static final String COLUMN_AULA_DESCRIPTION = "description";
    public static final String COLUMN_AULA_SUBJECT = "subject";
    public static final String COLUMN_AULA_VOLUNTEER_EMAIL = "volunteer_email";
    public static final String COLUMN_AULA_CREATED_AT = "created_at";

    public static final String TABLE_ACCESS_REQUESTS = "access_requests";
    public static final String COLUMN_REQUEST_ID = "id";
    public static final String COLUMN_REQUEST_AULA_ID = "aula_id";
    public static final String COLUMN_REQUEST_STUDENT_EMAIL = "student_email";
    public static final String COLUMN_REQUEST_STATUS = "status";
    public static final String COLUMN_REQUEST_CREATED_AT = "created_at";

    public static final String TABLE_TUTORING_REQUESTS = "tutoring_requests";
    public static final String COLUMN_TUTORING_ID = "id";
    public static final String COLUMN_TUTORING_AULA_ID = "aula_id";
    public static final String COLUMN_TUTORING_STUDENT_EMAIL = "student_email";
    public static final String COLUMN_TUTORING_TOPIC = "topic";
    public static final String COLUMN_TUTORING_DESCRIPTION = "description";
    public static final String COLUMN_TUTORING_PREFERRED_DATE = "preferred_date";
    public static final String COLUMN_TUTORING_PREFERRED_TIME = "preferred_time";
    public static final String COLUMN_TUTORING_STATUS = "status";
    public static final String COLUMN_TUTORING_CREATED_AT = "created_at";

    public static final String TABLE_SCHEDULE_SLOTS = "schedule_slots";
    public static final String COLUMN_SLOT_ID = "id";
    public static final String COLUMN_SLOT_AULA_ID = "aula_id";
    public static final String COLUMN_SLOT_VOLUNTEER_EMAIL = "volunteer_email";
    public static final String COLUMN_SLOT_DATE = "slot_date";
    public static final String COLUMN_SLOT_START_TIME = "start_time";
    public static final String COLUMN_SLOT_END_TIME = "end_time";
    public static final String COLUMN_SLOT_TYPE = "type";
    public static final String COLUMN_SLOT_TOPIC = "topic";
    public static final String COLUMN_SLOT_TARGET_STUDENT_EMAIL = "target_student_email";
    public static final String COLUMN_SLOT_CREATED_AT = "created_at";

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
                COLUMN_TIME_SPENT + " INTEGER DEFAULT 0, " +
                COLUMN_TIME_IN_APP + " INTEGER DEFAULT 0)";
        db.execSQL(createTableUsers);

        String createTableAulas = "CREATE TABLE " + TABLE_AULAS + " (" +
                COLUMN_AULA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_AULA_NAME + " TEXT, " +
                COLUMN_AULA_DESCRIPTION + " TEXT, " +
                COLUMN_AULA_SUBJECT + " TEXT, " +
                COLUMN_AULA_VOLUNTEER_EMAIL + " TEXT, " +
                COLUMN_AULA_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableAulas);

        String createTableRequests = "CREATE TABLE " + TABLE_ACCESS_REQUESTS + " (" +
                COLUMN_REQUEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REQUEST_AULA_ID + " INTEGER, " +
                COLUMN_REQUEST_STUDENT_EMAIL + " TEXT, " +
                COLUMN_REQUEST_STATUS + " TEXT DEFAULT 'pending', " +
                COLUMN_REQUEST_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableRequests);

        String createTableTutoring = "CREATE TABLE " + TABLE_TUTORING_REQUESTS + " (" +
                COLUMN_TUTORING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TUTORING_AULA_ID + " INTEGER, " +
                COLUMN_TUTORING_STUDENT_EMAIL + " TEXT, " +
                COLUMN_TUTORING_TOPIC + " TEXT, " +
                COLUMN_TUTORING_DESCRIPTION + " TEXT, " +
                COLUMN_TUTORING_PREFERRED_DATE + " TEXT, " +
                COLUMN_TUTORING_PREFERRED_TIME + " TEXT, " +
                COLUMN_TUTORING_STATUS + " TEXT DEFAULT 'pending', " +
                COLUMN_TUTORING_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableTutoring);

        String createTableSchedule = "CREATE TABLE " + TABLE_SCHEDULE_SLOTS + " (" +
                COLUMN_SLOT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SLOT_AULA_ID + " INTEGER, " +
                COLUMN_SLOT_VOLUNTEER_EMAIL + " TEXT, " +
                COLUMN_SLOT_DATE + " TEXT, " +
                COLUMN_SLOT_START_TIME + " TEXT, " +
                COLUMN_SLOT_END_TIME + " TEXT, " +
                COLUMN_SLOT_TYPE + " TEXT DEFAULT 'availability', " +
                COLUMN_SLOT_TOPIC + " TEXT, " +
                COLUMN_SLOT_TARGET_STUDENT_EMAIL + " TEXT, " +
                COLUMN_SLOT_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableSchedule);
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
            v1.put(COLUMN_TIME_IN_APP, 7200);
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
            v2.put(COLUMN_TIME_IN_APP, 10800);
            db.insert(TABLE_USERS, null, v2);
        }
        c2.close();

        // Admin
        Cursor c3 = db.rawQuery("SELECT 1 FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{"admin@correo.com"});
        if (c3.getCount() == 0) {
            ContentValues v3 = new ContentValues();
            v3.put(COLUMN_NAME, "Administrador");
            v3.put(COLUMN_EMAIL, "admin@correo.com");
            v3.put(COLUMN_PASSWORD, "admin123");
            v3.put(COLUMN_ROLE, "Admin");
            v3.put(COLUMN_TIME_SPENT, 0);
            v3.put(COLUMN_TIME_IN_APP, 0);
            db.insert(TABLE_USERS, null, v3);
        }
        c3.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AULAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCESS_REQUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TUTORING_REQUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE_SLOTS);
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
        values.put(COLUMN_TIME_IN_APP, 0);
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

    public long getTotalTimeInAppByRole(String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_TIME_IN_APP + ") FROM " + TABLE_USERS + " WHERE " + COLUMN_ROLE + "=?", new String[]{role});
        long total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getLong(0);
        }
        cursor.close();
        return total;
    }

    public int getAulasCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_AULAS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
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

    public java.util.List<co.edu.aulasenora.models.Aula> getAllAulas() {
        java.util.List<co.edu.aulasenora.models.Aula> aulasList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_AULAS + " ORDER BY " + COLUMN_AULA_CREATED_AT + " DESC", null);
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

    public java.util.List<co.edu.aulasenora.models.Aula> getRecentAulas(int limit) {
        java.util.List<co.edu.aulasenora.models.Aula> aulasList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_AULAS + " ORDER BY " + COLUMN_AULA_CREATED_AT + " DESC LIMIT ?", new String[]{String.valueOf(limit)});
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

    public java.util.List<co.edu.aulasenora.models.Aula> searchAulasByName(String query) {
        java.util.List<co.edu.aulasenora.models.Aula> aulasList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_AULAS + " WHERE " + COLUMN_AULA_NAME + " LIKE ? ORDER BY " + COLUMN_AULA_CREATED_AT + " DESC", new String[]{"%" + query + "%"});
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

    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        String name = null;
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }

    public long createAccessRequest(int aulaId, String studentEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REQUEST_AULA_ID, aulaId);
        values.put(COLUMN_REQUEST_STUDENT_EMAIL, studentEmail);
        values.put(COLUMN_REQUEST_STATUS, "pending");
        return db.insert(TABLE_ACCESS_REQUESTS, null, values);
    }

    public boolean hasExistingRequest(int aulaId, String studentEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_ACCESS_REQUESTS + " WHERE " + COLUMN_REQUEST_AULA_ID + "=? AND " + COLUMN_REQUEST_STUDENT_EMAIL + "=?", new String[]{String.valueOf(aulaId), studentEmail});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public String getAccessRequestStatus(int aulaId, String studentEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_REQUEST_STATUS + " FROM " + TABLE_ACCESS_REQUESTS + " WHERE " + COLUMN_REQUEST_AULA_ID + "=? AND " + COLUMN_REQUEST_STUDENT_EMAIL + "=?", new String[]{String.valueOf(aulaId), studentEmail});
        String status = null;
        if (cursor.moveToFirst()) {
            status = cursor.getString(0);
        }
        cursor.close();
        return status;
    }

    public void updateAccessRequestStatus(int requestId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REQUEST_STATUS, status);
        db.update(TABLE_ACCESS_REQUESTS, values, COLUMN_REQUEST_ID + "=?", new String[]{String.valueOf(requestId)});
    }

    public java.util.List<co.edu.aulasenora.models.AccessRequest> getPendingRequestsForVolunteer(String volunteerEmail, int limit) {
        java.util.List<co.edu.aulasenora.models.AccessRequest> requests = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT ar." + COLUMN_REQUEST_ID + ", ar." + COLUMN_REQUEST_AULA_ID + ", a." + COLUMN_AULA_NAME +
                ", ar." + COLUMN_REQUEST_STUDENT_EMAIL + ", u." + COLUMN_NAME + ", ar." + COLUMN_REQUEST_STATUS +
                ", ar." + COLUMN_REQUEST_CREATED_AT +
                " FROM " + TABLE_ACCESS_REQUESTS + " ar" +
                " INNER JOIN " + TABLE_AULAS + " a ON ar." + COLUMN_REQUEST_AULA_ID + " = a." + COLUMN_AULA_ID +
                " INNER JOIN " + TABLE_USERS + " u ON ar." + COLUMN_REQUEST_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE a." + COLUMN_AULA_VOLUNTEER_EMAIL + "=? AND ar." + COLUMN_REQUEST_STATUS + "=?" +
                " ORDER BY ar." + COLUMN_REQUEST_CREATED_AT + " DESC LIMIT ?";
        Cursor cursor = db.rawQuery(query, new String[]{volunteerEmail, "pending", String.valueOf(limit)});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.AccessRequest req = new co.edu.aulasenora.models.AccessRequest(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                requests.add(req);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return requests;
    }

    public java.util.List<co.edu.aulasenora.models.AccessRequest> getAllPendingRequestsForVolunteer(String volunteerEmail) {
        java.util.List<co.edu.aulasenora.models.AccessRequest> requests = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT ar." + COLUMN_REQUEST_ID + ", ar." + COLUMN_REQUEST_AULA_ID + ", a." + COLUMN_AULA_NAME +
                ", ar." + COLUMN_REQUEST_STUDENT_EMAIL + ", u." + COLUMN_NAME + ", ar." + COLUMN_REQUEST_STATUS +
                ", ar." + COLUMN_REQUEST_CREATED_AT +
                " FROM " + TABLE_ACCESS_REQUESTS + " ar" +
                " INNER JOIN " + TABLE_AULAS + " a ON ar." + COLUMN_REQUEST_AULA_ID + " = a." + COLUMN_AULA_ID +
                " INNER JOIN " + TABLE_USERS + " u ON ar." + COLUMN_REQUEST_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE a." + COLUMN_AULA_VOLUNTEER_EMAIL + "=? AND ar." + COLUMN_REQUEST_STATUS + "=?" +
                " ORDER BY ar." + COLUMN_REQUEST_CREATED_AT + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{volunteerEmail, "pending"});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.AccessRequest req = new co.edu.aulasenora.models.AccessRequest(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                requests.add(req);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return requests;
    }

    public java.util.List<co.edu.aulasenora.models.Aula> getEnrolledAulas(String studentEmail) {
        java.util.List<co.edu.aulasenora.models.Aula> aulasList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a." + COLUMN_AULA_ID + ", a." + COLUMN_AULA_NAME + ", a." + COLUMN_AULA_DESCRIPTION +
                ", a." + COLUMN_AULA_SUBJECT + ", a." + COLUMN_AULA_VOLUNTEER_EMAIL + ", a." + COLUMN_AULA_CREATED_AT +
                " FROM " + TABLE_AULAS + " a" +
                " INNER JOIN " + TABLE_ACCESS_REQUESTS + " ar ON a." + COLUMN_AULA_ID + " = ar." + COLUMN_REQUEST_AULA_ID +
                " WHERE ar." + COLUMN_REQUEST_STUDENT_EMAIL + "=? AND ar." + COLUMN_REQUEST_STATUS + "=?" +
                " ORDER BY ar." + COLUMN_REQUEST_CREATED_AT + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{studentEmail, "approved"});
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

    public java.util.List<Integer> getEnrolledAulaIds(String studentEmail) {
        java.util.List<Integer> ids = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_REQUEST_AULA_ID + " FROM " + TABLE_ACCESS_REQUESTS +
                " WHERE " + COLUMN_REQUEST_STUDENT_EMAIL + "=? AND " + COLUMN_REQUEST_STATUS + "=?",
                new String[]{studentEmail, "approved"});
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ids;
    }

    public java.util.List<co.edu.aulasenora.models.AdmittedStudent> getAdmittedStudents(int aulaId) {
        java.util.List<co.edu.aulasenora.models.AdmittedStudent> students = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u." + COLUMN_EMAIL + ", u." + COLUMN_NAME + ", ar." + COLUMN_REQUEST_CREATED_AT +
                " FROM " + TABLE_USERS + " u" +
                " INNER JOIN " + TABLE_ACCESS_REQUESTS + " ar ON u." + COLUMN_EMAIL + " = ar." + COLUMN_REQUEST_STUDENT_EMAIL +
                " WHERE ar." + COLUMN_REQUEST_AULA_ID + "=? AND ar." + COLUMN_REQUEST_STATUS + "=?" +
                " ORDER BY ar." + COLUMN_REQUEST_CREATED_AT + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(aulaId), "approved"});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.AdmittedStudent s = new co.edu.aulasenora.models.AdmittedStudent(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2)
                );
                students.add(s);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return students;
    }

    public java.util.List<co.edu.aulasenora.models.AccessRequest> getPendingAccessRequestsForAula(int aulaId) {
        java.util.List<co.edu.aulasenora.models.AccessRequest> requests = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT ar." + COLUMN_REQUEST_ID + ", ar." + COLUMN_REQUEST_AULA_ID + ", a." + COLUMN_AULA_NAME +
                ", ar." + COLUMN_REQUEST_STUDENT_EMAIL + ", u." + COLUMN_NAME + ", ar." + COLUMN_REQUEST_STATUS +
                ", ar." + COLUMN_REQUEST_CREATED_AT +
                " FROM " + TABLE_ACCESS_REQUESTS + " ar" +
                " INNER JOIN " + TABLE_AULAS + " a ON ar." + COLUMN_REQUEST_AULA_ID + " = a." + COLUMN_AULA_ID +
                " INNER JOIN " + TABLE_USERS + " u ON ar." + COLUMN_REQUEST_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE ar." + COLUMN_REQUEST_AULA_ID + "=? AND ar." + COLUMN_REQUEST_STATUS + "=?" +
                " ORDER BY ar." + COLUMN_REQUEST_CREATED_AT + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(aulaId), "pending"});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.AccessRequest req = new co.edu.aulasenora.models.AccessRequest(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                requests.add(req);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return requests;
    }

    public long createTutoringRequest(int aulaId, String studentEmail, String topic, String description,
                                      String preferredDate, String preferredTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TUTORING_AULA_ID, aulaId);
        values.put(COLUMN_TUTORING_STUDENT_EMAIL, studentEmail);
        values.put(COLUMN_TUTORING_TOPIC, topic);
        values.put(COLUMN_TUTORING_DESCRIPTION, description);
        values.put(COLUMN_TUTORING_PREFERRED_DATE, preferredDate);
        values.put(COLUMN_TUTORING_PREFERRED_TIME, preferredTime);
        values.put(COLUMN_TUTORING_STATUS, "pending");
        return db.insert(TABLE_TUTORING_REQUESTS, null, values);
    }

    public java.util.List<co.edu.aulasenora.models.TutoringRequest> getTutoringRequestsForAula(int aulaId) {
        java.util.List<co.edu.aulasenora.models.TutoringRequest> requests = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT tr." + COLUMN_TUTORING_ID + ", tr." + COLUMN_TUTORING_AULA_ID +
                ", tr." + COLUMN_TUTORING_STUDENT_EMAIL + ", u." + COLUMN_NAME +
                ", tr." + COLUMN_TUTORING_TOPIC + ", tr." + COLUMN_TUTORING_DESCRIPTION +
                ", tr." + COLUMN_TUTORING_PREFERRED_DATE + ", tr." + COLUMN_TUTORING_PREFERRED_TIME +
                ", tr." + COLUMN_TUTORING_STATUS + ", tr." + COLUMN_TUTORING_CREATED_AT +
                " FROM " + TABLE_TUTORING_REQUESTS + " tr" +
                " INNER JOIN " + TABLE_USERS + " u ON tr." + COLUMN_TUTORING_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE tr." + COLUMN_TUTORING_AULA_ID + "=? AND tr." + COLUMN_TUTORING_STATUS + "=?" +
                " ORDER BY tr." + COLUMN_TUTORING_CREATED_AT + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(aulaId), "pending"});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.TutoringRequest req = new co.edu.aulasenora.models.TutoringRequest(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9)
                );
                requests.add(req);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return requests;
    }

    public void updateTutoringRequestStatus(int requestId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TUTORING_STATUS, status);
        db.update(TABLE_TUTORING_REQUESTS, values, COLUMN_TUTORING_ID + "=?", new String[]{String.valueOf(requestId)});
    }

    public long createScheduleSlot(int aulaId, String volunteerEmail, String date, String startTime,
                                   String endTime, String type, String topic, String targetStudentEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SLOT_AULA_ID, aulaId);
        values.put(COLUMN_SLOT_VOLUNTEER_EMAIL, volunteerEmail);
        values.put(COLUMN_SLOT_DATE, date);
        values.put(COLUMN_SLOT_START_TIME, startTime);
        values.put(COLUMN_SLOT_END_TIME, endTime);
        values.put(COLUMN_SLOT_TYPE, type);
        values.put(COLUMN_SLOT_TOPIC, topic);
        values.put(COLUMN_SLOT_TARGET_STUDENT_EMAIL, targetStudentEmail);
        return db.insert(TABLE_SCHEDULE_SLOTS, null, values);
    }

    public java.util.List<co.edu.aulasenora.models.ScheduleSlot> getScheduleSlotsForAula(int aulaId) {
        java.util.List<co.edu.aulasenora.models.ScheduleSlot> slots = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s." + COLUMN_SLOT_ID + ", s." + COLUMN_SLOT_AULA_ID +
                ", s." + COLUMN_SLOT_VOLUNTEER_EMAIL + ", s." + COLUMN_SLOT_DATE +
                ", s." + COLUMN_SLOT_START_TIME + ", s." + COLUMN_SLOT_END_TIME +
                ", s." + COLUMN_SLOT_TYPE + ", s." + COLUMN_SLOT_TOPIC +
                ", s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + ", COALESCE(u." + COLUMN_NAME + ", '')" +
                ", s." + COLUMN_SLOT_CREATED_AT +
                " FROM " + TABLE_SCHEDULE_SLOTS + " s" +
                " LEFT JOIN " + TABLE_USERS + " u ON s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE s." + COLUMN_SLOT_AULA_ID + "=?" +
                " ORDER BY s." + COLUMN_SLOT_DATE + " ASC, s." + COLUMN_SLOT_START_TIME + " ASC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(aulaId)});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.ScheduleSlot slot = new co.edu.aulasenora.models.ScheduleSlot(
                        cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10)
                );
                slots.add(slot);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return slots;
    }

    public java.util.List<co.edu.aulasenora.models.ScheduleSlot> getUpcomingTutoringSessions(int aulaId, int limit) {
        java.util.List<co.edu.aulasenora.models.ScheduleSlot> sessions = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(new java.util.Date());
        String query = "SELECT s." + COLUMN_SLOT_ID + ", s." + COLUMN_SLOT_AULA_ID +
                ", s." + COLUMN_SLOT_VOLUNTEER_EMAIL + ", s." + COLUMN_SLOT_DATE +
                ", s." + COLUMN_SLOT_START_TIME + ", s." + COLUMN_SLOT_END_TIME +
                ", s." + COLUMN_SLOT_TYPE + ", s." + COLUMN_SLOT_TOPIC +
                ", s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + ", COALESCE(u." + COLUMN_NAME + ", '')" +
                ", s." + COLUMN_SLOT_CREATED_AT +
                " FROM " + TABLE_SCHEDULE_SLOTS + " s" +
                " LEFT JOIN " + TABLE_USERS + " u ON s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE s." + COLUMN_SLOT_AULA_ID + "=? AND s." + COLUMN_SLOT_TYPE + "=? AND s." + COLUMN_SLOT_DATE + ">=?" +
                " ORDER BY s." + COLUMN_SLOT_DATE + " ASC, s." + COLUMN_SLOT_START_TIME + " ASC LIMIT ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(aulaId), "tutoring", today, String.valueOf(limit)});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.ScheduleSlot slot = new co.edu.aulasenora.models.ScheduleSlot(
                        cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10)
                );
                sessions.add(slot);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sessions;
    }

    public boolean hasScheduleConflict(String volunteerEmail, String date, String startTime, String endTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_SCHEDULE_SLOTS +
                " WHERE " + COLUMN_SLOT_VOLUNTEER_EMAIL + "=? AND " + COLUMN_SLOT_DATE + "=?" +
                " AND " + COLUMN_SLOT_START_TIME + " < ? AND " + COLUMN_SLOT_END_TIME + " > ?" +
                " LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{volunteerEmail, date, endTime, startTime});
        boolean conflict = cursor.getCount() > 0;
        cursor.close();
        return conflict;
    }

    public String getConflictAulaName(String volunteerEmail, String date, String startTime, String endTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a." + COLUMN_AULA_NAME +
                " FROM " + TABLE_SCHEDULE_SLOTS + " s" +
                " INNER JOIN " + TABLE_AULAS + " a ON s." + COLUMN_SLOT_AULA_ID + " = a." + COLUMN_AULA_ID +
                " WHERE s." + COLUMN_SLOT_VOLUNTEER_EMAIL + "=? AND s." + COLUMN_SLOT_DATE + "=?" +
                " AND s." + COLUMN_SLOT_START_TIME + " < ? AND s." + COLUMN_SLOT_END_TIME + " > ?" +
                " LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{volunteerEmail, date, endTime, startTime});
        String name = null;
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }

    public void deleteScheduleSlot(int slotId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCHEDULE_SLOTS, COLUMN_SLOT_ID + "=?", new String[]{String.valueOf(slotId)});
    }

    public java.util.List<co.edu.aulasenora.models.ScheduleSlot> getRecentScheduleSlots(int aulaId, int limit) {
        java.util.List<co.edu.aulasenora.models.ScheduleSlot> slots = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s." + COLUMN_SLOT_ID + ", s." + COLUMN_SLOT_AULA_ID +
                ", s." + COLUMN_SLOT_VOLUNTEER_EMAIL + ", s." + COLUMN_SLOT_DATE +
                ", s." + COLUMN_SLOT_START_TIME + ", s." + COLUMN_SLOT_END_TIME +
                ", s." + COLUMN_SLOT_TYPE + ", s." + COLUMN_SLOT_TOPIC +
                ", s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + ", COALESCE(u." + COLUMN_NAME + ", '')" +
                ", s." + COLUMN_SLOT_CREATED_AT +
                " FROM " + TABLE_SCHEDULE_SLOTS + " s" +
                " LEFT JOIN " + TABLE_USERS + " u ON s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE s." + COLUMN_SLOT_AULA_ID + "=?" +
                " ORDER BY s." + COLUMN_SLOT_CREATED_AT + " DESC LIMIT ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(aulaId), String.valueOf(limit)});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.ScheduleSlot slot = new co.edu.aulasenora.models.ScheduleSlot(
                        cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10)
                );
                slots.add(slot);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return slots;
    }

    public java.util.List<co.edu.aulasenora.models.ScheduleSlot> getAllUpcomingTutoringSessions(int aulaId) {
        java.util.List<co.edu.aulasenora.models.ScheduleSlot> sessions = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(new java.util.Date());
        String query = "SELECT s." + COLUMN_SLOT_ID + ", s." + COLUMN_SLOT_AULA_ID +
                ", s." + COLUMN_SLOT_VOLUNTEER_EMAIL + ", s." + COLUMN_SLOT_DATE +
                ", s." + COLUMN_SLOT_START_TIME + ", s." + COLUMN_SLOT_END_TIME +
                ", s." + COLUMN_SLOT_TYPE + ", s." + COLUMN_SLOT_TOPIC +
                ", s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + ", COALESCE(u." + COLUMN_NAME + ", '')" +
                ", s." + COLUMN_SLOT_CREATED_AT +
                " FROM " + TABLE_SCHEDULE_SLOTS + " s" +
                " LEFT JOIN " + TABLE_USERS + " u ON s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE s." + COLUMN_SLOT_AULA_ID + "=? AND s." + COLUMN_SLOT_TYPE + "=? AND s." + COLUMN_SLOT_DATE + ">=?" +
                " ORDER BY s." + COLUMN_SLOT_DATE + " ASC, s." + COLUMN_SLOT_START_TIME + " ASC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(aulaId), "tutoring", today});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.ScheduleSlot slot = new co.edu.aulasenora.models.ScheduleSlot(
                        cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10)
                );
                sessions.add(slot);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sessions;
    }

    public java.util.List<co.edu.aulasenora.models.ScheduleSlot> getAvailableScheduleSlotsForAula(int aulaId) {
        java.util.List<co.edu.aulasenora.models.ScheduleSlot> slots = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s." + COLUMN_SLOT_ID + ", s." + COLUMN_SLOT_AULA_ID +
                ", s." + COLUMN_SLOT_VOLUNTEER_EMAIL + ", s." + COLUMN_SLOT_DATE +
                ", s." + COLUMN_SLOT_START_TIME + ", s." + COLUMN_SLOT_END_TIME +
                ", s." + COLUMN_SLOT_TYPE + ", s." + COLUMN_SLOT_TOPIC +
                ", s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + ", COALESCE(u." + COLUMN_NAME + ", '')" +
                ", s." + COLUMN_SLOT_CREATED_AT +
                " FROM " + TABLE_SCHEDULE_SLOTS + " s" +
                " LEFT JOIN " + TABLE_USERS + " u ON s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE s." + COLUMN_SLOT_AULA_ID + "=? AND s." + COLUMN_SLOT_TYPE + "=?" +
                " ORDER BY s." + COLUMN_SLOT_DATE + " ASC, s." + COLUMN_SLOT_START_TIME + " ASC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(aulaId), "availability"});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.ScheduleSlot slot = new co.edu.aulasenora.models.ScheduleSlot(
                        cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10)
                );
                slots.add(slot);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return slots;
    }

    public co.edu.aulasenora.models.Aula getAulaById(int aulaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_AULAS + " WHERE " + COLUMN_AULA_ID + "=?", new String[]{String.valueOf(aulaId)});
        co.edu.aulasenora.models.Aula aula = null;
        if (cursor.moveToFirst()) {
            aula = new co.edu.aulasenora.models.Aula(
                    cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5)
            );
        }
        cursor.close();
        return aula;
    }
}
