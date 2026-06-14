package co.edu.aulasenora.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "aulasenora.db";
    // Si la versión sube, Android invocará onUpgrade automáticamente
    private static final int DATABASE_VERSION = 15;

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
    public static final String COLUMN_TUTORING_PREFERRED_END_TIME = "preferred_end_time";
    public static final String COLUMN_TUTORING_STATUS = "status";
    public static final String COLUMN_TUTORING_CREATED_AT = "created_at";

    public static final String TABLE_SUPPORT_MATERIALS = "support_materials";
    public static final String COLUMN_MATERIAL_ID = "id";
    public static final String COLUMN_MATERIAL_AULA_ID = "aula_id";
    public static final String COLUMN_MATERIAL_VOLUNTEER_EMAIL = "volunteer_email";
    public static final String COLUMN_MATERIAL_CUSTOM_NAME = "custom_name";
    public static final String COLUMN_MATERIAL_FILE_NAME = "file_name";
    public static final String COLUMN_MATERIAL_FILE_PATH = "file_path";
    public static final String COLUMN_MATERIAL_MIME_TYPE = "mime_type";
    public static final String COLUMN_MATERIAL_FILE_SIZE = "file_size";
    public static final String COLUMN_MATERIAL_CREATED_AT = "created_at";

    public static final String TABLE_MATERIAL_DOWNLOADS = "material_downloads";
    public static final String COLUMN_DOWNLOAD_ID = "id";
    public static final String COLUMN_DOWNLOAD_MATERIAL_ID = "material_id";
    public static final String COLUMN_DOWNLOAD_STUDENT_EMAIL = "student_email";
    public static final String COLUMN_DOWNLOAD_VIEWED_AT = "viewed_at";

    public static final String TABLE_CHAT_MESSAGES = "chat_messages";
    public static final String COLUMN_CHAT_ID = "id";
    public static final String COLUMN_CHAT_AULA_ID = "aula_id";
    public static final String COLUMN_CHAT_SENDER_EMAIL = "sender_email";
    public static final String COLUMN_CHAT_MESSAGE = "message";
    public static final String COLUMN_CHAT_CREATED_AT = "created_at";

    public static final String TABLE_CHAT_READ_STATUS = "chat_read_status";
    public static final String COLUMN_READ_ID = "id";
    public static final String COLUMN_READ_AULA_ID = "aula_id";
    public static final String COLUMN_READ_USER_EMAIL = "user_email";
    public static final String COLUMN_READ_LAST_MESSAGE_ID = "last_read_message_id";

    public static final String TABLE_VOLUNTEER_RATINGS = "volunteer_ratings";
    public static final String COLUMN_RATING_ID = "id";
    public static final String COLUMN_RATING_VOLUNTEER_EMAIL = "volunteer_email";
    public static final String COLUMN_RATING_STUDENT_EMAIL = "student_email";
    public static final String COLUMN_RATING_AULA_ID = "aula_id";
    public static final String COLUMN_RATING_VALUE = "rating";
    public static final String COLUMN_RATING_CREATED_AT = "created_at";

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
                COLUMN_TUTORING_PREFERRED_END_TIME + " TEXT, " +
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

        String createTableMaterials = "CREATE TABLE " + TABLE_SUPPORT_MATERIALS + " (" +
                COLUMN_MATERIAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MATERIAL_AULA_ID + " INTEGER, " +
                COLUMN_MATERIAL_VOLUNTEER_EMAIL + " TEXT, " +
                COLUMN_MATERIAL_CUSTOM_NAME + " TEXT, " +
                COLUMN_MATERIAL_FILE_NAME + " TEXT, " +
                COLUMN_MATERIAL_FILE_PATH + " TEXT, " +
                COLUMN_MATERIAL_MIME_TYPE + " TEXT, " +
                COLUMN_MATERIAL_FILE_SIZE + " INTEGER DEFAULT 0, " +
                COLUMN_MATERIAL_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableMaterials);

        String createTableMaterialDownloads = "CREATE TABLE " + TABLE_MATERIAL_DOWNLOADS + " (" +
                COLUMN_DOWNLOAD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DOWNLOAD_MATERIAL_ID + " INTEGER NOT NULL, " +
                COLUMN_DOWNLOAD_STUDENT_EMAIL + " TEXT NOT NULL, " +
                COLUMN_DOWNLOAD_VIEWED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "UNIQUE(" + COLUMN_DOWNLOAD_MATERIAL_ID + ", " + COLUMN_DOWNLOAD_STUDENT_EMAIL + "))";
        db.execSQL(createTableMaterialDownloads);

        String createTableChatMessages = "CREATE TABLE " + TABLE_CHAT_MESSAGES + " (" +
                COLUMN_CHAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CHAT_AULA_ID + " INTEGER, " +
                COLUMN_CHAT_SENDER_EMAIL + " TEXT, " +
                COLUMN_CHAT_MESSAGE + " TEXT, " +
                COLUMN_CHAT_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTableChatMessages);

        String createTableReadStatus = "CREATE TABLE " + TABLE_CHAT_READ_STATUS + " (" +
                COLUMN_READ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_READ_AULA_ID + " INTEGER, " +
                COLUMN_READ_USER_EMAIL + " TEXT, " +
                COLUMN_READ_LAST_MESSAGE_ID + " INTEGER DEFAULT 0, " +
                "UNIQUE(" + COLUMN_READ_AULA_ID + ", " + COLUMN_READ_USER_EMAIL + "))";
        db.execSQL(createTableReadStatus);
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
        if (oldVersion < 11) {
            try {
                db.execSQL("ALTER TABLE " + TABLE_TUTORING_REQUESTS + " ADD COLUMN " + COLUMN_TUTORING_PREFERRED_END_TIME + " TEXT");
            } catch (Exception ignored) { }
        }
        if (oldVersion < 12) {
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SUPPORT_MATERIALS + " (" +
                        COLUMN_MATERIAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_MATERIAL_AULA_ID + " INTEGER, " +
                        COLUMN_MATERIAL_VOLUNTEER_EMAIL + " TEXT, " +
                        COLUMN_MATERIAL_CUSTOM_NAME + " TEXT, " +
                        COLUMN_MATERIAL_FILE_NAME + " TEXT, " +
                        COLUMN_MATERIAL_FILE_PATH + " TEXT, " +
                        COLUMN_MATERIAL_MIME_TYPE + " TEXT, " +
                        COLUMN_MATERIAL_FILE_SIZE + " INTEGER DEFAULT 0, " +
                        COLUMN_MATERIAL_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP)");
            } catch (Exception ignored) { }
        }
        if (oldVersion < 13) {
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CHAT_MESSAGES + " (" +
                        COLUMN_CHAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_CHAT_AULA_ID + " INTEGER, " +
                        COLUMN_CHAT_SENDER_EMAIL + " TEXT, " +
                        COLUMN_CHAT_MESSAGE + " TEXT, " +
                        COLUMN_CHAT_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP)");
            } catch (Exception ignored) { }
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CHAT_READ_STATUS + " (" +
                        COLUMN_READ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_READ_AULA_ID + " INTEGER, " +
                        COLUMN_READ_USER_EMAIL + " TEXT, " +
                        COLUMN_READ_LAST_MESSAGE_ID + " INTEGER DEFAULT 0, " +
                        "UNIQUE(" + COLUMN_READ_AULA_ID + ", " + COLUMN_READ_USER_EMAIL + "))");
            } catch (Exception ignored) { }
        }
        if (oldVersion < 14) {
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MATERIAL_DOWNLOADS + " (" +
                        COLUMN_DOWNLOAD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_DOWNLOAD_MATERIAL_ID + " INTEGER NOT NULL, " +
                        COLUMN_DOWNLOAD_STUDENT_EMAIL + " TEXT NOT NULL, " +
                        COLUMN_DOWNLOAD_VIEWED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "UNIQUE(" + COLUMN_DOWNLOAD_MATERIAL_ID + ", " + COLUMN_DOWNLOAD_STUDENT_EMAIL + "))");
            } catch (Exception ignored) { }
        }
        if (oldVersion < 15) {
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_VOLUNTEER_RATINGS + " (" +
                        COLUMN_RATING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RATING_VOLUNTEER_EMAIL + " TEXT, " +
                        COLUMN_RATING_STUDENT_EMAIL + " TEXT, " +
                        COLUMN_RATING_AULA_ID + " INTEGER, " +
                        COLUMN_RATING_VALUE + " INTEGER DEFAULT 0, " +
                        COLUMN_RATING_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "UNIQUE(" + COLUMN_RATING_VOLUNTEER_EMAIL + ", " + COLUMN_RATING_STUDENT_EMAIL + ", " + COLUMN_RATING_AULA_ID + "))");
            } catch (Exception ignored) { }
        }
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

    public String getUserRoleByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ROLE + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
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

    public void saveVolunteerRating(String volunteerEmail, String studentEmail, int aulaId, int rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING_VOLUNTEER_EMAIL, volunteerEmail);
        values.put(COLUMN_RATING_STUDENT_EMAIL, studentEmail);
        values.put(COLUMN_RATING_AULA_ID, aulaId);
        values.put(COLUMN_RATING_VALUE, rating);
        db.insertWithOnConflict(TABLE_VOLUNTEER_RATINGS, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public int getVolunteerRating(String volunteerEmail, String studentEmail, int aulaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_RATING_VALUE + " FROM " + TABLE_VOLUNTEER_RATINGS +
                " WHERE " + COLUMN_RATING_VOLUNTEER_EMAIL + "=? AND " + COLUMN_RATING_STUDENT_EMAIL + "=? AND " + COLUMN_RATING_AULA_ID + "=?",
                new String[]{volunteerEmail, studentEmail, String.valueOf(aulaId)});
        int rating = 0;
        if (cursor.moveToFirst()) {
            rating = cursor.getInt(0);
        }
        cursor.close();
        return rating;
    }

    public int getDistinctStudentsHelpedCount(String volunteerEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(DISTINCT ar." + COLUMN_REQUEST_STUDENT_EMAIL + ") FROM " + TABLE_ACCESS_REQUESTS + " ar " +
                        "INNER JOIN " + TABLE_AULAS + " a ON ar." + COLUMN_REQUEST_AULA_ID + " = a." + COLUMN_AULA_ID +
                        " WHERE a." + COLUMN_AULA_VOLUNTEER_EMAIL + "=? AND ar." + COLUMN_REQUEST_STATUS + "='approved'",
                new String[]{volunteerEmail});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public double getVolunteerAverageRating(String volunteerEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT AVG(CAST(" + COLUMN_RATING_VALUE + " AS REAL)) FROM " + TABLE_VOLUNTEER_RATINGS +
                        " WHERE " + COLUMN_RATING_VOLUNTEER_EMAIL + "=?",
                new String[]{volunteerEmail});
        double avg = 0.0;
        if (cursor.moveToFirst()) {
            avg = cursor.getDouble(0);
        }
        cursor.close();
        return avg;
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
                                      String preferredDate, String preferredTime, String preferredEndTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TUTORING_AULA_ID, aulaId);
        values.put(COLUMN_TUTORING_STUDENT_EMAIL, studentEmail);
        values.put(COLUMN_TUTORING_TOPIC, topic);
        values.put(COLUMN_TUTORING_DESCRIPTION, description);
        values.put(COLUMN_TUTORING_PREFERRED_DATE, preferredDate);
        values.put(COLUMN_TUTORING_PREFERRED_TIME, preferredTime);
        values.put(COLUMN_TUTORING_PREFERRED_END_TIME, preferredEndTime);
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
                ", tr." + COLUMN_TUTORING_PREFERRED_END_TIME + ", tr." + COLUMN_TUTORING_STATUS +
                ", tr." + COLUMN_TUTORING_CREATED_AT +
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
                        cursor.getString(9),
                        cursor.getString(10)
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

    public java.util.List<co.edu.aulasenora.models.ScheduleSlot> getUpcomingTutoringForVolunteer(String volunteerEmail, int limit) {
        java.util.List<co.edu.aulasenora.models.ScheduleSlot> sessions = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(new java.util.Date());
        String query = "SELECT s." + COLUMN_SLOT_ID + ", s." + COLUMN_SLOT_AULA_ID +
                ", s." + COLUMN_SLOT_VOLUNTEER_EMAIL + ", s." + COLUMN_SLOT_DATE +
                ", s." + COLUMN_SLOT_START_TIME + ", s." + COLUMN_SLOT_END_TIME +
                ", s." + COLUMN_SLOT_TYPE + ", s." + COLUMN_SLOT_TOPIC +
                ", s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + ", COALESCE(u." + COLUMN_NAME + ", '')" +
                ", s." + COLUMN_SLOT_CREATED_AT + ", a." + COLUMN_AULA_NAME +
                " FROM " + TABLE_SCHEDULE_SLOTS + " s" +
                " INNER JOIN " + TABLE_AULAS + " a ON s." + COLUMN_SLOT_AULA_ID + " = a." + COLUMN_AULA_ID +
                " LEFT JOIN " + TABLE_USERS + " u ON s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE a." + COLUMN_AULA_VOLUNTEER_EMAIL + "=? AND s." + COLUMN_SLOT_TYPE + "=? AND s." + COLUMN_SLOT_DATE + ">=?" +
                " ORDER BY s." + COLUMN_SLOT_DATE + " ASC, s." + COLUMN_SLOT_START_TIME + " ASC LIMIT ?";
        Cursor cursor = db.rawQuery(query, new String[]{volunteerEmail, "tutoring", today, String.valueOf(limit)});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.ScheduleSlot slot = new co.edu.aulasenora.models.ScheduleSlot(
                        cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10)
                );
                slot.setAulaName(cursor.getString(11));
                sessions.add(slot);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sessions;
    }

    public java.util.List<co.edu.aulasenora.models.ScheduleSlot> getTodayTutoringForVolunteer(String volunteerEmail) {
        java.util.List<co.edu.aulasenora.models.ScheduleSlot> sessions = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(new java.util.Date());
        String query = "SELECT s." + COLUMN_SLOT_ID + ", s." + COLUMN_SLOT_AULA_ID +
                ", s." + COLUMN_SLOT_VOLUNTEER_EMAIL + ", s." + COLUMN_SLOT_DATE +
                ", s." + COLUMN_SLOT_START_TIME + ", s." + COLUMN_SLOT_END_TIME +
                ", s." + COLUMN_SLOT_TYPE + ", s." + COLUMN_SLOT_TOPIC +
                ", s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + ", COALESCE(u." + COLUMN_NAME + ", '')" +
                ", s." + COLUMN_SLOT_CREATED_AT + ", a." + COLUMN_AULA_NAME +
                " FROM " + TABLE_SCHEDULE_SLOTS + " s" +
                " INNER JOIN " + TABLE_AULAS + " a ON s." + COLUMN_SLOT_AULA_ID + " = a." + COLUMN_AULA_ID +
                " LEFT JOIN " + TABLE_USERS + " u ON s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE a." + COLUMN_AULA_VOLUNTEER_EMAIL + "=? AND s." + COLUMN_SLOT_TYPE + "=? AND s." + COLUMN_SLOT_DATE + "=?" +
                " ORDER BY s." + COLUMN_SLOT_START_TIME + " ASC";
        Cursor cursor = db.rawQuery(query, new String[]{volunteerEmail, "tutoring", today});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.ScheduleSlot slot = new co.edu.aulasenora.models.ScheduleSlot(
                        cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10)
                );
                slot.setAulaName(cursor.getString(11));
                sessions.add(slot);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sessions;
    }

    public java.util.List<co.edu.aulasenora.models.ScheduleSlot> getUpcomingTutoringForStudent(String studentEmail, int limit) {
        java.util.List<co.edu.aulasenora.models.ScheduleSlot> sessions = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(new java.util.Date());
        String query = "SELECT s." + COLUMN_SLOT_ID + ", s." + COLUMN_SLOT_AULA_ID +
                ", s." + COLUMN_SLOT_VOLUNTEER_EMAIL + ", s." + COLUMN_SLOT_DATE +
                ", s." + COLUMN_SLOT_START_TIME + ", s." + COLUMN_SLOT_END_TIME +
                ", s." + COLUMN_SLOT_TYPE + ", s." + COLUMN_SLOT_TOPIC +
                ", s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + ", COALESCE(u." + COLUMN_NAME + ", '')" +
                ", s." + COLUMN_SLOT_CREATED_AT + ", a." + COLUMN_AULA_NAME +
                " FROM " + TABLE_SCHEDULE_SLOTS + " s" +
                " INNER JOIN " + TABLE_AULAS + " a ON s." + COLUMN_SLOT_AULA_ID + " = a." + COLUMN_AULA_ID +
                " INNER JOIN " + TABLE_ACCESS_REQUESTS + " ar ON ar." + COLUMN_REQUEST_AULA_ID + " = s." + COLUMN_SLOT_AULA_ID +
                " LEFT JOIN " + TABLE_USERS + " u ON s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE ar." + COLUMN_REQUEST_STUDENT_EMAIL + "=? AND ar." + COLUMN_REQUEST_STATUS + "=?" +
                " AND s." + COLUMN_SLOT_TYPE + "=? AND (s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + "=? OR s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + " IS NULL)" +
                " AND s." + COLUMN_SLOT_DATE + ">=?" +
                " ORDER BY s." + COLUMN_SLOT_DATE + " ASC, s." + COLUMN_SLOT_START_TIME + " ASC LIMIT ?";
        Cursor cursor = db.rawQuery(query, new String[]{studentEmail, "approved", "tutoring", studentEmail, today, String.valueOf(limit)});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.ScheduleSlot slot = new co.edu.aulasenora.models.ScheduleSlot(
                        cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10)
                );
                slot.setAulaName(cursor.getString(11));
                sessions.add(slot);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sessions;
    }

    public java.util.List<co.edu.aulasenora.models.ScheduleSlot> getTodayTutoringForStudent(String studentEmail) {
        java.util.List<co.edu.aulasenora.models.ScheduleSlot> sessions = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(new java.util.Date());
        String query = "SELECT s." + COLUMN_SLOT_ID + ", s." + COLUMN_SLOT_AULA_ID +
                ", s." + COLUMN_SLOT_VOLUNTEER_EMAIL + ", s." + COLUMN_SLOT_DATE +
                ", s." + COLUMN_SLOT_START_TIME + ", s." + COLUMN_SLOT_END_TIME +
                ", s." + COLUMN_SLOT_TYPE + ", s." + COLUMN_SLOT_TOPIC +
                ", s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + ", COALESCE(u." + COLUMN_NAME + ", '')" +
                ", s." + COLUMN_SLOT_CREATED_AT + ", a." + COLUMN_AULA_NAME +
                " FROM " + TABLE_SCHEDULE_SLOTS + " s" +
                " INNER JOIN " + TABLE_AULAS + " a ON s." + COLUMN_SLOT_AULA_ID + " = a." + COLUMN_AULA_ID +
                " INNER JOIN " + TABLE_ACCESS_REQUESTS + " ar ON ar." + COLUMN_REQUEST_AULA_ID + " = s." + COLUMN_SLOT_AULA_ID +
                " LEFT JOIN " + TABLE_USERS + " u ON s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE ar." + COLUMN_REQUEST_STUDENT_EMAIL + "=? AND ar." + COLUMN_REQUEST_STATUS + "=?" +
                " AND s." + COLUMN_SLOT_TYPE + "=? AND (s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + "=? OR s." + COLUMN_SLOT_TARGET_STUDENT_EMAIL + " IS NULL)" +
                " AND s." + COLUMN_SLOT_DATE + "=?" +
                " ORDER BY s." + COLUMN_SLOT_START_TIME + " ASC";
        Cursor cursor = db.rawQuery(query, new String[]{studentEmail, "approved", "tutoring", studentEmail, today});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.ScheduleSlot slot = new co.edu.aulasenora.models.ScheduleSlot(
                        cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8),
                        cursor.getString(9), cursor.getString(10)
                );
                slot.setAulaName(cursor.getString(11));
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

    // ===== PENDING TUTORING REQUEST FOR STUDENT =====

    public boolean hasPendingTutoringRequest(int aulaId, String studentEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_TUTORING_REQUESTS +
                " WHERE " + COLUMN_TUTORING_AULA_ID + "=? AND " + COLUMN_TUTORING_STUDENT_EMAIL + "=? AND " + COLUMN_TUTORING_STATUS + "=?",
                new String[]{String.valueOf(aulaId), studentEmail, "pending"});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public co.edu.aulasenora.models.TutoringRequest getPendingTutoringForStudent(int aulaId, String studentEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT tr." + COLUMN_TUTORING_ID + ", tr." + COLUMN_TUTORING_AULA_ID +
                ", tr." + COLUMN_TUTORING_STUDENT_EMAIL + ", u." + COLUMN_NAME +
                ", tr." + COLUMN_TUTORING_TOPIC + ", tr." + COLUMN_TUTORING_DESCRIPTION +
                ", tr." + COLUMN_TUTORING_PREFERRED_DATE + ", tr." + COLUMN_TUTORING_PREFERRED_TIME +
                ", tr." + COLUMN_TUTORING_PREFERRED_END_TIME + ", tr." + COLUMN_TUTORING_STATUS +
                ", tr." + COLUMN_TUTORING_CREATED_AT +
                " FROM " + TABLE_TUTORING_REQUESTS + " tr" +
                " INNER JOIN " + TABLE_USERS + " u ON tr." + COLUMN_TUTORING_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE tr." + COLUMN_TUTORING_AULA_ID + "=? AND tr." + COLUMN_TUTORING_STUDENT_EMAIL + "=? AND tr." + COLUMN_TUTORING_STATUS + "=?" +
                " ORDER BY tr." + COLUMN_TUTORING_CREATED_AT + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(aulaId), studentEmail, "pending"});
        co.edu.aulasenora.models.TutoringRequest req = null;
        if (cursor.moveToFirst()) {
            req = new co.edu.aulasenora.models.TutoringRequest(
                    cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7), cursor.getString(8),
                    cursor.getString(9), cursor.getString(10)
            );
        }
        cursor.close();
        return req;
    }

    // ===== SUPPORT MATERIALS =====

    public long createSupportMaterial(int aulaId, String volunteerEmail, String customName,
                                       String fileName, String filePath, String mimeType, long fileSize) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MATERIAL_AULA_ID, aulaId);
        values.put(COLUMN_MATERIAL_VOLUNTEER_EMAIL, volunteerEmail);
        values.put(COLUMN_MATERIAL_CUSTOM_NAME, customName);
        values.put(COLUMN_MATERIAL_FILE_NAME, fileName);
        values.put(COLUMN_MATERIAL_FILE_PATH, filePath);
        values.put(COLUMN_MATERIAL_MIME_TYPE, mimeType);
        values.put(COLUMN_MATERIAL_FILE_SIZE, fileSize);
        return db.insert(TABLE_SUPPORT_MATERIALS, null, values);
    }

    public java.util.List<co.edu.aulasenora.models.SupportMaterial> getRecentMaterialsByVolunteer(String volunteerEmail, int limit) {
        java.util.List<co.edu.aulasenora.models.SupportMaterial> materials = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT sm.*, COALESCE(dl.cnt, 0) FROM " + TABLE_SUPPORT_MATERIALS + " sm" +
                " INNER JOIN " + TABLE_AULAS + " a ON sm." + COLUMN_MATERIAL_AULA_ID + " = a." + COLUMN_AULA_ID +
                " LEFT JOIN (SELECT " + COLUMN_DOWNLOAD_MATERIAL_ID + ", COUNT(*) as cnt FROM " + TABLE_MATERIAL_DOWNLOADS +
                " GROUP BY " + COLUMN_DOWNLOAD_MATERIAL_ID + ") dl" +
                " ON sm." + COLUMN_MATERIAL_ID + " = dl." + COLUMN_DOWNLOAD_MATERIAL_ID +
                " WHERE a." + COLUMN_AULA_VOLUNTEER_EMAIL + "=?" +
                " ORDER BY sm." + COLUMN_MATERIAL_CREATED_AT + " DESC LIMIT ?";
        Cursor cursor = db.rawQuery(query, new String[]{volunteerEmail, String.valueOf(limit)});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.SupportMaterial m = new co.edu.aulasenora.models.SupportMaterial(
                        cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getLong(7), cursor.getString(8),
                        cursor.getInt(9)
                );
                materials.add(m);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return materials;
    }

    public java.util.List<co.edu.aulasenora.models.SupportMaterial> getSupportMaterials(int aulaId) {
        java.util.List<co.edu.aulasenora.models.SupportMaterial> materials = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT sm.*, COALESCE(dl.cnt, 0) FROM " + TABLE_SUPPORT_MATERIALS + " sm" +
                " LEFT JOIN (SELECT " + COLUMN_DOWNLOAD_MATERIAL_ID + ", COUNT(*) as cnt FROM " + TABLE_MATERIAL_DOWNLOADS +
                " GROUP BY " + COLUMN_DOWNLOAD_MATERIAL_ID + ") dl" +
                " ON sm." + COLUMN_MATERIAL_ID + " = dl." + COLUMN_DOWNLOAD_MATERIAL_ID +
                " WHERE sm." + COLUMN_MATERIAL_AULA_ID + "=?" +
                " ORDER BY sm." + COLUMN_MATERIAL_CREATED_AT + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(aulaId)});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.SupportMaterial m = new co.edu.aulasenora.models.SupportMaterial(
                        cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getLong(7), cursor.getString(8),
                        cursor.getInt(9)
                );
                materials.add(m);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return materials;
    }

    public void deleteSupportMaterial(int materialId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUPPORT_MATERIALS, COLUMN_MATERIAL_ID + "=?", new String[]{String.valueOf(materialId)});
        db.delete(TABLE_MATERIAL_DOWNLOADS, COLUMN_DOWNLOAD_MATERIAL_ID + "=?", new String[]{String.valueOf(materialId)});
    }

    public void recordMaterialView(int materialId, String studentEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DOWNLOAD_MATERIAL_ID, materialId);
        values.put(COLUMN_DOWNLOAD_STUDENT_EMAIL, studentEmail);
        db.insertWithOnConflict(TABLE_MATERIAL_DOWNLOADS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public int getMaterialViewCount(int materialId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_MATERIAL_DOWNLOADS +
                " WHERE " + COLUMN_DOWNLOAD_MATERIAL_ID + "=?",
                new String[]{String.valueOf(materialId)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public co.edu.aulasenora.models.SupportMaterial getSupportMaterialById(int materialId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT sm.*, COALESCE(dl.cnt, 0) FROM " + TABLE_SUPPORT_MATERIALS + " sm" +
                " LEFT JOIN (SELECT " + COLUMN_DOWNLOAD_MATERIAL_ID + ", COUNT(*) as cnt FROM " + TABLE_MATERIAL_DOWNLOADS +
                " GROUP BY " + COLUMN_DOWNLOAD_MATERIAL_ID + ") dl" +
                " ON sm." + COLUMN_MATERIAL_ID + " = dl." + COLUMN_DOWNLOAD_MATERIAL_ID +
                " WHERE sm." + COLUMN_MATERIAL_ID + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(materialId)});
        co.edu.aulasenora.models.SupportMaterial m = null;
        if (cursor.moveToFirst()) {
            m = new co.edu.aulasenora.models.SupportMaterial(
                    cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getLong(7), cursor.getString(8),
                    cursor.getInt(9)
            );
        }
        cursor.close();
        return m;
    }

    // ===== CHAT MESSAGES =====

    public long createChatMessage(int aulaId, String senderEmail, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHAT_AULA_ID, aulaId);
        values.put(COLUMN_CHAT_SENDER_EMAIL, senderEmail);
        values.put(COLUMN_CHAT_MESSAGE, message);
        return db.insert(TABLE_CHAT_MESSAGES, null, values);
    }

    public java.util.List<co.edu.aulasenora.models.ChatMessage> getChatMessages(int aulaId) {
        java.util.List<co.edu.aulasenora.models.ChatMessage> messages = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CHAT_MESSAGES +
                " WHERE " + COLUMN_CHAT_AULA_ID + "=? ORDER BY " + COLUMN_CHAT_CREATED_AT + " ASC",
                new String[]{String.valueOf(aulaId)});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.ChatMessage msg = new co.edu.aulasenora.models.ChatMessage(
                        cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4)
                );
                messages.add(msg);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return messages;
    }

    public int getUnreadCount(int aulaId, String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        int lastRead = 0;
        Cursor statusCursor = db.rawQuery("SELECT " + COLUMN_READ_LAST_MESSAGE_ID +
                " FROM " + TABLE_CHAT_READ_STATUS +
                " WHERE " + COLUMN_READ_AULA_ID + "=? AND " + COLUMN_READ_USER_EMAIL + "=?",
                new String[]{String.valueOf(aulaId), userEmail});
        if (statusCursor.moveToFirst()) {
            lastRead = statusCursor.getInt(0);
        }
        statusCursor.close();

        Cursor countCursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CHAT_MESSAGES +
                " WHERE " + COLUMN_CHAT_AULA_ID + "=? AND " + COLUMN_CHAT_ID + ">?",
                new String[]{String.valueOf(aulaId), String.valueOf(lastRead)});
        int count = 0;
        if (countCursor.moveToFirst()) {
            count = countCursor.getInt(0);
        }
        countCursor.close();
        return count;
    }

    public int getTotalUnreadCount(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COALESCE(SUM(cnt), 0) FROM (" +
                " SELECT COUNT(*) as cnt FROM " + TABLE_CHAT_MESSAGES + " m" +
                " LEFT JOIN " + TABLE_CHAT_READ_STATUS + " r" +
                " ON m." + COLUMN_CHAT_AULA_ID + " = r." + COLUMN_READ_AULA_ID +
                " AND r." + COLUMN_READ_USER_EMAIL + "=?" +
                " WHERE r." + COLUMN_READ_LAST_MESSAGE_ID + " IS NULL" +
                " OR m." + COLUMN_CHAT_ID + " > r." + COLUMN_READ_LAST_MESSAGE_ID +
                " GROUP BY m." + COLUMN_CHAT_AULA_ID +
                ")", new String[]{userEmail});
        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        return total;
    }

    public void markChatAsRead(int aulaId, String userEmail, int lastMessageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_READ_AULA_ID, aulaId);
        values.put(COLUMN_READ_USER_EMAIL, userEmail);
        values.put(COLUMN_READ_LAST_MESSAGE_ID, lastMessageId);
        int updated = db.update(TABLE_CHAT_READ_STATUS, values,
                COLUMN_READ_AULA_ID + "=? AND " + COLUMN_READ_USER_EMAIL + "=?",
                new String[]{String.valueOf(aulaId), userEmail});
        if (updated == 0) {
            db.insert(TABLE_CHAT_READ_STATUS, null, values);
        }
    }

    public java.util.List<co.edu.aulasenora.models.Aula> getChatAulasForVolunteer(String volunteerEmail) {
        java.util.List<co.edu.aulasenora.models.Aula> aulasList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_AULAS +
                " WHERE " + COLUMN_AULA_VOLUNTEER_EMAIL + "=?" +
                " ORDER BY " + COLUMN_AULA_CREATED_AT + " DESC",
                new String[]{volunteerEmail});
        if (cursor.moveToFirst()) {
            do {
                co.edu.aulasenora.models.Aula aula = new co.edu.aulasenora.models.Aula(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5)
                );
                aulasList.add(aula);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return aulasList;
    }

    public java.util.List<co.edu.aulasenora.models.NotificationItem> getPendingAccessNotifications(String volunteerEmail) {
        java.util.List<co.edu.aulasenora.models.NotificationItem> items = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT ar." + COLUMN_REQUEST_ID + ", a." + COLUMN_AULA_ID + ", a." + COLUMN_AULA_NAME +
                ", u." + COLUMN_NAME + ", ar." + COLUMN_REQUEST_STUDENT_EMAIL + ", ar." + COLUMN_REQUEST_CREATED_AT +
                " FROM " + TABLE_ACCESS_REQUESTS + " ar" +
                " INNER JOIN " + TABLE_AULAS + " a ON ar." + COLUMN_REQUEST_AULA_ID + " = a." + COLUMN_AULA_ID +
                " INNER JOIN " + TABLE_USERS + " u ON ar." + COLUMN_REQUEST_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE a." + COLUMN_AULA_VOLUNTEER_EMAIL + "=? AND ar." + COLUMN_REQUEST_STATUS + "=?" +
                " ORDER BY ar." + COLUMN_REQUEST_CREATED_AT + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{volunteerEmail, "pending"});
        if (cursor.moveToFirst()) {
            do {
                items.add(new co.edu.aulasenora.models.NotificationItem(
                        "access",
                        "Solicitud de acceso",
                        cursor.getString(3) + " quiere unirse a " + cursor.getString(2),
                        cursor.getString(5),
                        cursor.getInt(1),
                        cursor.getInt(0),
                        cursor.getString(4),
                        0
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public java.util.List<co.edu.aulasenora.models.NotificationItem> getPendingTutoringNotifications(String volunteerEmail) {
        java.util.List<co.edu.aulasenora.models.NotificationItem> items = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT tr." + COLUMN_TUTORING_ID + ", a." + COLUMN_AULA_ID + ", a." + COLUMN_AULA_NAME +
                ", u." + COLUMN_NAME + ", tr." + COLUMN_TUTORING_TOPIC + ", tr." + COLUMN_TUTORING_STUDENT_EMAIL +
                ", tr." + COLUMN_TUTORING_CREATED_AT +
                " FROM " + TABLE_TUTORING_REQUESTS + " tr" +
                " INNER JOIN " + TABLE_AULAS + " a ON tr." + COLUMN_TUTORING_AULA_ID + " = a." + COLUMN_AULA_ID +
                " INNER JOIN " + TABLE_USERS + " u ON tr." + COLUMN_TUTORING_STUDENT_EMAIL + " = u." + COLUMN_EMAIL +
                " WHERE a." + COLUMN_AULA_VOLUNTEER_EMAIL + "=? AND tr." + COLUMN_TUTORING_STATUS + "=?" +
                " ORDER BY tr." + COLUMN_TUTORING_CREATED_AT + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{volunteerEmail, "pending"});
        if (cursor.moveToFirst()) {
            do {
                items.add(new co.edu.aulasenora.models.NotificationItem(
                        "tutoring",
                        "Solicitud de tutoría",
                        cursor.getString(3) + " pide ayuda sobre " + cursor.getString(4),
                        cursor.getString(6),
                        cursor.getInt(1),
                        cursor.getInt(0),
                        cursor.getString(5),
                        0
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public java.util.List<co.edu.aulasenora.models.NotificationItem> getUnreadChatNotifications(String volunteerEmail) {
        java.util.List<co.edu.aulasenora.models.NotificationItem> items = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a." + COLUMN_AULA_ID + ", a." + COLUMN_AULA_NAME +
                ", COALESCE(r." + COLUMN_READ_LAST_MESSAGE_ID + ", 0) as last_read" +
                " FROM " + TABLE_AULAS + " a" +
                " LEFT JOIN " + TABLE_CHAT_READ_STATUS + " r ON a." + COLUMN_AULA_ID + " = r." + COLUMN_READ_AULA_ID +
                " AND r." + COLUMN_READ_USER_EMAIL + "=?" +
                " WHERE a." + COLUMN_AULA_VOLUNTEER_EMAIL + "=?" +
                " ORDER BY a." + COLUMN_AULA_CREATED_AT + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{volunteerEmail, volunteerEmail});
        if (cursor.moveToFirst()) {
            do {
                int aulaId = cursor.getInt(0);
                String aulaName = cursor.getString(1);
                int lastRead = cursor.getInt(2);

                Cursor msgCursor = db.rawQuery("SELECT COUNT(*), MAX(" + COLUMN_CHAT_CREATED_AT + ")" +
                        " FROM " + TABLE_CHAT_MESSAGES +
                        " WHERE " + COLUMN_CHAT_AULA_ID + "=? AND " + COLUMN_CHAT_ID + ">?",
                        new String[]{String.valueOf(aulaId), String.valueOf(lastRead)});
                if (msgCursor.moveToFirst()) {
                    int count = msgCursor.getInt(0);
                    String lastTime = msgCursor.getString(1);
                    if (count > 0) {
                        items.add(new co.edu.aulasenora.models.NotificationItem(
                                "chat",
                                "Nuevo mensaje en " + aulaName,
                                count + " mensaje" + (count == 1 ? "" : "s") + " nuevo" + (count == 1 ? "" : "s"),
                                lastTime != null ? lastTime : "",
                                aulaId,
                                0,
                                null,
                                count
                        ));
                    }
                }
                msgCursor.close();
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public java.util.List<co.edu.aulasenora.models.Aula> getChatAulasForStudent(String studentEmail) {
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
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5)
                );
                aulasList.add(aula);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return aulasList;
    }
}
